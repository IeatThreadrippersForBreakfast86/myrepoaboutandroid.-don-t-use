package org.xbill.DNS.dnssec;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.xbill.DNS.Name;

/* loaded from: classes8.dex */
final class KeyCache {
    private static final int DEFAULT_MAX_CACHE_SIZE = 1000;
    private static final int DEFAULT_MAX_TTL = 900;
    public static final String MAX_CACHE_SIZE_CONFIG = "dnsjava.dnssec.keycache.max_size";
    public static final String MAX_TTL_CONFIG = "dnsjava.dnssec.keycache.max_ttl";
    private final Map<String, CacheEntry> cache;
    private final Clock clock;
    private int maxCacheSize;
    private long maxTtl;

    public KeyCache() {
        this(Clock.systemUTC());
    }

    public KeyCache(Clock clock) {
        this.maxTtl = 900L;
        this.maxCacheSize = 1000;
        this.clock = clock;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, CacheEntry>() { // from class: org.xbill.DNS.dnssec.KeyCache.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                return size() >= KeyCache.this.maxCacheSize;
            }
        });
    }

    public void init(Properties config) {
        if (config == null) {
            return;
        }
        String s = config.getProperty(MAX_TTL_CONFIG);
        if (s != null) {
            this.maxTtl = Long.parseLong(s);
        }
        String s2 = config.getProperty(MAX_CACHE_SIZE_CONFIG);
        if (s2 != null) {
            this.maxCacheSize = Integer.parseInt(s2);
        }
    }

    public KeyEntry find(Name n, int dclass) {
        while (n.labels() > 0) {
            String k = key(n, dclass);
            KeyEntry entry = lookupEntry(k);
            if (entry != null) {
                return entry;
            }
            n = new Name(n, 1);
        }
        return null;
    }

    public void store(KeyEntry ke) {
        if ((!ke.isGood() && !ke.isNull()) || ke.getType() != 48) {
            return;
        }
        String k = key(ke.getName(), ke.getDClass());
        CacheEntry ce = new CacheEntry(ke, this.maxTtl);
        this.cache.put(k, ce);
    }

    private String key(Name n, int dclass) {
        return "K" + dclass + "/" + n;
    }

    private KeyEntry lookupEntry(String key) {
        CacheEntry centry = this.cache.get(key);
        if (centry == null) {
            return null;
        }
        if (centry.expiration.isBefore(this.clock.instant())) {
            this.cache.remove(key);
            return null;
        }
        return centry.keyEntry;
    }

    private class CacheEntry {
        private final Instant expiration;
        private final KeyEntry keyEntry;

        CacheEntry(KeyEntry keyEntry, long maxTtl) {
            long ttl = keyEntry.getTTL();
            this.expiration = KeyCache.this.clock.instant().plus(ttl > maxTtl ? maxTtl : ttl, (TemporalUnit) ChronoUnit.SECONDS);
            this.keyEntry = keyEntry;
        }
    }
}
