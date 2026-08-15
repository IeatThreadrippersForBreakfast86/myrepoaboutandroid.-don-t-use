package org.xbill.DNS;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;

/* loaded from: classes8.dex */
public class NSEC3Record extends Record {
    public static final int SHA1_DIGEST_ID = 1;
    private static final base32 b32 = new base32(base32.Alphabet.BASE32HEX, false, false);
    private int flags;
    private int hashAlg;
    private int iterations;
    private byte[] next;
    private byte[] salt;
    private TypeBitmap types;

    public static class Flags {
        public static final int OPT_OUT = 1;

        private Flags() {
        }
    }

    public static class Digest {
        public static final int SHA1 = 1;
        private static final Mnemonic digests = new Mnemonic("DNSSEC NSEC3 Hash Algorithms", 1);

        private Digest() {
        }

        static {
            digests.add(1, "SHA-1");
        }

        public static String string(int alg) {
            return digests.getText(alg);
        }

        public static int value(String s) {
            return digests.getValue(s);
        }
    }

    NSEC3Record() {
    }

    public NSEC3Record(Name name, int dclass, long ttl, int hashAlg, int flags, int iterations, byte[] salt, byte[] next, int[] types) {
        super(name, 50, dclass, ttl);
        this.hashAlg = checkU8("hashAlg", hashAlg);
        this.flags = checkU8("flags", flags);
        this.iterations = checkU16("iterations", iterations);
        if (salt != null) {
            if (salt.length > 255) {
                throw new IllegalArgumentException("Invalid salt");
            }
            if (salt.length > 0) {
                this.salt = new byte[salt.length];
                System.arraycopy(salt, 0, this.salt, 0, salt.length);
            }
        }
        if (next.length > 255) {
            throw new IllegalArgumentException("Invalid next hash");
        }
        this.next = new byte[next.length];
        System.arraycopy(next, 0, this.next, 0, next.length);
        this.types = new TypeBitmap(types);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.hashAlg = in.readU8();
        this.flags = in.readU8();
        this.iterations = in.readU16();
        int saltLength = in.readU8();
        if (saltLength > 0) {
            this.salt = in.readByteArray(saltLength);
        } else {
            this.salt = null;
        }
        int nextLength = in.readU8();
        this.next = in.readByteArray(nextLength);
        this.types = new TypeBitmap(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.hashAlg);
        out.writeU8(this.flags);
        out.writeU16(this.iterations);
        if (this.salt != null) {
            out.writeU8(this.salt.length);
            out.writeByteArray(this.salt);
        } else {
            out.writeU8(0);
        }
        out.writeU8(this.next.length);
        out.writeByteArray(this.next);
        this.types.toWire(out);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.hashAlg = st.getUInt8();
        this.flags = st.getUInt8();
        this.iterations = st.getUInt16();
        String s = st.getString();
        if (s.equals("-")) {
            this.salt = null;
        } else {
            st.unget();
            this.salt = st.getHexString();
            if (this.salt.length > 255) {
                throw st.exception("salt value too long");
            }
        }
        this.next = st.getBase32String(b32);
        this.types = new TypeBitmap(st);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.hashAlg);
        sb.append(' ');
        sb.append(this.flags);
        sb.append(' ');
        sb.append(this.iterations);
        sb.append(' ');
        if (this.salt == null) {
            sb.append('-');
        } else {
            sb.append(base16.toString(this.salt));
        }
        sb.append(' ');
        sb.append(b32.toString(this.next));
        if (!this.types.empty()) {
            sb.append(' ');
            sb.append(this.types.toString());
        }
        return sb.toString();
    }

    public int getHashAlgorithm() {
        return this.hashAlg;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getIterations() {
        return this.iterations;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int type) {
        return this.types.contains(type);
    }

    static byte[] hashName(Name name, int hashAlg, int iterations, byte[] salt) throws NoSuchAlgorithmException {
        if (hashAlg == 1) {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            byte[] hash = null;
            for (int i = 0; i <= iterations; i++) {
                digest.reset();
                if (i == 0) {
                    digest.update(name.toWireCanonical());
                } else {
                    digest.update(hash);
                }
                if (salt != null) {
                    digest.update(salt);
                }
                hash = digest.digest();
            }
            return hash;
        }
        throw new NoSuchAlgorithmException("Unknown NSEC3 algorithm identifier: " + hashAlg);
    }

    public byte[] hashName(Name name) throws NoSuchAlgorithmException {
        return hashName(name, this.hashAlg, this.iterations, this.salt);
    }
}
