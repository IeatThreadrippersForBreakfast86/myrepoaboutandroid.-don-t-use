package org.xbill.DNS.utils;

import kotlin.UByte;

/* loaded from: classes8.dex */
public class hexdump {
    private static final char[] hex = "0123456789ABCDEF".toCharArray();

    public static String dump(String description, byte[] b, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(length).append("b");
        if (description != null) {
            sb.append(" (").append(description).append(")");
        }
        sb.append(':');
        int prefixlen = (sb.toString().length() + 8) & (-8);
        sb.append('\t');
        int perline = (80 - prefixlen) / 3;
        for (int i = 0; i < length; i++) {
            if (i != 0 && i % perline == 0) {
                sb.append('\n');
                for (int j = 0; j < prefixlen / 8; j++) {
                    sb.append('\t');
                }
            }
            int j2 = i + offset;
            int value = b[j2] & UByte.MAX_VALUE;
            sb.append(hex[value >> 4]);
            sb.append(hex[value & 15]);
            sb.append(' ');
        }
        sb.append('\n');
        return sb.toString();
    }

    public static String dump(String s, byte[] b) {
        return dump(s, b, 0, b.length);
    }
}
