package org.xbill.DNS.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

/* loaded from: classes8.dex */
public class SunJvmResolverConfigProvider extends BaseResolverConfigProvider {
    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() throws IllegalAccessException, InitializationException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        reset();
        try {
            Class<?> resConfClass = Class.forName("sun.net.dns.ResolverConfiguration");
            Method open = resConfClass.getDeclaredMethod("open", new Class[0]);
            Object resConf = open.invoke(null, new Object[0]);
            Method nameserversMethod = resConfClass.getMethod("nameservers", new Class[0]);
            List<String> jvmNameservers = (List) nameserversMethod.invoke(resConf, new Object[0]);
            for (String ns : jvmNameservers) {
                addNameserver(new InetSocketAddress(ns, 53));
            }
            Method searchlistMethod = resConfClass.getMethod("searchlist", new Class[0]);
            List<String> jvmSearchlist = (List) searchlistMethod.invoke(resConf, new Object[0]);
            for (String n : jvmSearchlist) {
                addSearchPath(n);
            }
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public boolean isEnabled() {
        return Boolean.getBoolean("dnsjava.configprovider.sunjvm.enabled");
    }
}
