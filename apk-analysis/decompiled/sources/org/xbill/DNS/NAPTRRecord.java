package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes8.dex */
public class NAPTRRecord extends Record {
    private byte[] flags;
    private int order;
    private int preference;
    private byte[] regexp;
    private Name replacement;
    private byte[] service;

    NAPTRRecord() {
    }

    public NAPTRRecord(Name name, int dclass, long ttl, int order, int preference, String flags, String service, String regexp, Name replacement) {
        super(name, 35, dclass, ttl);
        this.order = checkU16("order", order);
        this.preference = checkU16("preference", preference);
        try {
            this.flags = byteArrayFromString(flags);
            this.service = byteArrayFromString(service);
            this.regexp = byteArrayFromString(regexp);
            this.replacement = checkName("replacement", replacement);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.order = in.readU16();
        this.preference = in.readU16();
        this.flags = in.readCountedString();
        this.service = in.readCountedString();
        this.regexp = in.readCountedString();
        this.replacement = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.order = st.getUInt16();
        this.preference = st.getUInt16();
        try {
            this.flags = byteArrayFromString(st.getString());
            this.service = byteArrayFromString(st.getString());
            this.regexp = byteArrayFromString(st.getString());
            this.replacement = st.getName(origin);
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.order + " " + this.preference + " " + byteArrayToString(this.flags, true) + " " + byteArrayToString(this.service, true) + " " + byteArrayToString(this.regexp, true) + " " + this.replacement;
    }

    public int getOrder() {
        return this.order;
    }

    public int getPreference() {
        return this.preference;
    }

    public String getFlags() {
        return new String(this.flags, StandardCharsets.US_ASCII);
    }

    public String getService(boolean escape) {
        return escape ? byteArrayToString(this.service, false) : new String(this.service, StandardCharsets.UTF_8);
    }

    public String getService() {
        return getService(true);
    }

    public byte[] getServiceAsByteArray() {
        return this.service;
    }

    public String getRegexp(boolean escape) {
        return escape ? byteArrayToString(this.regexp, false) : new String(this.regexp, StandardCharsets.UTF_8);
    }

    public String getRegexp() {
        return getRegexp(true);
    }

    public byte[] getRegexpAsByteArray() {
        return this.regexp;
    }

    public Name getReplacement() {
        return this.replacement;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.order);
        out.writeU16(this.preference);
        out.writeCountedString(this.flags);
        out.writeCountedString(this.service);
        out.writeCountedString(this.regexp);
        this.replacement.toWire(out, null, canonical);
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return this.replacement;
    }
}
