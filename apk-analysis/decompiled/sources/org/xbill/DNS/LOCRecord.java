package org.xbill.DNS;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import kotlin.UByte;
import org.xbill.DNS.Tokenizer;

/* loaded from: classes8.dex */
public class LOCRecord extends Record {

    /* renamed from: w2 */
    private static final NumberFormat f258w2 = new DecimalFormat();

    /* renamed from: w3 */
    private static final NumberFormat f259w3;
    private long altitude;
    private long hPrecision;
    private long latitude;
    private long longitude;
    private long size;
    private long vPrecision;

    static {
        f258w2.setMinimumIntegerDigits(2);
        f259w3 = new DecimalFormat();
        f259w3.setMinimumIntegerDigits(3);
    }

    LOCRecord() {
    }

    public LOCRecord(Name name, int dclass, long ttl, double latitude, double longitude, double altitude, double size, double hPrecision, double vPrecision) {
        super(name, 29, dclass, ttl);
        this.latitude = (long) ((latitude * 3600.0d * 1000.0d) + 2.147483648E9d);
        this.longitude = (long) ((3600.0d * longitude * 1000.0d) + 2.147483648E9d);
        this.altitude = (long) ((altitude + 100000.0d) * 100.0d);
        this.size = (long) (size * 100.0d);
        this.hPrecision = (long) (hPrecision * 100.0d);
        this.vPrecision = (long) (vPrecision * 100.0d);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        int version = in.readU8();
        if (version != 0) {
            throw new WireParseException("Invalid LOC version");
        }
        this.size = parseLOCformat(in.readU8());
        this.hPrecision = parseLOCformat(in.readU8());
        this.vPrecision = parseLOCformat(in.readU8());
        this.latitude = in.readU32();
        this.longitude = in.readU32();
        this.altitude = in.readU32();
    }

    private double parseFixedPoint(String s) {
        if (s.matches("^-?\\d+$")) {
            return Integer.parseInt(s);
        }
        if (s.matches("^-?\\d+\\.\\d*$")) {
            String[] parts = s.split("\\.");
            double value = Integer.parseInt(parts[0]);
            double fraction = Integer.parseInt(parts[1]);
            if (value < 0.0d) {
                fraction *= -1.0d;
            }
            int digits = parts[1].length();
            return (fraction / Math.pow(10.0d, digits)) + value;
        }
        throw new NumberFormatException();
    }

    private long parsePosition(Tokenizer st, String type) throws IOException, NumberFormatException {
        boolean isLatitude = type.equals("latitude");
        int min = 0;
        double sec = 0.0d;
        int deg = st.getUInt16();
        if (deg > 180 || (deg > 90 && isLatitude)) {
            throw st.exception("Invalid LOC " + type + " degrees");
        }
        String s = st.getString();
        try {
            min = Integer.parseInt(s);
        } catch (NumberFormatException e) {
        }
        if (min < 0 || min > 59) {
            throw st.exception("Invalid LOC " + type + " minutes");
        }
        sec = parseFixedPoint(st.getString());
        if (sec < 0.0d || sec >= 60.0d) {
            throw st.exception("Invalid LOC " + type + " seconds");
        }
        s = st.getString();
        if (s.length() != 1) {
            throw st.exception("Invalid LOC " + type);
        }
        long value = (long) ((((min + (deg * 60)) * 60) + sec) * 1000.0d);
        char c = Character.toUpperCase(s.charAt(0));
        if ((isLatitude && c == 'S') || (!isLatitude && c == 'W')) {
            value = -value;
        } else if ((isLatitude && c != 'N') || (!isLatitude && c != 'E')) {
            throw st.exception("Invalid LOC " + type);
        }
        return value + 2147483648L;
    }

