package org.xbill.DNS;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import kotlin.UByte;

/* loaded from: classes8.dex */
public final class Address {
    public static final int IPv4 = 1;
    public static final int IPv6 = 2;

    private Address() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int[] toArray(String s, int family) {
        byte[] byteArray = toByteArray(s, family);
        if (byteArray == null) {
            return null;
        }
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i] & UByte.MAX_VALUE;
        }
        return intArray;
    }

    public static int[] toArray(String s) {
        return toArray(s, 1);
    }

    public static byte[] toByteArray(String s, int family) {
        if (family == 1) {
            return IPAddressUtils.parseV4(s);
        }
        if (family == 2) {
            return IPAddressUtils.parseV6(s);
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static boolean isDottedQuad(String s) {
        byte[] address = toByteArray(s, 1);
        return address != null;
    }

    public static String toDottedQuad(byte[] addr) {
        return (addr[0] & UByte.MAX_VALUE) + "." + (addr[1] & UByte.MAX_VALUE) + "." + (addr[2] & UByte.MAX_VALUE) + "." + (addr[3] & UByte.MAX_VALUE);
    }

    public static String toDottedQuad(int[] addr) {
        return addr[0] + "." + addr[1] + "." + addr[2] + "." + addr[3];
    }

    private static Record[] lookupHostName(String name, boolean all) throws UnknownHostException {
        Record[] aaaa;
        Record[] aaaa2;
        try {
            Lookup lookup = new Lookup(name, 1);
            Record[] a = lookup.run();
            if (a == null) {
                if (lookup.getResult() == 4 && (aaaa2 = new Lookup(name, 28).run()) != null) {
                    return aaaa2;
                }
                throw new UnknownHostException("<" + name + "> could not be resolved: " + lookup.getErrorString());
            }
            if (!all || (aaaa = new Lookup(name, 28).run()) == null) {
                return a;
            }
            Record[] merged = new Record[a.length + aaaa.length];
            System.arraycopy(a, 0, merged, 0, a.length);
            System.arraycopy(aaaa, 0, merged, a.length, aaaa.length);
            return merged;
        } catch (TextParseException e) {
            throw new UnknownHostException("<" + name + "> is invalid: " + e.getMessage());
        }
    }

    private static InetAddress addrFromRecord(String name, Record r) throws UnknownHostException {
        InetAddress addr;
        if (r instanceof ARecord) {
            addr = ((ARecord) r).getAddress();
        } else {
            addr = ((AAAARecord) r).getAddress();
        }
        return InetAddress.getByAddress(name, addr.getAddress());
    }

    public static InetAddress getByName(String name) throws UnknownHostException {
        try {
            return getByAddress(name);
        } catch (UnknownHostException e) {
            Record[] records = lookupHostName(name, false);
            return addrFromRecord(name, records[0]);
        }
    }

    public static InetAddress[] getAllByName(String name) throws UnknownHostException {
        try {
            InetAddress addr = getByAddress(name);
            return new InetAddress[]{addr};
        } catch (UnknownHostException e) {
            Record[] records = lookupHostName(name, true);
            InetAddress[] addrs = new InetAddress[records.length];
            for (int i = 0; i < records.length; i++) {
                addrs[i] = addrFromRecord(name, records[i]);
            }
            return addrs;
        }
    }

    public static InetAddress getByAddress(String addr) throws UnknownHostException {
        byte[] bytes = toByteArray(addr, 1);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        byte[] bytes2 = toByteArray(addr, 2);
        if (bytes2 != null) {
            return InetAddress.getByAddress(addr, bytes2);
        }
        throw new UnknownHostException("Invalid address: " + addr);
    }

    public static InetAddress getByAddress(String addr, int family) throws UnknownHostException {
        if (family != 1 && family != 2) {
            throw new IllegalArgumentException("unknown address family");
        }
        byte[] bytes = toByteArray(addr, family);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        throw new UnknownHostException("Invalid address: " + addr);
    }

    public static String getHostName(InetAddress addr) throws UnknownHostException {
        Name name = ReverseMap.fromAddress(addr);
        Record[] records = new Lookup(name, 12).run();
        if (records == null) {
            throw new UnknownHostException("unknown address: " + name);
        }
        PTRRecord ptr = (PTRRecord) records[0];
        return ptr.getTarget().toString();
    }

    public static int familyOf(InetAddress address) {
        if (address instanceof Inet4Address) {
            return 1;
        }
        if (address instanceof Inet6Address) {
            return 2;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static int addressLength(int family) {
        if (family == 1) {
            return 4;
        }
        if (family == 2) {
            return 16;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static InetAddress truncate(InetAddress address, int maskLength) {
        int family = familyOf(address);
        int maxMaskLength = addressLength(family) * 8;
        if (maskLength < 0 || maskLength > maxMaskLength) {
            throw new IllegalArgumentException("invalid mask length");
        }
        if (maskLength == maxMaskLength) {
            return address;
        }
        byte[] bytes = address.getAddress();
        for (int i = (maskLength / 8) + 1; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        int i2 = maskLength % 8;
        int bitmask = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            bitmask |= 1 << (7 - i3);
        }
        int i4 = maskLength / 8;
        bytes[i4] = (byte) (bytes[i4] & ((byte) bitmask));
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("invalid address");
        }
    }
}
