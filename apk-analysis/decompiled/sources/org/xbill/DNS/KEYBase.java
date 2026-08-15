package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import kotlin.UByte;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
abstract class KEYBase extends Record {
    protected int alg;
    protected int flags;
    protected int footprint;
    protected byte[] key;
    protected int proto;
    protected PublicKey publicKey;

    protected KEYBase() {
        this.footprint = -1;
        this.publicKey = null;
    }

    protected KEYBase(Name name, int type, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, type, dclass, ttl);
        this.footprint = -1;
        this.publicKey = null;
        this.flags = checkU16("flags", flags);
        this.proto = checkU8("proto", proto);
        this.alg = checkU8("alg", alg);
        this.key = key;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.flags = in.readU16();
        this.proto = in.readU8();
        this.alg = in.readU8();
        if (in.remaining() > 0) {
            this.key = in.readByteArray();
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.flags);
        sb.append(" ");
        sb.append(this.proto);
        sb.append(" ");
        sb.append(this.alg);
        if (this.key != null) {
            if (Options.multiline()) {
                sb.append(" (\n");
                sb.append(base64.formatString(this.key, 64, "\t", true));
                sb.append(" ; key_tag = ");
                sb.append(getFootprint());
            } else {
                sb.append(" ");
                sb.append(base64.toString(this.key));
            }
        }
        return sb.toString();
    }

    public int getFlags() {
        return this.flags;
    }

    public int getProtocol() {
        return this.proto;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getFootprint() {
        int foot;
        if (this.footprint >= 0) {
            return this.footprint;
        }
        int foot2 = 0;
        DNSOutput out = new DNSOutput();
        rrToWire(out, null, false);
        byte[] rdata = out.toByteArray();
        if (this.alg == 1) {
            int d1 = rdata[rdata.length - 3] & UByte.MAX_VALUE;
            int d2 = rdata[rdata.length - 2] & UByte.MAX_VALUE;
            foot = (d1 << 8) + d2;
        } else {
            int i = 0;
            while (i < rdata.length - 1) {
                int d12 = rdata[i] & UByte.MAX_VALUE;
                int d22 = rdata[i + 1] & UByte.MAX_VALUE;
                foot2 += (d12 << 8) + d22;
                i += 2;
            }
            if (i < rdata.length) {
                int d13 = rdata[i] & UByte.MAX_VALUE;
                foot2 += d13 << 8;
            }
            int d14 = foot2 >> 16;
            foot = foot2 + (d14 & 65535);
        }
        this.footprint = foot & 65535;
        return this.footprint;
    }

    public PublicKey getPublicKey() throws DNSSEC.DNSSECException {
        if (this.publicKey != null) {
            return this.publicKey;
        }
        this.publicKey = DNSSEC.toPublicKey(this);
        return this.publicKey;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.flags);
        out.writeU8(this.proto);
        out.writeU8(this.alg);
        if (this.key != null) {
            out.writeByteArray(this.key);
        }
    }
}
