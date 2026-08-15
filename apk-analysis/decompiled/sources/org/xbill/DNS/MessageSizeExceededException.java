package org.xbill.DNS;

/* loaded from: classes8.dex */
public final class MessageSizeExceededException extends Exception {
    private final int maxSize;

    public int getMaxSize() {
        return this.maxSize;
    }

    MessageSizeExceededException(int maxSize) {
        super("Message size would exceed the allowed maximum of " + maxSize + " bytes");
        this.maxSize = maxSize;
    }
}
