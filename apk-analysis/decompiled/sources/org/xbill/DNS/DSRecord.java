package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class DSRecord extends Record {

    @Deprecated
    public static final int GOST3411_DIGEST_ID = 3;

    @Deprecated
    public static final int SHA1_DIGEST_ID = 1;

    @Deprecated
    public static final int SHA256_DIGEST_ID = 2;

    @Deprecated
    public static final int SHA384_DIGEST_ID = 4;
    private int alg;
    private byte[] digest;
    private int digestid;
    private int footprint;

    @Deprecated
    public static class Digest {
        public static final int GOST3411 = 3;
        public static final int SHA1 = 1;
        public static final int SHA256 = 2;
        public static final int SHA384 = 4;

        private Digest() {
        }
    }

    DSRecord() {
    }

    protected DSRecord(Name name, int type, int dclass, long ttl, int footprint, int alg, int digestid, byte[] digest) {
        super(name, type, dclass, ttl);
        int len = DNSSEC.Digest.algLength(digestid);
        if (len > -1 && len != digest.length) {
            throw new IllegalArgumentException("Expected " + len + " bytes for " + DNSSEC.Digest.string(digestid) + ", got " + digest.length);
        }
        this.footprint = checkU16("footprint", footprint);
        this.alg = checkU8("alg", alg);
        this.digestid = checkU8("digestid", digestid);
        this.digest = digest;
    }

    public DSRecord(Name name, int dclass, long ttl, int footprint, int alg, int digestid, byte[] digest) {
        this(name, 43, dclass, ttl, footprint, alg, digestid, digest);
    }

    public DSRecord(Name name, int dclass, long ttl, int digestid, DNSKEYRecord key) {
        this(name, dclass, ttl, key.getFootprint(), key.getAlgorithm(), digestid, DNSSEC.generateDSDigest(key, digestid));
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.footprint = in.readU16();
        this.alg = in.readU8();
        this.digestid = in.readU8();
        this.digest = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.footprint = st.getUInt16();
        this.alg = st.getUInt8();
        this.digestid = st.getUInt8();
        this.digest = st.getHex(true);
        int len = DNSSEC.Digest.algLength(this.digestid);
        if (len > -1 && len != this.digest.length) {
            throw st.exception("Expected " + len + " bytes for " + DNSSEC.Digest.string(this.digestid) + ", got " + this.digest.length);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.footprint);
        sb.append(" ");
        sb.append(this.alg);
        sb.append(" ");
        sb.append(this.digestid);
        if (this.digest != null) {
            sb.append(" ");
            sb.append(base16.toString(this.digest));
        }
        return sb.toString();
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getDigestID() {
        return this.digestid;
    }

    public byte[] getDigest() {
        return this.digest;
    }

    public int getFootprint() {
        return this.footprint;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.footprint);
        out.writeU8(this.alg);
        out.writeU8(this.digestid);
        if (this.digest != null) {
            out.writeByteArray(this.digest);
        }
    }
}
