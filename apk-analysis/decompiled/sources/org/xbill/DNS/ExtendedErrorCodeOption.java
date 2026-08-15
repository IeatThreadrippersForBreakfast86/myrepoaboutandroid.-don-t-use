package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes8.dex */
public class ExtendedErrorCodeOption extends EDNSOption {
    public static final int BLOCKED = 15;
    public static final int CACHED_ERROR = 13;
    public static final int CENSORED = 16;
    public static final int DNSKEY_MISSING = 9;
    public static final int DNSSEC_BOGUS = 6;
    public static final int DNSSEC_INDETERMINATE = 5;
    public static final int FILTERED = 17;
    public static final int FORGED_ANSWER = 4;
    public static final int INVALID_DATA = 24;
    public static final int INVALID_QUERY_TYPE = 30;
    public static final int NETWORK_ERROR = 23;
    public static final int NOT_AUTHORITATIVE = 20;
    public static final int NOT_READY = 14;
    public static final int NOT_SUPPORTED = 21;
    public static final int NO_REACHABLE_AUTHORITY = 22;
    public static final int NO_ZONE_KEY_BIT_SET = 11;
    public static final int NSEC_MISSING = 12;
    public static final int OTHER = 0;
    public static final int PROHIBITED = 18;
    public static final int RRSIGS_MISSING = 10;
    public static final int SIGNATURE_EXPIRED = 7;
    public static final int SIGNATURE_EXPIRED_BEFORE_VALID = 25;
    public static final int SIGNATURE_NOT_YET_VALID = 8;
    public static final int STALE_ANSWER = 3;
    public static final int STALE_NXDOMAIN_ANSWER = 19;
    public static final int SYNTHESIZED = 29;
    public static final int TOO_EARLY = 26;
    public static final int UNABLE_TO_CONFORM_TO_POLICY = 28;
    public static final int UNSUPPORTED_DNSKEY_ALGORITHM = 1;
    public static final int UNSUPPORTED_DS_DIGEST_TYPE = 2;
    public static final int UNSUPPORTED_NSEC3_ITERATIONS_VALUE = 27;
    private static final Mnemonic codes = new Mnemonic("EDNS Extended Error Codes", 1);
    private int errorCode;
    private String text;

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getText() {
        return this.text;
    }

    static {
        codes.setMaximum(65535);
        codes.setPrefix("EDE");
        codes.add(0, "OTHER");
        codes.add(1, "UNSUPPORTED_DNSKEY_ALGORITHM");
        codes.add(2, "UNSUPPORTED_DS_DIGEST_TYPE");
        codes.add(3, "STALE_ANSWER");
        codes.add(4, "FORGED_ANSWER");
        codes.add(5, "DNSSEC_INDETERMINATE");
        codes.add(6, "DNSSEC_BOGUS");
        codes.add(7, "SIGNATURE_EXPIRED");
        codes.add(8, "SIGNATURE_NOT_YET_VALID");
        codes.add(9, "DNSKEY_MISSING");
        codes.add(10, "RRSIGS_MISSING");
        codes.add(11, "NO_ZONE_KEY_BIT_SET");
        codes.add(12, "NSEC_MISSING");
        codes.add(13, "CACHED_ERROR");
        codes.add(14, "NOT_READY");
        codes.add(15, "BLOCKED");
        codes.add(16, "CENSORED");
        codes.add(17, "FILTERED");
        codes.add(18, "PROHIBITED");
        codes.add(19, "STALE_NXDOMAIN_ANSWER");
        codes.add(20, "NOT_AUTHORITATIVE");
        codes.add(21, "NOT_SUPPORTED");
        codes.add(22, "NO_REACHABLE_AUTHORITY");
        codes.add(23, "NETWORK_ERROR");
        codes.add(24, "INVALID_DATA");
        codes.add(25, "SIGNATURE_EXPIRED_BEFORE_VALID");
        codes.add(26, "TOO_EARLY");
        codes.add(27, "UNSUPPORTED_NSEC3_ITERATIONS_VALUE");
        codes.add(28, "UNABLE_TO_CONFORM_TO_POLICY");
        codes.add(29, "SYNTHESIZED");
        codes.add(30, "INVALID_QUERY_TYPE");
    }

    public static String text(int code) {
        return codes.getText(code);
    }

    public static int code(String text) {
        return codes.getValue(text);
    }

    ExtendedErrorCodeOption() {
        super(15);
    }

    public ExtendedErrorCodeOption(int errorCode, String text) {
        super(15);
        this.errorCode = errorCode;
        this.text = text;
    }

    public ExtendedErrorCodeOption(int errorCode) {
        this(errorCode, null);
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) throws IOException {
        this.errorCode = in.readU16();
        if (in.remaining() > 0) {
            byte[] data = in.readByteArray();
            int len = data.length;
            if (data[data.length - 1] == 0) {
                len--;
            }
            this.text = new String(data, 0, len, StandardCharsets.UTF_8);
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(DNSOutput out) {
        out.writeU16(this.errorCode);
        if (this.text != null && !this.text.isEmpty()) {
            out.writeByteArray(this.text.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        if (this.text == null) {
            return codes.getText(this.errorCode);
        }
        return codes.getText(this.errorCode) + ": " + this.text;
    }
}
