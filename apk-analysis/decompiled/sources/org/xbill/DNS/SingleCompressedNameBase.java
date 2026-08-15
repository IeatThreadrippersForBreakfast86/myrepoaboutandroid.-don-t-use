package org.xbill.DNS;

/* loaded from: classes8.dex */
abstract class SingleCompressedNameBase extends SingleNameBase {
    protected SingleCompressedNameBase() {
    }

    protected SingleCompressedNameBase(Name name, int type, int dclass, long ttl, Name singleName, String description) {
        super(name, type, dclass, ttl, singleName, description);
    }

    @Override // org.xbill.DNS.SingleNameBase, org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.singleName.toWire(out, c, canonical);
    }
}
