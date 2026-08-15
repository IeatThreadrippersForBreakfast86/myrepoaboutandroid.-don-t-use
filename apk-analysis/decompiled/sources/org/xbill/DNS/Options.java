package org.xbill.DNS;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: classes8.dex */
public final class Options {
    private static Map<String, String> table;

    static {
        try {
            refresh();
        } catch (SecurityException e) {
        }
    }

    private Options() {
    }

    public static void refresh() {
        String s = System.getProperty("dnsjava.options");
        if (s != null) {
            StringTokenizer st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int index = token.indexOf(61);
                if (index == -1) {
                    set(token);
                } else {
                    String option = token.substring(0, index);
                    String value = token.substring(index + 1);
                    set(option, value);
                }
            }
        }
    }

    public static void clear() {
        table = null;
    }

    public static void set(String option) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(option.toLowerCase(), "true");
    }

    public static void set(String option, String value) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(option.toLowerCase(), value.toLowerCase());
    }

    public static void unset(String option) {
        if (table == null) {
            return;
        }
        table.remove(option.toLowerCase());
    }

    public static boolean check(String option) {
        return (table == null || table.get(option.toLowerCase()) == null) ? false : true;
    }

    public static String value(String option) {
        if (table == null) {
            return null;
        }
        return table.get(option.toLowerCase());
    }

    public static int intValue(String option) throws NumberFormatException {
        String s = value(option);
        if (s != null) {
            try {
                int val = Integer.parseInt(s);
                if (val > 0) {
                    return val;
                }
                return -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    static boolean multiline() {
        return (table == null || table.get("multiline") == null) ? false : true;
    }
}
