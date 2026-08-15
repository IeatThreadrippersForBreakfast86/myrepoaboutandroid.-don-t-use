package org.xbill.DNS;

/* loaded from: classes8.dex */
public class InvalidTypeException extends IllegalArgumentException {
    public InvalidTypeException(int type) {
        super("Invalid DNS type: " + type);
    }
}
