package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
abstract class U16NameBase extends Record {
    protected Name nameField;
    protected int u16Field;

    protected U16NameBase() {
    }

    protected U16NameBase(Name name, int type, int dclass, long ttl) {
        super(name, type, dclass, ttl);
    }

    protected U16NameBase(Name name, int type, int dclass, long ttl, int u16Field, String u16Description, Name nameField, String nameDescription) {
        super(name, type, dclass, ttl);
        this.u16Field = checkU16(u16Description, u16Field);
        this.nameField = checkName(nameDescription, nameField);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.u16Field = in.readU16();
        this.nameField = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.u16Field = st.getUInt16();
        this.nameField = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.u16Field + " " + this.nameField;
    }

    protected int getU16Field() {
        return this.u16Field;
    }

    protected Name getNameField() {
        return this.nameField;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.u16Field);
        this.nameField.toWire(out, null, canonical);
    }
}
