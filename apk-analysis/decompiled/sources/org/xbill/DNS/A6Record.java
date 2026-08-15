package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes8.dex */
public class A6Record extends Record {
    private Name prefix;
    private int prefixBits;
    private InetAddress suffix;

    A6Record() {
    }

    public A6Record(Name name, int dclass, long ttl, int prefixBits, InetAddress suffix, Name prefix) {
        super(name, 38, dclass, ttl);
        this.prefixBits = checkU8("prefixBits", prefixBits);
        if (suffix != null && Address.familyOf(suffix) != 2) {
            throw new IllegalArgumentException("invalid IPv6 address");
        }
        this.suffix = suffix;
        if (prefix != null) {
            this.prefix = checkName("prefix", prefix);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.prefixBits = in.readU8();
        int suffixbits = 128 - this.prefixBits;
        int suffixbytes = (suffixbits + 7) / 8;
        if (this.prefixBits < 128) {
            byte[] bytes = new byte[16];
            in.readByteArray(bytes, 16 - suffixbytes, suffixbytes);
            this.suffix = InetAddress.getByAddress(bytes);
        }
        if (this.prefixBits > 0) {
            this.prefix = new Name(in);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.prefixBits = st.getUInt8();
        if (this.prefixBits > 128) {
            throw st.exception("prefix bits must be [0..128]");
        }
        if (this.prefixBits < 128) {
            String s = st.getString();
            try {
                this.suffix = Address.getByAddress(s, 2);
            } catch (UnknownHostException e) {
                throw st.exception("invalid IPv6 address: " + s);
            }
        }
        if (this.prefixBits > 0) {
            this.prefix = st.getName(origin);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefixBits);
        if (this.suffix != null) {
            sb.append(" ");
            sb.append(this.suffix.getHostAddress());
        }
        if (this.prefix != null) {
            sb.append(" ");
            sb.append(this.prefix);
        }
        return sb.toString();
    }

    public int getPrefixBits() {
        return this.prefixBits;
    }

    public InetAddress getSuffix() {
        return this.suffix;
    }

    public Name getPrefix() {
        return this.prefix;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.prefixBits);
        if (this.suffix != null) {
            int suffixbits = 128 - this.prefixBits;
            int suffixbytes = (suffixbits + 7) / 8;
            byte[] data = this.suffix.getAddress();
            out.writeByteArray(data, 16 - suffixbytes, suffixbytes);
        }
        if (this.prefix != null) {
            this.prefix.toWire(out, null, canonical);
        }
    }
}
