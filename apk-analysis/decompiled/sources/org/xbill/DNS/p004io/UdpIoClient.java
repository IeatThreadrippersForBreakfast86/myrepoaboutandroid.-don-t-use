package org.xbill.DNS.p004io;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.xbill.DNS.Message;

/* loaded from: classes8.dex */
public interface UdpIoClient {
    CompletableFuture<byte[]> sendAndReceiveUdp(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, Message message, byte[] bArr, int i, Duration duration);
}
