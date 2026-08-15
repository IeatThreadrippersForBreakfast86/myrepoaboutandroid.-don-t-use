package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class UNKRecord extends Record {
    private byte[] data;

    UNKRecord() {
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
        this.data = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("invalid unknown RR encoding");
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return unknownToString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.data);
    }
}
