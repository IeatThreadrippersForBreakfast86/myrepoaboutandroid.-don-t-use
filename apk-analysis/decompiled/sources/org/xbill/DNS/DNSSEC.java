package org.xbill.DNS;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/* loaded from: classes8.dex */
public class DNSSEC {
    static final int ASN1_BITSTRING = 3;
    static final int ASN1_INT = 2;
    static final int ASN1_OID = 6;
    static final int ASN1_SEQ = 48;
    private static final int DSA_LEN = 20;
    private static final ECKeyInfo GOST = new ECKeyInfo(32, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFD97", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFD94", "A6", "1", "8D91E471E0989CDA27DF505A453F2B7635294F2DDF23E3B122ACC99C9E9F1E14", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF6C611070995AD10045841B09B761B893");
    private static final ECKeyInfo ECDSA_P256 = new ECKeyInfo(32, "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551");
    private static final ECKeyInfo ECDSA_P384 = new ECKeyInfo(48, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC", "B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF", "AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7", "3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973");

    public static class Algorithm {
        public static final int DELETE = 0;

        /* renamed from: DH */
        public static final int f242DH = 2;
        public static final int DSA = 3;
        public static final int DSA_NSEC3_SHA1 = 6;
        public static final int ECC_GOST = 12;
        public static final int ECC_GOST12 = 23;
        public static final int ECDSAP256SHA256 = 13;
        public static final int ECDSAP384SHA384 = 14;
        public static final int ED25519 = 15;
        public static final int ED448 = 16;
        public static final int INDIRECT = 252;
        public static final int PRIVATEDNS = 253;
        public static final int PRIVATEOID = 254;
        public static final int RSAMD5 = 1;
        public static final int RSASHA1 = 5;
        public static final int RSASHA256 = 8;
        public static final int RSASHA512 = 10;
        public static final int RSA_NSEC3_SHA1 = 7;
        public static final int SM2SM3 = 17;
        private static final Mnemonic algs = new Mnemonic("DNSSEC algorithm", 2);

        private Algorithm() {
        }

        static {
            algs.setMaximum(255);
            algs.setNumericAllowed(true);
            algs.add(0, "DELETE");
            algs.add(1, "RSAMD5");
            algs.add(2, "DH");
            algs.add(3, "DSA");
            algs.add(5, "RSASHA1");
            algs.add(6, "DSA-NSEC3-SHA1");
            algs.add(7, "RSASHA1-NSEC3-SHA1");
            algs.add(8, "RSASHA256");
            algs.add(10, "RSASHA512");
            algs.add(12, "ECC-GOST");
            algs.add(13, "ECDSAP256SHA256");
            algs.add(14, "ECDSAP384SHA384");
            algs.add(15, "ED25519");
            algs.add(16, "ED448");
            algs.add(17, "SM2SM3");
            algs.add(23, "ECC-GOST12");
            algs.add(252, "INDIRECT");
            algs.add(253, "PRIVATEDNS");
            algs.add(254, "PRIVATEOID");
        }

        public static String string(int alg) {
            return algs.getText(alg);
        }

        public static int value(String s) {
            return algs.getValue(s);
        }

        public static void check(int val) {
            algs.check(val);
        }
    }

    public static class Digest {
        public static final int GOST3411 = 3;
        public static final int GOST3411_12 = 5;
        public static final int SHA1 = 1;
        public static final int SHA256 = 2;
        public static final int SHA384 = 4;
        public static final int SM3 = 6;
        private static final Mnemonic algs = new Mnemonic("DNSSEC Digest Algorithm", 2);
        private static final Map<Integer, Integer> algLengths = new HashMap(4);

        private Digest() {
        }

        static {
            algs.setMaximum(255);
            algs.setNumericAllowed(true);
            algs.add(1, "SHA-1");
            algLengths.put(1, 20);
            algs.add(2, "SHA-256");
            algLengths.put(2, 32);
            algs.add(3, "GOST R 34.11-94");
            algLengths.put(3, 32);
            algs.add(4, "SHA-384");
            algLengths.put(4, 48);
            algs.add(5, "GOST12");
            algLengths.put(5, 64);
            algs.add(6, "SM3");
            algLengths.put(6, 32);
        }

        public static String string(int alg) {
            return algs.getText(alg);
        }

        public static int value(String s) {
            return algs.getValue(s);
        }

        public static int algLength(int alg) {
            Integer len = algLengths.get(Integer.valueOf(alg));
            if (len == null) {
                return -1;
            }
            return len.intValue();
        }
    }

    private DNSSEC() {
    }

    private static void digestSIG(DNSOutput out, SIGBase sig) {
        out.writeU16(sig.getTypeCovered());
        out.writeU8(sig.getAlgorithm());
        out.writeU8(sig.getLabels());
        out.writeU32(sig.getOrigTTL());
        out.writeU32(sig.getExpire().getEpochSecond());
        out.writeU32(sig.getTimeSigned().getEpochSecond());
        out.writeU16(sig.getFootprint());
        sig.getSigner().toWireCanonical(out);
    }

    public static byte[] digestRRset(RRSIGRecord rrsig, RRset rrset) {
        final DNSOutput out = new DNSOutput();
        digestSIG(out, rrsig);
        Name name = rrset.getName();
        Name wild = null;
        int sigLabels = rrsig.getLabels() + 1;
        if (name.labels() > sigLabels) {
            wild = name.wild(name.labels() - sigLabels);
        }
        final DNSOutput header = new DNSOutput();
        if (wild != null) {
            wild.toWireCanonical(header);
        } else {
            name.toWireCanonical(header);
        }
        header.writeU16(rrset.getType());
        header.writeU16(rrset.getDClass());
        header.writeU32(rrsig.getOrigTTL());
        rrset.rrs(false).stream().sorted().forEachOrdered(new Consumer() { // from class: org.xbill.DNS.DNSSEC$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DNSSEC.lambda$digestRRset$0(out, header, (Record) obj);
            }
        });
        return out.toByteArray();
    }

    static /* synthetic */ void lambda$digestRRset$0(DNSOutput out, DNSOutput header, Record r) {
        out.writeByteArray(header.toByteArray());
        int lengthPosition = out.current();
        out.writeU16(0);
        r.rrToWire(out, null, true);
        int rrlength = (out.current() - lengthPosition) - 2;
        out.save();
        out.jump(lengthPosition);
        out.writeU16(rrlength);
        out.restore();
    }

    public static byte[] digestMessage(SIGRecord sig, Message msg, byte[] previous) {
        DNSOutput out = new DNSOutput();
        digestSIG(out, sig);
        if (previous != null) {
            out.writeByteArray(previous);
        }
        msg.toWire(out);
        return out.toByteArray();
    }

    public static class DNSSECException extends Exception {
        DNSSECException(String message, Throwable cause) {
            super(message, cause);
        }

        DNSSECException(Throwable cause) {
            super(cause);
        }

        DNSSECException(String message) {
            super(message);
        }
    }

    public static class UnsupportedAlgorithmException extends DNSSECException {
        UnsupportedAlgorithmException(int alg) {
            super("Unsupported algorithm: " + alg);
        }
    }

    public static class InvalidDnskeyException extends DNSSECException {
        private final int edeCode;

        public int getEdeCode() {
            return this.edeCode;
        }

        InvalidDnskeyException(DNSKEYRecord dnskey, String message, int edeCode) {
            super("DNSKEY " + dnskey.getName() + " is invalid, " + message);
            this.edeCode = edeCode;
        }
    }

    public static class MalformedKeyException extends DNSSECException {
        MalformedKeyException(String message) {
            super(message);
        }

        MalformedKeyException(Record rec, Throwable cause) {
            super("Invalid key data: " + rec.rdataToString(), cause);
        }
    }

    public static class KeyMismatchException extends DNSSECException {
        KeyMismatchException(KEYBase key, SIGBase sig) {
            super("key " + key.getName() + "/" + Algorithm.string(key.getAlgorithm()) + "/" + key.getFootprint() + " does not match signature " + sig.getSigner() + "/" + Algorithm.string(sig.getAlgorithm()) + "/" + sig.getFootprint());
        }
    }

    public static class SignatureExpiredException extends DNSSECException {
        private final Instant now;
        private final Instant when;

        SignatureExpiredException(Instant when, Instant now) {
            super("signature expired");
            this.when = when;
            this.now = now;
        }

        public Instant getExpiration() {
            return this.when;
        }

        public Instant getVerifyTime() {
            return this.now;
        }
    }

    public static class SignatureNotYetValidException extends DNSSECException {
        private final Instant now;
        private final Instant when;

        SignatureNotYetValidException(Instant when, Instant now) {
            super("signature is not yet valid");
            this.when = when;
            this.now = now;
        }

        public Instant getExpiration() {
            return this.when;
        }

        public Instant getVerifyTime() {
            return this.now;
        }
    }

    public static class SignatureVerificationException extends DNSSECException {
        SignatureVerificationException(Throwable inner) {
            super("Signature verification failed", inner);
        }

        SignatureVerificationException(String message) {
            super("Signature verification failed: " + message);
        }
    }

    public static class IncompatibleKeyException extends IllegalArgumentException {
        IncompatibleKeyException() {
            super("incompatible keys");
        }
    }

    public static class NoSignatureException extends DNSSECException {
        NoSignatureException() {
            super("no signature found");
        }
    }

    private static int bigIntegerLength(BigInteger i) {
        return (i.bitLength() + 7) / 8;
    }

    private static BigInteger readBigInteger(DNSInput in, int len) throws IOException {
        byte[] b = in.readByteArray(len);
        return new BigInteger(1, b);
    }

    private static BigInteger readBigInteger(DNSInput in) {
        byte[] b = in.readByteArray();
        return new BigInteger(1, b);
    }

    private static byte[] trimByteArray(byte[] array) {
        if (array[0] == 0) {
            byte[] trimmedArray = new byte[array.length - 1];
            System.arraycopy(array, 1, trimmedArray, 0, array.length - 1);
            return trimmedArray;
        }
        return array;
    }

    private static void reverseByteArray(byte[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int j = (array.length - i) - 1;
            byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    private static BigInteger readBigIntegerLittleEndian(DNSInput in, int len) throws IOException {
        byte[] b = in.readByteArray(len);
        reverseByteArray(b);
        return new BigInteger(1, b);
    }

    private static void writeBigInteger(DNSOutput out, BigInteger val) {
        byte[] b = trimByteArray(val.toByteArray());
        out.writeByteArray(b);
    }

    private static void writePaddedBigInteger(DNSOutput out, BigInteger val, int len) {
        byte[] b = trimByteArray(val.toByteArray());
        if (b.length > len) {
            throw new IllegalArgumentException();
        }
        if (b.length < len) {
            byte[] pad = new byte[len - b.length];
            out.writeByteArray(pad);
        }
        out.writeByteArray(b);
    }

    private static void writePaddedBigIntegerLittleEndian(DNSOutput out, BigInteger val, int len) {
        byte[] b = trimByteArray(val.toByteArray());
        if (b.length > len) {
            throw new IllegalArgumentException();
        }
        reverseByteArray(b);
        out.writeByteArray(b);
        if (b.length < len) {
            byte[] pad = new byte[len - b.length];
            out.writeByteArray(pad);
        }
    }

    private static PublicKey toRSAPublicKey(byte[] key) throws GeneralSecurityException, IOException {
        DNSInput in = new DNSInput(key);
        int exponentLength = in.readU8();
        if (exponentLength == 0) {
            exponentLength = in.readU16();
        }
        BigInteger exponent = readBigInteger(in, exponentLength);
        BigInteger modulus = readBigInteger(in);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
    }

    private static PublicKey toDSAPublicKey(byte[] key) throws MalformedKeyException, GeneralSecurityException, IOException {
        DNSInput in = new DNSInput(key);
        int t = in.readU8();
        if (t > 8) {
            throw new MalformedKeyException("t is too large");
        }
        BigInteger q = readBigInteger(in, 20);
        BigInteger p = readBigInteger(in, (t * 8) + 64);
        BigInteger g = readBigInteger(in, (t * 8) + 64);
        BigInteger y = readBigInteger(in, (t * 8) + 64);
        KeyFactory factory = KeyFactory.getInstance("DSA");
        return factory.generatePublic(new DSAPublicKeySpec(y, p, q, g));
    }

    private static class ECKeyInfo {
        EllipticCurve curve;
        int length;
        ECParameterSpec spec;

        ECKeyInfo(int length, String p, String a, String b, String gx, String gy, String n) {
            this.length = length;
            BigInteger pi = new BigInteger(p, 16);
            BigInteger ai = new BigInteger(a, 16);
            BigInteger bi = new BigInteger(b, 16);
            BigInteger gxi = new BigInteger(gx, 16);
            BigInteger gyi = new BigInteger(gy, 16);
            BigInteger ni = new BigInteger(n, 16);
            this.curve = new EllipticCurve(new ECFieldFp(pi), ai, bi);
            this.spec = new ECParameterSpec(this.curve, new ECPoint(gxi, gyi), ni, 1);
        }
    }

    private static PublicKey toECGOSTPublicKey(byte[] key, ECKeyInfo keyinfo) throws GeneralSecurityException, IOException {
        DNSInput in = new DNSInput(key);
        BigInteger x = readBigIntegerLittleEndian(in, keyinfo.length);
        BigInteger y = readBigIntegerLittleEndian(in, keyinfo.length);
        ECPoint q = new ECPoint(x, y);
        KeyFactory factory = KeyFactory.getInstance("ECGOST3410");
        return factory.generatePublic(new ECPublicKeySpec(q, keyinfo.spec));
    }

    private static PublicKey toECDSAPublicKey(byte[] key, ECKeyInfo keyinfo) throws GeneralSecurityException, IOException {
        DNSInput in = new DNSInput(key);
        BigInteger x = readBigInteger(in, keyinfo.length);
        BigInteger y = readBigInteger(in, keyinfo.length);
        ECPoint q = new ECPoint(x, y);
        KeyFactory factory = KeyFactory.getInstance("EC");
        return factory.generatePublic(new ECPublicKeySpec(q, keyinfo.spec));
    }

    private static PublicKey toEdDSAPublicKey(byte[] key, byte algId) throws GeneralSecurityException {
        byte[] encoded = new byte[key.length + 12];
        encoded[0] = 48;
        encoded[1] = (byte) (key.length + 10);
        encoded[2] = 48;
        encoded[3] = 5;
        encoded[4] = 6;
        encoded[5] = 3;
        encoded[6] = 43;
        encoded[7] = 101;
        encoded[8] = algId;
        encoded[9] = 3;
        encoded[10] = (byte) (key.length + 1);
        System.arraycopy(key, 0, encoded, 12, key.length);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");
        return keyFactory.generatePublic(keySpec);
    }

    static PublicKey toPublicKey(KEYBase r) throws DNSSECException {
        return toPublicKey(r.getAlgorithm(), r.getKey(), r);
    }

    static PublicKey toPublicKey(int alg, byte[] key, Record r) throws DNSSECException {
        try {
            switch (alg) {
                case 1:
                case 5:
                case 7:
                case 8:
                case 10:
                    return toRSAPublicKey(key);
                case 2:
                case 4:
                case 9:
                case 11:
                default:
                    throw new UnsupportedAlgorithmException(alg);
                case 3:
                case 6:
                    return toDSAPublicKey(key);
                case 12:
                    return toECGOSTPublicKey(key, GOST);
                case 13:
                    return toECDSAPublicKey(key, ECDSA_P256);
                case 14:
                    return toECDSAPublicKey(key, ECDSA_P384);
                case 15:
                    return toEdDSAPublicKey(key, (byte) 112);
                case 16:
                    return toEdDSAPublicKey(key, (byte) 113);
            }
        } catch (IOException e) {
            throw new MalformedKeyException(r, e);
        } catch (GeneralSecurityException e2) {
            throw new DNSSECException(e2);
        }
    }

    private static byte[] fromRSAPublicKey(RSAPublicKey key) {
        DNSOutput out = new DNSOutput();
        BigInteger exponent = key.getPublicExponent();
        BigInteger modulus = key.getModulus();
        int exponentLength = bigIntegerLength(exponent);
        if (exponentLength < 256) {
            out.writeU8(exponentLength);
        } else {
            out.writeU8(0);
            out.writeU16(exponentLength);
        }
        writeBigInteger(out, exponent);
        writeBigInteger(out, modulus);
        return out.toByteArray();
    }

    private static byte[] fromDSAPublicKey(DSAPublicKey key) {
        DNSOutput out = new DNSOutput();
        BigInteger q = key.getParams().getQ();
        BigInteger p = key.getParams().getP();
        BigInteger g = key.getParams().getG();
        BigInteger y = key.getY();
        int t = (p.toByteArray().length - 64) / 8;
        out.writeU8(t);
        writeBigInteger(out, q);
        writeBigInteger(out, p);
        writePaddedBigInteger(out, g, (t * 8) + 64);
        writePaddedBigInteger(out, y, (t * 8) + 64);
        return out.toByteArray();
    }

    private static byte[] fromECGOSTPublicKey(ECPublicKey key, ECKeyInfo keyinfo) {
        DNSOutput out = new DNSOutput();
        BigInteger x = key.getW().getAffineX();
        BigInteger y = key.getW().getAffineY();
        writePaddedBigIntegerLittleEndian(out, x, keyinfo.length);
        writePaddedBigIntegerLittleEndian(out, y, keyinfo.length);
        return out.toByteArray();
    }

    private static byte[] fromECDSAPublicKey(ECPublicKey key, ECKeyInfo keyinfo) {
        DNSOutput out = new DNSOutput();
        BigInteger x = key.getW().getAffineX();
        BigInteger y = key.getW().getAffineY();
        writePaddedBigInteger(out, x, keyinfo.length);
        writePaddedBigInteger(out, y, keyinfo.length);
        return out.toByteArray();
    }

    private static byte[] fromEdDSAPublicKey(PublicKey key) {
        byte[] encoded = key.getEncoded();
        return Arrays.copyOfRange(encoded, 12, encoded.length);
    }

    static byte[] fromPublicKey(PublicKey key, int alg) throws DNSSECException {
        switch (alg) {
            case 1:
            case 5:
            case 7:
            case 8:
            case 10:
                if (!(key instanceof RSAPublicKey)) {
                    throw new IncompatibleKeyException();
                }
                return fromRSAPublicKey((RSAPublicKey) key);
            case 2:
            case 4:
            case 9:
            case 11:
            default:
                throw new UnsupportedAlgorithmException(alg);
            case 3:
            case 6:
                if (!(key instanceof DSAPublicKey)) {
                    throw new IncompatibleKeyException();
                }
                return fromDSAPublicKey((DSAPublicKey) key);
            case 12:
                if (!(key instanceof ECPublicKey)) {
                    throw new IncompatibleKeyException();
                }
                return fromECGOSTPublicKey((ECPublicKey) key, GOST);
            case 13:
                if (!(key instanceof ECPublicKey)) {
                    throw new IncompatibleKeyException();
                }
                return fromECDSAPublicKey((ECPublicKey) key, ECDSA_P256);
            case 14:
                if (!(key instanceof ECPublicKey)) {
                    throw new IncompatibleKeyException();
                }
                return fromECDSAPublicKey((ECPublicKey) key, ECDSA_P384);
            case 15:
            case 16:
                if (!key.getFormat().equalsIgnoreCase("X.509")) {
                    throw new IncompatibleKeyException();
                }
                return fromEdDSAPublicKey(key);
        }
    }

    public static String algString(int alg) throws UnsupportedAlgorithmException {
        switch (alg) {
            case 1:
                return "MD5withRSA";
            case 2:
            case 4:
            case 9:
            case 11:
            default:
                throw new UnsupportedAlgorithmException(alg);
            case 3:
            case 6:
                return "SHA1withDSA";
            case 5:
            case 7:
                return "SHA1withRSA";
            case 8:
                return "SHA256withRSA";
            case 10:
                return "SHA512withRSA";
            case 12:
                return "GOST3411withECGOST3410";
            case 13:
                return "SHA256withECDSA";
            case 14:
                return "SHA384withECDSA";
            case 15:
                return "Ed25519";
            case 16:
                return "Ed448";
        }
    }

    private static IOException asn1ParseException(Object expected, Object actual) {
        return new IOException("Invalid ASN.1 data, expected " + expected + " got " + actual);
    }

    private static byte[] dsaSignatureFromDNS(byte[] bArr, int i, boolean z) throws IOException, DNSSECException {
        if (bArr.length != (i * 2) + (z ? 1 : 0)) {
            throw new SignatureVerificationException("input has unexpected length " + bArr.length);
        }
        DNSInput dNSInput = new DNSInput(bArr);
        DNSOutput dNSOutput = new DNSOutput();
        if (z) {
            dNSInput.readU8();
        }
        byte[] byteArray = dNSInput.readByteArray(i);
        int dsaIntLen = getDsaIntLen(byteArray, i);
        byte[] byteArray2 = dNSInput.readByteArray(i);
        int dsaIntLen2 = getDsaIntLen(byteArray2, i);
        dNSOutput.writeU8(48);
        dNSOutput.writeU8(dsaIntLen + dsaIntLen2 + 4);
        writeAsn1Int(i, dNSOutput, byteArray, dsaIntLen);
        writeAsn1Int(i, dNSOutput, byteArray2, dsaIntLen2);
        return dNSOutput.toByteArray();
    }

    private static int getDsaIntLen(byte[] bigint, int dsaLen) {
        int len = dsaLen;
        if (bigint[0] < 0) {
            return len + 1;
        }
        for (int i = 0; i < dsaLen - 1 && bigint[i] == 0 && bigint[i + 1] >= 0; i++) {
            len--;
        }
        return len;
    }

    private static void writeAsn1Int(int keyLength, DNSOutput out, byte[] bigint, int bigintLen) {
        out.writeU8(2);
        out.writeU8(bigintLen);
        if (bigintLen > keyLength) {
            out.writeU8(0);
        }
        if (bigintLen >= keyLength) {
            out.writeByteArray(bigint);
        } else {
            out.writeByteArray(bigint, keyLength - bigintLen, bigintLen);
        }
    }

    private static byte[] dsaSignatureToDNS(byte[] signature, int rsLen, int t) throws IOException {
        DNSInput in = new DNSInput(signature);
        DNSOutput out = new DNSOutput();
        if (t > -1) {
            out.writeU8(t);
        }
        int tmp = in.readU8();
        if (tmp != 48) {
            throw asn1ParseException(48, Integer.valueOf(tmp));
        }
        in.readU8();
        transformAns1IntToDns(rsLen, in, out);
        transformAns1IntToDns(rsLen, in, out);
        return out.toByteArray();
    }

    private static void transformAns1IntToDns(int rsLen, DNSInput in, DNSOutput out) throws IOException {
        int tmp = in.readU8();
        if (tmp != 2) {
            throw asn1ParseException(2, Integer.valueOf(tmp));
        }
        int len = in.readU8();
        if (len == rsLen + 1 && in.readU8() == 0) {
            len--;
        } else if (len <= rsLen) {
            for (int i = 0; i < rsLen - len; i++) {
                out.writeU8(0);
            }
        } else {
            throw new IOException("Invalid r/s-value in ASN.1 DER encoded signature: " + len);
        }
        out.writeByteArray(in.readByteArray(len));
    }

    private static void verify(KEYBase keyRecord, SIGBase sigRecord, byte[] data, int coveredType) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSECException {
        PublicKey key = keyRecord.getPublicKey();
        int alg = sigRecord.getAlgorithm();
        byte[] signature = sigRecord.getSignature();
        if (key instanceof DSAPublicKey) {
            try {
                signature = dsaSignatureFromDNS(signature, 20, true);
            } catch (IOException e) {
                throw new SignatureVerificationException(e);
            }
        } else if (key instanceof ECPublicKey) {
            try {
                switch (alg) {
                    case 12:
                        if (signature.length != GOST.length * 2) {
                            throw new SignatureVerificationException("input has unexpected length " + signature.length);
                        }
                        break;
                    case 13:
                        signature = dsaSignatureFromDNS(signature, ECDSA_P256.length, false);
                        break;
                    case 14:
                        signature = dsaSignatureFromDNS(signature, ECDSA_P384.length, false);
                        break;
                    default:
                        throw new UnsupportedAlgorithmException(alg);
                }
            } catch (IOException e2) {
                throw new SignatureVerificationException(e2);
            }
        }
        try {
            Signature s = Signature.getInstance(algString(alg));
            s.initVerify(key);
            s.update(data);
            if (!s.verify(signature)) {
                throw new SignatureVerificationException("Key " + keyRecord.getName() + " (alg=" + keyRecord.getAlgorithm() + ",id=" + keyRecord.getFootprint() + ") doesn't validate <" + sigRecord.getName() + "/" + DClass.string(sigRecord.getDClass()) + "/" + Type.string(coveredType) + "> (alg=" + sigRecord.getAlgorithm() + ",id=" + sigRecord.getFootprint() + ")");
            }
        } catch (GeneralSecurityException e3) {
            throw new DNSSECException(e3);
        }
    }

    private static boolean matches(SIGBase sig, KEYBase key) {
        return key.getAlgorithm() == sig.getAlgorithm() && key.getFootprint() == sig.getFootprint() && key.getName().equals(sig.getSigner());
    }

    public static void verify(RRset rrset, RRSIGRecord rrsig, DNSKEYRecord key) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSECException {
        verify(rrset, rrsig, key, Instant.now());
    }

    @Deprecated
    public static void verify(RRset rrset, RRSIGRecord rrsig, DNSKEYRecord key, Date date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSECException {
        verify(rrset, rrsig, key, date.toInstant());
    }

    public static void verify(RRset rrset, RRSIGRecord rrsig, DNSKEYRecord key, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSECException {
        if ((key.getFlags() & 256) != 256) {
            throw new InvalidDnskeyException(key, "zone key flag is not set", 11);
        }
        if (key.getProtocol() != 3) {
            throw new InvalidDnskeyException(key, "invalid protocol", 6);
        }
        checkKeyAndSigRecord(rrsig, key, date);
        verify(key, rrsig, digestRRset(rrsig, rrset), rrset.getType());
    }

    private static void checkKeyAndSigRecord(SIGBase sig, KEYBase key, Instant date) throws DNSSECException {
        if (!matches(sig, key)) {
            throw new KeyMismatchException(key, sig);
        }
        if (date.compareTo(sig.getExpire()) > 0) {
            throw new SignatureExpiredException(sig.getExpire(), date);
        }
        if (date.compareTo(sig.getTimeSigned()) < 0) {
            throw new SignatureNotYetValidException(sig.getTimeSigned(), date);
        }
    }

    static byte[] sign(PrivateKey privkey, PublicKey pubkey, int alg, byte[] data, String provider) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, DNSSECException {
        Signature s;
        try {
            if (provider != null) {
                s = Signature.getInstance(algString(alg), provider);
            } else {
                s = Signature.getInstance(algString(alg));
            }
            s.initSign(privkey);
            s.update(data);
            byte[] signature = s.sign();
            if (pubkey instanceof DSAPublicKey) {
                try {
                    DSAPublicKey dsa = (DSAPublicKey) pubkey;
                    BigInteger p = dsa.getParams().getP();
                    int t = (bigIntegerLength(p) - 64) / 8;
                    return dsaSignatureToDNS(signature, 20, t);
                } catch (IOException e) {
                    throw new DNSSECException(e);
                }
            }
            if (pubkey instanceof ECPublicKey) {
                try {
                    switch (alg) {
                        case 12:
                            break;
                        case 13:
                            signature = dsaSignatureToDNS(signature, ECDSA_P256.length, -1);
                            break;
                        case 14:
                            signature = dsaSignatureToDNS(signature, ECDSA_P384.length, -1);
                            break;
                        default:
                            throw new UnsupportedAlgorithmException(alg);
                    }
                    return signature;
                } catch (IOException e2) {
                    throw new DNSSECException(e2);
                }
            }
            return signature;
        } catch (GeneralSecurityException e3) {
            throw new DNSSECException(e3);
        }
    }

    static void checkAlgorithm(PrivateKey key, int alg) throws UnsupportedAlgorithmException {
        switch (alg) {
            case 1:
            case 5:
            case 7:
            case 8:
            case 10:
                if (!"RSA".equals(key.getAlgorithm())) {
                    throw new IncompatibleKeyException();
                }
                return;
            case 2:
            case 4:
            case 9:
            case 11:
            default:
                throw new UnsupportedAlgorithmException(alg);
            case 3:
            case 6:
                if (!"DSA".equals(key.getAlgorithm())) {
                    throw new IncompatibleKeyException();
                }
                return;
            case 12:
            case 13:
            case 14:
                if (!"EC".equals(key.getAlgorithm()) && !"ECDSA".equals(key.getAlgorithm())) {
                    throw new IncompatibleKeyException();
                }
                return;
            case 15:
                if (!"Ed25519".equals(key.getAlgorithm()) && !"EdDSA".equals(key.getAlgorithm())) {
                    throw new IncompatibleKeyException();
                }
                return;
            case 16:
                if (!"Ed448".equals(key.getAlgorithm()) && !"EdDSA".equals(key.getAlgorithm())) {
                    throw new IncompatibleKeyException();
                }
                return;
        }
    }

    @Deprecated
    public static RRSIGRecord sign(RRset rrset, DNSKEYRecord key, PrivateKey privkey, Date inception, Date expiration) throws DNSSECException {
        return sign(rrset, key, privkey, inception.toInstant(), expiration.toInstant(), (String) null);
    }

    @Deprecated
    public static RRSIGRecord sign(RRset rrset, DNSKEYRecord key, PrivateKey privkey, Date inception, Date expiration, String provider) throws DNSSECException {
        return sign(rrset, key, privkey, inception.toInstant(), expiration.toInstant(), provider);
    }

    public static RRSIGRecord sign(RRset rrset, DNSKEYRecord key, PrivateKey privkey, Instant inception, Instant expiration) throws DNSSECException {
        return sign(rrset, key, privkey, inception, expiration, (String) null);
    }

    public static RRSIGRecord sign(RRset rrset, DNSKEYRecord key, PrivateKey privkey, Instant inception, Instant expiration, String provider) throws DNSSECException {
        int alg = key.getAlgorithm();
        checkAlgorithm(privkey, alg);
        RRSIGRecord rrsig = new RRSIGRecord(rrset.getName(), rrset.getDClass(), rrset.getTTL(), rrset.getType(), alg, rrset.getTTL(), expiration, inception, key.getFootprint(), key.getName(), (byte[]) null);
        rrsig.setSignature(sign(privkey, key.getPublicKey(), alg, digestRRset(rrsig, rrset), provider));
        return rrsig;
    }

    static SIGRecord signMessage(Message message, SIGRecord previous, KEYRecord key, PrivateKey privkey, Instant inception, Instant expiration) throws DNSSECException {
        int alg = key.getAlgorithm();
        checkAlgorithm(privkey, alg);
        SIGRecord sig = new SIGRecord(Name.root, 255, 0L, 0, alg, 0L, expiration, inception, key.getFootprint(), key.getName(), (byte[]) null);
        DNSOutput out = new DNSOutput();
        digestSIG(out, sig);
        if (previous != null) {
            out.writeByteArray(previous.getSignature());
        }
        out.writeByteArray(message.toWire());
        sig.setSignature(sign(privkey, key.getPublicKey(), alg, out.toByteArray(), (String) null));
        return sig;
    }

    static void verifyMessage(Message message, byte[] bytes, SIGRecord sig, SIGRecord previous, KEYRecord key, Instant now) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSECException, CloneNotSupportedException {
        if (message.sig0start == 0) {
            throw new NoSignatureException();
        }
        checkKeyAndSigRecord(sig, key, now);
        DNSOutput out = new DNSOutput();
        digestSIG(out, sig);
        if (previous != null) {
            out.writeByteArray(previous.getSignature());
        }
        Header header = message.getHeader().clone();
        header.decCount(3);
        out.writeByteArray(header.toWire());
        out.writeByteArray(bytes, 12, message.sig0start - 12);
        verify(key, sig, out.toByteArray(), 0);
    }

    static byte[] generateDSDigest(DNSKEYRecord key, int digestid) throws NoSuchAlgorithmException {
        MessageDigest digest;
        try {
            switch (digestid) {
                case 1:
                    digest = MessageDigest.getInstance("sha-1");
                    break;
                case 2:
                    digest = MessageDigest.getInstance("sha-256");
                    break;
                case 3:
                    digest = MessageDigest.getInstance("GOST3411");
                    break;
                case 4:
                    digest = MessageDigest.getInstance("sha-384");
                    break;
                default:
                    throw new IllegalArgumentException("unknown DS digest type " + digestid);
            }
            digest.update(key.getName().toWireCanonical());
            digest.update(key.rdataToWireCanonical());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("no message digest support");
        }
    }
}
