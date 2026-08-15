package com.ninjatech.minecraftlanbridge.relay;

import androidx.core.app.NotificationCompat;
import com.ninjatech.minecraftlanbridge.util.NetworkUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* compiled from: UdpRelayEngine.kt */
@Metadata(m145d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0011\u0018\u0000 42\u00020\u0001:\u000545678B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u001d\u001a\u00020\u001eH\u0082@¢\u0006\u0002\u0010\u001fJ\"\u0010 \u001a\u0004\u0018\u00010\u00102\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0006\u0010#\u001a\u00020$J\u001a\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u0014H\u0002J \u0010*\u001a\u00020(2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u00142\u0006\u0010+\u001a\u00020\u000fH\u0002J\u000e\u0010,\u001a\u00020\u001eH\u0082@¢\u0006\u0002\u0010\u001fJ&\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020\u00182\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0082@¢\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\u001e2\u0006\u00101\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u00102J\u000e\u00103\u001a\u00020\u001eH\u0086@¢\u0006\u0002\u0010\u001fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001a¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c¨\u00069"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine;", "", "()V", "_status", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayStatus;", "cleanupJob", "Lkotlinx/coroutines/Job;", "listenJob", "log", "Lcom/ninjatech/minecraftlanbridge/relay/LogBuffer;", "getLog", "()Lcom/ninjatech/minecraftlanbridge/relay/LogBuffer;", "mappings", "Ljava/util/concurrent/ConcurrentHashMap;", "Ljava/net/InetSocketAddress;", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpMapping;", "mappingsScope", "Lkotlinx/coroutines/CoroutineScope;", "portToClient", "", "runningConfig", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayConfig;", "serverSocket", "Ljava/net/DatagramSocket;", NotificationCompat.CATEGORY_STATUS, "Lkotlinx/coroutines/flow/StateFlow;", "getStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "cleanupLoop", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOrCreateMapping", "clientAddr", "realDest", "isRunning", "", "parseHeader", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$ParsedHeader;", "data", "", "length", "prependHeader", "address", "receiveLoop", "relayReceiverLoop", "relaySocket", "(Ljava/net/DatagramSocket;Ljava/net/InetSocketAddress;Ljava/net/DatagramSocket;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "start", "config", "(Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stop", "Companion", "ParsedHeader", "UdpMapping", "UdpRelayConfig", "UdpRelayStatus", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public final class UdpRelayEngine {
    public static final byte ADDR_TYPE_HOSTNAME = 4;
    public static final byte ADDR_TYPE_IPV4 = 1;
    private static final long CLEANUP_INTERVAL_MS = 30000;
    public static final byte MAGIC = 76;
    private static final long MAPPING_IDLE_TIMEOUT_MS = 60000;
    private static final int MAX_PACKET_SIZE = 65535;
    private Job cleanupJob;
    private Job listenJob;
    private CoroutineScope mappingsScope;
    private volatile UdpRelayConfig runningConfig;
    private DatagramSocket serverSocket;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final UdpRelayEngine shared = new UdpRelayEngine();
    private final ConcurrentHashMap<InetSocketAddress, UdpMapping> mappings = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, InetSocketAddress> portToClient = new ConcurrentHashMap<>();
    private final MutableStateFlow<UdpRelayStatus> _status = StateFlowKt.MutableStateFlow(new UdpRelayStatus(false, null, 0, 0, 0, null, 63, null));
    private final StateFlow<UdpRelayStatus> status = FlowKt.asStateFlow(this._status);
    private final LogBuffer log = new LogBuffer(0, 1, null);

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine", m162f = "UdpRelayEngine.kt", m163i = {0}, m164l = {356}, m165m = "cleanupLoop", m166n = {"this"}, m167s = {"L$0"})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$cleanupLoop$1 */
    static final class C09631 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C09631(Continuation<? super C09631> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return UdpRelayEngine.this.cleanupLoop(this);
        }
    }

    public final StateFlow<UdpRelayStatus> getStatus() {
        return this.status;
    }

    public final LogBuffer getLog() {
        return this.log;
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0010"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$Companion;", "", "()V", "ADDR_TYPE_HOSTNAME", "", "ADDR_TYPE_IPV4", "CLEANUP_INTERVAL_MS", "", "MAGIC", "MAPPING_IDLE_TIMEOUT_MS", "MAX_PACKET_SIZE", "", "shared", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine;", "getShared", "()Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine;", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final UdpRelayEngine getShared() {
            return UdpRelayEngine.shared;
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001f\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayConfig;", "", "bindHost", "", "localPort", "", "(Ljava/lang/String;I)V", "getBindHost", "()Ljava/lang/String;", "getLocalPort", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final /* data */ class UdpRelayConfig {
        private final String bindHost;
        private final int localPort;

        public static /* synthetic */ UdpRelayConfig copy$default(UdpRelayConfig udpRelayConfig, String str, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = udpRelayConfig.bindHost;
            }
            if ((i2 & 2) != 0) {
                i = udpRelayConfig.localPort;
            }
            return udpRelayConfig.copy(str, i);
        }

        /* renamed from: component1, reason: from getter */
        public final String getBindHost() {
            return this.bindHost;
        }

        /* renamed from: component2, reason: from getter */
        public final int getLocalPort() {
            return this.localPort;
        }

        public final UdpRelayConfig copy(String bindHost, int localPort) {
            return new UdpRelayConfig(bindHost, localPort);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof UdpRelayConfig)) {
                return false;
            }
            UdpRelayConfig udpRelayConfig = (UdpRelayConfig) other;
            return Intrinsics.areEqual(this.bindHost, udpRelayConfig.bindHost) && this.localPort == udpRelayConfig.localPort;
        }

        public int hashCode() {
            return ((this.bindHost == null ? 0 : this.bindHost.hashCode()) * 31) + Integer.hashCode(this.localPort);
        }

        public String toString() {
            return "UdpRelayConfig(bindHost=" + this.bindHost + ", localPort=" + this.localPort + ")";
        }

        public UdpRelayConfig(String bindHost, int localPort) {
            this.bindHost = bindHost;
            this.localPort = localPort;
        }

        public final String getBindHost() {
            return this.bindHost;
        }

        public final int getLocalPort() {
            return this.localPort;
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u001a\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\fJ\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001d\u001a\u00020\nHÆ\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0005HÆ\u0003JG\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010 \u001a\u00020\u00032\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\"\u001a\u00020\u0007HÖ\u0001J\t\u0010#\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018¨\u0006$"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayStatus;", "", "running", "", "localHost", "", "localPort", "", "activeMappings", "packetsRelayed", "", "lastError", "(ZLjava/lang/String;IIJLjava/lang/String;)V", "getActiveMappings", "()I", "display", "getDisplay", "()Ljava/lang/String;", "getLastError", "getLocalHost", "getLocalPort", "getPacketsRelayed", "()J", "getRunning", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final /* data */ class UdpRelayStatus {
        private final int activeMappings;
        private final String lastError;
        private final String localHost;
        private final int localPort;
        private final long packetsRelayed;
        private final boolean running;

        public UdpRelayStatus() {
            this(false, null, 0, 0, 0L, null, 63, null);
        }

        public static /* synthetic */ UdpRelayStatus copy$default(UdpRelayStatus udpRelayStatus, boolean z, String str, int i, int i2, long j, String str2, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                z = udpRelayStatus.running;
            }
            if ((i3 & 2) != 0) {
                str = udpRelayStatus.localHost;
            }
            String str3 = str;
            if ((i3 & 4) != 0) {
                i = udpRelayStatus.localPort;
            }
            int i4 = i;
            if ((i3 & 8) != 0) {
                i2 = udpRelayStatus.activeMappings;
            }
            int i5 = i2;
            if ((i3 & 16) != 0) {
                j = udpRelayStatus.packetsRelayed;
            }
            long j2 = j;
            if ((i3 & 32) != 0) {
                str2 = udpRelayStatus.lastError;
            }
            return udpRelayStatus.copy(z, str3, i4, i5, j2, str2);
        }

        /* renamed from: component1, reason: from getter */
        public final boolean getRunning() {
            return this.running;
        }

        /* renamed from: component2, reason: from getter */
        public final String getLocalHost() {
            return this.localHost;
        }

        /* renamed from: component3, reason: from getter */
        public final int getLocalPort() {
            return this.localPort;
        }

        /* renamed from: component4, reason: from getter */
        public final int getActiveMappings() {
            return this.activeMappings;
        }

        /* renamed from: component5, reason: from getter */
        public final long getPacketsRelayed() {
            return this.packetsRelayed;
        }

        /* renamed from: component6, reason: from getter */
        public final String getLastError() {
            return this.lastError;
        }

        public final UdpRelayStatus copy(boolean running, String localHost, int localPort, int activeMappings, long packetsRelayed, String lastError) {
            Intrinsics.checkNotNullParameter(localHost, "localHost");
            return new UdpRelayStatus(running, localHost, localPort, activeMappings, packetsRelayed, lastError);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof UdpRelayStatus)) {
                return false;
            }
            UdpRelayStatus udpRelayStatus = (UdpRelayStatus) other;
            return this.running == udpRelayStatus.running && Intrinsics.areEqual(this.localHost, udpRelayStatus.localHost) && this.localPort == udpRelayStatus.localPort && this.activeMappings == udpRelayStatus.activeMappings && this.packetsRelayed == udpRelayStatus.packetsRelayed && Intrinsics.areEqual(this.lastError, udpRelayStatus.lastError);
        }

        public int hashCode() {
            return (((((((((Boolean.hashCode(this.running) * 31) + this.localHost.hashCode()) * 31) + Integer.hashCode(this.localPort)) * 31) + Integer.hashCode(this.activeMappings)) * 31) + Long.hashCode(this.packetsRelayed)) * 31) + (this.lastError == null ? 0 : this.lastError.hashCode());
        }

        public String toString() {
            return "UdpRelayStatus(running=" + this.running + ", localHost=" + this.localHost + ", localPort=" + this.localPort + ", activeMappings=" + this.activeMappings + ", packetsRelayed=" + this.packetsRelayed + ", lastError=" + this.lastError + ")";
        }

        public UdpRelayStatus(boolean running, String localHost, int localPort, int activeMappings, long packetsRelayed, String lastError) {
            Intrinsics.checkNotNullParameter(localHost, "localHost");
            this.running = running;
            this.localHost = localHost;
            this.localPort = localPort;
            this.activeMappings = activeMappings;
            this.packetsRelayed = packetsRelayed;
            this.lastError = lastError;
        }

        public /* synthetic */ UdpRelayStatus(boolean z, String str, int i, int i2, long j, String str2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this((i3 & 1) != 0 ? false : z, (i3 & 2) != 0 ? "" : str, (i3 & 4) != 0 ? 0 : i, (i3 & 8) == 0 ? i2 : 0, (i3 & 16) != 0 ? 0L : j, (i3 & 32) != 0 ? null : str2);
        }

        public final boolean getRunning() {
            return this.running;
        }

        public final String getLocalHost() {
            return this.localHost;
        }

        public final int getLocalPort() {
            return this.localPort;
        }

        public final int getActiveMappings() {
            return this.activeMappings;
        }

        public final long getPacketsRelayed() {
            return this.packetsRelayed;
        }

        public final String getLastError() {
            return this.lastError;
        }

        public final String getDisplay() {
            if (!this.running) {
                return "—";
            }
            return this.localHost + ":" + this.localPort;
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001e\u001a\u00020\bHÆ\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\nHÆ\u0003J=\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nHÆ\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010$\u001a\u00020%HÖ\u0001J\t\u0010&\u001a\u00020'HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006("}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpMapping;", "", "clientAddress", "Ljava/net/InetSocketAddress;", "realDest", "relaySocket", "Ljava/net/DatagramSocket;", "lastActive", "", "receiverJob", "Lkotlinx/coroutines/Job;", "(Ljava/net/InetSocketAddress;Ljava/net/InetSocketAddress;Ljava/net/DatagramSocket;JLkotlinx/coroutines/Job;)V", "getClientAddress", "()Ljava/net/InetSocketAddress;", "getLastActive", "()J", "setLastActive", "(J)V", "getRealDest", "setRealDest", "(Ljava/net/InetSocketAddress;)V", "getReceiverJob", "()Lkotlinx/coroutines/Job;", "setReceiverJob", "(Lkotlinx/coroutines/Job;)V", "getRelaySocket", "()Ljava/net/DatagramSocket;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    private static final /* data */ class UdpMapping {
        private final InetSocketAddress clientAddress;
        private volatile long lastActive;
        private InetSocketAddress realDest;
        private Job receiverJob;
        private final DatagramSocket relaySocket;

        public static /* synthetic */ UdpMapping copy$default(UdpMapping udpMapping, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, DatagramSocket datagramSocket, long j, Job job, int i, Object obj) {
            if ((i & 1) != 0) {
                inetSocketAddress = udpMapping.clientAddress;
            }
            if ((i & 2) != 0) {
                inetSocketAddress2 = udpMapping.realDest;
            }
            InetSocketAddress inetSocketAddress3 = inetSocketAddress2;
            if ((i & 4) != 0) {
                datagramSocket = udpMapping.relaySocket;
            }
            DatagramSocket datagramSocket2 = datagramSocket;
            if ((i & 8) != 0) {
                j = udpMapping.lastActive;
            }
            long j2 = j;
            if ((i & 16) != 0) {
                job = udpMapping.receiverJob;
            }
            return udpMapping.copy(inetSocketAddress, inetSocketAddress3, datagramSocket2, j2, job);
        }

        /* renamed from: component1, reason: from getter */
        public final InetSocketAddress getClientAddress() {
            return this.clientAddress;
        }

        /* renamed from: component2, reason: from getter */
        public final InetSocketAddress getRealDest() {
            return this.realDest;
        }

        /* renamed from: component3, reason: from getter */
        public final DatagramSocket getRelaySocket() {
            return this.relaySocket;
        }

        /* renamed from: component4, reason: from getter */
        public final long getLastActive() {
            return this.lastActive;
        }

        /* renamed from: component5, reason: from getter */
        public final Job getReceiverJob() {
            return this.receiverJob;
        }

        public final UdpMapping copy(InetSocketAddress clientAddress, InetSocketAddress realDest, DatagramSocket relaySocket, long lastActive, Job receiverJob) {
            Intrinsics.checkNotNullParameter(clientAddress, "clientAddress");
            Intrinsics.checkNotNullParameter(realDest, "realDest");
            Intrinsics.checkNotNullParameter(relaySocket, "relaySocket");
            return new UdpMapping(clientAddress, realDest, relaySocket, lastActive, receiverJob);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof UdpMapping)) {
                return false;
            }
            UdpMapping udpMapping = (UdpMapping) other;
            return Intrinsics.areEqual(this.clientAddress, udpMapping.clientAddress) && Intrinsics.areEqual(this.realDest, udpMapping.realDest) && Intrinsics.areEqual(this.relaySocket, udpMapping.relaySocket) && this.lastActive == udpMapping.lastActive && Intrinsics.areEqual(this.receiverJob, udpMapping.receiverJob);
        }

        public int hashCode() {
            return (((((((this.clientAddress.hashCode() * 31) + this.realDest.hashCode()) * 31) + this.relaySocket.hashCode()) * 31) + Long.hashCode(this.lastActive)) * 31) + (this.receiverJob == null ? 0 : this.receiverJob.hashCode());
        }

        public String toString() {
            return "UdpMapping(clientAddress=" + this.clientAddress + ", realDest=" + this.realDest + ", relaySocket=" + this.relaySocket + ", lastActive=" + this.lastActive + ", receiverJob=" + this.receiverJob + ")";
        }

        public UdpMapping(InetSocketAddress clientAddress, InetSocketAddress realDest, DatagramSocket relaySocket, long lastActive, Job receiverJob) {
            Intrinsics.checkNotNullParameter(clientAddress, "clientAddress");
            Intrinsics.checkNotNullParameter(realDest, "realDest");
            Intrinsics.checkNotNullParameter(relaySocket, "relaySocket");
            this.clientAddress = clientAddress;
            this.realDest = realDest;
            this.relaySocket = relaySocket;
            this.lastActive = lastActive;
            this.receiverJob = receiverJob;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ UdpMapping(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, DatagramSocket datagramSocket, long j, Job job, int i, DefaultConstructorMarker defaultConstructorMarker) {
            Job job2;
            if ((i & 16) == 0) {
                job2 = job;
            } else {
                job2 = null;
            }
            this(inetSocketAddress, inetSocketAddress2, datagramSocket, j, job2);
        }

        public final InetSocketAddress getClientAddress() {
            return this.clientAddress;
        }

        public final InetSocketAddress getRealDest() {
            return this.realDest;
        }

        public final void setRealDest(InetSocketAddress inetSocketAddress) {
            Intrinsics.checkNotNullParameter(inetSocketAddress, "<set-?>");
            this.realDest = inetSocketAddress;
        }

        public final DatagramSocket getRelaySocket() {
            return this.relaySocket;
        }

        public final long getLastActive() {
            return this.lastActive;
        }

        public final void setLastActive(long j) {
            this.lastActive = j;
        }

        public final Job getReceiverJob() {
            return this.receiverJob;
        }

        public final void setReceiverJob(Job job) {
            this.receiverJob = job;
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2", m162f = "UdpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2 */
    static final class C09652 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ UdpRelayConfig $config;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09652(UdpRelayConfig udpRelayConfig, Continuation<? super C09652> continuation) {
            super(2, continuation);
            this.$config = udpRelayConfig;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return UdpRelayEngine.this.new C09652(this.$config, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09652) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws SocketException, RelayStartException {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    if (UdpRelayEngine.this.isRunning()) {
                        UdpRelayEngine.this.getLog().log("UDP relay already running");
                        return Unit.INSTANCE;
                    }
                    if (this.$config.getLocalPort() <= 0 || this.$config.getLocalPort() > 65535) {
                        UdpRelayEngine.this._status.setValue(UdpRelayStatus.copy$default((UdpRelayStatus) UdpRelayEngine.this._status.getValue(), false, null, 0, 0, 0L, "Invalid UDP relay port", 30, null));
                        UdpRelayEngine.this.getLog().log("UDP relay: invalid port " + this.$config.getLocalPort());
                        throw new RelayStartException("UDP relay port must be 1-65535");
                    }
                    String lanIp = this.$config.getBindHost();
                    if (lanIp == null) {
                        lanIp = NetworkUtils.INSTANCE.getBestLanIpv4();
                    }
                    if (lanIp == null) {
                        UdpRelayEngine.this._status.setValue(UdpRelayStatus.copy$default((UdpRelayStatus) UdpRelayEngine.this._status.getValue(), false, null, 0, 0, 0L, "No LAN/Wi-Fi detected", 30, null));
                        UdpRelayEngine.this.getLog().log("UDP relay: no LAN/Wi-Fi IPv4 detected");
                        throw new RelayStartException("No LAN/Wi-Fi network detected for UDP relay");
                    }
                    try {
                        DatagramSocket $this$invokeSuspend_u24lambda_u240 = new DatagramSocket((SocketAddress) null);
                        UdpRelayConfig udpRelayConfig = this.$config;
                        $this$invokeSuspend_u24lambda_u240.setReuseAddress(true);
                        $this$invokeSuspend_u24lambda_u240.bind(new InetSocketAddress(lanIp, udpRelayConfig.getLocalPort()));
                        $this$invokeSuspend_u24lambda_u240.setSoTimeout(0);
                        UdpRelayEngine.this.serverSocket = $this$invokeSuspend_u24lambda_u240;
                        UdpRelayEngine.this.runningConfig = this.$config;
                        UdpRelayEngine.this._status.setValue(new UdpRelayStatus(true, lanIp, this.$config.getLocalPort(), 0, 0L, null, 56, null));
                        UdpRelayEngine.this.getLog().log("UDP relay started on " + lanIp + ":" + this.$config.getLocalPort());
                        CoroutineScope scope = CoroutineScopeKt.CoroutineScope(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null).plus(Dispatchers.getIO()));
                        UdpRelayEngine.this.mappingsScope = scope;
                        UdpRelayEngine.this.listenJob = BuildersKt__Builders_commonKt.launch$default(scope, null, null, new AnonymousClass1(UdpRelayEngine.this, null), 3, null);
                        UdpRelayEngine.this.cleanupJob = BuildersKt__Builders_commonKt.launch$default(scope, null, null, new AnonymousClass2(UdpRelayEngine.this, null), 3, null);
                        return Unit.INSTANCE;
                    } catch (SocketException e) {
                        UdpRelayEngine.this._status.setValue(UdpRelayStatus.copy$default((UdpRelayStatus) UdpRelayEngine.this._status.getValue(), false, null, 0, 0, 0L, "UDP port unavailable: " + this.$config.getLocalPort(), 30, null));
                        UdpRelayEngine.this.getLog().log("UDP relay port unavailable: " + this.$config.getLocalPort() + " (" + e.getMessage() + ")");
                        throw new RelayStartException("UDP relay port " + this.$config.getLocalPort() + " is unavailable or in use");
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* compiled from: UdpRelayEngine.kt */
        @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2$1", m162f = "UdpRelayEngine.kt", m163i = {}, m164l = {187}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            int label;
            final /* synthetic */ UdpRelayEngine this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(UdpRelayEngine udpRelayEngine, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = udpRelayEngine;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        if (this.this$0.receiveLoop(this) != coroutine_suspended) {
                            break;
                        } else {
                            return coroutine_suspended;
                        }
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Unit.INSTANCE;
            }
        }

        /* compiled from: UdpRelayEngine.kt */
        @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2$2", m162f = "UdpRelayEngine.kt", m163i = {}, m164l = {188}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$start$2$2, reason: invalid class name */
        static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            int label;
            final /* synthetic */ UdpRelayEngine this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(UdpRelayEngine udpRelayEngine, Continuation<? super AnonymousClass2> continuation) {
                super(2, continuation);
                this.this$0 = udpRelayEngine;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass2(this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        if (this.this$0.cleanupLoop(this) != coroutine_suspended) {
                            break;
                        } else {
                            return coroutine_suspended;
                        }
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Unit.INSTANCE;
            }
        }
    }

    public final Object start(UdpRelayConfig config, Continuation<? super Unit> continuation) throws Throwable {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C09652(config, null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object receiveLoop(Continuation<? super Unit> continuation) throws IOException {
        boolean z;
        DatagramSocket ss;
        DatagramSocket datagramSocket = this.serverSocket;
        if (datagramSocket == null) {
            return Unit.INSTANCE;
        }
        DatagramSocket ss2 = datagramSocket;
        byte[] buf = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (JobKt.isActive(continuation.get$context()) && !ss2.isClosed()) {
            try {
                ss2.receive(packet);
                z = true;
            } catch (SocketException e) {
                if (!ss2.isClosed()) {
                    this.log.log("UDP relay receive error: " + e.getMessage());
                }
                z = false;
            } catch (IOException e2) {
                z = false;
            }
            boolean received = z;
            if (received) {
                SocketAddress socketAddress = packet.getSocketAddress();
                InetSocketAddress inetSocketAddress = socketAddress instanceof InetSocketAddress ? (InetSocketAddress) socketAddress : null;
                if (inetSocketAddress != null) {
                    InetSocketAddress clientAddr = inetSocketAddress;
                    byte[] data = packet.getData();
                    int length = packet.getLength();
                    try {
                        Intrinsics.checkNotNull(data);
                        ParsedHeader header = parseHeader(data, length);
                        if (header != null) {
                            try {
                                InetSocketAddress realDest = new InetSocketAddress(header.getDestAddress(), header.getDestPort());
                                UdpMapping mapping = getOrCreateMapping(clientAddr, realDest, ss2);
                                if (mapping != null) {
                                    mapping.setRealDest(realDest);
                                    mapping.setLastActive(System.currentTimeMillis());
                                    byte[] payload = new byte[header.getPayloadLength()];
                                    System.arraycopy(data, header.getPayloadOffset(), payload, 0, header.getPayloadLength());
                                    try {
                                        ss = ss2;
                                    } catch (IOException e3) {
                                        e = e3;
                                        ss = ss2;
                                    }
                                    try {
                                        mapping.getRelaySocket().send(new DatagramPacket(payload, payload.length, realDest));
                                        this._status.setValue(UdpRelayStatus.copy$default(this._status.getValue(), false, null, 0, this.mappings.size(), this._status.getValue().getPacketsRelayed() + 1, null, 39, null));
                                        ss2 = ss;
                                    } catch (IOException e4) {
                                        e = e4;
                                        this.log.log("UDP relay: forward error to " + realDest + " (" + e.getMessage() + ")");
                                        ss2 = ss;
                                    }
                                }
                            } catch (Exception e5) {
                                this.log.log("UDP relay: bad dest address in header from " + clientAddr);
                                ss2 = ss2;
                            }
                        }
                    } catch (Exception e6) {
                        this.log.log("UDP relay: bad header from " + clientAddr + " (" + e6.getMessage() + ")");
                        ss2 = ss2;
                    }
                }
            }
        }
        return Unit.INSTANCE;
    }

    private final UdpMapping getOrCreateMapping(InetSocketAddress clientAddr, InetSocketAddress realDest, DatagramSocket serverSocket) throws SocketException {
        UdpMapping mapping = this.mappings.get(clientAddr);
        if (mapping != null) {
            return mapping;
        }
        try {
            DatagramSocket relaySocket = new DatagramSocket((SocketAddress) null);
            relaySocket.setReuseAddress(true);
            relaySocket.bind(new InetSocketAddress(0));
            relaySocket.setSoTimeout(0);
            UdpMapping mapping2 = new UdpMapping(clientAddr, realDest, relaySocket, System.currentTimeMillis(), null, 16, null);
            UdpMapping prev = this.mappings.putIfAbsent(clientAddr, mapping2);
            if (prev != null) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    UdpRelayEngine udpRelayEngine = this;
                    relaySocket.close();
                    Result.m255constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    Result.m255constructorimpl(ResultKt.createFailure(th));
                }
                return prev;
            }
            this.portToClient.put(Integer.valueOf(relaySocket.getLocalPort()), clientAddr);
            this.log.log("UDP relay: new mapping " + clientAddr + " -> " + realDest + " (relay port " + relaySocket.getLocalPort() + ")");
            CoroutineScope scope = this.mappingsScope;
            if (scope == null) {
                return mapping2;
            }
            mapping2.setReceiverJob(BuildersKt__Builders_commonKt.launch$default(scope, null, null, new C09642(relaySocket, clientAddr, serverSocket, null), 3, null));
            this._status.setValue(UdpRelayStatus.copy$default(this._status.getValue(), false, null, 0, this.mappings.size(), 0L, null, 55, null));
            return mapping2;
        } catch (SocketException e) {
            this.log.log("UDP relay: could not create relay socket for " + clientAddr + " (" + e.getMessage() + ")");
            return null;
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$getOrCreateMapping$2", m162f = "UdpRelayEngine.kt", m163i = {}, m164l = {302}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$getOrCreateMapping$2 */
    static final class C09642 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ InetSocketAddress $clientAddr;
        final /* synthetic */ DatagramSocket $relaySocket;
        final /* synthetic */ DatagramSocket $serverSocket;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09642(DatagramSocket datagramSocket, InetSocketAddress inetSocketAddress, DatagramSocket datagramSocket2, Continuation<? super C09642> continuation) {
            super(2, continuation);
            this.$relaySocket = datagramSocket;
            this.$clientAddr = inetSocketAddress;
            this.$serverSocket = datagramSocket2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return UdpRelayEngine.this.new C09642(this.$relaySocket, this.$clientAddr, this.$serverSocket, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09642) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (UdpRelayEngine.this.relayReceiverLoop(this.$relaySocket, this.$clientAddr, this.$serverSocket, this) != coroutine_suspended) {
                        break;
                    } else {
                        return coroutine_suspended;
                    }
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object relayReceiverLoop(DatagramSocket relaySocket, InetSocketAddress clientAddr, DatagramSocket serverSocket, Continuation<? super Unit> continuation) throws IOException {
        byte[] buf = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (JobKt.isActive(continuation.get$context()) && !relaySocket.isClosed()) {
            boolean received = false;
            try {
                relaySocket.receive(packet);
                received = true;
            } catch (SocketException e) {
            } catch (IOException e2) {
            }
            if (received) {
                SocketAddress socketAddress = packet.getSocketAddress();
                InetSocketAddress inetSocketAddress = socketAddress instanceof InetSocketAddress ? (InetSocketAddress) socketAddress : null;
                if (inetSocketAddress != null) {
                    InetSocketAddress srcAddr = inetSocketAddress;
                    byte[] data = packet.getData();
                    int length = packet.getLength();
                    UdpMapping m = this.mappings.get(clientAddr);
                    if (m != null) {
                        m.setLastActive(System.currentTimeMillis());
                    }
                    Intrinsics.checkNotNull(data);
                    byte[] framed = prependHeader(data, length, srcAddr);
                    try {
                        try {
                            serverSocket.send(new DatagramPacket(framed, framed.length, clientAddr));
                            this._status.setValue(UdpRelayStatus.copy$default(this._status.getValue(), false, null, 0, 0, this._status.getValue().getPacketsRelayed() + 1, null, 47, null));
                        } catch (IOException e3) {
                        }
                    } catch (IOException e4) {
                    }
                }
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0019  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x005d -> B:20:0x0061). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object cleanupLoop(Continuation<? super Unit> continuation) {
        C09631 c09631;
        UdpRelayEngine udpRelayEngine;
        UdpRelayEngine udpRelayEngine2;
        Object obj;
        Object $result;
        C09631 c096312;
        Iterator it;
        if (continuation instanceof C09631) {
            C09631 c096313 = (C09631) continuation;
            if ((c096313.label & Integer.MIN_VALUE) != 0) {
                c096313.label -= Integer.MIN_VALUE;
                c09631 = c096313;
            } else {
                c09631 = new C09631(continuation);
            }
        }
        Object $result2 = c09631.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c09631.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                udpRelayEngine = this;
                if (JobKt.isActive(c09631.get$context())) {
                    c09631.L$0 = udpRelayEngine;
                    c09631.label = 1;
                    if (DelayKt.delay(CLEANUP_INTERVAL_MS, c09631) == $result3) {
                        return $result3;
                    }
                    udpRelayEngine2 = udpRelayEngine;
                    obj = $result3;
                    $result = $result2;
                    c096312 = c09631;
                    long now = System.currentTimeMillis();
                    it = udpRelayEngine2.mappings.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<InetSocketAddress, UdpMapping> next = it.next();
                        Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                        Map.Entry<InetSocketAddress, UdpMapping> entry = next;
                        UdpMapping value = entry.getValue();
                        Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                        UdpMapping m = value;
                        if (now - m.getLastActive() > MAPPING_IDLE_TIMEOUT_MS) {
                            it.remove();
                            udpRelayEngine2.portToClient.remove(Boxing.boxInt(m.getRelaySocket().getLocalPort()));
                            Job receiverJob = m.getReceiverJob();
                            if (receiverJob != null) {
                                Job.DefaultImpls.cancel$default(receiverJob, (CancellationException) null, 1, (Object) null);
                            }
                            try {
                                Result.Companion companion = Result.INSTANCE;
                                m.getRelaySocket().close();
                                Result.m255constructorimpl(Unit.INSTANCE);
                            } catch (Throwable th) {
                                Result.Companion companion2 = Result.INSTANCE;
                                Result.m255constructorimpl(ResultKt.createFailure(th));
                            }
                            udpRelayEngine2.log.log("UDP relay: idle mapping evicted for " + entry.getKey());
                        }
                    }
                    udpRelayEngine2._status.setValue(UdpRelayStatus.copy$default(udpRelayEngine2._status.getValue(), false, null, 0, udpRelayEngine2.mappings.size(), 0L, null, 55, null));
                    c09631 = c096312;
                    $result2 = $result;
                    $result3 = obj;
                    udpRelayEngine = udpRelayEngine2;
                    if (JobKt.isActive(c09631.get$context())) {
                        return Unit.INSTANCE;
                    }
                }
            case 1:
                UdpRelayEngine udpRelayEngine3 = (UdpRelayEngine) c09631.L$0;
                ResultKt.throwOnFailure($result2);
                udpRelayEngine2 = udpRelayEngine3;
                obj = $result3;
                $result = $result2;
                c096312 = c09631;
                long now2 = System.currentTimeMillis();
                it = udpRelayEngine2.mappings.entrySet().iterator();
                while (it.hasNext()) {
                }
                udpRelayEngine2._status.setValue(UdpRelayStatus.copy$default(udpRelayEngine2._status.getValue(), false, null, 0, udpRelayEngine2.mappings.size(), 0L, null, 55, null));
                c09631 = c096312;
                $result2 = $result;
                $result3 = obj;
                udpRelayEngine = udpRelayEngine2;
                if (JobKt.isActive(c09631.get$context())) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f¨\u0006\u001a"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$ParsedHeader;", "", "destAddress", "Ljava/net/InetAddress;", "destPort", "", "payloadOffset", "payloadLength", "(Ljava/net/InetAddress;III)V", "getDestAddress", "()Ljava/net/InetAddress;", "getDestPort", "()I", "getPayloadLength", "getPayloadOffset", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    private static final /* data */ class ParsedHeader {
        private final InetAddress destAddress;
        private final int destPort;
        private final int payloadLength;
        private final int payloadOffset;

        public static /* synthetic */ ParsedHeader copy$default(ParsedHeader parsedHeader, InetAddress inetAddress, int i, int i2, int i3, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                inetAddress = parsedHeader.destAddress;
            }
            if ((i4 & 2) != 0) {
                i = parsedHeader.destPort;
            }
            if ((i4 & 4) != 0) {
                i2 = parsedHeader.payloadOffset;
            }
            if ((i4 & 8) != 0) {
                i3 = parsedHeader.payloadLength;
            }
            return parsedHeader.copy(inetAddress, i, i2, i3);
        }

        /* renamed from: component1, reason: from getter */
        public final InetAddress getDestAddress() {
            return this.destAddress;
        }

        /* renamed from: component2, reason: from getter */
        public final int getDestPort() {
            return this.destPort;
        }

        /* renamed from: component3, reason: from getter */
        public final int getPayloadOffset() {
            return this.payloadOffset;
        }

        /* renamed from: component4, reason: from getter */
        public final int getPayloadLength() {
            return this.payloadLength;
        }

        public final ParsedHeader copy(InetAddress destAddress, int destPort, int payloadOffset, int payloadLength) {
            Intrinsics.checkNotNullParameter(destAddress, "destAddress");
            return new ParsedHeader(destAddress, destPort, payloadOffset, payloadLength);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParsedHeader)) {
                return false;
            }
            ParsedHeader parsedHeader = (ParsedHeader) other;
            return Intrinsics.areEqual(this.destAddress, parsedHeader.destAddress) && this.destPort == parsedHeader.destPort && this.payloadOffset == parsedHeader.payloadOffset && this.payloadLength == parsedHeader.payloadLength;
        }

        public int hashCode() {
            return (((((this.destAddress.hashCode() * 31) + Integer.hashCode(this.destPort)) * 31) + Integer.hashCode(this.payloadOffset)) * 31) + Integer.hashCode(this.payloadLength);
        }

        public String toString() {
            return "ParsedHeader(destAddress=" + this.destAddress + ", destPort=" + this.destPort + ", payloadOffset=" + this.payloadOffset + ", payloadLength=" + this.payloadLength + ")";
        }

        public ParsedHeader(InetAddress destAddress, int destPort, int payloadOffset, int payloadLength) {
            Intrinsics.checkNotNullParameter(destAddress, "destAddress");
            this.destAddress = destAddress;
            this.destPort = destPort;
            this.payloadOffset = payloadOffset;
            this.payloadLength = payloadLength;
        }

        public final InetAddress getDestAddress() {
            return this.destAddress;
        }

        public final int getDestPort() {
            return this.destPort;
        }

        public final int getPayloadOffset() {
            return this.payloadOffset;
        }

        public final int getPayloadLength() {
            return this.payloadLength;
        }
    }

    private final ParsedHeader parseHeader(byte[] data, int length) throws UnknownHostException {
        if (length < 1 || data[0] != 76) {
            return null;
        }
        if (length < 2) {
            throw new IllegalArgumentException("header too short for type byte");
        }
        byte addrType = data[1];
        if (addrType == 1) {
            if (length < 8) {
                throw new IllegalArgumentException("IPv4 header too short");
            }
            byte[] addrBytes = new byte[4];
            System.arraycopy(data, 2, addrBytes, 0, 4);
            int port = ((data[6] & UByte.MAX_VALUE) << 8) | (data[7] & UByte.MAX_VALUE);
            InetAddress addr = InetAddress.getByAddress(addrBytes);
            Intrinsics.checkNotNull(addr);
            return new ParsedHeader(addr, port, 8, length - 8);
        }
        if (addrType == 4) {
            if (length < 3) {
                throw new IllegalArgumentException("hostname header too short");
            }
            int nameLen = data[2] & 255;
            int headerLen = nameLen + 3 + 2;
            if (length >= headerLen) {
                String hostname = new String(data, 3, nameLen, Charsets.US_ASCII);
                int portOff = nameLen + 3;
                int port2 = ((data[portOff] & UByte.MAX_VALUE) << 8) | (data[portOff + 1] & UByte.MAX_VALUE);
                InetAddress addr2 = InetAddress.getByName(hostname);
                Intrinsics.checkNotNull(addr2);
                return new ParsedHeader(addr2, port2, headerLen, length - headerLen);
            }
            throw new IllegalArgumentException("hostname header truncated");
        }
        throw new IllegalArgumentException("unknown address type: " + ((int) addrType));
    }

    private final byte[] prependHeader(byte[] data, int length, InetSocketAddress address) {
        byte[] addrBytes = address.getAddress().getAddress();
        if (addrBytes.length == 4) {
            byte[] framed = new byte[length + 8];
            framed[0] = MAGIC;
            framed[1] = 1;
            System.arraycopy(addrBytes, 0, framed, 2, 4);
            int port = address.getPort();
            framed[6] = (byte) (port >> 8);
            framed[7] = (byte) port;
            System.arraycopy(data, 0, framed, 8, length);
            return framed;
        }
        String hostStr = address.getAddress().getHostAddress();
        if (hostStr == null) {
            hostStr = "";
        }
        byte[] hostBytes = hostStr.getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(hostBytes, "getBytes(...)");
        int headerLen = hostBytes.length + 3 + 2;
        byte[] framed2 = new byte[headerLen + length];
        framed2[0] = MAGIC;
        framed2[1] = 4;
        framed2[2] = (byte) hostBytes.length;
        System.arraycopy(hostBytes, 0, framed2, 3, hostBytes.length);
        int portOff = hostBytes.length + 3;
        int port2 = address.getPort();
        framed2[portOff] = (byte) (port2 >> 8);
        framed2[portOff + 1] = (byte) port2;
        System.arraycopy(data, 0, framed2, headerLen, length);
        return framed2;
    }

    /* compiled from: UdpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$stop$2", m162f = "UdpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine$stop$2 */
    static final class C09662 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        int label;

        C09662(Continuation<? super C09662> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C09662 c09662 = UdpRelayEngine.this.new C09662(continuation);
            c09662.L$0 = obj;
            return c09662;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09662) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Unit unit;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    UdpRelayEngine.this.getLog().log("Stopping UDP relay…");
                    Job job = UdpRelayEngine.this.cleanupJob;
                    if (job != null) {
                        Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
                    }
                    Job job2 = UdpRelayEngine.this.listenJob;
                    if (job2 != null) {
                        Job.DefaultImpls.cancel$default(job2, (CancellationException) null, 1, (Object) null);
                    }
                    CoroutineScope coroutineScope = UdpRelayEngine.this.mappingsScope;
                    if (coroutineScope != null) {
                        CoroutineScopeKt.cancel$default(coroutineScope, null, 1, null);
                    }
                    UdpRelayEngine.this.cleanupJob = null;
                    UdpRelayEngine.this.listenJob = null;
                    UdpRelayEngine.this.mappingsScope = null;
                    Iterator it = UdpRelayEngine.this.mappings.entrySet().iterator();
                    while (it.hasNext()) {
                        Object value = ((Map.Entry) it.next()).getValue();
                        Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                        UdpMapping m = (UdpMapping) value;
                        try {
                            Result.Companion companion = Result.INSTANCE;
                            m.getRelaySocket().close();
                            Result.m255constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th) {
                            Result.Companion companion2 = Result.INSTANCE;
                            Result.m255constructorimpl(ResultKt.createFailure(th));
                        }
                        it.remove();
                    }
                    UdpRelayEngine.this.portToClient.clear();
                    UdpRelayEngine udpRelayEngine = UdpRelayEngine.this;
                    try {
                        Result.Companion companion3 = Result.INSTANCE;
                        DatagramSocket datagramSocket = udpRelayEngine.serverSocket;
                        if (datagramSocket != null) {
                            datagramSocket.close();
                            unit = Unit.INSTANCE;
                        } else {
                            unit = null;
                        }
                        Result.m255constructorimpl(unit);
                    } catch (Throwable th2) {
                        Result.Companion companion4 = Result.INSTANCE;
                        Result.m255constructorimpl(ResultKt.createFailure(th2));
                    }
                    UdpRelayEngine.this.serverSocket = null;
                    UdpRelayEngine.this.runningConfig = null;
                    UdpRelayEngine.this._status.setValue(new UdpRelayStatus(false, null, 0, 0, 0L, null, 62, null));
                    UdpRelayEngine.this.getLog().log("UDP relay stopped");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final Object stop(Continuation<? super Unit> continuation) throws Throwable {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C09662(null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    public final boolean isRunning() {
        DatagramSocket datagramSocket = this.serverSocket;
        return (datagramSocket == null || datagramSocket.isClosed()) ? false : true;
    }
}
