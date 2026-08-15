package com.ninjatech.minecraftlanbridge.relay;

import android.content.Context;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.FrameMetricsAggregator;
import androidx.core.app.NotificationCompat;
import com.ninjatech.minecraftlanbridge.util.NetworkUtils;
import com.ninjatech.minecraftlanbridge.util.SrvResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import kotlinx.coroutines.selects.SelectImplementation;

/* compiled from: TcpRelayEngine.kt */
@Metadata(m145d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u0000 72\u00020\u0001:\u000278B\u0005¢\u0006\u0002\u0010\u0002J&\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0005H\u0082@¢\u0006\u0002\u0010#J\u0012\u0010$\u001a\u00020\u001e2\b\u0010%\u001a\u0004\u0018\u00010\nH\u0002J\u001e\u0010&\u001a\u00020\u001e2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*H\u0082@¢\u0006\u0002\u0010+J&\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020\n2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0005H\u0082@¢\u0006\u0002\u0010.J\u0006\u0010/\u001a\u000200J\u0010\u00101\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0005H\u0002J\u0016\u00102\u001a\u00020\u001e2\u0006\u00103\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u00104J\u000e\u00105\u001a\u00020\u001eH\u0086@¢\u0006\u0002\u00106R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R2\u0010\b\u001a&\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\n0\n \u000b*\u0012\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\n0\n\u0018\u00010\f0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001a¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c¨\u00069"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine;", "", "()V", "_status", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;", "activeConnections", "Ljava/util/concurrent/atomic/AtomicInteger;", "activeSockets", "", "Ljava/net/Socket;", "kotlin.jvm.PlatformType", "", "connectionsScope", "Lkotlinx/coroutines/CoroutineScope;", "listenJob", "Lkotlinx/coroutines/Job;", "log", "Lcom/ninjatech/minecraftlanbridge/relay/LogBuffer;", "getLog", "()Lcom/ninjatech/minecraftlanbridge/relay/LogBuffer;", "runningConfig", "Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine$RelayConfig;", "serverSocket", "Ljava/net/ServerSocket;", NotificationCompat.CATEGORY_STATUS, "Lkotlinx/coroutines/flow/StateFlow;", "getStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "acceptLoop", "", "connScope", "resolvedRemote", "", "baseStatus", "(Lkotlinx/coroutines/CoroutineScope;Ljava/lang/String;Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "closeQuietly", "s", "copy", "input", "Ljava/io/InputStream;", "output", "Ljava/io/OutputStream;", "(Ljava/io/InputStream;Ljava/io/OutputStream;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleConnection", "client", "(Ljava/net/Socket;Ljava/lang/String;Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isRunning", "", "publishState", "start", "config", "(Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine$RelayConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stop", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "RelayConfig", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public final class TcpRelayEngine {
    private static final int BUFFER_SIZE = 16384;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final TcpRelayEngine shared = new TcpRelayEngine();
    private CoroutineScope connectionsScope;
    private Job listenJob;
    private volatile RelayConfig runningConfig;
    private ServerSocket serverSocket;
    private final Set<Socket> activeSockets = Collections.synchronizedSet(new LinkedHashSet());
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final MutableStateFlow<BridgeStatus> _status = StateFlowKt.MutableStateFlow(new BridgeStatus(null, null, 0, null, false, null, 0, 0, null, FrameMetricsAggregator.EVERY_DURATION, null));
    private final StateFlow<BridgeStatus> status = FlowKt.asStateFlow(this._status);
    private final LogBuffer log = new LogBuffer(0, 1, null);

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine", m162f = "TcpRelayEngine.kt", m163i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2}, m164l = {289, 329, 350}, m165m = "handleConnection", m166n = {"this", "client", "baseStatus", "remote", "dialHost", "myIndex", "dialPort", "this", "client", "baseStatus", "remote", "myIndex", "this", "client", "baseStatus", "remote"}, m167s = {"L$0", "L$1", "L$2", "L$3", "L$4", "I$0", "I$1", "L$0", "L$1", "L$2", "L$3", "I$0", "L$0", "L$1", "L$2", "L$3"})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$1 */
    static final class C09571 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        /* synthetic */ Object result;

        C09571(Continuation<? super C09571> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TcpRelayEngine.this.handleConnection(null, null, null, this);
        }
    }

    public final StateFlow<BridgeStatus> getStatus() {
        return this.status;
    }

    public final LogBuffer getLog() {
        return this.log;
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine$Companion;", "", "()V", "BUFFER_SIZE", "", "shared", "Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine;", "getShared", "()Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine;", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final TcpRelayEngine getShared() {
            return TcpRelayEngine.shared;
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bJ\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u0015J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003HÆ\u0003J:\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010 \u001a\u00020\u0005HÖ\u0001J\t\u0010!\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\u0016\u001a\u0004\b\u0014\u0010\u0015¨\u0006\""}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine$RelayConfig;", "", "remoteHost", "", "remotePort", "", "localPort", "bindHost", "(Ljava/lang/String;Ljava/lang/Integer;ILjava/lang/String;)V", "appContext", "Landroid/content/Context;", "getAppContext", "()Landroid/content/Context;", "setAppContext", "(Landroid/content/Context;)V", "getBindHost", "()Ljava/lang/String;", "getLocalPort", "()I", "getRemoteHost", "getRemotePort", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/lang/Integer;ILjava/lang/String;)Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine$RelayConfig;", "equals", "", "other", "hashCode", "toString", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final /* data */ class RelayConfig {
        private Context appContext;
        private final String bindHost;
        private final int localPort;
        private final String remoteHost;
        private final Integer remotePort;

        public static /* synthetic */ RelayConfig copy$default(RelayConfig relayConfig, String str, Integer num, int i, String str2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = relayConfig.remoteHost;
            }
            if ((i2 & 2) != 0) {
                num = relayConfig.remotePort;
            }
            if ((i2 & 4) != 0) {
                i = relayConfig.localPort;
            }
            if ((i2 & 8) != 0) {
                str2 = relayConfig.bindHost;
            }
            return relayConfig.copy(str, num, i, str2);
        }

        /* renamed from: component1, reason: from getter */
        public final String getRemoteHost() {
            return this.remoteHost;
        }

        /* renamed from: component2, reason: from getter */
        public final Integer getRemotePort() {
            return this.remotePort;
        }

        /* renamed from: component3, reason: from getter */
        public final int getLocalPort() {
            return this.localPort;
        }

        /* renamed from: component4, reason: from getter */
        public final String getBindHost() {
            return this.bindHost;
        }

        public final RelayConfig copy(String remoteHost, Integer remotePort, int localPort, String bindHost) {
            Intrinsics.checkNotNullParameter(remoteHost, "remoteHost");
            return new RelayConfig(remoteHost, remotePort, localPort, bindHost);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof RelayConfig)) {
                return false;
            }
            RelayConfig relayConfig = (RelayConfig) other;
            return Intrinsics.areEqual(this.remoteHost, relayConfig.remoteHost) && Intrinsics.areEqual(this.remotePort, relayConfig.remotePort) && this.localPort == relayConfig.localPort && Intrinsics.areEqual(this.bindHost, relayConfig.bindHost);
        }

        public int hashCode() {
            return (((((this.remoteHost.hashCode() * 31) + (this.remotePort == null ? 0 : this.remotePort.hashCode())) * 31) + Integer.hashCode(this.localPort)) * 31) + (this.bindHost != null ? this.bindHost.hashCode() : 0);
        }

        public String toString() {
            return "RelayConfig(remoteHost=" + this.remoteHost + ", remotePort=" + this.remotePort + ", localPort=" + this.localPort + ", bindHost=" + this.bindHost + ")";
        }

        public RelayConfig(String remoteHost, Integer remotePort, int localPort, String bindHost) {
            Intrinsics.checkNotNullParameter(remoteHost, "remoteHost");
            this.remoteHost = remoteHost;
            this.remotePort = remotePort;
            this.localPort = localPort;
            this.bindHost = bindHost;
        }

        public final String getRemoteHost() {
            return this.remoteHost;
        }

        public final Integer getRemotePort() {
            return this.remotePort;
        }

        public final int getLocalPort() {
            return this.localPort;
        }

        public final String getBindHost() {
            return this.bindHost;
        }

        public final Context getAppContext() {
            return this.appContext;
        }

        public final void setAppContext(Context context) {
            this.appContext = context;
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$start$2", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$start$2 */
    static final class C09612 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ RelayConfig $config;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09612(RelayConfig relayConfig, Continuation<? super C09612> continuation) {
            super(2, continuation);
            this.$config = relayConfig;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return TcpRelayEngine.this.new C09612(this.$config, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09612) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws IOException, RelayStartException {
            String forwardingTo;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    if (TcpRelayEngine.this.isRunning()) {
                        TcpRelayEngine.this.getLog().log("Bridge already running");
                        return Unit.INSTANCE;
                    }
                    if (!NetworkUtils.INSTANCE.isValidHostOrIp(this.$config.getRemoteHost())) {
                        MutableStateFlow mutableStateFlow = TcpRelayEngine.this._status;
                        BridgeStatus bridgeStatus = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                        mutableStateFlow.setValue(bridgeStatus.copy((254 & 1) != 0 ? bridgeStatus.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus.remoteHost : null, (254 & 4) != 0 ? bridgeStatus.remotePort : 0, (254 & 8) != 0 ? bridgeStatus.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus.viaSrv : false, (254 & 32) != 0 ? bridgeStatus.localHost : null, (254 & 64) != 0 ? bridgeStatus.localPort : 0, (254 & 128) != 0 ? bridgeStatus.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus.lastError : "Invalid server address"));
                        TcpRelayEngine.this.getLog().log("Invalid server address: " + this.$config.getRemoteHost());
                        throw new RelayStartException("Invalid server address: " + this.$config.getRemoteHost());
                    }
                    if (this.$config.getRemotePort() != null && !NetworkUtils.INSTANCE.isValidPort(this.$config.getRemotePort().intValue())) {
                        MutableStateFlow mutableStateFlow2 = TcpRelayEngine.this._status;
                        BridgeStatus bridgeStatus2 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                        mutableStateFlow2.setValue(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus2.lastError : "Invalid remote port"));
                        TcpRelayEngine.this.getLog().log("Invalid remote port: " + this.$config.getRemotePort());
                        throw new RelayStartException("Remote port must be between 1 and 65535");
                    }
                    if (NetworkUtils.INSTANCE.isValidPort(this.$config.getLocalPort())) {
                        MutableStateFlow mutableStateFlow3 = TcpRelayEngine.this._status;
                        BridgeStatus bridgeStatus3 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                        mutableStateFlow3.setValue(bridgeStatus3.copy((254 & 1) != 0 ? bridgeStatus3.state : BridgeState.STARTING, (254 & 2) != 0 ? bridgeStatus3.remoteHost : null, (254 & 4) != 0 ? bridgeStatus3.remotePort : 0, (254 & 8) != 0 ? bridgeStatus3.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus3.viaSrv : false, (254 & 32) != 0 ? bridgeStatus3.localHost : null, (254 & 64) != 0 ? bridgeStatus3.localPort : 0, (254 & 128) != 0 ? bridgeStatus3.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus3.lastError : null));
                        TcpRelayEngine.this.getLog().log("Bridge starting…");
                        SrvResolver.ResolvedTarget target = SrvResolver.INSTANCE.resolve(this.$config.getRemoteHost(), this.$config.getRemotePort(), this.$config.getAppContext());
                        if (target.getViaSrv()) {
                            TcpRelayEngine.this.getLog().log("SRV record resolved: " + this.$config.getRemoteHost() + " -> " + target.getHost() + ":" + target.getPort());
                        } else if (this.$config.getRemotePort() == null) {
                            TcpRelayEngine.this.getLog().log("No SRV record for " + this.$config.getRemoteHost() + "; using default port " + target.getPort());
                        }
                        try {
                            InetAddress addr = InetAddress.getByName(target.getHost());
                            String hostAddress = addr.getHostAddress();
                            if (hostAddress == null) {
                                hostAddress = target.getHost();
                            }
                            String resolvedRemote = hostAddress;
                            TcpRelayEngine.this.getLog().log("Resolved " + target.getHost() + " -> " + resolvedRemote);
                            String lanIp = this.$config.getBindHost();
                            if (lanIp == null) {
                                lanIp = NetworkUtils.INSTANCE.getBestLanIpv4();
                            }
                            if (lanIp == null) {
                                MutableStateFlow mutableStateFlow4 = TcpRelayEngine.this._status;
                                BridgeStatus bridgeStatus4 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                                mutableStateFlow4.setValue(bridgeStatus4.copy((254 & 1) != 0 ? bridgeStatus4.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus4.remoteHost : null, (254 & 4) != 0 ? bridgeStatus4.remotePort : 0, (254 & 8) != 0 ? bridgeStatus4.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus4.viaSrv : false, (254 & 32) != 0 ? bridgeStatus4.localHost : null, (254 & 64) != 0 ? bridgeStatus4.localPort : 0, (254 & 128) != 0 ? bridgeStatus4.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus4.lastError : "No LAN/Wi-Fi network detected"));
                                TcpRelayEngine.this.getLog().log("No LAN/Wi-Fi IPv4 address detected (Wi-Fi disconnected?)");
                                throw new RelayStartException("No LAN/Wi-Fi network detected. Connect to Wi-Fi first.");
                            }
                            try {
                                ServerSocket ss = new ServerSocket();
                                RelayConfig relayConfig = this.$config;
                                ss.setReuseAddress(true);
                                ss.bind(new InetSocketAddress(lanIp, relayConfig.getLocalPort()));
                                TcpRelayEngine.this.serverSocket = ss;
                                TcpRelayEngine.this.runningConfig = this.$config;
                                BridgeStatus initialStatus = new BridgeStatus(BridgeState.LISTENING, this.$config.getRemoteHost(), target.getPort(), target.getHost(), target.getViaSrv(), lanIp, this.$config.getLocalPort(), 0, null);
                                TcpRelayEngine.this._status.setValue(initialStatus);
                                TcpRelayEngine.this.getLog().log("Bridge started");
                                TcpRelayEngine.this.getLog().log("Listening on " + lanIp + ":" + this.$config.getLocalPort());
                                if (target.getViaSrv() && !Intrinsics.areEqual(target.getHost(), this.$config.getRemoteHost())) {
                                    forwardingTo = this.$config.getRemoteHost() + " -> " + target.getHost() + ":" + target.getPort() + " (" + resolvedRemote + ")";
                                } else {
                                    forwardingTo = this.$config.getRemoteHost() + ":" + target.getPort() + " (" + resolvedRemote + ")";
                                }
                                TcpRelayEngine.this.getLog().log("Forwarding to " + forwardingTo);
                                CoroutineScope connScope = CoroutineScopeKt.CoroutineScope(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null).plus(Dispatchers.getIO()));
                                TcpRelayEngine.this.connectionsScope = connScope;
                                TcpRelayEngine.this.listenJob = BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(Dispatchers.getIO()), null, null, new AnonymousClass1(TcpRelayEngine.this, connScope, resolvedRemote, initialStatus, null), 3, null);
                                return Unit.INSTANCE;
                            } catch (IOException e) {
                                MutableStateFlow mutableStateFlow5 = TcpRelayEngine.this._status;
                                BridgeStatus bridgeStatus5 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                                mutableStateFlow5.setValue(bridgeStatus5.copy((254 & 1) != 0 ? bridgeStatus5.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus5.remoteHost : null, (254 & 4) != 0 ? bridgeStatus5.remotePort : 0, (254 & 8) != 0 ? bridgeStatus5.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus5.viaSrv : false, (254 & 32) != 0 ? bridgeStatus5.localHost : null, (254 & 64) != 0 ? bridgeStatus5.localPort : 0, (254 & 128) != 0 ? bridgeStatus5.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus5.lastError : "Port unavailable: " + this.$config.getLocalPort()));
                                TcpRelayEngine.this.getLog().log("Port unavailable: " + this.$config.getLocalPort() + " (" + e.getMessage() + ")");
                                throw new RelayStartException("Local port " + this.$config.getLocalPort() + " is unavailable or in use.");
                            }
                        } catch (Exception e2) {
                            MutableStateFlow mutableStateFlow6 = TcpRelayEngine.this._status;
                            BridgeStatus bridgeStatus6 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                            mutableStateFlow6.setValue(bridgeStatus6.copy((254 & 1) != 0 ? bridgeStatus6.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus6.remoteHost : null, (254 & 4) != 0 ? bridgeStatus6.remotePort : 0, (254 & 8) != 0 ? bridgeStatus6.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus6.viaSrv : false, (254 & 32) != 0 ? bridgeStatus6.localHost : null, (254 & 64) != 0 ? bridgeStatus6.localPort : 0, (254 & 128) != 0 ? bridgeStatus6.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus6.lastError : "DNS resolution failed"));
                            TcpRelayEngine.this.getLog().log("DNS resolution failed for " + target.getHost() + ": " + e2.getMessage());
                            throw new RelayStartException("Could not resolve hostname: " + target.getHost());
                        }
                    }
                    MutableStateFlow mutableStateFlow7 = TcpRelayEngine.this._status;
                    BridgeStatus bridgeStatus7 = (BridgeStatus) TcpRelayEngine.this._status.getValue();
                    mutableStateFlow7.setValue(bridgeStatus7.copy((254 & 1) != 0 ? bridgeStatus7.state : BridgeState.ERROR, (254 & 2) != 0 ? bridgeStatus7.remoteHost : null, (254 & 4) != 0 ? bridgeStatus7.remotePort : 0, (254 & 8) != 0 ? bridgeStatus7.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus7.viaSrv : false, (254 & 32) != 0 ? bridgeStatus7.localHost : null, (254 & 64) != 0 ? bridgeStatus7.localPort : 0, (254 & 128) != 0 ? bridgeStatus7.activeConnections : 0, (254 & 256) != 0 ? bridgeStatus7.lastError : "Invalid local port"));
                    TcpRelayEngine.this.getLog().log("Invalid local port: " + this.$config.getLocalPort());
                    throw new RelayStartException("Local port must be between 1 and 65535");
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* compiled from: TcpRelayEngine.kt */
        @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$start$2$1", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {231}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$start$2$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            final /* synthetic */ CoroutineScope $connScope;
            final /* synthetic */ BridgeStatus $initialStatus;
            final /* synthetic */ String $resolvedRemote;
            int label;
            final /* synthetic */ TcpRelayEngine this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(TcpRelayEngine tcpRelayEngine, CoroutineScope coroutineScope, String str, BridgeStatus bridgeStatus, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = tcpRelayEngine;
                this.$connScope = coroutineScope;
                this.$resolvedRemote = str;
                this.$initialStatus = bridgeStatus;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$connScope, this.$resolvedRemote, this.$initialStatus, continuation);
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
                        if (this.this$0.acceptLoop(this.$connScope, this.$resolvedRemote, this.$initialStatus, this) != coroutine_suspended) {
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

    public final Object start(RelayConfig config, Continuation<? super Unit> continuation) throws Throwable {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C09612(config, null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object acceptLoop(CoroutineScope connScope, String resolvedRemote, BridgeStatus baseStatus, Continuation<? super Unit> continuation) throws IOException {
        ServerSocket ss = this.serverSocket;
        if (ss == null) {
            return Unit.INSTANCE;
        }
        while (JobKt.isActive(continuation.getContext()) && !ss.isClosed()) {
            try {
                try {
                    Socket client = ss.accept();
                    InetAddress inetAddress = client.getInetAddress();
                    String clientIp = inetAddress != null ? inetAddress.getHostAddress() : null;
                    if (clientIp == null) {
                        clientIp = "?";
                    }
                    this.log.log("Client connected from " + clientIp);
                    BuildersKt__Builders_commonKt.launch$default(connScope, null, null, new C09552(client, resolvedRemote, baseStatus, null), 3, null);
                } catch (IOException e) {
                    if (!ss.isClosed()) {
                        this.log.log("Accept error: " + e.getMessage());
                    }
                }
            } catch (Exception e2) {
                if (!ss.isClosed()) {
                    this.log.log("Listener stopped: " + e2.getMessage());
                }
            }
        }
        return Unit.INSTANCE;
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$acceptLoop$2", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {257}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$acceptLoop$2 */
    static final class C09552 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ BridgeStatus $baseStatus;
        final /* synthetic */ Socket $client;
        final /* synthetic */ String $resolvedRemote;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09552(Socket socket, String str, BridgeStatus bridgeStatus, Continuation<? super C09552> continuation) {
            super(2, continuation);
            this.$client = socket;
            this.$resolvedRemote = str;
            this.$baseStatus = bridgeStatus;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return TcpRelayEngine.this.new C09552(this.$client, this.$resolvedRemote, this.$baseStatus, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09552) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    TcpRelayEngine tcpRelayEngine = TcpRelayEngine.this;
                    Socket client = this.$client;
                    Intrinsics.checkNotNullExpressionValue(client, "$client");
                    this.label = 1;
                    if (tcpRelayEngine.handleConnection(client, this.$resolvedRemote, this.$baseStatus, this) != coroutine_suspended) {
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
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x03e9  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x03f9  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x047a  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0482  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x04f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0178 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01c4 A[Catch: all -> 0x0379, Exception -> 0x0386, IOException -> 0x0392, TRY_ENTER, TryCatch #14 {IOException -> 0x0392, Exception -> 0x0386, all -> 0x0379, blocks: (B:51:0x016e, B:65:0x01d9, B:72:0x021a, B:64:0x01c4), top: B:186:0x016e }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0019  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x026f A[Catch: all -> 0x032e, Exception -> 0x0335, IOException -> 0x033c, TryCatch #21 {IOException -> 0x033c, Exception -> 0x0335, all -> 0x032e, blocks: (B:80:0x0260, B:82:0x0264, B:87:0x026f, B:88:0x028e), top: B:178:0x0260 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02a7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02d4  */
    /* JADX WARN: Type inference failed for: r4v46 */
    /* JADX WARN: Type inference failed for: r4v52, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r4v57 */
    /* JADX WARN: Type inference failed for: r4v58 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object handleConnection(Socket socket, String str, BridgeStatus bridgeStatus, Continuation<? super Unit> continuation) throws Throwable {
        C09571 c09571;
        BridgeStatus bridgeStatus2;
        TcpRelayEngine tcpRelayEngine;
        Socket socket2;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef objectRef2;
        String resolvedRemoteHost;
        int remotePort;
        Object objWithContext;
        int i;
        Ref.ObjectRef objectRef3;
        String str2;
        int i2;
        BridgeStatus bridgeStatus3;
        Socket socket3;
        TcpRelayEngine tcpRelayEngine2;
        Ref.ObjectRef objectRef4;
        T t;
        BridgeStatus bridgeStatus4;
        TcpRelayEngine tcpRelayEngine3;
        int i3;
        ServerSocket serverSocket;
        C09604 c09604;
        Ref.ObjectRef objectRef5;
        BridgeStatus bridgeStatus5;
        LogBuffer logBuffer;
        int iCoerceAtLeast;
        if (continuation instanceof C09571) {
            c09571 = (C09571) continuation;
            if ((c09571.label & Integer.MIN_VALUE) != 0) {
                c09571.label -= Integer.MIN_VALUE;
            } else {
                c09571 = new C09571(continuation);
            }
        }
        Object obj = c09571.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Ref.ObjectRef sb = c09571.label;
        try {
            switch (sb) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    tcpRelayEngine = this;
                    socket2 = socket;
                    if (tcpRelayEngine.runningConfig == null) {
                        tcpRelayEngine.closeQuietly(socket2);
                        return Unit.INSTANCE;
                    }
                    int iIncrementAndGet = tcpRelayEngine.activeConnections.incrementAndGet();
                    tcpRelayEngine.publishState(bridgeStatus.copy((254 & 1) != 0 ? bridgeStatus.state : null, (254 & 2) != 0 ? bridgeStatus.remoteHost : null, (254 & 4) != 0 ? bridgeStatus.remotePort : 0, (254 & 8) != 0 ? bridgeStatus.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus.viaSrv : false, (254 & 32) != 0 ? bridgeStatus.localHost : null, (254 & 64) != 0 ? bridgeStatus.localPort : 0, (254 & 128) != 0 ? bridgeStatus.activeConnections : iIncrementAndGet, (254 & 256) != 0 ? bridgeStatus.lastError : null));
                    Ref.ObjectRef objectRef6 = new Ref.ObjectRef();
                    try {
                        resolvedRemoteHost = bridgeStatus.getResolvedRemoteHost();
                        if (resolvedRemoteHost == null) {
                            resolvedRemoteHost = str;
                        }
                        remotePort = bridgeStatus.getRemotePort();
                        CoroutineDispatcher io = Dispatchers.getIO();
                        C09582 c09582 = new C09582(str, remotePort, null);
                        c09571.L$0 = tcpRelayEngine;
                        c09571.L$1 = socket2;
                        c09571.L$2 = bridgeStatus;
                        c09571.L$3 = objectRef6;
                        c09571.L$4 = resolvedRemoteHost;
                        c09571.L$5 = objectRef6;
                        c09571.I$0 = iIncrementAndGet;
                        c09571.I$1 = remotePort;
                        c09571.label = 1;
                        objWithContext = BuildersKt.withContext(io, c09582, c09571);
                    } catch (IOException e) {
                        e = e;
                        bridgeStatus2 = bridgeStatus;
                        objectRef2 = objectRef6;
                        tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef2.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket2 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Exception e2) {
                        e = e2;
                        bridgeStatus2 = bridgeStatus;
                        objectRef = objectRef6;
                        tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet2 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket3 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Throwable th) {
                        th = th;
                        bridgeStatus2 = bridgeStatus;
                        sb = objectRef6;
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(sb.element);
                        tcpRelayEngine.closeQuietly((Socket) sb.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet3 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket4 = tcpRelayEngine.serverSocket;
                        tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket4 != null && !serverSocket4.isClosed()) && iDecrementAndGet3 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet3, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                        tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet3, 0) + ")");
                        throw th;
                    }
                    if (objWithContext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    i = iIncrementAndGet;
                    objectRef3 = objectRef6;
                    str2 = resolvedRemoteHost;
                    i2 = remotePort;
                    bridgeStatus3 = bridgeStatus;
                    socket3 = socket2;
                    tcpRelayEngine2 = tcpRelayEngine;
                    objectRef4 = objectRef3;
                    t = objWithContext;
                    try {
                        objectRef4.element = t;
                        if (!bridgeStatus3.getViaSrv()) {
                            try {
                                String str3 = !Intrinsics.areEqual(str2, bridgeStatus3.getRemoteHost()) ? bridgeStatus3.getRemoteHost() + " -> " + str2 + ":" + i2 : str2 + ":" + i2;
                                tcpRelayEngine2.log.log("Connected to remote server " + str3);
                                socket3.setTcpNoDelay(true);
                                socket3.setKeepAlive(true);
                                tcpRelayEngine2.activeSockets.add(socket3);
                                tcpRelayEngine2.activeSockets.add(objectRef3.element);
                                String remoteHost = bridgeStatus3.getRemoteHost();
                                if (!(remoteHost.length() == 0)) {
                                    str2 = remoteHost;
                                }
                                String str4 = str2;
                                CoroutineDispatcher io2 = Dispatchers.getIO();
                                TcpRelayEngine tcpRelayEngine4 = tcpRelayEngine2;
                                int i4 = i2;
                                Socket socket4 = socket3;
                                BridgeStatus bridgeStatus6 = bridgeStatus3;
                                try {
                                    C09593 c09593 = new C09593(socket3, objectRef3, str4, i4, tcpRelayEngine4, null);
                                    try {
                                        c09571.L$0 = tcpRelayEngine4;
                                        c09571.L$1 = socket4;
                                        c09571.L$2 = bridgeStatus6;
                                        c09571.L$3 = objectRef3;
                                        c09571.L$4 = null;
                                        c09571.L$5 = null;
                                        c09571.I$0 = i;
                                        c09571.label = 2;
                                        if (BuildersKt.withContext(io2, c09593, c09571) == coroutine_suspended) {
                                            return coroutine_suspended;
                                        }
                                        socket2 = socket4;
                                        bridgeStatus4 = bridgeStatus6;
                                        tcpRelayEngine3 = tcpRelayEngine4;
                                        i3 = i;
                                        try {
                                            serverSocket = tcpRelayEngine3.serverSocket;
                                            if (serverSocket == null && !serverSocket.isClosed()) {
                                                BridgeStatus bridgeStatus7 = bridgeStatus4;
                                                tcpRelayEngine3.publishState(bridgeStatus7.copy((254 & 1) != 0 ? bridgeStatus7.state : BridgeState.RUNNING, (254 & 2) != 0 ? bridgeStatus7.remoteHost : null, (254 & 4) != 0 ? bridgeStatus7.remotePort : 0, (254 & 8) != 0 ? bridgeStatus7.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus7.viaSrv : false, (254 & 32) != 0 ? bridgeStatus7.localHost : null, (254 & 64) != 0 ? bridgeStatus7.localPort : 0, (254 & 128) != 0 ? bridgeStatus7.activeConnections : i3, (254 & 256) != 0 ? bridgeStatus7.lastError : null));
                                            }
                                            c09604 = tcpRelayEngine3.new C09604(socket2, objectRef3, null);
                                            c09571.L$0 = tcpRelayEngine3;
                                            c09571.L$1 = socket2;
                                            c09571.L$2 = bridgeStatus4;
                                            c09571.L$3 = objectRef3;
                                            c09571.label = 3;
                                            if (CoroutineScopeKt.coroutineScope(c09604, c09571) != coroutine_suspended) {
                                                return coroutine_suspended;
                                            }
                                            objectRef5 = objectRef3;
                                            bridgeStatus5 = bridgeStatus4;
                                            tcpRelayEngine = tcpRelayEngine3;
                                            tcpRelayEngine.activeSockets.remove(socket2);
                                            tcpRelayEngine.activeSockets.remove(objectRef5.element);
                                            tcpRelayEngine.closeQuietly((Socket) objectRef5.element);
                                            tcpRelayEngine.closeQuietly(socket2);
                                            int iDecrementAndGet4 = tcpRelayEngine.activeConnections.decrementAndGet();
                                            ServerSocket serverSocket5 = tcpRelayEngine.serverSocket;
                                            tcpRelayEngine.publishState(bridgeStatus5.copy((254 & 1) != 0 ? bridgeStatus5.state : ((serverSocket5 == null && !serverSocket5.isClosed()) || iDecrementAndGet4 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus5.remoteHost : null, (254 & 4) != 0 ? bridgeStatus5.remotePort : 0, (254 & 8) != 0 ? bridgeStatus5.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus5.viaSrv : false, (254 & 32) != 0 ? bridgeStatus5.localHost : null, (254 & 64) != 0 ? bridgeStatus5.localPort : 0, (254 & 128) != 0 ? bridgeStatus5.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet4, 0), (254 & 256) != 0 ? bridgeStatus5.lastError : null));
                                            logBuffer = tcpRelayEngine.log;
                                            iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet4, 0);
                                            sb = new StringBuilder();
                                        } catch (IOException e3) {
                                            e = e3;
                                            objectRef2 = objectRef3;
                                            bridgeStatus2 = bridgeStatus4;
                                            tcpRelayEngine = tcpRelayEngine3;
                                            tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                                            tcpRelayEngine.activeSockets.remove(socket2);
                                            tcpRelayEngine.activeSockets.remove(objectRef2.element);
                                            tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                                            tcpRelayEngine.closeQuietly(socket2);
                                            int iDecrementAndGet5 = tcpRelayEngine.activeConnections.decrementAndGet();
                                            ServerSocket serverSocket22 = tcpRelayEngine.serverSocket;
                                            if (serverSocket22 != null) {
                                                if (serverSocket22 != null && !serverSocket22.isClosed()) {
                                                    tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket22 != null && !serverSocket22.isClosed()) || iDecrementAndGet5 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet5, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                                    logBuffer = tcpRelayEngine.log;
                                                    iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet5, 0);
                                                    sb = new StringBuilder();
                                                }
                                            }
                                            logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                            return Unit.INSTANCE;
                                        } catch (Exception e4) {
                                            e = e4;
                                            objectRef = objectRef3;
                                            bridgeStatus2 = bridgeStatus4;
                                            tcpRelayEngine = tcpRelayEngine3;
                                            tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                                            tcpRelayEngine.activeSockets.remove(socket2);
                                            tcpRelayEngine.activeSockets.remove(objectRef.element);
                                            tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                                            tcpRelayEngine.closeQuietly(socket2);
                                            int iDecrementAndGet22 = tcpRelayEngine.activeConnections.decrementAndGet();
                                            ServerSocket serverSocket32 = tcpRelayEngine.serverSocket;
                                            if (serverSocket32 != null) {
                                                if (serverSocket32 != null && !serverSocket32.isClosed()) {
                                                    tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket32 != null && !serverSocket32.isClosed()) || iDecrementAndGet22 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet22, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                                    logBuffer = tcpRelayEngine.log;
                                                    iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet22, 0);
                                                    sb = new StringBuilder();
                                                }
                                            }
                                            logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                            return Unit.INSTANCE;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            sb = objectRef3;
                                            bridgeStatus2 = bridgeStatus4;
                                            tcpRelayEngine = tcpRelayEngine3;
                                            tcpRelayEngine.activeSockets.remove(socket2);
                                            tcpRelayEngine.activeSockets.remove(sb.element);
                                            tcpRelayEngine.closeQuietly((Socket) sb.element);
                                            tcpRelayEngine.closeQuietly(socket2);
                                            int iDecrementAndGet32 = tcpRelayEngine.activeConnections.decrementAndGet();
                                            ServerSocket serverSocket42 = tcpRelayEngine.serverSocket;
                                            tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket42 != null && !serverSocket42.isClosed()) && iDecrementAndGet32 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet32, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                            tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet32, 0) + ")");
                                            throw th;
                                        }
                                    } catch (IOException e5) {
                                        e = e5;
                                        socket2 = socket4;
                                        bridgeStatus2 = bridgeStatus6;
                                        objectRef2 = objectRef3;
                                        tcpRelayEngine = tcpRelayEngine4;
                                        tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                                        tcpRelayEngine.activeSockets.remove(socket2);
                                        tcpRelayEngine.activeSockets.remove(objectRef2.element);
                                        tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                                        tcpRelayEngine.closeQuietly(socket2);
                                        int iDecrementAndGet52 = tcpRelayEngine.activeConnections.decrementAndGet();
                                        ServerSocket serverSocket222 = tcpRelayEngine.serverSocket;
                                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                        return Unit.INSTANCE;
                                    } catch (Exception e6) {
                                        e = e6;
                                        socket2 = socket4;
                                        bridgeStatus2 = bridgeStatus6;
                                        objectRef = objectRef3;
                                        tcpRelayEngine = tcpRelayEngine4;
                                        tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                                        tcpRelayEngine.activeSockets.remove(socket2);
                                        tcpRelayEngine.activeSockets.remove(objectRef.element);
                                        tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                                        tcpRelayEngine.closeQuietly(socket2);
                                        int iDecrementAndGet222 = tcpRelayEngine.activeConnections.decrementAndGet();
                                        ServerSocket serverSocket322 = tcpRelayEngine.serverSocket;
                                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                        return Unit.INSTANCE;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        socket2 = socket4;
                                        bridgeStatus2 = bridgeStatus6;
                                        sb = objectRef3;
                                        tcpRelayEngine = tcpRelayEngine4;
                                        tcpRelayEngine.activeSockets.remove(socket2);
                                        tcpRelayEngine.activeSockets.remove(sb.element);
                                        tcpRelayEngine.closeQuietly((Socket) sb.element);
                                        tcpRelayEngine.closeQuietly(socket2);
                                        int iDecrementAndGet322 = tcpRelayEngine.activeConnections.decrementAndGet();
                                        ServerSocket serverSocket422 = tcpRelayEngine.serverSocket;
                                        tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket422 != null && !serverSocket422.isClosed()) && iDecrementAndGet322 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet322, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                        tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet322, 0) + ")");
                                        throw th;
                                    }
                                } catch (IOException e7) {
                                    e = e7;
                                    socket2 = socket4;
                                    bridgeStatus2 = bridgeStatus6;
                                    objectRef2 = objectRef3;
                                    tcpRelayEngine = tcpRelayEngine4;
                                } catch (Exception e8) {
                                    e = e8;
                                    socket2 = socket4;
                                    bridgeStatus2 = bridgeStatus6;
                                    objectRef = objectRef3;
                                    tcpRelayEngine = tcpRelayEngine4;
                                } catch (Throwable th4) {
                                    th = th4;
                                    socket2 = socket4;
                                    bridgeStatus2 = bridgeStatus6;
                                    sb = objectRef3;
                                    tcpRelayEngine = tcpRelayEngine4;
                                }
                            } catch (IOException e9) {
                                e = e9;
                                objectRef2 = objectRef3;
                                tcpRelayEngine = tcpRelayEngine2;
                                socket2 = socket3;
                                bridgeStatus2 = bridgeStatus3;
                                tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                                tcpRelayEngine.activeSockets.remove(socket2);
                                tcpRelayEngine.activeSockets.remove(objectRef2.element);
                                tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                                tcpRelayEngine.closeQuietly(socket2);
                                int iDecrementAndGet522 = tcpRelayEngine.activeConnections.decrementAndGet();
                                ServerSocket serverSocket2222 = tcpRelayEngine.serverSocket;
                                tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket2222 != null && !serverSocket2222.isClosed()) || iDecrementAndGet522 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet522, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                logBuffer = tcpRelayEngine.log;
                                iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet522, 0);
                                sb = new StringBuilder();
                                logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                return Unit.INSTANCE;
                            } catch (Exception e10) {
                                e = e10;
                                objectRef = objectRef3;
                                tcpRelayEngine = tcpRelayEngine2;
                                socket2 = socket3;
                                bridgeStatus2 = bridgeStatus3;
                                tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                                tcpRelayEngine.activeSockets.remove(socket2);
                                tcpRelayEngine.activeSockets.remove(objectRef.element);
                                tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                                tcpRelayEngine.closeQuietly(socket2);
                                int iDecrementAndGet2222 = tcpRelayEngine.activeConnections.decrementAndGet();
                                ServerSocket serverSocket3222 = tcpRelayEngine.serverSocket;
                                tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket3222 != null && !serverSocket3222.isClosed()) || iDecrementAndGet2222 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet2222, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                logBuffer = tcpRelayEngine.log;
                                iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet2222, 0);
                                sb = new StringBuilder();
                                logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                                return Unit.INSTANCE;
                            } catch (Throwable th5) {
                                th = th5;
                                sb = objectRef3;
                                tcpRelayEngine = tcpRelayEngine2;
                                socket2 = socket3;
                                bridgeStatus2 = bridgeStatus3;
                                tcpRelayEngine.activeSockets.remove(socket2);
                                tcpRelayEngine.activeSockets.remove(sb.element);
                                tcpRelayEngine.closeQuietly((Socket) sb.element);
                                tcpRelayEngine.closeQuietly(socket2);
                                int iDecrementAndGet3222 = tcpRelayEngine.activeConnections.decrementAndGet();
                                ServerSocket serverSocket4222 = tcpRelayEngine.serverSocket;
                                tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket4222 != null && !serverSocket4222.isClosed()) && iDecrementAndGet3222 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet3222, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                                tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet3222, 0) + ")");
                                throw th;
                            }
                        }
                    } catch (IOException e11) {
                        e = e11;
                        socket2 = socket3;
                        bridgeStatus2 = bridgeStatus3;
                        objectRef2 = objectRef3;
                        tcpRelayEngine = tcpRelayEngine2;
                    } catch (Exception e12) {
                        e = e12;
                        socket2 = socket3;
                        bridgeStatus2 = bridgeStatus3;
                        objectRef = objectRef3;
                        tcpRelayEngine = tcpRelayEngine2;
                    } catch (Throwable th6) {
                        th = th6;
                        socket2 = socket3;
                        bridgeStatus2 = bridgeStatus3;
                        sb = objectRef3;
                        tcpRelayEngine = tcpRelayEngine2;
                    }
                    logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                    return Unit.INSTANCE;
                case 1:
                    i2 = c09571.I$1;
                    int i5 = c09571.I$0;
                    objectRef4 = (Ref.ObjectRef) c09571.L$5;
                    String str5 = (String) c09571.L$4;
                    Ref.ObjectRef objectRef7 = (Ref.ObjectRef) c09571.L$3;
                    bridgeStatus3 = (BridgeStatus) c09571.L$2;
                    Socket socket5 = (Socket) c09571.L$1;
                    TcpRelayEngine tcpRelayEngine5 = (TcpRelayEngine) c09571.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        t = obj;
                        str2 = str5;
                        objectRef3 = objectRef7;
                        socket3 = socket5;
                        tcpRelayEngine2 = tcpRelayEngine5;
                        i = i5;
                        objectRef4.element = t;
                        if (!bridgeStatus3.getViaSrv()) {
                        }
                    } catch (IOException e13) {
                        e = e13;
                        tcpRelayEngine = tcpRelayEngine5;
                        objectRef2 = objectRef7;
                        bridgeStatus2 = bridgeStatus3;
                        socket2 = socket5;
                        tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef2.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet5222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket22222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Exception e14) {
                        e = e14;
                        tcpRelayEngine = tcpRelayEngine5;
                        objectRef = objectRef7;
                        bridgeStatus2 = bridgeStatus3;
                        socket2 = socket5;
                        tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet22222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket32222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Throwable th7) {
                        th = th7;
                        tcpRelayEngine = tcpRelayEngine5;
                        sb = objectRef7;
                        bridgeStatus2 = bridgeStatus3;
                        socket2 = socket5;
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(sb.element);
                        tcpRelayEngine.closeQuietly((Socket) sb.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet32222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket42222 = tcpRelayEngine.serverSocket;
                        tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket42222 != null && !serverSocket42222.isClosed()) && iDecrementAndGet32222 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet32222, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                        tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet32222, 0) + ")");
                        throw th;
                    }
                    logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                    return Unit.INSTANCE;
                case 2:
                    i3 = c09571.I$0;
                    Ref.ObjectRef objectRef8 = (Ref.ObjectRef) c09571.L$3;
                    bridgeStatus4 = (BridgeStatus) c09571.L$2;
                    Socket socket6 = (Socket) c09571.L$1;
                    tcpRelayEngine3 = (TcpRelayEngine) c09571.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        objectRef3 = objectRef8;
                        socket2 = socket6;
                        serverSocket = tcpRelayEngine3.serverSocket;
                        if (serverSocket == null) {
                            if (serverSocket == null && !serverSocket.isClosed()) {
                            }
                            c09604 = tcpRelayEngine3.new C09604(socket2, objectRef3, null);
                            c09571.L$0 = tcpRelayEngine3;
                            c09571.L$1 = socket2;
                            c09571.L$2 = bridgeStatus4;
                            c09571.L$3 = objectRef3;
                            c09571.label = 3;
                            if (CoroutineScopeKt.coroutineScope(c09604, c09571) != coroutine_suspended) {
                            }
                            break;
                        }
                    } catch (IOException e15) {
                        e = e15;
                        objectRef2 = objectRef8;
                        bridgeStatus2 = bridgeStatus4;
                        socket2 = socket6;
                        tcpRelayEngine = tcpRelayEngine3;
                        tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef2.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet52222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket222222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Exception e16) {
                        e = e16;
                        objectRef = objectRef8;
                        bridgeStatus2 = bridgeStatus4;
                        socket2 = socket6;
                        tcpRelayEngine = tcpRelayEngine3;
                        tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet222222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket322222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Throwable th8) {
                        th = th8;
                        sb = objectRef8;
                        bridgeStatus2 = bridgeStatus4;
                        socket2 = socket6;
                        tcpRelayEngine = tcpRelayEngine3;
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(sb.element);
                        tcpRelayEngine.closeQuietly((Socket) sb.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet322222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket422222 = tcpRelayEngine.serverSocket;
                        tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket422222 != null && !serverSocket422222.isClosed()) && iDecrementAndGet322222 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet322222, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                        tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet322222, 0) + ")");
                        throw th;
                    }
                    logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                    return Unit.INSTANCE;
                case 3:
                    sb = (Ref.ObjectRef) c09571.L$3;
                    BridgeStatus bridgeStatus8 = (BridgeStatus) c09571.L$2;
                    socket2 = (Socket) c09571.L$1;
                    tcpRelayEngine = (TcpRelayEngine) c09571.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        bridgeStatus5 = bridgeStatus8;
                        objectRef5 = sb;
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef5.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef5.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet42 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket52 = tcpRelayEngine.serverSocket;
                        if (serverSocket52 == null) {
                            if (serverSocket52 == null && !serverSocket52.isClosed()) {
                                tcpRelayEngine.publishState(bridgeStatus5.copy((254 & 1) != 0 ? bridgeStatus5.state : ((serverSocket52 == null && !serverSocket52.isClosed()) || iDecrementAndGet42 != 0) ? tcpRelayEngine._status.getValue().getState() : BridgeState.LISTENING, (254 & 2) != 0 ? bridgeStatus5.remoteHost : null, (254 & 4) != 0 ? bridgeStatus5.remotePort : 0, (254 & 8) != 0 ? bridgeStatus5.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus5.viaSrv : false, (254 & 32) != 0 ? bridgeStatus5.localHost : null, (254 & 64) != 0 ? bridgeStatus5.localPort : 0, (254 & 128) != 0 ? bridgeStatus5.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet42, 0), (254 & 256) != 0 ? bridgeStatus5.lastError : null));
                                logBuffer = tcpRelayEngine.log;
                                iCoerceAtLeast = RangesKt.coerceAtLeast(iDecrementAndGet42, 0);
                                sb = new StringBuilder();
                            }
                            break;
                        }
                    } catch (IOException e17) {
                        e = e17;
                        bridgeStatus2 = bridgeStatus8;
                        objectRef2 = sb;
                        tcpRelayEngine.log.log("Remote connection failed: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef2.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef2.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet522222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket2222222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Exception e18) {
                        e = e18;
                        bridgeStatus2 = bridgeStatus8;
                        objectRef = sb;
                        tcpRelayEngine.log.log("Connection error: " + e.getMessage());
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(objectRef.element);
                        tcpRelayEngine.closeQuietly((Socket) objectRef.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet2222222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket3222222 = tcpRelayEngine.serverSocket;
                        logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                        return Unit.INSTANCE;
                    } catch (Throwable th9) {
                        th = th9;
                        bridgeStatus2 = bridgeStatus8;
                        tcpRelayEngine.activeSockets.remove(socket2);
                        tcpRelayEngine.activeSockets.remove(sb.element);
                        tcpRelayEngine.closeQuietly((Socket) sb.element);
                        tcpRelayEngine.closeQuietly(socket2);
                        int iDecrementAndGet3222222 = tcpRelayEngine.activeConnections.decrementAndGet();
                        ServerSocket serverSocket4222222 = tcpRelayEngine.serverSocket;
                        tcpRelayEngine.publishState(bridgeStatus2.copy((254 & 1) != 0 ? bridgeStatus2.state : ((serverSocket4222222 != null && !serverSocket4222222.isClosed()) && iDecrementAndGet3222222 == 0) ? BridgeState.LISTENING : tcpRelayEngine._status.getValue().getState(), (254 & 2) != 0 ? bridgeStatus2.remoteHost : null, (254 & 4) != 0 ? bridgeStatus2.remotePort : 0, (254 & 8) != 0 ? bridgeStatus2.resolvedRemoteHost : null, (254 & 16) != 0 ? bridgeStatus2.viaSrv : false, (254 & 32) != 0 ? bridgeStatus2.localHost : null, (254 & 64) != 0 ? bridgeStatus2.localPort : 0, (254 & 128) != 0 ? bridgeStatus2.activeConnections : RangesKt.coerceAtLeast(iDecrementAndGet3222222, 0), (254 & 256) != 0 ? bridgeStatus2.lastError : null));
                        tcpRelayEngine.log.log("Connection closed (active: " + RangesKt.coerceAtLeast(iDecrementAndGet3222222, 0) + ")");
                        throw th;
                    }
                    logBuffer.log(sb.append("Connection closed (active: ").append(iCoerceAtLeast).append(")").toString());
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th10) {
            th = th10;
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "Ljava/net/Socket;", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$2", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$2 */
    static final class C09582 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Socket>, Object> {
        final /* synthetic */ int $dialPort;
        final /* synthetic */ String $resolvedRemote;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09582(String str, int i, Continuation<? super C09582> continuation) {
            super(2, continuation);
            this.$resolvedRemote = str;
            this.$dialPort = i;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C09582(this.$resolvedRemote, this.$dialPort, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Socket> continuation) {
            return ((C09582) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws IOException {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    Socket $this$invokeSuspend_u24lambda_u240 = new Socket();
                    $this$invokeSuspend_u24lambda_u240.connect(new InetSocketAddress(this.$resolvedRemote, this.$dialPort), 8000);
                    $this$invokeSuspend_u24lambda_u240.setTcpNoDelay(true);
                    $this$invokeSuspend_u24lambda_u240.setKeepAlive(true);
                    return $this$invokeSuspend_u24lambda_u240;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$3", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$3 */
    static final class C09593 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Socket $client;
        final /* synthetic */ int $dialPort;
        final /* synthetic */ String $handshakeHost;
        final /* synthetic */ Ref.ObjectRef<Socket> $remote;
        int label;
        final /* synthetic */ TcpRelayEngine this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09593(Socket socket, Ref.ObjectRef<Socket> objectRef, String str, int i, TcpRelayEngine tcpRelayEngine, Continuation<? super C09593> continuation) {
            super(2, continuation);
            this.$client = socket;
            this.$remote = objectRef;
            this.$handshakeHost = str;
            this.$dialPort = i;
            this.this$0 = tcpRelayEngine;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C09593(this.$client, this.$remote, this.$handshakeHost, this.$dialPort, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09593) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws IOException {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    HandshakeRewriter handshakeRewriter = HandshakeRewriter.INSTANCE;
                    InputStream inputStream = this.$client.getInputStream();
                    Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                    OutputStream outputStream = this.$remote.element.getOutputStream();
                    Intrinsics.checkNotNullExpressionValue(outputStream, "getOutputStream(...)");
                    String str = this.$handshakeHost;
                    int i = this.$dialPort;
                    final TcpRelayEngine tcpRelayEngine = this.this$0;
                    handshakeRewriter.rewriteHandshake(inputStream, outputStream, str, i, new Function1<String, Unit>() { // from class: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine.handleConnection.3.1
                        {
                            super(1);
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ Unit invoke(String str2) {
                            invoke2(str2);
                            return Unit.INSTANCE;
                        }

                        /* renamed from: invoke, reason: avoid collision after fix types in other method */
                        public final void invoke2(String msg) {
                            Intrinsics.checkNotNullParameter(msg, "msg");
                            tcpRelayEngine.getLog().log(msg);
                        }
                    });
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$4", m162f = "TcpRelayEngine.kt", m163i = {0, 0, 1}, m164l = {476, 362, 363}, m165m = "invokeSuspend", m166n = {"c2r", "r2c", "r2c"}, m167s = {"L$0", "L$1", "L$0"})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$4 */
    static final class C09604 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Socket $client;
        final /* synthetic */ Ref.ObjectRef<Socket> $remote;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09604(Socket socket, Ref.ObjectRef<Socket> objectRef, Continuation<? super C09604> continuation) {
            super(2, continuation);
            this.$client = socket;
            this.$remote = objectRef;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C09604 c09604 = TcpRelayEngine.this.new C09604(this.$client, this.$remote, continuation);
            c09604.L$0 = obj;
            return c09604;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09604) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00ca A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:16:0x00cb  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x00da A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00db  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C09604 c09604;
            Job r2c;
            Job c2r;
            Job r2c2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c09604 = this;
                    CoroutineScope $this$coroutineScope = (CoroutineScope) c09604.L$0;
                    Job c2r2 = BuildersKt__Builders_commonKt.launch$default($this$coroutineScope, null, null, new TcpRelayEngine$handleConnection$4$c2r$1(TcpRelayEngine.this, c09604.$client, c09604.$remote, null), 3, null);
                    Job r2c3 = BuildersKt__Builders_commonKt.launch$default($this$coroutineScope, null, null, new TcpRelayEngine$handleConnection$4$r2c$1(TcpRelayEngine.this, c09604.$remote, c09604.$client, null), 3, null);
                    SelectImplementation $this$select_u24lambda_u241$iv = new SelectImplementation(c09604.getContext());
                    SelectImplementation $this$invokeSuspend_u24lambda_u240 = $this$select_u24lambda_u241$iv;
                    $this$invokeSuspend_u24lambda_u240.invoke(c2r2.getOnJoin(), new TcpRelayEngine$handleConnection$4$1$1(null));
                    $this$invokeSuspend_u24lambda_u240.invoke(r2c3.getOnJoin(), new TcpRelayEngine$handleConnection$4$1$2(null));
                    c09604.L$0 = c2r2;
                    c09604.L$1 = r2c3;
                    c09604.label = 1;
                    if ($this$select_u24lambda_u241$iv.doSelect(c09604) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    r2c = r2c3;
                    c2r = c2r2;
                    TcpRelayEngine.this.closeQuietly(c09604.$client);
                    TcpRelayEngine.this.closeQuietly(c09604.$remote.element);
                    c09604.L$0 = r2c;
                    c09604.L$1 = null;
                    c09604.label = 2;
                    if (c2r.join(c09604) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    r2c2 = r2c;
                    c09604.L$0 = null;
                    c09604.label = 3;
                    return r2c2.join(c09604) != coroutine_suspended ? coroutine_suspended : Unit.INSTANCE;
                case 1:
                    c09604 = this;
                    r2c = (Job) c09604.L$1;
                    c2r = (Job) c09604.L$0;
                    ResultKt.throwOnFailure($result);
                    TcpRelayEngine.this.closeQuietly(c09604.$client);
                    TcpRelayEngine.this.closeQuietly(c09604.$remote.element);
                    c09604.L$0 = r2c;
                    c09604.L$1 = null;
                    c09604.label = 2;
                    if (c2r.join(c09604) != coroutine_suspended) {
                    }
                    break;
                case 2:
                    c09604 = this;
                    r2c2 = (Job) c09604.L$0;
                    ResultKt.throwOnFailure($result);
                    c09604.L$0 = null;
                    c09604.label = 3;
                    if (r2c2.join(c09604) != coroutine_suspended) {
                    }
                    break;
                case 3:
                    ResultKt.throwOnFailure($result);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$copy$2", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$copy$2 */
    static final class C09562 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ InputStream $input;
        final /* synthetic */ OutputStream $output;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09562(InputStream inputStream, OutputStream outputStream, Continuation<? super C09562> continuation) {
            super(2, continuation);
            this.$input = inputStream;
            this.$output = outputStream;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C09562 c09562 = new C09562(this.$input, this.$output, continuation);
            c09562.L$0 = obj;
            return c09562;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09562) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws IOException {
            int read;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    CoroutineScope $this$withContext = (CoroutineScope) this.L$0;
                    byte[] buffer = new byte[16384];
                    while (JobKt.isActive($this$withContext.getCoroutineContext()) && (read = this.$input.read(buffer)) > 0) {
                        try {
                            this.$output.write(buffer, 0, read);
                            this.$output.flush();
                        } catch (SocketException e) {
                        } catch (IOException e2) {
                        }
                    }
                    try {
                        this.$output.flush();
                    } catch (Exception e3) {
                    }
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object copy(InputStream input, OutputStream output, Continuation<? super Unit> continuation) throws Throwable {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C09562(input, output, null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    /* compiled from: TcpRelayEngine.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$stop$2", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$stop$2 */
    static final class C09622 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        int label;

        C09622(Continuation<? super C09622> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C09622 c09622 = TcpRelayEngine.this.new C09622(continuation);
            c09622.L$0 = obj;
            return c09622;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09622) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Unit unit;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    TcpRelayEngine.this.getLog().log("Stopping bridge…");
                    TcpRelayEngine tcpRelayEngine = TcpRelayEngine.this;
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        ServerSocket serverSocket = tcpRelayEngine.serverSocket;
                        if (serverSocket != null) {
                            serverSocket.close();
                            unit = Unit.INSTANCE;
                        } else {
                            unit = null;
                        }
                        Result.m255constructorimpl(unit);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m255constructorimpl(ResultKt.createFailure(th));
                    }
                    TcpRelayEngine.this.serverSocket = null;
                    Job job = TcpRelayEngine.this.listenJob;
                    if (job != null) {
                        Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
                    }
                    TcpRelayEngine.this.listenJob = null;
                    CoroutineScope coroutineScope = TcpRelayEngine.this.connectionsScope;
                    if (coroutineScope != null) {
                        CoroutineScopeKt.cancel$default(coroutineScope, null, 1, null);
                    }
                    TcpRelayEngine.this.connectionsScope = null;
                    Set set = TcpRelayEngine.this.activeSockets;
                    Intrinsics.checkNotNullExpressionValue(set, "access$getActiveSockets$p(...)");
                    TcpRelayEngine tcpRelayEngine2 = TcpRelayEngine.this;
                    synchronized (set) {
                        Set set2 = tcpRelayEngine2.activeSockets;
                        Intrinsics.checkNotNullExpressionValue(set2, "access$getActiveSockets$p(...)");
                        Iterable snap = CollectionsKt.toList(set2);
                        tcpRelayEngine2.activeSockets.clear();
                        Iterable $this$forEach$iv = snap;
                        for (Object element$iv : $this$forEach$iv) {
                            Socket it = (Socket) element$iv;
                            tcpRelayEngine2.closeQuietly(it);
                        }
                        Unit unit2 = Unit.INSTANCE;
                    }
                    TcpRelayEngine.this.runningConfig = null;
                    TcpRelayEngine.this.activeConnections.set(0);
                    TcpRelayEngine.this._status.setValue(new BridgeStatus(BridgeState.STOPPED, null, 0, null, false, null, 0, 0, null, TypedValues.PositionType.TYPE_POSITION_TYPE, null));
                    TcpRelayEngine.this.getLog().log("Bridge stopped");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final Object stop(Continuation<? super Unit> continuation) throws Throwable {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C09622(null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    public final boolean isRunning() {
        ServerSocket serverSocket = this.serverSocket;
        return (serverSocket == null || serverSocket.isClosed()) ? false : true;
    }

    private final void publishState(BridgeStatus s) {
        this._status.setValue(s.getLastError() == null ? s.copy((254 & 1) != 0 ? s.state : null, (254 & 2) != 0 ? s.remoteHost : null, (254 & 4) != 0 ? s.remotePort : 0, (254 & 8) != 0 ? s.resolvedRemoteHost : null, (254 & 16) != 0 ? s.viaSrv : false, (254 & 32) != 0 ? s.localHost : null, (254 & 64) != 0 ? s.localPort : 0, (254 & 128) != 0 ? s.activeConnections : 0, (254 & 256) != 0 ? s.lastError : this._status.getValue().getLastError()) : s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void closeQuietly(Socket s) {
        Unit unit;
        try {
            Result.Companion companion = Result.INSTANCE;
            TcpRelayEngine tcpRelayEngine = this;
            if (s != null) {
                s.close();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m255constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m255constructorimpl(ResultKt.createFailure(th));
        }
    }
}
