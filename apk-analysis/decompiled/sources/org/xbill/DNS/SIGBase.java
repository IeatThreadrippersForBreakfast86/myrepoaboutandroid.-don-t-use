package org.xbill.DNS;

import java.io.IOException;
import java.time.Instant;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
abstract class SIGBase extends Record {
    protected int alg;
    protected int covered;
    protected Instant expire;
    protected int footprint;
    protected int labels;
    protected long origttl;
    protected byte[] signature;
    protected Name signer;
    protected Instant timeSigned;

    protected SIGBase() {
    }

    protected SIGBase(Name name, int type, int dclass, long ttl, int covered, int alg, long origttl, Instant expire, Instant timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, type, dclass, ttl);
        Type.check(covered);
        TTL.check(origttl);
        this.covered = covered;
        this.alg = checkU8("alg", alg);
        this.labels = name.labels() - 1;
        if (name.isWild()) {
            this.labels--;
        }
        this.origttl = origttl;
        this.expire = expire;
        this.timeSigned = timeSigned;
        this.footprint = checkU16("footprint", footprint);
        this.signer = checkName("signer", signer);
        this.signature = signature;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.covered = in.readU16();
        this.alg = in.readU8();
        this.labels = in.readU8();
        this.origttl = in.readU32();
        this.expire = Instant.ofEpochSecond(in.readU32());
        this.timeSigned = Instant.ofEpochSecond(in.readU32());
        this.footprint = in.readU16();
        this.signer = new Name(in);
        this.signature = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String typeString = st.getString();
        this.covered = Type.value(typeString);
        if (this.covered < 0) {
            throw st.exception("Invalid type: " + typeString);
        }
        String algString = st.getString();
        this.alg = DNSSEC.Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception("Invalid algorithm: " + algString);
        }
        this.labels = st.getUInt8();
        this.origttl = st.getTTL();
        this.expire = FormattedTime.parse(st.getString());
        this.timeSigned = FormattedTime.parse(st.getString());
        this.footprint = st.getUInt16();
        this.signer = st.getName(origin);
        this.signature = st.getBase64();
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Type.string(this.covered));
        sb.append(" ");
        sb.append(this.alg);
        sb.append(" ");
        sb.append(this.labels);
        sb.append(" ");
        sb.append(this.origttl);
        sb.append(" ");
        if (Options.multiline()) {
            sb.append("(\n\t");
        }
        sb.append(FormattedTime.format(this.expire));
        sb.append(" ");
        sb.append(FormattedTime.format(this.timeSigned));
        sb.append(" ");
        sb.append(this.footprint);
        sb.append(" ");
        sb.append(this.signer);
        if (Options.multiline()) {
            sb.append("\n");
            sb.append(base64.formatString(this.signature, 64, "\t", true));
        } else {
            sb.append(" ");
            sb.append(base64.toString(this.signature));
        }
        return sb.toString();
    }

    public int getTypeCovered() {
        return this.covered;
    }

    @Override // org.xbill.DNS.Record
    public int getRRsetType() {
        return this.covered;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getLabels() {
        return this.labels;
    }

    public long getOrigTTL() {
        return this.origttl;
    }

    public Instant getExpire() {
        return this.expire;
    }

    public Instant getTimeSigned() {
        return this.timeSigned;
    }

    public int getFootprint() {
        return this.footprint;
    }

    public Name getSigner() {
        return this.signer;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.covered);
        out.writeU8(this.alg);
        out.writeU8(this.labels);
        out.writeU32(this.origttl);
        out.writeU32(this.expire.getEpochSecond());
        out.writeU32(this.timeSigned.getEpochSecond());
        out.writeU16(this.footprint);
        this.signer.toWire(out, null, canonical);
        out.writeByteArray(this.signature);
    }
}
