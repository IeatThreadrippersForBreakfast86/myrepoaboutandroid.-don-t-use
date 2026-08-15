package org.xbill.DNS;

/* loaded from: classes8.dex */
public class SMIMEARecord extends TLSARecord {
    SMIMEARecord() {
    }

    public SMIMEARecord(Name name, int dclass, long ttl, int certificateUsage, int selector, int matchingType, byte[] certificateAssociationData) {
        super(name, 53, dclass, ttl, certificateUsage, selector, matchingType, certificateAssociationData);
    }
}
