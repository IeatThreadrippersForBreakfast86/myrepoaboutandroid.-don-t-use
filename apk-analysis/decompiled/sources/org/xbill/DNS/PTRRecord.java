package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class PTRRecord extends SingleCompressedNameBase {
    PTRRecord() {
    }

    public PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 12, dclass, ttl, target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }
}
