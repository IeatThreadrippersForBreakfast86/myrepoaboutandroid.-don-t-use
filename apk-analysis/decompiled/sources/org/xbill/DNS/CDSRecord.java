package org.xbill.DNS;

/* loaded from: classes8.dex */
public class CDSRecord extends DSRecord {
    CDSRecord() {
    }

    public CDSRecord(Name name, int dclass, long ttl, int footprint, int alg, int digestid, byte[] digest) {
        super(name, 59, dclass, ttl, footprint, alg, digestid, digest);
    }

    public CDSRecord(Name name, int dclass, long ttl, int digestid, DNSKEYRecord key) {
        super(name, 59, dclass, ttl, key.getFootprint(), key.getAlgorithm(), digestid, DNSSEC.generateDSDigest(key, digestid));
    }
}
