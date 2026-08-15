package org.xbill.DNS.lookup;

import org.xbill.DNS.Name;

/* loaded from: classes8.dex */
public class NoSuchRRSetException extends LookupFailedException {
    public NoSuchRRSetException(Name name, int type) {
        this(name, type, false);
    }

    NoSuchRRSetException(Name name, int type, boolean isAuthenticated) {
        super(null, null, name, type, isAuthenticated);
    }
}
