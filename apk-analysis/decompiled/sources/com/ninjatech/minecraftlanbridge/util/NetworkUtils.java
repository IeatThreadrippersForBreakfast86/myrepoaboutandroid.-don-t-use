package com.ninjatech.minecraftlanbridge.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p002io.CloseableKt;
import kotlin.text.StringsKt;

/* compiled from: NetworkUtils.kt */
@Metadata(m145d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007J\u000e\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u0007J\u000e\u0010\u000f\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u0007J\u000e\u0010\u0010\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/util/NetworkUtils;", "", "()V", "MAX_PORT", "", "MIN_PORT", "getBestLanIpv4", "", "isIpv4", "", "s", "isLocalPortAvailable", "port", "isResolvable", "host", "isValidHostOrIp", "isValidPort", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes7.dex */
public final class NetworkUtils {
    public static final NetworkUtils INSTANCE = new NetworkUtils();
    public static final int MAX_PORT = 65535;
    public static final int MIN_PORT = 1;

    private NetworkUtils() {
    }

    public final String getBestLanIpv4() {
        Object objM255constructorimpl;
        String name;
        Object objM255constructorimpl2;
        Object objM255constructorimpl3;
        Object objM255constructorimpl4;
        Object objM255constructorimpl5;
        List candidates = new ArrayList();
        try {
            Result.Companion companion = Result.INSTANCE;
            NetworkUtils networkUtils = this;
            objM255constructorimpl = Result.m255constructorimpl(NetworkInterface.getNetworkInterfaces());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM255constructorimpl = Result.m255constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m261isFailureimpl(objM255constructorimpl)) {
            objM255constructorimpl = null;
        }
        Enumeration interfaces = (Enumeration) objM255constructorimpl;
        if (interfaces == null) {
            return null;
        }
        Iterator it = CollectionsKt.iterator(interfaces);
        while (it.hasNext()) {
            NetworkInterface nif = (NetworkInterface) it.next();
            String name2 = nif.getName();
            if (name2 != null) {
                name = name2.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(name, "toLowerCase(...)");
            } else {
                name = null;
            }
            if (name == null) {
                name = "";
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                NetworkUtils networkUtils2 = this;
                objM255constructorimpl2 = Result.m255constructorimpl(Boolean.valueOf(nif.isUp()));
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM255constructorimpl2 = Result.m255constructorimpl(ResultKt.createFailure(th2));
            }
            if (Result.m261isFailureimpl(objM255constructorimpl2)) {
                objM255constructorimpl2 = false;
            }
            boolean isUp = ((Boolean) objM255constructorimpl2).booleanValue();
            try {
                Result.Companion companion5 = Result.INSTANCE;
                NetworkUtils networkUtils3 = this;
                objM255constructorimpl3 = Result.m255constructorimpl(Boolean.valueOf(nif.isLoopback()));
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                objM255constructorimpl3 = Result.m255constructorimpl(ResultKt.createFailure(th3));
            }
            if (Result.m261isFailureimpl(objM255constructorimpl3)) {
                objM255constructorimpl3 = true;
            }
            boolean isLoopback = ((Boolean) objM255constructorimpl3).booleanValue();
            try {
                Result.Companion companion7 = Result.INSTANCE;
                NetworkUtils networkUtils4 = this;
                objM255constructorimpl4 = Result.m255constructorimpl(Boolean.valueOf(nif.isVirtual()));
            } catch (Throwable th4) {
                Result.Companion companion8 = Result.INSTANCE;
                objM255constructorimpl4 = Result.m255constructorimpl(ResultKt.createFailure(th4));
            }
            if (Result.m261isFailureimpl(objM255constructorimpl4)) {
                objM255constructorimpl4 = false;
            }
            boolean isVirtual = ((Boolean) objM255constructorimpl4).booleanValue();
            try {
                Result.Companion companion9 = Result.INSTANCE;
                NetworkUtils networkUtils5 = this;
                objM255constructorimpl5 = Result.m255constructorimpl(Boolean.valueOf(nif.isPointToPoint()));
            } catch (Throwable th5) {
                Result.Companion companion10 = Result.INSTANCE;
                objM255constructorimpl5 = Result.m255constructorimpl(ResultKt.createFailure(th5));
            }
            if (Result.m261isFailureimpl(objM255constructorimpl5)) {
                objM255constructorimpl5 = false;
            }
            boolean isP2p = ((Boolean) objM255constructorimpl5).booleanValue();
            if (isUp && !isLoopback && !isVirtual && !isP2p && !StringsKt.startsWith$default(name, "docker", false, 2, (Object) null) && !StringsKt.startsWith$default(name, "rmnet", false, 2, (Object) null) && !StringsKt.startsWith$default(name, "tun", false, 2, (Object) null) && !StringsKt.startsWith$default(name, "ppp", false, 2, (Object) null)) {
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                Intrinsics.checkNotNullExpressionValue(inetAddresses, "getInetAddresses(...)");
                Iterator it2 = CollectionsKt.iterator(inetAddresses);
                while (it2.hasNext()) {
                    InetAddress addr = (InetAddress) it2.next();
                    if ((addr instanceof Inet4Address) && !((Inet4Address) addr).isLoopbackAddress()) {
                        boolean linkLocal = ((Inet4Address) addr).isLinkLocalAddress();
                        String hostAddress = ((Inet4Address) addr).getHostAddress();
                        if (hostAddress != null) {
                            candidates.add(TuplesKt.m153to(hostAddress, Boolean.valueOf(linkLocal)));
                        }
                    }
                }
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        List $this$filter$iv = candidates;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Pair it3 = (Pair) element$iv$iv;
            if (!((Boolean) it3.getSecond()).booleanValue()) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List nonLinkLocal = (List) destination$iv$iv;
        List list = nonLinkLocal;
        if (list.isEmpty()) {
            list = candidates;
        }
        return (String) ((Pair) CollectionsKt.first(list)).getFirst();
    }

    public final boolean isValidHostOrIp(String host) {
        CharSequence $this$all$iv;
        Intrinsics.checkNotNullParameter(host, "host");
        String h = StringsKt.trim((CharSequence) host).toString();
        if ((h.length() == 0) || h.length() > 253) {
            return false;
        }
        if (isIpv4(h)) {
            return true;
        }
        List<String> labels = StringsKt.split$default((CharSequence) h, new String[]{"."}, false, 0, 6, (Object) null);
        if (labels.isEmpty()) {
            return false;
        }
        for (String label : labels) {
            if ((label.length() == 0) || label.length() > 63 || StringsKt.startsWith$default(label, "-", false, 2, (Object) null) || StringsKt.endsWith$default(label, "-", false, 2, (Object) null)) {
                return false;
            }
            String $this$all$iv2 = label;
            int i = 0;
            while (true) {
                if (i < $this$all$iv2.length()) {
                    char element$iv = $this$all$iv2.charAt(i);
                    char it = (Character.isLetterOrDigit(element$iv) || element$iv == '-') ? (char) 1 : (char) 0;
                    if (it == 0) {
                        $this$all$iv = null;
                        break;
                    }
                    i++;
                } else {
                    $this$all$iv = 1;
                    break;
                }
            }
            if ($this$all$iv == null) {
                return false;
            }
        }
        return true;
    }

    public final boolean isIpv4(String s) {
        CharSequence $this$any$iv;
        Integer intOrNull;
        int v;
        Intrinsics.checkNotNullParameter(s, "s");
        List<String> parts = StringsKt.split$default((CharSequence) s, new String[]{"."}, false, 0, 6, (Object) null);
        if (parts.size() != 4) {
            return false;
        }
        for (String p : parts) {
            if ((p.length() == 0) || p.length() > 3) {
                return false;
            }
            String $this$any$iv2 = p;
            int i = 0;
            while (true) {
                if (i < $this$any$iv2.length()) {
                    char element$iv = $this$any$iv2.charAt(i);
                    if (!Character.isDigit(element$iv)) {
                        $this$any$iv = 1;
                        break;
                    }
                    i++;
                } else {
                    $this$any$iv = null;
                    break;
                }
            }
            if ($this$any$iv != null || (intOrNull = StringsKt.toIntOrNull(p)) == null || (v = intOrNull.intValue()) < 0 || v > 255) {
                return false;
            }
            if (p.length() > 1 && StringsKt.startsWith$default(p, "0", false, 2, (Object) null)) {
                return false;
            }
        }
        return true;
    }

    public final boolean isValidPort(int port) {
        return 1 <= port && port < 65536;
    }

    public final boolean isLocalPortAvailable(int port) {
        try {
            Socket socket = new Socket();
            try {
                Socket s = socket;
                s.setReuseAddress(true);
                s.bind(new InetSocketAddress(port));
                CloseableKt.closeFinally(socket, null);
                return true;
            } finally {
            }
        } catch (Exception e) {
            return false;
        }
    }

    public final boolean isResolvable(String host) {
        Intrinsics.checkNotNullParameter(host, "host");
        String h = StringsKt.trim((CharSequence) host).toString();
        if (h.length() == 0) {
            return false;
        }
        try {
            return InetAddress.getByName(h) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
