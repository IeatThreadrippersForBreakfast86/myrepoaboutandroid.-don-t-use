package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes8.dex */
public class AAAARecord extends Record {
    private byte[] address;

    AAAARecord() {
    }

    public AAAARecord(Name name, int dclass, long ttl, InetAddress address) {
        super(name, 28, dclass, ttl);
        if (Address.familyOf(address) != 1 && Address.familyOf(address) != 2) {
            throw new IllegalArgumentException("invalid IPv4/IPv6 address");
        }
        this.address = address.getAddress();
    }

    public AAAARecord(Name name, int dclass, long ttl, byte[] address) {
        super(name, 28, dclass, ttl);
        if (address == null || address.length != 16) {
            throw new IllegalArgumentException("invalid IPv6 address");
        }
        this.address = address;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.address = in.readByteArray(16);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.address = st.getAddressBytes(2);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() throws UnknownHostException {
        try {
            InetAddress addr = InetAddress.getByAddress(null, this.address);
            if (addr.getAddress().length == 4) {
                return "::ffff:" + addr.getHostAddress();
            }
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public InetAddress getAddress() {
        try {
            if (this.name == null) {
                return InetAddress.getByAddress(this.address);
            }
            return InetAddress.getByAddress(this.name.toString(), this.address);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.address);
    }
}
