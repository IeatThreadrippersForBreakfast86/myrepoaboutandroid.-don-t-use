package org.xbill.DNS;

import java.io.IOException;

/* loaded from: classes8.dex */
public class TextParseException extends IOException {
    public TextParseException() {
    }

    public TextParseException(String s) {
        super(s);
    }

    public TextParseException(String name, String message) {
        super("'" + name + "': " + message);
    }

    public TextParseException(String name, String message, Exception inner) {
        super("'" + name + "': " + message, inner);
    }
}
