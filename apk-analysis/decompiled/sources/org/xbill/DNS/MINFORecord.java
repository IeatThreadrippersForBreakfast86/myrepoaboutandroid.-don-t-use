package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class MINFORecord extends Record {
    private Name errorAddress;
    private Name responsibleAddress;

    MINFORecord() {
    }

    public MINFORecord(Name name, int dclass, long ttl, Name responsibleAddress, Name errorAddress) {
        super(name, 14, dclass, ttl);
        this.responsibleAddress = checkName("responsibleAddress", responsibleAddress);
        this.errorAddress = checkName("errorAddress", errorAddress);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.responsibleAddress = new Name(in);
        this.errorAddress = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.responsibleAddress = st.getName(origin);
        this.errorAddress = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.responsibleAddress + " " + this.errorAddress;
    }

    public Name getResponsibleAddress() {
        return this.responsibleAddress;
    }

    public Name getErrorAddress() {
        return this.errorAddress;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.responsibleAddress.toWire(out, null, canonical);
        this.errorAddress.toWire(out, null, canonical);
    }
}
