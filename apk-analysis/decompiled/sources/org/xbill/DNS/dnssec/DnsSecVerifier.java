package org.xbill.DNS.dnssec;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
final class DnsSecVerifier {
    public static final String MAX_VALIDATE_RRSIGS_PROPERTY = "dnsjava.dnssec.max_validate_rrsigs";
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DnsSecVerifier.class);
    private int maxValidateRRsigs;
    private final ValUtils valUtils;

    public DnsSecVerifier(ValUtils valUtils) {
        this.valUtils = valUtils;
    }

    public void init(Properties config) {
        this.maxValidateRRsigs = Integer.parseInt(config.getProperty(MAX_VALIDATE_RRSIGS_PROPERTY, "8"));
    }

    private List<DNSKEYRecord> findKey(RRset dnskeyRrset, RRSIGRecord signature) {
        if (!signature.getSigner().equals(dnskeyRrset.getName())) {
            log.trace("Could not find appropriate key because incorrect keyset was supplied. Wanted: {}, got: {}", signature.getSigner(), dnskeyRrset.getName());
            return Collections.emptyList();
        }
        int keyid = signature.getFootprint();
        int alg = signature.getAlgorithm();
        List<DNSKEYRecord> res = new ArrayList<>(dnskeyRrset.size());
        for (Record r : dnskeyRrset.rrs(false)) {
            DNSKEYRecord dnskey = (DNSKEYRecord) r;
            if (dnskey.getAlgorithm() == alg && dnskey.getFootprint() == keyid) {
                res.add(dnskey);
            }
        }
        return res;
    }

    private JustifiedSecStatus verifySignature(SRRset rrset, RRSIGRecord sigrec, KeyEntry keyRrset, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        if (!rrset.getName().subdomain(sigrec.getSigner())) {
            log.debug("Signer name {} is off-tree for {}", sigrec.getSigner(), rrset.getName());
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.key_offtree", sigrec.getSigner(), rrset.getName()));
        }
        List<DNSKEYRecord> keys = findKey(keyRrset, sigrec);
        Iterator<DNSKEYRecord> it = keys.iterator();
        if (it.hasNext()) {
            DNSKEYRecord dnskey = it.next();
            try {
                DNSSEC.verify(rrset, sigrec, dnskey, date);
                ValUtils.setCanonicalNsecOwner(rrset, sigrec);
                return new JustifiedSecStatus(SecurityStatus.SECURE, -1, null);
            } catch (DNSSEC.InvalidDnskeyException e) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, e.getEdeCode(), C1336R.get("dnskey.invalid", new Object[0]));
            } catch (DNSSEC.KeyMismatchException e2) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.no_match", new Object[0]));
            } catch (DNSSEC.SignatureExpiredException e3) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 7, C1336R.get("dnskey.expired", new Object[0]));
            } catch (DNSSEC.SignatureNotYetValidException e4) {
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 8, C1336R.get("dnskey.not_yet_valid", new Object[0]));
            } catch (DNSSEC.DNSSECException e5) {
                log.error("Failed to validate RRset <{}/{}/{}>", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()), e5);
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("dnskey.invalid", new Object[0]));
            }
        }
        log.trace("Could not find appropriate key for {}", sigrec);
        return new JustifiedSecStatus(SecurityStatus.UNCHECKED, 9, C1336R.get("dnskey.no_key", sigrec.getSigner()));
    }

    public JustifiedSecStatus verify(SRRset rrset, KeyEntry keyRrset, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        List<RRSIGRecord> sigs = rrset.sigs();
        if (sigs.isEmpty()) {
            log.info("RRset <{}/{}/{}> failed to verify due to a lack of signatures", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 10, C1336R.get("validate.bogus.missingsig_named", rrset.getName(), Type.string(rrset.getType())));
        }
        AlgorithmRequirements needs = null;
        if (keyRrset.getAlgo() != null) {
            needs = new AlgorithmRequirements(this.valUtils);
            needs.initList(keyRrset.getAlgo());
            if (needs.getNum() == 0) {
                log.debug("{} has no known algorithms", rrset.getName());
                return new JustifiedSecStatus(SecurityStatus.INSECURE, 1, C1336R.get("validate.insecure.noalg", rrset.getName()));
            }
        }
        JustifiedSecStatus res = null;
        int numVerified = 0;
        for (RRSIGRecord sigrec : sigs) {
            res = verifySignature(rrset, sigrec, keyRrset, date);
            if (res.status == SecurityStatus.SECURE) {
                if (needs == null || needs.setSecure(sigrec.getAlgorithm())) {
                    return res;
                }
            } else if (needs != null && res.status == SecurityStatus.BOGUS) {
                needs.setBogus(sigrec.getAlgorithm());
            }
            numVerified++;
            if (numVerified > this.maxValidateRRsigs) {
                log.warn("RRset <{}/{}/{}> failed to verify: too many signatures", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
                return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("validate.bogus.rrsigtoomany", rrset.getName(), Type.string(rrset.getType())));
            }
        }
        log.warn("RRset <{}/{}/{}> failed to verify: all signatures are BOGUS", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
        return res;
    }

    public JustifiedSecStatus verify(RRset rrset, DNSKEYRecord dnskey, Instant date) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        List<RRSIGRecord> sigs = rrset.sigs();
        if (sigs.isEmpty()) {
            log.warn("RRset <{}/{}/{}> failed to verify due to lack of signatures", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
            return new JustifiedSecStatus(SecurityStatus.BOGUS, 10, C1336R.get("validate.bogus.missingsig_named", rrset.getName(), Type.string(rrset.getType())));
        }
        DNSSEC.DNSSECException lastException = null;
        int numVerified = 0;
        for (RRSIGRecord sigrec : sigs) {
            if (sigrec.getFootprint() == dnskey.getFootprint()) {
                numVerified++;
                try {
                    DNSSEC.verify(rrset, sigrec, dnskey, date);
                    return new JustifiedSecStatus(SecurityStatus.SECURE, -1, null);
                } catch (DNSSEC.DNSSECException e) {
                    log.warn("Failed to validate RRset <{}/{}/{}> with signature {}", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()), Integer.valueOf(sigrec.getFootprint()), e);
                    lastException = e;
                    if (numVerified > this.maxValidateRRsigs) {
                        log.warn("RRset <{}/{}/{}> failed to verify: too many signatures", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
                        return new JustifiedSecStatus(SecurityStatus.BOGUS, 6, C1336R.get("validate.bogus.rrsigtoomany", rrset.getName(), Type.string(rrset.getType())));
                    }
                }
            }
        }
        log.warn("RRset <{}/{}/{}> failed to verify: all signatures were BOGUS", rrset.getName(), DClass.string(rrset.getDClass()), Type.string(rrset.getType()));
        int edeReason = 6;
        String reason = "dnskey.invalid";
        if (numVerified == 0) {
            edeReason = 9;
            reason = "dnskey.no_ds_match";
        } else if (lastException instanceof DNSSEC.SignatureExpiredException) {
            edeReason = 7;
            reason = "dnskey.expired";
        } else if (lastException instanceof DNSSEC.SignatureNotYetValidException) {
            edeReason = 8;
            reason = "dnskey.not_yet_valid";
        }
        return new JustifiedSecStatus(SecurityStatus.BOGUS, edeReason, C1336R.get(reason, new Object[0]));
    }
}
