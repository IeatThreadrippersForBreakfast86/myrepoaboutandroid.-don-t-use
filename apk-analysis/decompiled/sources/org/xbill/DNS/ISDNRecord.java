package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.xbill.DNS.Tokenizer;

/* loaded from: classes8.dex */
public class ISDNRecord extends Record {
    private byte[] address;
    private byte[] subAddress;

    ISDNRecord() {
    }

    public ISDNRecord(Name name, int dclass, long ttl, String address, String subAddress) {
        super(name, 20, dclass, ttl);
        try {
            this.address = byteArrayFromString(address);
            if (subAddress != null) {
                this.subAddress = byteArrayFromString(subAddress);
            }
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.address = in.readCountedString();
        if (in.remaining() > 0) {
            this.subAddress = in.readCountedString();
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        try {
            this.address = byteArrayFromString(st.getString());
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                this.subAddress = byteArrayFromString(t.value());
            } else {
                st.unget();
            }
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    public String getAddress(boolean escape) {
        return escape ? byteArrayToString(this.address, false) : new String(this.address, StandardCharsets.UTF_8);
    }

    public String getAddress() {
        return getAddress(true);
    }

    public byte[] getAddressAsByteArray() {
        return this.address;
    }

    public String getSubAddress(boolean escape) {
        if (this.subAddress == null) {
            return null;
        }
        if (escape) {
            return byteArrayToString(this.subAddress, false);
        }
        return new String(this.subAddress, StandardCharsets.UTF_8);
    }

    public String getSubAddress() {
        return getSubAddress(true);
    }

    public byte[] getSubAddressAsByteArray() {
        return this.subAddress;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.address);
        if (this.subAddress != null) {
            out.writeCountedString(this.subAddress);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(byteArrayToString(this.address, true));
        if (this.subAddress != null) {
            sb.append(" ");
            sb.append(byteArrayToString(this.subAddress, true));
        }
        return sb.toString();
    }
}
