package org.xbill.DNS;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class Tokenizer implements AutoCloseable {
    public static final int COMMENT = 5;
    private static final String DEFAULT_DELIMITERS = " \t\n;()\"";
    public static final int EOF = 0;
    public static final int EOL = 1;
    public static final int IDENTIFIER = 3;
    public static final int QUOTED_STRING = 4;
    private static final String QUOTES = "\"";
    public static final int WHITESPACE = 2;
    private Token current;
    private String delimiters;
    private String filename;

    /* renamed from: is */
    private final PushbackInputStream f264is;
    private int line;
    private int multiline;
    private boolean quoting;

    /* renamed from: sb */
    private final StringBuilder f265sb;
    private boolean ungottenToken;
    private boolean wantClose;

    public static class Token {

        @Deprecated
        public int type;

        @Deprecated
        public String value;

        public int type() {
            return this.type;
        }

        public String value() {
            return this.value;
        }

        private Token(int type, StringBuilder value) {
            if (type < 0) {
                throw new IllegalArgumentException();
            }
            this.type = type;
            this.value = value == null ? null : value.toString();
        }

        public String toString() {
            switch (this.type) {
                case 0:
                    return "<eof>";
                case 1:
                    return "<eol>";
                case 2:
                    return "<whitespace>";
                case 3:
                    return "<identifier: " + this.value + ">";
                case 4:
                    return "<quoted_string: " + this.value + ">";
                case 5:
                    return "<comment: " + this.value + ">";
                default:
                    return "<unknown>";
            }
        }

        public boolean isString() {
            return this.type == 3 || this.type == 4;
        }

        public boolean isEOL() {
            return this.type == 1 || this.type == 0;
        }
    }

    public Tokenizer(InputStream is) {
        this.f264is = new PushbackInputStream(is instanceof BufferedInputStream ? is : new BufferedInputStream(is), 2);
        this.ungottenToken = false;
        this.multiline = 0;
        this.quoting = false;
        this.delimiters = DEFAULT_DELIMITERS;
        this.f265sb = new StringBuilder();
        this.filename = "<none>";
        this.line = 1;
    }

    public Tokenizer(String s) {
        this(new ByteArrayInputStream(s.getBytes()));
    }

    public Tokenizer(File f) throws FileNotFoundException {
        this(new FileInputStream(f));
        this.wantClose = true;
        this.filename = f.getName();
    }

    private int getChar() throws IOException {
        int c = this.f264is.read();
        if (c == 13) {
            int next = this.f264is.read();
            if (next != 10) {
                this.f264is.unread(next);
            }
            c = 10;
        }
        if (c == 10) {
            this.line++;
        }
        return c;
    }

    private void ungetChar(int c) throws IOException {
        if (c == -1) {
            return;
        }
        this.f264is.unread(c);
        if (c == 10) {
            this.line--;
        }
    }

    private int skipWhitespace() throws IOException {
        int c;
        int skipped = 0;
        while (true) {
            c = getChar();
            if (c != 32 && c != 9 && (c != 10 || this.multiline <= 0)) {
                break;
            }
            skipped++;
        }
        ungetChar(c);
        return skipped;
    }

    private void checkUnbalancedParens() throws TextParseException {
        if (this.multiline > 0) {
            throw exception("unbalanced parentheses");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0146, code lost:
    
        if (r2 == 4) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0148, code lost:
    
        checkUnbalancedParens();
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x014f, code lost:
    
        return setCurrentToken(0, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0156, code lost:
    
        return setCurrentToken(r2, r12.f265sb);
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x013a, code lost:
    
        ungetChar(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0144, code lost:
    
        if (r12.f265sb.length() != 0) goto L103;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Token get(boolean wantWhitespace, boolean wantComment) throws IOException {
        int c;
        if (this.ungottenToken) {
            this.ungottenToken = false;
            if (this.current.type == 2) {
                if (wantWhitespace) {
                    return this.current;
                }
            } else if (this.current.type == 5) {
                if (wantComment) {
                    return this.current;
                }
            } else {
                if (this.current.type == 1) {
                    this.line++;
                }
                return this.current;
            }
        }
        int skipped = skipWhitespace();
        if (skipped > 0 && wantWhitespace) {
            return setCurrentToken(2, null);
        }
        int type = 3;
        this.f265sb.setLength(0);
        while (true) {
            int c2 = getChar();
            if (c2 == -1 || this.delimiters.indexOf(c2) != -1) {
                if (c2 == -1) {
                    if (this.quoting) {
                        throw exception("EOF in quoted string");
                    }
                    if (this.f265sb.length() == 0) {
                        return setCurrentToken(0, null);
                    }
                    return setCurrentToken(type, this.f265sb);
                }
                if (this.f265sb.length() != 0 || type == 4) {
                    break;
                }
                if (c2 == 40) {
                    this.multiline++;
                    skipWhitespace();
                } else if (c2 == 41) {
                    if (this.multiline <= 0) {
                        throw exception("invalid close parenthesis");
                    }
                    this.multiline--;
                    skipWhitespace();
                } else if (c2 == 34) {
                    if (!this.quoting) {
                        this.quoting = true;
                        this.delimiters = QUOTES;
                        type = 4;
                    } else {
                        this.quoting = false;
                        this.delimiters = DEFAULT_DELIMITERS;
                        skipWhitespace();
                    }
                } else {
                    if (c2 == 10) {
                        return setCurrentToken(1, null);
                    }
                    if (c2 == 59) {
                        while (true) {
                            c = getChar();
                            if (c == 10 || c == -1) {
                                break;
                            }
                            this.f265sb.append((char) c);
                        }
                        if (wantComment) {
                            ungetChar(c);
                            return setCurrentToken(5, this.f265sb);
                        }
                        if (c == -1 && type != 4) {
                            checkUnbalancedParens();
                            return setCurrentToken(0, null);
                        }
                        if (this.multiline > 0) {
                            skipWhitespace();
                            this.f265sb.setLength(0);
                        } else {
                            return setCurrentToken(1, null);
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                }
            } else {
                if (c2 == 92) {
                    c2 = getChar();
                    if (c2 != -1) {
                        this.f265sb.append('\\');
                    } else {
                        throw exception("unterminated escape sequence");
                    }
                } else if (this.quoting && c2 == 10) {
                    throw exception("newline in quoted string");
                }
                this.f265sb.append((char) c2);
            }
        }
    }

    private Token setCurrentToken(int type, StringBuilder value) {
        this.current = new Token(type, value);
        return this.current;
    }

    public Token get() throws IOException {
        return get(false, false);
    }

    public void unget() {
        if (this.ungottenToken) {
            throw new IllegalStateException("Cannot unget multiple tokens");
        }
        if (this.current.type == 1) {
            this.line--;
        }
        this.ungottenToken = true;
    }

    public String getString() throws IOException {
        Token next = get();
        if (!next.isString()) {
            throw exception("expected a string");
        }
        return next.value;
    }

    private String getIdentifier(String expected) throws IOException {
        Token next = get();
        if (next.type != 3) {
            throw exception("expected " + expected);
        }
        return next.value;
    }

    public String getIdentifier() throws IOException {
        return getIdentifier("an identifier");
    }

    public long getLong() throws IOException {
        String next = getIdentifier("an integer");
        if (!Character.isDigit(next.charAt(0))) {
            throw exception("expected an integer");
        }
        try {
            return Long.parseLong(next);
        } catch (NumberFormatException e) {
            throw exception("expected an integer");
        }
    }

    public long getUInt32() throws IOException {
        long l = getLong();
        if (!Utils.isUInt32(l)) {
            throw exception("expected an 32 bit unsigned integer");
        }
        return l;
    }

    public int getUInt16() throws IOException {
        long l = getLong();
        if (!Utils.isUInt16(l)) {
            throw exception("expected an 16 bit unsigned integer");
        }
        return (int) l;
    }

    public int getUInt8() throws IOException {
        long l = getLong();
        if (!Utils.isUInt8(l)) {
            throw exception("expected an 8 bit unsigned integer");
        }
        return (int) l;
    }

    public long getTTL() throws IOException {
        String next = getIdentifier("a TTL value");
        try {
            return TTL.parseTTL(next);
        } catch (NumberFormatException e) {
            throw exception("expected a TTL value");
        }
    }

    public long getTTLLike() throws IOException {
        String next = getIdentifier("a TTL-like value");
        try {
            return TTL.parse(next, false);
        } catch (NumberFormatException e) {
            throw exception("expected a TTL-like value");
        }
    }

    public Name getName(Name origin) throws IOException {
        String next = getIdentifier("a name");
        try {
            Name name = Name.fromString(next, origin);
            if (!name.isAbsolute()) {
                throw new RelativeNameException(name);
            }
            return name;
        } catch (TextParseException e) {
            throw exception(e.getMessage());
        }
    }

    public byte[] getAddressBytes(int family) throws IOException, NumberFormatException {
        String next = getIdentifier("an address");
        byte[] bytes = null;
        if (family == 1) {
            bytes = IPAddressUtils.parseV4(next);
        } else if (family == 2) {
            bytes = IPAddressUtils.parseV6(next);
        }
        if (bytes != null) {
            return bytes;
        }
        throw exception("Invalid address: " + next);
    }

    public InetAddress getAddress(int family) throws IOException {
        String next = getIdentifier("an address");
        try {
            return Address.getByAddress(next, family);
        } catch (UnknownHostException e) {
            throw exception(e.getMessage());
        }
    }

    public void getEOL() throws IOException {
        Token next = get();
        if (next.type != 1 && next.type != 0) {
            throw exception("expected EOL or EOF");
        }
    }

    private String remainingStrings() throws IOException {
        StringBuilder buffer = null;
        while (true) {
            Token t = get();
            if (!t.isString()) {
                break;
            }
            if (buffer == null) {
                buffer = new StringBuilder();
            }
            buffer.append(t.value);
        }
        unget();
        if (buffer == null) {
            return null;
        }
        return buffer.toString();
    }

    public byte[] getBase64(boolean required) throws IOException {
        String s = remainingStrings();
        if (s == null) {
            if (required) {
                throw exception("expected base64 encoded string");
            }
            return null;
        }
        byte[] array = base64.fromString(s);
        if (array == null) {
            throw exception("invalid base64 encoding");
        }
        return array;
    }

    public byte[] getBase64() throws IOException {
        return getBase64(false);
    }

    public byte[] getHex(boolean required) throws IOException {
        String s = remainingStrings();
        if (s == null) {
            if (required) {
                throw exception("expected hex encoded string");
            }
            return null;
        }
        byte[] array = base16.fromString(s);
        if (array == null) {
            throw exception("invalid hex encoding");
        }
        return array;
    }

    public byte[] getHex() throws IOException {
        return getHex(false);
    }

    public byte[] getHexString() throws IOException {
        String next = getIdentifier("a hex string");
        byte[] array = base16.fromString(next);
        if (array == null) {
            throw exception("invalid hex encoding");
        }
        return array;
    }

    public byte[] getBase32String(base32 b32) throws IOException {
        String next = getIdentifier("a base32 string");
        byte[] array = b32.fromString(next);
        if (array == null) {
            throw exception("invalid base32 encoding");
        }
        return array;
    }

    public TextParseException exception(String s) {
        return new TextParseException(this.filename + ":" + this.line + ": " + s);
    }

    @Override // java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.wantClose) {
            try {
                this.f264is.close();
            } catch (IOException e) {
            }
        }
    }
}
