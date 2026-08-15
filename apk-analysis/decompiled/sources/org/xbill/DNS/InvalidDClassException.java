package org.xbill.DNS;

/* loaded from: classes8.dex */
public class InvalidDClassException extends IllegalArgumentException {
    public InvalidDClassException(int dclass) {
        super("Invalid DNS class: " + dclass);
    }
}
