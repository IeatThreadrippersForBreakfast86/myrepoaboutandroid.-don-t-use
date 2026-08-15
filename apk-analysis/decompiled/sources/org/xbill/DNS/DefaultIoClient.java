package org.xbill.DNS;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.xbill.DNS.p004io.TcpIoClient;
import org.xbill.DNS.p004io.UdpIoClient;

/* loaded from: classes8.dex */
public class DefaultIoClient implements TcpIoClient, UdpIoClient {
    private final TcpIoClient tcpIoClient = new NioTcpClient();
    private final UdpIoClient udpIoClient = new NioUdpClient();

    @Override // org.xbill.DNS.p004io.TcpIoClient
    public CompletableFuture<byte[]> sendAndReceiveTcp(InetSocketAddress local, InetSocketAddress remote, Message query, byte[] data, Duration timeout) {
        return this.tcpIoClient.sendAndReceiveTcp(local, remote, query, data, timeout);
    }

    @Override // org.xbill.DNS.p004io.UdpIoClient
    public CompletableFuture<byte[]> sendAndReceiveUdp(InetSocketAddress local, InetSocketAddress remote, Message query, byte[] data, int max, Duration timeout) {
        return this.udpIoClient.sendAndReceiveUdp(local, remote, query, data, max, timeout);
    }
}
