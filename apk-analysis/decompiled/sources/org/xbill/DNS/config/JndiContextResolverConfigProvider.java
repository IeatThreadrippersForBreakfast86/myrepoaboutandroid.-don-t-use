package org.xbill.DNS.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Name;

/* loaded from: classes8.dex */
public class JndiContextResolverConfigProvider implements ResolverConfigProvider {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) JndiContextResolverConfigProvider.class);
    private InnerJndiContextResolverConfigProvider inner;

    public JndiContextResolverConfigProvider() {
        if (!System.getProperty("java.vendor").contains("Android")) {
            try {
                this.inner = new InnerJndiContextResolverConfigProvider();
            } catch (NoClassDefFoundError e) {
                log.debug("JNDI DNS not available");
            }
        }
    }

    private static final class InnerJndiContextResolverConfigProvider extends BaseResolverConfigProvider {
        private static final Logger log = LoggerFactory.getLogger((Class<?>) InnerJndiContextResolverConfigProvider.class);

        static {
            log.debug("JNDI class: {}", DirContext.class.getName());
        }

        private InnerJndiContextResolverConfigProvider() {
        }

        @Override // org.xbill.DNS.config.ResolverConfigProvider
        public void initialize() {
            reset();
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns://");
            String servers = null;
            try {
                InitialDirContext initialDirContext = new InitialDirContext(env);
                servers = (String) initialDirContext.getEnvironment().get("java.naming.provider.url");
                initialDirContext.close();
            } catch (NamingException e) {
            }
            if (servers != null) {
                StringTokenizer st = new StringTokenizer(servers, " ");
                while (st.hasMoreTokens()) {
                    String server = st.nextToken();
                    try {
                        URI serverUri = new URI(server);
                        String host = serverUri.getHost();
                        if (host != null && !host.isEmpty()) {
                            int port = serverUri.getPort();
                            if (port == -1) {
                                port = 53;
                            }
                            addNameserver(new InetSocketAddress(host, port));
                        }
                    } catch (URISyntaxException e2) {
                        log.debug("Could not parse {} as a dns server, ignoring", server, e2);
                    }
                }
            }
        }
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() {
        this.inner.initialize();
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public List<InetSocketAddress> servers() {
        return this.inner.servers();
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public List<Name> searchPaths() {
        return this.inner.searchPaths();
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public boolean isEnabled() {
        return this.inner != null;
    }
}
