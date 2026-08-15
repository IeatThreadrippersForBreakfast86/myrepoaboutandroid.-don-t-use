package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class DHCIDRecord extends Record {
    private byte[] data;

    DHCIDRecord() {
    }

    public DHCIDRecord(Name name, int dclass, long ttl, byte[] data) {
        super(name, 49, dclass, ttl);
        this.data = data;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
        this.data = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.data = st.getBase64();
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.data);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return base64.toString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }
}
