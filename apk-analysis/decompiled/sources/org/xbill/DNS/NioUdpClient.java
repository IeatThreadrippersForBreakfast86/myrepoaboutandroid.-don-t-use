package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.NioClient;
import org.xbill.DNS.NioUdpClient;
import org.xbill.DNS.p004io.UdpIoClient;

/* loaded from: classes8.dex */
final class NioUdpClient extends NioClient implements UdpIoClient {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) NioUdpClient.class);
    private final int ephemeralRange;
    private final int ephemeralStart;
    private final SecureRandom prng;
    private final Queue<Transaction> registrationQueue = new ConcurrentLinkedQueue();
    private final Queue<Transaction> pendingTransactions = new ConcurrentLinkedQueue();

    NioUdpClient() {
        int ephemeralStartDefault = 49152;
        int ephemeralEndDefault = 65535;
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            ephemeralStartDefault = 32768;
            ephemeralEndDefault = 60999;
        }
        this.ephemeralStart = Integer.getInteger("dnsjava.udp.ephemeral.start", ephemeralStartDefault).intValue();
        int ephemeralEnd = Integer.getInteger("dnsjava.udp.ephemeral.end", ephemeralEndDefault).intValue();
        this.ephemeralRange = ephemeralEnd - this.ephemeralStart;
        if (Boolean.getBoolean("dnsjava.udp.ephemeral.use_ephemeral_port")) {
            this.prng = null;
        } else {
            this.prng = new SecureRandom();
        }
        setRegistrationsTask(new Consumer() { // from class: org.xbill.DNS.NioUdpClient$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.processPendingRegistrations((Selector) obj);
            }
        }, false);
        setTimeoutTask(new Runnable() { // from class: org.xbill.DNS.NioUdpClient$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.checkTransactionTimeouts();
            }
        }, false);
        setCloseTask(new Runnable() { // from class: org.xbill.DNS.NioUdpClient$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.closeUdp();
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPendingRegistrations(Selector selector) {
        while (!this.registrationQueue.isEmpty()) {
            Transaction t = this.registrationQueue.poll();
            if (t != null) {
                try {
                    log.trace("Registering OP_READ for transaction with id {}", Integer.valueOf(t.f263id));
                    t.channel.register(selector, 1, t);
                    t.send();
                } catch (IOException e) {
                    t.completeExceptionally(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkTransactionTimeouts() {
        Iterator<Transaction> it = this.pendingTransactions.iterator();
        while (it.hasNext()) {
            Transaction t = it.next();
            if (t.endTime - System.nanoTime() < 0) {
                t.completeExceptionally(new SocketTimeoutException("Query timed out"));
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class Transaction implements NioClient.KeyProcessor {
        private final DatagramChannel channel;
        private final byte[] data;
        private final long endTime;

        /* renamed from: f */
        private final CompletableFuture<byte[]> f262f;

        /* renamed from: id */
        private final int f263id;
        private final int max;

        public Transaction(int id, byte[] data, int max, long endTime, DatagramChannel channel, CompletableFuture<byte[]> f) {
            this.f263id = id;
            this.data = data;
            this.max = max;
            this.endTime = endTime;
            this.channel = channel;
            this.f262f = f;
        }

        void send() throws IOException {
            ByteBuffer buffer = ByteBuffer.wrap(this.data);
            NioClient.verboseLog("UDP write: transaction id=" + this.f263id, this.channel.socket().getLocalSocketAddress(), this.channel.socket().getRemoteSocketAddress(), this.data);
            int n = this.channel.send(buffer, this.channel.socket().getRemoteSocketAddress());
            if (n == 0) {
                throw new EOFException("Insufficient room for the datagram in the underlying output buffer for transaction " + this.f263id);
            }
            if (n < this.data.length) {
                throw new EOFException("Could not send all data for transaction " + this.f263id);
            }
        }

        @Override // org.xbill.DNS.NioClient.KeyProcessor
        public void processReadyKey(SelectionKey key) throws IOException {
            if (!key.isValid()) {
                completeExceptionally(new EOFException("Key for transaction " + this.f263id + " is invalid"));
                NioUdpClient.this.pendingTransactions.remove(this);
                return;
            }
            if (!key.isReadable()) {
                completeExceptionally(new EOFException("Key for transaction " + this.f263id + " is not readable"));
                NioUdpClient.this.pendingTransactions.remove(this);
                key.cancel();
                return;
            }
            DatagramChannel keyChannel = (DatagramChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(this.max);
            try {
                int read = keyChannel.read(buffer);
                if (read <= 0) {
                    throw new EOFException("Could not read expected data for transaction " + this.f263id);
                }
                buffer.flip();
                byte[] resultingData = new byte[read];
                System.arraycopy(buffer.array(), 0, resultingData, 0, read);
                NioClient.verboseLog("UDP read: transaction id=" + this.f263id, keyChannel.socket().getLocalSocketAddress(), keyChannel.socket().getRemoteSocketAddress(), resultingData);
                key.cancel();
                silentDisconnectAndCloseChannel();
                this.f262f.complete(resultingData);
                NioUdpClient.this.pendingTransactions.remove(this);
            } catch (IOException | NotYetConnectedException e) {
                completeExceptionally(e);
                NioUdpClient.this.pendingTransactions.remove(this);
                key.cancel();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void completeExceptionally(Exception e) {
            silentDisconnectAndCloseChannel();
            this.f262f.completeExceptionally(e);
        }

        private void silentDisconnectAndCloseChannel() {
            try {
                this.channel.disconnect();
            } catch (IOException e) {
            } catch (Throwable th) {
                NioUdpClient.silentCloseChannel(this.channel);
                throw th;
            }
            NioUdpClient.silentCloseChannel(this.channel);
        }
    }

    @Override // org.xbill.DNS.p004io.UdpIoClient
    public CompletableFuture<byte[]> sendAndReceiveUdp(InetSocketAddress local, InetSocketAddress remote, Message query, byte[] data, int max, Duration timeout) throws Throwable {
        long endTime = System.nanoTime() + timeout.toNanos();
        CompletableFuture<byte[]> f = new CompletableFuture<>();
        DatagramChannel channel = null;
        try {
            Selector selector = selector();
            DatagramChannel channel2 = DatagramChannel.open();
            try {
                channel2.configureBlocking(false);
                Transaction t = new Transaction(query.getHeader().getID(), data, max, endTime, channel2, f);
                if (local == null || local.getPort() == 0) {
                    boolean bound = false;
                    for (int i = 0; i < 1024 && !bound; i++) {
                        bound = tryBindToSocket(local, channel2);
                    }
                    if (!bound) {
                        t.completeExceptionally(new IOException("No available source port found"));
                        return f;
                    }
                }
                try {
                    channel2.connect(remote);
                    this.pendingTransactions.add(t);
                    this.registrationQueue.add(t);
                    selector.wakeup();
                } catch (IOException e) {
                    e = e;
                    channel = channel2;
                    silentCloseChannel(channel);
                    f.completeExceptionally(e);
                    return f;
                } catch (Throwable th) {
                    e = th;
                    channel = channel2;
                    silentCloseChannel(channel);
                    throw e;
                }
            } catch (IOException e2) {
                e = e2;
            } catch (Throwable th2) {
                e = th2;
            }
        } catch (IOException e3) {
            e = e3;
        } catch (Throwable th3) {
            e = th3;
        }
        return f;
    }

    private boolean tryBindToSocket(InetSocketAddress local, DatagramChannel channel) throws IOException {
        InetSocketAddress address = null;
        try {
            if (local == null) {
                if (this.prng != null) {
                    address = new InetSocketAddress(this.prng.nextInt(this.ephemeralRange) + this.ephemeralStart);
                }
            } else {
                int port = local.getPort();
                if (port == 0 && this.prng != null) {
                    port = this.prng.nextInt(this.ephemeralRange) + this.ephemeralStart;
                }
                address = new InetSocketAddress(local.getAddress(), port);
            }
            channel.bind((SocketAddress) address);
            return true;
        } catch (SocketException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void silentCloseChannel(DatagramChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeUdp() {
        this.registrationQueue.clear();
        final EOFException closing = new EOFException("Client is closing");
        this.pendingTransactions.forEach(new Consumer() { // from class: org.xbill.DNS.NioUdpClient$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((NioUdpClient.Transaction) obj).completeExceptionally(closing);
            }
        });
        this.pendingTransactions.clear();
    }
}
