package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class NULLRecord extends Record {
    private byte[] data;

    NULLRecord() {
    }

    public NULLRecord(Name name, int dclass, long ttl, byte[] data) {
        super(name, 10, dclass, ttl);
        if (data.length > 65535) {
            throw new IllegalArgumentException("data must be <65536 bytes");
        }
        this.data = data;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
        this.data = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no defined text format for NULL records");
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
