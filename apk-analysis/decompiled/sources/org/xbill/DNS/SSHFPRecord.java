package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class SSHFPRecord extends Record {
    private int alg;
    private int digestType;
    private byte[] fingerprint;

    public static class Algorithm {
        public static final int DSS = 2;
        public static final int RSA = 1;

        private Algorithm() {
        }
    }

    public static class Digest {
        public static final int SHA1 = 1;

        private Digest() {
        }
    }

    SSHFPRecord() {
    }

    public SSHFPRecord(Name name, int dclass, long ttl, int alg, int digestType, byte[] fingerprint) {
        super(name, 44, dclass, ttl);
        this.alg = checkU8("alg", alg);
        this.digestType = checkU8("digestType", digestType);
        this.fingerprint = fingerprint;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.alg = in.readU8();
        this.digestType = in.readU8();
        this.fingerprint = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.alg = st.getUInt8();
        this.digestType = st.getUInt8();
        this.fingerprint = st.getHex(true);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.alg + " " + this.digestType + " " + base16.toString(this.fingerprint);
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestType() {
        return this.digestType;
    }

    public byte[] getFingerPrint() {
        return this.fingerprint;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.alg);
        out.writeU8(this.digestType);
        out.writeByteArray(this.fingerprint);
    }
}
