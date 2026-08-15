package org.xbill.DNS;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class TKEYRecord extends Record {
    public static final int DELETE = 5;
    public static final int DIFFIEHELLMAN = 2;
    public static final int GSSAPI = 3;
    public static final int RESOLVERASSIGNED = 4;
    public static final int SERVERASSIGNED = 1;
    private Name alg;
    private int error;
    private byte[] key;
    private int mode;
    private byte[] other;
    private Instant timeExpire;
    private Instant timeInception;

    TKEYRecord() {
    }

    public TKEYRecord(Name name, int dclass, long ttl, Name alg, Instant timeInception, Instant timeExpire, int mode, int error, byte[] key, byte[] other) {
        super(name, Type.TKEY, dclass, ttl);
        this.alg = checkName("alg", alg);
        this.timeInception = timeInception;
        this.timeExpire = timeExpire;
        this.mode = checkU16("mode", mode);
        this.error = checkU16("error", error);
        this.key = key;
        this.other = other;
    }

    @Deprecated
    public TKEYRecord(Name name, int dclass, long ttl, Name alg, Date timeInception, Date timeExpire, int mode, int error, byte[] key, byte[] other) {
        this(name, dclass, ttl, alg, timeInception.toInstant(), timeExpire.toInstant(), mode, error, key, other);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.alg = new Name(in);
        this.timeInception = Instant.ofEpochSecond(in.readU32());
        this.timeExpire = Instant.ofEpochSecond(in.readU32());
        this.mode = in.readU16();
        this.error = in.readU16();
        int keylen = in.readU16();
        if (keylen > 0) {
            this.key = in.readByteArray(keylen);
        } else {
            this.key = null;
        }
        int otherlen = in.readU16();
        if (otherlen > 0) {
            this.other = in.readByteArray(otherlen);
        } else {
            this.other = null;
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for TKEY");
    }

    protected String modeString() {
        switch (this.mode) {
            case 1:
                return "SERVERASSIGNED";
            case 2:
                return "DIFFIEHELLMAN";
            case 3:
                return "GSSAPI";
            case 4:
                return "RESOLVERASSIGNED";
            case 5:
                return "DELETE";
            default:
                return Integer.toString(this.mode);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.alg);
        sb.append(" ");
        if (Options.multiline()) {
            sb.append("(\n\t");
        }
        sb.append(FormattedTime.format(this.timeInception));
        sb.append(" ");
        sb.append(FormattedTime.format(this.timeExpire));
        sb.append(" ");
        sb.append(modeString());
        sb.append(" ");
        sb.append(Rcode.TSIGstring(this.error));
        if (Options.multiline()) {
            sb.append("\n");
            if (this.key != null) {
                sb.append(base64.formatString(this.key, 64, "\t", false));
                sb.append("\n");
            }
            if (this.other != null) {
                sb.append(base64.formatString(this.other, 64, "\t", false));
            }
            sb.append(" )");
        } else {
            sb.append(" ");
            if (this.key != null) {
                sb.append(base64.toString(this.key));
                sb.append(" ");
            }
            if (this.other != null) {
                sb.append(base64.toString(this.other));
            }
        }
        return sb.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Instant getTimeInception() {
        return this.timeInception;
    }

    public Instant getTimeExpire() {
        return this.timeExpire;
    }

    public int getMode() {
        return this.mode;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getKey() {
        return this.key;
    }

    public byte[] getOther() {
        return this.other;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.alg.toWire(out, null, canonical);
        out.writeU32(this.timeInception.getEpochSecond());
        out.writeU32(this.timeExpire.getEpochSecond());
        out.writeU16(this.mode);
        out.writeU16(this.error);
        if (this.key != null) {
            out.writeU16(this.key.length);
            out.writeByteArray(this.key);
        } else {
            out.writeU16(0);
        }
        if (this.other != null) {
            out.writeU16(this.other.length);
            out.writeByteArray(this.other);
        } else {
            out.writeU16(0);
        }
    }
}
