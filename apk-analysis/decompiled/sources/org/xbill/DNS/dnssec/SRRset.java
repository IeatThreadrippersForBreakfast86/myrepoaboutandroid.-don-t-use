package org.xbill.DNS.dnssec;

import java.util.List;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;

/* loaded from: classes8.dex */
class SRRset extends RRset {
    private Name ownerName;
    private SecurityStatus securityStatus;

    @Override // org.xbill.DNS.RRset
    protected boolean canEqual(Object other) {
        return other instanceof SRRset;
    }

    @Override // org.xbill.DNS.RRset
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SRRset)) {
            return false;
        }
        SRRset other = (SRRset) o;
        if (!other.canEqual(this) || !super.equals(o)) {
            return false;
        }
        Object this$securityStatus = getSecurityStatus();
        Object other$securityStatus = other.getSecurityStatus();
        if (this$securityStatus != null ? !this$securityStatus.equals(other$securityStatus) : other$securityStatus != null) {
            return false;
        }
        Object this$ownerName = this.ownerName;
        Object other$ownerName = other.ownerName;
        return this$ownerName != null ? this$ownerName.equals(other$ownerName) : other$ownerName == null;
    }

    @Override // org.xbill.DNS.RRset
    public int hashCode() {
        int result = super.hashCode();
        Object $securityStatus = getSecurityStatus();
        int result2 = (result * 59) + ($securityStatus == null ? 43 : $securityStatus.hashCode());
        Object $ownerName = this.ownerName;
        return (result2 * 59) + ($ownerName != null ? $ownerName.hashCode() : 43);
    }

    public SRRset() {
        this.securityStatus = SecurityStatus.UNCHECKED;
    }

    public SRRset(Record r) {
        super(r);
        this.securityStatus = SecurityStatus.UNCHECKED;
    }

    public SRRset(RRset r) {
        super(r);
        this.securityStatus = SecurityStatus.UNCHECKED;
    }

    public SRRset(SRRset r) {
        super((RRset) r);
        this.securityStatus = r.securityStatus;
        this.ownerName = r.ownerName;
    }

    public SecurityStatus getSecurityStatus() {
        return this.securityStatus;
    }

    public void setSecurityStatus(SecurityStatus status) {
        this.securityStatus = status;
    }

    public Name getSignerName() {
        List<RRSIGRecord> sigs = sigs();
        if (!sigs.isEmpty()) {
            return sigs.get(0).getSigner();
        }
        return null;
    }

    @Override // org.xbill.DNS.RRset
    public Name getName() {
        return this.ownerName == null ? super.getName() : this.ownerName;
    }

    public void setName(Name ownerName) {
        this.ownerName = ownerName;
    }
}