    private long parseDouble(Tokenizer st, String type, boolean required, long min, long max, long defaultValue) throws IOException {
        String s;
        Tokenizer.Token token = st.get();
        if (token.isEOL()) {
            if (required) {
                throw st.exception("Invalid LOC " + type);
            }
            st.unget();
            return defaultValue;
        }
        String s2 = token.value();
        if (s2.length() > 1 && s2.charAt(s2.length() - 1) == 'm') {
            s = s2.substring(0, s2.length() - 1);
        } else {
            s = s2;
        }
        try {
            long value = (long) (parseFixedPoint(s) * 100.0d);
            if (value < min || value > max) {
                throw st.exception("Invalid LOC " + type);
            }
            return value;
        } catch (NumberFormatException e) {
            throw st.exception("Invalid LOC " + type);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.latitude = parsePosition(st, "latitude");
        this.longitude = parsePosition(st, "longitude");
        this.altitude = parseDouble(st, "altitude", true, -10000000L, 4284967295L, 0L) + 10000000;
        this.size = parseDouble(st, "size", false, 0L, 9000000000L, 100L);
        this.hPrecision = parseDouble(st, "horizontal precision", false, 0L, 9000000000L, 1000000L);
        this.vPrecision = parseDouble(st, "vertical precision", false, 0L, 9000000000L, 1000L);
    }

    private void renderFixedPoint(StringBuilder sb, NumberFormat formatter, long value, long divisor) {
        sb.append(value / divisor);
        long value2 = value % divisor;
        if (value2 != 0) {
            sb.append(".");
            sb.append(formatter.format(value2));
        }
    }

    private String positionToString(long value, char pos, char neg) {
        char direction;
        StringBuilder sb = new StringBuilder();
        long temp = value - 2147483648L;
        if (temp < 0) {
            temp = -temp;
            direction = neg;
        } else {
            direction = pos;
        }
        sb.append(temp / 3600000);
        long temp2 = temp % 3600000;
        sb.append(" ");
        sb.append(temp2 / 60000);
        sb.append(" ");
        renderFixedPoint(sb, f259w3, temp2 % 60000, 1000L);
        sb.append(" ");
        sb.append(direction);
        return sb.toString();
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(positionToString(this.latitude, 'N', 'S'));
        sb.append(" ");
        sb.append(positionToString(this.longitude, 'E', 'W'));
        sb.append(" ");
        renderFixedPoint(sb, f258w2, this.altitude - 10000000, 100L);
        sb.append("m ");
        renderFixedPoint(sb, f258w2, this.size, 100L);
        sb.append("m ");
        renderFixedPoint(sb, f258w2, this.hPrecision, 100L);
        sb.append("m ");
        renderFixedPoint(sb, f258w2, this.vPrecision, 100L);
        sb.append("m");
        return sb.toString();
    }

    public double getLatitude() {
        return (this.latitude - 2147483648L) / 3600000.0d;
    }

    public double getLongitude() {
        return (this.longitude - 2147483648L) / 3600000.0d;
    }

    public double getAltitude() {
        return (this.altitude - 10000000) / 100.0d;
    }

    public double getSize() {
        return this.size / 100.0d;
    }

    public double getHPrecision() {
        return this.hPrecision / 100.0d;
    }

    public double getVPrecision() {
        return this.vPrecision / 100.0d;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(0);
        out.writeU8(toLOCformat(this.size));
        out.writeU8(toLOCformat(this.hPrecision));
        out.writeU8(toLOCformat(this.vPrecision));
        out.writeU32(this.latitude);
        out.writeU32(this.longitude);
        out.writeU32(this.altitude);
    }

    private static long parseLOCformat(int b) throws WireParseException {
        long out = b >> 4;
        int exp = b & 15;
        if (out > 9 || exp > 9) {
            throw new WireParseException("Invalid LOC Encoding");
        }
        while (true) {
            int exp2 = exp - 1;
            if (exp > 0) {
                out *= 10;
                exp = exp2;
            } else {
                return out;
            }
        }
    }

    private int toLOCformat(long l) {
        byte exp = 0;
        while (l > 9) {
            exp = (byte) (exp + 1);
            l /= 10;
        }
        return (int) ((l << 4) + (exp & UByte.MAX_VALUE));
    }
}
