package org.xbill.DNS;

import java.security.PublicKey;
import org.xbill.DNS.DNSSEC;

/* loaded from: classes8.dex */
public class CDNSKEYRecord extends DNSKEYRecord {
    CDNSKEYRecord() {
    }

    public CDNSKEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, 60, dclass, ttl, flags, proto, alg, key);
    }

    public CDNSKEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, PublicKey key) throws DNSSEC.DNSSECException {
        super(name, 60, dclass, ttl, flags, proto, alg, DNSSEC.fromPublicKey(key, alg));
    }
}
