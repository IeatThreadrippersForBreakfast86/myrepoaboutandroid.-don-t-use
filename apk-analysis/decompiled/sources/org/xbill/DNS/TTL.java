package org.xbill.DNS;

import org.xbill.DNS.WKSRecord;

/* loaded from: classes8.dex */
public final class TTL {
    public static final long MAX_VALUE = 2147483647L;

    private TTL() {
    }

    static void check(long i) {
        if (i < 0 || i > MAX_VALUE) {
            throw new InvalidTTLException(i);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0067 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0064 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static long parse(String s, boolean clamp) {
        if (s == null || s.isEmpty() || !Character.isDigit(s.charAt(0))) {
            throw new NumberFormatException();
        }
        long value = 0;
        long ttl = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            long oldvalue = value;
            if (Character.isDigit(c)) {
                long value2 = (10 * value) + Character.getNumericValue(c);
                if (value2 < oldvalue) {
                    throw new NumberFormatException();
                }
                value = value2;
            } else {
                switch (Character.toUpperCase(c)) {
                    case WKSRecord.Service.BOOTPC /* 68 */:
                        value *= 24;
                        value *= 60;
                        value *= 60;
                        ttl += value;
                        value = 0;
                        if (ttl > 4294967295L) {
                            throw new NumberFormatException();
                        }
                        break;
                    case WKSRecord.Service.NETRJS_2 /* 72 */:
                        value *= 60;
                        value *= 60;
                        ttl += value;
                        value = 0;
                        if (ttl > 4294967295L) {
                        }
                        break;
                    case 'M':
                        value *= 60;
                        ttl += value;
                        value = 0;
                        if (ttl > 4294967295L) {
                        }
                        break;
                    case 'S':
                        ttl += value;
                        value = 0;
                        if (ttl > 4294967295L) {
                        }
                        break;
                    case 'W':
                        value *= 7;
                        value *= 24;
                        value *= 60;
                        value *= 60;
                        ttl += value;
                        value = 0;
                        if (ttl > 4294967295L) {
                        }
                        break;
                    default:
                        throw new NumberFormatException();
                }
            }
        }
        if (ttl == 0) {
            ttl = value;
        }
        if (ttl > 4294967295L) {
            throw new NumberFormatException();
        }
        if (ttl > MAX_VALUE && clamp) {
            return MAX_VALUE;
        }
        return ttl;
    }

    public static long parseTTL(String s) {
        return parse(s, true);
    }

    public static String format(long ttl) {
        check(ttl);
        StringBuilder sb = new StringBuilder();
        long secs = ttl % 60;
        long ttl2 = ttl / 60;
        long mins = ttl2 % 60;
        long ttl3 = ttl2 / 60;
        long hours = ttl3 % 24;
        long ttl4 = ttl3 / 24;
        long days = ttl4 % 7;
        long ttl5 = ttl4 / 7;
        if (ttl5 > 0) {
            sb.append(ttl5).append("W");
        }
        if (days > 0) {
            sb.append(days).append("D");
        }
        if (hours > 0) {
            sb.append(hours).append("H");
        }
        if (mins > 0) {
            sb.append(mins).append("M");
        }
        if (secs > 0 || (ttl5 == 0 && days == 0 && hours == 0 && mins == 0)) {
            sb.append(secs).append("S");
        }
        return sb.toString();
    }
}
