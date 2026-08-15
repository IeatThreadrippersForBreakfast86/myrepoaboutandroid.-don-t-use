package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class Flags {

    /* renamed from: AA */
    public static final byte f247AA = 5;

    /* renamed from: AD */
    public static final byte f248AD = 10;

    /* renamed from: CD */
    public static final byte f249CD = 11;

    /* renamed from: CO */
    public static final int f250CO = 32769;

    /* renamed from: DO */
    public static final int f251DO = 32768;
    private static final Mnemonic HEADER_FLAGS = new Mnemonic("DNS Header Flag", 3);

    /* renamed from: QR */
    public static final byte f252QR = 0;

    /* renamed from: RA */
    public static final byte f253RA = 8;

    /* renamed from: RD */
    public static final byte f254RD = 7;

    /* renamed from: TC */
    public static final byte f255TC = 6;

    static {
        HEADER_FLAGS.setMaximum(15);
        HEADER_FLAGS.setPrefix("FLAG");
        HEADER_FLAGS.setNumericAllowed(true);
        HEADER_FLAGS.add(0, "qr");
        HEADER_FLAGS.add(5, "aa");
        HEADER_FLAGS.add(6, "tc");
        HEADER_FLAGS.add(7, "rd");
        HEADER_FLAGS.add(8, "ra");
        HEADER_FLAGS.add(10, "ad");
        HEADER_FLAGS.add(11, "cd");
    }

    private Flags() {
    }

    public static String string(int i) {
        return HEADER_FLAGS.getText(i);
    }

    public static int value(String s) {
        return HEADER_FLAGS.getValue(s);
    }

    public static boolean isFlag(int index) {
        HEADER_FLAGS.check(index);
        return (index < 1 || index > 4) && index < 12;
    }
}
