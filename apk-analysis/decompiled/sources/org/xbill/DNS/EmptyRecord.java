package org.xbill.DNS;

/* loaded from: classes8.dex */
class EmptyRecord extends Record {
    EmptyRecord() {
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) {
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return "";
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
    }
}
