package org.xbill.DNS.p004io;

/* loaded from: classes8.dex */
public interface IoClientFactory {
    TcpIoClient createOrGetTcpClient();

    UdpIoClient createOrGetUdpClient();
}
