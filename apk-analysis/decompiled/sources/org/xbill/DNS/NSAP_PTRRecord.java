package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class NSAP_PTRRecord extends SingleNameBase {
    NSAP_PTRRecord() {
    }

    public NSAP_PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 23, dclass, ttl, target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }
}
