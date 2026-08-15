package org.xbill.DNS;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class ZoneMDRecord extends Record {
    private byte[] digest;
    private int hashAlgorithm;
    private int scheme;
    private long serial;

    public static final class Scheme {
        public static final int RESERVED = 0;
        public static final int SIMPLE = 1;
        private static final Mnemonic schemes = new Mnemonic("ZONEMD Schemes", 2);

        private Scheme() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        static {
            schemes.setMaximum(255);
            schemes.setNumericAllowed(true);
            schemes.add(0, "RESERVED");
            schemes.add(1, "SIMPLE");
        }

        public static String string(int alg) {
            return schemes.getText(alg);
        }

        public static int value(String s) {
            return schemes.getValue(s);
        }
    }

    public static final class Hash {
        public static final int RESERVED = 0;
        public static final int SHA384 = 1;
        public static final int SHA512 = 2;
        private static final Mnemonic schemes = new Mnemonic("ZONEMD Hash Algorithms", 2);
        private static final Map<Integer, Integer> hashLengths = new HashMap(2);

        private Hash() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        static {
            schemes.setMaximum(255);
            schemes.setNumericAllowed(true);
            schemes.add(0, "RESERVED");
            schemes.add(1, "SHA384");
            hashLengths.put(1, 48);
            schemes.add(2, "SHA512");
            hashLengths.put(2, 64);
        }

        public static String string(int alg) {
            return schemes.getText(alg);
        }

        public static int value(String s) {
            return schemes.getValue(s);
        }

        public static int hashLength(int hashAlgorithm) {
            Integer len = hashLengths.get(Integer.valueOf(hashAlgorithm));
            if (len == null) {
                return -1;
            }
            return len.intValue();
        }
    }

    public long getSerial() {
        return this.serial;
    }

    public int getScheme() {
        return this.scheme;
    }

    public int getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public byte[] getDigest() {
        return this.digest;
    }

    ZoneMDRecord() {
    }

    public ZoneMDRecord(Name name, int dclass, long ttl, long serial, int scheme, int hashAlgorithm, byte[] digest) {
        super(name, 63, dclass, ttl);
        this.serial = checkU32("serial", serial);
        this.scheme = checkU8("scheme", scheme);
        this.hashAlgorithm = checkU8("hashAlgorithm", hashAlgorithm);
        String validateDigestSizeMessage = getDigestSizeExceptionMessage(hashAlgorithm, digest);
        if (validateDigestSizeMessage != null) {
            throw new IllegalArgumentException(validateDigestSizeMessage);
        }
        this.digest = digest;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU32(this.serial);
        out.writeU8(this.scheme);
        out.writeU8(this.hashAlgorithm);
        out.writeByteArray(this.digest);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.serial = in.readU32();
        this.scheme = in.readU8();
        this.hashAlgorithm = in.readU8();
        this.digest = in.readByteArray();
        String validateDigestSizeMessage = getDigestSizeExceptionMessage(this.hashAlgorithm, this.digest);
        if (validateDigestSizeMessage != null) {
            throw new WireParseException(validateDigestSizeMessage);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        String rr = this.serial + " " + this.scheme + " " + this.hashAlgorithm + " ";
        if (Options.multiline()) {
            return rr + "(" + base16.toString(this.digest, 48, "\t", true);
        }
        return rr + base16.toString(this.digest);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.serial = st.getUInt32();
        this.scheme = st.getUInt8();
        this.hashAlgorithm = st.getUInt8();
        this.digest = st.getHex(true);
        String validateDigestSizeMessage = getDigestSizeExceptionMessage(this.hashAlgorithm, this.digest);
        if (validateDigestSizeMessage != null) {
            throw st.exception(validateDigestSizeMessage);
        }
    }

    private String getDigestSizeExceptionMessage(int hashAlgorithm, byte[] digest) {
        int len = Hash.hashLength(hashAlgorithm);
        if (len != -1 && len != digest.length) {
            return "Digest size for " + Hash.string(hashAlgorithm) + " be exactly " + Hash.hashLength(hashAlgorithm) + " bytes, got " + digest.length;
        }
        if (digest.length < 12) {
            return "Digest size must be at least 12 bytes, got " + digest.length;
        }
        return null;
    }
}
