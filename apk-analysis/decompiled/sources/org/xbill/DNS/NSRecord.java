package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class NSRecord extends SingleCompressedNameBase {
    NSRecord() {
    }

    public NSRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 2, dclass, ttl, target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getSingleName();
    }
}
