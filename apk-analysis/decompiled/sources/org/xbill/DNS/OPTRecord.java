package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kotlin.UByte;
import org.xbill.DNS.EDNSOption;

/* loaded from: classes8.dex */
public class OPTRecord extends Record {
    private List<EDNSOption> options;

    OPTRecord() {
    }

    public OPTRecord(int payloadSize, int xrcode, int version, int flags, EDNSOption... options) {
        this(payloadSize, xrcode, version, flags);
        if (options != null) {
            this.options = new ArrayList(Arrays.asList(options));
        }
    }

    public OPTRecord(int payloadSize, int xrcode, int version, int flags, List<EDNSOption> options) {
        this(payloadSize, xrcode, version, flags);
        if (options != null) {
            this.options = new ArrayList(options);
        }
    }

    public OPTRecord(int payloadSize, int xrcode, int version, int flags) {
        super(Name.root, 41, payloadSize, 0L);
        checkU16("payloadSize", payloadSize);
        checkU8("xrcode", xrcode);
        checkU8("version", version);
        checkU16("flags", flags);
        this.ttl = (xrcode << 24) + (version << 16) + flags;
    }

    public OPTRecord(int payloadSize, int xrcode, int version) {
        this(payloadSize, xrcode, version, 0);
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        if (in.remaining() > 0) {
            this.options = new ArrayList();
        }
        while (in.remaining() > 0) {
            EDNSOption option = EDNSOption.fromWire(in);
            this.options.add(option);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for OPT");
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        if (this.options != null) {
            sb.append(this.options);
            sb.append(" ");
        }
        sb.append(" ; payload ");
        sb.append(getPayloadSize());
        sb.append(", xrcode ");
        sb.append(getExtendedRcode());
        sb.append(", version ");
        sb.append(getVersion());
        sb.append(", flags ");
        sb.append(getFlags());
        return sb.toString();
    }

    @Override // org.xbill.DNS.Record
    public String toString() {
        return Name.root + "\t\t\t\t" + Type.string(this.type) + "\t" + rrToString();
    }

    void printPseudoSection(StringBuilder sb) {
        sb.append(";; OPT PSEUDOSECTION: \n; EDNS: version: ");
        sb.append(getVersion());
        sb.append("; flags: ");
        for (int i = 0; i < 16; i++) {
            if ((getFlags() & (1 << (15 - i))) != 0) {
                sb.append(ExtendedFlags.stringFromBit(i));
                sb.append(" ");
            }
        }
        sb.append("; udp: ").append(getPayloadSize());
        if (this.options != null) {
            for (EDNSOption o : this.options) {
                sb.append("\n; ").append(EDNSOption.Code.string(o.getCode())).append(": ").append(o.optionToString());
            }
        }
    }

    public int getPayloadSize() {
        return this.dclass;
    }

    public int getExtendedRcode() {
        return (int) (this.ttl >>> 24);
    }

    public int getVersion() {
        return (int) ((this.ttl >>> 16) & 255);
    }

    public int getFlags() {
        return (int) (this.ttl & 65535);
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        if (this.options == null) {
            return;
        }
        for (EDNSOption option : this.options) {
            option.toWire(out);
        }
    }

    public List<EDNSOption> getOptions() {
        if (this.options == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.options);
    }

    public List<EDNSOption> getOptions(int code) {
        if (this.options == null) {
            return Collections.emptyList();
        }
        List<EDNSOption> list = new ArrayList<>();
        for (EDNSOption opt : this.options) {
            if (opt.getCode() == code) {
                list.add(opt);
            }
        }
        return list;
    }

    @Override // org.xbill.DNS.Record
    public boolean equals(Object arg) {
        return super.equals(arg) && this.ttl == ((OPTRecord) arg).ttl;
    }

    @Override // org.xbill.DNS.Record
    public int hashCode() {
        byte[] array = toWireCanonical();
        int code = 0;
        for (byte b : array) {
            code += (code << 3) + (b & UByte.MAX_VALUE);
        }
        return code;
    }
}
