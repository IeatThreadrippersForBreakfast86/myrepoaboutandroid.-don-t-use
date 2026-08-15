package org.xbill.DNS.lookup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNAMERecord;
import org.xbill.DNS.EDNSOption;
import org.xbill.DNS.ExtendedErrorCodeOption;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ResolverConfig;
import org.xbill.DNS.SetResponse;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WireParseException;
import org.xbill.DNS.hosts.HostsFileParser;
import org.xbill.DNS.lookup.LookupSession;

/* loaded from: classes8.dex */
public class LookupSession {
    public static final int DEFAULT_MAX_ITERATIONS = 16;
    public static final int DEFAULT_NDOTS = 1;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) LookupSession.class);
    private final Map<Integer, Cache> caches;
    private final boolean cycleResults;
    private final Executor executor;
    private final HostsFileParser hostsFileParser;
    private final IrrelevantRecordMode irrelevantRecordMode;
    private final int maxRedirects;
    private final int ndots;
    private final Resolver resolver;
    private final List<Name> searchPath;

    /* renamed from: $r8$lambda$wBZ5N9SAmxJLRX-vx8hIUeo_fgc, reason: not valid java name */
    public static /* synthetic */ ArrayList m1862$r8$lambda$wBZ5N9SAmxJLRXvx8hIUeo_fgc() {
        return new ArrayList();
    }

    private LookupSession(Resolver resolver, int maxRedirects, int ndots, List<Name> searchPath, boolean cycleResults, List<Cache> caches, HostsFileParser hostsFileParser, Executor executor, IrrelevantRecordMode irrelevantRecordMode) {
        Map<Integer, Cache> mapEmptyMap;
        if (resolver == null) {
            throw new NullPointerException("resolver is marked non-null but is null");
        }
        this.resolver = resolver;
        this.maxRedirects = maxRedirects;
        this.ndots = ndots;
        this.searchPath = searchPath;
        this.cycleResults = cycleResults;
        if (caches == null) {
            mapEmptyMap = Collections.emptyMap();
        } else {
            mapEmptyMap = (Map) caches.stream().collect(Collectors.toMap(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Integer.valueOf(((Cache) obj).getDClass());
                }
            }, new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return LookupSession.lambda$new$0((Cache) obj);
                }
            }));
        }
        this.caches = mapEmptyMap;
        this.hostsFileParser = hostsFileParser;
        this.executor = executor == null ? ForkJoinPool.commonPool() : executor;
        this.irrelevantRecordMode = irrelevantRecordMode;
    }

    static /* synthetic */ Cache lambda$new$0(Cache e) {
        return e;
    }

    public static class LookupSessionBuilder {
        private List<Cache> caches;
        private boolean cycleResults;
        private Executor executor;
        private HostsFileParser hostsFileParser;
        private IrrelevantRecordMode irrelevantRecordMode;
        private int maxRedirects;
        private int ndots;
        private Resolver resolver;
        private List<Name> searchPath;

        /* renamed from: $r8$lambda$wBZ5N9SAmxJLRX-vx8hIUeo_fgc, reason: not valid java name */
        public static /* synthetic */ ArrayList m1868$r8$lambda$wBZ5N9SAmxJLRXvx8hIUeo_fgc() {
            return new ArrayList();
        }

        public String toString() {
            return "LookupSession.LookupSessionBuilder(resolver=" + this.resolver + ", maxRedirects=" + this.maxRedirects + ", ndots=" + this.ndots + ", searchPath=" + this.searchPath + ", cycleResults=" + this.cycleResults + ", caches=" + this.caches + ", hostsFileParser=" + this.hostsFileParser + ", executor=" + this.executor + ", irrelevantRecordMode=" + this.irrelevantRecordMode + ")";
        }

        private LookupSessionBuilder() {
            this.irrelevantRecordMode = IrrelevantRecordMode.REMOVE;
        }

        public LookupSessionBuilder resolver(Resolver resolver) {
            if (resolver == null) {
                throw new NullPointerException("resolver is marked non-null but is null");
            }
            this.resolver = resolver;
            return this;
        }

        public LookupSessionBuilder maxRedirects(int maxRedirects) {
            this.maxRedirects = maxRedirects;
            return this;
        }

        public LookupSessionBuilder ndots(int ndots) {
            this.ndots = ndots;
            return this;
        }

        public LookupSessionBuilder searchPath(Name searchPath) {
            if (this.searchPath == null) {
                this.searchPath = new ArrayList();
            }
            this.searchPath.add(searchPath);
            return this;
        }

        public LookupSessionBuilder searchPath(Collection<? extends Name> searchPath) {
            if (this.searchPath == null) {
                this.searchPath = new ArrayList();
            }
            this.searchPath.addAll(searchPath);
            return this;
        }

        public LookupSessionBuilder clearSearchPath() {
            if (this.searchPath != null) {
                this.searchPath.clear();
            }
            return this;
        }

        public LookupSessionBuilder cycleResults(boolean cycleResults) {
            this.cycleResults = cycleResults;
            return this;
        }

        public LookupSessionBuilder hostsFileParser(HostsFileParser hostsFileParser) {
            this.hostsFileParser = hostsFileParser;
            return this;
        }

        public LookupSessionBuilder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        LookupSessionBuilder irrelevantRecordMode(IrrelevantRecordMode irrelevantRecordMode) {
            this.irrelevantRecordMode = irrelevantRecordMode;
            return this;
        }

        public LookupSessionBuilder defaultHostsFileParser() {
            this.hostsFileParser = new HostsFileParser();
            return this;
        }

        public LookupSessionBuilder cache(Cache cache) {
            if (cache == null) {
                throw new NullPointerException("cache is marked non-null but is null");
            }
            if (this.caches == null) {
                this.caches = new ArrayList(1);
            }
            Iterator<Cache> it = this.caches.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Cache c = it.next();
                if (c.getDClass() == cache.getDClass()) {
                    this.caches.remove(c);
                    break;
                }
            }
            this.caches.add(cache);
            return this;
        }

        public LookupSessionBuilder caches(Collection<Cache> caches) {
            if (caches == null) {
                throw new NullPointerException("caches is marked non-null but is null");
            }
            caches.forEach(new Consumer() { // from class: org.xbill.DNS.lookup.LookupSession$LookupSessionBuilder$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.cache((Cache) obj);
                }
            });
            return this;
        }

        public LookupSessionBuilder clearCaches() {
            if (this.caches != null) {
                this.caches.clear();
            }
            return this;
        }

        @Deprecated
        public LookupSessionBuilder cache(Integer dclass, Cache cache) {
            if (dclass == null) {
                throw new NullPointerException("dclass is marked non-null but is null");
            }
            if (cache == null) {
                throw new NullPointerException("cache is marked non-null but is null");
            }
            cache(cache);
            return this;
        }

        @Deprecated
        public LookupSessionBuilder caches(Map<Integer, Cache> caches) {
            if (caches == null) {
                throw new NullPointerException("caches is marked non-null but is null");
            }
            return caches(caches.values());
        }

        public LookupSession build() {
            if (this.searchPath != null) {
                this.searchPath = (List) this.searchPath.stream().map(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$LookupSessionBuilder$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return LookupSession.LookupSessionBuilder.lambda$build$0((Name) obj);
                    }
                }).collect(Collectors.toCollection(new Supplier() { // from class: org.xbill.DNS.lookup.LookupSession$LookupSessionBuilder$$ExternalSyntheticLambda2
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        return LookupSession.LookupSessionBuilder.m1868$r8$lambda$wBZ5N9SAmxJLRXvx8hIUeo_fgc();
                    }
                }));
            } else {
                this.searchPath = Collections.emptyList();
            }
            return new LookupSession(this.resolver, this.maxRedirects, this.ndots, this.searchPath, this.cycleResults, this.caches, this.hostsFileParser, this.executor, this.irrelevantRecordMode);
        }

        static /* synthetic */ Name lambda$build$0(Name name) {
            try {
                return Name.concatenate(name, Name.root);
            } catch (NameTooLongException e) {
                throw new IllegalArgumentException("Search path name too long");
            }
        }
    }

    public static LookupSessionBuilder builder() {
        LookupSessionBuilder builder = new LookupSessionBuilder();
        builder.maxRedirects = 16;
        builder.ndots = 1;
        return builder;
    }

    public static LookupSessionBuilder defaultBuilder() {
        return builder().resolver(new ExtendedResolver((Iterable<Resolver>) ResolverConfig.getCurrentConfig().servers().stream().map(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return new SimpleResolver((InetSocketAddress) obj);
            }
        }).collect(Collectors.toList()))).ndots(ResolverConfig.getCurrentConfig().ndots()).cache(new Cache(1)).defaultHostsFileParser();
    }

    Cache getCache(int dclass) {
        return this.caches.get(Integer.valueOf(dclass));
    }

    public CompletionStage<LookupResult> lookupAsync(Record question) {
        return lookupAsync(question.getName(), question.getType(), question.getDClass());
    }

    public CompletionStage<LookupResult> lookupAsync(Name name, int type) {
        return lookupAsync(name, type, 1);
    }

    public CompletionStage<LookupResult> lookupAsync(Name name, int type, int dclass) {
        List<Name> searchNames = expandName(name);
        LookupResult localHostsLookupResult = lookupWithHosts(searchNames, type);
        if (localHostsLookupResult != null) {
            return CompletableFuture.completedFuture(localHostsLookupResult);
        }
        return lookupUntilSuccess(searchNames.iterator(), type, dclass);
    }

    List<Name> expandName(final Name name) {
        if (name.isAbsolute()) {
            return Collections.singletonList(name);
        }
        List<Name> fromSearchPath = (List) this.searchPath.stream().map(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return LookupSession.safeConcat(name, (Name) obj);
            }
        }).filter(new Predicate() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Name) obj);
            }
        }).collect(Collectors.toCollection(new Supplier() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final Object get() {
                return LookupSession.m1862$r8$lambda$wBZ5N9SAmxJLRXvx8hIUeo_fgc();
            }
        }));
        if (name.labels() > this.ndots) {
            fromSearchPath.add(0, safeConcat(name, Name.root));
        } else {
            fromSearchPath.add(safeConcat(name, Name.root));
        }
        return fromSearchPath;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Name safeConcat(Name name, Name suffix) {
        try {
            return Name.concatenate(name, suffix);
        } catch (NameTooLongException e) {
            return null;
        }
    }

    private LookupResult lookupWithHosts(List<Name> names, int type) {
        Record r;
        if (this.hostsFileParser == null) {
            return null;
        }
        if (type == 1 || type == 28) {
            try {
                for (Name name : names) {
                    Optional<InetAddress> result = this.hostsFileParser.getAddressForHost(name, type);
                    if (result.isPresent()) {
                        if (type == 1) {
                            r = new ARecord(name, 1, 0L, result.get());
                        } else {
                            r = new AAAARecord(name, 1, 0L, result.get());
                        }
                        return new LookupResult(Record.newRecord(name, type, 1), true, r);
                    }
                }
                return null;
            } catch (IOException e) {
                log.debug("Local hosts database parsing failed, ignoring and using resolver", (Throwable) e);
                return null;
            }
        }
        return null;
    }

    private CompletionStage<LookupResult> lookupUntilSuccess(final Iterator<Name> names, final int type, final int dclass) {
        final Record query = Record.newRecord(names.next(), type, dclass);
        return lookupWithCache(query, null).thenCompose(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1863lambda$lookupUntilSuccess$0$orgxbillDNSlookupLookupSession(query, (LookupResult) obj);
            }
        }).handle(new BiFunction() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return this.f$0.m1864lambda$lookupUntilSuccess$1$orgxbillDNSlookupLookupSession(names, type, dclass, (LookupResult) obj, (Throwable) obj2);
            }
        }).thenCompose(Function.identity());
    }

    /* renamed from: lambda$lookupUntilSuccess$1$org-xbill-DNS-lookup-LookupSession, reason: not valid java name */
    /* synthetic */ CompletionStage m1864lambda$lookupUntilSuccess$1$orgxbillDNSlookupLookupSession(Iterator names, int type, int dclass, LookupResult result, Throwable ex) {
        Throwable cause = ex == null ? null : ex.getCause();
        if ((cause instanceof NoSuchDomainException) || (cause instanceof NoSuchRRSetException)) {
            if (names.hasNext()) {
                return lookupUntilSuccess(names, type, dclass);
            }
            return completeExceptionally(cause);
        }
        if (cause != null) {
            return completeExceptionally(cause);
        }
        return CompletableFuture.completedFuture(result);
    }

    private CompletionStage<LookupResult> lookupWithCache(final Record queryRecord, final List<Name> aliases) {
        return (CompletionStage) Optional.ofNullable(this.caches.get(Integer.valueOf(queryRecord.getDClass()))).map(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return LookupSession.lambda$lookupWithCache$0(queryRecord, (Cache) obj);
            }
        }).map(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1865lambda$lookupWithCache$1$orgxbillDNSlookupLookupSession(queryRecord, aliases, (SetResponse) obj);
            }
        }).orElseGet(new Supplier() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda15
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.m1866lambda$lookupWithCache$2$orgxbillDNSlookupLookupSession(queryRecord, aliases);
            }
        });
    }

    static /* synthetic */ SetResponse lambda$lookupWithCache$0(Record queryRecord, Cache c) {
        log.debug("Looking for <{}/{}/{}> in cache", queryRecord.getName(), Type.string(queryRecord.getType()), DClass.string(queryRecord.getDClass()));
        return c.lookupRecords(queryRecord.getName(), queryRecord.getType(), 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: lookupWithResolver, reason: merged with bridge method [inline-methods] */
    public CompletionStage<LookupResult> m1866lambda$lookupWithCache$2$orgxbillDNSlookupLookupSession(final Record queryRecord, final List<Name> aliases) {
        final Message query = Message.newQuery(queryRecord);
        log.debug("Asking {} for <{}/{}/{}>", this.resolver, queryRecord.getName(), Type.string(queryRecord.getType()), DClass.string(queryRecord.getDClass()));
        return this.resolver.sendAsync(query, this.executor).thenCompose(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1867lambda$lookupWithResolver$0$orgxbillDNSlookupLookupSession(query, queryRecord, (Message) obj);
            }
        }).thenApply(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.maybeAddToCache((Message) obj);
            }
        }).thenApply(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return LookupSession.buildResult((Message) obj, aliases, queryRecord);
            }
        });
    }

    /* renamed from: lambda$lookupWithResolver$0$org-xbill-DNS-lookup-LookupSession, reason: not valid java name */
    /* synthetic */ CompletionStage m1867lambda$lookupWithResolver$0$orgxbillDNSlookupLookupSession(Message query, Record queryRecord, Message m) {
        try {
            Message normalized = m.normalize(query, this.irrelevantRecordMode == IrrelevantRecordMode.THROW);
            log.trace("Normalized response for <{}/{}/{}> from \n{}\ninto\n{}", queryRecord.getName(), Type.string(queryRecord.getType()), DClass.string(queryRecord.getDClass()), m, normalized);
            if (normalized == null) {
                return completeExceptionally(new InvalidZoneDataException("Failed to normalize message"));
            }
            return CompletableFuture.completedFuture(normalized);
        } catch (WireParseException e) {
            return completeExceptionally(new LookupFailedException("Message normalization failed, refusing to return it", e));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message maybeAddToCache(final Message message) {
        for (RRset set : message.getSectionRRsets(1)) {
            if (set.getType() == 5 || set.getType() == 39) {
                if (set.size() != 1) {
                    throw new InvalidZoneDataException("Multiple CNAME RRs not allowed, see RFC 1034 3.6.2");
                }
            }
        }
        Optional.ofNullable(this.caches.get(Integer.valueOf(message.getQuestion().getDClass()))).ifPresent(new Consumer() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Cache) obj).addMessage(message);
            }
        });
        return message;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setResponseToMessageFuture, reason: merged with bridge method [inline-methods] */
    public CompletionStage<LookupResult> m1865lambda$lookupWithCache$1$orgxbillDNSlookupLookupSession(SetResponse setResponse, Record queryRecord, List<Name> aliases) {
        if (setResponse.isNXDOMAIN()) {
            return completeExceptionally(new NoSuchDomainException(queryRecord.getName(), queryRecord.getType()));
        }
        if (setResponse.isNXRRSET()) {
            return completeExceptionally(new NoSuchRRSetException(queryRecord.getName(), queryRecord.getType()));
        }
        if (setResponse.isCNAME()) {
            return CompletableFuture.completedFuture(new LookupResult(Collections.singletonList(setResponse.getCNAME()), aliases));
        }
        if (setResponse.isDNAME()) {
            return CompletableFuture.completedFuture(new LookupResult(Collections.singletonList(setResponse.getDNAME()), aliases));
        }
        if (setResponse.isSuccessful()) {
            List<Record> records = (List) setResponse.answers().stream().flatMap(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda11
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.m188xe177d73((RRset) obj);
                }
            }).collect(Collectors.toList());
            return CompletableFuture.completedFuture(new LookupResult(records, aliases));
        }
        return null;
    }

    /* renamed from: lambda$setResponseToMessageFuture$0$org-xbill-DNS-lookup-LookupSession */
    /* synthetic */ Stream m188xe177d73(RRset rrset) {
        return rrset.rrs(this.cycleResults).stream();
    }

    private <T extends Throwable, R> CompletionStage<R> completeExceptionally(T failure) {
        CompletableFuture<R> future = new CompletableFuture<>();
        future.completeExceptionally(failure);
        return future;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: resolveRedirects, reason: merged with bridge method [inline-methods] */
    public CompletionStage<LookupResult> m1863lambda$lookupUntilSuccess$0$orgxbillDNSlookupLookupSession(LookupResult response, Record query) {
        return m187x326eed3e(response, query, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: maybeFollowRedirect, reason: merged with bridge method [inline-methods] */
    public CompletionStage<LookupResult> m187x326eed3e(LookupResult response, Record query, int redirectCount) {
        if (redirectCount > this.maxRedirects) {
            throw new RedirectOverflowException(this.maxRedirects);
        }
        List<Record> records = response.getRecords();
        if (!records.isEmpty() && query.getType() != records.get(0).getType() && (records.get(0).getType() == 5 || records.get(0).getType() == 39)) {
            return maybeFollowRedirectsInAnswer(response, query, redirectCount);
        }
        return CompletableFuture.completedFuture(response);
    }

    private CompletionStage<LookupResult> maybeFollowRedirectsInAnswer(LookupResult response, Record query, int redirectCount) throws NameTooLongException {
        List<Name> aliases = new ArrayList<>(response.getAliases());
        List<Record> results = new ArrayList<>();
        Name current = query.getName();
        for (Record r : response.getRecords()) {
            if (aliases.contains(current)) {
                return completeExceptionally(new RedirectLoopException(this.maxRedirects));
            }
            if (redirectCount >= this.maxRedirects) {
                throw new RedirectOverflowException(this.maxRedirects);
            }
            if (r.getDClass() == query.getDClass()) {
                if (r.getType() == 5 && current.equals(r.getName())) {
                    aliases.add(current);
                    redirectCount++;
                    current = ((CNAMERecord) r).getTarget();
                } else if (r.getType() == 39 && current.subdomain(r.getName())) {
                    aliases.add(current);
                    redirectCount++;
                    try {
                        current = current.fromDNAME((DNAMERecord) r);
                    } catch (NameTooLongException e) {
                        throw new InvalidZoneDataException("Cannot derive DNAME from " + r + " for " + current, e);
                    }
                } else if (r.getType() == query.getType() && current.equals(r.getName())) {
                    results.add(r);
                }
            }
        }
        if (!results.isEmpty()) {
            return CompletableFuture.completedFuture(new LookupResult(results, aliases));
        }
        if (aliases.contains(current)) {
            return completeExceptionally(new RedirectLoopException(this.maxRedirects));
        }
        if (redirectCount >= this.maxRedirects) {
            throw new RedirectOverflowException(this.maxRedirects);
        }
        final int finalRedirectCount = redirectCount;
        final Record redirectQuery = Record.newRecord(current, query.getType(), query.getDClass());
        return lookupWithCache(redirectQuery, aliases).thenCompose(new Function() { // from class: org.xbill.DNS.lookup.LookupSession$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m187x326eed3e(redirectQuery, finalRedirectCount, (LookupResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LookupResult buildResult(Message answer, List<Name> aliases, Record query) {
        int rcode = answer.getRcode();
        List<Record> answerRecords = answer.getSection(1);
        if (answerRecords.isEmpty() && rcode != 0) {
            switch (rcode) {
                case 2:
                    if (answer.getOPT() != null) {
                        List<EDNSOption> options = answer.getOPT().getOptions(15);
                        if (!options.isEmpty()) {
                            throw new ServerFailedException(query.getName(), query.getType(), (ExtendedErrorCodeOption) options.get(0));
                        }
                    }
                    throw new ServerFailedException(query.getName(), query.getType());
                case 3:
                    throw new NoSuchDomainException(query.getName(), query.getType());
                case 8:
                    throw new NoSuchRRSetException(query.getName(), query.getType());
                default:
                    throw new LookupFailedException(String.format("Unknown non-success error code %s", Rcode.string(rcode)));
            }
        }
        return new LookupResult(answerRecords, aliases);
    }
}
