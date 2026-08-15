package org.xbill.DNS.spi;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/* loaded from: classes8.dex */
public class DNSJavaNameServiceDescriptor implements NameServiceDescriptor {
    public NameService createNameService() {
        return new DNSJavaNameService();
    }

    public String getType() {
        return "dns";
    }

    public String getProviderName() {
        return "dnsjava";
    }
}
