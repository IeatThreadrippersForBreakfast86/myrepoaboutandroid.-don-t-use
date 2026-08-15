package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes8.dex */
public class CAARecord extends Record {
    private int flags;
    private byte[] tag;
    private byte[] value;

    public static class Flags {
        public static final int IssuerCritical = 128;

        private Flags() {
        }
    }

    CAARecord() {
    }

    public CAARecord(Name name, int dclass, long ttl, int flags, String tag, String value) {
        super(name, 257, dclass, ttl);
        this.flags = checkU8("flags", flags);
        try {
            this.tag = byteArrayFromString(tag);
            this.value = byteArrayFromString(value);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.flags = in.readU8();
        this.tag = in.readCountedString();
        this.value = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.flags = st.getUInt8();
        try {
            this.tag = byteArrayFromString(st.getString());
            this.value = byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.flags + " " + byteArrayToString(this.tag, false) + " " + byteArrayToString(this.value, true);
    }

    public int getFlags() {
        return this.flags;
    }

    public String getTag() {
        return new String(this.tag, StandardCharsets.US_ASCII);
    }

    public String getValue(boolean escape) {
        return escape ? byteArrayToString(this.value, false) : new String(this.value, StandardCharsets.UTF_8);
    }

    public String getValue() {
        return getValue(true);
    }

    public byte[] getValueAsByteArray() {
        return this.value;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.flags);
        out.writeCountedString(this.tag);
        out.writeByteArray(this.value);
    }
}
