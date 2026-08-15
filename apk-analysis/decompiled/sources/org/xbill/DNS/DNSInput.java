package org.xbill.DNS;

import java.nio.ByteBuffer;
import kotlin.UByte;
import kotlin.UShort;

/* loaded from: classes8.dex */
public class DNSInput {
    private final ByteBuffer byteBuffer;
    private final int limit;
    private final int offset;
    private int savedEnd;
    private int savedPos;

    public DNSInput(byte[] input) {
        this(ByteBuffer.wrap(input));
    }

    public DNSInput(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.offset = byteBuffer.position();
        this.limit = byteBuffer.limit();
        this.savedPos = -1;
        this.savedEnd = -1;
    }

    public int current() {
        return this.byteBuffer.position() - this.offset;
    }

    public int remaining() {
        return this.byteBuffer.remaining();
    }

    private void require(int n) throws WireParseException {
        if (n > remaining()) {
            throw new WireParseException("end of input");
        }
    }

    public void setActive(int len) {
        if (len > this.limit - this.byteBuffer.position()) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.byteBuffer.limit(this.byteBuffer.position() + len);
    }

    public void clearActive() {
        this.byteBuffer.limit(this.limit);
    }

    public int saveActive() {
        return this.byteBuffer.limit() - this.offset;
    }

    public void restoreActive(int pos) {
        if (this.offset + pos > this.limit) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.byteBuffer.limit(this.offset + pos);
    }

    public void jump(int index) {
        if (this.offset + index >= this.limit) {
            throw new IllegalArgumentException("cannot jump past end of input");
        }
        this.byteBuffer.position(this.offset + index);
        this.byteBuffer.limit(this.limit);
    }

    public void save() {
        this.savedPos = this.byteBuffer.position();
        this.savedEnd = this.byteBuffer.limit();
    }

    public void restore() {
        if (this.savedPos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.byteBuffer.position(this.savedPos);
        this.byteBuffer.limit(this.savedEnd);
        this.savedPos = -1;
        this.savedEnd = -1;
    }

    public int readU8() throws WireParseException {
        require(1);
        return this.byteBuffer.get() & UByte.MAX_VALUE;
    }

    public int readU16() throws WireParseException {
        require(2);
        return this.byteBuffer.getShort() & UShort.MAX_VALUE;
    }

    public long readU32() throws WireParseException {
        require(4);
        return this.byteBuffer.getInt() & 4294967295L;
    }

    public void readByteArray(byte[] b, int off, int len) throws WireParseException {
        require(len);
        this.byteBuffer.get(b, off, len);
    }

    public byte[] readByteArray(int len) throws WireParseException {
        require(len);
        byte[] out = new byte[len];
        this.byteBuffer.get(out, 0, len);
        return out;
    }

    public byte[] readByteArray() {
        int len = remaining();
        byte[] out = new byte[len];
        this.byteBuffer.get(out, 0, len);
        return out;
    }

    public byte[] readCountedString() throws WireParseException {
        int len = readU8();
        return readByteArray(len);
    }
}
