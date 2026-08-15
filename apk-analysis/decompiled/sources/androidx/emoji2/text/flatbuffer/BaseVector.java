package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class BaseVector {

    /* renamed from: bb */
    protected ByteBuffer f85bb;
    private int element_size;
    private int length;
    private int vector;

    protected int __vector() {
        return this.vector;
    }

    protected int __element(int j) {
        return this.vector + (this.element_size * j);
    }

    protected void __reset(int _vector, int _element_size, ByteBuffer _bb) {
        this.f85bb = _bb;
        if (this.f85bb != null) {
            this.vector = _vector;
            this.length = this.f85bb.getInt(_vector - 4);
            this.element_size = _element_size;
        } else {
            this.vector = 0;
            this.length = 0;
            this.element_size = 0;
        }
    }

    public void reset() {
        __reset(0, 0, null);
    }

    public int length() {
        return this.length;
    }
}
