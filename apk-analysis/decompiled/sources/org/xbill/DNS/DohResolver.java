package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.AsyncSemaphore;

/* loaded from: classes8.dex */
public final class DohResolver extends DohResolverCommon {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DohResolver.class);
    private final SSLSocketFactory sslSocketFactory;

    @Override // org.xbill.DNS.DohResolverCommon
    @Deprecated
    public /* bridge */ /* synthetic */ Executor getExecutor() {
        return super.getExecutor();
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ Duration getTimeout() {
        return super.getTimeout();
    }

    @Override // org.xbill.DNS.DohResolverCommon
    public /* bridge */ /* synthetic */ String getUriTemplate() {
        return super.getUriTemplate();
    }

    @Override // org.xbill.DNS.DohResolverCommon
    public /* bridge */ /* synthetic */ boolean isUsePost() {
        return super.isUsePost();
    }

    @Override // org.xbill.DNS.DohResolverCommon
    @Deprecated
    public /* bridge */ /* synthetic */ void setExecutor(Executor executor) {
        super.setExecutor(executor);
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ void setIgnoreTruncation(boolean z) {
        super.setIgnoreTruncation(z);
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ void setPort(int i) {
        super.setPort(i);
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ void setTCP(boolean z) {
        super.setTCP(z);
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ void setTSIGKey(TSIG tsig) {
        super.setTSIGKey(tsig);
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public /* bridge */ /* synthetic */ void setTimeout(Duration duration) {
        super.setTimeout(duration);
    }

    @Override // org.xbill.DNS.DohResolverCommon
    public /* bridge */ /* synthetic */ void setUriTemplate(String str) {
        super.setUriTemplate(str);
    }

    @Override // org.xbill.DNS.DohResolverCommon
    public /* bridge */ /* synthetic */ void setUsePost(boolean z) {
        super.setUsePost(z);
    }

    @Override // org.xbill.DNS.DohResolverCommon
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public DohResolver(String uriTemplate) {
        this(uriTemplate, 100, Duration.ZERO);
    }

    public DohResolver(String uriTemplate, int maxConcurrentRequests, Duration idleConnectionTimeout) {
        super(uriTemplate, maxConcurrentRequests);
        log.debug("Using Java 8 implementation");
        try {
            this.sslSocketFactory = SSLContext.getDefault().getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.xbill.DNS.DohResolverCommon, org.xbill.DNS.Resolver
    public void setEDNS(int version, int payloadSize, int flags, List<EDNSOption> options) {
        super.setEDNS(version, payloadSize, flags, options);
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(Message query) {
        return sendAsync(query, this.defaultExecutor);
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(final Message query, final Executor executor) {
        final byte[] queryBytes = prepareQuery(query).toWire();
        final String url = getUrl(queryBytes);
        final long startTime = getNanoTime();
        final int queryId = query.getHeader().getID();
        CompletableFuture<Message> f = this.maxConcurrentRequests.acquire(this.timeout, queryId, executor).handleAsync(new BiFunction() { // from class: org.xbill.DNS.DohResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return this.f$0.m1828lambda$sendAsync$0$orgxbillDNSDohResolver(query, url, queryBytes, startTime, queryId, executor, (AsyncSemaphore.Permit) obj, (Throwable) obj2);
            }
        }, executor).thenCompose(Function.identity()).toCompletableFuture();
        final Duration remainingTimeout = this.timeout.minus(getNanoTime() - startTime, ChronoUnit.NANOS);
        return TimeoutCompletableFuture.compatTimeout(f, remainingTimeout.toMillis(), TimeUnit.MILLISECONDS).exceptionally(new Function() { // from class: org.xbill.DNS.DohResolver$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return DohResolver.lambda$sendAsync$1(queryId, query, remainingTimeout, (Throwable) obj);
            }
        });
    }

    /* renamed from: lambda$sendAsync$0$org-xbill-DNS-DohResolver, reason: not valid java name */
    /* synthetic */ CompletableFuture m1828lambda$sendAsync$0$orgxbillDNSDohResolver(Message query, String url, byte[] queryBytes, long startTime, int queryId, Executor executor, AsyncSemaphore.Permit permit, Throwable ex) {
        Message response;
        try {
            if (ex != null) {
                return timeoutFailedFuture(query, "could not acquire lock to send request", ex);
            }
            try {
                SendAndGetMessageBytesResponse result = sendAndGetMessageBytes(url, queryBytes, startTime);
                if (result.f243rc == 0) {
                    response = new Message(result.responseBytes);
                    verifyTSIG(query, response, result.responseBytes, this.tsig);
                } else {
                    response = new Message(0);
                    response.getHeader().setRcode(result.f243rc);
                }
                response.setResolver(this);
                CompletableFuture completableFutureCompletedFuture = CompletableFuture.completedFuture(response);
                permit.release(queryId, executor);
                return completableFutureCompletedFuture;
            } catch (SocketTimeoutException e) {
                CompletableFuture completableFutureTimeoutFailedFuture = timeoutFailedFuture(query, e);
                permit.release(queryId, executor);
                return completableFutureTimeoutFailedFuture;
            } catch (IOException e2) {
                e = e2;
                CompletableFuture completableFutureFailedFuture = failedFuture(e);
                permit.release(queryId, executor);
                return completableFutureFailedFuture;
            } catch (URISyntaxException e3) {
                e = e3;
                CompletableFuture completableFutureFailedFuture2 = failedFuture(e);
                permit.release(queryId, executor);
                return completableFutureFailedFuture2;
            }
        } catch (Throwable th) {
            permit.release(queryId, executor);
            throw th;
        }
    }

    static /* synthetic */ Message lambda$sendAsync$1(int queryId, Message query, Duration remainingTimeout, Throwable ex) {
        if (ex instanceof TimeoutException) {
            throw new CompletionException(new TimeoutException("Query " + queryId + " for " + query.getQuestion().getName() + "/" + Type.string(query.getQuestion().getType()) + " timed out in remaining " + remainingTimeout.toMillis() + "ms"));
        }
        if (ex instanceof CompletionException) {
            throw ((CompletionException) ex);
        }
        throw new CompletionException(ex);
    }

    private static final class SendAndGetMessageBytesResponse {

        /* renamed from: rc */
        private final int f243rc;
        private final byte[] responseBytes;

        public SendAndGetMessageBytesResponse(int rc, byte[] responseBytes) {
            this.f243rc = rc;
            this.responseBytes = responseBytes;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof SendAndGetMessageBytesResponse)) {
                return false;
            }
            SendAndGetMessageBytesResponse other = (SendAndGetMessageBytesResponse) o;
            return getRc() == other.getRc() && Arrays.equals(getResponseBytes(), other.getResponseBytes());
        }

        public int hashCode() {
            int result = (1 * 59) + getRc();
            return (result * 59) + Arrays.hashCode(getResponseBytes());
        }

        public String toString() {
            return "DohResolver.SendAndGetMessageBytesResponse(rc=" + getRc() + ", responseBytes=" + Arrays.toString(getResponseBytes()) + ")";
        }

        public int getRc() {
            return this.f243rc;
        }

        public byte[] getResponseBytes() {
            return this.responseBytes;
        }
    }

    private SendAndGetMessageBytesResponse sendAndGetMessageBytes(String url, byte[] queryBytes, long startTime) throws URISyntaxException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(this.sslSocketFactory);
        }
        conn.setRequestMethod(this.usePost ? "POST" : "GET");
        conn.setRequestProperty("Content-Type", "application/dns-message");
        conn.setRequestProperty("Accept", "application/dns-message");
        Duration remainingTimeout = this.timeout.minus(getNanoTime() - startTime, ChronoUnit.NANOS);
        if (remainingTimeout.toMillis() <= 0) {
            throw new SocketTimeoutException("No time left to connect");
        }
        conn.setConnectTimeout((int) remainingTimeout.toMillis());
        if (this.usePost) {
            conn.setDoOutput(true);
        }
        conn.connect();
        Duration remainingTimeout2 = this.timeout.minus(getNanoTime() - startTime, ChronoUnit.NANOS);
        if (remainingTimeout2.toMillis() <= 0) {
            throw new SocketTimeoutException("No time left to request data");
        }
        conn.setReadTimeout((int) remainingTimeout2.toMillis());
        if (this.usePost) {
            conn.getOutputStream().write(queryBytes);
        }
        int rc = conn.getResponseCode();
        if (rc < 200 || rc >= 300) {
            discardStream(conn.getInputStream());
            discardStream(conn.getErrorStream());
            return new SendAndGetMessageBytesResponse(2, null);
        }
        try {
            InputStream is = conn.getInputStream();
            try {
                int length = conn.getContentLength();
                if (length > -1) {
                    byte[] responseBytes = new byte[conn.getContentLength()];
                    int offset = 0;
                    while (true) {
                        int r = is.read(responseBytes, offset, responseBytes.length - offset);
                        if (r > 0) {
                            offset += r;
                            Duration remainingTimeout3 = this.timeout.minus(getNanoTime() - startTime, ChronoUnit.NANOS);
                            if (offset != responseBytes.length && (remainingTimeout3.isNegative() || remainingTimeout3.isZero())) {
                                break;
                            }
                        } else {
                            if (offset < responseBytes.length) {
                                throw new EOFException("Could not read expected content length");
                            }
                            SendAndGetMessageBytesResponse sendAndGetMessageBytesResponse = new SendAndGetMessageBytesResponse(0, responseBytes);
                            if (is != null) {
                                is.close();
                            }
                            return sendAndGetMessageBytesResponse;
                        }
                    }
                    throw new SocketTimeoutException("Timed out waiting for response data, got " + offset + " of " + responseBytes.length + " expected bytes");
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int r2 = is.read(buffer, 0, buffer.length);
                        if (r2 > 0) {
                            Duration remainingTimeout4 = this.timeout.minus(getNanoTime() - startTime, ChronoUnit.NANOS);
                            if (remainingTimeout4.isNegative() || remainingTimeout4.isZero()) {
                                break;
                            }
                            bos.write(buffer, 0, r2);
                        } else {
                            SendAndGetMessageBytesResponse sendAndGetMessageBytesResponse2 = new SendAndGetMessageBytesResponse(0, bos.toByteArray());
                            bos.close();
                            if (is != null) {
                                is.close();
                            }
                            return sendAndGetMessageBytesResponse2;
                        }
                    }
                    throw new SocketTimeoutException("Timed out waiting for response data, got " + bos.size() + " bytes so far");
                } finally {
                }
            } finally {
            }
        } catch (IOException ioe) {
            discardStream(conn.getErrorStream());
            throw ioe;
        }
    }

    private void discardStream(InputStream es) throws IOException {
        if (es != null) {
            try {
                try {
                    byte[] buf = new byte[4096];
                    do {
                    } while (es.read(buf) > 0);
                    if (es != null) {
                        es.close();
                    }
                } finally {
                }
            } catch (IOException e) {
            }
        }
    }

    @Override // org.xbill.DNS.DohResolverCommon
    protected <T> CompletableFuture<T> failedFuture(Throwable e) {
        CompletableFuture<T> f = new CompletableFuture<>();
        f.completeExceptionally(e);
        return f;
    }
}
