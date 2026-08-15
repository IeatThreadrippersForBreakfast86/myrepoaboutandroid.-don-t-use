package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* loaded from: classes8.dex */
public class MXRecord extends U16NameBase {
    MXRecord() {
    }

    public MXRecord(Name name, int dclass, long ttl, int priority, Name target) {
        super(name, 15, dclass, ttl, priority, "priority", target, TypedValues.AttributesType.S_TARGET);
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPriority() {
        return getU16Field();
    }

    @Override // org.xbill.DNS.U16NameBase, org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.u16Field);
        this.nameField.toWire(out, c, canonical);
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return getNameField();
    }
}
