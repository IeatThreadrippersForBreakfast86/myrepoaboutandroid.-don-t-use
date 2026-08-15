package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class OPENPGPKEYRecord extends Record {
    private byte[] cert;

    OPENPGPKEYRecord() {
    }

    public OPENPGPKEYRecord(Name name, int dclass, long ttl, byte[] cert) {
        super(name, 61, dclass, ttl);
        this.cert = cert;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
        this.cert = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.cert = st.getBase64();
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        if (this.cert != null) {
            if (Options.multiline()) {
                sb.append("(\n");
                sb.append(base64.formatString(this.cert, 64, "\t", true));
            } else {
                sb.append(base64.toString(this.cert));
            }
        }
        return sb.toString();
    }

    public byte[] getCert() {
        return this.cert;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.cert);
    }
}
