package org.xbill.DNS;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.xbill.DNS.Zone;

/* loaded from: classes8.dex */
public class Zone implements Serializable, Iterable<RRset> {
    public static final int PRIMARY = 1;
    public static final int SECONDARY = 2;
    private boolean hasWild;
    private RRset nsRRset;
    private Name origin;
    private Object originNode;
    private SOARecord soaRecord;
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
    private final Map<Name, Object> data = new ConcurrentSkipListMap();

    public Name getOrigin() {
        return this.origin;
    }

    public int getDClass() {
        return 1;
    }

    public RRset getNS() {
        return (RRset) withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.m1847lambda$getNS$0$orgxbillDNSZone();
            }
        });
    }

    /* renamed from: lambda$getNS$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ RRset m1847lambda$getNS$0$orgxbillDNSZone() {
        return new RRset(this.nsRRset);
    }

    public SOARecord getSOA() {
        return this.soaRecord;
    }

    public Zone(Name zone, InputStream input) throws IOException {
        if (zone == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        if (input == null) {
            throw new IllegalArgumentException("no input stream specified");
        }
        this.origin = zone;
        fromMasterFile(new Master(input, this.origin));
    }

    public Zone(Name zone, String file) throws IOException {
        if (zone == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        if (file == null) {
            throw new IllegalArgumentException("no file name specified");
        }
        this.origin = zone;
        fromMasterFile(new Master(file, this.origin));
    }

    public Zone(Name zone, Record... records) throws IOException {
        if (zone == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        if (records == null) {
            throw new IllegalArgumentException("no records are specified");
        }
        this.origin = zone;
        for (Record r : records) {
            maybeAddRecord(r);
        }
        validate();
    }

    public Zone(ZoneTransferIn xfrin) throws ZoneTransferException, IOException {
        if (xfrin == null) {
            throw new IllegalArgumentException("no xfrin specified");
        }
        fromXFR(xfrin);
    }

    public Zone(Name zone, int dclass, String remote) throws ZoneTransferException, IOException {
        if (zone == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        DClass.check(dclass);
        ZoneTransferIn xfrin = ZoneTransferIn.newAXFR(zone, remote, (TSIG) null);
        xfrin.setDClass(dclass);
        fromXFR(xfrin);
    }

    private void fromMasterFile(Master m) throws IOException {
        while (true) {
            try {
                Record r = m.nextRecord();
                if (r != null) {
                    maybeAddRecord(r);
                } else {
                    m.close();
                    validate();
                    return;
                }
            } catch (Throwable th) {
                m.close();
                throw th;
            }
        }
    }

    private void fromXFR(ZoneTransferIn xfrin) throws ZoneTransferException, IOException {
        this.origin = xfrin.getName();
        xfrin.run();
        if (!xfrin.isAXFR()) {
            throw new IllegalArgumentException("zones can only be created from AXFRs");
        }
        for (Record r : xfrin.getAXFR()) {
            maybeAddRecord(r);
        }
        validate();
    }

    private void maybeAddRecord(Record r) throws IOException {
        int rtype = r.getType();
        Name name = r.getName();
        if (rtype == 6 && !name.equals(this.origin)) {
            throw new IOException("SOA owner " + name + " does not match zone origin " + this.origin);
        }
        if (name.subdomain(this.origin)) {
            addRecord(r);
        }
    }

    private void validate() throws IOException {
        this.originNode = exactName(this.origin);
        if (this.originNode == null) {
            throw new IOException(this.origin + ": no data specified");
        }
        RRset rrset = oneRRsetWithoutLock(this.originNode, 6);
        if (rrset == null || rrset.size() != 1) {
            throw new IOException(this.origin + ": exactly 1 SOA must be specified");
        }
        this.soaRecord = (SOARecord) rrset.first();
        this.nsRRset = oneRRsetWithoutLock(this.originNode, 2);
        if (this.nsRRset == null) {
            throw new IOException(this.origin + ": no NS set specified");
        }
    }

    @Override // java.lang.Iterable
    public Iterator<RRset> iterator() {
        return new ZoneIterator(false);
    }

    public Iterator<RRset> AXFR() {
        return new ZoneIterator(true);
    }

    public <T extends Record> void addRecord(final T r) {
        if (r == null) {
            throw new IllegalArgumentException("r must not be null");
        }
        final Name name = r.getName();
        final int rtype = r.getRRsetType();
        final int actualType = r.getType();
        if (rtype == 6 && !name.equals(this.origin)) {
            throw new IllegalArgumentException("SOA owner " + name + " does not match zone origin " + this.origin);
        }
        if (!name.subdomain(this.origin)) {
            throw new IllegalArgumentException("name " + name + " is absolute and not a subdomain of " + this.origin);
        }
        withWriteLock(new Runnable() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1844lambda$addRecord$0$orgxbillDNSZone(name, rtype, r, actualType);
            }
        });
    }

    /* renamed from: lambda$addRecord$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ void m1844lambda$addRecord$0$orgxbillDNSZone(Name name, int rtype, Record r, int actualType) {
        RRset rrset = findRRsetWithoutLock(name, rtype);
        if (rrset == null) {
            addRRsetWithoutLock(name, new RRset(r));
            return;
        }
        if (actualType == 6) {
            rrset.deleteRR(this.soaRecord);
            this.soaRecord = (SOARecord) r;
        }
        rrset.addRR(r);
    }

    public void removeRecord(final Record r) {
        if (r == null) {
            throw new IllegalArgumentException("r must not be null");
        }
        final Name name = r.getName();
        final int rtype = r.getRRsetType();
        if (r.getType() == 6) {
            throw new IllegalArgumentException("Cannot remove SOA record");
        }
        withWriteLock(new Runnable() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1849lambda$removeRecord$0$orgxbillDNSZone(name, rtype, r);
            }
        });
    }

    /* renamed from: lambda$removeRecord$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ void m1849lambda$removeRecord$0$orgxbillDNSZone(Name name, int rtype, Record r) {
        RRset rrset = findRRsetWithoutLock(name, rtype);
        if (rrset == null) {
            return;
        }
        if (rtype == 2 && rrset.size() == 1) {
            throw new IllegalArgumentException("Cannot remove all NS");
        }
        if (rrset.size() + rrset.sigSize() > 1) {
            rrset.deleteRR(r);
        } else {
            m1848lambda$removeRRset$0$orgxbillDNSZone(name, rtype);
        }
    }

    public void addRRset(final RRset rrset) {
        if (rrset == null) {
            throw new IllegalArgumentException("rrset must not be null");
        }
        final Name name = rrset.getName();
        final int type = rrset.getType();
        if (type == 6) {
            if (!name.equals(this.origin)) {
                throw new IllegalArgumentException("SOA owner " + name + " does not match zone origin " + this.origin);
            }
            if (rrset.size() != 1) {
                throw new IllegalArgumentException(this.origin + ": exactly 1 SOA must be specified");
            }
        }
        if (!name.subdomain(this.origin)) {
            throw new IllegalArgumentException("name " + name + " is absolute and not a subdomain of " + this.origin);
        }
        withWriteLock(new Runnable() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1843lambda$addRRset$0$orgxbillDNSZone(name, rrset, type);
            }
        });
    }

    /* renamed from: lambda$addRRset$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ void m1843lambda$addRRset$0$orgxbillDNSZone(Name name, RRset rrset, int type) {
        addRRsetWithoutLock(name, rrset);
        if (type == 6) {
            this.soaRecord = (SOARecord) rrset.first();
        }
    }

    public void removeRRset(final Name name, final int type) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        Type.check(type);
        withWriteLock(new Runnable() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1848lambda$removeRRset$0$orgxbillDNSZone(name, type);
            }
        });
    }

    public RRset findExactMatch(final Name name, final int type) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        Type.check(type);
        return (RRset) withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.m1845lambda$findExactMatch$0$orgxbillDNSZone(name, type);
            }
        });
    }

    /* renamed from: lambda$findExactMatch$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ RRset m1845lambda$findExactMatch$0$orgxbillDNSZone(Name name, int type) {
        RRset set = findRRsetWithoutLock(name, type);
        if (set == null) {
            return null;
        }
        return new RRset(set);
    }

    public SetResponse findRecords(final Name name, final int type) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        Type.check(type);
        if (!name.subdomain(this.origin)) {
            return SetResponse.ofType(SetResponseType.NXDOMAIN);
        }
        return (SetResponse) withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.m1846lambda$findRecords$0$orgxbillDNSZone(name, type);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> T withReadLock(Supplier<T> callable) {
        this.readLock.lock();
        try {
            return callable.get();
        } finally {
            this.readLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void withWriteLock(Runnable callable) {
        this.writeLock.lock();
        try {
            callable.run();
        } finally {
            this.writeLock.unlock();
        }
    }

    private Object exactName(Name name) {
        return this.data.get(name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<RRset> allRRsetsWithoutLock(Object types) {
        if (types instanceof List) {
            return (List) types;
        }
        return Collections.singletonList((RRset) types);
    }

    private RRset oneRRsetWithoutLock(Object types, int type) {
        if (type == 255) {
            throw new IllegalArgumentException("Cannot lookup an exact match for type ANY");
        }
        if (types instanceof List) {
            List<RRset> list = (List) types;
            for (RRset set : list) {
                if (set.getType() == type) {
                    return set;
                }
            }
            return null;
        }
        RRset set2 = (RRset) types;
        if (set2.getType() == type) {
            return set2;
        }
        return null;
    }

    private RRset findRRsetWithoutLock(Name name, int type) {
        Object types = exactName(name);
        if (types == null) {
            return null;
        }
        return oneRRsetWithoutLock(types, type);
    }

    private void addRRsetWithoutLock(Name name, RRset rrset) {
        if (!this.hasWild && name.isWild()) {
            this.hasWild = true;
        }
        Object types = this.data.get(name);
        if (types == null) {
            this.data.put(name, rrset);
            return;
        }
        int rtype = rrset.getType();
        if (types instanceof List) {
            List<RRset> list = (List) types;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == rtype) {
                    list.set(i, rrset);
                    return;
                }
            }
            list.add(rrset);
            return;
        }
        RRset set = (RRset) types;
        if (set.getType() == rtype) {
            this.data.put(name, rrset);
            return;
        }
        LinkedList<RRset> list2 = new LinkedList<>();
        list2.add(set);
        list2.add(rrset);
        this.data.put(name, list2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeRRsetWithoutLock, reason: merged with bridge method [inline-methods] */
    public void m1848lambda$removeRRset$0$orgxbillDNSZone(Name name, int type) {
        if (type == 6) {
            throw new IllegalArgumentException("Cannot remove SOA");
        }
        if (type == 2) {
            throw new IllegalArgumentException("Cannot remove all NS");
        }
        Object types = this.data.get(name);
        if (types == null) {
            return;
        }
        if (types instanceof List) {
            List<RRset> list = (List) types;
            int i = 0;
            while (true) {
                if (i >= list.size()) {
                    break;
                }
                RRset set = list.get(i);
                if (set.getType() != type) {
                    i++;
                } else {
                    list.remove(i);
                    break;
                }
            }
            if (list.isEmpty()) {
                this.data.remove(name);
                return;
            }
            return;
        }
        RRset set2 = (RRset) types;
        if (set2.getType() != type) {
            return;
        }
        this.data.remove(name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: findRecordsWithoutLock, reason: merged with bridge method [inline-methods] */
    public SetResponse m1846lambda$findRecords$0$orgxbillDNSZone(Name name, int type) {
        Name tname;
        RRset ns;
        int labels = name.labels();
        int olabels = this.origin.labels();
        int tlabels = olabels;
        while (tlabels <= labels) {
            boolean isOrigin = tlabels == olabels;
            boolean isExact = tlabels == labels;
            if (isOrigin) {
                tname = this.origin;
            } else if (isExact) {
                tname = name;
            } else {
                tname = new Name(name, labels - tlabels);
            }
            Object types = exactName(tname);
            if (types != null) {
                if (!isOrigin && (ns = oneRRsetWithoutLock(types, 2)) != null) {
                    return SetResponse.ofType(SetResponseType.DELEGATION, ns);
                }
                if (isExact && type == 255) {
                    SetResponse sr = SetResponse.ofType(SetResponseType.SUCCESSFUL);
                    for (RRset set : allRRsetsWithoutLock(types)) {
                        sr.addRRset(set);
                    }
                    return sr;
                }
                if (isExact) {
                    RRset rrset = oneRRsetWithoutLock(types, type);
                    if (rrset != null) {
                        return SetResponse.ofType(SetResponseType.SUCCESSFUL, rrset);
                    }
                    RRset rrset2 = oneRRsetWithoutLock(types, 5);
                    if (rrset2 != null) {
                        return SetResponse.ofType(SetResponseType.CNAME, rrset2);
                    }
                } else {
                    RRset rrset3 = oneRRsetWithoutLock(types, 39);
                    if (rrset3 != null) {
                        return SetResponse.ofType(SetResponseType.DNAME, rrset3);
                    }
                }
                if (isExact) {
                    return SetResponse.ofType(SetResponseType.NXRRSET);
                }
            }
            tlabels++;
        }
        if (this.hasWild) {
            for (int i = 0; i < labels - olabels; i++) {
                Name tname2 = name.wild(i + 1);
                Object types2 = exactName(tname2);
                if (types2 != null) {
                    if (type == 255) {
                        SetResponse sr2 = SetResponse.ofType(SetResponseType.SUCCESSFUL);
                        for (RRset set2 : allRRsetsWithoutLock(types2)) {
                            sr2.addRRset(expandSet(set2, name));
                        }
                        return sr2;
                    }
                    RRset rrset4 = oneRRsetWithoutLock(types2, type);
                    if (rrset4 != null) {
                        return SetResponse.ofType(SetResponseType.SUCCESSFUL, expandSet(rrset4, name));
                    }
                }
            }
        }
        return SetResponse.ofType(SetResponseType.NXDOMAIN);
    }

    private RRset expandSet(RRset set, Name tname) {
        RRset expandedSet = new RRset();
        for (Record r : set.rrs(false)) {
            expandedSet.addRR(r.withName(tname));
        }
        for (RRSIGRecord r2 : set.sigs()) {
            expandedSet.addRR(r2.withName(tname));
        }
        return expandedSet;
    }

    private void nodeToString(final StringBuilder sb, Object node) {
        List<RRset> sets = allRRsetsWithoutLock(node);
        for (RRset rrset : sets) {
            rrset.rrs(false).forEach(new Consumer() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    sb.append((Record) obj).append('\n');
                }
            });
            rrset.sigs().forEach(new Consumer() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    sb.append((RRSIGRecord) obj).append('\n');
                }
            });
        }
    }

    public String toMasterFile() {
        final StringBuilder sb = new StringBuilder();
        withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.m1850lambda$toMasterFile$0$orgxbillDNSZone(sb);
            }
        });
        return sb.toString();
    }

    /* renamed from: lambda$toMasterFile$0$org-xbill-DNS-Zone, reason: not valid java name */
    /* synthetic */ Object m1850lambda$toMasterFile$0$orgxbillDNSZone(StringBuilder sb) {
        nodeToString(sb, this.originNode);
        for (Map.Entry<Name, Object> entry : this.data.entrySet()) {
            if (!this.origin.equals(entry.getKey())) {
                nodeToString(sb, entry.getValue());
            }
        }
        return null;
    }

    public String toString() {
        return toMasterFile();
    }

    class ZoneIterator implements Iterator<RRset> {
        private List<RRset> current;
        private int index;
        private RRset returnedSet;
        private RRset soaSet;
        private boolean wantLastSOA;
        private final Iterator<Map.Entry<Name, Object>> zoneEntries;

        ZoneIterator(boolean axfr) {
            this.zoneEntries = Zone.this.data.entrySet().iterator();
            this.wantLastSOA = axfr;
            List<RRset> originSets = (List) Zone.this.withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$ZoneIterator$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    return Zone.ZoneIterator.lambda$new$0(zone);
                }
            });
            RRset[] sortedOriginSets = new RRset[originSets.size()];
            this.current = Arrays.asList(sortedOriginSets);
            int j = 2;
            for (int i = 0; i < originSets.size(); i++) {
                RRset originSet = originSets.get(i);
                int type = originSet.getType();
                if (type == 6) {
                    RRset rRset = new RRset(originSet);
                    this.soaSet = rRset;
                    sortedOriginSets[0] = rRset;
                } else if (type == 2) {
                    sortedOriginSets[1] = new RRset(originSet);
                } else {
                    sortedOriginSets[j] = new RRset(originSet);
                    j++;
                }
            }
        }

        static /* synthetic */ ArrayList lambda$new$0(Zone this$0) {
            return new ArrayList(this$0.allRRsetsWithoutLock(this$0.originNode));
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.current != null || this.wantLastSOA;
        }

        @Override // java.util.Iterator
        public RRset next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            if (this.current == null) {
                this.wantLastSOA = false;
                this.returnedSet = this.soaSet;
                return this.returnedSet;
            }
            List<RRset> list = this.current;
            int i = this.index;
            this.index = i + 1;
            this.returnedSet = new RRset(list.get(i));
            if (this.index == this.current.size()) {
                this.current = null;
                while (true) {
                    if (!this.zoneEntries.hasNext()) {
                        break;
                    }
                    final Map.Entry<Name, Object> entry = this.zoneEntries.next();
                    if (!entry.getKey().equals(Zone.this.origin)) {
                        List<RRset> sets = (List) Zone.this.withReadLock(new Supplier() { // from class: org.xbill.DNS.Zone$ZoneIterator$$ExternalSyntheticLambda2
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return this.f$0.m1851lambda$next$0$orgxbillDNSZone$ZoneIterator(entry);
                            }
                        });
                        if (!sets.isEmpty()) {
                            this.current = sets;
                            this.index = 0;
                            break;
                        }
                    }
                }
            }
            return this.returnedSet;
        }

        /* renamed from: lambda$next$0$org-xbill-DNS-Zone$ZoneIterator, reason: not valid java name */
        /* synthetic */ ArrayList m1851lambda$next$0$orgxbillDNSZone$ZoneIterator(Map.Entry entry) {
            return new ArrayList(Zone.this.allRRsetsWithoutLock(entry.getValue()));
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.returnedSet != null) {
                Zone.this.withWriteLock(new Runnable() { // from class: org.xbill.DNS.Zone$ZoneIterator$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.m1852lambda$remove$0$orgxbillDNSZone$ZoneIterator();
                    }
                });
                return;
            }
            throw new IllegalStateException("Not at an element");
        }

        /* renamed from: lambda$remove$0$org-xbill-DNS-Zone$ZoneIterator, reason: not valid java name */
        /* synthetic */ void m1852lambda$remove$0$orgxbillDNSZone$ZoneIterator() {
            Zone.this.m1848lambda$removeRRset$0$orgxbillDNSZone(this.returnedSet.getName(), this.returnedSet.getType());
        }
    }
}
