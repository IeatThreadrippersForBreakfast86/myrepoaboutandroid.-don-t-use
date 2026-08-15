package org.xbill.DNS.lookup;

import org.xbill.DNS.Name;

/* loaded from: classes8.dex */
public class NoSuchDomainException extends LookupFailedException {
    public NoSuchDomainException(Name name, int type) {
        this(name, type, false);
    }

    NoSuchDomainException(Name name, int type, boolean isAuthenticated) {
        super(null, null, name, type, isAuthenticated);
    }
}
