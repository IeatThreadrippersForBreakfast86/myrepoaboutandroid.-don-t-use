package org.xbill.DNS.dnssec;

import kotlin.UByte;

/* loaded from: classes8.dex */
final class ByteArrayComparator {
    private static final int MAX_BYTE = 255;

    private ByteArrayComparator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int compare(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return b1.length - b2.length;
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return (b1[i] & UByte.MAX_VALUE) - (b2[i] & UByte.MAX_VALUE);
            }
        }
        return 0;
    }
}
