package org.xbill.DNS;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;

/* loaded from: classes8.dex */
public interface Resolver {
    void setEDNS(int i, int i2, int i3, List<EDNSOption> list);

    void setIgnoreTruncation(boolean z);

    void setPort(int i);

    void setTCP(boolean z);

    void setTSIGKey(TSIG tsig);

    void setTimeout(Duration duration);

    default void setEDNS(int version) {
        setEDNS(version, 0, 0, Collections.emptyList());
    }

    default void setEDNS(int version, int payloadSize, int flags, EDNSOption... options) {
        setEDNS(version, payloadSize, flags, options == null ? Collections.emptyList() : Arrays.asList(options));
    }

    @Deprecated
    default void setTimeout(int secs, int msecs) {
        setTimeout(Duration.ofMillis((secs * 1000) + msecs));
    }

    @Deprecated
    default void setTimeout(int secs) {
        setTimeout(Duration.ofSeconds(secs));
    }

    default Duration getTimeout() {
        return Duration.ofSeconds(10L);
    }

    default Message send(Message query) throws IOException {
        try {
            CompletableFuture<Message> result = sendAsync(query).toCompletableFuture();
            return result.get(getTimeout().toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } catch (ExecutionException e2) {
            if (e2.getCause() instanceof IOException) {
                throw ((IOException) e2.getCause());
            }
            if (e2.getCause() != null) {
                throw new IOException(e2.getCause());
            }
            throw new IOException(e2);
        } catch (TimeoutException e3) {
            throw new IOException("Timed out while trying to resolve " + query.getQuestion().getName() + "/" + Type.string(query.getQuestion().type) + ", id=" + query.getHeader().getID(), e3);
        }
    }

    default CompletionStage<Message> sendAsync(Message query) {
        return sendAsync(query, ForkJoinPool.commonPool());
    }

    default CompletionStage<Message> sendAsync(Message query, Executor executor) {
        final CompletableFuture<Message> f = new CompletableFuture<>();
        sendAsync(query, new ResolverListener(this) { // from class: org.xbill.DNS.Resolver.1
            final /* synthetic */ Resolver this$0;

            {
                this.this$0 = this;
            }

            @Override // org.xbill.DNS.ResolverListener
            public void receiveMessage(Object id, Message m) {
                f.complete(m);
            }

            @Override // org.xbill.DNS.ResolverListener
            public void handleException(Object id, Exception e) {
                f.completeExceptionally(e);
            }
        });
        return f;
    }

    @Deprecated
    default Object sendAsync(Message query, final ResolverListener listener) {
        final Object id = new Object();
        CompletionStage<Message> f = sendAsync(query);
        f.handleAsync(new BiFunction() { // from class: org.xbill.DNS.Resolver$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return Resolver.lambda$sendAsync$0(listener, id, (Message) obj, (Throwable) obj2);
            }
        });
        return id;
    }

    static /* synthetic */ Object lambda$sendAsync$0(ResolverListener listener, Object id, Message result, Throwable throwable) {
        Exception exception;
        if (throwable != null) {
            if ((throwable instanceof CompletionException) && throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            if (throwable instanceof Exception) {
                exception = (Exception) throwable;
            } else {
                exception = new Exception(throwable);
            }
            listener.handleException(id, exception);
            return null;
        }
        listener.receiveMessage(id, result);
        return null;
    }
}
