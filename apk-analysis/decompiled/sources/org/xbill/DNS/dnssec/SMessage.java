package org.xbill.DNS.dnssec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;

/* loaded from: classes8.dex */
final class SMessage {
    private static final int EXTENDED_FLAGS_BIT_OFFSET = 4;
    private static final int MAX_FLAGS = 16;
    private static final int NUM_SECTIONS = 3;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) SMessage.class);
    private String bogusReason;
    private int edeReason;
    private final Header header;
    private OPTRecord oPTRecord;
    private Record question;
    private final List<SRRset>[] sections;
    private SecurityStatus securityStatus;

    public SMessage(Header h) {
        this.edeReason = -1;
        this.sections = new List[3];
        this.header = h;
        this.securityStatus = SecurityStatus.UNCHECKED;
    }

    public SMessage(int id, Record question) {
        this(new Header(id));
        this.question = question;
    }

    public SMessage(Message m) {
        this(m.getHeader());
        this.question = m.getQuestion();
        this.oPTRecord = m.getOPT();
        for (int i = 1; i <= 3; i++) {
            for (RRset rrset : m.getSectionRRsets(i)) {
                addRRset(new SRRset(rrset), i);
            }
        }
    }

    public Header getHeader() {
        return this.header;
    }

    public Record getQuestion() {
        return this.question;
    }

    public List<SRRset> getSectionRRsets(int section) {
        checkSectionValidity(section);
        if (this.sections[section - 1] == null) {
            this.sections[section - 1] = new LinkedList();
        }
        return this.sections[section - 1];
    }

    private void addRRset(SRRset srrset, int section) {
        checkSectionValidity(section);
        if (srrset.getType() == 41) {
            this.oPTRecord = (OPTRecord) srrset.first();
        } else {
            List<SRRset> sectionList = getSectionRRsets(section);
            sectionList.add(srrset);
        }
    }

    private void checkSectionValidity(int section) {
        if (section <= 0 || section > 3) {
            throw new IllegalArgumentException("Invalid section");
        }
    }

    public List<SRRset> getSectionRRsets(int section, int qtype) {
        List<SRRset> slist = getSectionRRsets(section);
        if (slist.isEmpty()) {
            return Collections.emptyList();
        }
        List<SRRset> result = new ArrayList<>(slist.size());
        for (SRRset rrset : slist) {
            if (rrset.getType() == qtype) {
                result.add(rrset);
            }
        }
        return result;
    }

    public int getRcode() {
        int rcode = this.header.getRcode();
        if (this.oPTRecord != null) {
            return rcode + (this.oPTRecord.getExtendedRcode() << 4);
        }
        return rcode;
    }

    public SecurityStatus getStatus() {
        return this.securityStatus;
    }

    public void setStatus(SecurityStatus status, int edeReason) {
        setStatus(status, edeReason, null);
    }

    public void setStatus(SecurityStatus status, int edeReason, String reason) {
        this.securityStatus = status;
        this.edeReason = edeReason;
        this.bogusReason = reason;
        if (reason != null) {
            log.debug("Setting bad reason for message to {}", reason);
        }
    }

    public void setBogus(String reason) {
        setStatus(SecurityStatus.BOGUS, 6, reason);
    }

    public void setBogus(String reason, int edeReason) {
        setStatus(SecurityStatus.BOGUS, edeReason, reason);
    }

    public String getBogusReason() {
        return this.bogusReason;
    }

    public int getEdeReason() {
        return this.edeReason;
    }

    public Message getMessage() {
        Message m = new Message(this.header.getID());
        Header h = m.getHeader();
        h.setOpcode(this.header.getOpcode());
        h.setRcode(this.header.getRcode());
        for (int i = 0; i < 16; i++) {
            if (Flags.isFlag(i) && this.header.getFlag(i)) {
                h.setFlag(i);
            }
        }
        if (this.question != null) {
            m.addRecord(this.question, 0);
        }
        for (int sec = 1; sec <= 3; sec++) {
            List<SRRset> slist = getSectionRRsets(sec);
            for (SRRset rrset : slist) {
                for (Record j : rrset.rrs()) {
                    m.addRecord(j, sec);
                }
                for (RRSIGRecord j2 : rrset.sigs()) {
                    m.addRecord(j2, sec);
                }
            }
        }
        if (this.oPTRecord != null) {
            m.addRecord(this.oPTRecord, 3);
        }
        return m;
    }

    public int getCount(int section) {
        if (section == 0) {
            return 1;
        }
        List<SRRset> sectionList = getSectionRRsets(section);
        if (sectionList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (SRRset sr : sectionList) {
            count += sr.size();
        }
        return count;
    }

    public SRRset findRRset(Name name, int type, int dclass, int section) {
        checkSectionValidity(section);
        for (SRRset set : getSectionRRsets(section)) {
            if (set.getName().equals(name) && set.getType() == type && set.getDClass() == dclass) {
                return set;
            }
        }
        return null;
    }

    public SRRset findAnswerRRset(Name qname, int qtype, int qclass) {
        return findRRset(qname, qtype, qclass, 1);
    }
}
