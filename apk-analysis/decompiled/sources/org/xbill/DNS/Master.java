package org.xbill.DNS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer;

/* loaded from: classes8.dex */
public class Master implements AutoCloseable {
    private int currentDClass;
    private long currentTTL;
    private int currentType;
    private long defaultTTL;
    private File file;
    private Generator generator;
    private List<Generator> generators;
    private boolean includeThrowsException;
    private Master included;
    private Record last;
    private boolean needSOATTL;
    private boolean noExpandGenerate;
    private boolean noExpandIncludes;
    private Name origin;

    /* renamed from: st */
    private final Tokenizer f260st;

    Master(File file, Name origin, long initialTTL) throws IOException {
        this.last = null;
        this.included = null;
        if (origin != null && !origin.isAbsolute()) {
            throw new RelativeNameException(origin);
        }
        this.file = file;
        this.f260st = new Tokenizer(file);
        this.origin = origin;
        this.defaultTTL = initialTTL;
    }

    public Master(String filename, Name origin, long ttl) throws IOException {
        this(new File(filename), origin, ttl);
    }

    public Master(String filename, Name origin) throws IOException {
        this(new File(filename), origin, -1L);
    }

    public Master(String filename) throws IOException {
        this(new File(filename), (Name) null, -1L);
    }

    public Master(InputStream in, Name origin, long ttl) {
        this.last = null;
        this.included = null;
        if (origin != null && !origin.isAbsolute()) {
            throw new RelativeNameException(origin);
        }
        this.f260st = new Tokenizer(in);
        this.origin = origin;
        this.defaultTTL = ttl;
    }

    public Master(InputStream in, Name origin) {
        this(in, origin, -1L);
    }

    public Master(InputStream in) {
        this(in, (Name) null, -1L);
    }

    private Name parseName(String s, Name origin) throws TextParseException {
        try {
            return Name.fromString(s, origin);
        } catch (TextParseException e) {
            throw this.f260st.exception(e.getMessage());
        }
    }

    private void parseTTLClassAndType() throws IOException {
        boolean seenClass = false;
        String s = this.f260st.getString();
        int iValue = DClass.value(s);
        this.currentDClass = iValue;
        if (iValue >= 0) {
            s = this.f260st.getString();
            seenClass = true;
        }
        this.currentTTL = -1L;
        try {
            this.currentTTL = TTL.parseTTL(s);
            s = this.f260st.getString();
        } catch (NumberFormatException e) {
            if (this.defaultTTL >= 0) {
                this.currentTTL = this.defaultTTL;
            } else if (this.last != null) {
                this.currentTTL = this.last.getTTL();
            }
        }
        if (!seenClass) {
            int iValue2 = DClass.value(s);
            this.currentDClass = iValue2;
            if (iValue2 >= 0) {
                s = this.f260st.getString();
            } else {
                this.currentDClass = 1;
            }
        }
        int iValue3 = Type.value(s);
        this.currentType = iValue3;
        if (iValue3 < 0) {
            throw this.f260st.exception("Invalid type '" + s + "'");
        }
        if (this.currentTTL < 0) {
            if (this.currentType != 6) {
                throw this.f260st.exception("missing TTL");
            }
            this.needSOATTL = true;
            this.currentTTL = 0L;
        }
    }

