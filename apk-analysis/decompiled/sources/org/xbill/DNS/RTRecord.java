package org.xbill.DNS;

/* loaded from: classes8.dex */
public class RTRecord extends U16NameBase {
    RTRecord() {
    }

    public RTRecord(Name name, int dclass, long ttl, int preference, Name intermediateHost) {
        super(name, 21, dclass, ttl, preference, "preference", intermediateHost, "intermediateHost");
    }

    public int getPreference() {
        return getU16Field();
    }

    public Name getIntermediateHost() {
        return getNameField();
    }
}
