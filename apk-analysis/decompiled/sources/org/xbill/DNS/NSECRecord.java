package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class NSECRecord extends Record {
    private Name next;
    private TypeBitmap types;

    NSECRecord() {
    }

    public NSECRecord(Name name, int dclass, long ttl, Name next, int[] types) {
        super(name, 47, dclass, ttl);
        this.next = checkName("next", next);
        for (int value : types) {
            Type.check(value);
        }
        this.types = new TypeBitmap(types);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.next = new Name(in);
        this.types = new TypeBitmap(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.next.toWire(out, null, false);
        this.types.toWire(out);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.next = st.getName(origin);
        this.types = new TypeBitmap(st);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.next);
        if (!this.types.empty()) {
            sb.append(' ');
            sb.append(this.types.toString());
        }
        return sb.toString();
    }

    public Name getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int type) {
        return this.types.contains(type);
    }
}
