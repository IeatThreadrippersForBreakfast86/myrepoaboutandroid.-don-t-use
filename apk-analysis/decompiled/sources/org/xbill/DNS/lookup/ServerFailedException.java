package org.xbill.DNS.lookup;

import org.xbill.DNS.ExtendedErrorCodeOption;
import org.xbill.DNS.Name;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
public class ServerFailedException extends LookupFailedException {
    private final ExtendedErrorCodeOption extendedRcode;

    public ExtendedErrorCodeOption getExtendedRcode() {
        return this.extendedRcode;
    }

    public ServerFailedException() {
        this.extendedRcode = null;
    }

    public ServerFailedException(Name name, int type) {
        super(name, type);
        this.extendedRcode = null;
    }

    public ServerFailedException(Name name, int type, ExtendedErrorCodeOption extendedRcode) {
        super("Lookup for " + name + "/" + Type.string(type) + " failed with " + extendedRcode.getText(), name, type);
        this.extendedRcode = extendedRcode;
    }
}
