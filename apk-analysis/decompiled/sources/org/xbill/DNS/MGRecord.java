package org.xbill.DNS;

/* loaded from: classes8.dex */
public class MGRecord extends SingleNameBase {
    MGRecord() {
    }

    public MGRecord(Name name, int dclass, long ttl, Name mailbox) {
        super(name, 8, dclass, ttl, mailbox, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }
}
