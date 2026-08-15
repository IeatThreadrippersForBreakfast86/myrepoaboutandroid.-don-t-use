package org.xbill.DNS;

/* loaded from: classes8.dex */
final class Utils {
    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static boolean isUInt8(int value) {
        return value >= 0 && value <= 255;
    }

    static boolean isUInt8(long value) {
        return value >= 0 && value <= 255;
    }

    static boolean isUInt16(int value) {
        return value >= 0 && value <= 65535;
    }

    static boolean isUInt16(long value) {
        return value >= 0 && value <= 65535;
    }

    static boolean isUInt32(long value) {
        return value >= 0 && value <= 4294967295L;
    }
}
