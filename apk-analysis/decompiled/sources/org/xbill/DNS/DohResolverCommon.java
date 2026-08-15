package org.xbill.DNS;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
abstract class DohResolverCommon implements Resolver {
    protected static final String APPLICATION_DNS_MESSAGE = "application/dns-message";
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DohResolverCommon.class);
    protected final AsyncSemaphore maxConcurrentRequests;
    protected TSIG tsig;
    protected String uriTemplate;
    protected final AtomicLong lastRequest = new AtomicLong(0);
    protected boolean usePost = false;
    protected Duration timeout = Duration.ofSeconds(5);
    protected OPTRecord queryOPT = new OPTRecord(0, 0, 0);
    protected Executor defaultExecutor = ForkJoinPool.commonPool();

    protected abstract <T> CompletableFuture<T> failedFuture(Throwable th);

    long getNanoTime() {
        return System.nanoTime();
    }

    protected DohResolverCommon(String uriTemplate, int maxConcurrentRequests) throws NumberFormatException {
        this.uriTemplate = uriTemplate;
        if (maxConcurrentRequests <= 0) {
            throw new IllegalArgumentException("maxConcurrentRequests must be > 0");
        }
        try {
            int javaMaxConn = Integer.parseInt(System.getProperty("http.maxConnections", "5"));
            if (maxConcurrentRequests > javaMaxConn) {
                maxConcurrentRequests = javaMaxConn;
            }
        } catch (NumberFormatException e) {
        }
        this.maxConcurrentRequests = new AsyncSemaphore(maxConcurrentRequests, "concurrent request limit");
    }

    @Override // org.xbill.DNS.Resolver
    public void setPort(int port) {
    }

    @Override // org.xbill.DNS.Resolver
    public void setTCP(boolean flag) {
    }

    @Override // org.xbill.DNS.Resolver
    public void setIgnoreTruncation(boolean flag) {
    }

    @Override // org.xbill.DNS.Resolver
    public void setEDNS(int version, int payloadSize, int flags, List<EDNSOption> options) {
        switch (version) {
            case -1:
                this.queryOPT = null;
                return;
            case 0:
                this.queryOPT = new OPTRecord(0, 0, version, flags, options);
                return;
            default:
                throw new IllegalArgumentException("invalid EDNS version - must be 0 or -1 to disable");
        }
    }

    @Override // org.xbill.DNS.Resolver
    public void setTSIGKey(TSIG key) {
        this.tsig = key;
    }

    @Override // org.xbill.DNS.Resolver
    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    @Override // org.xbill.DNS.Resolver
    public Duration getTimeout() {
        return this.timeout;
    }

    protected String getUrl(byte[] queryBytes) {
        String url = this.uriTemplate;
        if (!this.usePost) {
            return url + "?dns=" + base64.toString(queryBytes, true);
        }
        return url;
    }

    protected Message prepareQuery(Message query) throws NoSuchAlgorithmException, InvalidKeyException, CloneNotSupportedException {
        Message preparedQuery = query.clone();
        preparedQuery.getHeader().setID(0);
        if (this.queryOPT != null && preparedQuery.getOPT() == null) {
            preparedQuery.addRecord(this.queryOPT, 3);
        }
        if (this.tsig != null) {
            this.tsig.apply(preparedQuery, null);
        }
        return preparedQuery;
    }

    protected void verifyTSIG(Message query, Message response, byte[] b, TSIG tsig) {
        if (tsig == null) {
            return;
        }
        int error = tsig.verify(response, b, query.getGeneratedTSIG());
        log.debug("TSIG verify for query {}, {}/{}: {}", Integer.valueOf(query.getHeader().getID()), query.getQuestion().getName(), Type.string(query.getQuestion().getType()), Rcode.TSIGstring(error));
    }

    public boolean isUsePost() {
        return this.usePost;
    }

    public void setUsePost(boolean usePost) {
        this.usePost = usePost;
    }

    public String getUriTemplate() {
        return this.uriTemplate;
    }

    public void setUriTemplate(String uriTemplate) {
        this.uriTemplate = uriTemplate;
    }

    @Deprecated
    public Executor getExecutor() {
        return this.defaultExecutor;
    }

    @Deprecated
    public void setExecutor(Executor executor) {
        this.defaultExecutor = executor == null ? ForkJoinPool.commonPool() : executor;
    }

    public String toString() {
        return "DohResolver {" + (this.usePost ? "POST " : "GET ") + this.uriTemplate + "}";
    }

    protected final <T> CompletableFuture<T> timeoutFailedFuture(Message query, Throwable inner) {
        return timeoutFailedFuture(query, null, inner);
    }

    protected final <T> CompletableFuture<T> timeoutFailedFuture(Message query, String message, Throwable inner) {
        String str = "";
        StringBuilder sbAppend = new StringBuilder().append("Query ").append(query.getHeader().getID()).append(" for ").append(query.getQuestion().getName()).append("/").append(Type.string(query.getQuestion().getType())).append(" timed out").append(message != null ? ": " + message : "");
        if (inner != null && inner.getMessage() != null) {
            str = ", " + inner.getMessage();
        }
        return failedFuture(new TimeoutException(sbAppend.append(str).toString()));
    }
}
