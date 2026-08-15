package org.xbill.DNS;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.utils.base64;
import org.xbill.DNS.utils.hexdump;

/* loaded from: classes8.dex */
public class TSIG {
    public static final Duration FUDGE;
    private static final Map<Name, Integer> algLengthMap;
    private static final Map<Name, String> algMap;
    private final Name alg;
    private final Clock clock;
    private final String macAlgorithm;
    private final SecretKey macKey;
    private final Name name;
    private final Mac sharedHmac;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) TSIG.class);
    public static final Name GSS_TSIG = Name.fromConstantString("gss-tsig.");
    public static final Name HMAC_MD5 = Name.fromConstantString("HMAC-MD5.SIG-ALG.REG.INT.");

    @Deprecated
    public static final Name HMAC = HMAC_MD5;
    public static final Name HMAC_SHA1 = Name.fromConstantString("hmac-sha1.");
    public static final Name HMAC_SHA224 = Name.fromConstantString("hmac-sha224.");
    public static final Name HMAC_SHA256 = Name.fromConstantString("hmac-sha256.");
    public static final Name HMAC_SHA384 = Name.fromConstantString("hmac-sha384.");
    public static final Name HMAC_SHA512 = Name.fromConstantString("hmac-sha512.");
    public static final Name HMAC_SHA256_128 = Name.fromConstantString("hmac-sha256-128.");
    public static final Name HMAC_SHA384_192 = Name.fromConstantString("hmac-sha384-192.");
    public static final Name HMAC_SHA512_256 = Name.fromConstantString("hmac-sha512-256.");
    private static final Pattern javaAlgNamePattern = Pattern.compile("^Hmac(?<alg>(SHA(1|\\d{3})|MD5))(/(?<length>\\d{3}))?$", 2);

    static {
        Map<Name, String> names = new TreeMap<>();
        names.put(HMAC_MD5, "HmacMD5");
        names.put(HMAC_SHA1, "HmacSHA1");
        names.put(HMAC_SHA224, "HmacSHA224");
        names.put(HMAC_SHA256, "HmacSHA256");
        names.put(HMAC_SHA384, "HmacSHA384");
        names.put(HMAC_SHA512, "HmacSHA512");
        names.put(HMAC_SHA256_128, "HmacSHA256");
        names.put(HMAC_SHA384_192, "HmacSHA384");
        names.put(HMAC_SHA512_256, "HmacSHA512");
        algMap = Collections.unmodifiableMap(names);
        Map<Name, Integer> lengths = new HashMap<>();
        lengths.put(HMAC_MD5, 16);
        lengths.put(HMAC_SHA1, 20);
        lengths.put(HMAC_SHA224, 28);
        lengths.put(HMAC_SHA256, 32);
        lengths.put(HMAC_SHA384, 48);
        lengths.put(HMAC_SHA512, 64);
        lengths.put(HMAC_SHA256_128, 16);
        lengths.put(HMAC_SHA384_192, 24);
        lengths.put(HMAC_SHA512_256, 32);
        algLengthMap = Collections.unmodifiableMap(lengths);
        FUDGE = Duration.ofSeconds(300L);
    }

    public static Name algorithmToName(String alg) {
        if (alg == null) {
            throw new IllegalArgumentException("Null algorithm");
        }
        if (!alg.contains("-")) {
            Matcher m = javaAlgNamePattern.matcher(alg);
            if (m.matches()) {
                alg = "hmac-" + m.group("alg");
                String truncatedLength = m.group("length");
                if (truncatedLength != null) {
                    alg = alg + "-" + truncatedLength;
                }
            }
        }
        if (!alg.endsWith(".")) {
            alg = alg + ".";
        }
        try {
            Name nameAlg = Name.fromString(alg);
            if (nameAlg.equals(Name.fromConstantString("hmac-md5."))) {
                return HMAC_MD5;
            }
            if (algMap.get(nameAlg) == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + nameAlg);
            }
            return nameAlg;
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Deprecated
    public static String nameToAlgorithm(Name name) {
        String alg = algMap.get(name);
        if (alg != null) {
            return alg;
        }
        throw new IllegalArgumentException("Unknown algorithm: " + name);
    }

    private static boolean verify(byte[] expected, byte[] signature) {
        if (signature.length < expected.length) {
            byte[] truncated = new byte[signature.length];
            System.arraycopy(expected, 0, truncated, 0, truncated.length);
            expected = truncated;
        }
        return Arrays.equals(signature, expected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Mac initHmac() throws NoSuchAlgorithmException, InvalidKeyException {
        if (this.sharedHmac != null) {
            try {
                return (Mac) this.sharedHmac.clone();
            } catch (CloneNotSupportedException e) {
                this.sharedHmac.reset();
                return this.sharedHmac;
            }
        }
        try {
            Mac mac = Mac.getInstance(this.macAlgorithm);
            mac.init(this.macKey);
            return mac;
        } catch (GeneralSecurityException ex) {
            throw new IllegalArgumentException("Caught security exception setting up HMAC.", ex);
        }
    }

    public TSIG(Name algorithm, Name name, String key) {
        this(algorithm, name, (byte[]) Objects.requireNonNull(base64.fromString(key)));
    }

    public TSIG(Name algorithm, Name name, byte[] keyBytes) {
        this(algorithm, name, new SecretKeySpec(keyBytes, nameToAlgorithm(algorithm)));
    }

    public TSIG(Name algorithm, Name name, SecretKey key) {
        this(algorithm, name, key, Clock.systemUTC());
    }

    public TSIG(Name algorithm, Name name, SecretKey key, Clock clock) {
        this.name = name;
        this.alg = algorithm;
        this.clock = clock;
        this.macAlgorithm = nameToAlgorithm(algorithm);
        this.macKey = key;
        this.sharedHmac = null;
    }

    @Deprecated
    public TSIG(Mac mac, Name name) {
        this.name = name;
        this.sharedHmac = mac;
        this.macAlgorithm = null;
        this.macKey = null;
        this.clock = Clock.systemUTC();
        this.alg = algorithmToName(mac.getAlgorithm());
    }

    @Deprecated
    public TSIG(Name name, byte[] key) {
        this(HMAC_MD5, name, key);
    }

    public TSIG(Name algorithm, String name, String key) throws IOException {
        byte[] keyBytes = base64.fromString(key);
        if (keyBytes == null) {
            throw new IllegalArgumentException("Invalid TSIG key string");
        }
        try {
            this.name = Name.fromString(name, Name.root);
            this.alg = algorithm;
            this.clock = Clock.systemUTC();
            this.macAlgorithm = nameToAlgorithm(algorithm);
            this.sharedHmac = null;
            this.macKey = new SecretKeySpec(keyBytes, this.macAlgorithm);
        } catch (TextParseException e) {
            throw new IllegalArgumentException("Invalid TSIG key name");
        }
    }

    public TSIG(String algorithm, String name, String key) {
        this(algorithmToName(algorithm), name, key);
    }

    @Deprecated
    public TSIG(String name, String key) {
        this(HMAC_MD5, name, key);
    }

    @Deprecated
    public static TSIG fromString(String str) {
        String[] parts = str.split("[:/]", 3);
        switch (parts.length) {
            case 2:
                return new TSIG(HMAC_MD5, parts[0], parts[1]);
            case 3:
                return new TSIG(parts[0], parts[1], parts[2]);
            default:
                throw new IllegalArgumentException("Invalid TSIG key specification");
        }
    }

    public TSIGRecord generate(Message m, byte[] b, int error, TSIGRecord old) {
        return generate(m, b, error, old, true);
    }

    public TSIGRecord generate(Message m, byte[] b, int error, TSIGRecord old, boolean fullSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = null;
        if (error == 0 || error == 18 || error == 22) {
            hmac = initHmac();
        }
        return generate(m, b, error, old, fullSignature, hmac);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TSIGRecord generate(Message m, byte[] b, int error, TSIGRecord old, boolean fullSignature, Mac hmac) throws IllegalStateException, NumberFormatException {
        byte[] signature;
        byte[] other;
        Instant timeSigned = getTimeSigned(error, old);
        Duration fudge = getTsigFudge();
        boolean signing = hmac != null;
        if (old != null && signing) {
            hmacAddSignature(hmac, old);
        }
        if (signing) {
            if (log.isTraceEnabled()) {
                log.trace(hexdump.dump("TSIG-HMAC rendered message", b));
            }
            hmac.update(b);
        }
        DNSOutput out = new DNSOutput();
        if (fullSignature) {
            this.name.toWireCanonical(out);
            out.writeU16(255);
            out.writeU32(0L);
            this.alg.toWireCanonical(out);
        }
        writeTsigTimerVariables(timeSigned, fudge, out);
        if (fullSignature) {
            out.writeU16(error);
            out.writeU16(0);
        }
        if (signing) {
            byte[] tsigVariables = out.toByteArray();
            if (log.isTraceEnabled()) {
                log.trace(hexdump.dump("TSIG-HMAC variables", tsigVariables));
            }
            byte[] signature2 = hmac.doFinal(tsigVariables);
            if (signature2.length > algLengthMap.get(this.alg).intValue()) {
                signature2 = Arrays.copyOfRange(signature2, 0, algLengthMap.get(this.alg).intValue());
            }
            signature = signature2;
        } else {
            byte[] signature3 = new byte[0];
            signature = signature3;
        }
        if (error != 18) {
            other = null;
        } else {
            DNSOutput out2 = new DNSOutput(6);
            writeTsigTime(this.clock.instant(), out2);
            byte[] other2 = out2.toByteArray();
            other = other2;
        }
        return new TSIGRecord(this.name, 255, 0L, this.alg, timeSigned, fudge, signature, m.getHeader().getID(), error, other);
    }

    private Instant getTimeSigned(int error, TSIGRecord old) {
        return error == 18 ? old.getTimeSigned() : this.clock.instant();
    }

    private static Duration getTsigFudge() throws NumberFormatException {
        int fudgeOption = Options.intValue("tsigfudge");
        return (fudgeOption < 0 || fudgeOption > 32767) ? FUDGE : Duration.ofSeconds(fudgeOption);
    }

    public void apply(Message m, TSIGRecord old) throws NoSuchAlgorithmException, InvalidKeyException {
        apply(m, 0, old, true);
    }

    public void apply(Message m, int error, TSIGRecord old) throws NoSuchAlgorithmException, InvalidKeyException {
        apply(m, error, old, true);
    }

    public void apply(Message m, TSIGRecord old, boolean fullSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        apply(m, 0, old, fullSignature);
    }

    public void apply(Message m, int error, TSIGRecord old, boolean fullSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        Record r = generate(m, m.toWire(), error, old, fullSignature);
        m.addRecord(r, 3);
        m.tsigState = 3;
    }

    @Deprecated
    public void applyStream(Message m, TSIGRecord old, boolean fullSignature) throws NoSuchAlgorithmException, InvalidKeyException {
        apply(m, 0, old, fullSignature);
    }

    @Deprecated
    public byte verify(Message m, byte[] b, int length, TSIGRecord old) {
        return (byte) verify(m, b, old);
    }

    public int verify(Message m, byte[] messageBytes, TSIGRecord requestTSIG) {
        return verify(m, messageBytes, requestTSIG, true);
    }

    public int verify(Message m, byte[] messageBytes, TSIGRecord requestTSIG, boolean fullSignature) {
        return verify(m, messageBytes, requestTSIG, fullSignature, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int verify(Message m, byte[] messageBytes, TSIGRecord requestTSIG, boolean fullSignature, Mac hmac) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        m.tsigState = 4;
        TSIGRecord tsig = m.getTSIG();
        if (tsig == null) {
            return 1;
        }
        if (!tsig.getName().equals(this.name) || !tsig.getAlgorithm().equals(this.alg)) {
            log.debug("BADKEY failure on message id {}, expected: {}/{}, actual: {}/{}", Integer.valueOf(m.getHeader().getID()), this.name, this.alg, tsig.getName(), tsig.getAlgorithm());
            return 17;
        }
        if (hmac == null) {
            hmac = initHmac();
        }
        if (requestTSIG != null && tsig.getError() != 17 && tsig.getError() != 16) {
            hmacAddSignature(hmac, requestTSIG);
        }
        m.getHeader().decCount(3);
        byte[] header = m.getHeader().toWire();
        m.getHeader().incCount(3);
        if (log.isTraceEnabled()) {
            log.trace(hexdump.dump("TSIG-HMAC header", header));
        }
        hmac.update(header);
        int len = m.tsigstart - header.length;
        if (log.isTraceEnabled()) {
            log.trace(hexdump.dump("TSIG-HMAC message after header", messageBytes, header.length, len));
        }
        hmac.update(messageBytes, header.length, len);
        byte[] tsigVariables = getTsigVariables(fullSignature, tsig);
        hmac.update(tsigVariables);
        byte[] signature = tsig.getSignature();
        int badsig = verifySignature(hmac, signature);
        if (badsig != 0) {
            return badsig;
        }
        int badtime = verifyTime(tsig);
        if (badtime != 0) {
            return badtime;
        }
        m.tsigState = 1;
        return 0;
    }

    private static byte[] getTsigVariables(boolean fullSignature, TSIGRecord tsig) {
        DNSOutput out = new DNSOutput();
        if (fullSignature) {
            tsig.getName().toWireCanonical(out);
            out.writeU16(tsig.dclass);
            out.writeU32(tsig.ttl);
            tsig.getAlgorithm().toWireCanonical(out);
        }
        writeTsigTimerVariables(tsig.getTimeSigned(), tsig.getFudge(), out);
        if (fullSignature) {
            out.writeU16(tsig.getError());
            if (tsig.getOther() != null) {
                out.writeU16(tsig.getOther().length);
                out.writeByteArray(tsig.getOther());
            } else {
                out.writeU16(0);
            }
        }
        byte[] tsigVariables = out.toByteArray();
        if (log.isTraceEnabled()) {
            log.trace(hexdump.dump("TSIG-HMAC variables", tsigVariables));
        }
        return tsigVariables;
    }

    private int verifySignature(Mac hmac, byte[] signature) throws IllegalStateException {
        int digestLength = hmac.getMacLength();
        int minDigestLength = Math.max(10, digestLength / 2);
        if (signature.length > digestLength) {
            log.debug("BADSIG: signature too long, expected: {}, actual: {}", Integer.valueOf(digestLength), Integer.valueOf(signature.length));
            return 16;
        }
        if (signature.length < minDigestLength) {
            log.debug("BADSIG: signature too short, expected: {} of {}, actual: {}", Integer.valueOf(minDigestLength), Integer.valueOf(digestLength), Integer.valueOf(signature.length));
            return 16;
        }
        byte[] expectedSignature = hmac.doFinal();
        if (expectedSignature.length > algLengthMap.get(this.alg).intValue()) {
            expectedSignature = Arrays.copyOfRange(expectedSignature, 0, algLengthMap.get(this.alg).intValue());
        }
        if (verify(expectedSignature, signature)) {
            return 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("BADSIG: signature verification failed, expected: {}, actual: {}", base64.toString(expectedSignature), base64.toString(signature));
        }
        return 16;
    }

    private int verifyTime(TSIGRecord tsig) {
        Instant now = this.clock.instant();
        Duration delta = Duration.between(now, tsig.getTimeSigned()).abs();
        if (delta.compareTo(tsig.getFudge()) > 0) {
            log.debug("BADTIME failure, now {} +/- tsig {} > fudge {}", now, tsig.getTimeSigned(), tsig.getFudge());
            return 18;
        }
        return 0;
    }

    public int recordLength() {
        return this.name.length() + 10 + this.alg.length() + 8 + 2 + algLengthMap.get(this.alg).intValue() + 4 + 8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void hmacAddSignature(Mac hmac, TSIGRecord tsig) throws IllegalStateException {
        byte[] signatureSize = DNSOutput.toU16(tsig.getSignature().length);
        if (log.isTraceEnabled()) {
            log.trace(hexdump.dump("TSIG-HMAC signature size", signatureSize));
            log.trace(hexdump.dump("TSIG-HMAC signature", tsig.getSignature()));
        }
        hmac.update(signatureSize);
        hmac.update(tsig.getSignature());
    }

    private static void writeTsigTimerVariables(Instant instant, Duration fudge, DNSOutput out) {
        writeTsigTime(instant, out);
        out.writeU16((int) fudge.getSeconds());
    }

    private static void writeTsigTime(Instant instant, DNSOutput out) {
        long time = instant.getEpochSecond();
        int timeHigh = (int) (time >> 32);
        long timeLow = 4294967295L & time;
        out.writeU16(timeHigh);
        out.writeU32(timeLow);
    }

    public static class StreamGenerator {
        private final TSIG key;
        private TSIGRecord lastTsigRecord;
        private int numGenerated;
        private final Mac sharedHmac;
        private final int signEveryNthMessage;

        public StreamGenerator(TSIG key, TSIGRecord queryTsig) {
            this(key, queryTsig, 1);
        }

        StreamGenerator(TSIG key, TSIGRecord queryTsig, int signEveryNthMessage) {
            if (signEveryNthMessage < 1 || signEveryNthMessage > 100) {
                throw new IllegalArgumentException("signEveryNthMessage must be between 1 and 100");
            }
            this.key = key;
            this.lastTsigRecord = queryTsig;
            this.signEveryNthMessage = signEveryNthMessage;
            this.sharedHmac = this.key.initHmac();
        }

        public void generate(Message message) throws IllegalStateException, NumberFormatException {
            generate(message, true);
        }

        void generate(Message message, boolean isLastMessage) throws IllegalStateException, NumberFormatException {
            boolean isNthMessage = this.numGenerated % this.signEveryNthMessage == 0;
            boolean isFirstMessage = this.numGenerated == 0;
            if (isFirstMessage || isNthMessage || isLastMessage) {
                TSIGRecord r = this.key.generate(message, message.toWire(), 0, isFirstMessage ? this.lastTsigRecord : null, isFirstMessage, this.sharedHmac);
                message.addRecord(r, 3);
                message.tsigState = 3;
                this.lastTsigRecord = r;
                TSIG.hmacAddSignature(this.sharedHmac, r);
            } else {
                byte[] responseBytes = message.toWire(65535);
                this.sharedHmac.update(responseBytes);
            }
            this.numGenerated++;
        }
    }

    public static class StreamVerifier {
        private String errorMessage;
        private final TSIG key;
        private int lastsigned;
        private int nresponses = 0;
        private final TSIGRecord queryTsig;
        private final Mac sharedHmac;

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public StreamVerifier(TSIG tsig, TSIGRecord queryTsig) {
            this.key = tsig;
            this.sharedHmac = this.key.initHmac();
            this.queryTsig = queryTsig;
        }

        public int verify(Message message, byte[] messageBytes) {
            return verify(message, messageBytes, false);
        }

        public int verify(Message message, byte[] messageBytes, boolean isLastMessage) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
            TSIGRecord tsig = message.getTSIG();
            this.nresponses++;
            if (this.nresponses == 1) {
                if (tsig != null) {
                    int result = this.key.verify(message, messageBytes, this.queryTsig, true, this.sharedHmac);
                    TSIG.hmacAddSignature(this.sharedHmac, tsig);
                    this.lastsigned = this.nresponses;
                    return result;
                }
                this.errorMessage = "missing required signature on first message";
                TSIG.log.debug("FORMERR: {}", this.errorMessage);
                message.tsigState = 4;
                return 1;
            }
            if (tsig != null) {
                int result2 = this.key.verify(message, messageBytes, null, false, this.sharedHmac);
                this.lastsigned = this.nresponses;
                TSIG.hmacAddSignature(this.sharedHmac, tsig);
                return result2;
            }
            int result3 = this.nresponses;
            boolean required = result3 - this.lastsigned >= 100;
            if (required) {
                this.errorMessage = "Missing required signature on message #" + this.nresponses;
                TSIG.log.debug("FORMERR: {}", this.errorMessage);
                message.tsigState = 4;
                return 1;
            }
            if (isLastMessage) {
                this.errorMessage = "Missing required signature on last message";
                TSIG.log.debug("FORMERR: {}", this.errorMessage);
                message.tsigState = 4;
                return 1;
            }
            this.errorMessage = "Intermediate message #" + this.nresponses + " without signature";
            TSIG.log.debug("FORMERR: {}", this.errorMessage);
            addUnsignedMessageToMac(message, messageBytes, this.sharedHmac);
            return 0;
        }

        private void addUnsignedMessageToMac(Message m, byte[] messageBytes, Mac hmac) throws IllegalStateException {
            byte[] header = m.getHeader().toWire();
            if (TSIG.log.isTraceEnabled()) {
                TSIG.log.trace(hexdump.dump("TSIG-HMAC header", header));
            }
            hmac.update(header);
            int len = messageBytes.length - header.length;
            if (TSIG.log.isTraceEnabled()) {
                TSIG.log.trace(hexdump.dump("TSIG-HMAC message after header", messageBytes, header.length, len));
            }
            hmac.update(messageBytes, header.length, len);
            m.tsigState = 2;
        }
    }
}
