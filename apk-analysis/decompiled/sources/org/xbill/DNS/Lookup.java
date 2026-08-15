package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.hosts.HostsFileParser;

/* loaded from: classes8.dex */
public final class Lookup {
    public static final int HOST_NOT_FOUND = 3;
    public static final int SUCCESSFUL = 0;
    public static final int TRY_AGAIN = 2;
    public static final int TYPE_NOT_FOUND = 4;
    public static final int UNRECOVERABLE = 1;
    private static Map<Integer, Cache> defaultCaches;
    private static HostsFileParser defaultHostsFileParser;
    private static int defaultNdots;
    private static Resolver defaultResolver;
    private List<Name> aliases;
    private Record[] answers;
    private boolean badresponse;
    private String badresponseError;
    private Cache cache;
    private int credibility;
    private boolean cycleResults;
    private final int dclass;
    private boolean done;
    private boolean doneCurrent;
    private String error;
    private boolean foundAlias;
    private HostsFileParser hostsFileParser;
    private int iterations;
    private final int maxIterations;
    private final Name name;
    private boolean nametoolong;
    private int ndots;
    private boolean networkerror;
    private boolean nxdomain;
    private boolean referral;
    private Resolver resolver;
    private int result;
    private List<Name> searchPath;
    private boolean temporaryCache;
    private boolean timedout;
    private final int type;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Lookup.class);
    private static List<Name> defaultSearchPath = Collections.emptyList();
    private static final Name[] noAliases = new Name[0];

    static {
        refreshDefault();
    }

    public HostsFileParser getHostsFileParser() {
        return this.hostsFileParser;
    }

    public void setHostsFileParser(HostsFileParser hostsFileParser) {
        this.hostsFileParser = hostsFileParser;
    }

    public static synchronized void refreshDefault() {
        defaultResolver = new ExtendedResolver();
        defaultSearchPath = ResolverConfig.getCurrentConfig().searchPath();
        defaultCaches = new HashMap();
        defaultNdots = ResolverConfig.getCurrentConfig().ndots();
        defaultHostsFileParser = new HostsFileParser();
    }

    public static synchronized Resolver getDefaultResolver() {
        return defaultResolver;
    }

    public static synchronized void setDefaultResolver(Resolver resolver) {
        defaultResolver = resolver;
    }

    public static synchronized Cache getDefaultCache(int dclass) {
        Cache c;
        DClass.check(dclass);
        c = defaultCaches.get(Integer.valueOf(dclass));
        if (c == null) {
            c = new Cache(dclass);
            defaultCaches.put(Integer.valueOf(dclass), c);
        }
        return c;
    }

    public static synchronized void setDefaultCache(Cache cache, int dclass) {
        DClass.check(dclass);
        defaultCaches.put(Integer.valueOf(dclass), cache);
    }

    public static synchronized List<Name> getDefaultSearchPath() {
        return defaultSearchPath;
    }

    public static synchronized void setDefaultSearchPath(List<Name> domains) {
        if (domains == null) {
            defaultSearchPath = Collections.emptyList();
        } else {
            defaultSearchPath = convertSearchPathDomainList(domains);
        }
    }

    public static synchronized void setDefaultSearchPath(Name... domains) {
        if (domains == null) {
            defaultSearchPath = Collections.emptyList();
        } else {
            setDefaultSearchPath((List<Name>) Arrays.asList(domains));
        }
    }

    public static synchronized void setDefaultSearchPath(String... domains) throws TextParseException {
        if (domains == null) {
            defaultSearchPath = Collections.emptyList();
            return;
        }
        List<Name> newDomains = new ArrayList<>(domains.length);
        for (String domain : domains) {
            newDomains.add(Name.fromString(domain, Name.root));
        }
        defaultSearchPath = newDomains;
    }

    public static synchronized HostsFileParser getDefaultHostsFileParser() {
        return defaultHostsFileParser;
    }

    public static synchronized void setDefaultHostsFileParser(HostsFileParser hostsFileParser) {
        defaultHostsFileParser = hostsFileParser;
    }

    private static List<Name> convertSearchPathDomainList(List<Name> domains) {
        try {
            return (List) domains.stream().map(new Function() { // from class: org.xbill.DNS.Lookup$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Lookup.lambda$convertSearchPathDomainList$0((Name) obj);
                }
            }).collect(Collectors.toList());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof NameTooLongException) {
                throw new IllegalArgumentException(e.getCause());
            }
            throw e;
        }
    }

    static /* synthetic */ Name lambda$convertSearchPathDomainList$0(Name n) {
        try {
            return Name.concatenate(n, Name.root);
        } catch (NameTooLongException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void setPacketLogger(PacketLogger logger) {
        NioClient.setPacketLogger(logger);
    }

    private void reset() {
        this.iterations = 0;
        this.foundAlias = false;
        this.done = false;
        this.doneCurrent = false;
        this.aliases = null;
        this.answers = null;
        this.result = -1;
        this.error = null;
        this.nxdomain = false;
        this.badresponse = false;
        this.badresponseError = null;
        this.networkerror = false;
        this.timedout = false;
        this.nametoolong = false;
        this.referral = false;
        if (this.temporaryCache) {
            this.cache.clearCache();
        }
    }

    public Lookup(Name name, int type, int dclass) {
        this.cycleResults = true;
        Type.check(type);
        DClass.check(dclass);
        if (!Type.isRR(type) && type != 255) {
            throw new IllegalArgumentException("Cannot query for meta-types other than ANY");
        }
        this.name = name;
        this.type = type;
        this.dclass = dclass;
        synchronized (Lookup.class) {
            this.resolver = getDefaultResolver();
            this.searchPath = getDefaultSearchPath();
            this.cache = getDefaultCache(dclass);
        }
        this.ndots = defaultNdots;
        this.credibility = 3;
        this.result = -1;
        this.maxIterations = Integer.parseInt(System.getProperty("dnsjava.lookup.max_iterations", "16"));
        if (Boolean.parseBoolean(System.getProperty("dnsjava.lookup.use_hosts_file", "true"))) {
            this.hostsFileParser = getDefaultHostsFileParser();
        }
    }

    public Lookup(Name name, int type) {
        this(name, type, 1);
    }

    public Lookup(Name name) {
        this(name, 1, 1);
    }

    public Lookup(String name, int type, int dclass) throws TextParseException {
        this(Name.fromString(name), type, dclass);
    }

    public Lookup(String name, int type) throws TextParseException {
        this(Name.fromString(name), type, 1);
    }

    public Lookup(String name) throws TextParseException {
        this(Name.fromString(name), 1, 1);
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public void setSearchPath(List<Name> domains) {
        if (domains == null) {
            this.searchPath = Collections.emptyList();
        } else {
            this.searchPath = convertSearchPathDomainList(domains);
        }
    }

    public void setSearchPath(Name... domains) {
        if (domains == null) {
            this.searchPath = Collections.emptyList();
        } else {
            setSearchPath(Arrays.asList(domains));
        }
    }

    public void setSearchPath(String... domains) throws TextParseException {
        if (domains == null) {
            this.searchPath = Collections.emptyList();
            return;
        }
        List<Name> newDomains = new ArrayList<>(domains.length);
        for (String domain : domains) {
            newDomains.add(Name.fromString(domain, Name.root));
        }
        this.searchPath = newDomains;
    }

    public void setCache(Cache cache) {
        if (cache == null) {
            this.cache = new Cache(this.dclass);
            this.temporaryCache = true;
        } else {
            if (cache.getDClass() != this.dclass) {
                throw new IllegalArgumentException("DClass of cache doesn't match DClass of this Lookup instance");
            }
            this.cache = cache;
            this.temporaryCache = false;
        }
    }

    public static void setDefaultNdots(int ndots) {
        if (ndots < 0) {
            throw new IllegalArgumentException("Illegal ndots value: " + ndots);
        }
        defaultNdots = ndots;
    }

    public void setNdots(int ndots) {
        if (ndots < 0) {
            throw new IllegalArgumentException("Illegal ndots value: " + ndots);
        }
        this.ndots = ndots;
    }

    public void setCredibility(int credibility) {
        this.credibility = credibility;
    }

    public void setCycleResults(boolean cycleResults) {
        this.cycleResults = cycleResults;
    }

    private void follow(Name name, Name oldname) {
        this.foundAlias = true;
        this.badresponse = false;
        this.networkerror = false;
        this.timedout = false;
        this.nxdomain = false;
        this.referral = false;
        this.iterations++;
        if (this.iterations >= this.maxIterations || name.equals(oldname)) {
            this.result = 1;
            this.error = "CNAME loop";
            this.done = true;
        } else {
            if (this.aliases == null) {
                this.aliases = new ArrayList();
            }
            this.aliases.add(oldname);
            lookup(name);
        }
    }

    private void processResponse(Name name, SetResponse response) {
        if (response.isSuccessful()) {
            List<RRset> rrsets = response.answers();
            List<Record> l = new ArrayList<>();
            for (RRset set : rrsets) {
                l.addAll(set.rrs(this.cycleResults));
            }
            this.result = 0;
            this.answers = (Record[]) l.toArray(new Record[0]);
            this.done = true;
            return;
        }
        if (response.isNXDOMAIN()) {
            this.nxdomain = true;
            this.doneCurrent = true;
            if (this.iterations > 0) {
                this.result = 3;
                this.done = true;
                return;
            }
            return;
        }
        if (response.isNXRRSET()) {
            this.result = 4;
            this.answers = null;
            this.done = true;
            return;
        }
        if (response.isCNAME()) {
            CNAMERecord cname = response.getCNAME();
            follow(cname.getTarget(), name);
            return;
        }
        if (response.isDNAME()) {
            DNAMERecord dname = response.getDNAME();
            try {
                follow(name.fromDNAME(dname), name);
                return;
            } catch (NameTooLongException e) {
                this.result = 1;
                this.error = "Invalid DNAME target";
                this.done = true;
                return;
            }
        }
        if (response.isDelegation()) {
            this.referral = true;
        }
    }

    private void lookup(Name current) {
        if (lookupFromHostsFile(current)) {
            return;
        }
        SetResponse sr = this.cache.lookupRecords(current, this.type, this.credibility);
        log.debug("Lookup for {}/{}, cache answer: {}", current, Type.string(this.type), sr);
        processResponse(current, sr);
        if (this.done || this.doneCurrent) {
            return;
        }
        Record question = Record.newRecord(current, this.type, this.dclass);
        Message query = Message.newQuery(question);
        try {
            Message response = this.resolver.send(query).normalize(query);
            int rcode = response.getHeader().getRcode();
            if (rcode != 0 && rcode != 3) {
                this.badresponse = true;
                this.badresponseError = Rcode.string(rcode);
            } else {
                if (!query.getQuestion().equals(response.getQuestion())) {
                    this.badresponse = true;
                    this.badresponseError = "response does not match query";
                    return;
                }
                SetResponse sr2 = this.cache.addMessage(response);
                if (sr2 == null) {
                    sr2 = this.cache.lookupRecords(current, this.type, this.credibility);
                }
                log.debug("Queried {}/{}, id={}: {}", current, Type.string(this.type), Integer.valueOf(response.getHeader().getID()), sr2);
                processResponse(current, sr2);
            }
        } catch (IOException e) {
            log.debug("Lookup for {}/{}, id={} failed using resolver {}", current, Type.string(query.getQuestion().getType()), Integer.valueOf(query.getHeader().getID()), this.resolver, e);
            if (e instanceof InterruptedIOException) {
                this.timedout = true;
            } else {
                this.networkerror = true;
            }
        }
    }

    private boolean lookupFromHostsFile(Name current) {
        if (this.hostsFileParser != null && (this.type == 1 || this.type == 28)) {
            try {
                Optional<InetAddress> localLookup = this.hostsFileParser.getAddressForHost(current, this.type);
                if (localLookup.isPresent()) {
                    this.result = 0;
                    this.done = true;
                    if (this.type == 1) {
                        this.answers = new ARecord[]{new ARecord(current, this.dclass, 0L, localLookup.get())};
                    } else {
                        this.answers = new AAAARecord[]{new AAAARecord(current, this.dclass, 0L, localLookup.get())};
                    }
                    return true;
                }
            } catch (IOException e) {
                log.debug("Local hosts database parsing failed, ignoring and using resolver", (Throwable) e);
            }
        }
        return false;
    }

    private void resolve(Name current, Name suffix) {
        this.doneCurrent = false;
        if (suffix == null) {
            lookup(current);
            return;
        }
        try {
            lookup(Name.concatenate(current, suffix));
        } catch (NameTooLongException e) {
            this.nametoolong = true;
        }
    }

    public Record[] run() {
        if (this.done) {
            reset();
        }
        if (this.name.isAbsolute()) {
            resolve(this.name, null);
        } else {
            boolean absoluteNameAttempted = this.name.labels() > this.ndots;
            if (absoluteNameAttempted) {
                resolve(this.name, Name.root);
                if (this.done) {
                    return this.answers;
                }
            }
            for (Name value : this.searchPath) {
                resolve(this.name, value);
                if (this.done) {
                    return this.answers;
                }
                if (this.foundAlias) {
                    break;
                }
            }
            if (!absoluteNameAttempted) {
                resolve(this.name, Name.root);
            }
        }
        if (!this.done) {
            if (this.badresponse) {
                this.result = 2;
                this.error = this.badresponseError;
                this.done = true;
            } else if (this.timedout) {
                this.result = 2;
                this.error = "timed out";
                this.done = true;
            } else if (this.networkerror) {
                this.result = 2;
                this.error = "network error";
                this.done = true;
            } else if (this.nxdomain) {
                this.result = 3;
                this.done = true;
            } else if (this.referral) {
                this.result = 1;
                this.error = "referral";
                this.done = true;
            } else if (this.nametoolong) {
                this.result = 1;
                this.error = "name too long";
                this.done = true;
            }
        }
        return this.answers;
    }

    private void checkDone() {
        if (this.done && this.result != -1) {
            return;
        }
        StringBuilder sb = new StringBuilder("Lookup of " + this.name + " ");
        if (this.dclass != 1) {
            sb.append(DClass.string(this.dclass)).append(" ");
        }
        sb.append(Type.string(this.type)).append(" isn't done");
        throw new IllegalStateException(sb.toString());
    }

    public Record[] getAnswers() {
        checkDone();
        return this.answers;
    }

    public Name[] getAliases() {
        checkDone();
        if (this.aliases == null) {
            return noAliases;
        }
        return (Name[]) this.aliases.toArray(new Name[0]);
    }

    public int getResult() {
        checkDone();
        return this.result;
    }

    public String getErrorString() {
        checkDone();
        if (this.error != null) {
            return this.error;
        }
        switch (this.result) {
            case 0:
                return "successful";
            case 1:
                return "unrecoverable error";
            case 2:
                return "try again";
            case 3:
                return "host not found";
            case 4:
                return "type not found";
            default:
                throw new IllegalStateException("unknown result");
        }
    }
}
