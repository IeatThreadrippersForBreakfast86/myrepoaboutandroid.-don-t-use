package org.xbill.DNS.config;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.ptr.IntByReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Name;
import org.xbill.DNS.config.IPHlpAPI;

/* loaded from: classes8.dex */
public class WindowsResolverConfigProvider implements ResolverConfigProvider {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) WindowsResolverConfigProvider.class);
    private InnerWindowsResolverConfigProvider inner;

    public WindowsResolverConfigProvider() {
        if (System.getProperty("os.name").contains("Windows")) {
            try {
                this.inner = new InnerWindowsResolverConfigProvider();
            } catch (NoClassDefFoundError e) {
                log.debug("JNA not available");
            }
        }
    }

    private static final class InnerWindowsResolverConfigProvider extends BaseResolverConfigProvider {
        private static final Logger log = LoggerFactory.getLogger((Class<?>) InnerWindowsResolverConfigProvider.class);

        static {
            log.debug("Checking for JNA classes: {} and {}", Memory.class.getName(), Win32Exception.class.getName());
        }

        private InnerWindowsResolverConfigProvider() {
        }

        @Override // org.xbill.DNS.config.ResolverConfigProvider
        public void initialize() throws InitializationException {
            reset();
            Pointer memory = new Memory(15360L);
            IntByReference size = new IntByReference(0);
            if (IPHlpAPI.INSTANCE.GetAdaptersAddresses(0, 39, Pointer.NULL, memory, size) == 111) {
                Pointer memory2 = new Memory(size.getValue());
                int error = IPHlpAPI.INSTANCE.GetAdaptersAddresses(0, 39, Pointer.NULL, memory2, size);
                if (error != 0) {
                    throw new InitializationException((Exception) new Win32Exception(error));
                }
                memory = memory2;
            }
            IPHlpAPI.IP_ADAPTER_ADDRESSES_LH result = new IPHlpAPI.IP_ADAPTER_ADDRESSES_LH(memory);
            do {
                if (result.OperStatus == 1) {
                    for (IPHlpAPI.IP_ADAPTER_DNS_SERVER_ADDRESS_XP dns = result.FirstDnsServerAddress; dns != null; dns = dns.Next) {
                        try {
                            InetAddress address = dns.Address.toAddress();
                            if ((address instanceof Inet4Address) || !address.isSiteLocalAddress()) {
                                addNameserver(new InetSocketAddress(address, 53));
                            } else {
                                log.debug("Skipped site-local IPv6 server address {} on adapter index {}", address, Integer.valueOf(result.IfIndex));
                            }
                        } catch (UnknownHostException e) {
                            log.warn("Invalid nameserver address on adapter index {}", Integer.valueOf(result.IfIndex), e);
                        }
                    }
                    addSearchPath(result.DnsSuffix.toString());
                    for (IPHlpAPI.IP_ADAPTER_DNS_SUFFIX suffix = result.FirstDnsSuffix; suffix != null; suffix = suffix.Next) {
                        addSearchPath(String.valueOf(suffix._String));
                    }
                }
                result = result.Next;
            } while (result != null);
        }
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() throws InitializationException {
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
