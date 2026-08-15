package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class KXRecord extends U16NameBase {
    KXRecord() {
    }

    public KXRecord(Name name, int dclass, long ttl, int preference, Name target) {
        super(name, 36, dclass, ttl, preference, "preference", target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPreference() {
        return getU16Field();
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getNameField();
    }
}
