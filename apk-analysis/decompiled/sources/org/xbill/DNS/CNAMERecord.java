package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class CNAMERecord extends SingleCompressedNameBase {
    CNAMERecord() {
    }

    public CNAMERecord(Name name, int dclass, long ttl, Name target) {
        super(name, 5, dclass, ttl, target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }

    @Deprecated
    public Name getAlias() {
        return getName();
    }
}
