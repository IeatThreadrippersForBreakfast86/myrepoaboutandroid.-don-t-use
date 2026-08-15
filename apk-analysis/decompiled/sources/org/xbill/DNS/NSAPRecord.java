package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class NSAPRecord extends Record {
    private byte[] address;

    NSAPRecord() {
    }

    private static byte[] checkAndConvertAddress(String address) {
        if (!address.substring(0, 2).equalsIgnoreCase("0x")) {
            return null;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        boolean partial = false;
        int current = 0;
        for (int i = 2; i < address.length(); i++) {
            char c = address.charAt(i);
            if (c != '.') {
                int value = Character.digit(c, 16);
                if (value == -1) {
                    return null;
                }
                if (partial) {
                    current += value;
                    bytes.write(current);
                    partial = false;
                } else {
                    current = value << 4;
                    partial = true;
                }
            }
        }
        if (partial) {
            return null;
        }
        return bytes.toByteArray();
    }

    public NSAPRecord(Name name, int dclass, long ttl, String address) {
        super(name, 22, dclass, ttl);
        this.address = checkAndConvertAddress(address);
        if (this.address == null) {
            throw new IllegalArgumentException("invalid NSAP address " + address);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) {
        this.address = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String addr = st.getString();
        this.address = checkAndConvertAddress(addr);
        if (this.address == null) {
            throw st.exception("invalid NSAP address " + addr);
        }
    }

    @Deprecated
    public String getAddress() {
        return byteArrayToString(this.address, false);
    }

    public byte[] getAddressAsByteArray() {
        return this.address;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.address);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return "0x" + base16.toString(this.address);
    }
}
