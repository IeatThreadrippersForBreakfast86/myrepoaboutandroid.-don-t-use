package org.xbill.DNS;

/* loaded from: classes8.dex */
public class AFSDBRecord extends U16NameBase {
    AFSDBRecord() {
    }

    public AFSDBRecord(Name name, int dclass, long ttl, int subtype, Name host) {
        super(name, 18, dclass, ttl, subtype, "subtype", host, "host");
    }

    public int getSubtype() {
        return getU16Field();
    }

    public Name getHost() {
        return getNameField();
    }
}
