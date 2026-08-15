package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class ExtendedFlags {

    /* renamed from: CO */
    public static final int f245CO = 32769;

    /* renamed from: DO */
    public static final int f246DO = 32768;
    private static final Mnemonic extflags = new Mnemonic("EDNS Flag", 3);

    static {
        extflags.setMaximum(65535);
        extflags.setPrefix("FLAG");
        extflags.setNumericAllowed(true);
        extflags.add(32768, "do");
        extflags.add(32769, "co");
    }

    private ExtendedFlags() {
    }

    public static String string(int i) {
        return extflags.getText(i);
    }

    public static String stringFromBit(int bit) {
        return extflags.getText(1 << (15 - bit));
    }

    public static int value(String s) {
        return extflags.getValue(s);
    }
}
