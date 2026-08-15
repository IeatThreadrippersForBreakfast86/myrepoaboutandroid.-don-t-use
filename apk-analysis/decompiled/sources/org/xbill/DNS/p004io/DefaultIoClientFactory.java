package org.xbill.DNS.p004io;

import org.xbill.DNS.DefaultIoClient;

/* loaded from: classes8.dex */
public class DefaultIoClientFactory implements IoClientFactory {
    private static final DefaultIoClient RESOLVER_CLIENT = new DefaultIoClient();

    @Override // org.xbill.DNS.p004io.IoClientFactory
    public TcpIoClient createOrGetTcpClient() {
        return RESOLVER_CLIENT;
    }

    @Override // org.xbill.DNS.p004io.IoClientFactory
    public UdpIoClient createOrGetUdpClient() {
        return RESOLVER_CLIENT;
    }
}
