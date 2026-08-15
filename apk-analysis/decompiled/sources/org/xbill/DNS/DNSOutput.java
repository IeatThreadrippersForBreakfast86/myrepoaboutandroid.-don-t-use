package org.xbill.DNS;

/* loaded from: classes8.dex */
public class DNSOutput {
    private byte[] array;
    private int pos;
    private int savedPos;

    public DNSOutput(int size) {
        this.array = new byte[size];
        this.pos = 0;
        this.savedPos = -1;
    }

    public DNSOutput() {
        this(32);
    }

    public int current() {
        return this.pos;
    }

    private void check(long val, int bits) {
        long max = 1 << bits;
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(val + " out of range for " + bits + " bit value");
        }
    }

    private void need(int n) {
        if (this.array.length - this.pos >= n) {
            return;
        }
        int newsize = this.array.length * 2;
        if (newsize < this.pos + n) {
            newsize = this.pos + n;
        }
        byte[] newarray = new byte[newsize];
        System.arraycopy(this.array, 0, newarray, 0, this.pos);
        this.array = newarray;
    }

    public void jump(int index) {
        if (index > this.pos) {
            throw new IllegalArgumentException("cannot jump past end of data");
        }
        this.pos = index;
    }

    public void save() {
        this.savedPos = this.pos;
    }

    public void restore() {
        if (this.savedPos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.pos = this.savedPos;
        this.savedPos = -1;
    }

    public void writeU8(int val) {
        check(val, 8);
        need(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (val & 255);
    }

    public void writeU16(int val) {
        check(val, 16);
        need(2);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((val >>> 8) & 255);
        byte[] bArr2 = this.array;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr2[i2] = (byte) (val & 255);
    }

    public void writeU16At(int val, int where) {
        check(val, 16);
        if (where > this.pos - 2) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        this.array[where] = (byte) ((val >>> 8) & 255);
        this.array[where + 1] = (byte) (val & 255);
    }

    public void writeU32(long val) {
        check(val, 32);
        need(4);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((val >>> 24) & 255);
        byte[] bArr2 = this.array;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr2[i2] = (byte) ((val >>> 16) & 255);
        byte[] bArr3 = this.array;
        int i3 = this.pos;
        this.pos = i3 + 1;
        bArr3[i3] = (byte) ((val >>> 8) & 255);
        byte[] bArr4 = this.array;
        int i4 = this.pos;
        this.pos = i4 + 1;
        bArr4[i4] = (byte) (val & 255);
    }

    public void writeByteArray(byte[] b, int off, int len) {
        need(len);
        System.arraycopy(b, off, this.array, this.pos, len);
        this.pos += len;
    }

    public void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b.length);
    }

    public void writeCountedString(byte[] s) {
        if (s.length > 255) {
            throw new IllegalArgumentException("Invalid counted string");
        }
        need(s.length + 1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (255 & s.length);
        writeByteArray(s, 0, s.length);
    }

    public byte[] toByteArray() {
        byte[] out = new byte[this.pos];
        System.arraycopy(this.array, 0, out, 0, this.pos);
        return out;
    }

    static byte[] toU16(int val) {
        return new byte[]{(byte) ((val >>> 8) & 255), (byte) (val & 255)};
    }
}
