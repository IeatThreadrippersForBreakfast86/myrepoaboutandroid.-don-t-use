package org.xbill.DNS;

/* loaded from: classes8.dex */
public class RelativeNameException extends IllegalArgumentException {
    public RelativeNameException(Name name) {
        super("'" + name + "' is not an absolute name");
    }

    public RelativeNameException(String s) {
        super(s);
    }
}
