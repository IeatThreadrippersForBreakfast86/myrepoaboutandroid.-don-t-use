package org.xbill.DNS.config;

/* loaded from: classes8.dex */
public class FallbackPropertyResolverConfigProvider extends PropertyResolverConfigProvider {
    public static final String DNS_FALLBACK_NDOTS_PROP = "dns.fallback.ndots";
    public static final String DNS_FALLBACK_SEARCH_PROP = "dns.fallback.search";
    public static final String DNS_FALLBACK_SERVER_PROP = "dns.fallback.server";

    @Override // org.xbill.DNS.config.PropertyResolverConfigProvider, org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() {
        initialize(DNS_FALLBACK_SERVER_PROP, DNS_FALLBACK_SEARCH_PROP, DNS_FALLBACK_NDOTS_PROP);
    }
}
