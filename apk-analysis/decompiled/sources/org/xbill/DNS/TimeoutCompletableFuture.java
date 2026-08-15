package org.xbill.DNS;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.TimeoutCompletableFuture;

/* loaded from: classes8.dex */
class TimeoutCompletableFuture<T> extends CompletableFuture<T> {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) TimeoutCompletableFuture.class);

    TimeoutCompletableFuture() {
    }

    public CompletableFuture<T> compatTimeout(long timeout, TimeUnit unit) {
        return compatTimeout(this, timeout, unit);
    }

    public static <T> CompletableFuture<T> compatTimeout(final CompletableFuture<T> f, final long timeout, final TimeUnit unit) {
        if (timeout <= 0) {
            f.completeExceptionally(new TimeoutException("timeout is " + timeout + ", but must be > 0"));
        }
        final ScheduledFuture<?> sf = TimeoutScheduler.executor.schedule(new Runnable() { // from class: org.xbill.DNS.TimeoutCompletableFuture$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimeoutCompletableFuture.lambda$compatTimeout$0(f, unit, timeout);
            }
        }, timeout, unit);
        f.whenComplete((BiConsumer) new BiConsumer() { // from class: org.xbill.DNS.TimeoutCompletableFuture$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TimeoutCompletableFuture.lambda$compatTimeout$1(sf, obj, (Throwable) obj2);
            }
        });
        return f;
    }

    static /* synthetic */ void lambda$compatTimeout$0(CompletableFuture f, TimeUnit unit, long timeout) {
        if (!f.isDone()) {
            f.completeExceptionally(new TimeoutException("Timeout of " + unit.toMillis(timeout) + "ms has elapsed before the task completed"));
        }
    }

    static /* synthetic */ void lambda$compatTimeout$1(ScheduledFuture sf, Object result, Throwable ex) {
        if (ex == null && !sf.isDone()) {
            sf.cancel(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class TimeoutScheduler {
        private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: org.xbill.DNS.TimeoutCompletableFuture$TimeoutScheduler$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return TimeoutCompletableFuture.TimeoutScheduler.lambda$static$0(runnable);
            }
        });

        private TimeoutScheduler() {
        }

        static {
            executor.setRemoveOnCancelPolicy(true);
        }

        static /* synthetic */ Thread lambda$static$0(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("dnsjava AsyncSemaphoreTimeoutScheduler");
            return t;
        }
    }
}
