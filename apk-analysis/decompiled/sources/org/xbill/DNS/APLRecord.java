package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class APLRecord extends Record {
    private List<Element> elements;

    public static class Element implements Serializable {
        public final Object address;
        public final int family;
        public final boolean negative;
        public final int prefixLength;

        private Element(int family, boolean negative, Object address, int prefixLength) {
            this.family = family;
            this.negative = negative;
            this.address = address;
            this.prefixLength = prefixLength;
            if (!APLRecord.validatePrefixLength(family, prefixLength)) {
                throw new IllegalArgumentException("invalid prefix length");
            }
        }

        public Element(boolean negative, InetAddress address, int prefixLength) {
            this(Address.familyOf(address), negative, address, prefixLength);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.negative) {
                sb.append("!");
            }
            sb.append(this.family);
            sb.append(":");
            if (this.family == 1 || this.family == 2) {
                sb.append(((InetAddress) this.address).getHostAddress());
            } else {
                sb.append(base16.toString((byte[]) this.address));
            }
            sb.append("/");
            sb.append(this.prefixLength);
            return sb.toString();
        }

        public boolean equals(Object arg) {
            if (!(arg instanceof Element)) {
                return false;
            }
            Element elt = (Element) arg;
            return this.family == elt.family && this.negative == elt.negative && this.prefixLength == elt.prefixLength && this.address.equals(elt.address);
        }

        public int hashCode() {
            return this.address.hashCode() + this.prefixLength + (this.negative ? 1 : 0);
        }
    }

    APLRecord() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean validatePrefixLength(int family, int prefixLength) {
        if (prefixLength < 0 || prefixLength >= 256) {
            return false;
        }
        if (family == 1 && prefixLength > 32) {
            return false;
        }
        if (family == 2 && prefixLength > 128) {
            return false;
        }
        return true;
    }

    public APLRecord(Name name, int dclass, long ttl, List<Element> elements) {
        super(name, 42, dclass, ttl);
        this.elements = new ArrayList(elements.size());
        for (Element element : elements) {
            if (element.family != 1 && element.family != 2) {
                throw new IllegalArgumentException("unknown family");
            }
            this.elements.add(element);
        }
    }

    private static byte[] parseAddress(byte[] in, int length) throws WireParseException {
        if (in.length > length) {
            throw new WireParseException("invalid address length");
        }
        if (in.length == length) {
            return in;
        }
        byte[] out = new byte[length];
        System.arraycopy(in, 0, out, 0, in.length);
        return out;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        Element element;
        this.elements = new ArrayList(1);
        while (in.remaining() != 0) {
            int family = in.readU16();
            int prefix = in.readU8();
            int length = in.readU8();
            boolean negative = (length & 128) != 0;
            byte[] data = in.readByteArray(length & (-129));
            if (!validatePrefixLength(family, prefix)) {
                throw new WireParseException("invalid prefix length");
            }
            if (family == 1 || family == 2) {
                InetAddress addr = InetAddress.getByAddress(parseAddress(data, Address.addressLength(family)));
                element = new Element(negative, addr, prefix);
            } else {
                element = new Element(family, negative, data, prefix);
            }
            this.elements.add(element);
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException, NumberFormatException {
        int start;
        boolean negative;
        APLRecord aPLRecord = this;
        int i = 1;
        aPLRecord.elements = new ArrayList(1);
        while (true) {
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                String s = t.value();
                if (!s.startsWith("!")) {
                    start = 0;
                    negative = false;
                } else {
                    start = 1;
                    negative = true;
                }
                int colon = s.indexOf(58, start);
                if (colon < 0) {
                    throw st.exception("invalid address prefix element");
                }
                int slash = s.indexOf(47, colon);
                if (slash < 0) {
                    throw st.exception("invalid address prefix element");
                }
                String familyString = s.substring(start, colon);
                String addressString = s.substring(colon + 1, slash);
                String prefixString = s.substring(slash + 1);
                try {
                    int family = Integer.parseInt(familyString);
                    if (family != i && family != 2) {
                        throw st.exception("unknown family");
                    }
                    try {
                        int prefix = Integer.parseInt(prefixString);
                        if (!validatePrefixLength(family, prefix)) {
                            throw st.exception("invalid prefix length");
                        }
                        byte[] bytes = Address.toByteArray(addressString, family);
                        if (bytes == null) {
                            throw st.exception("invalid IP address " + addressString);
                        }
                        InetAddress address = InetAddress.getByAddress(bytes);
                        aPLRecord.elements.add(new Element(negative, address, prefix));
                        i = 1;
                        aPLRecord = this;
                    } catch (NumberFormatException e) {
                        throw st.exception("invalid prefix length");
                    }
                } catch (NumberFormatException e2) {
                    throw st.exception("invalid family");
                }
            } else {
                st.unget();
                return;
            }
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Element> it = this.elements.iterator();
        while (it.hasNext()) {
            Element element = it.next();
            sb.append(element);
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public List<Element> getElements() {
        return this.elements;
    }

    private static int addressLength(byte[] addr) {
        for (int i = addr.length - 1; i >= 0; i--) {
            if (addr[i] != 0) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        int length;
        byte[] data;
        for (Element element : this.elements) {
            if (element.family == 1 || element.family == 2) {
                InetAddress addr = (InetAddress) element.address;
                byte[] data2 = addr.getAddress();
                int length2 = addressLength(data2);
                length = length2;
                data = data2;
            } else {
                data = (byte[]) element.address;
                length = data.length;
            }
            int wlength = length;
            if (element.negative) {
                wlength |= 128;
            }
            out.writeU16(element.family);
            out.writeU8(element.prefixLength);
            out.writeU8(wlength);
            out.writeByteArray(data, 0, length);
        }
    }
}
