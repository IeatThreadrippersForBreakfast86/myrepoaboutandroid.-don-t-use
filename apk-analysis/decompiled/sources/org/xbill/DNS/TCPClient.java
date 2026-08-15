package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import kotlin.UByte;

/* loaded from: classes8.dex */
class TCPClient implements AutoCloseable {
    private final SelectionKey key;
    private final long startTime = System.nanoTime();
    private final Duration timeout;

    TCPClient(Duration timeout) throws IOException {
        this.timeout = timeout;
        boolean done = false;
        Selector selector = null;
        SocketChannel channel = SocketChannel.open();
        try {
            selector = Selector.open();
            channel.configureBlocking(false);
            this.key = channel.register(selector, 1);
            done = true;
        } finally {
            if (!done && selector != null) {
                selector.close();
            }
            if (!done) {
                channel.close();
            }
        }
    }

    void bind(SocketAddress addr) throws IOException {
        SocketChannel channel = (SocketChannel) this.key.channel();
        channel.socket().bind(addr);
    }

    void connect(SocketAddress addr) throws IOException {
        SocketChannel channel = (SocketChannel) this.key.channel();
        if (channel.connect(addr)) {
            return;
        }
        this.key.interestOps(8);
        while (true) {
            try {
                if (channel.finishConnect()) {
                    break;
                } else if (!this.key.isConnectable()) {
                    blockUntil(this.key);
                }
            } finally {
                if (this.key.isValid()) {
                    this.key.interestOps(0);
                }
            }
        }
    }

    void send(byte[] data) throws IOException {
        SocketChannel channel = (SocketChannel) this.key.channel();
        NioClient.verboseLog("TCP write", channel.socket().getLocalSocketAddress(), channel.socket().getRemoteSocketAddress(), data);
        byte[] lengthArray = {(byte) (data.length >>> 8), (byte) (data.length & 255)};
        ByteBuffer[] buffers = {ByteBuffer.wrap(lengthArray), ByteBuffer.wrap(data)};
        int nsent = 0;
        this.key.interestOps(4);
        while (nsent < data.length + 2) {
            try {
                if (this.key.isWritable()) {
                    long n = channel.write(buffers);
                    if (n < 0) {
                        throw new EOFException();
                    }
                    nsent += (int) n;
                    if (nsent < data.length + 2 && System.nanoTime() - this.startTime >= this.timeout.toNanos()) {
                        throw new SocketTimeoutException();
                    }
                } else {
                    blockUntil(this.key);
                }
            } finally {
                if (this.key.isValid()) {
                    this.key.interestOps(0);
                }
            }
        }
    }

    private byte[] recv(int length) throws IOException {
        SocketChannel channel = (SocketChannel) this.key.channel();
        int nrecvd = 0;
        byte[] data = new byte[length];
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.key.interestOps(1);
        while (true) {
            if (nrecvd < length) {
                try {
                    if (this.key.isReadable()) {
                        long n = channel.read(buffer);
                        if (n < 0) {
                            throw new EOFException();
                        }
                        nrecvd += (int) n;
                        if (nrecvd < length && System.nanoTime() - this.startTime >= this.timeout.toNanos()) {
                            throw new SocketTimeoutException();
                        }
                    } else {
                        blockUntil(this.key);
                    }
                } finally {
                    if (this.key.isValid()) {
                        this.key.interestOps(0);
                    }
                }
            } else {
                return data;
            }
        }
    }

    private void blockUntil(SelectionKey key) throws IOException {
        long remainingTimeout = this.timeout.minus(System.nanoTime() - this.startTime, ChronoUnit.NANOS).toMillis();
        int nkeys = 0;
        if (remainingTimeout > 0) {
            nkeys = key.selector().select(remainingTimeout);
        } else if (remainingTimeout == 0) {
            nkeys = key.selector().selectNow();
        }
        if (nkeys == 0) {
            throw new SocketTimeoutException();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws IOException {
        this.key.selector().close();
        this.key.channel().close();
    }

    byte[] recv() throws IOException {
        byte[] buf = recv(2);
        int length = ((buf[0] & UByte.MAX_VALUE) << 8) + (buf[1] & UByte.MAX_VALUE);
        byte[] data = recv(length);
        SocketChannel channel = (SocketChannel) this.key.channel();
        NioClient.verboseLog("TCP read", channel.socket().getLocalSocketAddress(), channel.socket().getRemoteSocketAddress(), data);
        return data;
    }
}
