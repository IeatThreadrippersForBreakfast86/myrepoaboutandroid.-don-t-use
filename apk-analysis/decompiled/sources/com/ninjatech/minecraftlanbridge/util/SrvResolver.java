package com.ninjatech.minecraftlanbridge.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.text.StringsKt;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.SimpleResolver;

/* compiled from: SrvResolver.kt */
@Metadata(m145d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0017B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J(\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J)\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u00062\b\u0010\u0012\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\u0013J\"\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00040\u000e2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0018"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/util/SrvResolver;", "", "()V", "MINECRAFT_DEFAULT_PORT", "", "SRV_PREFIX", "", "buildResolvers", "", "Lorg/xbill/DNS/SimpleResolver;", "appContext", "Landroid/content/Context;", "getSystemDnsServers", "lookupSrv", "Lkotlin/Pair;", "host", "resolve", "Lcom/ninjatech/minecraftlanbridge/util/SrvResolver$ResolvedTarget;", "explicitPort", "(Ljava/lang/String;Ljava/lang/Integer;Landroid/content/Context;)Lcom/ninjatech/minecraftlanbridge/util/SrvResolver$ResolvedTarget;", "selectByPriorityAndWeight", "records", "Lorg/xbill/DNS/SRVRecord;", "ResolvedTarget", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes7.dex */
public final class SrvResolver {
    public static final SrvResolver INSTANCE = new SrvResolver();
    public static final int MINECRAFT_DEFAULT_PORT = 25565;
    private static final String SRV_PREFIX = "_minecraft._tcp.";

    private SrvResolver() {
    }

    /* compiled from: SrvResolver.kt */
    @Metadata(m145d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003¢\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00072\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001a"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/util/SrvResolver$ResolvedTarget;", "", "host", "", "port", "", "viaSrv", "", "displayHost", "(Ljava/lang/String;IZLjava/lang/String;)V", "getDisplayHost", "()Ljava/lang/String;", "getHost", "getPort", "()I", "getViaSrv", "()Z", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final /* data */ class ResolvedTarget {
        private final String displayHost;
        private final String host;
        private final int port;
        private final boolean viaSrv;

        public static /* synthetic */ ResolvedTarget copy$default(ResolvedTarget resolvedTarget, String str, int i, boolean z, String str2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = resolvedTarget.host;
            }
            if ((i2 & 2) != 0) {
                i = resolvedTarget.port;
            }
            if ((i2 & 4) != 0) {
                z = resolvedTarget.viaSrv;
            }
            if ((i2 & 8) != 0) {
                str2 = resolvedTarget.displayHost;
            }
            return resolvedTarget.copy(str, i, z, str2);
        }

        /* renamed from: component1, reason: from getter */
        public final String getHost() {
            return this.host;
        }

        /* renamed from: component2, reason: from getter */
        public final int getPort() {
            return this.port;
        }

        /* renamed from: component3, reason: from getter */
        public final boolean getViaSrv() {
            return this.viaSrv;
        }

        /* renamed from: component4, reason: from getter */
        public final String getDisplayHost() {
            return this.displayHost;
        }

        public final ResolvedTarget copy(String host, int port, boolean viaSrv, String displayHost) {
            Intrinsics.checkNotNullParameter(host, "host");
            Intrinsics.checkNotNullParameter(displayHost, "displayHost");
            return new ResolvedTarget(host, port, viaSrv, displayHost);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ResolvedTarget)) {
                return false;
            }
            ResolvedTarget resolvedTarget = (ResolvedTarget) other;
            return Intrinsics.areEqual(this.host, resolvedTarget.host) && this.port == resolvedTarget.port && this.viaSrv == resolvedTarget.viaSrv && Intrinsics.areEqual(this.displayHost, resolvedTarget.displayHost);
        }

        public int hashCode() {
            return (((((this.host.hashCode() * 31) + Integer.hashCode(this.port)) * 31) + Boolean.hashCode(this.viaSrv)) * 31) + this.displayHost.hashCode();
        }

        public String toString() {
            return "ResolvedTarget(host=" + this.host + ", port=" + this.port + ", viaSrv=" + this.viaSrv + ", displayHost=" + this.displayHost + ")";
        }

        public ResolvedTarget(String host, int port, boolean viaSrv, String displayHost) {
            Intrinsics.checkNotNullParameter(host, "host");
            Intrinsics.checkNotNullParameter(displayHost, "displayHost");
            this.host = host;
            this.port = port;
            this.viaSrv = viaSrv;
            this.displayHost = displayHost;
        }

        public final String getHost() {
            return this.host;
        }

        public final int getPort() {
            return this.port;
        }

        public final boolean getViaSrv() {
            return this.viaSrv;
        }

        public final String getDisplayHost() {
            return this.displayHost;
        }
    }

    public static /* synthetic */ ResolvedTarget resolve$default(SrvResolver srvResolver, String str, Integer num, Context context, int i, Object obj) {
        if ((i & 4) != 0) {
            context = null;
        }
        return srvResolver.resolve(str, num, context);
    }

    public final ResolvedTarget resolve(String host, Integer explicitPort, Context appContext) {
        Intrinsics.checkNotNullParameter(host, "host");
        String cleanedHost = StringsKt.removeSuffix(StringsKt.trim((CharSequence) host).toString(), (CharSequence) ".");
        if (NetworkUtils.INSTANCE.isIpv4(cleanedHost)) {
            int port = explicitPort != null ? explicitPort.intValue() : 25565;
            return new ResolvedTarget(cleanedHost, port, false, cleanedHost);
        }
        if (explicitPort != null) {
            return new ResolvedTarget(cleanedHost, explicitPort.intValue(), false, cleanedHost);
        }
        Pair srv = lookupSrv(cleanedHost, appContext);
        if (srv != null) {
            return new ResolvedTarget(srv.getFirst(), srv.getSecond().intValue(), true, cleanedHost);
        }
        return new ResolvedTarget(cleanedHost, 25565, false, cleanedHost);
    }

    private final Pair<String, Integer> lookupSrv(String host, Context appContext) {
        String queryName = SRV_PREFIX + host;
        List resolvers = buildResolvers(appContext);
        Cache cache = null;
        try {
            Iterator<SimpleResolver> it = resolvers.iterator();
            while (it.hasNext()) {
                SimpleResolver resolver = it.next();
                Lookup lookup = new Lookup(queryName, 33);
                lookup.setResolver(resolver);
                lookup.setCache(cache);
                Record[] records = lookup.run();
                if (records != null) {
                    Collection destination$iv$iv = new ArrayList();
                    int length = records.length;
                    int i = 0;
                    while (i < length) {
                        Record record = records[i];
                        Iterator<SimpleResolver> it2 = it;
                        if (record instanceof SRVRecord) {
                            destination$iv$iv.add(record);
                        }
                        i++;
                        it = it2;
                    }
                    Iterator<SimpleResolver> it3 = it;
                    List srvRecords = (List) destination$iv$iv;
                    if (!(!srvRecords.isEmpty())) {
                        srvRecords = null;
                    }
                    if (srvRecords != null) {
                        return selectByPriorityAndWeight(srvRecords);
                    }
                    it = it3;
                    cache = null;
                }
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private final List<SimpleResolver> buildResolvers(Context appContext) {
        LinkedHashSet addresses = new LinkedHashSet();
        Iterable $this$forEach$iv = getSystemDnsServers(appContext);
        for (Object element$iv : $this$forEach$iv) {
            String it = (String) element$iv;
            addresses.add(it);
        }
        addresses.add("8.8.8.8");
        addresses.add("1.1.1.1");
        LinkedHashSet $this$map$iv = addresses;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            String addr = (String) item$iv$iv;
            SimpleResolver $this$buildResolvers_u24lambda_u244_u24lambda_u243 = new SimpleResolver(addr);
            $this$buildResolvers_u24lambda_u244_u24lambda_u243.setTimeout(Duration.ofSeconds(3L));
            destination$iv$iv.add($this$buildResolvers_u24lambda_u244_u24lambda_u243);
        }
        return (List) destination$iv$iv;
    }

    private final List<String> getSystemDnsServers(Context appContext) {
        LinkProperties lp;
        if (appContext == null) {
            return CollectionsKt.emptyList();
        }
        try {
            Object systemService = appContext.getSystemService("connectivity");
            ConnectivityManager cm = systemService instanceof ConnectivityManager ? (ConnectivityManager) systemService : null;
            if (cm == null) {
                return CollectionsKt.emptyList();
            }
            Network active = cm.getActiveNetwork();
            if (active != null && (lp = cm.getLinkProperties(active)) != null) {
                Iterable dnsServers = lp.getDnsServers();
                Intrinsics.checkNotNullExpressionValue(dnsServers, "getDnsServers(...)");
                Iterable $this$mapNotNull$iv = dnsServers;
                Collection destination$iv$iv = new ArrayList();
                for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                    InetAddress it = (InetAddress) element$iv$iv$iv;
                    String hostAddress = it.getHostAddress();
                    if (hostAddress != null) {
                        destination$iv$iv.add(hostAddress);
                    }
                }
                return (List) destination$iv$iv;
            }
            return CollectionsKt.emptyList();
        } catch (Throwable th) {
            return CollectionsKt.emptyList();
        }
    }

    private final Pair<String, Integer> selectByPriorityAndWeight(List<? extends SRVRecord> records) {
        int weight;
        SRVRecord selected;
        Iterator<T> it = records.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        SRVRecord it2 = (SRVRecord) it.next();
        int priority = it2.getPriority();
        while (it.hasNext()) {
            SRVRecord it3 = (SRVRecord) it.next();
            int priority2 = it3.getPriority();
            if (priority > priority2) {
                priority = priority2;
            }
        }
        int minPriority = priority;
        List<? extends SRVRecord> $this$filter$iv = records;
        Collection destination$iv$iv = new ArrayList();
        Iterator it4 = $this$filter$iv.iterator();
        while (true) {
            if (!it4.hasNext()) {
                break;
            }
            Object element$iv$iv = it4.next();
            SRVRecord it5 = (SRVRecord) element$iv$iv;
            if ((it5.getPriority() == minPriority ? 1 : 0) != 0) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List<SRVRecord> candidates = (List) destination$iv$iv;
        if (candidates.size() == 1) {
            selected = (SRVRecord) CollectionsKt.first(candidates);
        } else {
            for (SRVRecord it6 : candidates) {
                weight += it6.getWeight();
            }
            int totalWeight = weight;
            if (totalWeight <= 0) {
                selected = (SRVRecord) CollectionsKt.random(candidates, Random.INSTANCE);
            } else {
                int r = Random.INSTANCE.nextInt(totalWeight);
                SRVRecord chosen = (SRVRecord) CollectionsKt.last(candidates);
                Iterator it7 = candidates.iterator();
                while (true) {
                    if (!it7.hasNext()) {
                        break;
                    }
                    SRVRecord rec = (SRVRecord) it7.next();
                    r -= rec.getWeight();
                    if (r <= 0) {
                        chosen = rec;
                        break;
                    }
                }
                selected = chosen;
            }
        }
        String string = selected.getTarget().toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        String targetHost = StringsKt.removeSuffix(string, (CharSequence) ".");
        return TuplesKt.m153to(targetHost, Integer.valueOf(selected.getPort()));
    }
}
