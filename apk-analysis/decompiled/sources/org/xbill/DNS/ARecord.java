package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import kotlin.UByte;

/* loaded from: classes8.dex */
public class ARecord extends Record {
    private int addr;

    ARecord() {
    }

    private static int fromArray(byte[] array) {
        return ((array[0] & UByte.MAX_VALUE) << 24) | ((array[1] & UByte.MAX_VALUE) << 16) | ((array[2] & UByte.MAX_VALUE) << 8) | (array[3] & UByte.MAX_VALUE);
    }

    private static byte[] toArray(int addr) {
        byte[] bytes = {(byte) ((addr >>> 24) & 255), (byte) ((addr >>> 16) & 255), (byte) ((addr >>> 8) & 255), (byte) (addr & 255)};
        return bytes;
    }

    public ARecord(Name name, int dclass, long ttl, InetAddress address) {
        super(name, 1, dclass, ttl);
        if (Address.familyOf(address) != 1) {
            throw new IllegalArgumentException("invalid IPv4 address");
        }
        this.addr = fromArray(address.getAddress());
    }

    public ARecord(Name name, int dclass, long ttl, byte[] address) {
        super(name, 1, dclass, ttl);
        if (address == null || address.length != 4) {
            throw new IllegalArgumentException("invalid IPv4 address");
        }
        this.addr = fromArray(address);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.addr = fromArray(in.readByteArray(4));
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.addr = fromArray(st.getAddressBytes(1));
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return Address.toDottedQuad(toArray(this.addr));
    }

    public InetAddress getAddress() {
        try {
            if (this.name == null) {
                return InetAddress.getByAddress(toArray(this.addr));
            }
            return InetAddress.getByAddress(this.name.toString(), toArray(this.addr));
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU32(this.addr & 4294967295L);
    }
}
