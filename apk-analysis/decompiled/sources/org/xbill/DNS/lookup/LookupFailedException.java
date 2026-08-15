package org.xbill.DNS.lookup;

import org.xbill.DNS.Name;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
public class LookupFailedException extends RuntimeException {
    private final boolean isAuthenticated;
    private final Name name;
    private final int type;

    boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public LookupFailedException() {
        this(null, null, null, 0, false);
    }

    public LookupFailedException(String message) {
        this(message, null, null, 0, false);
    }

    LookupFailedException(String message, Throwable inner) {
        this(message, inner, null, 0, false);
    }

    public LookupFailedException(Name name, int type) {
        this("Lookup for " + name + "/" + Type.string(type) + " failed", name, type);
    }

    public LookupFailedException(String message, Name name, int type) {
        this(message, null, name, type, false);
    }

    LookupFailedException(String message, Throwable inner, Name name, int type, boolean isAuthenticated) {
        super(message, inner);
        this.name = name;
        this.type = type;
        this.isAuthenticated = isAuthenticated;
    }

    public Name getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }
}
