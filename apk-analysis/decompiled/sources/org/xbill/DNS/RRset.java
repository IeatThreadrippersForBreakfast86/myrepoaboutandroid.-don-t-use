package org.xbill.DNS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: classes8.dex */
public class RRset implements Serializable, Iterable<Record> {
    private short position;
    private final ArrayList<Record> rrs;
    private final ArrayList<RRSIGRecord> sigs;
    private long ttl;

    protected boolean canEqual(Object other) {
        return other instanceof RRset;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RRset)) {
            return false;
        }
        RRset other = (RRset) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$rrs = this.rrs;
        Object other$rrs = other.rrs;
        if (this$rrs != null ? !this$rrs.equals(other$rrs) : other$rrs != null) {
            return false;
        }
        Object this$sigs = this.sigs;
        Object other$sigs = other.sigs;
        return this$sigs != null ? this$sigs.equals(other$sigs) : other$sigs == null;
    }

    public int hashCode() {
        Object $rrs = this.rrs;
        int result = (1 * 59) + ($rrs == null ? 43 : $rrs.hashCode());
        Object $sigs = this.sigs;
        return (result * 59) + ($sigs != null ? $sigs.hashCode() : 43);
    }

    public RRset() {
        this.rrs = new ArrayList<>(1);
        this.sigs = new ArrayList<>(0);
    }

    public RRset(Record r) {
        this();
        addRR(r);
    }

    public RRset(Record... records) {
        this();
        Objects.requireNonNull(records);
        for (Record r : records) {
            addRR(r);
        }
    }

    public RRset(Iterable<Record> records) {
        this();
        Objects.requireNonNull(records);
        for (Record r : records) {
            addRR(r);
        }
    }

    public RRset(RRset rrset) {
        this.rrs = new ArrayList<>(rrset.rrs);
        this.sigs = new ArrayList<>(rrset.sigs);
        this.position = rrset.position;
        this.ttl = rrset.ttl;
    }

    public void addRR(RRSIGRecord r) {
        addRR(r, this.sigs);
    }

    public void addRR(Record r) {
        if (r instanceof RRSIGRecord) {
            addRR((RRSIGRecord) r, this.sigs);
        } else {
            addRR(r, this.rrs);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <X extends Record> void addRR(X r, List<X> list) {
        if (this.sigs.isEmpty() && this.rrs.isEmpty()) {
            list.add(r);
            this.ttl = r.getTTL();
            return;
        }
        checkSameRRset(r, this.rrs);
        checkSameRRset(r, this.sigs);
        Record recordCloneRecord = r;
        if (recordCloneRecord.getTTL() > this.ttl) {
            recordCloneRecord = r.cloneRecord();
            recordCloneRecord.setTTL(this.ttl);
        } else if (recordCloneRecord.getTTL() < this.ttl) {
            this.ttl = recordCloneRecord.getTTL();
            adjustTtl(recordCloneRecord.getTTL(), this.rrs);
            adjustTtl(recordCloneRecord.getTTL(), this.sigs);
        }
        if (!list.contains(recordCloneRecord)) {
            list.add(recordCloneRecord);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <X extends Record> void adjustTtl(long ttl, List<X> list) {
        for (int i = 0; i < list.size(); i++) {
            Record recordCloneRecord = ((Record) list.get(i)).cloneRecord();
            recordCloneRecord.setTTL(ttl);
            list.set(i, recordCloneRecord);
        }
    }

    private void checkSameRRset(Record r, List<? extends Record> rs) {
        if (!rs.isEmpty() && !r.sameRRset(rs.get(0))) {
            throw new IllegalArgumentException("record does not match rrset");
        }
    }

    public void deleteRR(RRSIGRecord r) {
        this.sigs.remove(r);
    }

    public void deleteRR(Record r) {
        if (r instanceof RRSIGRecord) {
            this.sigs.remove(r);
        } else {
            this.rrs.remove(r);
        }
    }

    public void clear() {
        this.rrs.clear();
        this.sigs.clear();
    }

    public List<Record> rrs(boolean cycle) {
        if (!cycle || this.rrs.size() <= 1) {
            return Collections.unmodifiableList(this.rrs);
        }
        List<Record> l = new ArrayList<>(this.rrs.size());
        if (this.position == Short.MAX_VALUE) {
            this.position = (short) 0;
        }
        short s = this.position;
        this.position = (short) (s + 1);
        int start = s % this.rrs.size();
        l.addAll(this.rrs.subList(start, this.rrs.size()));
        l.addAll(this.rrs.subList(0, start));
        return l;
    }

    public List<Record> rrs() {
        return rrs(true);
    }

    public List<RRSIGRecord> sigs() {
        return Collections.unmodifiableList(this.sigs);
    }

    public int size() {
        return this.rrs.size();
    }

    public int sigSize() {
        return this.sigs.size();
    }

    public boolean isEmpty() {
        return this.rrs.isEmpty() && this.sigs.isEmpty();
    }

    public Name getName() {
        return first().getName();
    }

    public int getType() {
        return first().getRRsetType();
    }

    int getActualType() {
        return first().getType();
    }

    public int getDClass() {
        return first().getDClass();
    }

    public long getTTL() {
        return first().getTTL();
    }

    public Record first() {
        if (!this.rrs.isEmpty()) {
            return this.rrs.get(0);
        }
        if (!this.sigs.isEmpty()) {
            return this.sigs.get(0);
        }
        throw new IllegalStateException("rrset is empty");
    }

    private void appendRrList(Iterator<? extends Record> it, StringBuilder sb) {
        while (it.hasNext()) {
            Record rr = it.next();
            sb.append("[");
            sb.append(rr.rdataToString());
            sb.append("]");
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
    }

    public String toString() {
        if (this.rrs.isEmpty() && this.sigs.isEmpty()) {
            return "{empty}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(getName()).append(" ");
        sb.append(getTTL()).append(" ");
        sb.append(DClass.string(getDClass())).append(" ");
        sb.append(Type.string(getType())).append(" ");
        appendRrList(this.rrs.iterator(), sb);
        if (!this.sigs.isEmpty()) {
            sb.append(" sigs: ");
            appendRrList(this.sigs.iterator(), sb);
        }
        sb.append(" }");
        return sb.toString();
    }

    @Override // java.lang.Iterable
    public Iterator<Record> iterator() {
        return rrs().iterator();
    }
}
