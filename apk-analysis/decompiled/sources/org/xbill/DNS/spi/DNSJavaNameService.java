package org.xbill.DNS.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.TextParseException;
import sun.net.spi.nameservice.NameService;

/* loaded from: classes8.dex */
public class DNSJavaNameService implements NameService {
    private static final String DOMAIN_PROPERTY = "sun.net.spi.nameservice.domain";
    private static final String NAMESERVERS_PROPERTY = "sun.net.spi.nameservice.nameservers";
    private static final String PREFER_V6_PROPERTY = "java.net.preferIPv6Addresses";
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DNSJavaNameService.class);
    private boolean addressesLoaded;
    private InetAddress[] localhostAddresses;
    private Name localhostName;
    private InetAddress[] localhostNamedAddresses;
    private final boolean preferV6 = Boolean.getBoolean(PREFER_V6_PROPERTY);

    protected DNSJavaNameService() throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.localhostName = null;
        this.localhostNamedAddresses = null;
        this.localhostAddresses = null;
        this.addressesLoaded = false;
        String nameServers = System.getProperty(NAMESERVERS_PROPERTY);
        String domain = System.getProperty(DOMAIN_PROPERTY);
        if (nameServers != null) {
            StringTokenizer st = new StringTokenizer(nameServers, ",");
            String[] servers = new String[st.countTokens()];
            int n = 0;
            while (st.hasMoreTokens()) {
                servers[n] = st.nextToken();
                n++;
            }
            try {
                Resolver res = new ExtendedResolver(servers);
                Lookup.setDefaultResolver(res);
            } catch (UnknownHostException e) {
                log.error("DNSJavaNameService: invalid {}", NAMESERVERS_PROPERTY);
            }
        }
        if (domain != null) {
            try {
                Lookup.setDefaultSearchPath(domain);
            } catch (TextParseException e2) {
                log.error("DNSJavaNameService: invalid {}", DOMAIN_PROPERTY);
            }
        }
        try {
            Class<?> inetAddressImplFactoryClass = Class.forName("java.net.InetAddressImplFactory");
            Method createMethod = inetAddressImplFactoryClass.getDeclaredMethod("create", new Class[0]);
            createMethod.setAccessible(true);
            Object inetAddressImpl = createMethod.invoke(null, new Object[0]);
            Class<?> inetAddressImplClass = Class.forName("java.net.InetAddressImpl");
            Method hostnameMethod = inetAddressImplClass.getMethod("getLocalHostName", new Class[0]);
            hostnameMethod.setAccessible(true);
            this.localhostName = Name.fromString((String) hostnameMethod.invoke(inetAddressImpl, new Object[0]));
            Method lookupAllHostAddrMethod = inetAddressImplClass.getMethod("lookupAllHostAddr", String.class);
            lookupAllHostAddrMethod.setAccessible(true);
            this.localhostNamedAddresses = (InetAddress[]) lookupAllHostAddrMethod.invoke(inetAddressImpl, this.localhostName.toString());
            this.localhostAddresses = (InetAddress[]) lookupAllHostAddrMethod.invoke(inetAddressImpl, "localhost");
            this.addressesLoaded = true;
        } catch (Exception e3) {
            log.error("Could not obtain localhost", (Throwable) e3);
        }
    }

    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        try {
            Name name = new Name(host);
            if (this.addressesLoaded) {
                if (name.equals(this.localhostName)) {
                    return this.localhostNamedAddresses;
                }
                if ("localhost".equalsIgnoreCase(host)) {
                    return this.localhostAddresses;
                }
            }
            Record[] records = null;
            if (this.preferV6) {
                records = new Lookup(name, 28).run();
            }
            if (records == null) {
                records = new Lookup(name, 1).run();
            }
            if (records == null && !this.preferV6) {
                records = new Lookup(name, 28).run();
            }
            if (records == null) {
                throw new UnknownHostException(host);
            }
            InetAddress[] array = new InetAddress[records.length];
            for (int i = 0; i < records.length; i++) {
                if (records[i] instanceof ARecord) {
                    ARecord a = (ARecord) records[i];
                    array[i] = a.getAddress();
                } else {
                    AAAARecord aaaa = (AAAARecord) records[i];
                    array[i] = aaaa.getAddress();
                }
            }
            return array;
        } catch (TextParseException e) {
            throw new UnknownHostException(host);
        }
    }

    public String getHostByAddr(byte[] addr) throws UnknownHostException {
        Name name = ReverseMap.fromAddress(InetAddress.getByAddress(addr));
        Record[] records = new Lookup(name, 12).run();
        if (records == null) {
            throw new UnknownHostException("Unknown address: " + name);
        }
        return ((PTRRecord) records[0]).getTarget().toString();
    }
}
