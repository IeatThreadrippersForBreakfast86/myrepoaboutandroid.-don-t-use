package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import kotlin.UByte;

/* loaded from: classes8.dex */
public class base16 {
    private static final String BASE_16_CHARS = "0123456789ABCDEF";

    private base16() {
    }

    public static String toString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte item : b) {
            short value = (short) (item & UByte.MAX_VALUE);
            byte high = (byte) (value >> 4);
            byte low = (byte) (value & 15);
            sb.append(BASE_16_CHARS.charAt(high));
            sb.append(BASE_16_CHARS.charAt(low));
        }
        return sb.toString();
    }

    public static String toString(byte[] b, int lineLength, String prefix, boolean addClose) {
        return BaseUtils.wrapLines(toString(b), lineLength, prefix, addClose);
    }

    public static byte[] fromString(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new byte[0];
        }
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F')) {
                bs.write(c);
            } else if (c >= 'a' && c <= 'f') {
                bs.write(c - ' ');
            } else if (!Character.isWhitespace(c)) {
                return null;
            }
        }
        byte[] in = bs.toByteArray();
        if ((in.length & 1) != 0) {
            return null;
        }
        bs.reset();
        for (int i2 = 0; i2 < in.length; i2 += 2) {
            byte high = (byte) BASE_16_CHARS.indexOf(in[i2]);
            byte low = (byte) BASE_16_CHARS.indexOf(in[i2 + 1]);
            bs.write((high << 4) + (low & 15));
        }
        return bs.toByteArray();
    }
}
