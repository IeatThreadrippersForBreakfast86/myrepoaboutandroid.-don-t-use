package org.xbill.DNS.dnssec;

/* loaded from: classes8.dex */
final class JustifiedSecStatus {
    int edeReason;
    String reason;
    SecurityStatus status;

    JustifiedSecStatus(SecurityStatus status, int edeReason, String reason) {
        this.status = status;
        this.edeReason = edeReason;
        this.reason = reason;
    }

    void applyToResponse(SMessage response) {
        response.setStatus(this.status, this.edeReason, this.reason);
    }
}
