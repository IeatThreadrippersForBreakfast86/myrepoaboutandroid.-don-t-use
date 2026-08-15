package org.xbill.DNS.dnssec;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
final class KeyEntry extends SRRset {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) KeyEntry.class);
    private final List<Integer> algo;
    private String badReason;
    private int edeReason;
    private boolean isEmpty;

    @Override // org.xbill.DNS.dnssec.SRRset, org.xbill.DNS.RRset
    protected boolean canEqual(Object other) {
        return other instanceof KeyEntry;
    }

    @Override // org.xbill.DNS.dnssec.SRRset, org.xbill.DNS.RRset
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof KeyEntry)) {
            return false;
        }
        KeyEntry other = (KeyEntry) o;
        if (!other.canEqual(this) || !super.equals(o) || this.edeReason != other.edeReason || this.isEmpty != other.isEmpty) {
            return false;
        }
        Object this$badReason = this.badReason;
        Object other$badReason = other.badReason;
        return this$badReason != null ? this$badReason.equals(other$badReason) : other$badReason == null;
    }

    @Override // org.xbill.DNS.dnssec.SRRset, org.xbill.DNS.RRset
    public int hashCode() {
        int result = super.hashCode();
        int result2 = ((result * 59) + this.edeReason) * 59;
        int i = this.isEmpty ? 79 : 97;
        Object $badReason = this.badReason;
        return ((result2 + i) * 59) + ($badReason == null ? 43 : $badReason.hashCode());
    }

    public List<Integer> getAlgo() {
        return this.algo;
    }

    private KeyEntry(SRRset rrset) {
        this(rrset, null);
    }

    private KeyEntry(SRRset rrset, List<Integer> sigalg) {
        super(rrset);
        this.edeReason = -1;
        this.algo = sigalg;
    }

    private KeyEntry(Name name, int dclass, long ttl, boolean isBad) {
        super(new SRRset(Record.newRecord(name, 48, dclass, ttl)));
        this.edeReason = -1;
        this.isEmpty = true;
        this.algo = null;
        if (isBad) {
            setSecurityStatus(SecurityStatus.BOGUS);
        }
    }

    public static KeyEntry newKeyEntry(SRRset rrset) {
        return new KeyEntry(rrset);
    }

    public static KeyEntry newKeyEntry(SRRset rrset, List<Integer> sigalg) {
        return new KeyEntry(rrset, sigalg);
    }

    public static KeyEntry newNullKeyEntry(Name n, int dclass, long ttl) {
        return new KeyEntry(n, dclass, ttl, false);
    }

    public static KeyEntry newBadKeyEntry(Name n, int dclass, long ttl) {
        return new KeyEntry(n, dclass, ttl, true);
    }

    public boolean isNull() {
        return this.isEmpty && getSecurityStatus() == SecurityStatus.UNCHECKED;
    }

    public boolean isBad() {
        return this.isEmpty && getSecurityStatus() == SecurityStatus.BOGUS;
    }

    public boolean isGood() {
        return !this.isEmpty && getSecurityStatus() == SecurityStatus.SECURE;
    }

    public void setBadReason(int edeReason, String reason) {
        this.edeReason = edeReason;
        this.badReason = reason;
    }

    JustifiedSecStatus validateKeyFor(SRRset set) {
        Name signerName = set.getSignerName();
        if (signerName == null) {
            if (set.getType() == 5 && set.getSecurityStatus() == SecurityStatus.SECURE) {
                return new JustifiedSecStatus(set.getSecurityStatus(), -1, null);
            }
            log.debug("No signerName for <{}/{}/{}>", set.getName(), DClass.string(set.getDClass()), Type.string(set.getType()));
            if (isNull()) {
                String reason = this.badReason;
                if (reason == null) {
                    reason = C1336R.get("validate.insecure_unsigned", new Object[0]);
                }
                return new JustifiedSecStatus(SecurityStatus.INSECURE, this.edeReason, reason);
            }
            if (isGood()) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 10, C1336R.get("validate.bogus.missingsig", new Object[0]));
            }
            return new JustifiedSecStatus(SecurityStatus.BOGUS, this.edeReason, C1336R.get("validate.bogus", this.badReason));
        }
        if (isBad()) {
            return new JustifiedSecStatus(SecurityStatus.BOGUS, this.edeReason, C1336R.get("validate.bogus.badkey", getName(), this.badReason));
        }
        if (!isNull()) {
            return null;
        }
        String reason2 = this.badReason;
        if (reason2 == null) {
            reason2 = C1336R.get("validate.insecure", new Object[0]);
        }
        return new JustifiedSecStatus(SecurityStatus.INSECURE, this.edeReason, reason2);
    }
}
