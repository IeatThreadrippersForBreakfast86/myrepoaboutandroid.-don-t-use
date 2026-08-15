package org.xbill.DNS.dnssec;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.NSEC3Record;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.DNS.dnssec.Nsec3ValidationState;
import org.xbill.DNS.utils.base32;

/* loaded from: classes8.dex */
final class NSEC3ValUtils {
    private static final int MAX_ITERATION_COUNT = 65536;
    private static final int MAX_NSEC3_CALCULATIONS = 8;
    private static final int MAX_NSEC3_ERRORS = -1;
    public static final String NSEC3_MAX_ITERATIONS_PROPERTY_PREFIX = "dnsjava.dnssec.nsec3.iterations";
    private final TreeMap<Integer, Integer> maxIterations = new TreeMap<>();
    private static final Logger log = LoggerFactory.getLogger((Class<?>) NSEC3ValUtils.class);
    private static final Name ASTERISK_LABEL = Name.fromConstantString(Marker.ANY_MARKER);

    NSEC3ValUtils() {
        this.maxIterations.put(1024, 150);
        this.maxIterations.put(2048, 500);
        this.maxIterations.put(4096, 2500);
    }

    void init(Properties config) throws NumberFormatException {
        boolean first = true;
        for (Map.Entry<?, ?> s : config.entrySet()) {
            String key = s.getKey().toString();
            if (key.startsWith(NSEC3_MAX_ITERATIONS_PROPERTY_PREFIX)) {
                int keySize = Integer.parseInt(key.substring(key.lastIndexOf(".") + 1));
                int iterations = Integer.parseInt(s.getValue().toString());
                if (iterations > 65536) {
                    throw new IllegalArgumentException(iterations + " iterations is too high, maximum is 65536");
                }
                if (first) {
                    first = false;
                    this.maxIterations.clear();
                }
                this.maxIterations.put(Integer.valueOf(keySize), Integer.valueOf(iterations));
            }
        }
    }

    private static final class CEResponse {
        private final NSEC3Record ceNsec3;
        private final Name closestEncloser;
        private NSEC3Record ncNsec3;
        private SecurityStatus status;

        private CEResponse(Name ce, NSEC3Record nsec3) {
            this.status = SecurityStatus.UNCHECKED;
            this.closestEncloser = ce;
            this.ceNsec3 = nsec3;
        }
    }

    private boolean supportsHashAlgorithm(int alg) {
        return alg == 1;
    }

    public void stripUnknownAlgNSEC3s(List<SRRset> nsec3s) {
        ListIterator<SRRset> i = nsec3s.listIterator();
        while (i.hasNext()) {
            NSEC3Record nsec3 = (NSEC3Record) i.next().first();
            if (!supportsHashAlgorithm(nsec3.getHashAlgorithm())) {
                i.remove();
            }
        }
    }

    private Name ceWildcard(Name closestEncloser) {
        try {
            return Name.concatenate(ASTERISK_LABEL, closestEncloser);
        } catch (NameTooLongException e) {
            return null;
        }
    }

    private Name nextClosest(Name qname, Name closestEncloser) {
        int strip = (qname.labels() - closestEncloser.labels()) - 1;
        return strip > 0 ? new Name(qname, strip) : qname;
    }

    private NSEC3Record findMatchingNSEC3(Name name, Name zonename, List<SRRset> nsec3s, Nsec3ValidationState state) {
        NSEC3Record nsec3;
        Name complete;
        for (SRRset set : nsec3s) {
            if (state.numCalc >= 8) {
                if (state.numCalc == state.numCalcErrors) {
                    log.debug("NSEC3 reached max. hash calculation errors");
                    state.numCalc = -1;
                    return null;
                }
                log.debug("NSEC3 reached max. hash calculations");
                return null;
            }
            try {
                nsec3 = (NSEC3Record) set.first();
                Nsec3ValidationState.Nsec3CacheEntry hash = state.computeIfAbsent(nsec3, name);
                complete = new Name(hash.getHashAsBase32(), zonename);
            } catch (NoSuchAlgorithmException | TextParseException e) {
                state.numCalcErrors++;
                log.debug("Unrecognized NSEC3 in set: {}", set, e);
            }
            if (complete.equals(nsec3.getName())) {
                return nsec3;
            }
        }
        return null;
    }

