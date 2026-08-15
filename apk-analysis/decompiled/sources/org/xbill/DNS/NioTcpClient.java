package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import kotlin.UByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.NioClient;
import org.xbill.DNS.NioTcpClient;
import org.xbill.DNS.p004io.TcpIoClient;

/* loaded from: classes8.dex */
final class NioTcpClient extends NioClient implements TcpIoClient {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) NioTcpClient.class);
    private final Queue<ChannelState> registrationQueue = new ConcurrentLinkedQueue();
    private final Map<ChannelKey, ChannelState> channelMap = new ConcurrentHashMap();

    NioTcpClient() {
        setRegistrationsTask(new Consumer() { // from class: org.xbill.DNS.NioTcpClient$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.processPendingRegistrations((Selector) obj);
            }
        }, true);
        setTimeoutTask(new Runnable() { // from class: org.xbill.DNS.NioTcpClient$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.checkTransactionTimeouts();
            }
        }, true);
        setCloseTask(new Runnable() { // from class: org.xbill.DNS.NioTcpClient$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.closeTcp();
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPendingRegistrations(Selector selector) {
        while (!this.registrationQueue.isEmpty()) {
            ChannelState state = this.registrationQueue.poll();
            if (state != null) {
                try {
                    if (!state.channel.isConnected()) {
                        state.channel.register(selector, 8, state);
                    } else {
                        state.channel.keyFor(selector).interestOps(4);
                    }
                } catch (IOException e) {
                    state.handleChannelException(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkTransactionTimeouts() {
        for (ChannelState state : this.channelMap.values()) {
            Iterator<Transaction> it = state.pendingTransactions.iterator();
            while (it.hasNext()) {
                Transaction t = it.next();
                if (t.endTime - System.nanoTime() < 0) {
                    t.f261f.completeExceptionally(new SocketTimeoutException("Query timed out"));
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeTcp() {
        this.registrationQueue.clear();
        EOFException closing = new EOFException("Client is closing");
        for (ChannelState state : this.channelMap.values()) {
            state.handleTransactionException(closing);
            state.handleChannelException(closing);
        }
        this.channelMap.clear();
    }

    private static final class Transaction {
        long bytesWrittenTotal = 0;
        private final SocketChannel channel;
        private final long endTime;

        /* renamed from: f */
        private final CompletableFuture<byte[]> f261f;
        private final Message query;
        private final byte[] queryData;
        private ByteBuffer queryDataBuffer;

        public Transaction(Message query, byte[] queryData, long endTime, SocketChannel channel, CompletableFuture<byte[]> f) {
            this.query = query;
            this.queryData = queryData;
            this.endTime = endTime;
            this.channel = channel;
            this.f261f = f;
        }

        boolean send() throws IOException {
            if (this.bytesWrittenTotal == this.queryData.length + 2) {
                return true;
            }
            if (this.queryDataBuffer == null) {
                this.queryDataBuffer = ByteBuffer.allocate(this.queryData.length + 2);
                this.queryDataBuffer.put((byte) (this.queryData.length >>> 8));
                this.queryDataBuffer.put((byte) (this.queryData.length & 255));
                this.queryDataBuffer.put(this.queryData);
                this.queryDataBuffer.flip();
            }
            NioClient.verboseLog("TCP write: transaction id=" + this.query.getHeader().getID(), this.channel.socket().getLocalSocketAddress(), this.channel.socket().getRemoteSocketAddress(), this.queryDataBuffer);
            while (this.queryDataBuffer.hasRemaining()) {
                long bytesWritten = this.channel.write(this.queryDataBuffer);
                this.bytesWrittenTotal += bytesWritten;
                if (bytesWritten == 0) {
                    NioTcpClient.log.debug("Insufficient room for the data in the underlying output buffer for transaction {}, retrying", Integer.valueOf(this.query.getHeader().getID()));
                    return false;
                }
                if (this.bytesWrittenTotal < this.queryData.length) {
                    NioTcpClient.log.debug("Wrote {} of {} bytes data for transaction {}", Long.valueOf(this.bytesWrittenTotal), Integer.valueOf(this.queryData.length), Integer.valueOf(this.query.getHeader().getID()));
                }
            }
            NioTcpClient.log.debug("Send for transaction {} is complete, wrote {} bytes", Integer.valueOf(this.query.getHeader().getID()), Long.valueOf(this.bytesWrittenTotal));
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ChannelState implements NioClient.KeyProcessor {
        private final SocketChannel channel;
        final Queue<Transaction> pendingTransactions = new ConcurrentLinkedQueue();
        ByteBuffer responseLengthData = ByteBuffer.allocate(2);
        ByteBuffer responseData = ByteBuffer.allocate(65535);
        int readState = 0;

        public ChannelState(SocketChannel channel) {
            this.channel = channel;
        }

        @Override // org.xbill.DNS.NioClient.KeyProcessor
        public void processReadyKey(SelectionKey key) throws IOException {
            if (key.isValid()) {
                if (key.isConnectable()) {
                    processConnect(key);
                    return;
                }
                if (key.isWritable()) {
                    processWrite(key);
                }
                if (key.isReadable()) {
                    processRead(key);
                    return;
                }
                return;
            }
            handleTransactionException(new EOFException("Invalid key"));
        }

        void handleTransactionException(IOException e) {
            Iterator<Transaction> it = this.pendingTransactions.iterator();
            while (it.hasNext()) {
                Transaction t = it.next();
                t.f261f.completeExceptionally(e);
                it.remove();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleChannelException(IOException e) {
            handleTransactionException(e);
            for (Map.Entry<ChannelKey, ChannelState> entry : NioTcpClient.this.channelMap.entrySet()) {
                if (entry.getValue() == this) {
                    NioTcpClient.this.channelMap.remove(entry.getKey());
                    try {
                        this.channel.close();
                        return;
                    } catch (IOException ex) {
                        NioTcpClient.log.warn("Failed to close channel l={}/r={}", entry.getKey().local, entry.getKey().remote, ex);
                        return;
                    }
                }
            }
        }

        private void processConnect(SelectionKey key) throws IOException {
            try {
                this.channel.finishConnect();
                key.interestOps(4);
            } catch (IOException e) {
                handleChannelException(e);
                key.cancel();
            }
        }

        private void processRead(SelectionKey key) throws IOException {
            try {
                if (this.readState == 0) {
                    int read = this.channel.read(this.responseLengthData);
                    if (read < 0) {
                        handleChannelException(new EOFException());
                        key.cancel();
                        return;
                    } else if (this.responseLengthData.position() == 2) {
                        int length = ((this.responseLengthData.get(0) & UByte.MAX_VALUE) << 8) + (this.responseLengthData.get(1) & UByte.MAX_VALUE);
                        this.responseLengthData.flip();
                        this.responseData.limit(length);
                        this.readState = 1;
                    }
                }
                int read2 = this.channel.read(this.responseData);
                if (read2 < 0) {
                    handleChannelException(new EOFException());
                    key.cancel();
                    return;
                }
                if (this.responseData.hasRemaining()) {
                    return;
                }
                this.readState = 0;
                this.responseData.flip();
                byte[] data = new byte[this.responseData.limit()];
                System.arraycopy(this.responseData.array(), this.responseData.arrayOffset(), data, 0, this.responseData.limit());
                if (data.length < 2) {
                    NioClient.verboseLog("TCP read: response too short for a valid reply, discarding", this.channel.socket().getLocalSocketAddress(), this.channel.socket().getRemoteSocketAddress(), data);
                    return;
                }
                int id = ((data[0] & UByte.MAX_VALUE) << 8) + (data[1] & UByte.MAX_VALUE);
                NioClient.verboseLog("TCP read: transaction id=" + id, this.channel.socket().getLocalSocketAddress(), this.channel.socket().getRemoteSocketAddress(), data);
                Iterator<Transaction> it = this.pendingTransactions.iterator();
                while (it.hasNext()) {
                    Transaction t = it.next();
                    int qid = t.query.getHeader().getID();
                    if (id == qid) {
                        t.f261f.complete(data);
                        it.remove();
                        return;
                    }
                }
                NioTcpClient.log.warn("Transaction for answer to id {} not found", Integer.valueOf(id));
            } catch (IOException e) {
                handleChannelException(e);
                key.cancel();
            }
        }

        private void processWrite(SelectionKey key) {
            Iterator<Transaction> it = this.pendingTransactions.iterator();
            while (it.hasNext()) {
                Transaction t = it.next();
                try {
                } catch (IOException e) {
                    t.f261f.completeExceptionally(e);
                    it.remove();
                    key.cancel();
                }
                if (!t.send()) {
                    key.interestOps(4);
                    return;
                }
                continue;
            }
            key.interestOps(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ChannelKey {
        final InetSocketAddress local;
        final InetSocketAddress remote;

        public ChannelKey(InetSocketAddress local, InetSocketAddress remote) {
            this.local = local;
            this.remote = remote;
        }

        protected boolean canEqual(Object other) {
            return other instanceof ChannelKey;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ChannelKey)) {
                return false;
            }
            ChannelKey other = (ChannelKey) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$local = this.local;
            Object other$local = other.local;
            if (this$local != null ? !this$local.equals(other$local) : other$local != null) {
                return false;
            }
            Object this$remote = this.remote;
            Object other$remote = other.remote;
            return this$remote != null ? this$remote.equals(other$remote) : other$remote == null;
        }

        public int hashCode() {
            Object $local = this.local;
            int result = (1 * 59) + ($local == null ? 43 : $local.hashCode());
            Object $remote = this.remote;
            return (result * 59) + ($remote != null ? $remote.hashCode() : 43);
        }
    }

    @Override // org.xbill.DNS.p004io.TcpIoClient
    public CompletableFuture<byte[]> sendAndReceiveTcp(final InetSocketAddress local, final InetSocketAddress remote, Message query, byte[] data, Duration timeout) {
        final CompletableFuture<byte[]> f = new CompletableFuture<>();
        try {
            Selector selector = selector();
            long endTime = System.nanoTime() + timeout.toNanos();
            ChannelState channel = this.channelMap.computeIfAbsent(new ChannelKey(local, remote), new Function() { // from class: org.xbill.DNS.NioTcpClient$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.m1836lambda$sendAndReceiveTcp$0$orgxbillDNSNioTcpClient(local, remote, f, (NioTcpClient.ChannelKey) obj);
                }
            });
            if (channel != null) {
                log.trace("Creating transaction for id {} ({}/{})", Integer.valueOf(query.getHeader().getID()), query.getQuestion().getName(), Type.string(query.getQuestion().getType()));
                Transaction t = new Transaction(query, data, endTime, channel.channel, f);
                channel.pendingTransactions.add(t);
                this.registrationQueue.add(channel);
                selector.wakeup();
            }
        } catch (IOException e) {
            f.completeExceptionally(e);
        }
        return f;
    }

    /* renamed from: lambda$sendAndReceiveTcp$0$org-xbill-DNS-NioTcpClient, reason: not valid java name */
    /* synthetic */ ChannelState m1836lambda$sendAndReceiveTcp$0$orgxbillDNSNioTcpClient(InetSocketAddress local, InetSocketAddress remote, CompletableFuture f, ChannelKey key) throws IOException {
        log.debug("Opening async channel for l={}/r={}", local, remote);
        SocketChannel c = null;
        try {
            c = SocketChannel.open();
            c.configureBlocking(false);
            if (local != null) {
                c.bind((SocketAddress) local);
            }
            c.connect(remote);
            return new ChannelState(c);
        } catch (IOException e) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e2) {
                }
            }
            f.completeExceptionally(e);
            return null;
        }
    }
}
