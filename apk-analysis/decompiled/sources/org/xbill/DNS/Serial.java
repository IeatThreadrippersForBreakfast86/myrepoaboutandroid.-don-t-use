package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class Serial {
    private static final String ERROR_MESSAGE_SUFFIX = " out of range";
    private static final long MAX32 = 4294967295L;

    private Serial() {
    }

    public static int compare(long serial1, long serial2) {
        if (serial1 < 0 || serial1 > MAX32) {
            throw new IllegalArgumentException(serial1 + ERROR_MESSAGE_SUFFIX);
        }
        if (serial2 < 0 || serial2 > MAX32) {
            throw new IllegalArgumentException(serial2 + ERROR_MESSAGE_SUFFIX);
        }
        long diff = serial1 - serial2;
        if (diff >= MAX32) {
            diff -= 4294967296L;
        }
        return (int) diff;
    }

    public static long increment(long serial) {
        if (serial < 0 || serial > MAX32) {
            throw new IllegalArgumentException(serial + ERROR_MESSAGE_SUFFIX);
        }
        if (serial == MAX32) {
            return 1L;
        }
        return 1 + serial;
    }
}
