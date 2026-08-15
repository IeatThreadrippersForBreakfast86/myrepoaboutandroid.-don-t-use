package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import kotlin.UByte;

/* loaded from: classes8.dex */
public abstract class EDNSOption implements Serializable {
    private final int code;

    abstract void optionFromWire(DNSInput dNSInput) throws IOException;

    abstract String optionToString();

    abstract void optionToWire(DNSOutput dNSOutput);

    public static class Code {
        public static final int CHAIN = 13;
        public static final int CLIENT_SUBNET = 8;
        public static final int COOKIE = 10;
        public static final int DAU = 5;
        public static final int DHU = 6;
        public static final int EDNS_CLIENT_TAG = 16;
        public static final int EDNS_EXPIRE = 9;
        public static final int EDNS_EXTENDED_ERROR = 15;
        public static final int EDNS_KEY_TAG = 14;
        public static final int EDNS_SERVER_TAG = 17;
        public static final int LLQ = 1;
        public static final int N3U = 7;
        public static final int NSID = 3;
        public static final int PADDING = 12;
        public static final int REPORT_CHANNEL = 18;
        public static final int TCP_KEEPALIVE = 11;

        /* renamed from: UL */
        public static final int f244UL = 2;
        public static final int ZONEVERSION = 19;
        private static final Mnemonic codes = new Mnemonic("EDNS Option Codes", 1);

        private Code() {
        }

        static {
            codes.setMaximum(65535);
            codes.setPrefix("CODE");
            codes.setNumericAllowed(true);
            codes.add(1, "LLQ");
            codes.add(2, "UL");
            codes.add(3, "NSID");
            codes.add(5, "DAU");
            codes.add(6, "DHU");
            codes.add(7, "N3U");
            codes.add(8, "edns-client-subnet");
            codes.add(9, "EDNS_EXPIRE");
            codes.add(10, "COOKIE");
            codes.add(11, "edns-tcp-keepalive");
            codes.add(12, "Padding");
            codes.add(13, "CHAIN");
            codes.add(14, "edns-key-tag");
            codes.add(15, "Extended_DNS_Error");
            codes.add(16, "EDNS-Client-Tag");
            codes.add(17, "EDNS-Server-Tag");
            codes.add(18, "Report-Channel");
            codes.add(19, "ZONEVERSION");
        }

        public static String string(int code) {
            return codes.getText(code);
        }

        public static int value(String s) {
            return codes.getValue(s);
        }
    }

    public EDNSOption(int code) {
        this.code = Record.checkU16("code", code);
    }

    public String toString() {
        return "{" + Code.string(this.code) + ": " + optionToString() + "}";
    }

    public int getCode() {
        return this.code;
    }

    byte[] getData() {
        DNSOutput out = new DNSOutput();
        optionToWire(out);
        return out.toByteArray();
    }

    static EDNSOption fromWire(DNSInput in) throws IOException {
        EDNSOption option;
        int code = in.readU16();
        int length = in.readU16();
        if (in.remaining() < length) {
            throw new WireParseException("truncated option");
        }
        int save = in.saveActive();
        in.setActive(length);
        switch (code) {
            case 3:
                option = new NSIDOption();
                break;
            case 4:
            case 9:
            case 12:
            case 13:
            case 14:
            default:
                option = new GenericEDNSOption(code);
                break;
            case 5:
            case 6:
            case 7:
                option = new DnssecAlgorithmOption(code, new int[0]);
                break;
            case 8:
                option = new ClientSubnetOption();
                break;
            case 10:
                option = new CookieOption();
                break;
            case 11:
                option = new TcpKeepaliveOption();
                break;
            case 15:
                option = new ExtendedErrorCodeOption();
                break;
        }
        option.optionFromWire(in);
        in.restoreActive(save);
        return option;
    }

    public static EDNSOption fromWire(byte[] b) throws IOException {
        return fromWire(new DNSInput(b));
    }

    void toWire(DNSOutput out) {
        out.writeU16(this.code);
        int lengthPosition = out.current();
        out.writeU16(0);
        optionToWire(out);
        int length = (out.current() - lengthPosition) - 2;
        out.writeU16At(length, lengthPosition);
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        return out.toByteArray();
    }

    public boolean equals(Object arg) {
        if (!(arg instanceof EDNSOption)) {
            return false;
        }
        EDNSOption opt = (EDNSOption) arg;
        if (this.code != opt.code) {
            return false;
        }
        return Arrays.equals(getData(), opt.getData());
    }

    public int hashCode() {
        byte[] array = getData();
        int hashval = 0;
        for (byte b : array) {
            hashval += (hashval << 3) + (b & UByte.MAX_VALUE);
        }
        return hashval;
    }
}
