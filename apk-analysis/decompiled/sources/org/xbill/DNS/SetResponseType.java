package org.xbill.DNS;

/* loaded from: classes8.dex */
enum SetResponseType {
    UNKNOWN(false, true),
    NXDOMAIN(false, true),
    NXRRSET(false, true),
    DELEGATION(true, false),
    CNAME(true, false),
    DNAME(true, false),
    SUCCESSFUL(false, false);

    private final boolean isSealed;
    private final boolean printRecords;

    SetResponseType(boolean printRecords, boolean isSealed) {
        this.printRecords = printRecords;
        this.isSealed = isSealed;
    }

    public boolean isPrintRecords() {
        return this.printRecords;
    }

    public boolean isSealed() {
        return this.isSealed;
    }
}
