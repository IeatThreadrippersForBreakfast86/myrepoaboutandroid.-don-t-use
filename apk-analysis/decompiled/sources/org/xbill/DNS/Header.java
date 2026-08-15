package org.xbill.DNS;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/* loaded from: classes8.dex */
public class Header implements Cloneable {
    public static final int LENGTH = 12;
    private static final Random random = new SecureRandom();
    private int[] counts;
    private int flags;

    /* renamed from: id */
    private int f257id;

    public Header(int id) {
        if (!Utils.isUInt16(id)) {
            throw new IllegalArgumentException("DNS message ID " + id + " is out of range");
        }
        this.counts = new int[4];
        this.flags = 0;
        this.f257id = id;
    }

    public Header() {
        this(random.nextInt(65535));
    }

    Header(DNSInput in) throws IOException {
        this(in.readU16());
        this.flags = in.readU16();
        for (int i = 0; i < this.counts.length; i++) {
            this.counts[i] = in.readU16();
        }
    }

    public Header(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    void toWire(DNSOutput out) {
        out.writeU16(getID());
        out.writeU16(this.flags);
        for (int count : this.counts) {
            out.writeU16(count);
        }
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        return out.toByteArray();
    }

    private static boolean validFlag(int bit) {
        return bit >= 0 && bit <= 15 && Flags.isFlag(bit);
    }

    private static void checkFlag(int bit) {
        if (!validFlag(bit)) {
            throw new IllegalArgumentException("invalid flag bit " + bit);
        }
    }

    static int setFlag(int flags, int bit, boolean value) {
        checkFlag(bit);
        return value ? (1 << (15 - bit)) | flags : (~(1 << (15 - bit))) & flags;
    }

    static boolean getFlag(int flags, int bit) {
        checkFlag(bit);
        return ((1 << (15 - bit)) & flags) != 0;
    }

    public void setFlag(int bit) {
        checkFlag(bit);
        this.flags = setFlag(this.flags, bit, true);
    }

    public void unsetFlag(int bit) {
        checkFlag(bit);
        this.flags = setFlag(this.flags, bit, false);
    }

    public boolean getFlag(int bit) {
        return getFlag(this.flags, bit);
    }

    boolean[] getFlags() {
        boolean[] array = new boolean[16];
        for (int i = 0; i < array.length; i++) {
            if (validFlag(i)) {
                array[i] = getFlag(i);
            }
        }
        return array;
    }

    public int getID() {
        return this.f257id;
    }

    public void setID(int id) {
        if (!Utils.isUInt16(id)) {
            throw new IllegalArgumentException("DNS message ID " + id + " is out of range");
        }
        this.f257id = id;
    }

    public void setRcode(int value) {
        if (value < 0 || value > 15) {
            throw new IllegalArgumentException("DNS Rcode " + value + " is out of range");
        }
        this.flags &= -16;
        this.flags |= value;
    }

    public int getRcode() {
        return this.flags & 15;
    }

    public void setOpcode(int value) {
        if (value < 0 || value > 15) {
            throw new IllegalArgumentException("DNS Opcode " + value + "is out of range");
        }
        this.flags &= 34815;
        this.flags |= value << 11;
    }

    public int getOpcode() {
        return (this.flags >> 11) & 15;
    }

    void setCount(int field, int value) {
        if (!Utils.isUInt16(value)) {
            throw new IllegalArgumentException("DNS section count " + value + " is out of range");
        }
        this.counts[field] = value;
    }

    void incCount(int field) {
        if (this.counts[field] == 65535) {
            throw new IllegalStateException("DNS section count cannot be incremented");
        }
        int[] iArr = this.counts;
        iArr[field] = iArr[field] + 1;
    }

    void decCount(int field) {
        if (this.counts[field] == 0) {
            throw new IllegalStateException("DNS section count cannot be decremented");
        }
        this.counts[field] = r0[field] - 1;
    }

    public int getCount(int field) {
        return this.counts[field];
    }

    int getFlagsByte() {
        return this.flags;
    }

    public String printFlags() {
        StringBuilder sb = new StringBuilder();
        printFlags(sb);
        return sb.toString();
    }

    private void printFlags(StringBuilder sb) {
        for (int i = 0; i < 16; i++) {
            if (validFlag(i) && getFlag(i)) {
                sb.append(Flags.string(i));
                sb.append(" ");
            }
        }
    }

    String toStringWithRcode(int newrcode) {
        StringBuilder sb = new StringBuilder();
        sb.append(";; ->>HEADER<<- ");
        sb.append("opcode: ").append(Opcode.string(getOpcode()));
        sb.append(", status: ").append(Rcode.string(newrcode));
        sb.append(", id: ").append(getID());
        sb.append("\n");
        sb.append(";; flags: ");
        printFlags(sb);
        sb.append("; ");
        for (int i = 0; i < 4; i++) {
            sb.append(Section.string(i)).append(": ").append(getCount(i)).append(" ");
        }
        return sb.toString();
    }

    public String toString() {
        return toStringWithRcode(getRcode());
    }

    public Header clone() throws CloneNotSupportedException {
        try {
            Header h = (Header) super.clone();
            h.f257id = this.f257id;
            h.flags = this.flags;
            h.counts = new int[h.counts.length];
            System.arraycopy(this.counts, 0, h.counts, 0, this.counts.length);
            return h;
        } catch (CloneNotSupportedException $ex) {
            throw $ex;
        }
    }
}