    private boolean nsec3Covers(NSEC3Record nsec3, Name zonename, byte[] hash) throws IOException {
        if (!new Name(nsec3.getName(), 1).equals(zonename)) {
            return false;
        }
        byte[] owner = new base32(base32.Alphabet.BASE32HEX, false, false).fromString(nsec3.getName().getLabelString(0));
        byte[] next = nsec3.getNext();
        if (ByteArrayComparator.compare(owner, hash) >= 0 || ByteArrayComparator.compare(hash, next) >= 0) {
            return ByteArrayComparator.compare(next, owner) <= 0 && (ByteArrayComparator.compare(hash, owner) > 0 || ByteArrayComparator.compare(hash, next) < 0);
        }
        return true;
    }

    private NSEC3Record findCoveringNSEC3(Name name, Name zonename, List<SRRset> nsec3s, Nsec3ValidationState state) {
        NSEC3Record nsec3;
        Nsec3ValidationState.Nsec3CacheEntry hash;
        for (SRRset set : nsec3s) {
            if (state.numCalc >= 8) {
                if (state.numCalcErrors == state.numCalc) {
                    state.numCalc = -1;
                }
                return null;
            }
            try {
                nsec3 = (NSEC3Record) set.first();
                hash = state.computeIfAbsent(nsec3, name);
            } catch (NoSuchAlgorithmException e) {
                state.numCalcErrors++;
                log.debug("Unrecognized NSEC3 in set: {}", set, e);
            }
            if (nsec3Covers(nsec3, zonename, hash.getHash())) {
                return nsec3;
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
    
        org.xbill.DNS.dnssec.NSEC3ValUtils.log.debug("NSEC3 reached max. hash calculations");
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0034, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CEResponse findClosestEncloser(Name name, Name zonename, List<SRRset> nsec3s, Nsec3ValidationState state) {
        while (true) {
            if (name.labels() < zonename.labels()) {
                break;
            }
            if (state.numCalc >= 8 || state.numCalc == -1) {
                break;
            }
            NSEC3Record nsec3 = findMatchingNSEC3(name, zonename, nsec3s, state);
            if (nsec3 != null) {
                return new CEResponse(name, nsec3);
            }
            name = new Name(name, 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CEResponse proveClosestEncloser(Name name, Name name2, List<SRRset> list, Nsec3ValidationState nsec3ValidationState) {
        CEResponse cEResponseFindClosestEncloser = findClosestEncloser(name, name2, list, nsec3ValidationState);
        if (cEResponseFindClosestEncloser != null) {
            if (!cEResponseFindClosestEncloser.closestEncloser.equals(name)) {
                if (!cEResponseFindClosestEncloser.ceNsec3.hasType(2) || cEResponseFindClosestEncloser.ceNsec3.hasType(6)) {
                    if (!cEResponseFindClosestEncloser.ceNsec3.hasType(39)) {
                        cEResponseFindClosestEncloser.ncNsec3 = findCoveringNSEC3(nextClosest(name, cEResponseFindClosestEncloser.closestEncloser), name2, list, nsec3ValidationState);
                        if (cEResponseFindClosestEncloser.ncNsec3 != null) {
                            cEResponseFindClosestEncloser.status = SecurityStatus.SECURE;
                            return cEResponseFindClosestEncloser;
                        }
                        log.debug("Could not find proof that the closest encloser was the closest encloser");
                        cEResponseFindClosestEncloser.status = SecurityStatus.BOGUS;
                        return cEResponseFindClosestEncloser;
                    }
                    log.debug("Closest encloser was a DNAME!");
                    cEResponseFindClosestEncloser.status = SecurityStatus.BOGUS;
                    return cEResponseFindClosestEncloser;
                }
                if (!cEResponseFindClosestEncloser.ceNsec3.hasType(43)) {
                    cEResponseFindClosestEncloser.status = SecurityStatus.INSECURE;
                    return cEResponseFindClosestEncloser;
                }
                log.debug("Closest encloser was a delegation!");
                cEResponseFindClosestEncloser.status = SecurityStatus.BOGUS;
                return cEResponseFindClosestEncloser;
            }
            log.debug("Proved that qname existed!");
            cEResponseFindClosestEncloser.status = SecurityStatus.BOGUS;
            return cEResponseFindClosestEncloser;
        }
        log.debug("Could not find a candidate for the closest encloser");
        CEResponse cEResponse = new CEResponse(Name.empty, null);
        cEResponse.status = SecurityStatus.BOGUS;
        return cEResponse;
    }

    private boolean validIterations(SRRset nsec, KeyCache keyCache) {
        int keysize;
        SRRset dnskeyRrset = keyCache.find(nsec.getSignerName(), nsec.getDClass());
        if (dnskeyRrset == null) {
            return false;
        }
        int smallestKeySize = Integer.MAX_VALUE;
        try {
            for (Record r : dnskeyRrset.rrs(false)) {
                DNSKEYRecord dnskey = (DNSKEYRecord) r;
                if ((dnskey.getFlags() & 256) == 256) {
                    switch (dnskey.getAlgorithm()) {
                        case 1:
                            return false;
                        case 2:
                        case 4:
                        case 9:
                        case 11:
                        default:
                            return false;
                        case 3:
                        case 6:
                            keysize = ((DSAPublicKey) dnskey.getPublicKey()).getParams().getP().bitLength();
                            break;
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                            keysize = ((RSAPublicKey) dnskey.getPublicKey()).getModulus().bitLength();
                            break;
                        case 12:
                            keysize = 512;
                            break;
                        case 13:
                        case 14:
                            keysize = ((ECPublicKey) dnskey.getPublicKey()).getParams().getCurve().getField().getFieldSize();
                            break;
                        case 15:
                            keysize = 256;
                            break;
                        case 16:
                            keysize = 456;
                            break;
                    }
                    if (keysize < smallestKeySize) {
                        smallestKeySize = keysize;
                    }
                }
            }
            Integer maxIterationsForKeySet = this.maxIterations.floorKey(Integer.valueOf(smallestKeySize));
            if (maxIterationsForKeySet == null) {
                maxIterationsForKeySet = this.maxIterations.firstKey();
            }
            return ((NSEC3Record) nsec.first()).getIterations() <= this.maxIterations.get(maxIterationsForKeySet).intValue();
        } catch (DNSSEC.DNSSECException e) {
            log.error("Could not get public key from NSEC3 record", (Throwable) e);
            return false;
        }
    }

    public boolean allNSEC3sIgnorable(List<SRRset> nsec3s, KeyCache dnskeyRrset) {
        Map<Name, NSEC3Record> foundNsecs = new HashMap<>();
        for (SRRset set : nsec3s) {
            for (Record r : set.rrs()) {
                NSEC3Record current = (NSEC3Record) r;
                Name key = new Name(current.getName(), 1);
                NSEC3Record previous = foundNsecs.get(key);
                if (previous != null) {
                    if (current.getHashAlgorithm() != previous.getHashAlgorithm() || current.getIterations() != previous.getIterations()) {
                        return true;
                    }
                    if ((current.getSalt() == null) ^ (previous.getSalt() == null)) {
                        return true;
                    }
                    if (current.getSalt() != null && ByteArrayComparator.compare(current.getSalt(), previous.getSalt()) != 0) {
                        return true;
                    }
                } else {
                    foundNsecs.put(key, current);
                }
            }
        }
        for (SRRset set2 : nsec3s) {
            if (validIterations(set2, dnskeyRrset)) {
                return false;
            }
        }
        return true;
    }

    public SecurityStatus proveNameError(List<SRRset> nsec3s, Name qname, Name zonename, Nsec3ValidationState state) {
        if (nsec3s == null || nsec3s.isEmpty()) {
            return SecurityStatus.BOGUS;
        }
        CEResponse ce = proveClosestEncloser(qname, zonename, nsec3s, state);
        if (ce.status == SecurityStatus.SECURE) {
            Name wc = ceWildcard(ce.closestEncloser);
            if (wc == null) {
                return SecurityStatus.BOGUS;
            }
            NSEC3Record nsec3 = findCoveringNSEC3(wc, zonename, nsec3s, state);
            if (nsec3 != null) {
                if ((ce.ncNsec3.getFlags() & 1) == 1) {
                    log.debug("NSEC3 nameerror proof: nc has optout");
                    return SecurityStatus.INSECURE;
                }
                return SecurityStatus.SECURE;
            }
            log.debug("Could not prove that the applicable wildcard did not exist");
            if (state.numCalc == -1) {
                log.debug("NSEC3 reached max. hash calculation errors");
                return SecurityStatus.BOGUS;
            }
            if (state.numCalc == 8) {
                log.debug("NSEC3 reached max. hash calculations");
                return SecurityStatus.UNCHECKED;
            }
            return SecurityStatus.BOGUS;
        }
        log.debug("Failed to prove a closest encloser");
        return ce.status;
    }

    public JustifiedSecStatus proveNodata(List<SRRset> nsec3s, Name qname, int qtype, Name zonename, Nsec3ValidationState state) {
        if (nsec3s == null || nsec3s.isEmpty()) {
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 12, C1336R.get("failed.nsec3.none", new Object[0]));
        }
        NSEC3Record nsec3 = findMatchingNSEC3(qname, zonename, nsec3s, state);
        if (nsec3 != null) {
            if (nsec3.hasType(qtype)) {
                log.debug("Matching NSEC3 proved that type existed!");
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.type_exists", new Object[0]));
            }
            if (nsec3.hasType(5)) {
                log.debug("Matching NSEC3 proved that a CNAME existed!");
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.cname_exists", new Object[0]));
            }
            if (qtype == 43 && nsec3.hasType(6) && !Name.root.equals(qname)) {
                log.debug("Apex NSEC3 abused for no DS proof, bogus");
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.apex_abuse", new Object[0]));
            }
            if (qtype != 43 && nsec3.hasType(2) && !nsec3.hasType(6)) {
                if (!nsec3.hasType(43)) {
                    log.debug("Matching NSEC3 is insecure delegation");
                    return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, null);
                }
                log.debug("Matching NSEC3 is a delegation, bogus");
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.delegation", new Object[0]));
            }
            return new JustifiedSecStatus(SecurityStatus.SECURE, -1, null);
        }
        if (state.numCalc == -1) {
            log.debug("NSEC3 reached max. hash calculation errors");
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.hash_errors", new Object[0]));
        }
        if (state.numCalc == 8) {
            log.debug("NSEC3 reached max. hash calculations");
            return new JustifiedSecStatus(SecurityStatus.UNCHECKED, -1, null);
        }
        CEResponse ce = proveClosestEncloser(qname, zonename, nsec3s, state);
        if (ce.status != SecurityStatus.BOGUS) {
            if (ce.status != SecurityStatus.INSECURE || qtype == 43) {
                if (ce.status != SecurityStatus.UNCHECKED) {
                    Name wc = ceWildcard(ce.closestEncloser);
                    NSEC3Record nsec32 = findMatchingNSEC3(wc, zonename, nsec3s, state);
                    if (nsec32 != null) {
                        if (nsec32.hasType(qtype)) {
                            log.debug("Matching wildcard has qtype {}", Type.string(qtype));
                            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.type_exists_wc", new Object[0]));
                        }
                        if (nsec32.hasType(5)) {
                            log.debug("Matching wildcard has a CNAME, bogus");
                            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.cname_exists_wc", new Object[0]));
                        }
                        if (qtype == 43 && qname.labels() != 1 && nsec32.hasType(6)) {
                            log.debug("Matching wildcard for no DS proof has a SOA, bogus");
                            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.wc_soa", new Object[0]));
                        }
                        if (qtype == 43 || !nsec32.hasType(2) || nsec32.hasType(6)) {
                            if (ce.ncNsec3 != null && (ce.ncNsec3.getFlags() & 1) == 1) {
                                log.debug("Matching wildcard is in opt-out range, insecure");
                                return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, null);
                            }
                            return new JustifiedSecStatus(SecurityStatus.SECURE, -1, null);
                        }
                        log.debug("Matching wildcard is a delegation, bogus");
                        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.delegation_wc", new Object[0]));
                    }
                    if (state.numCalc == -1) {
                        log.debug("NSEC3 reached max. hash calculation errors");
                        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.wc.hash_errors", new Object[0]));
                    }
                    if (state.numCalc != 8) {
                        if (ce.ncNsec3 != null) {
                            if ((ce.ncNsec3.getFlags() & 1) == 0) {
                                if (qtype != 43) {
                                    log.debug("Covering NSEC3 was not opt-out in an opt-out DS NOERROR/NODATA case");
                                    return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.not_optout", new Object[0]));
                                }
                                log.debug("Could not find matching NSEC3, nor matching wildcard, and qtype is not DS -- no more options");
                                return new JustifiedSecStatus(SecurityStatus.BOGUS, 12, C1336R.get("failed.nsec3.not_found", new Object[0]));
                            }
                            return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, null);
                        }
                        log.debug("No next closer NSEC3");
                        return new JustifiedSecStatus(SecurityStatus.BOGUS, 12, C1336R.get("failed.nsec3.no_next", new Object[0]));
                    }
                    log.debug("NSEC3 reached max. hash calculations");
                    return new JustifiedSecStatus(SecurityStatus.UNCHECKED, -1, null);
                }
                return new JustifiedSecStatus(SecurityStatus.UNCHECKED, -1, null);
            }
            log.debug("Closest NSEC3 is insecure delegation");
            return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, null);
        }
        log.debug("Did not match qname, nor found a proven closest encloser");
        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.nsec3.qname_ce", new Object[0]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SecurityStatus proveWildcard(List<SRRset> list, Name name, Name name2, Name name3, Nsec3ValidationState nsec3ValidationState) {
        if (list == null || list.isEmpty() || name == null || name3 == null) {
            return SecurityStatus.BOGUS;
        }
        CEResponse cEResponse = new CEResponse(new Name(name3, 1), null);
        cEResponse.ncNsec3 = findCoveringNSEC3(nextClosest(name, cEResponse.closestEncloser), name2, list, nsec3ValidationState);
        if (cEResponse.ncNsec3 != null) {
            if ((cEResponse.ncNsec3.getFlags() & 1) == 1) {
                return SecurityStatus.INSECURE;
            }
            return SecurityStatus.SECURE;
        }
        log.debug("did not find a covering NSEC3 that covered the next closer name to {} from {} (derived from wildcard {})", name, cEResponse.closestEncloser, name3);
        return SecurityStatus.BOGUS;
    }

    public SecurityStatus proveNoDS(List<SRRset> nsec3s, Name qname, Name zonename, Nsec3ValidationState state) {
        if (nsec3s == null || nsec3s.isEmpty()) {
            return SecurityStatus.BOGUS;
        }
        NSEC3Record nsec3 = findMatchingNSEC3(qname, zonename, nsec3s, state);
        if (nsec3 != null) {
            if (nsec3.hasType(6) || nsec3.hasType(43)) {
                return SecurityStatus.BOGUS;
            }
            if (!nsec3.hasType(2)) {
                return SecurityStatus.INDETERMINATE;
            }
            return SecurityStatus.SECURE;
        }
        CEResponse ce = proveClosestEncloser(qname, zonename, nsec3s, state);
        if (ce.status == SecurityStatus.SECURE) {
            if ((ce.ncNsec3.getFlags() & 1) != 1) {
                return SecurityStatus.BOGUS;
            }
            return SecurityStatus.INSECURE;
        }
        return SecurityStatus.BOGUS;
    }
}
