package org.xbill.DNS;

/* loaded from: classes8.dex */
public class MBRecord extends SingleNameBase {
    MBRecord() {
    }

    public MBRecord(Name name, int dclass, long ttl, Name mailbox) {
        super(name, 7, dclass, ttl, mailbox, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getSingleName();
    }
}
