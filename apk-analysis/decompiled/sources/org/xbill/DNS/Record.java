package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.Supplier;
import kotlin.UByte;
import kotlin.text.Typography;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public abstract class Record implements Cloneable, Comparable<Record>, Serializable {
    protected int dclass;
    protected Name name;
    protected long ttl;
    protected int type;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Record.class);
    private static final DecimalFormat byteFormat = new DecimalFormat();

    protected abstract void rdataFromString(Tokenizer tokenizer, Name name) throws IOException;

    protected abstract void rrFromWire(DNSInput dNSInput) throws IOException;

    protected abstract String rrToString();

    protected abstract void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z);

    static {
        byteFormat.setMinimumIntegerDigits(3);
    }

    private static class RecordSerializationProxy implements Serializable {
        private static final long serialVersionUID = 1434159920070152561L;
        private final boolean isEmpty;
        private final byte[] wireData;

        RecordSerializationProxy(Record record) {
            this.isEmpty = record instanceof EmptyRecord;
            this.wireData = record.toWire(!this.isEmpty ? 1 : 0);
        }

        protected Object readResolve() throws ObjectStreamException {
            try {
                return Record.fromWire(this.wireData, this.isEmpty ? 0 : 1);
            } catch (IOException e) {
                throw new InvalidObjectException(e.getMessage());
            }
        }
    }

    protected Record() {
    }

    protected Record(Name name, int type, int dclass, long ttl) {
        if (!name.isAbsolute()) {
            throw new RelativeNameException(name);
        }
        Type.check(type);
        DClass.check(dclass);
        TTL.check(ttl);
        this.name = name;
        this.type = type;
        this.dclass = dclass;
        this.ttl = ttl;
    }

    Object writeReplace() {
        log.trace("Creating proxy object for serialization");
        return new RecordSerializationProxy(this);
    }

    private void readObject(ObjectInputStream ois) throws InvalidObjectException {
        throw new InvalidObjectException("Use RecordSerializationProxy");
    }

    private static Record getEmptyRecord(Name name, int type, int dclass, long ttl, boolean hasData) {
        Record rec;
        if (hasData) {
            Supplier<Record> factory = Type.getFactory(type);
            if (factory != null) {
                rec = factory.get();
            } else {
                rec = new UNKRecord();
            }
        } else {
            rec = new EmptyRecord();
        }
        rec.name = name;
        rec.type = type;
        rec.dclass = dclass;
        rec.ttl = ttl;
        return rec;
    }

    private static Record newRecord(Name name, int type, int dclass, long ttl, int length, DNSInput in) throws IOException {
        Record rec = getEmptyRecord(name, type, dclass, ttl, in != null);
        if (in != null) {
            if (in.remaining() < length) {
                throw new WireParseException("truncated record");
            }
            in.setActive(length);
            rec.rrFromWire(in);
            if (in.remaining() > 0) {
                throw new WireParseException("invalid record length");
            }
            in.clearActive();
        }
        return rec;
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl, int length, byte[] data) {
        DNSInput in;
        if (!name.isAbsolute()) {
            throw new RelativeNameException(name);
        }
        Type.check(type);
        DClass.check(dclass);
        TTL.check(ttl);
        if (data != null) {
            in = new DNSInput(data);
        } else {
            in = null;
        }
        try {
            return newRecord(name, type, dclass, ttl, length, in);
        } catch (IOException e) {
            return null;
        }
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl, byte[] data) {
        return newRecord(name, type, dclass, ttl, data.length, data);
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl) {
        if (!name.isAbsolute()) {
            throw new RelativeNameException(name);
        }
        Type.check(type);
        DClass.check(dclass);
        TTL.check(ttl);
        return getEmptyRecord(name, type, dclass, ttl, false);
    }

    public static Record newRecord(Name name, int type, int dclass) {
        return newRecord(name, type, dclass, 0L);
    }

    static Record fromWire(DNSInput in, int section, boolean isUpdate) throws IOException {
        Name name = new Name(in);
        int type = in.readU16();
        int dclass = in.readU16();
        if (section == 0) {
            return newRecord(name, type, dclass);
        }
        long ttl = in.readU32();
        int length = in.readU16();
        if (length == 0 && isUpdate && (section == 1 || section == 2)) {
            return newRecord(name, type, dclass, ttl);
        }
        Record rec = newRecord(name, type, dclass, ttl, length, in);
        return rec;
    }

    static Record fromWire(DNSInput in, int section) throws IOException {
        return fromWire(in, section, false);
    }

    public static Record fromWire(byte[] b, int section) throws IOException {
        return fromWire(new DNSInput(b), section, false);
    }

    void toWire(DNSOutput out, int section, Compression c) {
        this.name.toWire(out, c);
        out.writeU16(this.type);
        out.writeU16(this.dclass);
        if (section == 0) {
            return;
        }
        out.writeU32(this.ttl);
        int lengthPosition = out.current();
        out.writeU16(0);
        rrToWire(out, c, false);
        int rrlength = (out.current() - lengthPosition) - 2;
        out.writeU16At(rrlength, lengthPosition);
    }

    public byte[] toWire(int section) {
        DNSOutput out = new DNSOutput();
        toWire(out, section, null);
        return out.toByteArray();
    }

    private void toWireCanonical(DNSOutput out, boolean noTTL) {
        this.name.toWireCanonical(out);
        out.writeU16(this.type);
        out.writeU16(this.dclass);
        if (noTTL) {
            out.writeU32(0L);
        } else {
            out.writeU32(this.ttl);
        }
        int lengthPosition = out.current();
        out.writeU16(0);
        rrToWire(out, null, true);
        int rrlength = (out.current() - lengthPosition) - 2;
        out.writeU16At(rrlength, lengthPosition);
    }

    private byte[] toWireCanonical(boolean noTTL) {
        DNSOutput out = new DNSOutput();
        toWireCanonical(out, noTTL);
        return out.toByteArray();
    }

    public byte[] toWireCanonical() {
        return toWireCanonical(false);
    }

    public byte[] rdataToWireCanonical() {
        DNSOutput out = new DNSOutput();
        rrToWire(out, null, true);
        return out.toByteArray();
    }

    public String rdataToString() {
        return rrToString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if (sb.length() < 8) {
            sb.append("\t");
        }
        if (sb.length() < 16) {
            sb.append("\t");
        }
        sb.append("\t");
        if (Options.check("BINDTTL")) {
            sb.append(TTL.format(this.ttl));
        } else {
            sb.append(this.ttl);
        }
        sb.append("\t");
        if (this.dclass != 1 || !Options.check("noPrintIN")) {
            sb.append(DClass.string(this.dclass));
            sb.append("\t");
        }
        sb.append(Type.string(this.type));
        String rdata = rrToString();
        if (!rdata.isEmpty()) {
            sb.append("\t");
            sb.append(rdata);
        }
        return sb.toString();
    }

    protected static byte[] byteArrayFromString(String s) throws TextParseException {
        byte[] array = s.getBytes();
        boolean escaped = false;
        boolean hasEscapes = false;
        int length = array.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            byte item = array[i];
            if (item != 92) {
                i++;
            } else {
                hasEscapes = true;
                break;
            }
        }
        if (!hasEscapes) {
            if (array.length > 255) {
                throw new TextParseException("text string too long");
            }
            return array;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int digits = 0;
        int intval = 0;
        for (byte value : array) {
            if (escaped) {
                byte b = value;
                if (b >= 48 && b <= 57) {
                    digits++;
                    intval = (intval * 10) + (b - 48);
                    if (intval > 255) {
                        throw new TextParseException("bad escape");
                    }
                    if (digits >= 3) {
                        b = (byte) intval;
                    }
                } else if (digits > 0) {
                    throw new TextParseException("bad escape");
                }
                os.write(b);
                escaped = false;
            } else if (value == 92) {
                escaped = true;
                digits = 0;
                intval = 0;
            } else {
                os.write(value);
            }
        }
        if (digits > 0 && digits < 3) {
            throw new TextParseException("bad escape");
        }
        if (os.toByteArray().length > 255) {
            throw new TextParseException("text string too long");
        }
        return os.toByteArray();
    }

    protected static String byteArrayToString(byte[] array, boolean quote) {
        StringBuilder sb = new StringBuilder();
        if (quote) {
            sb.append(Typography.quote);
        }
        for (byte value : array) {
            int b = value & UByte.MAX_VALUE;
            if (b < 32 || b >= 127) {
                sb.append('\\');
                sb.append(byteFormat.format(b));
            } else if (b == 34 || b == 92) {
                sb.append('\\');
                sb.append((char) b);
            } else {
                sb.append((char) b);
            }
        }
        if (quote) {
            sb.append(Typography.quote);
        }
        return sb.toString();
    }

    protected static String unknownToString(byte[] data) {
        return "\\# " + data.length + " " + base16.toString(data);
    }

    public static Record fromString(Name name, int type, int dclass, long ttl, Tokenizer st, Name origin) throws IOException {
        byte[] data;
        if (!name.isAbsolute()) {
            throw new RelativeNameException(name);
        }
        Type.check(type);
        DClass.check(dclass);
        TTL.check(ttl);
        Tokenizer.Token t = st.get();
        if (t.type() == 3 && t.value().equals("\\#")) {
            int length = st.getUInt16();
            byte[] data2 = st.getHex();
            if (data2 != null) {
                data = data2;
            } else {
                data = new byte[0];
            }
            if (length != data.length) {
                throw st.exception("invalid unknown RR encoding: length mismatch");
            }
            DNSInput in = new DNSInput(data);
            return newRecord(name, type, dclass, ttl, length, in);
        }
        st.unget();
        Record rec = getEmptyRecord(name, type, dclass, ttl, true);
        rec.rdataFromString(st, origin);
        Tokenizer.Token t2 = st.get();
        if (t2.type() != 1 && t2.type() != 0) {
            throw st.exception("unexpected tokens at end of record (wanted EOL/EOF, got " + t2 + ")");
        }
        return rec;
    }

    public static Record fromString(Name name, int type, int dclass, long ttl, String s, Name origin) throws IOException {
        return fromString(name, type, dclass, ttl, new Tokenizer(s), origin);
    }

    public Name getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public int getRRsetType() {
        return this.type;
    }

    public int getDClass() {
        return this.dclass;
    }

    public long getTTL() {
        return this.ttl;
    }

    public boolean sameRRset(Record rec) {
        return getRRsetType() == rec.getRRsetType() && this.dclass == rec.dclass && this.name.equals(rec.name);
    }

    public boolean sameRRset(RRset set) {
        return getRRsetType() == set.getType() && this.dclass == set.getDClass() && this.name.equals(set.getName());
    }

    public boolean equals(Object arg) {
        if (!(arg instanceof Record)) {
            return false;
        }
        Record r = (Record) arg;
        if (this.type != r.type || this.dclass != r.dclass || !this.name.equals(r.name)) {
            return false;
        }
        byte[] array1 = rdataToWireCanonical();
        byte[] array2 = r.rdataToWireCanonical();
        return Arrays.equals(array1, array2);
    }

    public int hashCode() {
        byte[] array = toWireCanonical(true);
        int code = 0;
        for (byte b : array) {
            code += (code << 3) + (b & UByte.MAX_VALUE);
        }
        return code;
    }

    Record cloneRecord() {
        try {
            return (Record) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    public Record withName(Name name) {
        if (!name.isAbsolute()) {
            throw new RelativeNameException(name);
        }
        Record rec = cloneRecord();
        rec.name = name;
        return rec;
    }

    Record withDClass(int dclass, long ttl) {
        Record rec = cloneRecord();
        rec.dclass = dclass;
        rec.ttl = ttl;
        return rec;
    }

    void setTTL(long ttl) {
        this.ttl = ttl;
    }

    @Override // java.lang.Comparable
    public int compareTo(Record arg) {
        if (this == arg) {
            return 0;
        }
        int n = this.name.compareTo(arg.name);
        if (n != 0) {
            return n;
        }
        int n2 = this.dclass - arg.dclass;
        if (n2 != 0) {
            return n2;
        }
        int n3 = this.type - arg.type;
        if (n3 != 0) {
            return n3;
        }
        byte[] rdata1 = rdataToWireCanonical();
        byte[] rdata2 = arg.rdataToWireCanonical();
        int minLen = Math.min(rdata1.length, rdata2.length);
        for (int i = 0; i < minLen; i++) {
            if (rdata1[i] != rdata2[i]) {
                return (rdata1[i] & UByte.MAX_VALUE) - (rdata2[i] & UByte.MAX_VALUE);
            }
        }
        int i2 = rdata1.length;
        return i2 - rdata2.length;
    }

    public Name getAdditionalName() {
        return null;
    }

    static int checkU8(String field, int val) {
        if (!Utils.isUInt8(val)) {
            throw new IllegalArgumentException("\"" + field + "\" " + val + " must be an unsigned 8 bit value");
        }
        return val;
    }

    static int checkU16(String field, int val) {
        if (!Utils.isUInt16(val)) {
            throw new IllegalArgumentException("\"" + field + "\" " + val + " must be an unsigned 16 bit value");
        }
        return val;
    }

    static long checkU32(String field, long val) {
        if (!Utils.isUInt32(val)) {
            throw new IllegalArgumentException("\"" + field + "\" " + val + " must be an unsigned 32 bit value");
        }
        return val;
    }

    static Name checkName(String field, Name name) {
        if (!name.isAbsolute()) {
            throw new RelativeNameException("'" + name + "' on field " + field + " is not an absolute name");
        }
        return name;
    }

    static byte[] checkByteArrayLength(String field, byte[] array, int maxLength) {
        if (array.length > 65535) {
            throw new IllegalArgumentException("\"" + field + "\" array must have no more than " + maxLength + " elements");
        }
        byte[] out = new byte[array.length];
        System.arraycopy(array, 0, out, 0, array.length);
        return out;
    }
}
