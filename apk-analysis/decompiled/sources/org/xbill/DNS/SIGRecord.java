package org.xbill.DNS;

import java.time.Instant;
import java.util.Date;

/* loaded from: classes8.dex */
public class SIGRecord extends SIGBase {
    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ int getAlgorithm() {
        return super.getAlgorithm();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ Instant getExpire() {
        return super.getExpire();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ int getFootprint() {
        return super.getFootprint();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ int getLabels() {
        return super.getLabels();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ long getOrigTTL() {
        return super.getOrigTTL();
    }

    @Override // org.xbill.DNS.SIGBase, org.xbill.DNS.Record
    public /* bridge */ /* synthetic */ int getRRsetType() {
        return super.getRRsetType();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ byte[] getSignature() {
        return super.getSignature();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ Name getSigner() {
        return super.getSigner();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ Instant getTimeSigned() {
        return super.getTimeSigned();
    }

    @Override // org.xbill.DNS.SIGBase
    public /* bridge */ /* synthetic */ int getTypeCovered() {
        return super.getTypeCovered();
    }

    SIGRecord() {
    }

    public SIGRecord(Name name, int dclass, long ttl, int covered, int alg, long origttl, Instant expire, Instant timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, 24, dclass, ttl, covered, alg, origttl, expire, timeSigned, footprint, signer, signature);
    }

    @Deprecated
    public SIGRecord(Name name, int dclass, long ttl, int covered, int alg, long origttl, Date expire, Date timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, 24, dclass, ttl, covered, alg, origttl, expire.toInstant(), timeSigned.toInstant(), footprint, signer, signature);
    }
}
