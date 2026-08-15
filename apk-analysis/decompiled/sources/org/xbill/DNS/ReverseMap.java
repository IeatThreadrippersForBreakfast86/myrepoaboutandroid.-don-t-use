package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;
import kotlin.UByte;

/* loaded from: classes8.dex */
public final class ReverseMap {
    private static final Name inaddr4 = Name.fromConstantString("in-addr.arpa.");
    private static final Name inaddr6 = Name.fromConstantString("ip6.arpa.");

    private ReverseMap() {
    }

    public static Name fromAddress(byte[] addr) {
        if (addr.length != 4 && addr.length != 16) {
            throw new IllegalArgumentException("array must contain 4 or 16 elements");
        }
        StringBuilder sb = new StringBuilder();
        if (addr.length == 4) {
            for (int i = addr.length - 1; i >= 0; i--) {
                sb.append(addr[i] & UByte.MAX_VALUE);
                if (i > 0) {
                    sb.append(".");
                }
            }
        } else {
            int[] nibbles = new int[2];
            for (int i2 = addr.length - 1; i2 >= 0; i2--) {
                nibbles[0] = (addr[i2] & UByte.MAX_VALUE) >> 4;
                nibbles[1] = addr[i2] & 15;
                for (int j = nibbles.length - 1; j >= 0; j--) {
                    sb.append(Integer.toHexString(nibbles[j]));
                    if (i2 > 0 || j > 0) {
                        sb.append(".");
                    }
                }
            }
        }
        try {
            if (addr.length == 4) {
                return Name.fromString(sb.toString(), inaddr4);
            }
            return Name.fromString(sb.toString(), inaddr6);
        } catch (TextParseException e) {
            throw new IllegalStateException("name cannot be invalid");
        }
    }

    public static Name fromAddress(int[] addr) {
        byte[] bytes = new byte[addr.length];
        for (int i = 0; i < addr.length; i++) {
            if (addr[i] < 0 || addr[i] > 255) {
                throw new IllegalArgumentException("array must contain values between 0 and 255");
            }
            bytes[i] = (byte) addr[i];
        }
        return fromAddress(bytes);
    }

    public static Name fromAddress(InetAddress addr) {
        return fromAddress(addr.getAddress());
    }

    public static Name fromAddress(String addr, int family) throws UnknownHostException {
        byte[] array = Address.toByteArray(addr, family);
        if (array == null) {
            throw new UnknownHostException("Invalid IP address: " + addr);
        }
        return fromAddress(array);
    }

    public static Name fromAddress(String addr) throws UnknownHostException {
        byte[] array = Address.toByteArray(addr, 1);
        if (array == null) {
            array = Address.toByteArray(addr, 2);
        }
        if (array == null) {
            throw new UnknownHostException("Invalid IP address: " + addr);
        }
        return fromAddress(array);
    }

    public static InetAddress fromName(String name) throws TextParseException, UnknownHostException {
        return fromName(Name.fromString(name));
    }

    public static InetAddress fromName(Name name) throws UnknownHostException {
        if (name.labels() <= 3) {
            throw new UnknownHostException("Not an arpa address: " + name);
        }
        if (name.subdomain(inaddr4)) {
            Name ip = name.relativize(inaddr4);
            if (ip.labels() > 4) {
                throw new UnknownHostException("Invalid IPv4 arpa address: " + name);
            }
            byte[] ipBytes = new byte[4];
            for (int i = 0; i < ip.labels(); i++) {
                try {
                    ipBytes[(ip.labels() - i) - 1] = (byte) Integer.parseInt(ip.getLabelString(i));
                } catch (NumberFormatException e) {
                    throw new UnknownHostException("Invalid IPv4 arpa address: " + name);
                }
            }
            return InetAddress.getByAddress(ipBytes);
        }
        if (name.subdomain(inaddr6)) {
            Name ip2 = name.relativize(inaddr6);
            if (ip2.labels() > 32) {
                throw new UnknownHostException("Invalid IPv6 arpa address: " + name);
            }
            byte[] ipBytes2 = new byte[16];
            for (int i2 = 0; i2 < ip2.labels(); i2++) {
                try {
                    int iLabels = ((ip2.labels() - i2) - 1) / 2;
                    ipBytes2[iLabels] = (byte) (ipBytes2[iLabels] | ((byte) (Byte.parseByte(ip2.getLabelString(i2), 16) << ((ip2.labels() - i2) % 2 == 0 ? (byte) 0 : (byte) 4))));
                } catch (NumberFormatException e2) {
                    throw new UnknownHostException("Invalid IPv6 arpa address: " + name);
                }
            }
            return InetAddress.getByAddress(ipBytes2);
        }
        throw new UnknownHostException("Not an arpa address: " + name);
    }
}
