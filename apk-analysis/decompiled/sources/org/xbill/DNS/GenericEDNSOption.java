package org.xbill.DNS;

import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class GenericEDNSOption extends EDNSOption {
    private byte[] data;

    GenericEDNSOption(int code) {
        super(code);
    }

    public GenericEDNSOption(int code, byte[] data) {
        super(code);
        this.data = Record.checkByteArrayLength("option data", data, 65535);
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) {
        this.data = in.readByteArray();
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(DNSOutput out) {
        out.writeByteArray(this.data);
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        return "<" + base16.toString(this.data) + ">";
    }
}
