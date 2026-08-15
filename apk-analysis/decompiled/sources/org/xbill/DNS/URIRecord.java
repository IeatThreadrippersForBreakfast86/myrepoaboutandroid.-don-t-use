package org.xbill.DNS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes8.dex */
public class URIRecord extends Record {
    private int priority;
    private byte[] target;
    private int weight;

    URIRecord() {
        this.target = new byte[0];
    }

    public URIRecord(Name name, int dclass, long ttl, int priority, int weight, String target) {
        super(name, 256, dclass, ttl);
        this.priority = checkU16("priority", priority);
        this.weight = checkU16("weight", weight);
        try {
            this.target = byteArrayFromString(target);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.priority = in.readU16();
        this.weight = in.readU16();
        this.target = in.readByteArray();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.priority = st.getUInt16();
        this.weight = st.getUInt16();
        try {
            this.target = byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.priority).append(" ");
        sb.append(this.weight).append(" ");
        sb.append(byteArrayToString(this.target, true));
        return sb.toString();
    }

    public int getPriority() {
        return this.priority;
    }

    public int getWeight() {
        return this.weight;
    }

    @Deprecated
    public String getTarget() {
        return byteArrayToString(this.target, false);
    }

    public URI getTargetAsURI() throws URISyntaxException {
        return new URI(new String(this.target, StandardCharsets.UTF_8));
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.priority);
        out.writeU16(this.weight);
        out.writeByteArray(this.target);
    }
}
