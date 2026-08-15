package org.xbill.DNS;

import java.util.List;

/* loaded from: classes8.dex */
public class TXTRecord extends TXTBase {
    @Override // org.xbill.DNS.TXTBase
    public /* bridge */ /* synthetic */ List getStrings() {
        return super.getStrings();
    }

    @Override // org.xbill.DNS.TXTBase
    public /* bridge */ /* synthetic */ List getStrings(boolean z) {
        return super.getStrings(z);
    }

    @Override // org.xbill.DNS.TXTBase
    public /* bridge */ /* synthetic */ List getStringsAsByteArrays() {
        return super.getStringsAsByteArrays();
    }

    TXTRecord() {
    }

    public TXTRecord(Name name, int dclass, long ttl, List<String> strings) {
        super(name, 16, dclass, ttl, strings);
    }

    public TXTRecord(Name name, int dclass, long ttl, String string) {
        super(name, 16, dclass, ttl, string);
    }
}
