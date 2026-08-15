package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class RPRecord extends Record {
    private Name mailbox;
    private Name textDomain;

    RPRecord() {
    }

    public RPRecord(Name name, int dclass, long ttl, Name mailbox, Name textDomain) {
        super(name, 17, dclass, ttl);
        this.mailbox = checkName("mailbox", mailbox);
        this.textDomain = checkName("textDomain", textDomain);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.mailbox = new Name(in);
        this.textDomain = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.mailbox = st.getName(origin);
        this.textDomain = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return this.mailbox + " " + this.textDomain;
    }

    public Name getMailbox() {
        return this.mailbox;
    }

    public Name getTextDomain() {
        return this.textDomain;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.mailbox.toWire(out, null, canonical);
        this.textDomain.toWire(out, null, canonical);
    }
}
