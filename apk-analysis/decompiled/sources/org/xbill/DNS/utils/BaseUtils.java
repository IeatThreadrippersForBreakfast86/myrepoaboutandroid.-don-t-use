package org.xbill.DNS.utils;

/* loaded from: classes8.dex */
final class BaseUtils {
    private BaseUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static String wrapLines(String s, int lineLength, String prefix, boolean addClose) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            sb.append(prefix);
            if (i + lineLength >= s.length()) {
                sb.append(s.substring(i));
                if (addClose) {
                    sb.append(" )");
                }
            } else {
                sb.append((CharSequence) s, i, i + lineLength);
                sb.append("\n");
            }
            i += lineLength;
        }
        return sb.toString();
    }
}
