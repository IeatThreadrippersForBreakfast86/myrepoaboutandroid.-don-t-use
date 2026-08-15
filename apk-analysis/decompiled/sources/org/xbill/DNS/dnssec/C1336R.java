package org.xbill.DNS.dnssec;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* renamed from: org.xbill.DNS.dnssec.R */
/* loaded from: classes8.dex */
public final class C1336R {

    /* renamed from: rb */
    private static ResourceBundle f283rb;
    private static boolean useNeutral;

    private C1336R() {
    }

    public static void setBundle(ResourceBundle resourceBundle) {
        f283rb = resourceBundle;
    }

    public static void setUseNeutralMessages(boolean useNeutral2) {
        useNeutral = useNeutral2;
    }

    public static String get(String key, Object... values) {
        if (useNeutral) {
            return getNeutral(key, values);
        }
        try {
            if (f283rb == null) {
                f283rb = ResourceBundle.getBundle("messages");
            }
            return MessageFormat.format(f283rb.getString(key), values);
        } catch (MissingResourceException e) {
            return getNeutral(key, values);
        }
    }

    private static String getNeutral(String key, Object[] values) {
        StringBuilder sb = new StringBuilder(key);
        for (Object val : values) {
            sb.append(":");
            sb.append(val);
        }
        return sb.toString();
    }
}
