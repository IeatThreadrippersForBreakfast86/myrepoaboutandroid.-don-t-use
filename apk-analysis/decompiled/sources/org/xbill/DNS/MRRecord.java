package org.xbill.DNS;

/* loaded from: classes8.dex */
public class MRRecord extends SingleNameBase {
    MRRecord() {
    }

    public MRRecord(Name name, int dclass, long ttl, Name newName) {
        super(name, 9, dclass, ttl, newName, "new name");
    }

    public Name getNewName() {
        return getSingleName();
    }
}
