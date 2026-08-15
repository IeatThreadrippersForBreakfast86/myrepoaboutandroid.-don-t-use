package org.xbill.DNS;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.AsyncSemaphore;

/* loaded from: classes8.dex */
final class AsyncSemaphore {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) AsyncSemaphore.class);
    private final String name;
    private volatile int permits;
    private final Queue<CompletableFuture<Permit>> queue = new ArrayDeque();
    private final Permit singletonPermit = new Permit();

    static /* synthetic */ int access$108(AsyncSemaphore x0) {
        int i = x0.permits;
        x0.permits = i + 1;
        return i;
    }

    final class Permit {
        Permit() {
        }

        public void release(int id, Executor executor) {
            synchronized (AsyncSemaphore.this.queue) {
                final CompletableFuture<Permit> next = (CompletableFuture) AsyncSemaphore.this.queue.poll();
                if (next != null) {
                    AsyncSemaphore.log.trace("{} permit released id={}, available={}, immediate next", AsyncSemaphore.this.name, Integer.valueOf(id), Integer.valueOf(AsyncSemaphore.this.permits));
                    executor.execute(new Runnable() { // from class: org.xbill.DNS.AsyncSemaphore$Permit$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m1827lambda$release$0$orgxbillDNSAsyncSemaphore$Permit(next);
                        }
                    });
                } else {
                    AsyncSemaphore.access$108(AsyncSemaphore.this);
                    AsyncSemaphore.log.trace("{} permit released id={}, available={}", AsyncSemaphore.this.name, Integer.valueOf(id), Integer.valueOf(AsyncSemaphore.this.permits));
                }
            }
        }

        /* renamed from: lambda$release$0$org-xbill-DNS-AsyncSemaphore$Permit, reason: not valid java name */
        /* synthetic */ void m1827lambda$release$0$orgxbillDNSAsyncSemaphore$Permit(CompletableFuture next) {
            next.complete(this);
        }
    }

    AsyncSemaphore(int permits, String name) {
        this.permits = permits;
        this.name = name;
        log.debug("Using Java 8 implementation for {}", name);
    }

    CompletionStage<Permit> acquire(Duration timeout, final int id, Executor executor) {
        synchronized (this.queue) {
            if (this.permits > 0) {
                this.permits--;
                log.trace("{} permit acquired id={}, available={}", this.name, Integer.valueOf(id), Integer.valueOf(this.permits));
                return CompletableFuture.completedFuture(this.singletonPermit);
            }
            final TimeoutCompletableFuture<Permit> f = new TimeoutCompletableFuture<>();
            f.compatTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS).whenCompleteAsync(new BiConsumer() { // from class: org.xbill.DNS.AsyncSemaphore$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    this.f$0.m1826lambda$acquire$0$orgxbillDNSAsyncSemaphore(id, f, (AsyncSemaphore.Permit) obj, (Throwable) obj2);
                }
            }, executor);
            log.trace("{} permit queued id={}, available={}", this.name, Integer.valueOf(id), Integer.valueOf(this.permits));
            this.queue.add(f);
            return f;
        }
    }

    /* renamed from: lambda$acquire$0$org-xbill-DNS-AsyncSemaphore, reason: not valid java name */
    /* synthetic */ void m1826lambda$acquire$0$orgxbillDNSAsyncSemaphore(int id, TimeoutCompletableFuture f, Permit result, Throwable ex) {
        synchronized (this.queue) {
            if (ex != null) {
                log.trace("{} permit timed out id={}, available={}", this.name, Integer.valueOf(id), Integer.valueOf(this.permits));
                this.queue.remove(f);
            } else {
                this.queue.remove(f);
            }
        }
    }
}
