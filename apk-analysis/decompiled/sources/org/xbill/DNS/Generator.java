package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kotlin.UByte;

/* loaded from: classes8.dex */
public class Generator {
    private long current;
    public final int dclass;
    public long end;
    public final String namePattern;
    public final Name origin;
    public final String rdataPattern;
    public long start;
    public long step;
    public final long ttl;
    public final int type;

    public static boolean supportedType(int type) {
        Type.check(type);
        return type == 12 || type == 5 || type == 39 || type == 1 || type == 28 || type == 2;
    }

    public Generator(long start, long end, long step, String namePattern, int type, int dclass, long ttl, String rdataPattern, Name origin) {
        if (start < 0 || end < 0 || start > end || step <= 0) {
            throw new IllegalArgumentException("invalid range specification");
        }
        if (!supportedType(type)) {
            throw new IllegalArgumentException("unsupported type");
        }
        DClass.check(dclass);
        this.start = start;
        this.end = end;
        this.step = step;
        this.namePattern = namePattern;
        this.type = type;
        this.dclass = dclass;
        this.ttl = ttl;
        this.rdataPattern = rdataPattern;
        this.origin = origin;
        this.current = start;
    }

    private String substitute(String spec, long n) throws IOException {
        byte[] str;
        String number;
        boolean escaped;
        boolean negative;
        boolean escaped2 = false;
        byte[] str2 = spec.getBytes();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        loop0: while (i < str2.length) {
            char c = (char) (str2[i] & UByte.MAX_VALUE);
            if (escaped2) {
                sb.append(c);
                escaped2 = false;
                str = str2;
            } else if (c == '\\') {
                if (i + 1 == str2.length) {
                    throw new TextParseException("invalid escape character");
                }
                escaped2 = true;
                str = str2;
            } else if (c == '$') {
                boolean negative2 = false;
                long offset = 0;
                long width = 0;
                long base = 10;
                boolean wantUpperCase = false;
                if (i + 1 < str2.length && str2[i + 1] == 36) {
                    i++;
                    sb.append((char) (str2[i] & UByte.MAX_VALUE));
                    str = str2;
                } else {
                    if (i + 1 < str2.length && str2[i + 1] == 123) {
                        int i2 = i + 1;
                        if (i2 + 1 < str2.length && str2[i2 + 1] == 45) {
                            negative2 = true;
                            i2++;
                        }
                        while (i2 + 1 < str2.length) {
                            i2++;
                            c = (char) (str2[i2] & UByte.MAX_VALUE);
                            if (c == ',' || c == '}') {
                                break;
                            }
                            if (c < '0' || c > '9') {
                                throw new TextParseException("invalid offset");
                            }
                            c = (char) (c - '0');
                            offset = (offset * 10) + c;
                        }
                        if (negative2) {
                            offset = -offset;
                        }
                        if (c != ',') {
                            negative = negative2;
                        } else {
                            while (true) {
                                if (i2 + 1 >= str2.length) {
                                    negative = negative2;
                                    break;
                                }
                                i2++;
                                c = (char) (str2[i2] & UByte.MAX_VALUE);
                                if (c == ',') {
                                    negative = negative2;
                                    break;
                                }
                                if (c == '}') {
                                    negative = negative2;
                                    break;
                                }
                                if (c < '0' || c > '9') {
                                    break loop0;
                                }
                                c = (char) (c - '0');
                                width = (width * 10) + c;
                                negative2 = negative2;
                            }
                            throw new TextParseException("invalid width");
                        }
                        if (c == ',') {
                            if (i2 + 1 == str2.length) {
                                throw new TextParseException("invalid base");
                            }
                            i2++;
                            char c2 = (char) (str2[i2] & UByte.MAX_VALUE);
                            if (c2 == 'o') {
                                base = 8;
                            } else if (c2 == 'x') {
                                base = 16;
                            } else if (c2 == 'X') {
                                wantUpperCase = true;
                                base = 16;
                            } else if (c2 != 'd') {
                                throw new TextParseException("invalid base");
                            }
                        }
                        if (i2 + 1 == str2.length || str2[i2 + 1] != 125) {
                            throw new TextParseException("invalid modifiers");
                        }
                        i = i2 + 1;
                    }
                    long v = n + offset;
                    if (v < 0) {
                        throw new TextParseException("invalid offset expansion");
                    }
                    if (base == 8) {
                        number = Long.toOctalString(v);
                    } else if (base == 16) {
                        number = Long.toHexString(v);
                    } else {
                        number = Long.toString(v);
                    }
                    if (wantUpperCase) {
                        number = number.toUpperCase();
                    }
                    if (width != 0) {
                        escaped = escaped2;
                        str = str2;
                        if (width > number.length()) {
                            int zeros = ((int) width) - number.length();
                            while (true) {
                                int zeros2 = zeros - 1;
                                if (zeros <= 0) {
                                    break;
                                }
                                sb.append('0');
                                zeros = zeros2;
                            }
                        }
                    } else {
                        escaped = escaped2;
                        str = str2;
                    }
                    sb.append(number);
                    escaped2 = escaped;
                }
            } else {
                str = str2;
                sb.append(c);
            }
            i++;
            str2 = str;
        }
        return sb.toString();
    }

    public Record nextRecord() throws IOException {
        if (this.current > this.end) {
            return null;
        }
        String namestr = substitute(this.namePattern, this.current);
        Name name = Name.fromString(namestr, this.origin);
        String rdata = substitute(this.rdataPattern, this.current);
        this.current += this.step;
        return Record.fromString(name, this.type, this.dclass, this.ttl, rdata, this.origin);
    }

    public Record[] expand() throws IOException {
        List<Record> list = new ArrayList<>();
        long i = this.start;
        while (i < this.end) {
            String namestr = substitute(this.namePattern, this.current);
            Name name = Name.fromString(namestr, this.origin);
            String rdata = substitute(this.rdataPattern, this.current);
            list.add(Record.fromString(name, this.type, this.dclass, this.ttl, rdata, this.origin));
            i += this.step;
        }
        return (Record[]) list.toArray(new Record[0]);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("$GENERATE ");
        sb.append(this.start).append("-").append(this.end);
        if (this.step > 1) {
            sb.append("/").append(this.step);
        }
        sb.append(" ");
        sb.append(this.namePattern).append(" ");
        sb.append(this.ttl).append(" ");
        if (this.dclass != 1 || !Options.check("noPrintIN")) {
            sb.append(DClass.string(this.dclass)).append(" ");
        }
        sb.append(Type.string(this.type)).append(" ");
        sb.append(this.rdataPattern).append(" ");
        return sb.toString();
    }
}