    private long parseUInt32(String s) throws NumberFormatException {
        if (!Character.isDigit(s.charAt(0))) {
            return -1L;
        }
        try {
            long l = Long.parseLong(s);
            if (Utils.isUInt32(l)) {
                return l;
            }
            return -1L;
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private void startGenerate() throws IOException, NumberFormatException {
        long step;
        String s = this.f260st.getIdentifier();
        int n = s.indexOf("-");
        if (n < 0) {
            throw this.f260st.exception("Invalid $GENERATE range specifier: " + s);
        }
        String startstr = s.substring(0, n);
        String endstr = s.substring(n + 1);
        String stepstr = null;
        int n2 = endstr.indexOf("/");
        if (n2 >= 0) {
            stepstr = endstr.substring(n2 + 1);
            endstr = endstr.substring(0, n2);
        }
        long start = parseUInt32(startstr);
        long end = parseUInt32(endstr);
        if (stepstr != null) {
            step = parseUInt32(stepstr);
        } else {
            step = 1;
        }
        if (start < 0 || end < 0 || start > end || step <= 0) {
            throw this.f260st.exception("Invalid $GENERATE range specifier: " + s);
        }
        String nameSpec = this.f260st.getIdentifier();
        parseTTLClassAndType();
        if (!Generator.supportedType(this.currentType)) {
            throw this.f260st.exception("$GENERATE does not support " + Type.string(this.currentType) + " records");
        }
        String rdataSpec = this.f260st.getIdentifier();
        this.f260st.getEOL();
        this.f260st.unget();
        this.generator = new Generator(start, end, step, nameSpec, this.currentType, this.currentDClass, this.currentTTL, rdataSpec, this.origin);
        if (this.generators == null) {
            this.generators = new ArrayList(1);
        }
        this.generators.add(this.generator);
    }

    private void endGenerate() throws IOException {
        this.f260st.getEOL();
        this.generator = null;
    }

    private Record nextGenerated() throws IOException {
        try {
            return this.generator.nextRecord();
        } catch (TextParseException e) {
            throw this.f260st.exception("Parsing $GENERATE: " + e.getMessage());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:81:0x0185, code lost:
    
        parseTTLClassAndType();
        r11.last = org.xbill.DNS.Record.fromString(r1, r11.currentType, r11.currentDClass, r11.currentTTL, r11.f260st, r11.origin);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x019b, code lost:
    
        if (r11.needSOATTL == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x019d, code lost:
    
        r4 = ((org.xbill.DNS.SOARecord) r11.last).getMinimum();
        r11.last.setTTL(r4);
        r11.defaultTTL = r4;
        r11.needSOATTL = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01b0, code lost:
    
        return r11.last;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Record nextRecordInternal() throws IOException, NumberFormatException {
        Name name;
        if (this.included != null) {
            Record rec = this.included.nextRecord();
            if (rec != null) {
                return rec;
            }
            this.included = null;
        }
        if (this.generator != null) {
            Record rec2 = nextGenerated();
            if (rec2 != null) {
                return rec2;
            }
            endGenerate();
        }
        while (true) {
            Tokenizer.Token token = this.f260st.get(true, false);
            if (token.type() == 2) {
                Tokenizer.Token next = this.f260st.get();
                if (next.type() != 1) {
                    if (next.type() == 0) {
                        return null;
                    }
                    this.f260st.unget();
                    if (this.last == null) {
                        throw this.f260st.exception("no owner");
                    }
                    name = this.last.getName();
                }
            } else if (token.type() == 1) {
                continue;
            } else {
                if (token.type() == 0) {
                    return null;
                }
                if (token.value().charAt(0) == '$') {
                    String s = token.value();
                    if (s.equalsIgnoreCase("$ORIGIN")) {
                        this.origin = this.f260st.getName(Name.root);
                        this.f260st.getEOL();
                    } else if (s.equalsIgnoreCase("$TTL")) {
                        this.defaultTTL = this.f260st.getTTL();
                        this.f260st.getEOL();
                    } else if (s.equalsIgnoreCase("$INCLUDE")) {
                        if (this.noExpandIncludes) {
                            if (this.includeThrowsException) {
                                throw this.f260st.exception("$INCLUDE encountered, but processing disabled in strict mode");
                            }
                            this.f260st.getString();
                            this.f260st.getEOL();
                        } else {
                            String filename = this.f260st.getString();
                            File includeFile = new File(filename);
                            if (!includeFile.isAbsolute()) {
                                if (this.file != null) {
                                    includeFile = new File(this.file.getParent(), filename);
                                } else {
                                    throw this.f260st.exception("Cannot $INCLUDE using relative path when parsing from stream");
                                }
                            }
                            Name includeOrigin = this.origin;
                            Tokenizer.Token token2 = this.f260st.get();
                            if (token2.isString()) {
                                includeOrigin = parseName(token2.value(), Name.root);
                                this.f260st.getEOL();
                            }
                            this.included = new Master(includeFile, includeOrigin, this.defaultTTL);
                            return nextRecord();
                        }
                    } else if (s.equalsIgnoreCase("$GENERATE")) {
                        if (this.generator != null) {
                            throw new IllegalStateException("cannot nest $GENERATE");
                        }
                        startGenerate();
                        if (this.noExpandGenerate) {
                            endGenerate();
                        } else {
                            return nextGenerated();
                        }
                    } else {
                        throw this.f260st.exception("Invalid directive: " + s);
                    }
                } else {
                    Name name2 = parseName(token.value(), this.origin);
                    if (this.last != null && name2.equals(this.last.getName())) {
                        Name name3 = this.last.getName();
                        name = name3;
                    } else {
                        name = name2;
                    }
                }
            }
        }
    }

    public Record nextRecord() throws IOException {
        Record rec = null;
        try {
            rec = nextRecordInternal();
            return rec;
        } finally {
            if (rec == null) {
                this.f260st.close();
            }
        }
    }

    public void disableIncludes() {
        disableIncludes(false);
    }

    public void disableIncludes(boolean strict) {
        this.noExpandIncludes = true;
        this.includeThrowsException = strict;
    }

    public void expandGenerate(boolean wantExpand) {
        this.noExpandGenerate = !wantExpand;
    }

    public Iterator<Generator> generators() {
        if (this.generators != null) {
            return Collections.unmodifiableList(this.generators).iterator();
        }
        return Collections.emptyIterator();
    }

    @Override // java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.f260st != null) {
            this.f260st.close();
        }
    }
}
