package org.xbill.DNS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ExtendedResolver;

/* loaded from: classes8.dex */
public class ExtendedResolver implements Resolver {
    private final AtomicInteger lbStart;
    private boolean loadBalance;
    private final List<ResolverEntry> resolvers;
    private int retries;
    private Duration timeout;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ExtendedResolver.class);
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_RESOLVER_TIMEOUT = Duration.ofSeconds(5);

    /* JADX INFO: Access modifiers changed from: private */
    static class Resolution {
        private final int[] attempts;
        private int currentResolver;
        private final long endTime;
        private final Message query;
        private List<ResolverEntry> resolvers;
        private final int retriesPerResolver;

        Resolution(ExtendedResolver eres, Message query) {
            this.resolvers = new ArrayList(eres.resolvers);
            this.endTime = System.nanoTime() + eres.timeout.toNanos();
            if (eres.loadBalance) {
                int start = eres.lbStart.updateAndGet(new IntUnaryOperator() { // from class: org.xbill.DNS.ExtendedResolver$Resolution$$ExternalSyntheticLambda0
                    @Override // java.util.function.IntUnaryOperator
                    public final int applyAsInt(int i) {
                        return this.f$0.m1830lambda$new$0$orgxbillDNSExtendedResolver$Resolution(i);
                    }
                });
                if (start > 0) {
                    List<ResolverEntry> shuffle = new ArrayList<>(this.resolvers.size());
                    for (int i = 0; i < this.resolvers.size(); i++) {
                        int pos = (i + start) % this.resolvers.size();
                        shuffle.add(this.resolvers.get(pos));
                    }
                    this.resolvers = shuffle;
                }
            } else {
                this.resolvers = (List) this.resolvers.stream().sorted(Comparator.comparingInt(new ToIntFunction() { // from class: org.xbill.DNS.ExtendedResolver$Resolution$$ExternalSyntheticLambda1
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        return ((ExtendedResolver.ResolverEntry) obj).failures.get();
                    }
                })).collect(Collectors.toList());
            }
            this.attempts = new int[this.resolvers.size()];
            this.retriesPerResolver = eres.retries;
            this.query = query;
        }

        /* renamed from: lambda$new$0$org-xbill-DNS-ExtendedResolver$Resolution, reason: not valid java name */
        /* synthetic */ int m1830lambda$new$0$orgxbillDNSExtendedResolver$Resolution(int i) {
            return (i + 1) % this.resolvers.size();
        }

        private CompletionStage<Message> send(Executor executorService) {
            ResolverEntry r = this.resolvers.get(this.currentResolver);
            ExtendedResolver.log.debug("Sending {}/{}, id={} to resolver {} ({}), attempt {} of {}", this.query.getQuestion().getName(), Type.string(this.query.getQuestion().getType()), Integer.valueOf(this.query.getHeader().getID()), Integer.valueOf(this.currentResolver), r.resolver, Integer.valueOf(this.attempts[this.currentResolver] + 1), Integer.valueOf(this.retriesPerResolver));
            int[] iArr = this.attempts;
            int i = this.currentResolver;
            iArr[i] = iArr[i] + 1;
            return r.resolver.sendAsync(this.query, executorService);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CompletionStage<Message> startAsync(final Executor executorService) {
            return send(executorService).handle(new BiFunction() { // from class: org.xbill.DNS.ExtendedResolver$Resolution$$ExternalSyntheticLambda2
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return this.f$0.m1831lambda$startAsync$0$orgxbillDNSExtendedResolver$Resolution(executorService, (Message) obj, (Throwable) obj2);
                }
            }).thenCompose(Function.identity());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: handle, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public CompletionStage<Message> m1831lambda$startAsync$0$orgxbillDNSExtendedResolver$Resolution(Message result, Throwable ex, final Executor executorService) {
            AtomicInteger failureCounter = this.resolvers.get(this.currentResolver).failures;
            if (ex != null) {
                ExtendedResolver.log.debug("Failed to resolve {}/{}, id={} with resolver {} ({}) on attempt {} of {}, reason={}", this.query.getQuestion().getName(), Type.string(this.query.getQuestion().getType()), Integer.valueOf(this.query.getHeader().getID()), Integer.valueOf(this.currentResolver), this.resolvers.get(this.currentResolver).resolver, Integer.valueOf(this.attempts[this.currentResolver]), Integer.valueOf(this.retriesPerResolver), ex.getMessage());
                failureCounter.incrementAndGet();
                if (this.endTime - System.nanoTime() < 0) {
                    CompletableFuture<Message> f = new CompletableFuture<>();
                    f.completeExceptionally(new IOException("Timed out while trying to resolve " + this.query.getQuestion().getName() + "/" + Type.string(this.query.getQuestion().type) + ", id=" + this.query.getHeader().getID()));
                    return f;
                }
                this.currentResolver = (this.currentResolver + 1) % this.resolvers.size();
                if (this.attempts[this.currentResolver] < this.retriesPerResolver) {
                    return send(executorService).handle(new BiFunction() { // from class: org.xbill.DNS.ExtendedResolver$Resolution$$ExternalSyntheticLambda3
                        @Override // java.util.function.BiFunction
                        public final Object apply(Object obj, Object obj2) {
                            return this.f$0.m1829lambda$handle$0$orgxbillDNSExtendedResolver$Resolution(executorService, (Message) obj, (Throwable) obj2);
                        }
                    }).thenCompose(Function.identity());
                }
                CompletableFuture<Message> f2 = new CompletableFuture<>();
                f2.completeExceptionally(ex);
                return f2;
            }
            failureCounter.updateAndGet(new IntUnaryOperator() { // from class: org.xbill.DNS.ExtendedResolver$Resolution$$ExternalSyntheticLambda4
                @Override // java.util.function.IntUnaryOperator
                public final int applyAsInt(int i) {
                    return ExtendedResolver.Resolution.lambda$handle$1(i);
                }
            });
            return CompletableFuture.completedFuture(result);
        }

        static /* synthetic */ int lambda$handle$1(int i) {
            if (i > 0) {
                return (int) Math.log(i);
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ResolverEntry {
        private final AtomicInteger failures;
        private final Resolver resolver;

        public ResolverEntry(Resolver resolver, AtomicInteger failures) {
            this.resolver = resolver;
            this.failures = failures;
        }

        ResolverEntry(Resolver r) {
            this(r, new AtomicInteger(0));
        }

        public String toString() {
            return this.resolver.toString();
        }
    }

    public ExtendedResolver() {
        this.resolvers = new CopyOnWriteArrayList();
        this.lbStart = new AtomicInteger();
        this.retries = 3;
        this.timeout = DEFAULT_TIMEOUT;
        List<InetSocketAddress> servers = ResolverConfig.getCurrentConfig().servers();
        this.resolvers.addAll((Collection) servers.stream().map(new Function() { // from class: org.xbill.DNS.ExtendedResolver$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ExtendedResolver.lambda$new$0((InetSocketAddress) obj);
            }
        }).collect(Collectors.toList()));
    }

    static /* synthetic */ ResolverEntry lambda$new$0(InetSocketAddress server) {
        Resolver r = new SimpleResolver(server);
        r.setTimeout(DEFAULT_RESOLVER_TIMEOUT);
        return new ResolverEntry(r);
    }

    public ExtendedResolver(String[] servers) throws UnknownHostException {
        this.resolvers = new CopyOnWriteArrayList();
        this.lbStart = new AtomicInteger();
        this.retries = 3;
        this.timeout = DEFAULT_TIMEOUT;
        for (String server : servers) {
            Resolver r = new SimpleResolver(server);
            r.setTimeout(DEFAULT_RESOLVER_TIMEOUT);
            this.resolvers.add(new ResolverEntry(r));
        }
    }

    public ExtendedResolver(Resolver[] resolvers) {
        this(Arrays.asList(resolvers));
    }

    public ExtendedResolver(Iterable<Resolver> resolvers) {
        this.resolvers = new CopyOnWriteArrayList();
        this.lbStart = new AtomicInteger();
        this.retries = 3;
        this.timeout = DEFAULT_TIMEOUT;
        for (Resolver r : resolvers) {
            this.resolvers.add(new ResolverEntry(r));
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setPort(int port) {
        for (ResolverEntry re : this.resolvers) {
            re.resolver.setPort(port);
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setTCP(boolean flag) {
        for (ResolverEntry re : this.resolvers) {
            re.resolver.setTCP(flag);
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setIgnoreTruncation(boolean flag) {
        for (ResolverEntry re : this.resolvers) {
            re.resolver.setIgnoreTruncation(flag);
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setEDNS(int version, int payloadSize, int flags, List<EDNSOption> options) {
        for (ResolverEntry re : this.resolvers) {
            re.resolver.setEDNS(version, payloadSize, flags, options);
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setTSIGKey(TSIG key) {
        for (ResolverEntry re : this.resolvers) {
            re.resolver.setTSIGKey(key);
        }
    }

    @Override // org.xbill.DNS.Resolver
    public Duration getTimeout() {
        return this.timeout;
    }

    @Override // org.xbill.DNS.Resolver
    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(Message query) {
        return sendAsync(query, ForkJoinPool.commonPool());
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(Message query, Executor executor) {
        Resolution res = new Resolution(this, query);
        return res.startAsync(executor);
    }

    public Resolver getResolver(int n) {
        if (n < this.resolvers.size()) {
            return this.resolvers.get(n).resolver;
        }
        return null;
    }

    static /* synthetic */ Resolver[] lambda$getResolvers$1(int x$0) {
        return new Resolver[x$0];
    }

    public Resolver[] getResolvers() {
        return (Resolver[]) this.resolvers.stream().map(new Function() { // from class: org.xbill.DNS.ExtendedResolver$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ExtendedResolver.ResolverEntry) obj).resolver;
            }
        }).toArray(new IntFunction() { // from class: org.xbill.DNS.ExtendedResolver$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return ExtendedResolver.lambda$getResolvers$1(i);
            }
        });
    }

    public void addResolver(Resolver r) {
        this.resolvers.add(new ResolverEntry(r));
    }

    static /* synthetic */ boolean lambda$deleteResolver$0(Resolver r, ResolverEntry re) {
        return re.resolver == r;
    }

    public void deleteResolver(final Resolver r) {
        this.resolvers.removeIf(new Predicate() { // from class: org.xbill.DNS.ExtendedResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ExtendedResolver.lambda$deleteResolver$0(r, (ExtendedResolver.ResolverEntry) obj);
            }
        });
    }

    public boolean getLoadBalance() {
        return this.loadBalance;
    }

    public void setLoadBalance(boolean flag) {
        this.loadBalance = flag;
    }

    public int getRetries() {
        return this.retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String toString() {
        return "ExtendedResolver of " + this.resolvers;
    }
}
