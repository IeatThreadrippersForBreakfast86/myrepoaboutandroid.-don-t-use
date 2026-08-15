package org.xbill.DNS;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/* loaded from: classes8.dex */
class Mnemonic {
    static final int CASE_LOWER = 3;
    static final int CASE_SENSITIVE = 1;
    static final int CASE_UPPER = 2;
    private final String description;
    private boolean numericok;
    private String prefix;
    private final int wordcase;
    private final HashMap<String, Integer> strings = new HashMap<>();
    private final HashMap<Integer, String> values = new HashMap<>();
    private int max = Integer.MAX_VALUE;

    public Mnemonic(String description, int wordcase) {
        this.description = description;
        this.wordcase = wordcase;
    }

    public void setMaximum(int max) {
        this.max = max;
    }

    public void setPrefix(String prefix) {
        this.prefix = sanitize(prefix);
    }

    public void setNumericAllowed(boolean numeric) {
        this.numericok = numeric;
    }

    public void check(int val) {
        if (val < 0 || val > this.max) {
            throw new IllegalArgumentException(this.description + " " + val + " is out of range");
        }
    }

    private String sanitize(String str) {
        if (this.wordcase == 2) {
            return str.toUpperCase();
        }
        if (this.wordcase == 3) {
            return str.toLowerCase();
        }
        return str;
    }

    private int parseNumeric(String s) throws NumberFormatException {
        try {
            int val = Integer.parseInt(s);
            if (val < 0) {
                return -1;
            }
            if (val <= this.max) {
                return val;
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void add(int val, String str) {
        check(val);
        String str2 = sanitize(str);
        this.strings.put(str2, Integer.valueOf(val));
        this.values.put(Integer.valueOf(val), str2);
    }

    public void remove(final int val) {
        this.values.remove(Integer.valueOf(val));
        this.strings.entrySet().removeIf(new Predicate() { // from class: org.xbill.DNS.Mnemonic$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Mnemonic.lambda$remove$0(val, (Map.Entry) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$remove$0(int val, Map.Entry entry) {
        return ((Integer) entry.getValue()).intValue() == val;
    }

    public void addAlias(int val, String str) {
        check(val);
        this.strings.put(sanitize(str), Integer.valueOf(val));
    }

    public void removeAlias(String str) {
        this.strings.remove(sanitize(str));
    }

    public void addAll(Mnemonic source) {
        if (this.wordcase != source.wordcase) {
            throw new IllegalArgumentException(source.description + ": wordcases do not match");
        }
        this.strings.putAll(source.strings);
        this.values.putAll(source.values);
    }

    public String getText(int val) {
        check(val);
        String str = this.values.get(Integer.valueOf(val));
        if (str != null) {
            return str;
        }
        String str2 = Integer.toString(val);
        if (this.prefix != null) {
            return this.prefix + str2;
        }
        return str2;
    }

    public int getValue(String str) {
        int val;
        String str2 = sanitize(str);
        Integer value = this.strings.get(str2);
        if (value != null) {
            return value.intValue();
        }
        if (this.prefix != null && str2.startsWith(this.prefix) && (val = parseNumeric(str2.substring(this.prefix.length()))) >= 0) {
            return val;
        }
        if (this.numericok) {
            return parseNumeric(str2);
        }
        return -1;
    }
}
