package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes8.dex */
public class ClientSubnetOption extends EDNSOption {
    private InetAddress address;
    private int family;
    private int scopePrefixLength;
    private int sourcePrefixLength;

    ClientSubnetOption() {
        super(8);
    }

    private static int checkMaskLength(String field, int family, int val) {
        int max = Address.addressLength(family) * 8;
        if (val < 0 || val > max) {
            throw new IllegalArgumentException("\"" + field + "\" " + val + " must be in the range [0.." + max + "]");
        }
        return val;
    }

    public ClientSubnetOption(int sourcePrefixLength, int scopePrefixLength, InetAddress address) {
        super(8);
        this.family = Address.familyOf(address);
        this.sourcePrefixLength = checkMaskLength("source netmask", this.family, sourcePrefixLength);
        this.scopePrefixLength = checkMaskLength("scope netmask", this.family, scopePrefixLength);
        this.address = Address.truncate(address, sourcePrefixLength);
        if (!address.equals(this.address)) {
            throw new IllegalArgumentException("source netmask is not valid for address");
        }
    }

    public ClientSubnetOption(int sourcePrefixLength, InetAddress address) {
        this(sourcePrefixLength, 0, address);
    }

    public int getFamily() {
        return this.family;
    }

    public int getSourcePrefixLength() {
        return this.sourcePrefixLength;
    }

    public int getScopePrefixLength() {
        return this.scopePrefixLength;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) throws WireParseException {
        this.family = in.readU16();
        if (this.family != 1 && this.family != 2) {
            throw new WireParseException("unknown address family");
        }
        this.sourcePrefixLength = in.readU8();
        if (this.sourcePrefixLength > Address.addressLength(this.family) * 8) {
            throw new WireParseException("invalid source netmask");
        }
        this.scopePrefixLength = in.readU8();
        if (this.scopePrefixLength > Address.addressLength(this.family) * 8) {
            throw new WireParseException("invalid scope netmask");
        }
        byte[] addr = in.readByteArray();
        if (addr.length != (this.sourcePrefixLength + 7) / 8) {
            throw new WireParseException("invalid address");
        }
        byte[] fulladdr = new byte[Address.addressLength(this.family)];
        System.arraycopy(addr, 0, fulladdr, 0, addr.length);
        try {
            this.address = InetAddress.getByAddress(fulladdr);
            InetAddress tmp = Address.truncate(this.address, this.sourcePrefixLength);
            if (!tmp.equals(this.address)) {
                throw new WireParseException("invalid padding");
            }
        } catch (UnknownHostException e) {
            throw new WireParseException("invalid address", e);
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(DNSOutput out) {
        out.writeU16(this.family);
        out.writeU8(this.sourcePrefixLength);
        out.writeU8(this.scopePrefixLength);
        out.writeByteArray(this.address.getAddress(), 0, (this.sourcePrefixLength + 7) / 8);
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        return this.address.getHostAddress() + "/" + this.sourcePrefixLength + ", scope netmask " + this.scopePrefixLength;
    }
}
