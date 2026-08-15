package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
abstract class SingleNameBase extends Record {
    protected Name singleName;

    protected SingleNameBase() {
    }

    protected SingleNameBase(Name name, int type, int dclass, long ttl) {
        super(name, type, dclass, ttl);
    }

    protected SingleNameBase(Name name, int type, int dclass, long ttl, Name singleName, String description) {
        super(name, type, dclass, ttl);
        this.singleName = checkName(description, singleName);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.singleName = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.singleName = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.singleName.toString();
    }

    protected Name getSingleName() {
        return this.singleName;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.singleName.toWire(out, null, canonical);
    }
}
