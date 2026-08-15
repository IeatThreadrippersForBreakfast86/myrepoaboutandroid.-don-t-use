package org.xbill.DNS.config;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

/* loaded from: classes8.dex */
public abstract class BaseResolverConfigProvider implements ResolverConfigProvider {
    protected static final int DEFAULT_PORT = 53;
    private static final boolean IPV4_ONLY = Boolean.getBoolean("java.net.preferIPv4Stack");
    private static final boolean IPV6_FIRST = Boolean.getBoolean("java.net.preferIPv6Addresses");
    private final List<InetSocketAddress> nameservers = new ArrayList(3);
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final List<Name> searchlist = new ArrayList(1);

    protected final void reset() {
        this.nameservers.clear();
        this.searchlist.clear();
    }

    protected void parseSearchPathList(String search, String delimiter) {
        if (search != null) {
            StringTokenizer st = new StringTokenizer(search, delimiter);
            while (st.hasMoreTokens()) {
                addSearchPath(st.nextToken());
            }
        }
    }

    protected void addSearchPath(String searchPath) {
        if (searchPath == null || searchPath.isEmpty()) {
            return;
        }
        try {
            Name n = Name.fromString(searchPath, Name.root);
            if (!this.searchlist.contains(n)) {
                this.searchlist.add(n);
                this.log.debug("Added {} to search paths", n);
            }
        } catch (TextParseException e) {
            this.log.warn("Could not parse search path {} as a dns name, ignoring", searchPath);
        }
    }

    protected void addNameserver(InetSocketAddress server) {
        if (!this.nameservers.contains(server)) {
            this.nameservers.add(server);
            this.log.debug("Added {} to nameservers", server);
        }
    }

    protected int parseNdots(String token) throws NumberFormatException {
        if (token != null && !token.isEmpty()) {
            try {
                int ndots = Integer.parseInt(token);
                if (ndots >= 0) {
                    if (ndots > 15) {
                        return 15;
                    }
                    return ndots;
                }
                return 1;
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public final List<InetSocketAddress> servers() {
        if (IPV6_FIRST) {
            return (List) this.nameservers.stream().sorted(new Comparator() { // from class: org.xbill.DNS.config.BaseResolverConfigProvider$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return Integer.compare(((InetSocketAddress) obj2).getAddress().getAddress().length, ((InetSocketAddress) obj).getAddress().getAddress().length);
                }
            }).collect(Collectors.toList());
        }
        if (IPV4_ONLY) {
            return (List) this.nameservers.stream().filter(new Predicate() { // from class: org.xbill.DNS.config.BaseResolverConfigProvider$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return BaseResolverConfigProvider.lambda$servers$1((InetSocketAddress) obj);
                }
            }).collect(Collectors.toList());
        }
        return Collections.unmodifiableList(this.nameservers);
    }

    static /* synthetic */ boolean lambda$servers$1(InetSocketAddress isa) {
        return isa.getAddress() instanceof Inet4Address;
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public final List<Name> searchPaths() {
        return Collections.unmodifiableList(this.searchlist);
    }
}
