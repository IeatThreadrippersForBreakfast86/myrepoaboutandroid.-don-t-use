package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class Rcode {
    public static final int BADALG = 21;
    public static final int BADCOOKIE = 23;
    public static final int BADKEY = 17;
    public static final int BADMODE = 19;
    public static final int BADNAME = 20;
    public static final int BADSIG = 16;
    public static final int BADTIME = 18;
    public static final int BADTRUNC = 22;
    public static final int BADVERS = 16;
    public static final int FORMERR = 1;
    public static final int NOERROR = 0;
    public static final int NOTAUTH = 9;
    public static final int NOTIMP = 4;

    @Deprecated
    public static final int NOTIMPL = 4;
    public static final int NOTZONE = 10;
    public static final int NXDOMAIN = 3;
    public static final int NXRRSET = 8;
    public static final int REFUSED = 5;
    public static final int SERVFAIL = 2;
    public static final int YXDOMAIN = 6;
    public static final int YXRRSET = 7;
    private static final Mnemonic rcodes = new Mnemonic("DNS Rcode", 2);

    static {
        rcodes.setMaximum(4095);
        rcodes.setPrefix("RESERVED");
        rcodes.setNumericAllowed(true);
        rcodes.add(0, "NOERROR");
        rcodes.add(1, "FORMERR");
        rcodes.add(2, "SERVFAIL");
        rcodes.add(3, "NXDOMAIN");
        rcodes.add(4, "NOTIMP");
        rcodes.addAlias(4, "NOTIMPL");
        rcodes.add(5, "REFUSED");
        rcodes.add(6, "YXDOMAIN");
        rcodes.add(7, "YXRRSET");
        rcodes.add(8, "NXRRSET");
        rcodes.add(9, "NOTAUTH");
        rcodes.add(10, "NOTZONE");
        rcodes.add(16, "BADVERS");
        rcodes.add(17, "BADKEY");
        rcodes.add(18, "BADTIME");
        rcodes.add(19, "BADMODE");
        rcodes.add(20, "BADNAME");
        rcodes.add(21, "BADALG");
        rcodes.add(22, "BADTRUNC");
        rcodes.add(23, "BADCOOKIE");
    }

    private Rcode() {
    }

    public static String string(int i) {
        return rcodes.getText(i);
    }

    public static String TSIGstring(int i) {
        if (i == 16) {
            return "BADSIG";
        }
        return string(i);
    }

    public static int value(String s) {
        if ("BADSIG".equalsIgnoreCase(s)) {
            return 16;
        }
        return rcodes.getValue(s);
    }
}
