package org.xbill.DNS.dnssec;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNAMERecord;
import org.xbill.DNS.EDNSOption;
import org.xbill.DNS.ExtendedErrorCodeOption;
import org.xbill.DNS.Header;
import org.xbill.DNS.Master;
import org.xbill.DNS.Message;
import org.xbill.DNS.NSECRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.dnssec.ValUtils;

/* loaded from: classes8.dex */
public final class ValidatingResolver implements Resolver {
    private static final long DEFAULT_TA_BAD_KEY_TTL = 60;
    public static final String TRUST_ANCHOR_FILE_PROPERTY = "dnsjava.dnssec.trust_anchor_file";
    public static final int VALIDATION_REASON_QCLASS = 65280;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ValidatingResolver.class);
    private final Clock clock;
    private final Resolver headResolver;
    private boolean isAddReasonToAdditional;
    private final KeyCache keyCache;
    private final NSEC3ValUtils n3valUtils;
    private final TrustAnchorStore trustAnchors;
    private final ValUtils valUtils;

    public boolean isAddReasonToAdditional() {
        return this.isAddReasonToAdditional;
    }

    public void setAddReasonToAdditional(boolean isAddReasonToAdditional) {
        this.isAddReasonToAdditional = isAddReasonToAdditional;
    }

    public ValidatingResolver(Resolver headResolver) {
        this(headResolver, Clock.systemUTC());
    }

    public ValidatingResolver(Resolver headResolver, Clock clock) throws NumberFormatException {
        this.isAddReasonToAdditional = true;
        this.headResolver = headResolver;
        this.clock = clock;
        headResolver.setEDNS(0, 0, 32768, new EDNSOption[0]);
        headResolver.setIgnoreTruncation(false);
        this.keyCache = new KeyCache();
        this.valUtils = new ValUtils();
        this.n3valUtils = new NSEC3ValUtils();
        this.trustAnchors = new DefaultTrustAnchorStore();
        try {
            init(System.getProperties());
        } catch (IOException e) {
            log.error("Could not initialize from system properties", (Throwable) e);
        }
    }

    public void init(Properties config) throws NumberFormatException, IOException {
        this.keyCache.init(config);
        this.n3valUtils.init(config);
        this.valUtils.init(config);
        String s = config.getProperty(TRUST_ANCHOR_FILE_PROPERTY);
        if (s != null) {
            log.debug("Reading trust anchor file: {}", s);
            loadTrustAnchors(new FileInputStream(s));
        }
    }

    public void loadTrustAnchors(InputStream data) throws IOException {
        List<Record> records = new ArrayList<>();
        Master master = new Master(data, Name.root, 0L);
        while (true) {
            try {
                Record mr = master.nextRecord();
                if (mr == null) {
                    break;
                } else {
                    records.add(mr);
                }
            } catch (Throwable th) {
                try {
                    master.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        master.close();
        Collections.sort(records);
        SRRset currentRrset = new SRRset();
        for (Record r : records) {
            if (r.getType() == 48 || r.getType() == 43) {
                if (currentRrset.size() == 0) {
                    currentRrset.addRR(r);
                } else if (currentRrset.getName().equals(r.getName()) && currentRrset.getType() == r.getType() && currentRrset.getDClass() == r.getDClass()) {
                    currentRrset.addRR(r);
                } else {
                    this.trustAnchors.store(currentRrset);
                    currentRrset = new SRRset();
                    currentRrset.addRR(r);
                }
            }
        }
        if (currentRrset.size() > 0) {
            this.trustAnchors.store(currentRrset);
        }
    }

    public TrustAnchorStore getTrustAnchors() {
        return this.trustAnchors;
    }

    private void removeSpuriousAuthority(SMessage response) {
        if (response.getSectionRRsets(1).isEmpty() && response.getSectionRRsets(2).size() == 1) {
            return;
        }
        Iterator<SRRset> authRrsetIterator = response.getSectionRRsets(2).iterator();
        while (authRrsetIterator.hasNext()) {
            SRRset rrset = authRrsetIterator.next();
            if (rrset.getType() == 2 && rrset.sigs().isEmpty()) {
                log.trace("Removing spurious unsigned NS record (likely inserted by forwarder) {}/{}/{}", rrset.getName(), Type.string(rrset.getType()), DClass.string(rrset.getDClass()));
                authRrsetIterator.remove();
            }
        }
    }

    private CompletionStage<Void> validatePositiveResponse(final Message request, final SMessage response, final Nsec3ValidationState nsec3State, final Executor executor) {
        final Map<Name, Name> wcs = new HashMap<>(1);
        final List<SRRset> nsec3s = new ArrayList<>(0);
        final List<SRRset> nsecs = new ArrayList<>(0);
        return validateAnswerAndGetWildcards(response, request.getQuestion().getType(), wcs, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda17
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m184xab81e678(request, response, wcs, nsec3s, nsecs, executor, (Boolean) obj);
            }
        }).thenAccept(new Consumer() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.m185x38221179(wcs, nsecs, nsec3s, response, nsec3State, (Boolean) obj);
            }
        });
    }

    /* renamed from: lambda$validatePositiveResponse$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m184xab81e678(Message request, SMessage response, Map wcs, List nsec3s, List nsecs, Executor executor, Boolean success) {
        int[] sections;
        if (Boolean.TRUE.equals(success)) {
            if (request.getQuestion().getType() == 255) {
                sections = new int[]{1, 2};
            } else {
                sections = new int[]{2};
            }
            return validatePositiveResponseRecursive(response, wcs, nsec3s, nsecs, sections, new AtomicInteger(0), new AtomicInteger(0), executor);
        }
        return CompletableFuture.completedFuture(false);
    }

    /* renamed from: lambda$validatePositiveResponse$1$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ void m185x38221179(Map wcs, List nsecs, List nsec3s, SMessage response, Nsec3ValidationState nsec3State, Boolean success) {
        boolean wcNsecOk;
        if (!Boolean.TRUE.equals(success)) {
            return;
        }
        if (!wcs.isEmpty()) {
            for (Map.Entry<Name, Name> wc : wcs.entrySet()) {
                Iterator i$ = nsecs.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        wcNsecOk = false;
                        break;
                    }
                    SRRset set = (SRRset) i$.next();
                    NSECRecord nsec = (NSECRecord) set.first();
                    if (ValUtils.nsecProvesNameError(set, nsec, wc.getKey())) {
                        try {
                            Name nsecWc = ValUtils.nsecWildcard(wc.getKey(), set, nsec);
                            if (wc.getValue().equals(nsecWc)) {
                                wcNsecOk = true;
                                break;
                            }
                        } catch (NameTooLongException e) {
                            throw new IllegalStateException(C1336R.get("failed.positive.wildcardgeneration", new Object[0]));
                        }
                    }
                }
                if (!wcNsecOk && !nsec3s.isEmpty()) {
                    if (this.n3valUtils.allNSEC3sIgnorable(nsec3s, this.keyCache)) {
                        response.setStatus(SecurityStatus.INSECURE, -1, C1336R.get("failed.nsec3_ignored", new Object[0]));
                        return;
                    }
                    SecurityStatus status = this.n3valUtils.proveWildcard(nsec3s, wc.getKey(), ((SRRset) nsec3s.get(0)).getSignerName(), wc.getValue(), nsec3State);
                    if (status == SecurityStatus.INSECURE) {
                        response.setStatus(status, -1);
                        return;
                    } else if (status == SecurityStatus.SECURE) {
                        wcNsecOk = true;
                    }
                }
                if (!wcNsecOk) {
                    response.setBogus(C1336R.get("failed.positive.wildcard_too_broad", new Object[0]));
                    return;
                }
            }
        }
        response.setStatus(SecurityStatus.SECURE, -1);
    }

    private CompletionStage<Boolean> validatePositiveResponseRecursive(final SMessage response, final Map<Name, Name> wcs, final List<SRRset> nsec3s, final List<SRRset> nsecs, final int[] sections, final AtomicInteger sectionIndex, final AtomicInteger setIndex, final Executor executor) {
        if (sectionIndex.get() < sections.length) {
            List<SRRset> sectionRRsets = response.getSectionRRsets(sections[sectionIndex.get()]);
            if (setIndex.get() >= sectionRRsets.size()) {
                sectionIndex.getAndIncrement();
                setIndex.set(0);
                return validatePositiveResponseRecursive(response, wcs, nsec3s, nsecs, sections, sectionIndex, setIndex, executor);
            }
            final SRRset set = sectionRRsets.get(setIndex.getAndIncrement());
            return prepareFindKey(set, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda11
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.m186x99a63190(set, response, wcs, nsecs, nsec3s, sections, sectionIndex, setIndex, executor, (KeyEntry) obj);
                }
            });
        }
        return CompletableFuture.completedFuture(true);
    }

    /* renamed from: lambda$validatePositiveResponseRecursive$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m186x99a63190(SRRset set, SMessage response, Map wcs, List nsecs, List nsec3s, int[] sections, AtomicInteger sectionIndex, AtomicInteger setIndex, Executor executor, KeyEntry ke) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        JustifiedSecStatus kve = ke.validateKeyFor(set);
        if (kve == null) {
            JustifiedSecStatus res = this.valUtils.verifySRRset(set, ke, this.clock.instant());
            if (res.status != SecurityStatus.SECURE) {
                response.setBogus(C1336R.get("failed.authority.positive", set));
                return CompletableFuture.completedFuture(false);
            }
            if (!wcs.isEmpty()) {
                if (set.getType() == 47) {
                    nsecs.add(set);
                } else if (set.getType() == 50) {
                    nsec3s.add(set);
                }
            }
            return validatePositiveResponseRecursive(response, wcs, nsec3s, nsecs, sections, sectionIndex, setIndex, executor);
        }
        kve.applyToResponse(response);
        return CompletableFuture.completedFuture(false);
    }

    private CompletionStage<Boolean> validateAnswerAndGetWildcards(SMessage response, int qtype, Map<Name, Name> wcs, Executor executor) {
        return validateAnswerAndGetWildcardsRecursive(response, qtype, wcs, new AtomicInteger(0), executor);
    }

    private CompletionStage<Boolean> validateAnswerAndGetWildcardsRecursive(final SMessage response, final int qtype, final Map<Name, Name> wcs, final AtomicInteger setIndex, final Executor executor) {
        final List<SRRset> sectionRRsets = response.getSectionRRsets(1);
        if (setIndex.get() >= sectionRRsets.size()) {
            return CompletableFuture.completedFuture(true);
        }
        final SRRset set = sectionRRsets.get(setIndex.get());
        return prepareFindKey(set, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m179xd106bd74(set, response, wcs, qtype, setIndex, sectionRRsets, executor, (KeyEntry) obj);
            }
        });
    }

    /* renamed from: lambda$validateAnswerAndGetWildcardsRecursive$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m179xd106bd74(SRRset set, SMessage response, Map wcs, int qtype, AtomicInteger setIndex, List sectionRRsets, Executor executor, KeyEntry ke) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        JustifiedSecStatus kve = ke.validateKeyFor(set);
        if (kve == null) {
            JustifiedSecStatus res = this.valUtils.verifySRRset(set, ke, this.clock.instant());
            if (res.status != SecurityStatus.SECURE) {
                response.setBogus(C1336R.get("failed.answer.positive", set));
                return CompletableFuture.completedFuture(false);
            }
            try {
                Name wc = ValUtils.rrsetWildcard(set);
                if (wc != null) {
                    if (set.getType() == 39) {
                        response.setBogus(C1336R.get("failed.dname.wildcard", set.getName()));
                        return CompletableFuture.completedFuture(false);
                    }
                    wcs.put(set.getName(), wc);
                }
                if (qtype != 39 && set.getType() == 39) {
                    DNAMERecord dname = (DNAMERecord) set.first();
                    if (setIndex.getAndIncrement() < sectionRRsets.size()) {
                        SRRset cnameSet = (SRRset) sectionRRsets.get(setIndex.get());
                        if (cnameSet.getType() == 5 && dname != null) {
                            if (cnameSet.size() > 1) {
                                response.setBogus(C1336R.get("failed.synthesize.multiple", new Object[0]));
                                return CompletableFuture.completedFuture(false);
                            }
                            CNAMERecord cname = (CNAMERecord) cnameSet.first();
                            try {
                                Name expected = Name.concatenate(cname.getName().relativize(dname.getName()), dname.getTarget());
                                if (!expected.equals(cname.getTarget())) {
                                    try {
                                        response.setBogus(C1336R.get("failed.synthesize.nomatch", cname.getTarget(), expected));
                                        return CompletableFuture.completedFuture(false);
                                    } catch (NameTooLongException e) {
                                        response.setBogus(C1336R.get("failed.synthesize.toolong", new Object[0]));
                                        return CompletableFuture.completedFuture(false);
                                    }
                                }
                                cnameSet.setSecurityStatus(SecurityStatus.SECURE);
                            } catch (NameTooLongException e2) {
                            }
                        }
                    }
                }
                setIndex.getAndIncrement();
                return validateAnswerAndGetWildcardsRecursive(response, qtype, wcs, setIndex, executor);
            } catch (RuntimeException ex) {
                response.setBogus(C1336R.get(ex.getMessage(), set.getName()));
                return CompletableFuture.completedFuture(false);
            }
        }
        kve.applyToResponse(response);
        return CompletableFuture.completedFuture(false);
    }

    private CompletionStage<Void> validateNodataResponse(Message request, final SMessage response, final Nsec3ValidationState nsec3State, Executor executor) {
        Name intermediateQname = request.getQuestion().getName();
        final int qtype = request.getQuestion().getType();
        for (SRRset set : response.getSectionRRsets(1)) {
            if (set.getSecurityStatus() != SecurityStatus.SECURE) {
                response.setBogus(C1336R.get("failed.answer.cname_nodata", set.getName()));
                return CompletableFuture.completedFuture(null);
            }
            if (set.getType() == 5) {
                intermediateQname = ((CNAMERecord) set.first()).getTarget();
            }
        }
        final Name qname = intermediateQname;
        return validateNodataResponseRecursive(response, new AtomicInteger(0), nsec3State, executor).handleAsync(new BiFunction() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda13
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return this.f$0.m182xd96571aa(response, qname, qtype, nsec3State, (Void) obj, (Throwable) obj2);
            }
        });
    }

    /* renamed from: lambda$validateNodataResponse$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ Void m182xd96571aa(SMessage response, Name qname, int qtype, Nsec3ValidationState nsec3State, Void result, Throwable ex) {
        boolean hasValidNSEC;
        int edeReason;
        int i;
        int edeReason2;
        if (ex != null) {
            return null;
        }
        boolean hasValidNSEC2 = false;
        ValUtils.NsecProvesNodataResponse ndp = new ValUtils.NsecProvesNodataResponse();
        List<SRRset> nsec3s = new ArrayList<>(0);
        int edeReason3 = 12;
        Name ce = null;
        ValUtils.NsecProvesNodataResponse ndp2 = ndp;
        Name nsec3Signer = null;
        for (SRRset set : response.getSectionRRsets(2)) {
            if (set.getType() == 47) {
                NSECRecord nsec = (NSECRecord) set.first();
                ValUtils.NsecProvesNodataResponse ndp3 = ValUtils.nsecProvesNodata(set, nsec, qname, qtype);
                if (ndp3.result) {
                    hasValidNSEC2 = true;
                } else {
                    edeReason3 = 6;
                }
                if (!ValUtils.nsecProvesNameError(set, nsec, qname)) {
                    ndp2 = ndp3;
                } else {
                    ndp2 = ndp3;
                    ce = ValUtils.closestEncloser(qname, set.getName(), nsec.getNext());
                }
            }
            if (set.getType() == 50) {
                nsec3s.add(set);
                nsec3Signer = set.getSignerName();
            }
        }
        if (ndp2.f284wc != null && (ce == null || (!ce.equals(ndp2.f284wc) && !qname.equals(ce)))) {
            hasValidNSEC = false;
            edeReason = 6;
        } else {
            hasValidNSEC = hasValidNSEC2;
            edeReason = edeReason3;
        }
        this.n3valUtils.stripUnknownAlgNSEC3s(nsec3s);
        if (hasValidNSEC || nsec3s.isEmpty()) {
            i = -1;
            edeReason2 = edeReason;
        } else {
            log.debug("Using NSEC3 records");
            if (!this.n3valUtils.allNSEC3sIgnorable(nsec3s, this.keyCache)) {
                i = -1;
                JustifiedSecStatus res = this.n3valUtils.proveNodata(nsec3s, qname, qtype, nsec3Signer, nsec3State);
                edeReason2 = res.edeReason;
                if (res.status == SecurityStatus.INSECURE) {
                    response.setStatus(SecurityStatus.INSECURE, -1);
                    return null;
                }
                hasValidNSEC = res.status == SecurityStatus.SECURE;
            } else {
                response.setBogus(C1336R.get("failed.nsec3_ignored", new Object[0]));
                return null;
            }
        }
        if (!hasValidNSEC) {
            response.setBogus(C1336R.get("failed.nodata", new Object[0]), edeReason2);
            log.trace("Failed NODATA for {}", qname);
            return null;
        }
        log.trace("Successfully validated NODATA response");
        response.setStatus(SecurityStatus.SECURE, i);
        return null;
    }

    private CompletionStage<Void> validateNodataResponseRecursive(final SMessage response, final AtomicInteger setIndex, final Nsec3ValidationState nsec3State, final Executor executor) {
        if (setIndex.get() >= response.getSectionRRsets(2).size()) {
            return CompletableFuture.completedFuture(null);
        }
        final SRRset set = response.getSectionRRsets(2).get(setIndex.getAndIncrement());
        return prepareFindKey(set, executor).thenComposeAsync(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m183xf1e71e9e(set, response, setIndex, nsec3State, executor, (KeyEntry) obj);
            }
        });
    }

    /* renamed from: lambda$validateNodataResponseRecursive$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m183xf1e71e9e(SRRset set, SMessage response, AtomicInteger setIndex, Nsec3ValidationState nsec3State, Executor executor, KeyEntry ke) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        JustifiedSecStatus kve = ke.validateKeyFor(set);
        if (kve != null) {
            kve.applyToResponse(response);
            return failedFuture(new Exception(kve.reason));
        }
        JustifiedSecStatus res = this.valUtils.verifySRRset(set, ke, this.clock.instant());
        if (res.status != SecurityStatus.SECURE) {
            response.setBogus(C1336R.get("failed.authority.nodata", set));
            return failedFuture(new Exception("failed.authority.nodata"));
        }
        return validateNodataResponseRecursive(response, setIndex, nsec3State, executor);
    }

    private <T> CompletionStage<T> failedFuture(Throwable e) {
        CompletableFuture<T> f = new CompletableFuture<>();
        f.completeExceptionally(e);
        return f;
    }

    private CompletionStage<Void> validateNameErrorResponse(final Message request, final SMessage response, final Nsec3ValidationState nsec3State, final Executor executor) {
        Name intermediateQname = request.getQuestion().getName();
        for (SRRset set : response.getSectionRRsets(1)) {
            if (set.getSecurityStatus() != SecurityStatus.SECURE) {
                response.setBogus(C1336R.get("failed.nxdomain.cname_nxdomain", set));
                return CompletableFuture.completedFuture(null);
            }
            if (set.getType() == 5) {
                intermediateQname = ((CNAMERecord) set.first()).getTarget();
            }
        }
        final Name qname = intermediateQname;
        return validateNameErrorResponseRecursive(response, new AtomicInteger(0), executor).thenComposeAsync(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m180x1d6c90b6(response, qname, nsec3State, request, executor, (Void) obj);
            }
        }).exceptionally(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ValidatingResolver.lambda$validateNameErrorResponse$2((Throwable) obj);
            }
        });
    }

    /* renamed from: lambda$validateNameErrorResponse$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m180x1d6c90b6(final SMessage response, Name qname, Nsec3ValidationState nsec3State, Message request, Executor executor, Void v) {
        boolean hasValidNSEC = false;
        boolean hasValidWCNSEC = false;
        List<SRRset> nsec3s = new ArrayList<>(0);
        Name nsec3Signer = null;
        int previousClosestEncloseLabels = 0;
        for (SRRset set : response.getSectionRRsets(2)) {
            if (set.getType() == 47) {
                NSECRecord nsec = (NSECRecord) set.first();
                if (ValUtils.nsecProvesNameError(set, nsec, qname)) {
                    hasValidNSEC = true;
                }
                Name next = nsec.getNext();
                int closestEncloserLabels = ValUtils.closestEncloser(qname, set.getName(), next).labels();
                if (closestEncloserLabels > previousClosestEncloseLabels || (closestEncloserLabels == previousClosestEncloseLabels && !hasValidWCNSEC)) {
                    hasValidWCNSEC = ValUtils.nsecProvesNoWC(set, nsec, qname);
                }
                previousClosestEncloseLabels = closestEncloserLabels;
            }
            if (set.getType() == 50) {
                nsec3s.add(set);
                nsec3Signer = set.getSignerName();
            }
        }
        this.n3valUtils.stripUnknownAlgNSEC3s(nsec3s);
        if ((!hasValidNSEC || !hasValidWCNSEC) && !nsec3s.isEmpty()) {
            log.debug("Validating nxdomain: using NSEC3 records");
            if (this.n3valUtils.allNSEC3sIgnorable(nsec3s, this.keyCache)) {
                response.setStatus(SecurityStatus.INSECURE, -1, C1336R.get("failed.nsec3_ignored", new Object[0]));
                return CompletableFuture.completedFuture(null);
            }
            SecurityStatus status = this.n3valUtils.proveNameError(nsec3s, qname, nsec3Signer, nsec3State);
            if (status != SecurityStatus.SECURE) {
                if (status == SecurityStatus.INSECURE) {
                    response.setStatus(status, -1, C1336R.get("failed.nxdomain.nsec3_insecure", new Object[0]));
                } else {
                    response.setStatus(status, 6, C1336R.get("failed.nxdomain.nsec3_bogus", new Object[0]));
                }
                return CompletableFuture.completedFuture(null);
            }
            hasValidNSEC = true;
            hasValidWCNSEC = true;
        }
        if (!hasValidNSEC || !hasValidWCNSEC) {
            final boolean hasValidNSEC2 = hasValidNSEC;
            return validateNodataResponse(request, response, nsec3State, executor).thenRun(new Runnable() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    ValidatingResolver.lambda$validateNameErrorResponse$1(response, hasValidNSEC2);
                }
            });
        }
        log.trace("Successfully validated NAME ERROR response");
        response.setStatus(SecurityStatus.SECURE, -1);
        return CompletableFuture.completedFuture(null);
    }

    static /* synthetic */ void lambda$validateNameErrorResponse$1(SMessage response, boolean hasValidNSEC2) {
        if (response.getStatus() == SecurityStatus.SECURE) {
            response.getHeader().setRcode(0);
        } else if (!hasValidNSEC2) {
            response.setBogus(C1336R.get("failed.nxdomain.exists", response.getQuestion().getName()));
        } else {
            response.setBogus(C1336R.get("failed.nxdomain.haswildcard", new Object[0]));
        }
    }

    static /* synthetic */ Void lambda$validateNameErrorResponse$2(Throwable ex) {
        return null;
    }

    private CompletionStage<Void> validateNameErrorResponseRecursive(final SMessage response, final AtomicInteger setIndex, final Executor executor) {
        if (setIndex.get() >= response.getSectionRRsets(2).size()) {
            return CompletableFuture.completedFuture(null);
        }
        final SRRset set = response.getSectionRRsets(2).get(setIndex.getAndIncrement());
        return prepareFindKey(set, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m181x9de42d12(set, response, setIndex, executor, (KeyEntry) obj);
            }
        });
    }

    /* renamed from: lambda$validateNameErrorResponseRecursive$0$org-xbill-DNS-dnssec-ValidatingResolver */
    /* synthetic */ CompletionStage m181x9de42d12(SRRset set, SMessage response, AtomicInteger setIndex, Executor executor, KeyEntry ke) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        JustifiedSecStatus kve = ke.validateKeyFor(set);
        if (kve != null) {
            kve.applyToResponse(response);
            return failedFuture(new Exception(kve.reason));
        }
        JustifiedSecStatus res = this.valUtils.verifySRRset(set, ke, this.clock.instant());
        if (res.status != SecurityStatus.SECURE) {
            response.setBogus(C1336R.get("failed.nxdomain.authority", set));
            return failedFuture(new Exception("failed.nxdomain.authority"));
        }
        return validateNameErrorResponseRecursive(response, setIndex, executor);
    }

    private CompletionStage<SMessage> sendRequest(Message request, Executor executor) throws CloneNotSupportedException {
        Record q = request.getQuestion();
        log.trace("Sending request: <{}/{}/{}>", q.getName(), Type.string(q.getType()), DClass.string(q.getDClass()));
        final Message localRequest = request.clone();
        localRequest.getHeader().setFlag(11);
        return this.headResolver.sendAsync(localRequest, executor).thenApply(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ValidatingResolver.lambda$sendRequest$0(localRequest, (Message) obj);
            }
        });
    }

    static /* synthetic */ SMessage lambda$sendRequest$0(Message localRequest, Message message) {
        return new SMessage(message.normalize(localRequest));
    }

    private CompletionStage<KeyEntry> prepareFindKey(SRRset rrset, Executor executor) {
        final FindKeyState state = new FindKeyState();
        state.signerName = rrset.getSignerName();
        state.qclass = rrset.getDClass();
        if (state.signerName == null) {
            state.signerName = rrset.getName();
        }
        RRset trustAnchorRRset = this.trustAnchors.find(state.signerName, rrset.getDClass());
        if (trustAnchorRRset == null) {
            KeyEntry ke = KeyEntry.newNullKeyEntry(state.signerName, rrset.getDClass(), DEFAULT_TA_BAD_KEY_TTL);
            return CompletableFuture.completedFuture(ke);
        }
        SRRset trustAnchorSRRset = new SRRset(trustAnchorRRset);
        trustAnchorSRRset.setSecurityStatus(SecurityStatus.SECURE);
        state.keyEntry = this.keyCache.find(state.signerName, rrset.getDClass());
        if (state.keyEntry == null || (!state.keyEntry.getName().equals(state.signerName) && state.keyEntry.isGood())) {
            if (trustAnchorSRRset.getType() == 43) {
                state.dsRRset = trustAnchorSRRset;
                state.keyEntry = null;
                state.currentDSKeyName = new Name(trustAnchorRRset.getName(), 1);
                return processFindKey(state, executor).thenApply(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda7
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return state.keyEntry;
                    }
                });
            }
            state.keyEntry = KeyEntry.newKeyEntry(trustAnchorSRRset);
            state.keyEntry.setSecurityStatus(SecurityStatus.SECURE);
            this.keyCache.store(state.keyEntry);
        }
        return CompletableFuture.completedFuture(state.keyEntry);
    }

    private CompletionStage<Void> processFindKey(final FindKeyState state, final Executor executor) {
        int qclass = state.qclass;
        Name targetKeyName = state.signerName;
        Name currentKeyName = Name.empty;
        if (state.keyEntry != null) {
            currentKeyName = state.keyEntry.getName();
        }
        if (state.currentDSKeyName != null) {
            currentKeyName = state.currentDSKeyName;
            state.currentDSKeyName = null;
        }
        if (currentKeyName.equals(targetKeyName)) {
            return CompletableFuture.completedFuture(null);
        }
        if (state.emptyDSName != null) {
            currentKeyName = state.emptyDSName;
        }
        int targetLabels = targetKeyName.labels();
        int currentLabels = currentKeyName.labels();
        int l = (targetLabels - currentLabels) - 1;
        if (l < 0) {
            return CompletableFuture.completedFuture(null);
        }
        Name nextKeyName = new Name(targetKeyName, l);
        log.trace("Key search: targetKeyName = {}, currentKeyName = {}, nextKeyName = {}", targetKeyName, currentKeyName, nextKeyName);
        if (state.dsRRset == null || !state.dsRRset.getName().equals(nextKeyName)) {
            final Message dsRequest = Message.newQuery(Record.newRecord(nextKeyName, 43, qclass));
            return sendRequest(dsRequest, executor).thenComposeAsync(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.m1853lambda$processFindKey$0$orgxbillDNSdnssecValidatingResolver(dsRequest, state, executor, (SMessage) obj);
                }
            });
        }
        final Message dnskeyRequest = Message.newQuery(Record.newRecord(state.dsRRset.getName(), 48, qclass));
        return sendRequest(dnskeyRequest, executor).thenComposeAsync(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1854lambda$processFindKey$1$orgxbillDNSdnssecValidatingResolver(dnskeyRequest, state, executor, (SMessage) obj);
            }
        });
    }

    private KeyEntry dsResponseToKE(SMessage response, Message request, KeyEntry keyRrset) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        Name qname = request.getQuestion().getName();
        int qclass = request.getQuestion().getDClass();
        ResponseClassification subtype = ValUtils.classifyResponse(request, response);
        KeyEntry bogusKE = KeyEntry.newBadKeyEntry(qname, qclass, DEFAULT_TA_BAD_KEY_TTL);
        switch (subtype) {
            case POSITIVE:
                SRRset dsRrset = response.findAnswerRRset(qname, 43, qclass);
                JustifiedSecStatus res = this.valUtils.verifySRRset(dsRrset, keyRrset, this.clock.instant());
                if (res.status != SecurityStatus.SECURE) {
                    bogusKE.setBadReason(res.edeReason, res.reason);
                    return bogusKE;
                }
                if (!this.valUtils.atLeastOneSupportedAlgorithm(dsRrset)) {
                    KeyEntry nullKey = KeyEntry.newNullKeyEntry(qname, qclass, dsRrset.getTTL());
                    nullKey.setBadReason(1, C1336R.get("insecure.ds.noalgorithms", qname));
                    return nullKey;
                }
                log.trace("DS RRset was good");
                return KeyEntry.newKeyEntry(dsRrset);
            case CNAME:
                SRRset cnameRrset = response.findAnswerRRset(qname, 5, qclass);
                if (this.valUtils.verifySRRset(cnameRrset, keyRrset, this.clock.instant()).status == SecurityStatus.SECURE) {
                    return null;
                }
                bogusKE.setBadReason(6, C1336R.get("failed.ds.cname", new Object[0]));
                return bogusKE;
            case NODATA:
            case NAMEERROR:
                return dsResponseToKeForNodata(response, request, keyRrset);
            default:
                bogusKE.setBadReason(6, C1336R.get("failed.ds.notype", subtype));
                return bogusKE;
        }
    }

    private KeyEntry dsResponseToKeForNodata(SMessage response, Message request, KeyEntry keyRrset) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        Name qname = request.getQuestion().getName();
        int qclass = request.getQuestion().getDClass();
        KeyEntry bogusKE = KeyEntry.newBadKeyEntry(qname, qclass, DEFAULT_TA_BAD_KEY_TTL);
        if (!this.valUtils.hasSignedNsecs(response)) {
            bogusKE.setBadReason(10, C1336R.get("failed.ds.nonsec", qname));
            return bogusKE;
        }
        JustifiedSecStatus status = this.valUtils.nsecProvesNodataDsReply(request, response, keyRrset, this.clock.instant());
        switch (status.status) {
            case SECURE:
                KeyEntry nullKey = KeyEntry.newNullKeyEntry(qname, qclass, DEFAULT_TA_BAD_KEY_TTL);
                nullKey.setBadReason(-1, C1336R.get("insecure.ds.nsec", new Object[0]));
                break;
            case INSECURE:
                break;
            case BOGUS:
                bogusKE.setBadReason(status.edeReason, status.reason);
                break;
            default:
                List<SRRset> nsec3Rrsets = response.getSectionRRsets(2, 50);
                List<SRRset> nsec3s = new ArrayList<>(0);
                Name nsec3Signer = null;
                long nsec3TTL = -1;
                if (!nsec3Rrsets.isEmpty()) {
                    for (SRRset nsec3set : nsec3Rrsets) {
                        JustifiedSecStatus res = this.valUtils.verifySRRset(nsec3set, keyRrset, this.clock.instant());
                        if (res.status != SecurityStatus.SECURE) {
                            log.debug("Skipping bad NSEC3");
                        } else {
                            nsec3Signer = nsec3set.getSignerName();
                            if (nsec3TTL < 0 || nsec3set.getTTL() < nsec3TTL) {
                                nsec3TTL = nsec3set.getTTL();
                            }
                            nsec3s.add(nsec3set);
                        }
                    }
                    new Nsec3ValidationState();
                    switch (this.n3valUtils.proveNoDS(nsec3s, qname, nsec3Signer, nsec3State)) {
                        case SECURE:
                        case INSECURE:
                            KeyEntry nullKey2 = KeyEntry.newNullKeyEntry(qname, qclass, nsec3TTL);
                            nullKey2.setBadReason(-1, C1336R.get("insecure.ds.nsec3", new Object[0]));
                            break;
                        case BOGUS:
                            bogusKE.setBadReason(6, C1336R.get("failed.ds.nsec3", new Object[0]));
                            break;
                        case INDETERMINATE:
                            log.debug("NSEC3s for the referral proved no delegation");
                            break;
                        default:
                            bogusKE.setBadReason(6, C1336R.get("unknown.ds.nsec3", new Object[0]));
                            break;
                    }
                } else {
                    bogusKE.setBadReason(6, C1336R.get("failed.ds.unknown", new Object[0]));
                    break;
                }
                break;
        }
        return bogusKE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: processDSResponse, reason: merged with bridge method [inline-methods] */
    public CompletionStage<Void> m1853lambda$processFindKey$0$orgxbillDNSdnssecValidatingResolver(Message request, SMessage response, FindKeyState state, Executor executor) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        Name qname = request.getQuestion().getName();
        state.emptyDSName = null;
        state.dsRRset = null;
        KeyEntry dsKE = dsResponseToKE(response, request, state.keyEntry);
        if (dsKE == null) {
            state.emptyDSName = qname;
        } else if (dsKE.isGood()) {
            state.dsRRset = dsKE;
            state.currentDSKeyName = new Name(dsKE.getName(), 1);
        } else {
            state.keyEntry = dsKE;
            if (dsKE.isNull()) {
                this.keyCache.store(dsKE);
            }
            return CompletableFuture.completedFuture(null);
        }
        return processFindKey(state, executor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: processDNSKEYResponse, reason: merged with bridge method [inline-methods] */
    public CompletionStage<Void> m1854lambda$processFindKey$1$orgxbillDNSdnssecValidatingResolver(Message request, SMessage response, FindKeyState state, Executor executor) {
        Name qname = request.getQuestion().getName();
        int qclass = request.getQuestion().getDClass();
        SRRset dnskeyRrset = response.findAnswerRRset(qname, 48, qclass);
        if (dnskeyRrset == null) {
            state.keyEntry = KeyEntry.newBadKeyEntry(qname, qclass, DEFAULT_TA_BAD_KEY_TTL);
            state.keyEntry.setBadReason(9, C1336R.get("dnskey.no_rrset", qname));
            return CompletableFuture.completedFuture(null);
        }
        state.keyEntry = this.valUtils.verifyNewDNSKEYs(dnskeyRrset, state.dsRRset, DEFAULT_TA_BAD_KEY_TTL, this.clock.instant());
        if (!state.keyEntry.isGood()) {
            return CompletableFuture.completedFuture(null);
        }
        this.keyCache.store(state.keyEntry);
        return processFindKey(state, executor);
    }

    private CompletionStage<SMessage> processValidate(final Message request, final SMessage response, final Executor executor) {
        CompletionStage<Void> completionStage;
        ResponseClassification subtype = ValUtils.classifyResponse(request, response);
        if (subtype != ResponseClassification.REFERRAL) {
            removeSpuriousAuthority(response);
        }
        final Nsec3ValidationState nsec3State = new Nsec3ValidationState();
        switch (subtype) {
            case POSITIVE:
            case CNAME:
            case ANY:
                log.trace("Validating a positive response");
                completionStage = validatePositiveResponse(request, response, nsec3State, executor);
                break;
            case NODATA:
                log.trace("Validating a nodata response");
                completionStage = validateNodataResponse(request, response, nsec3State, executor);
                break;
            case NAMEERROR:
                log.trace("Validating a nxdomain response");
                completionStage = validateNameErrorResponse(request, response, nsec3State, executor);
                break;
            case CNAME_NODATA:
                log.trace("Validating a CNAME_NODATA response");
                completionStage = validatePositiveResponse(request, response, nsec3State, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return this.f$0.m1855lambda$processValidate$0$orgxbillDNSdnssecValidatingResolver(response, request, nsec3State, executor, (Void) obj);
                    }
                });
                break;
            case CNAME_NAMEERROR:
                log.trace("Validating a cname_nxdomain response");
                completionStage = validatePositiveResponse(request, response, nsec3State, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return this.f$0.m1856lambda$processValidate$1$orgxbillDNSdnssecValidatingResolver(response, request, nsec3State, executor, (Void) obj);
                    }
                });
                break;
            default:
                response.setBogus(C1336R.get("validate.response.unknown", subtype));
                completionStage = CompletableFuture.completedFuture(null);
                break;
        }
        return completionStage.thenApply(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1857lambda$processValidate$2$orgxbillDNSdnssecValidatingResolver(request, response, (Void) obj);
            }
        });
    }

    /* renamed from: lambda$processValidate$0$org-xbill-DNS-dnssec-ValidatingResolver, reason: not valid java name */
    /* synthetic */ CompletionStage m1855lambda$processValidate$0$orgxbillDNSdnssecValidatingResolver(SMessage response, Message request, Nsec3ValidationState nsec3State, Executor executor, Void v) {
        if (response.getStatus() != SecurityStatus.INSECURE) {
            response.setStatus(SecurityStatus.UNCHECKED, -1);
            return validateNodataResponse(request, response, nsec3State, executor);
        }
        return CompletableFuture.completedFuture(null);
    }

    /* renamed from: lambda$processValidate$1$org-xbill-DNS-dnssec-ValidatingResolver, reason: not valid java name */
    /* synthetic */ CompletionStage m1856lambda$processValidate$1$orgxbillDNSdnssecValidatingResolver(SMessage response, Message request, Nsec3ValidationState nsec3State, Executor executor, Void v) {
        if (response.getStatus() != SecurityStatus.INSECURE) {
            response.setStatus(SecurityStatus.UNCHECKED, -1);
            return validateNameErrorResponse(request, response, nsec3State, executor);
        }
        return CompletableFuture.completedFuture(null);
    }

    /* renamed from: lambda$processValidate$2$org-xbill-DNS-dnssec-ValidatingResolver, reason: not valid java name */
    /* synthetic */ SMessage m1857lambda$processValidate$2$orgxbillDNSdnssecValidatingResolver(Message request, SMessage response, Void v) {
        return processFinishedState(request, response);
    }

    private SMessage processFinishedState(Message request, SMessage response) {
        SecurityStatus status = response.getStatus();
        String reason = response.getBogusReason();
        int edeReason = response.getEdeReason();
        switch (status) {
            case SECURE:
                response.getHeader().setFlag(10);
                break;
            case INSECURE:
            case UNCHECKED:
                break;
            case BOGUS:
                int code = response.getHeader().getRcode();
                if (code == 0 || code == 3) {
                    code = 2;
                }
                response = errorMessage(request, code);
                break;
            case INDETERMINATE:
            default:
                throw new IllegalArgumentException("unexpected security status");
        }
        response.setStatus(status, edeReason, reason);
        return response;
    }

    @Override // org.xbill.DNS.Resolver
    public void setPort(int port) {
        this.headResolver.setPort(port);
    }

    @Override // org.xbill.DNS.Resolver
    public void setTCP(boolean flag) {
        this.headResolver.setTCP(flag);
    }

    @Override // org.xbill.DNS.Resolver
    public void setIgnoreTruncation(boolean flag) {
    }

    @Override // org.xbill.DNS.Resolver
    public void setEDNS(int version, int payloadSize, int flags, List<EDNSOption> options) {
        if (version == -1) {
            throw new IllegalArgumentException("EDNS cannot be disabled");
        }
        this.headResolver.setEDNS(version, payloadSize, 32768 | flags, options);
    }

    @Override // org.xbill.DNS.Resolver
    public void setTSIGKey(TSIG key) {
        this.headResolver.setTSIGKey(key);
    }

    @Override // org.xbill.DNS.Resolver
    public Duration getTimeout() {
        return this.headResolver.getTimeout();
    }

    @Override // org.xbill.DNS.Resolver
    public void setTimeout(Duration duration) {
        this.headResolver.setTimeout(duration);
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(final Message query, final Executor executor) {
        return sendRequest(query, executor).thenCompose(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1858lambda$sendAsync$0$orgxbillDNSdnssecValidatingResolver(query, executor, (SMessage) obj);
            }
        });
    }

    /* renamed from: lambda$sendAsync$0$org-xbill-DNS-dnssec-ValidatingResolver, reason: not valid java name */
    /* synthetic */ CompletionStage m1858lambda$sendAsync$0$orgxbillDNSdnssecValidatingResolver(Message query, Executor executor, SMessage response) {
        response.getHeader().unsetFlag(10);
        if (query.getHeader().getFlag(11)) {
            return CompletableFuture.completedFuture(response.getMessage());
        }
        Message rrsigResponse = response.getMessage();
        if (query.getQuestion().getType() == 46 && rrsigResponse.getHeader().getRcode() == 0 && !rrsigResponse.getSectionRRsets(1).isEmpty()) {
            rrsigResponse.getHeader().unsetFlag(10);
            return CompletableFuture.completedFuture(rrsigResponse);
        }
        return processValidate(query, response, executor).thenApply(new Function() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1859lambda$sendAsync$1$orgxbillDNSdnssecValidatingResolver((SMessage) obj);
            }
        });
    }

    /* renamed from: lambda$sendAsync$1$org-xbill-DNS-dnssec-ValidatingResolver, reason: not valid java name */
    /* synthetic */ Message m1859lambda$sendAsync$1$orgxbillDNSdnssecValidatingResolver(SMessage validated) {
        Message m = validated.getMessage();
        String reason = validated.getBogusReason();
        if (reason != null) {
            applyEdeToOpt(validated, m);
            if (this.isAddReasonToAdditional) {
                addValidationReasonTxtRecord(m, reason);
            }
        }
        return m;
    }

    private void applyEdeToOpt(SMessage validated, Message m) {
        OPTRecord newOpt;
        if (validated.getEdeReason() <= -1) {
            return;
        }
        OPTRecord old = m.getOPT();
        List<EDNSOption> options = new ArrayList<>();
        options.add(new ExtendedErrorCodeOption(validated.getEdeReason(), validated.getBogusReason()));
        if (old != null) {
            options.addAll((Collection) old.getOptions().stream().filter(new Predicate() { // from class: org.xbill.DNS.dnssec.ValidatingResolver$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ValidatingResolver.lambda$applyEdeToOpt$0((EDNSOption) obj);
                }
            }).collect(Collectors.toList()));
            newOpt = new OPTRecord(old.getPayloadSize(), old.getExtendedRcode(), old.getVersion(), old.getFlags(), options);
            m.removeRecord(m.getOPT(), 3);
        } else {
            newOpt = new OPTRecord(SimpleResolver.DEFAULT_EDNS_PAYLOADSIZE, 0, 0, 0, options);
        }
        m.addRecord(newOpt, 3);
    }

    static /* synthetic */ boolean lambda$applyEdeToOpt$0(EDNSOption o) {
        return o.getCode() != 15;
    }

    private void addValidationReasonTxtRecord(Message m, String reason) {
        String[] parts = new String[(reason.length() / 255) + 1];
        for (int i = 0; i < parts.length; i++) {
            int length = Math.min((i + 1) * 255, reason.length());
            parts[i] = reason.substring(i * 255, length);
        }
        m.addRecord(new TXTRecord(Name.root, 65280, 0L, (List<String>) Arrays.asList(parts)), 3);
    }

    private static SMessage errorMessage(Message request, int rcode) {
        SMessage m = new SMessage(request.getHeader().getID(), request.getQuestion());
        Header h = m.getHeader();
        h.setRcode(rcode);
        h.setFlag(0);
        return m;
    }
}
