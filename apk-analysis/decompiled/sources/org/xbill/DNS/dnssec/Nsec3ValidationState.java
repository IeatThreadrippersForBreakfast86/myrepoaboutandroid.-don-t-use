package org.xbill.DNS.dnssec;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.NSEC3Record;
import org.xbill.DNS.Name;
import org.xbill.DNS.utils.base32;

/* loaded from: classes8.dex */
class Nsec3ValidationState {
    private static final base32 b32 = new base32(base32.Alphabet.BASE32HEX, false, false);
    private final Map<String, Nsec3CacheEntry> cache = new HashMap();
    int numCalc;
    int numCalcErrors;

    Nsec3ValidationState() {
    }

    public Nsec3CacheEntry computeIfAbsent(NSEC3Record nsec3, Name name) throws NoSuchAlgorithmException {
        String key = key(nsec3, name);
        Nsec3CacheEntry entry = this.cache.get(key);
        if (entry == null) {
            byte[] hash = nsec3.hashName(name);
            Nsec3CacheEntry entry2 = new Nsec3CacheEntry(hash);
            this.cache.put(key, entry2);
            this.numCalc++;
            return entry2;
        }
        return entry;
    }

    static class Nsec3CacheEntry {
        private String asBase32;
        private final byte[] hash;

        public Nsec3CacheEntry(byte[] hash) {
            this.hash = hash;
        }

        public byte[] getHash() {
            return this.hash;
        }

        String getHashAsBase32() {
            if (this.asBase32 == null) {
                this.asBase32 = Nsec3ValidationState.b32.toString(this.hash);
            }
            return this.asBase32;
        }
    }

    private String key(NSEC3Record nsec3, Name name) {
        return name + "/" + nsec3.getHashAlgorithm() + "/" + nsec3.getIterations() + "/" + (nsec3.getSalt() == null ? "-" : new BigInteger(nsec3.getSalt()).toString());
    }
}
