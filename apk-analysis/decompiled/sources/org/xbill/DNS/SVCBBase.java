package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.xbill.DNS.SVCBBase;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public abstract class SVCBBase extends Record {
    public static final int ALPN = 1;
    public static final int ECH = 5;

    @Deprecated
    public static final int ECHCONFIG = 5;
    public static final int IPV4HINT = 4;
    public static final int IPV6HINT = 6;
    public static final int MANDATORY = 0;
    public static final int NO_DEFAULT_ALPN = 2;
    public static final int PORT = 3;
    private static final ParameterMnemonic parameters = new ParameterMnemonic();
    protected final Map<Integer, ParameterBase> svcParams;
    protected int svcPriority;
    protected Name targetName;

    protected SVCBBase() {
        this.svcParams = new TreeMap();
    }

    protected SVCBBase(Name name, int type, int dclass, long ttl) {
        super(name, type, dclass, ttl);
        this.svcParams = new TreeMap();
    }

    protected SVCBBase(Name name, int type, int dclass, long ttl, int priority, Name domain, List<ParameterBase> params) {
        super(name, type, dclass, ttl);
        this.svcPriority = priority;
        this.targetName = domain;
        this.svcParams = new TreeMap();
        for (ParameterBase param : params) {
            if (this.svcParams.containsKey(Integer.valueOf(param.getKey()))) {
                throw new IllegalArgumentException("Duplicate SvcParam for key " + param.getKey());
            }
            this.svcParams.put(Integer.valueOf(param.getKey()), param);
        }
    }

    public int getSvcPriority() {
        return this.svcPriority;
    }

    public Name getTargetName() {
        return this.targetName;
    }

    public Set<Integer> getSvcParamKeys() {
        return this.svcParams.keySet();
    }

    public ParameterBase getSvcParamValue(int key) {
        return this.svcParams.get(Integer.valueOf(key));
    }

    private static class ParameterMnemonic extends Mnemonic {
        private final HashMap<Integer, Supplier<ParameterBase>> factories;

        public ParameterMnemonic() {
            super("SVCB/HTTPS Parameters", 3);
            setPrefix("key");
            setNumericAllowed(true);
            setMaximum(65535);
            this.factories = new HashMap<>();
        }

        public void add(int val, String str, Supplier<ParameterBase> factory) {
            super.add(val, str);
            this.factories.put(Integer.valueOf(val), factory);
        }

        public Supplier<ParameterBase> getFactory(int val) {
            return this.factories.get(Integer.valueOf(val));
        }
    }

    static {
        parameters.add(0, "mandatory", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterMandatory();
            }
        });
        parameters.add(1, "alpn", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterAlpn();
            }
        });
        parameters.add(2, "no-default-alpn", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterNoDefaultAlpn();
            }
        });
        parameters.add(3, "port", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterPort();
            }
        });
        parameters.add(4, "ipv4hint", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterIpv4Hint();
            }
        });
        parameters.add(5, "ech", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterEch();
            }
        });
        parameters.add(6, "ipv6hint", new Supplier() { // from class: org.xbill.DNS.SVCBBase$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBBase.ParameterIpv6Hint();
            }
        });
        parameters.addAlias(5, "echconfig");
    }

    public static abstract class ParameterBase implements Serializable {
        public abstract void fromString(String str) throws IOException;

        public abstract void fromWire(byte[] bArr) throws IOException;

        public abstract int getKey();

        public abstract String toString();

        public abstract byte[] toWire();

        public static String[] splitStringWithEscapedCommas(String string) {
            return string.split("(?<!\\\\),");
        }
    }

    public static class ParameterMandatory extends ParameterBase {
        private final List<Integer> values;

        public ParameterMandatory() {
            this.values = new ArrayList();
        }

        public ParameterMandatory(List<Integer> values) {
            this.values = values;
        }

        public List<Integer> getValues() {
            return this.values;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 0;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws IOException {
            this.values.clear();
            DNSInput in = new DNSInput(bytes);
            while (in.remaining() >= 2) {
                int key = in.readU16();
                this.values.add(Integer.valueOf(key));
            }
            if (in.remaining() > 0) {
                throw new WireParseException("Unexpected number of bytes in mandatory parameter");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            this.values.clear();
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty list must be specified for mandatory");
            }
            for (String str : splitStringWithEscapedCommas(string)) {
                int key = SVCBBase.parameters.getValue(str);
                if (key == 0) {
                    throw new TextParseException("Key mandatory must not appear in its own list");
                }
                if (this.values.contains(Integer.valueOf(key))) {
                    throw new TextParseException("Duplicate key " + str + " not allowed in mandatory list");
                }
                this.values.add(Integer.valueOf(key));
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            DNSOutput out = new DNSOutput();
            for (Integer val : this.values) {
                out.writeU16(val.intValue());
            }
            return out.toByteArray();
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Integer val : this.values) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(SVCBBase.parameters.getText(val.intValue()));
            }
            return sb.toString();
        }
    }

    public static class ParameterAlpn extends ParameterBase {
        private final List<byte[]> values = new ArrayList();

        public ParameterAlpn() {
        }

        public ParameterAlpn(List<String> values) throws TextParseException {
            for (String str : values) {
                this.values.add(Record.byteArrayFromString(str));
            }
        }

        public List<String> getValues() {
            List<String> result = new ArrayList<>();
            for (byte[] b : this.values) {
                result.add(Record.byteArrayToString(b, false));
            }
            return result;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 1;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws IOException {
            this.values.clear();
            DNSInput in = new DNSInput(bytes);
            while (in.remaining() > 0) {
                byte[] b = in.readCountedString();
                this.values.add(b);
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            this.values.clear();
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty list must be specified for alpn");
            }
            for (String str : splitStringWithEscapedCommas(string)) {
                this.values.add(Record.byteArrayFromString(str));
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            DNSOutput out = new DNSOutput();
            for (byte[] b : this.values) {
                out.writeCountedString(b);
            }
            return out.toByteArray();
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (byte[] b : this.values) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                String str = Record.byteArrayToString(b, false);
                sb.append(str.replace(",", "\\,"));
            }
            return sb.toString();
        }
    }

    public static class ParameterNoDefaultAlpn extends ParameterBase {
        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 2;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws WireParseException {
            if (bytes.length > 0) {
                throw new WireParseException("No value can be specified for no-default-alpn");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            if (string != null && !string.isEmpty()) {
                throw new TextParseException("No value can be specified for no-default-alpn");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            return new byte[0];
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            return "";
        }
    }

    public static class ParameterPort extends ParameterBase {
        private int port;

        public ParameterPort() {
        }

        public ParameterPort(int port) {
            this.port = port;
        }

        public int getPort() {
            return this.port;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 3;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws IOException {
            DNSInput in = new DNSInput(bytes);
            this.port = in.readU16();
            if (in.remaining() > 0) {
                throw new WireParseException("Unexpected number of bytes in port parameter");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Integer value must be specified for port");
            }
            this.port = Integer.parseInt(string);
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            DNSOutput out = new DNSOutput();
            out.writeU16(this.port);
            return out.toByteArray();
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            return Integer.toString(this.port);
        }
    }

    public static class ParameterIpv4Hint extends ParameterBase {
        private final List<byte[]> addresses;

        public ParameterIpv4Hint() {
            this.addresses = new ArrayList();
        }

        public ParameterIpv4Hint(List<Inet4Address> addresses) {
            this.addresses = (List) addresses.stream().map(new Function() { // from class: org.xbill.DNS.SVCBBase$ParameterIpv4Hint$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((Inet4Address) obj).getAddress();
                }
            }).collect(Collectors.toList());
        }

        public List<Inet4Address> getAddresses() throws UnknownHostException {
            List<Inet4Address> result = new LinkedList<>();
            for (byte[] bytes : this.addresses) {
                InetAddress address = InetAddress.getByAddress(bytes);
                if (address instanceof Inet4Address) {
                    result.add((Inet4Address) address);
                }
            }
            return result;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 4;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws IOException {
            this.addresses.clear();
            DNSInput in = new DNSInput(bytes);
            while (in.remaining() >= 4) {
                this.addresses.add(in.readByteArray(4));
            }
            if (in.remaining() > 0) {
                throw new WireParseException("Unexpected number of bytes in ipv4hint parameter");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws IOException {
            this.addresses.clear();
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty IPv4 list must be specified for ipv4hint");
            }
            for (String str : string.split(",")) {
                byte[] address = Address.toByteArray(str, 1);
                if (address == null) {
                    throw new TextParseException("Invalid ipv4hint value '" + str + "'");
                }
                this.addresses.add(address);
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            DNSOutput out = new DNSOutput();
            for (byte[] b : this.addresses) {
                out.writeByteArray(b);
            }
            return out.toByteArray();
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (byte[] b : this.addresses) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(Address.toDottedQuad(b));
            }
            return sb.toString();
        }
    }

    public static class ParameterEch extends ParameterBase {
        private byte[] data;

        public ParameterEch() {
        }

        public ParameterEch(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return this.data;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 5;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) {
            this.data = bytes;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty base64 value must be specified for ech");
            }
            this.data = base64.fromString(string);
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            return this.data;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            return base64.toString(this.data);
        }
    }

    @Deprecated
    public static class ParameterEchConfig extends ParameterBase {
        private byte[] data;

        public ParameterEchConfig() {
        }

        public ParameterEchConfig(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return this.data;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 5;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) {
            this.data = bytes;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws TextParseException {
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty base64 value must be specified for echconfig");
            }
            this.data = base64.fromString(string);
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            return this.data;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            return base64.toString(this.data);
        }
    }

    public static class ParameterIpv6Hint extends ParameterBase {
        private final List<byte[]> addresses;

        public ParameterIpv6Hint() {
            this.addresses = new ArrayList();
        }

        public ParameterIpv6Hint(List<Inet6Address> addresses) {
            this.addresses = (List) addresses.stream().map(new Function() { // from class: org.xbill.DNS.SVCBBase$ParameterIpv6Hint$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((Inet6Address) obj).getAddress();
                }
            }).collect(Collectors.toList());
        }

        public List<Inet6Address> getAddresses() throws UnknownHostException {
            List<Inet6Address> result = new LinkedList<>();
            for (byte[] bytes : this.addresses) {
                InetAddress address = InetAddress.getByAddress(bytes);
                if (address instanceof Inet6Address) {
                    result.add((Inet6Address) address);
                }
            }
            return result;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return 6;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) throws IOException {
            this.addresses.clear();
            DNSInput in = new DNSInput(bytes);
            while (in.remaining() >= 16) {
                this.addresses.add(in.readByteArray(16));
            }
            if (in.remaining() > 0) {
                throw new WireParseException("Unexpected number of bytes in ipv6hint parameter");
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws IOException {
            this.addresses.clear();
            if (string == null || string.isEmpty()) {
                throw new TextParseException("Non-empty IPv6 list must be specified for ipv6hint");
            }
            for (String str : string.split(",")) {
                byte[] address = Address.toByteArray(str, 2);
                if (address == null) {
                    throw new TextParseException("Invalid ipv6hint value '" + str + "'");
                }
                this.addresses.add(address);
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            DNSOutput out = new DNSOutput();
            for (byte[] b : this.addresses) {
                out.writeByteArray(b);
            }
            return out.toByteArray();
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() throws UnknownHostException {
            StringBuilder sb = new StringBuilder();
            for (byte[] b : this.addresses) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                try {
                    InetAddress addr = InetAddress.getByAddress(null, b);
                    sb.append(addr.getHostAddress());
                } catch (UnknownHostException e) {
                    return e.getMessage();
                }
            }
            return sb.toString();
        }
    }

    public static class ParameterUnknown extends ParameterBase {
        private final int key;
        private byte[] value;

        public ParameterUnknown(int key) {
            this.key = key;
            this.value = new byte[0];
        }

        public ParameterUnknown(int key, byte[] value) {
            this.key = key;
            this.value = value;
        }

        public byte[] getValue() {
            return this.value;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public int getKey() {
            return this.key;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromWire(byte[] bytes) {
            this.value = bytes;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public void fromString(String string) throws IOException {
            if (string == null || string.isEmpty()) {
                this.value = new byte[0];
            } else {
                this.value = Record.byteArrayFromString(string);
            }
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public byte[] toWire() {
            return this.value;
        }

        @Override // org.xbill.DNS.SVCBBase.ParameterBase
        public String toString() {
            return Record.byteArrayToString(this.value, false);
        }
    }

    protected boolean checkMandatoryParams() {
        ParameterMandatory param = (ParameterMandatory) getSvcParamValue(0);
        if (param != null) {
            Iterator it = param.values.iterator();
            while (it.hasNext()) {
                int key = ((Integer) it.next()).intValue();
                if (getSvcParamValue(key) == null) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        ParameterBase param;
        this.svcPriority = in.readU16();
        this.targetName = new Name(in);
        this.svcParams.clear();
        while (in.remaining() >= 4) {
            int key = in.readU16();
            int length = in.readU16();
            byte[] value = in.readByteArray(length);
            Supplier<ParameterBase> factory = parameters.getFactory(key);
            if (factory != null) {
                param = factory.get();
            } else {
                param = new ParameterUnknown(key);
            }
            param.fromWire(value);
            this.svcParams.put(Integer.valueOf(key), param);
        }
        if (in.remaining() > 0) {
            throw new WireParseException("Record had unexpected number of bytes");
        }
        if (!checkMandatoryParams()) {
            throw new WireParseException("Not all mandatory SvcParams are specified");
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.svcPriority);
        sb.append(" ");
        sb.append(this.targetName);
        for (Map.Entry<Integer, ParameterBase> entry : this.svcParams.entrySet()) {
            sb.append(" ");
            sb.append(parameters.getText(entry.getKey().intValue()));
            ParameterBase param = entry.getValue();
            String value = param.toString();
            if (value != null && !value.isEmpty()) {
                sb.append("=");
                sb.append(value);
            }
        }
        return sb.toString();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String keyStr;
        ParameterBase param;
        this.svcPriority = st.getUInt16();
        this.targetName = st.getName(origin);
        this.svcParams.clear();
        while (true) {
            String valueStr = null;
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                int indexOfEquals = t.value().indexOf(61);
                if (indexOfEquals == -1) {
                    keyStr = t.value();
                } else if (indexOfEquals == t.value().length() - 1) {
                    keyStr = t.value().substring(0, indexOfEquals);
                    Tokenizer.Token valueToken = st.get();
                    if (!valueToken.isString()) {
                        throw new TextParseException("Expected value for parameter key '" + keyStr + "'");
                    }
                    valueStr = valueToken.value();
                } else if (indexOfEquals > 0) {
                    keyStr = t.value().substring(0, indexOfEquals);
                    valueStr = t.value().substring(indexOfEquals + 1);
                } else {
                    throw new TextParseException("Expected valid parameter key=value for '" + t.value() + "'");
                }
                int key = parameters.getValue(keyStr);
                if (key == -1) {
                    throw new TextParseException("Expected a valid parameter key for '" + keyStr + "'");
                }
                if (this.svcParams.containsKey(Integer.valueOf(key))) {
                    throw new TextParseException("Duplicate parameter key for '" + keyStr + "'");
                }
                Supplier<ParameterBase> factory = parameters.getFactory(key);
                if (factory != null) {
                    param = factory.get();
                } else {
                    param = new ParameterUnknown(key);
                }
                param.fromString(valueStr);
                this.svcParams.put(Integer.valueOf(key), param);
            } else {
                st.unget();
                if (this.svcPriority == 0 && !this.svcParams.isEmpty()) {
                    throw new TextParseException("No parameter values allowed for AliasMode");
                }
                if (!checkMandatoryParams()) {
                    throw new TextParseException("Not all mandatory SvcParams are specified");
                }
                return;
            }
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.svcPriority);
        this.targetName.toWire(out, null, canonical);
        for (Map.Entry<Integer, ParameterBase> entry : this.svcParams.entrySet()) {
            out.writeU16(entry.getKey().intValue());
            ParameterBase param = entry.getValue();
            byte[] value = param.toWire();
            out.writeU16(value.length);
            out.writeByteArray(value);
        }
    }
}
