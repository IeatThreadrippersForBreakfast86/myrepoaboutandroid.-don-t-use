package org.xbill.DNS;

/* loaded from: classes8.dex */
public class MFRecord extends SingleNameBase {
    MFRecord() {
    }

    public MFRecord(Name name, int dclass, long ttl, Name mailAgent) {
        super(name, 4, dclass, ttl, mailAgent, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getSingleName();
    }
}
