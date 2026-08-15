package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class TLSARecord extends Record {
    private byte[] certificateAssociationData;
    private int certificateUsage;
    private int matchingType;
    private int selector;

    public static class CertificateUsage {
        public static final int CA_CONSTRAINT = 0;
        public static final int DOMAIN_ISSUED_CERTIFICATE = 3;
        public static final int SERVICE_CERTIFICATE_CONSTRAINT = 1;
        public static final int TRUST_ANCHOR_ASSERTION = 2;

        private CertificateUsage() {
        }
    }

    public static class Selector {
        public static final int FULL_CERTIFICATE = 0;
        public static final int SUBJECT_PUBLIC_KEY_INFO = 1;

        private Selector() {
        }
    }

    public static class MatchingType {
        public static final int EXACT = 0;
        public static final int SHA256 = 1;
        public static final int SHA512 = 2;

        private MatchingType() {
        }
    }

    TLSARecord() {
    }

    protected TLSARecord(Name name, int type, int dclass, long ttl, int certificateUsage, int selector, int matchingType, byte[] certificateAssociationData) {
        super(name, type, dclass, ttl);
        this.certificateUsage = checkU8("certificateUsage", certificateUsage);
        this.selector = checkU8("selector", selector);
        this.matchingType = checkU8("matchingType", matchingType);
        if (certificateAssociationData == null || certificateAssociationData.length == 0) {
            throw new IllegalArgumentException("certificateAssociationData must not be null or empty");
        }
        this.certificateAssociationData = checkByteArrayLength("certificateAssociationData", certificateAssociationData, 65535);
    }

    public TLSARecord(Name name, int dclass, long ttl, int certificateUsage, int selector, int matchingType, byte[] certificateAssociationData) {
        this(name, 52, dclass, ttl, certificateUsage, selector, matchingType, certificateAssociationData);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.certificateUsage = in.readU8();
        this.selector = in.readU8();
        this.matchingType = in.readU8();
        this.certificateAssociationData = in.readByteArray();
        if (this.certificateAssociationData.length == 0) {
            throw new WireParseException("end of input");
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.certificateUsage = st.getUInt8();
        this.selector = st.getUInt8();
        this.matchingType = st.getUInt8();
        this.certificateAssociationData = st.getHex(true);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.certificateUsage + " " + this.selector + " " + this.matchingType + " " + base16.toString(this.certificateAssociationData);
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.certificateUsage);
        out.writeU8(this.selector);
        out.writeU8(this.matchingType);
        out.writeByteArray(this.certificateAssociationData);
    }

    public int getCertificateUsage() {
        return this.certificateUsage;
    }

    public int getSelector() {
        return this.selector;
    }

    public int getMatchingType() {
        return this.matchingType;
    }

    public final byte[] getCertificateAssociationData() {
        return this.certificateAssociationData;
    }
}
