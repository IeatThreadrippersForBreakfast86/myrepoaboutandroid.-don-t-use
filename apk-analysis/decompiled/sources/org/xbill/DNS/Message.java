package org.xbill.DNS;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes8.dex */
public class Message implements Cloneable {
    public static final int MAXLENGTH = 65535;
    static final int TSIG_FAILED = 4;
    static final int TSIG_INTERMEDIATE = 2;
    static final int TSIG_SIGNED = 3;
    static final int TSIG_UNSIGNED = 0;
    static final int TSIG_VERIFIED = 1;
    private TSIGRecord generatedTsig;
    private Header header;
    private TSIGRecord querytsig;
    private Resolver resolver;
    private List<Record>[] sections;
    int sig0start;
    private int size;
    int tsigState;
    private int tsigerror;
    private TSIG tsigkey;
    int tsigstart;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Message.class);
    private static final Record[] emptyRecordArray = new Record[0];

    private Message(Header header) {
        this.sections = new List[4];
        this.header = header;
    }

    public Message(int id) {
        this(new Header(id));
    }

    public Message() {
        this(new Header());
    }

    public static Message newQuery(Record r) {
        Message m = new Message();
        m.header.setOpcode(0);
        m.header.setFlag(7);
        m.addRecord(r, 0);
        return m;
    }

    public static Message newUpdate(Name zone) {
        return new Update(zone);
    }

    Message(DNSInput in) throws IOException {
        this(new Header(in));
        boolean isUpdate = this.header.getOpcode() == 5;
        boolean truncated = this.header.getFlag(6);
        for (int i = 0; i < 4; i++) {
            try {
                int count = this.header.getCount(i);
                if (count > 0) {
                    this.sections[i] = new ArrayList(count);
                }
                for (int j = 0; j < count; j++) {
                    int pos = in.current();
                    Record rec = Record.fromWire(in, i, isUpdate);
                    this.sections[i].add(rec);
                    if (i == 3) {
                        if (rec.getType() == 250) {
                            this.tsigstart = pos;
                            if (j != count - 1) {
                                throw new WireParseException("TSIG is not the last record in the message");
                            }
                        }
                        if (rec.getType() == 24) {
                            SIGRecord sig = (SIGRecord) rec;
                            if (sig.getTypeCovered() == 0) {
                                this.sig0start = pos;
                            }
                        }
                    }
                }
            } catch (WireParseException e) {
                if (!truncated) {
                    throw e;
                }
            }
        }
        this.size = in.current();
    }

    public Message(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    public Message(ByteBuffer byteBuffer) throws IOException {
        this(new DNSInput(byteBuffer));
    }

    public void setHeader(Header h) {
        this.header = h;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addRecord(Record r, int section) {
        if (this.sections[section] == null) {
            this.sections[section] = new LinkedList();
        }
        this.header.incCount(section);
        this.sections[section].add(r);
    }

    public boolean removeRecord(Record r, int section) {
        Section.check(section);
        if (this.sections[section] != null && this.sections[section].remove(r)) {
            this.header.decCount(section);
            return true;
        }
        return false;
    }

    public void removeAllRecords(int section) {
        Section.check(section);
        this.sections[section] = null;
        this.header.setCount(section, 0);
    }

    public boolean findRecord(Record r, int section) {
        Section.check(section);
        return this.sections[section] != null && this.sections[section].contains(r);
    }

    public boolean findRecord(Record r) {
        for (int i = 1; i <= 3; i++) {
            if (this.sections[i] != null && this.sections[i].contains(r)) {
                return true;
            }
        }
        return false;
    }

    public boolean findRRset(Name name, int type, int section) {
        Type.check(type);
        Section.check(section);
        if (this.sections[section] == null) {
            return false;
        }
        for (int i = 0; i < this.sections[section].size(); i++) {
            Record r = this.sections[section].get(i);
            if (r.getType() == type && name.equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean findRRset(Name name, int type) {
        return findRRset(name, type, 1) || findRRset(name, type, 2) || findRRset(name, type, 3);
    }

    public Record getQuestion() {
        List<Record> l = this.sections[0];
        if (l == null || l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    public TSIGRecord getTSIG() {
        int count = this.header.getCount(3);
        if (count == 0) {
            return null;
        }
        List<Record> l = this.sections[3];
        Record rec = l.get(count - 1);
        if (rec.type != 250) {
            return null;
        }
        return (TSIGRecord) rec;
    }

    TSIGRecord getGeneratedTSIG() {
        return this.generatedTsig;
    }

    public boolean isSigned() {
        return this.tsigState == 3 || this.tsigState == 1 || this.tsigState == 4;
    }

    public boolean isVerified() {
        return this.tsigState == 1;
    }

    public OPTRecord getOPT() {
        for (Record r : getSection(3)) {
            if (r instanceof OPTRecord) {
                return (OPTRecord) r;
            }
        }
        return null;
    }

    public int getRcode() {
        int rcode = this.header.getRcode();
        OPTRecord opt = getOPT();
        if (opt != null) {
            return rcode + (opt.getExtendedRcode() << 4);
        }
        return rcode;
    }

    @Deprecated
    public Record[] getSectionArray(int section) {
        Section.check(section);
        if (this.sections[section] == null) {
            return emptyRecordArray;
        }
        List<Record> l = this.sections[section];
        return (Record[]) l.toArray(new Record[0]);
    }

    public List<Record> getSection(int section) {
        Section.check(section);
        if (this.sections[section] == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.sections[section]);
    }

    public List<RRset> getSectionRRsets(int section) {
        Section.check(section);
        if (this.sections[section] == null) {
            return Collections.emptyList();
        }
        List<RRset> sets = new LinkedList<>();
        for (Record rec : this.sections[section]) {
            int j = sets.size() - 1;
            while (true) {
                if (j >= 0) {
                    RRset set = sets.get(j);
                    if (rec.sameRRset(set)) {
                        set.addRR(rec);
                        break;
                    }
                    j--;
                } else {
                    sets.add(new RRset(rec));
                    break;
                }
            }
        }
        return sets;
    }

    void toWire(DNSOutput out) {
        this.header.toWire(out);
        Compression c = new Compression();
        for (int i = 0; i < this.sections.length; i++) {
            if (this.sections[i] != null) {
                for (Record rec : this.sections[i]) {
                    rec.toWire(out, i, c);
                }
            }
        }
    }

    private int sectionToWire(DNSOutput out, int section, Compression c, int maxLength) {
        int n = this.sections[section].size();
        int pos = out.current();
        int rendered = 0;
        int count = 0;
        Record lastrec = null;
        for (int i = 0; i < n; i++) {
            Record rec = this.sections[section].get(i);
            if (section != 3 || !(rec instanceof OPTRecord)) {
                if (lastrec != null && !rec.sameRRset(lastrec)) {
                    pos = out.current();
                    rendered = count;
                }
                lastrec = rec;
                rec.toWire(out, section, c);
                if (out.current() > maxLength) {
                    out.jump(pos);
                    return n - rendered;
                }
                count++;
            }
        }
        int i2 = n - count;
        return i2;
    }

    private boolean toWire(DNSOutput out, int maxLength) {
        if (maxLength < 12) {
            return false;
        }
        int tempMaxLength = maxLength;
        if (this.tsigkey != null) {
            tempMaxLength -= this.tsigkey.recordLength();
        }
        OPTRecord opt = getOPT();
        byte[] optBytes = null;
        if (opt != null) {
            optBytes = opt.toWire(3);
            tempMaxLength -= optBytes.length;
        }
        int startpos = out.current();
        this.header.toWire(out);
        Compression c = new Compression();
        int flags = this.header.getFlagsByte();
        int additionalCount = 0;
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            }
            if (this.sections[i] != null) {
                int skipped = sectionToWire(out, i, c, tempMaxLength);
                if (skipped != 0 && i != 3) {
                    flags = Header.setFlag(flags, 6, true);
                    out.writeU16At(this.header.getCount(i) - skipped, startpos + 4 + (i * 2));
                    for (int j = i + 1; j < 3; j++) {
                        out.writeU16At(0, startpos + 4 + (j * 2));
                    }
                } else if (i == 3) {
                    additionalCount = this.header.getCount(i) - skipped;
                }
            }
            i++;
        }
        if (optBytes != null) {
            out.writeByteArray(optBytes);
            additionalCount++;
        }
        if (flags != this.header.getFlagsByte()) {
            out.writeU16At(flags, startpos + 2);
        }
        if (additionalCount != this.header.getCount(3)) {
            out.writeU16At(additionalCount, startpos + 10);
        }
        if (this.tsigkey != null) {
            TSIGRecord tsigrec = this.tsigkey.generate(this, out.toByteArray(), this.tsigerror, this.querytsig);
            tsigrec.toWire(out, 3, c);
            this.generatedTsig = tsigrec;
            out.writeU16At(additionalCount + 1, startpos + 10);
        }
        return !Header.getFlag(flags, 6);
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        this.size = out.current();
        return out.toByteArray();
    }

    public byte[] toWire(int maxLength) {
        DNSOutput out = new DNSOutput();
        toWire(out, maxLength);
        this.size = out.current();
        return out.toByteArray();
    }

    public byte[] toWire(int maxLength, boolean truncate) throws MessageSizeExceededException {
        DNSOutput out = new DNSOutput();
        boolean completelyRendered = toWire(out, maxLength);
        if (!completelyRendered && !truncate) {
            throw new MessageSizeExceededException(maxLength);
        }
        this.size = out.current();
        return out.toByteArray();
    }

    public void setTSIG(TSIG key) {
        setTSIG(key, 0, null);
    }

    public void setTSIG(TSIG key, int error, TSIGRecord querytsig) {
        this.tsigkey = key;
        this.tsigerror = error;
        this.querytsig = querytsig;
    }

    public int numBytes() {
        return this.size;
    }

    public String sectionToString(int section) {
        Section.check(section);
        StringBuilder sb = new StringBuilder();
        sectionToString(sb, section);
        return sb.toString();
    }

    private void sectionToString(StringBuilder sb, int i) {
        if (i > 3) {
            return;
        }
        for (Record rec : getSection(i)) {
            if (i == 0) {
                sb.append(";;\t").append(rec.name);
                sb.append(", type = ").append(Type.string(rec.type));
                sb.append(", class = ").append(DClass.string(rec.dclass));
            } else if (!(rec instanceof OPTRecord)) {
                sb.append(rec);
            }
            sb.append("\n");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        OPTRecord opt = getOPT();
        if (opt == null) {
            sb.append(this.header).append('\n');
        } else {
            sb.append(this.header.toStringWithRcode(getRcode())).append("\n\n");
            opt.printPseudoSection(sb);
            sb.append('\n');
        }
        if (isSigned()) {
            sb.append(";; TSIG ");
            if (isVerified()) {
                sb.append("ok");
            } else {
                sb.append("invalid");
            }
            sb.append('\n');
        }
        for (int i = 0; i < 4; i++) {
            if (this.header.getOpcode() != 5) {
                sb.append(";; ").append(Section.longString(i)).append(":\n");
            } else {
                sb.append(";; ").append(Section.updString(i)).append(":\n");
            }
            sectionToString(sb, i);
            sb.append("\n");
        }
        sb.append(";; Message size: ").append(numBytes()).append(" bytes");
        return sb.toString();
    }

    public Message clone() throws CloneNotSupportedException {
        try {
            Message m = (Message) super.clone();
            m.sections = new List[this.sections.length];
            for (int i = 0; i < this.sections.length; i++) {
                if (this.sections[i] != null) {
                    m.sections[i] = new LinkedList(this.sections[i]);
                }
            }
            m.header = this.header.clone();
            if (this.querytsig != null) {
                m.querytsig = (TSIGRecord) this.querytsig.cloneRecord();
            }
            if (this.generatedTsig != null) {
                m.generatedTsig = (TSIGRecord) this.generatedTsig.cloneRecord();
            }
            return m;
        } catch (CloneNotSupportedException $ex) {
            throw $ex;
        }
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public Optional<Resolver> getResolver() {
        return Optional.ofNullable(this.resolver);
    }

    boolean isTypeAllowedInSection(int type, int section) {
        Type.check(type);
        Section.check(section);
        switch (section) {
            case 2:
                if (type == 6 || type == 2 || type == 43 || type == 47 || type == 50) {
                    return true;
                }
                break;
            case 3:
                if (type == 1 || type == 28) {
                    return true;
                }
                break;
        }
        return true ^ Boolean.parseBoolean(System.getProperty("dnsjava.harden_unknown_additional", "true"));
    }

    public Message normalize(Message query) {
        try {
            return normalize(query, false);
        } catch (WireParseException e) {
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Message normalize(Message query, boolean throwOnIrrelevantRecord) throws WireParseException {
        int size;
        int i;
        Name sname;
        int i2;
        if (getRcode() != 0 && getRcode() != 3) {
            return this;
        }
        Name sname2 = query.getQuestion().getName();
        int i3 = 1;
        List<RRset> answerSectionSets = getSectionRRsets(1);
        List<RRset> additionalSectionSets = getSectionRRsets(3);
        List<RRset> authoritySectionSets = getSectionRRsets(2);
        List<RRset>[] cleanedSection = new ArrayList[4];
        cleanedSection[1] = new ArrayList<>();
        cleanedSection[2] = new ArrayList<>();
        cleanedSection[3] = new ArrayList<>();
        boolean hadNsInAuthority = false;
        Name sname3 = sname2;
        int i4 = 0;
        while (i4 < answerSectionSets.size()) {
            RRset rrset = answerSectionSets.get(i4);
            Name oldSname = sname3;
            if (rrset.getType() == 39 && sname3.subdomain(rrset.getName())) {
                if (rrset.size() > i3) {
                    if (throwOnIrrelevantRecord) {
                        throw new WireParseException("Normalization failed in response to <{}/{}/{}> (id {}), found {} entries (instead of just one) in DNAME RRSet <{}/{}>".replace("{}", "%s"));
                    }
                    log.warn("Normalization failed in response to <{}/{}/{}> (id {}), found {} entries (instead of just one) in DNAME RRSet <{}/{}>", sname3, Type.string(query.getQuestion().getType()), DClass.string(query.getQuestion().getDClass()), Integer.valueOf(getHeader().getID()), Integer.valueOf(rrset.size()), rrset.getName(), DClass.string(rrset.getDClass()));
                    return null;
                }
                if (query.getQuestion().getType() != 39) {
                    cleanedSection[i3].add(rrset);
                    RRset nextRRSet = answerSectionSets.size() >= i4 + 2 ? answerSectionSets.get(i4 + 1) : null;
                    DNAMERecord dname = (DNAMERecord) rrset.first();
                    if (nextRRSet != null) {
                        try {
                            if (nextRRSet.getType() == 5 && nextRRSet.getName().equals(sname3)) {
                                Name expected = Name.concatenate(nextRRSet.getName().relativize(dname.getName()), dname.getTarget());
                                if (expected.equals(((CNAMERecord) nextRRSet.first()).getTarget())) {
                                    i2 = i4;
                                    i4 = i2;
                                }
                            }
                        } catch (NameTooLongException e) {
                            e = e;
                            if (!throwOnIrrelevantRecord) {
                            }
                        }
                    }
                    try {
                        Name dnameTarget = sname3.fromDNAME(dname);
                        try {
                            cleanedSection[1].add(new RRset(new CNAMERecord(sname3, dname.getDClass(), 0L, dnameTarget)));
                            sname3 = dnameTarget;
                            if (query.getQuestion().getType() == 255) {
                                while (true) {
                                    i4++;
                                    if (i4 < answerSectionSets.size()) {
                                        RRset rrset2 = answerSectionSets.get(i4);
                                        if (rrset2.getName().equals(oldSname)) {
                                            cleanedSection[1].add(rrset2);
                                        }
                                    }
                                }
                            }
                        } catch (NameTooLongException e2) {
                            e = e2;
                            if (!throwOnIrrelevantRecord) {
                                throw new WireParseException("Normalization failed in response to <{}/{}/{}> (id {}), could not synthesize CNAME for DNAME <{}/{}>".replace("{}", "%s"), e);
                            }
                            log.warn("Normalization failed in response to <{}/{}/{}> (id {}), could not synthesize CNAME for DNAME <{}/{}>", sname3, Type.string(query.getQuestion().getType()), DClass.string(query.getQuestion().getDClass()), Integer.valueOf(getHeader().getID()), rrset.getName(), DClass.string(rrset.getDClass()));
                            return null;
                        }
                    } catch (NameTooLongException e3) {
                        e = e3;
                    }
                }
            } else {
                if (sname3.equals(rrset.getName())) {
                    Name sname4 = sname3;
                    int i5 = i4;
                    if (rrset.getType() == 5 && query.getQuestion().getType() != 5) {
                        if (rrset.size() > 1) {
                            if (throwOnIrrelevantRecord) {
                                throw new WireParseException(String.format("Found {} CNAMEs in <{}/{}> response to <{}/{}/{}> (id {}), removing all but the first".replace("{}", "%s"), Integer.valueOf(rrset.rrs(false).size()), rrset.getName(), DClass.string(rrset.getDClass()), sname4, Type.string(query.getQuestion().getType()), DClass.string(query.getQuestion().getDClass()), Integer.valueOf(getHeader().getID())));
                            }
                            log.warn("Found {} CNAMEs in <{}/{}> response to <{}/{}/{}> (id {}), removing all but the first", Integer.valueOf(rrset.rrs(false).size()), rrset.getName(), DClass.string(rrset.getDClass()), sname4, Type.string(query.getQuestion().getType()), DClass.string(query.getQuestion().getDClass()), Integer.valueOf(getHeader().getID()));
                            List<Record> cnameRRset = rrset.rrs(false);
                            for (int cnameIndex = 1; cnameIndex < cnameRRset.size(); cnameIndex++) {
                                rrset.deleteRR(cnameRRset.get(i5));
                            }
                        }
                        Name sname5 = ((CNAMERecord) rrset.first()).getTarget();
                        cleanedSection[1].add(rrset);
                        if (query.getQuestion().getType() != 255) {
                            i4 = i5;
                            sname3 = sname5;
                        } else {
                            i4 = i5 + 1;
                            while (i4 < answerSectionSets.size()) {
                                RRset rrset3 = answerSectionSets.get(i4);
                                if (!rrset3.getName().equals(oldSname)) {
                                    break;
                                }
                                cleanedSection[1].add(rrset3);
                                i4++;
                            }
                            sname3 = sname5;
                        }
                    } else {
                        int qtype = getQuestion().getType();
                        if (qtype == 255 || rrset.getActualType() == qtype) {
                            i2 = i5;
                            cleanedSection[1].add(rrset);
                            sname3 = sname4;
                            if (sname3.equals(rrset.getName())) {
                                addAdditionalRRset(rrset, additionalSectionSets, cleanedSection[3]);
                            }
                        } else {
                            i2 = i5;
                            logOrThrow(throwOnIrrelevantRecord, "Ignoring irrelevant RRset <{}/{}/{}> in ANSWER section response to <{}/{}/{}> (id {})", rrset, sname4, query);
                            sname3 = sname4;
                        }
                    }
                } else {
                    i2 = i4;
                    logOrThrow(throwOnIrrelevantRecord, "Ignoring irrelevant RRset <{}/{}/{}> in response to <{}/{}/{}> (id {})", rrset, sname3, query);
                    sname3 = sname3;
                }
                i4 = i2;
            }
            i4++;
            i3 = 1;
        }
        int i6 = 0;
        for (RRset rrset4 : authoritySectionSets) {
            switch (rrset4.getType()) {
                case 1:
                case 5:
                case 28:
                case 39:
                    i = i6;
                    sname = sname3;
                    logOrThrow(throwOnIrrelevantRecord, "Ignoring forbidden RRset <{}/{}/{}> in AUTHORITY section response to <{}/{}/{}> (id {})", rrset4, sname3, query);
                    i6 = i;
                    sname3 = sname;
                    break;
                default:
                    i = i6;
                    sname = sname3;
                    if (!isTypeAllowedInSection(rrset4.getType(), 2)) {
                        logOrThrow(throwOnIrrelevantRecord, "Ignoring disallowed RRset <{}/{}/{}> in AUTHORITY section response to <{}/{}/{}> (id {})", rrset4, sname, query);
                    } else {
                        if (rrset4.getType() == 2) {
                            if (!sname.subdomain(rrset4.getName())) {
                                logOrThrow(throwOnIrrelevantRecord, "Ignoring disallowed RRset <{}/{}/{}> in AUTHORITY section response to <{}/{}/{}> (id {}), not a subdomain of the query", rrset4, sname, query);
                            } else if (getRcode() == 3 || (getRcode() == 0 && authoritySectionSets.stream().anyMatch(new Predicate() { // from class: org.xbill.DNS.Message$$ExternalSyntheticLambda0
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    return Message.lambda$normalize$0((RRset) obj);
                                }
                            }) && this.sections[1] == null)) {
                                logOrThrow(throwOnIrrelevantRecord, "Ignoring disallowed RRset <{}/{}/{}> in AUTHORITY section response to <{}/{}/{}> (id {}), NXDOMAIN or NODATA", rrset4, sname, query);
                            } else if (!hadNsInAuthority) {
                                hadNsInAuthority = true;
                            } else {
                                logOrThrow(throwOnIrrelevantRecord, "Ignoring disallowed RRset <{}/{}/{}> in AUTHORITY section response to <{}/{}/{}> (id {}), already seen another NS", rrset4, sname, query);
                            }
                        }
                        cleanedSection[2].add(rrset4);
                        addAdditionalRRset(rrset4, additionalSectionSets, cleanedSection[3]);
                        i6 = i;
                        sname3 = sname;
                        break;
                    }
                    i6 = i;
                    sname3 = sname;
                    break;
            }
        }
        int i7 = i6;
        Message cleanedMessage = new Message(getHeader());
        cleanedMessage.sections[i7] = this.sections[i7];
        int[] iArr = {1, 2, 3};
        while (i6 < 3) {
            int section = iArr[i6];
            cleanedMessage.sections[section] = rrsetListToRecords(cleanedSection[section]);
            Header header = cleanedMessage.getHeader();
            if (cleanedMessage.sections[section] == null) {
                size = i7;
            } else {
                size = cleanedMessage.sections[section].size();
            }
            header.setCount(section, size);
            i6++;
        }
        return cleanedMessage;
    }

    static /* synthetic */ boolean lambda$normalize$0(RRset set) {
        return set.getType() == 6;
    }

    private void logOrThrow(boolean throwOnIrrelevantRecord, String format, RRset rrset, Name sname, Message query) throws WireParseException {
        if (throwOnIrrelevantRecord) {
            throw new WireParseException(String.format(format.replace("{}", "%s"), rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()), sname, DClass.string(query.getQuestion().getDClass()), Type.string(query.getQuestion().getType()), Integer.valueOf(getHeader().getID())));
        }
        log.debug(format, rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()), sname, DClass.string(query.getQuestion().getDClass()), Type.string(query.getQuestion().getType()), Integer.valueOf(getHeader().getID()));
    }

    private List<Record> rrsetListToRecords(List<RRset> rrsets) {
        if (rrsets.isEmpty()) {
            return null;
        }
        List<Record> result = new ArrayList<>(rrsets.size());
        for (RRset set : rrsets) {
            result.addAll(set.rrs(false));
            result.addAll(set.sigs());
        }
        return result;
    }

    private void addAdditionalRRset(RRset rrset, List<RRset> additionalSectionSets, List<RRset> cleanedAdditionalSection) {
        if (!doesTypeHaveAdditionalRecords(rrset.getType())) {
            return;
        }
        for (Record r : rrset.rrs(false)) {
            for (RRset set : additionalSectionSets) {
                if (set.getName().equals(r.getAdditionalName()) && isTypeAllowedInSection(set.getType(), 3)) {
                    cleanedAdditionalSection.add(set);
                }
            }
        }
    }

    private boolean doesTypeHaveAdditionalRecords(int type) {
        switch (type) {
            case 2:
            case 3:
            case 4:
            case 7:
            case 15:
            case 33:
            case 35:
            case 36:
                return true;
            default:
                return false;
        }
    }
}
