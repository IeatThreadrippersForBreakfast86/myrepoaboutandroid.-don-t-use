package org.xbill.DNS.config;

import java.net.InetSocketAddress;
import java.util.List;
import org.xbill.DNS.Name;

/* loaded from: classes8.dex */
public interface ResolverConfigProvider {
    void initialize() throws InitializationException;

    List<Name> searchPaths();

    List<InetSocketAddress> servers();

    default int ndots() {
        return 1;
    }

    default boolean isEnabled() {
        return true;
    }
}
