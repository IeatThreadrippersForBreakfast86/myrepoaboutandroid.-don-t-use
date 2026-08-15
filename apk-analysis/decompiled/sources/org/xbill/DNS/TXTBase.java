package org.xbill.DNS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer;

/* loaded from: classes8.dex */
abstract class TXTBase extends Record {
    protected List<byte[]> strings;

    protected TXTBase() {
    }

    protected TXTBase(Name name, int type, int dclass, long ttl) {
        super(name, type, dclass, ttl);
    }

    protected TXTBase(Name name, int type, int dclass, long ttl, List<String> strings) {
        super(name, type, dclass, ttl);
        if (strings == null) {
            throw new IllegalArgumentException("strings must not be null");
        }
        this.strings = new ArrayList(strings.size());
        for (String s : strings) {
            try {
                this.strings.add(byteArrayFromString(s));
            } catch (TextParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    protected TXTBase(Name name, int type, int dclass, long ttl, String string) {
        this(name, type, dclass, ttl, (List<String>) Collections.singletonList(string));
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        this.strings = new ArrayList(2);
        while (in.remaining() > 0) {
            byte[] b = in.readCountedString();
            this.strings.add(b);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.strings = new ArrayList(2);
        while (true) {
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                try {
                    this.strings.add(byteArrayFromString(t.value()));
                } catch (TextParseException e) {
                    throw st.exception(e.getMessage());
                }
            } else {
                st.unget();
                return;
            }
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        if (this.strings.isEmpty()) {
            return "\"\"";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<byte[]> it = this.strings.iterator();
        while (it.hasNext()) {
            byte[] array = it.next();
            sb.append(byteArrayToString(array, true));
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public List<String> getStrings(boolean escape) {
        List<String> list = new ArrayList<>(this.strings.size());
        for (byte[] string : this.strings) {
            list.add(escape ? byteArrayToString(string, false) : new String(string, StandardCharsets.UTF_8));
        }
        return list;
    }

    public List<String> getStrings() {
        return getStrings(true);
    }

    public List<byte[]> getStringsAsByteArrays() {
        return this.strings;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        for (byte[] b : this.strings) {
            out.writeCountedString(b);
        }
    }
}
