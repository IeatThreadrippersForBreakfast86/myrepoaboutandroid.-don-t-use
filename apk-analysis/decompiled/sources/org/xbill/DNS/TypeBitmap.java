package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;
import org.xbill.DNS.Tokenizer;

/* loaded from: classes8.dex */
final class TypeBitmap implements Serializable {
    private static final long serialVersionUID = -125354057735389003L;
    private final TreeSet<Integer> types;

    private TypeBitmap() {
        this.types = new TreeSet<>();
    }

    public TypeBitmap(int[] array) {
        this();
        for (int value : array) {
            Type.check(value);
            this.types.add(Integer.valueOf(value));
        }
    }

    public TypeBitmap(DNSInput in) throws WireParseException {
        this();
        while (in.remaining() > 0) {
            if (in.remaining() < 2) {
                throw new WireParseException("invalid bitmap descriptor");
            }
            int windowBlockNumber = getWindowBlockNumber(in, -1);
            int mapLength = getMapLength(in);
            for (int i = 0; i < mapLength; i++) {
                int bitmapByte = in.readU8();
                for (int j = 0; j < 8 && bitmapByte > 0; j++) {
                    if (((1 << (7 - j)) & bitmapByte) != 0) {
                        this.types.add(Integer.valueOf((windowBlockNumber * 256) + (i * 8) + j));
                    }
                }
            }
        }
    }

    private static int getWindowBlockNumber(DNSInput in, int lastWindowBlockNumber) throws WireParseException {
        int windowBlockNumber = in.readU8();
        if (windowBlockNumber < lastWindowBlockNumber) {
            throw new WireParseException("invalid ordering");
        }
        return windowBlockNumber;
    }

    private static int getMapLength(DNSInput in) throws WireParseException {
        int mapLength = in.readU8();
        if (mapLength > in.remaining()) {
            throw new WireParseException("invalid bitmap");
        }
        return mapLength;
    }

    public TypeBitmap(Tokenizer st) throws IOException {
        this();
        while (true) {
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                int typecode = Type.value(t.value());
                if (typecode < 0) {
                    throw st.exception("Invalid type: " + t.value());
                }
                this.types.add(Integer.valueOf(typecode));
            } else {
                st.unget();
                return;
            }
        }
    }

    public int[] toArray() {
        int[] array = new int[this.types.size()];
        int n = 0;
        Iterator<Integer> it = this.types.iterator();
        while (it.hasNext()) {
            Integer type = it.next();
            array[n] = type.intValue();
            n++;
        }
        return array;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = this.types.iterator();
        while (it.hasNext()) {
            int t = it.next().intValue();
            sb.append(Type.string(t));
            if (it.hasNext()) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static void mapToWire(DNSOutput out, TreeSet<Integer> map, int mapbase) {
        int arraymax = map.last().intValue() & 255;
        int arraylength = (arraymax / 8) + 1;
        int[] array = new int[arraylength];
        out.writeU8(mapbase);
        out.writeU8(arraylength);
        Iterator<Integer> it = map.iterator();
        while (it.hasNext()) {
            int typecode = it.next().intValue();
            int i = (typecode & 255) / 8;
            array[i] = array[i] | (1 << (7 - (typecode % 8)));
        }
        for (int j = 0; j < arraylength; j++) {
            out.writeU8(array[j]);
        }
    }

    public void toWire(DNSOutput out) {
        if (this.types.isEmpty()) {
            return;
        }
        int mapbase = -1;
        TreeSet<Integer> map = new TreeSet<>();
        Iterator<Integer> it = this.types.iterator();
        while (it.hasNext()) {
            Integer type = it.next();
            int t = type.intValue();
            int base = t >> 8;
            if (base != mapbase) {
                if (!map.isEmpty()) {
                    mapToWire(out, map, mapbase);
                    map.clear();
                }
                mapbase = base;
            }
            map.add(Integer.valueOf(t));
        }
        mapToWire(out, map, mapbase);
    }

    public boolean empty() {
        return this.types.isEmpty();
    }

    public boolean contains(int typecode) {
        return this.types.contains(Integer.valueOf(typecode));
    }
}
