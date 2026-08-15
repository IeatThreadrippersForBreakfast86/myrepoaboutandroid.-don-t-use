package org.xbill.DNS;

/* loaded from: classes8.dex */
public class DNAMERecord extends SingleNameBase {
    DNAMERecord() {
    }

    public DNAMERecord(Name name, int dclass, long ttl, Name alias) {
        super(name, 39, dclass, ttl, alias, "alias");
    }

    public Name getTarget() {
        return getSingleName();
    }

    @Deprecated
    public Name getAlias() {
        return getName();
    }
}
