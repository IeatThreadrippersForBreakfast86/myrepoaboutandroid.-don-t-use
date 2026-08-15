package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class PXRecord extends Record {
    private Name map822;
    private Name mapX400;
    private int preference;

    PXRecord() {
    }

    public PXRecord(Name name, int dclass, long ttl, int preference, Name map822, Name mapX400) {
        super(name, 26, dclass, ttl);
        this.preference = checkU16("preference", preference);
        this.map822 = checkName("map822", map822);
        this.mapX400 = checkName("mapX400", mapX400);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.preference = in.readU16();
        this.map822 = new Name(in);
        this.mapX400 = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.preference = st.getUInt16();
        this.map822 = st.getName(origin);
        this.mapX400 = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.preference + " " + this.map822 + " " + this.mapX400;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.preference);
        this.map822.toWire(out, null, canonical);
        this.mapX400.toWire(out, null, canonical);
    }

    public int getPreference() {
        return this.preference;
    }

    public Name getMap822() {
        return this.map822;
    }

    public Name getMapX400() {
        return this.mapX400;
    }
}
