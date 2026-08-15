package org.xbill.DNS;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import kotlin.UByte;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class TSIGRecord extends Record {
    private Name alg;
    private int error;
    private Duration fudge;
    private int originalID;
    private byte[] other;
    private byte[] signature;
    private Instant timeSigned;

    TSIGRecord() {
    }

    @Deprecated
    public TSIGRecord(Name name, int dclass, long ttl, Name alg, Date timeSigned, int fudge, byte[] signature, int originalID, int error, byte[] other) {
        this(name, dclass, ttl, alg, timeSigned.toInstant(), Duration.ofSeconds(fudge), signature, originalID, error, other);
    }

    public TSIGRecord(Name name, int dclass, long ttl, Name alg, Instant timeSigned, Duration fudge, byte[] signature, int originalID, int error, byte[] other) {
        super(name, 250, dclass, ttl);
        this.alg = checkName("alg", alg);
        this.timeSigned = timeSigned;
        checkU16("fudge", (int) fudge.getSeconds());
        this.fudge = fudge;
        this.signature = signature;
        this.originalID = checkU16("originalID", originalID);
        this.error = checkU16("error", error);
        this.other = other;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.alg = new Name(in);
        long timeHigh = in.readU16();
        long timeLow = in.readU32();
        long time = (timeHigh << 32) + timeLow;
        this.timeSigned = Instant.ofEpochSecond(time);
        this.fudge = Duration.ofSeconds(in.readU16());
        int sigLen = in.readU16();
        this.signature = in.readByteArray(sigLen);
        this.originalID = in.readU16();
        this.error = in.readU16();
        int otherLen = in.readU16();
        if (otherLen > 0) {
            this.other = in.readByteArray(otherLen);
        } else {
            this.other = null;
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for TSIG");
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.alg);
        sb.append(" ");
        if (Options.multiline()) {
            sb.append("(\n\t");
        }
        sb.append(this.timeSigned.getEpochSecond());
        sb.append(" ");
        sb.append((int) this.fudge.getSeconds());
        sb.append(" ");
        sb.append(this.signature.length);
        if (Options.multiline()) {
            sb.append("\n");
            sb.append(base64.formatString(this.signature, 64, "\t", false));
        } else {
            sb.append(" ");
            sb.append(base64.toString(this.signature));
        }
        sb.append(" ");
        sb.append(Rcode.TSIGstring(this.error));
        sb.append(" ");
        if (this.other == null) {
            sb.append(0);
        } else {
            sb.append(this.other.length);
            if (Options.multiline()) {
                sb.append("\n\n\n\t");
            } else {
                sb.append(" ");
            }
            if (this.error == 18) {
                if (this.other.length != 6) {
                    sb.append("<invalid BADTIME other data>");
                } else {
                    long time = ((this.other[0] & UByte.MAX_VALUE) << 40) + ((this.other[1] & UByte.MAX_VALUE) << 32) + ((this.other[2] & UByte.MAX_VALUE) << 24) + ((this.other[3] & UByte.MAX_VALUE) << 16) + ((this.other[4] & UByte.MAX_VALUE) << 8) + (this.other[5] & UByte.MAX_VALUE);
                    sb.append("<server time: ");
                    sb.append(Instant.ofEpochSecond(time));
                    sb.append(">");
                }
            } else {
                sb.append("<");
                sb.append(base64.toString(this.other));
                sb.append(">");
            }
        }
        if (Options.multiline()) {
            sb.append(" )");
        }
        return sb.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Instant getTimeSigned() {
        return this.timeSigned;
    }

    public Duration getFudge() {
        return this.fudge;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public int getOriginalID() {
        return this.originalID;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getOther() {
        return this.other;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.alg.toWire(out, null, canonical);
        long time = this.timeSigned.getEpochSecond();
        int timeHigh = (int) (time >> 32);
        long timeLow = 4294967295L & time;
        out.writeU16(timeHigh);
        out.writeU32(timeLow);
        out.writeU16((int) this.fudge.getSeconds());
        out.writeU16(this.signature.length);
        out.writeByteArray(this.signature);
        out.writeU16(this.originalID);
        out.writeU16(this.error);
        if (this.other != null) {
            out.writeU16(this.other.length);
            out.writeByteArray(this.other);
        } else {
            out.writeU16(0);
        }
    }
}
