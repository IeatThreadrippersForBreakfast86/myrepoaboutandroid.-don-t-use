package org.xbill.DNS.dnssec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;

/* loaded from: classes8.dex */
final class DefaultTrustAnchorStore implements TrustAnchorStore {
    private final Map<String, RRset> map = new HashMap();

    @Override // org.xbill.DNS.dnssec.TrustAnchorStore
    public void store(final RRset rrset) {
        if (rrset.getType() != 43 && rrset.getType() != 48) {
            throw new IllegalArgumentException("Trust anchors can only be DS or DNSKEY records");
        }
        if (rrset.getType() == 48) {
            SRRset temp = new SRRset();
            for (Record r : rrset.rrs()) {
                DNSKEYRecord key = (DNSKEYRecord) r;
                DSRecord ds = new DSRecord(key.getName(), key.getDClass(), key.getTTL(), 4, key);
                temp.addRR(ds);
            }
            rrset = temp;
        }
        String k = key(rrset.getName(), rrset.getDClass());
        RRset previous = this.map.put(k, rrset);
        if (previous != null) {
            List<Record> listRrs = previous.rrs();
            Objects.requireNonNull(rrset);
            listRrs.forEach(new Consumer() { // from class: org.xbill.DNS.dnssec.DefaultTrustAnchorStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    rrset.addRR((Record) obj);
                }
            });
        }
    }

    @Override // org.xbill.DNS.dnssec.TrustAnchorStore
    public RRset find(Name name, int dclass) {
        while (name.labels() > 0) {
            String k = key(name, dclass);
            RRset r = lookup(k);
            if (r != null) {
                return r;
            }
            name = new Name(name, 1);
        }
        return null;
    }

    @Override // org.xbill.DNS.dnssec.TrustAnchorStore
    public void clear() {
        this.map.clear();
    }

    @Override // org.xbill.DNS.dnssec.TrustAnchorStore
    public Collection<RRset> items() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    private RRset lookup(String key) {
        return this.map.get(key);
    }

    private String key(Name n, int dclass) {
        return "T" + dclass + "/" + n.canonicalize();
    }
}
