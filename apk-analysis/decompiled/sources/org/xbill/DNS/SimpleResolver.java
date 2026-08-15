package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import kotlin.UByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.p004io.DefaultIoClientFactory;
import org.xbill.DNS.p004io.IoClientFactory;

/* loaded from: classes8.dex */
public class SimpleResolver implements Resolver {
    public static final int DEFAULT_EDNS_PAYLOADSIZE = 1280;
    public static final int DEFAULT_PORT = 53;
    private static final short DEFAULT_UDPSIZE = 512;
    private InetSocketAddress address;
    private boolean ignoreTruncation;
    private IoClientFactory ioClientFactory;
    private InetSocketAddress localAddress;
    private OPTRecord queryOPT;
    private Duration timeoutValue;
    private TSIG tsig;
    private boolean useTCP;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) SimpleResolver.class);
    private static InetSocketAddress defaultResolver = new InetSocketAddress(InetAddress.getLoopbackAddress(), 53);

    public IoClientFactory getIoClientFactory() {
        return this.ioClientFactory;
    }

    public void setIoClientFactory(IoClientFactory ioClientFactory) {
        this.ioClientFactory = ioClientFactory;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public SimpleResolver() throws UnknownHostException {
        this((String) null);
    }

    public SimpleResolver(String hostname) throws UnknownHostException {
        InetAddress addr;
        this.queryOPT = new OPTRecord(DEFAULT_EDNS_PAYLOADSIZE, 0, 0, 0);
        this.timeoutValue = Duration.ofSeconds(10L);
        this.ioClientFactory = new DefaultIoClientFactory();
        if (hostname == null) {
            this.address = ResolverConfig.getCurrentConfig().server();
            if (this.address == null) {
                this.address = defaultResolver;
                return;
            }
            return;
        }
        if ("0".equals(hostname)) {
            addr = InetAddress.getLoopbackAddress();
        } else {
            addr = InetAddress.getByName(hostname);
        }
        this.address = new InetSocketAddress(addr, 53);
    }

    public SimpleResolver(InetSocketAddress host) {
        this.queryOPT = new OPTRecord(DEFAULT_EDNS_PAYLOADSIZE, 0, 0, 0);
        this.timeoutValue = Duration.ofSeconds(10L);
        this.ioClientFactory = new DefaultIoClientFactory();
        this.address = (InetSocketAddress) Objects.requireNonNull(host, "host must not be null");
    }

    public SimpleResolver(InetAddress host) {
        this.queryOPT = new OPTRecord(DEFAULT_EDNS_PAYLOADSIZE, 0, 0, 0);
        this.timeoutValue = Duration.ofSeconds(10L);
        this.ioClientFactory = new DefaultIoClientFactory();
        Objects.requireNonNull(host, "host must not be null");
        this.address = new InetSocketAddress(host, 53);
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public static void setDefaultResolver(InetSocketAddress hostname) {
        defaultResolver = hostname;
    }

    public static void setDefaultResolver(String hostname) {
        defaultResolver = new InetSocketAddress(hostname, 53);
    }

    public int getPort() {
        return this.address.getPort();
    }

    @Override // org.xbill.DNS.Resolver
    public void setPort(int port) {
        this.address = new InetSocketAddress(this.address.getAddress(), port);
    }

    public void setAddress(InetSocketAddress addr) {
        this.address = addr;
    }

    public void setAddress(InetAddress addr) {
        this.address = new InetSocketAddress(addr, this.address.getPort());
    }

    public void setLocalAddress(InetSocketAddress addr) {
        this.localAddress = addr;
    }

    public void setLocalAddress(InetAddress addr) {
        this.localAddress = new InetSocketAddress(addr, 0);
    }

    public boolean getTCP() {
        return this.useTCP;
    }

    @Override // org.xbill.DNS.Resolver
    public void setTCP(boolean flag) {
        this.useTCP = flag;
    }

    public boolean getIgnoreTruncation() {
        return this.ignoreTruncation;
    }

    @Override // org.xbill.DNS.Resolver
    public void setIgnoreTruncation(boolean flag) {
        this.ignoreTruncation = flag;
    }

    public OPTRecord getEDNS() {
        return this.queryOPT;
    }

    public void setEDNS(OPTRecord optRecord) {
        this.queryOPT = optRecord;
    }

    @Override // org.xbill.DNS.Resolver
    public void setEDNS(int version, int payloadSize, int flags, List<EDNSOption> options) {
        switch (version) {
            case -1:
                this.queryOPT = null;
                return;
            case 0:
                if (payloadSize == 0) {
                    payloadSize = DEFAULT_EDNS_PAYLOADSIZE;
                }
                this.queryOPT = new OPTRecord(payloadSize, 0, version, flags, options);
                return;
            default:
                throw new IllegalArgumentException("invalid EDNS version - must be 0 or -1 to disable");
        }
    }

    public TSIG getTSIGKey() {
        return this.tsig;
    }

    @Override // org.xbill.DNS.Resolver
    public void setTSIGKey(TSIG key) {
        this.tsig = key;
    }

    @Override // org.xbill.DNS.Resolver
    public void setTimeout(Duration timeout) {
        this.timeoutValue = timeout;
    }

    @Override // org.xbill.DNS.Resolver
    public Duration getTimeout() {
        return this.timeoutValue;
    }

    private Message parseMessage(byte[] b) throws WireParseException {
        try {
            return new Message(b);
        } catch (IOException e) {
            if (!(e instanceof WireParseException)) {
                throw new WireParseException("Error parsing message", e);
            }
            throw ((WireParseException) e);
        }
    }

    private void verifyTSIG(Message query, Message response, byte[] b) {
        if (this.tsig == null) {
            return;
        }
        int error = this.tsig.verify(response, b, query.getGeneratedTSIG());
        log.debug("TSIG verify on message id {}: {}", Integer.valueOf(query.getHeader().getID()), Rcode.TSIGstring(error));
    }

    private void applyEDNS(Message query) {
        if (this.queryOPT == null || query.getOPT() != null) {
            return;
        }
        query.addRecord(this.queryOPT, 3);
    }

    private int maxUDPSize(Message query) {
        OPTRecord opt = query.getOPT();
        if (opt == null) {
            return 512;
        }
        return opt.getPayloadSize();
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(Message query) {
        return sendAsync(query, ForkJoinPool.commonPool());
    }

    @Override // org.xbill.DNS.Resolver
    public CompletionStage<Message> sendAsync(final Message query, Executor executor) throws CloneNotSupportedException {
        Record question;
        if (query.getHeader().getOpcode() == 0 && (question = query.getQuestion()) != null && question.getType() == 252) {
            final CompletableFuture<Message> f = new CompletableFuture<>();
            CompletableFuture.runAsync(new Runnable() { // from class: org.xbill.DNS.SimpleResolver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m1841lambda$sendAsync$0$orgxbillDNSSimpleResolver(f, query);
                }
            }, executor);
            return f;
        }
        Message ednsTsigQuery = query.clone();
        applyEDNS(ednsTsigQuery);
        if (this.tsig != null) {
            ednsTsigQuery.setTSIG(this.tsig, 0, null);
        }
        return sendAsync(ednsTsigQuery, this.useTCP, executor);
    }

    /* renamed from: lambda$sendAsync$0$org-xbill-DNS-SimpleResolver, reason: not valid java name */
    /* synthetic */ void m1841lambda$sendAsync$0$orgxbillDNSSimpleResolver(CompletableFuture f, Message query) {
        try {
            f.complete(sendAXFR(query));
        } catch (IOException e) {
            f.completeExceptionally(e);
        }
    }

    CompletableFuture<Message> sendAsync(final Message query, boolean forceTcp, final Executor executor) {
        CompletableFuture<byte[]> result;
        final int qid = query.getHeader().getID();
        boolean z = true;
        boolean truncate = query.getHeader().getOpcode() != 5;
        try {
            byte[] out = query.toWire(65535, truncate);
            int udpSize = maxUDPSize(query);
            if (!forceTcp && out.length <= udpSize) {
                z = false;
            }
            final boolean tcp = z;
            if (log.isTraceEnabled()) {
                log.trace("Sending {}/{}, id={} to {}/{}:{}, query:\n{}", query.getQuestion().getName(), Type.string(query.getQuestion().getType()), Integer.valueOf(qid), tcp ? "tcp" : "udp", this.address.getAddress().getHostAddress(), Integer.valueOf(this.address.getPort()), query);
            } else if (log.isDebugEnabled()) {
                log.debug("Sending {}/{}, id={} to {}/{}:{}", query.getQuestion().getName(), Type.string(query.getQuestion().getType()), Integer.valueOf(qid), tcp ? "tcp" : "udp", this.address.getAddress().getHostAddress(), Integer.valueOf(this.address.getPort()));
            }
            if (tcp) {
                result = this.ioClientFactory.createOrGetTcpClient().sendAndReceiveTcp(this.localAddress, this.address, query, out, this.timeoutValue);
            } else {
                result = this.ioClientFactory.createOrGetUdpClient().sendAndReceiveUdp(this.localAddress, this.address, query, out, udpSize, this.timeoutValue);
            }
            return result.thenComposeAsync(new Function() { // from class: org.xbill.DNS.SimpleResolver$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.m1842lambda$sendAsync$1$orgxbillDNSSimpleResolver(qid, query, tcp, executor, (byte[]) obj);
                }
            }, executor);
        } catch (MessageSizeExceededException e) {
            CompletableFuture<Message> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    /* renamed from: lambda$sendAsync$1$org-xbill-DNS-SimpleResolver, reason: not valid java name */
    /* synthetic */ CompletionStage m1842lambda$sendAsync$1$orgxbillDNSSimpleResolver(int qid, Message query, boolean tcp, Executor executor, byte[] in) {
        CompletableFuture<Message> f = new CompletableFuture<>();
        if (in.length < 12) {
            f.completeExceptionally(new WireParseException("invalid DNS header - too short"));
            return f;
        }
        int id = ((in[0] & UByte.MAX_VALUE) << 8) + (in[1] & UByte.MAX_VALUE);
        if (id != qid) {
            f.completeExceptionally(new WireParseException("invalid message id: expected " + qid + "; got id " + id));
            return f;
        }
        try {
            Message response = parseMessage(in);
            if (query.getHeader().getOpcode() == 5) {
                if (response.getHeader().getOpcode() != 5) {
                    f.completeExceptionally(new WireParseException("invalid message: opcode response is not UPDATE"));
                    return f;
                }
            } else {
                if (response.getQuestion() == null) {
                    f.completeExceptionally(new WireParseException("invalid message: question section missing"));
                    return f;
                }
                if (!query.getQuestion().getName().equals(response.getQuestion().getName())) {
                    f.completeExceptionally(new WireParseException("invalid name in message: expected " + query.getQuestion().getName() + "; got " + response.getQuestion().getName()));
                    return f;
                }
                if (query.getQuestion().getDClass() != response.getQuestion().getDClass()) {
                    f.completeExceptionally(new WireParseException("invalid class in message: expected " + DClass.string(query.getQuestion().getDClass()) + "; got " + DClass.string(response.getQuestion().getDClass())));
                    return f;
                }
                if (query.getQuestion().getType() != response.getQuestion().getType()) {
                    f.completeExceptionally(new WireParseException("invalid type in message: expected " + Type.string(query.getQuestion().getType()) + "; got " + Type.string(response.getQuestion().getType())));
                    return f;
                }
            }
            verifyTSIG(query, response, in);
            if (!tcp && !this.ignoreTruncation && response.getHeader().getFlag(6)) {
                if (log.isTraceEnabled()) {
                    log.trace("Got truncated response for id {}, retrying via TCP, response:\n{}", Integer.valueOf(qid), response);
                } else {
                    log.debug("Got truncated response for id {}, retrying via TCP", Integer.valueOf(qid));
                }
                return sendAsync(query, true, executor);
            }
            response.setResolver(this);
            f.complete(response);
            return f;
        } catch (WireParseException e) {
            f.completeExceptionally(e);
            return f;
        }
    }

    private Message sendAXFR(Message query) throws IOException, IllegalArgumentException {
        Name qname = query.getQuestion().getName();
        ZoneTransferIn xfrin = ZoneTransferIn.newAXFR(qname, this.address, this.tsig);
        xfrin.setTimeout(this.timeoutValue);
        xfrin.setLocalAddress(this.localAddress);
        try {
            xfrin.run();
            List<Record> records = xfrin.getAXFR();
            Message response = new Message(query.getHeader().getID());
            response.getHeader().setFlag(5);
            response.getHeader().setFlag(0);
            response.addRecord(query.getQuestion(), 0);
            for (Record r : records) {
                response.addRecord(r, 1);
            }
            return response;
        } catch (ZoneTransferException e) {
            throw new WireParseException(e.getMessage());
        }
    }

    public String toString() {
        return "SimpleResolver [" + this.address + "]";
    }
}
