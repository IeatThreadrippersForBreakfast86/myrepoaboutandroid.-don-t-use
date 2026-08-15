package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes8.dex */
public class HINFORecord extends Record {
    private byte[] cpu;

    /* renamed from: os */
    private byte[] f256os;

    HINFORecord() {
    }

    public HINFORecord(Name name, int dclass, long ttl, String cpu, String os) {
        super(name, 13, dclass, ttl);
        try {
            this.cpu = byteArrayFromString(cpu);
            this.f256os = byteArrayFromString(os);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.cpu = in.readCountedString();
        this.f256os = in.readCountedString();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        try {
            this.cpu = byteArrayFromString(st.getString());
            this.f256os = byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    public String getCPU(boolean escape) {
        return escape ? byteArrayToString(this.cpu, false) : new String(this.cpu, StandardCharsets.UTF_8);
    }

    public String getCPU() {
        return getCPU(true);
    }

    public byte[] getCPUAsByteArray() {
        return this.cpu;
    }

    public String getOS(boolean escape) {
        return escape ? byteArrayToString(this.f256os, false) : new String(this.f256os, StandardCharsets.UTF_8);
    }

    public String getOS() {
        return getOS(true);
    }

    public byte[] getOSAsByteArray() {
        return this.f256os;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.cpu);
        out.writeCountedString(this.f256os);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        return byteArrayToString(this.cpu, true) + " " + byteArrayToString(this.f256os, true);
    }
}
