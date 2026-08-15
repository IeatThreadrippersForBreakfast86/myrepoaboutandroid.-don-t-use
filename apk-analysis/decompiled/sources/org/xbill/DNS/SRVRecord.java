package org.xbill.DNS;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.io.IOException;

/* loaded from: classes8.dex */
public class SRVRecord extends Record {
    private int port;
    private int priority;
    private Name target;
    private int weight;

    SRVRecord() {
    }

    public SRVRecord(Name name, int dclass, long ttl, int priority, int weight, int port, Name target) {
        super(name, 33, dclass, ttl);
        this.priority = checkU16("priority", priority);
        this.weight = checkU16("weight", weight);
        this.port = checkU16("port", port);
        this.target = checkName(TypedValues.AttributesType.S_TARGET, target);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.priority = in.readU16();
        this.weight = in.readU16();
        this.port = in.readU16();
        this.target = new Name(in);
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.priority = st.getUInt16();
        this.weight = st.getUInt16();
        this.port = st.getUInt16();
        this.target = st.getName(origin);
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.priority).append(" ");
        sb.append(this.weight).append(" ");
        sb.append(this.port).append(" ");
        sb.append(this.target);
        return sb.toString();
    }

    public int getPriority() {
        return this.priority;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getPort() {
        return this.port;
    }

    public Name getTarget() {
        return this.target;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.priority);
        out.writeU16(this.weight);
        out.writeU16(this.port);
        this.target.toWire(out, null, canonical);
    }

    @Override // org.xbill.DNS.Record
    public Name getAdditionalName() {
        return this.target;
    }
}
