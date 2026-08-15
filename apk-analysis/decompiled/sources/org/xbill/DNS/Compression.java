package org.xbill.DNS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes8.dex */
public class Compression {
    private static final int MAX_POINTER = 16383;
    private static final int TABLE_SIZE = 17;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Compression.class);
    private final Entry[] table = new Entry[17];

    private static class Entry {
        Name name;
        Entry next;
        int pos;

        private Entry() {
        }
    }

    public void add(int pos, Name name) {
        if (pos > MAX_POINTER) {
            return;
        }
        int row = (name.hashCode() & Integer.MAX_VALUE) % 17;
        Entry entry = new Entry();
        entry.name = name;
        entry.pos = pos;
        entry.next = this.table[row];
        this.table[row] = entry;
        log.trace("Adding {} at {}", name, Integer.valueOf(pos));
    }

    public int get(Name name) {
        int row = (name.hashCode() & Integer.MAX_VALUE) % 17;
        int pos = -1;
        for (Entry entry = this.table[row]; entry != null; entry = entry.next) {
            if (entry.name.equals(name)) {
                pos = entry.pos;
            }
        }
        log.trace("Looking for {}, found {}", name, Integer.valueOf(pos));
        return pos;
    }
}
