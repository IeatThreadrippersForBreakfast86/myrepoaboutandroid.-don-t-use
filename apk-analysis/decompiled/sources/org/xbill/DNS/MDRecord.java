package org.xbill.DNS;

/* loaded from: classes8.dex */
public class MDRecord extends SingleNameBase {
    MDRecord() {
    }

    public MDRecord(Name name, int dclass, long ttl, Name mailAgent) {
        super(name, 3, dclass, ttl, mailAgent, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getSingleName();
    }
}
