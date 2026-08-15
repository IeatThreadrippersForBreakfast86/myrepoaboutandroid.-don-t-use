package org.xbill.DNS;

import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.Cache;

/* loaded from: classes8.dex */
public class SetResponse {
    private List<RRset> data;
    private boolean isAuthenticated;
    private final SetResponseType type;
    private static final SetResponse SR_UNKNOWN = new SetResponse(SetResponseType.UNKNOWN, null, false);
    private static final SetResponse SR_UNKNOWN_AUTH = new SetResponse(SetResponseType.UNKNOWN, null, true);
    private static final SetResponse SR_NXDOMAIN = new SetResponse(SetResponseType.NXDOMAIN, null, false);
    private static final SetResponse SR_NXDOMAIN_AUTH = new SetResponse(SetResponseType.NXDOMAIN, null, true);
    private static final SetResponse SR_NXRRSET = new SetResponse(SetResponseType.NXRRSET, null, false);
    private static final SetResponse SR_NXRRSET_AUTH = new SetResponse(SetResponseType.NXRRSET, null, true);

    boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    private SetResponse(SetResponseType type, RRset rrset, boolean isAuthenticated) {
        this.type = type;
        this.isAuthenticated = isAuthenticated;
        if (rrset != null) {
            addRRset(rrset);
        }
    }

    static SetResponse ofType(SetResponseType type) {
        return ofType(type, null, false);
    }

    static SetResponse ofType(SetResponseType type, RRset rrset) {
        return ofType(type, rrset, false);
    }

    static SetResponse ofType(SetResponseType type, Cache.CacheRRset rrset) {
        return ofType(type, rrset, rrset.isAuthenticated());
    }

    static SetResponse ofType(SetResponseType type, RRset rrset, boolean isAuthenticated) {
        switch (type) {
            case UNKNOWN:
                return isAuthenticated ? SR_UNKNOWN_AUTH : SR_UNKNOWN;
            case NXDOMAIN:
                return isAuthenticated ? SR_NXDOMAIN_AUTH : SR_NXDOMAIN;
            case NXRRSET:
                return isAuthenticated ? SR_NXRRSET_AUTH : SR_NXRRSET;
            case DELEGATION:
            case CNAME:
            case DNAME:
            case SUCCESSFUL:
                return new SetResponse(type, rrset, isAuthenticated);
            default:
                throw new IllegalArgumentException("invalid type");
        }
    }

    void addRRset(RRset rrset) {
        if (this.type.isSealed()) {
            throw new IllegalStateException("Attempted to add RRset to sealed response of type " + this.type);
        }
        if (this.data == null) {
            this.data = new ArrayList();
            if (rrset instanceof Cache.CacheRRset) {
                this.isAuthenticated = ((Cache.CacheRRset) rrset).isAuthenticated();
            }
        } else if ((rrset instanceof Cache.CacheRRset) && this.isAuthenticated) {
            this.isAuthenticated = ((Cache.CacheRRset) rrset).isAuthenticated();
        }
        this.data.add(rrset);
    }

    public boolean isUnknown() {
        return this.type == SetResponseType.UNKNOWN;
    }

    public boolean isNXDOMAIN() {
        return this.type == SetResponseType.NXDOMAIN;
    }

    public boolean isNXRRSET() {
        return this.type == SetResponseType.NXRRSET;
    }

    public boolean isDelegation() {
        return this.type == SetResponseType.DELEGATION;
    }

    public boolean isCNAME() {
        return this.type == SetResponseType.CNAME;
    }

    public boolean isDNAME() {
        return this.type == SetResponseType.DNAME;
    }

    public boolean isSuccessful() {
        return this.type == SetResponseType.SUCCESSFUL;
    }

    public List<RRset> answers() {
        if (this.type != SetResponseType.SUCCESSFUL) {
            return null;
        }
        return this.data;
    }

    public CNAMERecord getCNAME() {
        return (CNAMERecord) this.data.get(0).first();
    }

    public DNAMERecord getDNAME() {
        return (DNAMERecord) this.data.get(0).first();
    }

    public RRset getNS() {
        if (this.data != null) {
            return this.data.get(0);
        }
        return null;
    }

    public String toString() {
        return this.type + (this.type.isPrintRecords() ? ": " + this.data.get(0) : "");
    }
}
