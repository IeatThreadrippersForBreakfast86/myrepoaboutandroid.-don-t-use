package org.xbill.DNS.dnssec;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.DSRecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.NSECRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
final class ValUtils {
    public static final String ALGORITHM_ENABLED = "dnsjava.dnssec.algorithm";
    public static final String ALGORITHM_RSA_MIN_KEY_SIZE = "dnsjava.dnssec.algorithm_rsa_min_key_size";
    public static final String DIGEST_ENABLED = "dnsjava.dnssec.digest";
    public static final String DIGEST_HARDEN_DOWNGRADE = "dnsjava.dnssec.harden_algo_downgrade";
    public static final String DIGEST_PREFERENCE = "dnsjava.dnssec.digest_preference";
    public static final String MAX_DS_MATCH_FAILURES_PROPERTY = "dnsjava.dnssec.max_ds_match_failures";
    private boolean hasEd25519;
    private boolean hasEd448;
    private boolean hasGost;
    private final DnsSecVerifier verifier;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ValUtils.class);
    private static final Name WILDCARD = Name.fromConstantString(Marker.ANY_MARKER);
    private int[] digestPreference = null;
    private Properties config = null;
    private boolean digestHardenDowngrade = true;
    private int minRsaKeySize = 1024;
    private int maxDsMatchFailures = 4;

    public static class NsecProvesNodataResponse {
        boolean result;

        /* renamed from: wc */
        Name f284wc;
    }

    public ValUtils() {
        this.hasGost = Security.getProviders("MessageDigest.GOST3411") != null;
        this.hasEd25519 = Security.getProviders("KeyFactory.Ed25519") != null;
        this.hasEd448 = Security.getProviders("KeyFactory.Ed448") != null;
        this.verifier = new DnsSecVerifier(this);
    }

    public static void setCanonicalNsecOwner(SRRset set, RRSIGRecord sig) {
        if (set.getType() != 47) {
            return;
        }
        Record nsec = set.first();
        int fqdnLabelCount = nsec.getName().labels() - 1;
        if (nsec.getName().isWild()) {
            fqdnLabelCount--;
        }
        if (sig.getLabels() == fqdnLabelCount) {
            set.setName(nsec.getName());
        } else {
            if (sig.getLabels() < fqdnLabelCount) {
                set.setName(nsec.getName().wild(sig.getSigner().labels() - sig.getLabels()));
                return;
            }
            throw new IllegalArgumentException("invalid nsec record");
        }
    }

    public void init(Properties config) {
        this.hasGost = Security.getProviders("MessageDigest.GOST3411") != null;
        this.hasEd25519 = Security.getProviders("KeyFactory.Ed25519") != null;
        this.hasEd448 = Security.getProviders("KeyFactory.Ed448") != null;
        this.config = config;
        String dp = config.getProperty(DIGEST_PREFERENCE);
        if (dp != null) {
            String[] dpdata = dp.split(",");
            this.digestPreference = new int[dpdata.length];
            for (int i = 0; i < dpdata.length; i++) {
                this.digestPreference[i] = Integer.parseInt(dpdata[i]);
                if (!isDigestSupported(this.digestPreference[i])) {
                    throw new IllegalArgumentException("Unsupported or disabled digest ID in digest preferences");
                }
            }
        }
        this.digestHardenDowngrade = Boolean.parseBoolean(config.getProperty(DIGEST_HARDEN_DOWNGRADE, Boolean.TRUE.toString()));
        this.minRsaKeySize = Integer.parseInt(config.getProperty(ALGORITHM_RSA_MIN_KEY_SIZE, Integer.toString(this.minRsaKeySize)));
        this.maxDsMatchFailures = Integer.parseInt(config.getProperty(MAX_DS_MATCH_FAILURES_PROPERTY, Integer.toString(this.maxDsMatchFailures)));
        this.verifier.init(config);
    }

    public static ResponseClassification classifyResponse(Message request, SMessage m) {
        if (m.getRcode() == 3 && m.getCount(1) == 0) {
            return ResponseClassification.NAMEERROR;
        }
        if (!request.getHeader().getFlag(7) && m.getCount(1) == 0 && m.getRcode() != 0) {
            boolean sawNs = false;
            for (RRset set : m.getSectionRRsets(2)) {
                if (set.getType() == 6) {
                    return ResponseClassification.NODATA;
                }
                if (set.getType() == 43) {
                    return ResponseClassification.REFERRAL;
                }
                if (set.getType() == 2) {
                    sawNs = true;
                }
            }
            return sawNs ? ResponseClassification.REFERRAL : ResponseClassification.NODATA;
        }
        if (m.getSectionRRsets(2).isEmpty() && m.getSectionRRsets(1).size() == 1 && m.getRcode() == 0 && m.getSectionRRsets(1).get(0).getType() == 2 && !m.getSectionRRsets(1).get(0).getName().equals(request.getQuestion().getName())) {
            return ResponseClassification.REFERRAL;
        }
        if (m.getRcode() != 0 && m.getRcode() != 3) {
            return ResponseClassification.UNKNOWN;
        }
        if (m.getRcode() == 0 && m.getCount(1) == 0) {
            return ResponseClassification.NODATA;
        }
        int qtype = m.getQuestion().getType();
        if (qtype == 255) {
            return ResponseClassification.ANY;
        }
        boolean hadCname = false;
        for (RRset set2 : m.getSectionRRsets(1)) {
            if (set2.getType() == qtype) {
                return ResponseClassification.POSITIVE;
            }
            if (set2.getType() == 5 || set2.getType() == 39) {
                hadCname = true;
                if (qtype == 43) {
                    return ResponseClassification.CNAME;
                }
            }
        }
        if (hadCname) {
            if (m.getRcode() == 3) {
                return ResponseClassification.CNAME_NAMEERROR;
            }
            return ResponseClassification.CNAME_NODATA;
        }
        log.warn("Failed to classify response message:\n{}", m);
        return ResponseClassification.UNKNOWN;
    }

    public KeyEntry verifyNewDNSKEYs(SRRset dnskeyRrset, SRRset dsRrset, long badKeyTTL, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        int favoriteDigestID;
        int alg;
        Iterator<Record> it;
        JustifiedSecStatus lastVerificationResult;
        int favoriteDigestID2;
        ValUtils valUtils = this;
        if (!dnskeyRrset.getName().equals(dsRrset.getName())) {
            KeyEntry ke = KeyEntry.newBadKeyEntry(dsRrset.getName(), dsRrset.getDClass(), badKeyTTL);
            ke.setBadReason(6, C1336R.get("dnskey.no_name_match", new Object[0]));
            return ke;
        }
        AlgorithmRequirements needs = null;
        List<Integer> sigalg = null;
        if (valUtils.digestHardenDowngrade) {
            favoriteDigestID = valUtils.favoriteDSDigestID(dsRrset);
            needs = new AlgorithmRequirements(valUtils);
            sigalg = needs.initDs(dsRrset, favoriteDigestID);
            log.trace("Favorite DigestID for rrset {}/DNSKEY is {} ({})", dnskeyRrset.getName(), Integer.valueOf(favoriteDigestID), DNSSEC.Digest.string(favoriteDigestID));
        } else {
            favoriteDigestID = -1;
        }
        boolean hasAlgoRefusal = false;
        boolean hasCheckedDs = false;
        boolean hasUsefulDs = false;
        JustifiedSecStatus lastVerificationResult2 = null;
        AtomicInteger numDsChecked = new AtomicInteger(0);
        Iterator<Record> it2 = dsRrset.rrs(false).iterator();
        while (it2.hasNext()) {
            Record dsr = it2.next();
            DSRecord ds = (DSRecord) dsr;
            if (!valUtils.isDigestSupported(ds.getDigestID())) {
                it = it2;
                lastVerificationResult = lastVerificationResult2;
                log.debug("Digest ID {} ({}) is not supported", Integer.valueOf(ds.getDigestID()), DNSSEC.Digest.string(ds.getDigestID()));
            } else {
                it = it2;
                lastVerificationResult = lastVerificationResult2;
                if (!valUtils.isAlgorithmSupported(ds.getAlgorithm())) {
                    log.debug("Algorithm {} ({}) is not supported", Integer.valueOf(ds.getAlgorithm()), DNSSEC.Algorithm.string(ds.getAlgorithm()));
                } else if (needs != null && ds.getDigestID() != favoriteDigestID) {
                    log.debug("Downgrade protection prevents using digest ID {} ({})", Integer.valueOf(ds.getDigestID()), DNSSEC.Digest.string(ds.getDigestID()));
                } else {
                    lastVerificationResult2 = valUtils.verifyDnskeysWithDs(dnskeyRrset, ds, date, numDsChecked);
                    if (lastVerificationResult2.status == SecurityStatus.INSECURE) {
                        log.debug("Algorithm {} ({}) refused", Integer.valueOf(ds.getAlgorithm()), DNSSEC.Algorithm.string(ds.getAlgorithm()));
                        hasAlgoRefusal = true;
                        valUtils = this;
                        it2 = it;
                    } else {
                        if (numDsChecked.get() > 0) {
                            log.debug("Checked #{} DS", Integer.valueOf(numDsChecked.get()));
                            hasCheckedDs = true;
                        }
                        hasUsefulDs = true;
                        if (lastVerificationResult2.status == SecurityStatus.SECURE) {
                            if (needs == null || needs.setSecure(ds.getAlgorithm())) {
                                if (!isKeySizeSupported(dnskeyRrset)) {
                                    log.debug("DS {} (footprint={}, id={}, alg={}) works, but DNSKEY set contains keys that are unsupported, treat as insecure", ds.getName(), Integer.valueOf(ds.getFootprint()), Integer.valueOf(ds.getDigestID()), Integer.valueOf(ds.getAlgorithm()));
                                    return KeyEntry.newNullKeyEntry(dsRrset.getName(), dsRrset.getDClass(), badKeyTTL);
                                }
                                dnskeyRrset.setSecurityStatus(SecurityStatus.SECURE);
                                return KeyEntry.newKeyEntry(dnskeyRrset, sigalg);
                            }
                            favoriteDigestID2 = favoriteDigestID;
                        } else {
                            favoriteDigestID2 = favoriteDigestID;
                            if (needs != null && lastVerificationResult2.status == SecurityStatus.BOGUS) {
                                needs.setBogus(ds.getAlgorithm());
                            }
                        }
                        valUtils = this;
                        it2 = it;
                        favoriteDigestID = favoriteDigestID2;
                    }
                }
            }
            lastVerificationResult2 = lastVerificationResult;
            it2 = it;
        }
        JustifiedSecStatus lastVerificationResult3 = lastVerificationResult2;
        if (hasAlgoRefusal && !hasCheckedDs) {
            log.debug("No supported DS records were found -- treating as insecure");
            KeyEntry ke2 = KeyEntry.newNullKeyEntry(dsRrset.getName(), dsRrset.getDClass(), badKeyTTL);
            ke2.setBadReason(2, C1336R.get("failed.ds.nodigest", dsRrset.getName()));
            return ke2;
        }
        if (!hasUsefulDs) {
            log.debug("No usable DS records were found -- treating as insecure");
            KeyEntry ke3 = KeyEntry.newNullKeyEntry(dsRrset.getName(), dsRrset.getDClass(), badKeyTTL);
            ke3.setBadReason(2, C1336R.get("failed.ds.no_usable_digest", dsRrset.getName()));
            return ke3;
        }
        log.debug("Failed to match any usable DS to a DNSKEY");
        if (needs != null && (alg = needs.missing()) != 0) {
            log.debug("Missing verification of DNSKEY signature with algorithm {} ({})", Integer.valueOf(alg), DNSSEC.Algorithm.string(alg));
        }
        KeyEntry ke4 = KeyEntry.newBadKeyEntry(dsRrset.getName(), dsRrset.getDClass(), badKeyTTL);
        ke4.setBadReason(lastVerificationResult3.edeReason, lastVerificationResult3.reason);
        return ke4;
    }

    private JustifiedSecStatus verifyDnskeysWithDs(SRRset dnskeyRrset, DSRecord ds, Instant date, AtomicInteger numDsChecked) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        int numDsOk = 0;
        int numDsSizeUnsupported = 0;
        for (Record dsnkeyr : dnskeyRrset.rrs(false)) {
            DNSKEYRecord dnskey = (DNSKEYRecord) dsnkeyr;
            log.trace("Validating DNSKEY {} (footprint={}, alg={}) against DS {} (footprint={}, digest={}, alg={})", dnskey.getName(), Integer.valueOf(dnskey.getFootprint()), Integer.valueOf(dnskey.getAlgorithm()), ds.getName(), Integer.valueOf(ds.getFootprint()), Integer.valueOf(ds.getDigestID()), Integer.valueOf(ds.getAlgorithm()));
            if (ds.getFootprint() != dnskey.getFootprint() || ds.getAlgorithm() != dnskey.getAlgorithm()) {
                log.trace("Footprint or algorithm mismatch, ignoring");
            } else {
                numDsChecked.getAndIncrement();
                if (!dsDigestMatchesDnskey(ds, dnskey)) {
                    log.debug("DS did not match DNSKEY, ignoring");
                    if (numDsChecked.get() > this.maxDsMatchFailures + numDsOk) {
                        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.ds_max_match", new Object[0]));
                    }
                } else {
                    numDsOk++;
                    if (!isKeySizeSupported(dnskey)) {
                        log.debug("DS okay but that DNSKEY size is not supported");
                        numDsSizeUnsupported++;
                    } else {
                        JustifiedSecStatus sec = this.verifier.verify(dnskeyRrset, dnskey, date);
                        if (sec.status == SecurityStatus.SECURE) {
                            return sec;
                        }
                    }
                }
            }
        }
        if (numDsSizeUnsupported > 0) {
            return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, null);
        }
        if (numDsChecked.get() == 0) {
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 9, C1336R.get("dnskey.no_ds_alg_match", dnskeyRrset.getName(), DNSSEC.Algorithm.string(ds.getAlgorithm())));
        }
        if (numDsOk == 0) {
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.no_ds_match", new Object[0]));
        }
        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.ds_match_mismatch", new Object[0]));
    }

    private boolean dsDigestMatchesDnskey(DSRecord ds, DNSKEYRecord dnskey) {
        byte[] dsHash = ds.getDigest();
        try {
            DSRecord keyDigest = new DSRecord(Name.root, ds.getDClass(), 0L, ds.getDigestID(), dnskey);
            byte[] keyHash = keyDigest.getDigest();
            if (!Arrays.equals(keyHash, dsHash)) {
                log.debug("Hash mismatch: key {} != ds {}", keyHash, dsHash);
                return false;
            }
            return true;
        } catch (IllegalArgumentException iae) {
            log.debug("Digest generation failed", (Throwable) iae);
            return false;
        }
    }

    int favoriteDSDigestID(SRRset dsset) {
        if (this.digestPreference == null) {
            int max = 0;
            for (Record r : dsset.rrs(false)) {
                DSRecord ds = (DSRecord) r;
                if (ds.getDigestID() > max && isDigestSupported(ds.getDigestID()) && isAlgorithmSupported(ds.getAlgorithm())) {
                    max = ds.getDigestID();
                }
            }
            return max;
        }
        for (int preference : this.digestPreference) {
            for (Record r2 : dsset.rrs(false)) {
                DSRecord ds2 = (DSRecord) r2;
                if (ds2.getDigestID() == preference) {
                    return ds2.getDigestID();
                }
            }
        }
        return 0;
    }

    public JustifiedSecStatus verifySRRset(SRRset rrset, KeyEntry keyRrset, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        if (rrset.getSecurityStatus() == SecurityStatus.SECURE) {
            log.trace("RRset <{}/{}/{}> previously found to be SECURE", rrset.getName(), Type.string(rrset.getType()), DClass.string(rrset.getDClass()));
            return new JustifiedSecStatus(SecurityStatus.SECURE, -1, null);
        }
        JustifiedSecStatus res = this.verifier.verify(rrset, keyRrset, date);
        rrset.setSecurityStatus(res.status);
        return res;
    }

    public static Name rrsetWildcard(RRset rrset) {
        List<RRSIGRecord> sigs = rrset.sigs();
        RRSIGRecord firstSig = sigs.get(0);
        for (int i = 1; i < sigs.size(); i++) {
            if (sigs.get(i).getLabels() != firstSig.getLabels()) {
                throw new IllegalArgumentException("failed.wildcard.label_count_mismatch");
            }
        }
        Name wn = rrset.getName();
        if (rrset.getName().isWild()) {
            wn = new Name(wn, 1);
        }
        int labelDiff = (wn.labels() - 1) - firstSig.getLabels();
        if (labelDiff > 0) {
            return wn.wild(labelDiff);
        }
        return null;
    }

    public static Name longestCommonName(Name domain1, Name domain2) {
        int l = Math.min(domain1.labels(), domain2.labels());
        Name domain12 = new Name(domain1, domain1.labels() - l);
        Name domain22 = new Name(domain2, domain2.labels() - l);
        for (int i = 0; i < l - 1; i++) {
            Name ns1 = new Name(domain12, i);
            if (ns1.equals(new Name(domain22, i))) {
                return ns1;
            }
        }
        return Name.root;
    }

    public static boolean strictSubdomain(Name domain1, Name domain2) {
        if (domain1.labels() <= domain2.labels()) {
            return false;
        }
        return new Name(domain1, domain1.labels() - domain2.labels()).equals(domain2);
    }

    public static Name closestEncloser(Name domain, Name owner, Name next) {
        Name n1 = longestCommonName(domain, owner);
        Name n2 = longestCommonName(domain, next);
        return n1.labels() > n2.labels() ? n1 : n2;
    }

    public static Name nsecWildcard(Name domain, SRRset set, NSECRecord nsec) throws NameTooLongException {
        Name origin = closestEncloser(domain, set.getName(), nsec.getNext());
        return Name.concatenate(WILDCARD, origin);
    }

    public static boolean nsecProvesNameError(SRRset set, NSECRecord nsec, Name qname) {
        Name owner = set.getName();
        Name next = nsec.getNext();
        if (qname.equals(owner) || !next.subdomain(set.getSignerName())) {
            return false;
        }
        if (qname.subdomain(owner)) {
            if (nsec.hasType(39)) {
                return false;
            }
            if (nsec.hasType(2) && !nsec.hasType(6)) {
                return false;
            }
        }
        if (owner.equals(next)) {
            return strictSubdomain(qname, next);
        }
        return owner.compareTo(next) > 0 ? owner.compareTo(qname) < 0 && strictSubdomain(qname, next) : owner.compareTo(qname) < 0 && qname.compareTo(next) < 0;
    }

    public static boolean nsecProvesNoWC(SRRset set, NSECRecord nsec, Name qname) {
        Name ce = closestEncloser(qname, set.getName(), nsec.getNext());
        int labelsToStrip = qname.labels() - ce.labels();
        if (labelsToStrip > 0) {
            Name wcName = qname.wild(labelsToStrip);
            return nsecProvesNameError(set, nsec, wcName);
        }
        return false;
    }

    public static NsecProvesNodataResponse nsecProvesNodata(SRRset set, NSECRecord nsec, Name qname, int qtype) {
        NsecProvesNodataResponse result = new NsecProvesNodataResponse();
        if (!set.getName().equals(qname)) {
            if (strictSubdomain(nsec.getNext(), qname) && set.getName().compareTo(qname) < 0) {
                result.result = true;
                return result;
            }
            if (set.getName().isWild()) {
                Name ce = new Name(set.getName(), 1);
                if (strictSubdomain(qname, ce)) {
                    if (nsec.hasType(5)) {
                        log.debug("NSEC proofed wildcard CNAME");
                        result.result = false;
                        return result;
                    }
                    if (nsec.hasType(2) && !nsec.hasType(6)) {
                        log.debug("Wrong parent (wildcard) NSEC used");
                        result.result = false;
                        return result;
                    }
                    if (nsec.hasType(qtype)) {
                        log.debug("NSEC proofed that {} exists", Type.string(qtype));
                        result.result = false;
                        return result;
                    }
                }
                result.f284wc = ce;
                result.result = true;
                return result;
            }
            result.result = false;
            return result;
        }
        if (nsec.hasType(qtype)) {
            log.debug("NSEC proofed that {} exists", Type.string(qtype));
            result.result = false;
            return result;
        }
        if (nsec.hasType(5)) {
            log.debug("NSEC proofed CNAME");
            result.result = false;
            return result;
        }
        if (qtype != 43 && nsec.hasType(2) && !nsec.hasType(6)) {
            log.debug("NSEC proofed missing referral");
            result.result = false;
            return result;
        }
        if (qtype == 43 && nsec.hasType(6) && !Name.root.equals(qname)) {
            log.debug("NSEC from wrong zone");
            result.result = false;
            return result;
        }
        result.result = true;
        return result;
    }

    public JustifiedSecStatus nsecProvesNodataDsReply(Message request, SMessage response, KeyEntry keyRrset, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        ValUtils valUtils = this;
        Name qname = request.getQuestion().getName();
        int qclass = request.getQuestion().getDClass();
        SRRset nsecRrset = response.findRRset(qname, 47, qclass, 2);
        if (nsecRrset != null) {
            JustifiedSecStatus res = valUtils.verifySRRset(nsecRrset, keyRrset, date);
            if (res.status != SecurityStatus.SECURE) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("failed.ds.nsec", res.reason));
            }
            SecurityStatus status = nsecProvesNoDS((NSECRecord) nsecRrset.first(), qname);
            switch (status) {
                case INSECURE:
                    return new JustifiedSecStatus(status, -1, C1336R.get("failed.ds.nodelegation", new Object[0]));
                case SECURE:
                    return new JustifiedSecStatus(status, -1, C1336R.get("insecure.ds.nsec", new Object[0]));
                default:
                    return new JustifiedSecStatus(status, 6, C1336R.get("failed.ds.nsec.hasdata", new Object[0]));
            }
        }
        NsecProvesNodataResponse ndp = new NsecProvesNodataResponse();
        Name ce = null;
        boolean hasValidNSEC = false;
        NSECRecord wcNsec = null;
        for (SRRset set : response.getSectionRRsets(2, 47)) {
            JustifiedSecStatus res2 = valUtils.verifySRRset(set, keyRrset, date);
            if (res2.status != SecurityStatus.SECURE) {
                return new JustifiedSecStatus(res2.status, res2.edeReason, C1336R.get("failed.ds.nsec.ent", new Object[0]));
            }
            NSECRecord nsec = (NSECRecord) set.rrs(false).get(0);
            ndp = nsecProvesNodata(set, nsec, qname, 43);
            if (ndp.result) {
                if (ndp.f284wc != null && nsec.getName().isWild()) {
                    hasValidNSEC = true;
                    wcNsec = nsec;
                } else {
                    hasValidNSEC = true;
                }
            }
            boolean hasValidNSEC2 = nsecProvesNameError(set, nsec, qname);
            if (hasValidNSEC2) {
                ce = closestEncloser(qname, set.getName(), nsec.getNext());
            }
            valUtils = this;
        }
        if (ndp.f284wc != null && (ce == null || !ce.equals(ndp.f284wc))) {
            hasValidNSEC = false;
        }
        if (hasValidNSEC) {
            if (ndp.f284wc != null) {
                return new JustifiedSecStatus(nsecProvesNoDS(wcNsec, qname), 12, C1336R.get("failed.ds.nowildcardproof", new Object[0]));
            }
            return new JustifiedSecStatus(SecurityStatus.INSECURE, -1, C1336R.get("insecure.ds.nsec.ent", new Object[0]));
        }
        return new JustifiedSecStatus(SecurityStatus.UNCHECKED, 5, C1336R.get("failed.ds.nonconclusive", new Object[0]));
    }

    public boolean hasSignedNsecs(SMessage message) {
        for (SRRset set : message.getSectionRRsets(2)) {
            if (set.getType() == 47 || set.getType() == 50) {
                if (!set.sigs().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static SecurityStatus nsecProvesNoDS(NSECRecord nsec, Name qname) {
        if ((nsec.hasType(6) && !Name.root.equals(qname)) || nsec.hasType(43)) {
            return SecurityStatus.BOGUS;
        }
        if (!nsec.hasType(2)) {
            return SecurityStatus.INSECURE;
        }
        return SecurityStatus.SECURE;
    }

    boolean atLeastOneSupportedAlgorithm(RRset dsRRset) {
        for (Record r : dsRRset.rrs(false)) {
            if (isAlgorithmSupported(((DSRecord) r).getAlgorithm())) {
                return true;
            }
        }
        return false;
    }

    boolean isAlgorithmSupported(int alg) {
        String configKey = "dnsjava.dnssec.algorithm." + alg;
        switch (alg) {
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
                if (this.config == null) {
                    return false;
                }
                return Boolean.parseBoolean(this.config.getProperty(configKey, Boolean.FALSE.toString()));
            case 5:
            case 7:
            case 8:
            case 10:
            case 13:
            case 14:
                return propertyOrTrueWithPrecondition(configKey, true);
            case 12:
                return propertyOrTrueWithPrecondition(configKey, this.hasGost);
            case 15:
                return propertyOrTrueWithPrecondition(configKey, this.hasEd25519);
            case 16:
                return propertyOrTrueWithPrecondition(configKey, this.hasEd448);
        }
    }

    private boolean isKeySizeSupported(RRset dnskeyRrset) {
        for (Record r : dnskeyRrset.rrs(false)) {
            if (!isKeySizeSupported((DNSKEYRecord) r)) {
                return false;
            }
        }
        return true;
    }

    private boolean isKeySizeSupported(DNSKEYRecord dnskey) {
        try {
            PublicKey publicKey = dnskey.getPublicKey();
            boolean valid = true;
            switch (dnskey.getAlgorithm()) {
                case 1:
                case 5:
                case 7:
                case 8:
                case 10:
                    int bitLength = ((RSAPublicKey) publicKey).getModulus().bitLength();
                    if (bitLength < this.minRsaKeySize) {
                        valid = false;
                    }
                    if (!valid) {
                        log.debug("Key size {} for DNSKEY <{}/{}>, alg={}, id={} is less than minimum of {}", Integer.valueOf(bitLength), dnskey.getName(), DClass.string(dnskey.getDClass()), DNSSEC.Algorithm.string(dnskey.getAlgorithm()), Integer.valueOf(dnskey.getFootprint()), Integer.valueOf(this.minRsaKeySize));
                    }
                    return valid;
                default:
                    return true;
            }
        } catch (DNSSEC.DNSSECException e) {
            return false;
        }
        return false;
    }

    boolean atLeastOneSupportedDigest(RRset dsRRset) {
        for (Record r : dsRRset.rrs(false)) {
            if (isDigestSupported(((DSRecord) r).getDigestID())) {
                return true;
            }
        }
        return false;
    }

    boolean isDigestSupported(int digestID) {
        String configKey = "dnsjava.dnssec.digest." + digestID;
        switch (digestID) {
            case 1:
            case 2:
            case 4:
                if (this.config == null) {
                    return true;
                }
                return Boolean.parseBoolean(this.config.getProperty(configKey, Boolean.TRUE.toString()));
            case 3:
                return propertyOrTrueWithPrecondition(configKey, this.hasGost);
            default:
                return false;
        }
    }

    private boolean propertyOrTrueWithPrecondition(String configKey, boolean precondition) {
        if (!precondition) {
            return false;
        }
        if (this.config == null) {
            return true;
        }
        return Boolean.parseBoolean(this.config.getProperty(configKey, Boolean.TRUE.toString()));
    }
}
