package org.xbill.DNS.hosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Address;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

/* loaded from: classes8.dex */
public final class HostsFileParser {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) HostsFileParser.class);
    private final boolean clearCacheOnChange;
    private Clock clock;
    private final Duration fileChangeCheckInterval;
    private volatile Map<String, InetAddress> hostsCache;
    private long hostsFileSizeBytes;
    private boolean hostsFileWarningLogged;
    private boolean isEntireFileParsed;
    private Instant lastFileModificationCheckTime;
    private Instant lastFileReadTime;
    private final int maxFullCacheFileSizeBytes;
    private final Path path;

    /* JADX WARN: Illegal instructions before constructor call */
    public HostsFileParser() {
        Path path;
        if (System.getProperty("os.name").contains("Windows")) {
            path = Paths.get(System.getenv("SystemRoot"), "\\System32\\drivers\\etc\\hosts");
        } else {
            path = Paths.get("/etc/hosts", new String[0]);
        }
        this(path, true);
    }

    public HostsFileParser(Path path) {
        this(path, true);
    }

    public HostsFileParser(Path path, boolean clearCacheOnChange) {
        this.maxFullCacheFileSizeBytes = Integer.parseInt(System.getProperty("dnsjava.hostsfile.max_size_bytes", "16384"));
        this.fileChangeCheckInterval = Duration.ofMillis(Integer.parseInt(System.getProperty("dnsjava.hostsfile.change_check_interval_ms", "300000")));
        this.clock = Clock.systemUTC();
        this.lastFileModificationCheckTime = null;
        this.lastFileReadTime = null;
        this.hostsFileWarningLogged = false;
        this.path = (Path) Objects.requireNonNull(path, "path is required");
        this.clearCacheOnChange = clearCacheOnChange;
        if (Files.isDirectory(path, new LinkOption[0])) {
            throw new IllegalArgumentException("path must be a file");
        }
    }

    public Optional<InetAddress> getAddressForHost(Name name, int type) throws IOException {
        Objects.requireNonNull(name, "name is required");
        if (type != 1 && type != 28) {
            throw new IllegalArgumentException("type can only be A or AAAA");
        }
        validateCache();
        InetAddress cachedAddress = this.hostsCache.get(key(name, type));
        if (cachedAddress != null) {
            return Optional.of(cachedAddress);
        }
        if (this.isEntireFileParsed) {
            return Optional.empty();
        }
        if (this.hostsFileSizeBytes > this.maxFullCacheFileSizeBytes) {
            searchHostsFileForEntry(name, type);
        }
        return Optional.ofNullable(this.hostsCache.get(key(name, type)));
    }

    private void parseEntireHostsFile() throws IOException {
        int lineNumber = 0;
        AtomicInteger addressFailures = new AtomicInteger(0);
        AtomicInteger nameFailures = new AtomicInteger(0);
        BufferedReader hostsReader = Files.newBufferedReader(this.path, StandardCharsets.UTF_8);
        while (true) {
            try {
                String line = hostsReader.readLine();
                if (line == null) {
                    break;
                }
                lineNumber++;
                LineData lineData = parseLine(lineNumber, line, addressFailures, nameFailures);
                if (lineData != null) {
                    for (Name lineName : lineData.names) {
                        InetAddress lineAddress = InetAddress.getByAddress(lineName.toString(true), lineData.address);
                        this.hostsCache.putIfAbsent(key(lineName, lineData.type), lineAddress);
                    }
                }
            } catch (Throwable th) {
                if (hostsReader != null) {
                    try {
                        hostsReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (hostsReader != null) {
            hostsReader.close();
        }
        if (this.hostsFileWarningLogged) {
            return;
        }
        if (addressFailures.get() > 0 || nameFailures.get() > 0) {
            log.warn("Failed to parse entire hosts file {}, address failures={}, name failures={}", this.path, Integer.valueOf(addressFailures.get()), nameFailures);
            this.hostsFileWarningLogged = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0042, code lost:
    
        r6 = java.net.InetAddress.getByAddress(r8.toString(true), r4.address);
        r11.hostsCache.putIfAbsent(key(r8, r4.type), r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0057, code lost:
    
        if (r3 == null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0059, code lost:
    
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005c, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void searchHostsFileForEntry(Name name, int type) throws IOException {
        int lineNumber = 0;
        AtomicInteger addressFailures = new AtomicInteger(0);
        AtomicInteger nameFailures = new AtomicInteger(0);
        BufferedReader hostsReader = Files.newBufferedReader(this.path, StandardCharsets.UTF_8);
        loop0: while (true) {
            try {
                String line = hostsReader.readLine();
                if (line != null) {
                    lineNumber++;
                    LineData lineData = parseLine(lineNumber, line, addressFailures, nameFailures);
                    if (lineData != null) {
                        Iterator<? extends Name> it = lineData.names.iterator();
                        while (it.hasNext()) {
                            Name lineName = it.next();
                            boolean isSearchedEntry = lineName.equals(name);
                            if (isSearchedEntry && type == lineData.type) {
                                break loop0;
                            }
                        }
                    }
                } else {
                    if (hostsReader != null) {
                        hostsReader.close();
                    }
                    if (this.hostsFileWarningLogged) {
                        return;
                    }
                    if (addressFailures.get() > 0 || nameFailures.get() > 0) {
                        log.warn("Failed to find {} in hosts file {}, address failures={}, name failures={}", name, this.path, Integer.valueOf(addressFailures.get()), nameFailures);
                        this.hostsFileWarningLogged = true;
                        return;
                    }
                    return;
                }
            } catch (Throwable th) {
                if (hostsReader != null) {
                    try {
                        hostsReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    private static final class LineData {
        final byte[] address;
        final Iterable<? extends Name> names;
        final int type;

        public LineData(int type, byte[] address, Iterable<? extends Name> names) {
            this.type = type;
            this.address = address;
            this.names = names;
        }
    }

    private LineData parseLine(final int lineNumber, String line, AtomicInteger addressFailures, final AtomicInteger nameFailures) {
        String[] lineTokens = getLineTokens(line);
        if (lineTokens.length < 2) {
            return null;
        }
        int lineAddressType = 1;
        byte[] lineAddressBytes = Address.toByteArray(lineTokens[0], 1);
        if (lineAddressBytes == null) {
            lineAddressBytes = Address.toByteArray(lineTokens[0], 2);
            lineAddressType = 28;
        }
        if (lineAddressBytes == null) {
            log.debug("Could not decode address {}, {}#L{}", lineTokens[0], this.path, Integer.valueOf(lineNumber));
            addressFailures.incrementAndGet();
            return null;
        }
        final Stream streamFilter = Arrays.stream(lineTokens).skip(1L).map(new Function() { // from class: org.xbill.DNS.hosts.HostsFileParser$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.m1860lambda$parseLine$0$orgxbillDNShostsHostsFileParser(lineNumber, nameFailures, (String) obj);
            }
        }).filter(new Predicate() { // from class: org.xbill.DNS.hosts.HostsFileParser$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Name) obj);
            }
        });
        Objects.requireNonNull(streamFilter);
        Iterable<? extends Name> lineNames = new Iterable() { // from class: org.xbill.DNS.hosts.HostsFileParser$$ExternalSyntheticLambda2
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return streamFilter.iterator();
            }
        };
        return new LineData(lineAddressType, lineAddressBytes, lineNames);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: safeName, reason: merged with bridge method [inline-methods] */
    public Name m1860lambda$parseLine$0$orgxbillDNShostsHostsFileParser(String name, int lineNumber, AtomicInteger nameFailures) {
        try {
            return Name.fromString(name, Name.root);
        } catch (TextParseException e) {
            log.debug("Could not decode name {}, {}#L{}, skipping", name, this.path, Integer.valueOf(lineNumber));
            nameFailures.incrementAndGet();
            return null;
        }
    }

    private String[] getLineTokens(String line) {
        int commentStart = line.indexOf(35);
        if (commentStart == -1) {
            commentStart = line.length();
        }
        return line.substring(0, commentStart).trim().split("\\s+");
    }

    private void validateCache() throws IOException {
        if (!this.clearCacheOnChange) {
            if (this.hostsCache == null) {
                synchronized (this) {
                    if (this.hostsCache == null) {
                        readHostsFile();
                    }
                }
                return;
            }
            return;
        }
        if (this.hostsCache == null || this.lastFileModificationCheckTime == null || this.lastFileModificationCheckTime.plus((TemporalAmount) this.fileChangeCheckInterval).isBefore(this.clock.instant())) {
            log.debug("Checked for changes more than 5minutes ago, checking");
            synchronized (this) {
                if (this.hostsCache != null && this.lastFileModificationCheckTime != null && !this.lastFileModificationCheckTime.plus((TemporalAmount) this.fileChangeCheckInterval).isBefore(this.clock.instant())) {
                    log.debug("Never mind, check fulfilled in another thread");
                } else {
                    this.lastFileModificationCheckTime = this.clock.instant();
                    readHostsFile();
                }
            }
        }
    }

    private void readHostsFile() throws IOException {
        if (Files.exists(this.path, new LinkOption[0])) {
            Instant fileTime = Files.getLastModifiedTime(this.path, new LinkOption[0]).toInstant();
            if (this.lastFileReadTime == null || !this.lastFileReadTime.equals(fileTime)) {
                createOrClearCache();
                this.hostsFileSizeBytes = Files.size(this.path);
                if (this.hostsFileSizeBytes <= this.maxFullCacheFileSizeBytes) {
                    parseEntireHostsFile();
                    this.isEntireFileParsed = true;
                }
                this.lastFileReadTime = fileTime;
                return;
            }
            return;
        }
        createOrClearCache();
    }

    private void createOrClearCache() {
        if (this.hostsCache == null) {
            this.hostsCache = new ConcurrentHashMap();
        } else {
            this.hostsCache.clear();
        }
    }

    private String key(Name name, int type) {
        return name.toString() + '\t' + type;
    }

    int cacheSize() {
        if (this.hostsCache == null) {
            return 0;
        }
        return this.hostsCache.size();
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }
}
