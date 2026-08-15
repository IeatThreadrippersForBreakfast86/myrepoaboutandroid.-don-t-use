package org.xbill.DNS.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

/* loaded from: classes8.dex */
public class PropertyResolverConfigProvider extends BaseResolverConfigProvider {
    public static final String DNS_NDOTS_PROP = "dns.ndots";
    public static final String DNS_SEARCH_PROP = "dns.search";
    public static final String DNS_SERVER_PROP = "dns.server";
    private int ndots;

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() {
        initialize(DNS_SERVER_PROP, DNS_SEARCH_PROP, DNS_NDOTS_PROP);
    }

    protected void initialize(String serverName, String searchName, String ndotsName) {
        reset();
        String servers = System.getProperty(serverName);
        if (servers != null) {
            StringTokenizer st = new StringTokenizer(servers, ",");
            while (st.hasMoreTokens()) {
                String server = st.nextToken();
                try {
                    URI uri = new URI("dns://" + server);
                    if (uri.getHost() == null) {
                        addNameserver(new InetSocketAddress(server, 53));
                    } else {
                        int port = uri.getPort();
                        if (port == -1) {
                            port = 53;
                        }
                        addNameserver(new InetSocketAddress(uri.getHost(), port));
                    }
                } catch (URISyntaxException e) {
                    this.log.warn("Ignored invalid server {}", server);
                }
            }
        }
        String searchPathProperty = System.getProperty(searchName);
        parseSearchPathList(searchPathProperty, ",");
        String ndotsProperty = System.getProperty(ndotsName);
        this.ndots = parseNdots(ndotsProperty);
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public int ndots() {
        return this.ndots;
    }
}
