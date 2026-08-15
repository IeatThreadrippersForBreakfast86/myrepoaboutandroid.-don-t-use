package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import kotlin.UByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes8.dex */
public class Name implements Comparable<Name>, Serializable {
    private static final int LABEL_COMPRESSION = 192;
    private static final int LABEL_MASK = 192;
    private static final int LABEL_NORMAL = 0;
    private static final int MAXLABEL = 63;
    private static final int MAXNAME = 255;
    private static final int MAXOFFSETS = 9;
    public static final Name empty;
    public static final Name root;
    private static final long serialVersionUID = -6036624806201621219L;
    private static final Name wild;
    private transient int hashcode;
    private int labels;
    private byte[] name;
    private long offsets;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Name.class);
    private static final byte[] emptyLabel = {0};
    private static final byte[] wildLabel = {1, 42};
    private static final byte[] lowercase = new byte[256];

    static {
        for (int i = 0; i < lowercase.length; i++) {
            if (i < 65 || i > 90) {
                lowercase[i] = (byte) i;
            } else {
                lowercase[i] = (byte) ((i - 65) + 97);
            }
        }
        root = new Name();
        root.name = emptyLabel;
        root.labels = 1;
        empty = new Name();
        empty.name = new byte[0];
        wild = new Name();
        wild.name = wildLabel;
        wild.labels = 1;
    }

    private Name() {
    }

    private void setOffset(int n, int offset) {
        if (n == 0 || n >= 9) {
            return;
        }
        int shift = (n - 1) * 8;
        this.offsets &= ~(255 << shift);
        this.offsets |= offset << shift;
    }

    private int offset(int n) {
        if (n == 0) {
            return 0;
        }
        if (n < 1 || n >= this.labels) {
            throw new IllegalArgumentException("label out of range");
        }
        if (n < 9) {
            int shift = (n - 1) * 8;
            return ((int) (this.offsets >>> shift)) & 255;
        }
        int pos = ((int) (this.offsets >>> 56)) & 255;
        for (int i = 8; i < n; i++) {
            pos += this.name[pos] + 1;
        }
        return pos;
    }

    private static void copy(Name src, Name dst) {
        dst.name = src.name;
        dst.offsets = src.offsets;
        dst.labels = src.labels;
    }

    private void append(byte[] array, int arrayOffset, int numLabels) throws NameTooLongException {
        byte[] newname;
        int length = this.name == null ? 0 : this.name.length;
        int appendLength = 0;
        int pos = arrayOffset;
        for (int i = 0; i < numLabels; i++) {
            int len = array[pos] + 1;
            pos += len;
            appendLength += len;
        }
        int i2 = length + appendLength;
        if (i2 > 255) {
            throw new NameTooLongException();
        }
        if (this.name != null) {
            newname = Arrays.copyOf(this.name, i2);
        } else {
            newname = new byte[i2];
        }
        System.arraycopy(array, arrayOffset, newname, length, appendLength);
        this.name = newname;
        int pos2 = length;
        for (int i3 = 0; i3 < numLabels && i3 < 9; i3++) {
            setOffset(this.labels + i3, pos2);
            pos2 += newname[pos2] + 1;
        }
        int i4 = this.labels;
        this.labels = i4 + numLabels;
    }

    private void append(char[] label, int len) throws NameTooLongException {
        int destPos = prepareAppend(len);
        for (int i = 0; i < len; i++) {
            this.name[destPos + i] = (byte) label[i];
        }
    }

    private int prepareAppend(int len) throws NameTooLongException {
        byte[] newname;
        int length = this.name == null ? 0 : this.name.length;
        int newlength = length + 1 + len;
        if (newlength > 255) {
            throw new NameTooLongException();
        }
        if (this.name != null) {
            newname = Arrays.copyOf(this.name, newlength);
        } else {
            newname = new byte[newlength];
        }
        newname[length] = (byte) len;
        this.name = newname;
        setOffset(this.labels, length);
        this.labels++;
        return length + 1;
    }

    private void appendFromString(String fullName, char[] label, int length) throws TextParseException {
        try {
            append(label, length);
        } catch (NameTooLongException e) {
            throw new TextParseException(fullName, "Name too long", e);
        }
    }

    private void appendFromString(String fullName, byte[] label, int n) throws TextParseException {
        try {
            append(label, 0, n);
        } catch (NameTooLongException e) {
            throw new TextParseException(fullName, "Name too long");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Name(String s, Name origin) throws TextParseException {
        int pos;
        switch (s) {
            case "":
                throw new TextParseException("empty name");
            case "@":
                if (origin == null) {
                    copy(empty, this);
                    return;
                } else {
                    copy(origin, this);
                    return;
                }
            case ".":
                copy(root, this);
                return;
            default:
                int labelstart = -1;
                int pos2 = 0;
                char[] label = new char[63];
                boolean escaped = false;
                int digits = 0;
                int intval = 0;
                boolean absolute = false;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c <= 255) {
                        if (escaped) {
                            if (c >= '0' && c <= '9' && digits < 3) {
                                digits++;
                                intval = (intval * 10) + (c - '0');
                                if (intval > 255) {
                                    throw new TextParseException(s, "bad escape");
                                }
                                if (digits < 3) {
                                    pos = 63;
                                } else {
                                    c = (char) intval;
                                }
                            } else if (digits > 0 && digits < 3) {
                                throw new TextParseException(s, "bad escape");
                            }
                            if (pos2 >= 63) {
                                throw new TextParseException(s, "label too long");
                            }
                            labelstart = pos2;
                            label[pos2] = c;
                            pos2++;
                            escaped = false;
                            pos = 63;
                        } else if (c == '\\') {
                            escaped = true;
                            digits = 0;
                            intval = 0;
                            pos = 63;
                        } else if (c == '.') {
                            if (labelstart == -1) {
                                throw new TextParseException(s, "invalid empty label");
                            }
                            appendFromString(s, label, pos2);
                            labelstart = -1;
                            pos2 = 0;
                            pos = 63;
                        } else {
                            labelstart = labelstart == -1 ? i : labelstart;
                            pos = 63;
                            if (pos2 >= 63) {
                                throw new TextParseException(s, "label too long");
                            }
                            label[pos2] = c;
                            pos2++;
                        }
                    } else {
                        throw new TextParseException(s, "Illegal character in name");
                    }
                }
                if ((digits > 0 && digits < 3) || escaped) {
                    throw new TextParseException(s, "bad escape");
                }
                if (labelstart == -1) {
                    appendFromString(s, emptyLabel, 1);
                    absolute = true;
                } else {
                    appendFromString(s, label, pos2);
                }
                if (origin != null && !absolute) {
                    absolute = origin.isAbsolute();
                    appendFromString(s, origin.name, origin.labels);
                }
                if (!absolute && length() == 255) {
                    throw new TextParseException(s, "Name too long");
                }
                return;
        }
    }

    public Name(String s) throws TextParseException {
        this(s, (Name) null);
    }

    public static Name fromString(String s, Name origin) throws TextParseException {
        if (s.equals("@")) {
            return origin != null ? origin : empty;
        }
        if (s.equals(".")) {
            return root;
        }
        return new Name(s, origin);
    }

    public static Name fromString(String s) throws TextParseException {
        return fromString(s, null);
    }

    public static Name fromConstantString(String s) {
        try {
            return fromString(s, null);
        } catch (TextParseException e) {
            throw new IllegalArgumentException("Invalid name '" + s + "'");
        }
    }

    public Name(DNSInput in) throws WireParseException {
        boolean done = false;
        byte[] label = new byte[64];
        boolean savedState = false;
        while (!done) {
            int len = in.readU8();
            switch (len & 192) {
                case 0:
                    if (len == 0) {
                        append(emptyLabel, 0, 1);
                        done = true;
                        break;
                    } else {
                        label[0] = (byte) len;
                        in.readByteArray(label, 1, len);
                        append(label, 0, 1);
                        break;
                    }
                case 192:
                    int pos = in.readU8() + ((len & (-193)) << 8);
                    log.trace("currently {}, pointer to {}", Integer.valueOf(in.current()), Integer.valueOf(pos));
                    if (pos >= in.current() - 2) {
                        throw new WireParseException("bad compression");
                    }
                    if (!savedState) {
                        in.save();
                        savedState = true;
                    }
                    in.jump(pos);
                    log.trace("current name '{}', seeking to {}", this, Integer.valueOf(pos));
                    break;
                default:
                    throw new WireParseException("bad label type");
            }
        }
        if (savedState) {
            in.restore();
        }
    }

    public Name(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    public Name(Name src, int n) {
        if (n > src.labels) {
            throw new IllegalArgumentException("attempted to remove too many labels");
        }
        if (n == src.labels) {
            copy(empty, this);
            return;
        }
        this.labels = src.labels - n;
        this.name = Arrays.copyOfRange(src.name, src.offset(n), src.name.length);
        int strippedBytes = src.offset(n);
        for (int i = 1; i < 9 && i < this.labels; i++) {
            setOffset(i, src.offset(i + n) - strippedBytes);
        }
    }

    public static Name concatenate(Name prefix, Name suffix) throws NameTooLongException {
        if (prefix.isAbsolute()) {
            return prefix;
        }
        Name newname = new Name();
        newname.append(prefix.name, 0, prefix.labels);
        newname.append(suffix.name, 0, suffix.labels);
        return newname;
    }

    public Name relativize(Name origin) {
        if (origin == null || !subdomain(origin)) {
            return this;
        }
        Name newname = new Name();
        int length = length() - origin.length();
        newname.labels = this.labels - origin.labels;
        newname.offsets = this.offsets;
        newname.name = new byte[length];
        System.arraycopy(this.name, 0, newname.name, 0, length);
        return newname;
    }

    public Name wild(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("must replace 1 or more labels");
        }
        try {
            Name newname = new Name();
            copy(wild, newname);
            newname.append(this.name, offset(n), this.labels - n);
            return newname;
        } catch (NameTooLongException e) {
            throw new IllegalStateException("Name.wild: concatenate failed");
        }
    }

    public Name canonicalize() {
        boolean canonical = true;
        byte[] bArr = this.name;
        int length = bArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            byte b = bArr[i];
            if (lowercase[b & UByte.MAX_VALUE] == b) {
                i++;
            } else {
                canonical = false;
                break;
            }
        }
        if (canonical) {
            return this;
        }
        Name newname = new Name();
        newname.offsets = this.offsets;
        newname.labels = this.labels;
        newname.name = new byte[length()];
        for (int i2 = 0; i2 < newname.name.length; i2++) {
            newname.name[i2] = lowercase[this.name[i2] & UByte.MAX_VALUE];
        }
        return newname;
    }

    public Name fromDNAME(DNAMERecord dname) throws NameTooLongException {
        Name dnameowner = dname.getName();
        Name dnametarget = dname.getTarget();
        if (!subdomain(dnameowner)) {
            return null;
        }
        int plabels = this.labels - dnameowner.labels;
        int plength = length() - dnameowner.length();
        int dlabels = dnametarget.labels;
        int dlength = dnametarget.length();
        if (plength + dlength > 255) {
            throw new NameTooLongException();
        }
        Name newname = new Name();
        newname.labels = plabels + dlabels;
        newname.name = Arrays.copyOf(this.name, plength + dlength);
        System.arraycopy(dnametarget.name, 0, newname.name, plength, dlength);
        int pos = 0;
        for (int i = 0; i < 9 && i < plabels + dlabels; i++) {
            newname.setOffset(i, pos);
            pos += newname.name[pos] + 1;
        }
        return newname;
    }

    public boolean isWild() {
        return this.labels != 0 && this.name[0] == 1 && this.name[1] == 42;
    }

    public boolean isAbsolute() {
        return this.labels != 0 && this.name[offset(this.labels - 1)] == 0;
    }

    public short length() {
        if (this.labels == 0) {
            return (short) 0;
        }
        return (short) this.name.length;
    }

    public int labels() {
        return this.labels;
    }

    public boolean subdomain(Name domain) {
        int dlabels = domain.labels;
        if (dlabels > this.labels) {
            return false;
        }
        if (dlabels == this.labels) {
            return equals(domain);
        }
        return domain.equals(this.name, offset(this.labels - dlabels));
    }

    private String byteString(byte[] array, int pos) {
        StringBuilder sb = new StringBuilder();
        int pos2 = pos + 1;
        int len = array[pos];
        for (int i = pos2; i < pos2 + len; i++) {
            int b = array[i] & UByte.MAX_VALUE;
            if (b <= 32 || b >= 127) {
                sb.append('\\');
                if (b < 10) {
                    sb.append("00");
                } else if (b < 100) {
                    sb.append('0');
                }
                sb.append(b);
            } else if (b == 34 || b == 40 || b == 41 || b == 46 || b == 59 || b == 92 || b == 64 || b == 36) {
                sb.append('\\');
                sb.append((char) b);
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }

    public String toString(boolean omitFinalDot) {
        if (this.labels == 0) {
            return "@";
        }
        if (this.labels == 1 && this.name[0] == 0) {
            return ".";
        }
        StringBuilder sb = new StringBuilder();
        int label = 0;
        int pos = 0;
        while (true) {
            if (label >= this.labels) {
                break;
            }
            int len = this.name[pos];
            if (len == 0) {
                if (!omitFinalDot) {
                    sb.append('.');
                }
            } else {
                if (label > 0) {
                    sb.append('.');
                }
                sb.append(byteString(this.name, pos));
                pos += len + 1;
                label++;
            }
        }
        return sb.toString();
    }

    public String toString() {
        return toString(false);
    }

    public byte[] getLabel(int n) {
        int pos = offset(n);
        byte len = (byte) (this.name[pos] + 1);
        return Arrays.copyOfRange(this.name, pos, pos + len);
    }

    public String getLabelString(int n) {
        int pos = offset(n);
        return byteString(this.name, pos);
    }

    public void toWire(DNSOutput out, Compression c) {
        Name tname;
        if (!isAbsolute()) {
            throw new IllegalArgumentException("toWire() called on non-absolute name");
        }
        for (int i = 0; i < this.labels - 1; i++) {
            if (i == 0) {
                tname = this;
            } else {
                tname = new Name(this, i);
            }
            int pos = -1;
            if (c != null) {
                pos = c.get(tname);
            }
            if (pos >= 0) {
                out.writeU16(pos | 49152);
                return;
            }
            if (c != null) {
                c.add(out.current(), tname);
            }
            int off = offset(i);
            out.writeByteArray(this.name, off, this.name[off] + 1);
        }
        out.writeU8(0);
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out, null);
        return out.toByteArray();
    }

    public void toWireCanonical(DNSOutput out) {
        byte[] b = toWireCanonical();
        out.writeByteArray(b);
    }

    public byte[] toWireCanonical() {
        if (this.labels == 0) {
            return new byte[0];
        }
        byte[] b = new byte[this.name.length];
        int i = 0;
        int spos = 0;
        int dpos = 0;
        while (i < this.labels) {
            int len = this.name[spos];
            int spos2 = spos + 1;
            b[dpos] = this.name[spos];
            int j = 0;
            dpos++;
            while (j < len) {
                b[dpos] = lowercase[this.name[spos2] & 255];
                j++;
                dpos++;
                spos2++;
            }
            i++;
            spos = spos2;
        }
        return b;
    }

    public void toWire(DNSOutput out, Compression c, boolean canonical) {
        if (canonical) {
            toWireCanonical(out);
        } else {
            toWire(out, c);
        }
    }

    private boolean equals(byte[] b, int bpos) {
        int i = 0;
        int pos = 0;
        while (i < this.labels) {
            if (this.name[pos] != b[bpos]) {
                return false;
            }
            int pos2 = pos + 1;
            int len = this.name[pos];
            bpos++;
            int j = 0;
            while (j < len) {
                int pos3 = pos2 + 1;
                int bpos2 = bpos + 1;
                if (lowercase[this.name[pos2] & 255] != lowercase[b[bpos] & 255]) {
                    return false;
                }
                j++;
                bpos = bpos2;
                pos2 = pos3;
            }
            i++;
            pos = pos2;
        }
        return true;
    }

    public boolean equals(Object arg) {
        if (arg == this) {
            return true;
        }
        if (!(arg instanceof Name)) {
            return false;
        }
        Name other = (Name) arg;
        if (other.labels == this.labels && other.hashCode() == hashCode()) {
            return equals(other.name, 0);
        }
        return false;
    }

    public int hashCode() {
        if (this.hashcode != 0) {
            return this.hashcode;
        }
        int code = 0;
        for (int i = offset(0); i < this.name.length; i++) {
            code += (code << 3) + (lowercase[this.name[i] & UByte.MAX_VALUE] & UByte.MAX_VALUE);
        }
        this.hashcode = code;
        return this.hashcode;
    }

    @Override // java.lang.Comparable
    public int compareTo(Name arg) {
        if (this == arg) {
            return 0;
        }
        int alabels = arg.labels;
        int compares = Math.min(this.labels, alabels);
        for (int i = 1; i <= compares; i++) {
            int start = offset(this.labels - i);
            int astart = arg.offset(alabels - i);
            int length = this.name[start];
            int alength = arg.name[astart];
            for (int j = 0; j < length && j < alength; j++) {
                int n = (lowercase[this.name[(j + start) + 1] & UByte.MAX_VALUE] & UByte.MAX_VALUE) - (lowercase[arg.name[(j + astart) + 1] & UByte.MAX_VALUE] & UByte.MAX_VALUE);
                if (n != 0) {
                    return n;
                }
            }
            if (length != alength) {
                return length - alength;
            }
        }
        int i2 = this.labels;
        return i2 - alabels;
    }
}
