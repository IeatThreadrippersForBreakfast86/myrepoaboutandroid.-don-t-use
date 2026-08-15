package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class DClass {
    public static final int ANY = 255;

    /* renamed from: CH */
    public static final int f239CH = 3;
    public static final int CHAOS = 3;
    public static final int HESIOD = 4;

    /* renamed from: HS */
    public static final int f240HS = 4;

    /* renamed from: IN */
    public static final int f241IN = 1;
    public static final int NONE = 254;
    private static final Mnemonic classes = new DClassMnemonic();

    private static class DClassMnemonic extends Mnemonic {
        public DClassMnemonic() {
            super("DClass", 2);
            setPrefix("CLASS");
        }

        @Override // org.xbill.DNS.Mnemonic
        public void check(int val) {
            DClass.check(val);
        }
    }

    static {
        classes.add(1, "IN");
        classes.add(3, "CH");
        classes.addAlias(3, "CHAOS");
        classes.add(4, "HS");
        classes.addAlias(4, "HESIOD");
        classes.add(254, "NONE");
        classes.add(255, "ANY");
    }

    private DClass() {
    }

    public static void check(int i) {
        if (i < 0 || i > 65535) {
            throw new InvalidDClassException(i);
        }
    }

    public static String string(int i) {
        return classes.getText(i);
    }

    public static int value(String s) {
        return classes.getValue(s);
    }
}
