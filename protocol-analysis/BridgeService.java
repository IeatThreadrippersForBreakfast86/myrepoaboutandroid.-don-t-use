package com.ninjatech.minecraftlanbridge.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.ninjatech.minecraftlanbridge.C0948R;
import com.ninjatech.minecraftlanbridge.config.BridgeConfig;
import com.ninjatech.minecraftlanbridge.p001ui.MainActivity;
import com.ninjatech.minecraftlanbridge.relay.BridgeState;
import com.ninjatech.minecraftlanbridge.relay.BridgeStatus;
import com.ninjatech.minecraftlanbridge.relay.HttpProxyServer;
import com.ninjatech.minecraftlanbridge.relay.RelayStartException;
import com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine;
import com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine;
import com.ninjatech.minecraftlanbridge.util.NetworkUtils;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.flow.FlowKt;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord;

/* compiled from: BridgeService.kt */
@Metadata(m145d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\u0018\u0000 )2\u00020\u0001:\u0001)B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\u0014\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0012H\u0016J\b\u0010\u001a\u001a\u00020\u0012H\u0016J\"\u0010\u001b\u001a\u00020\u001c2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001cH\u0016J7\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00142\b\u0010!\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\u001cH\u0002¢\u0006\u0002\u0010%J\u0010\u0010&\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010'\u001a\u00020\u0012H\u0002J\u0010\u0010(\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/service/BridgeService;", "Landroid/app/Service;", "()V", "engine", "Lcom/ninjatech/minecraftlanbridge/relay/TcpRelayEngine;", "proxy", "Lcom/ninjatech/minecraftlanbridge/relay/HttpProxyServer;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "statusCollectorJob", "Lkotlinx/coroutines/Job;", "udpRelay", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine;", "buildNotification", "Landroid/app/Notification;", NotificationCompat.CATEGORY_STATUS, "Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;", "createNotificationChannel", "", "lanHost", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "startBridge", "host", "remotePort", "localPort", "proxyPort", "udpRelayPort", "(Ljava/lang/String;Ljava/lang/Integer;III)V", "startInForeground", "stopBridge", "updateNotification", "Companion", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes4.dex */
public final class BridgeService extends Service {
    public static final String ACTION_START = "com.ninjatech.minecraftlanbridge.START";
    public static final String ACTION_STOP = "com.ninjatech.minecraftlanbridge.STOP";
    public static final String ACTION_STOP_FROM_NOTIF = "com.ninjatech.minecraftlanbridge.STOP_NOTIF";
    private static final String CHANNEL_ID = "bridge_service_channel";

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String EXTRA_LOCAL_PORT = "local_port";
    public static final String EXTRA_PROXY_PORT = "proxy_port";
    public static final String EXTRA_REMOTE_HOST = "remote_host";
    public static final String EXTRA_REMOTE_PORT = "remote_port";
    public static final String EXTRA_UDP_RELAY_PORT = "udp_relay_port";
    public static final int NOTIF_ID = 4242;
    private Job statusCollectorJob;
    private final TcpRelayEngine engine = TcpRelayEngine.INSTANCE.getShared();
    private final HttpProxyServer proxy = HttpProxyServer.INSTANCE.getShared();
    private final UdpRelayEngine udpRelay = UdpRelayEngine.INSTANCE.getShared();
    private final CoroutineScope serviceScope = CoroutineScopeKt.CoroutineScope(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null).plus(Dispatchers.getMain().getImmediate()));

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        this.statusCollectorJob = BuildersKt__Builders_commonKt.launch$default(this.serviceScope, null, null, new C09671(null), 3, null);
    }

    /* compiled from: BridgeService.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.service.BridgeService$onCreate$1", m162f = "BridgeService.kt", m163i = {}, m164l = {Type.CDS}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.service.BridgeService$onCreate$1 */
    static final class C09671 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09671(Continuation<? super C09671> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return BridgeService.this.new C09671(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09671) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: BridgeService.kt */
        @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.service.BridgeService$onCreate$1$1", m162f = "BridgeService.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.service.BridgeService$onCreate$1$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<BridgeStatus, Continuation<? super Unit>, Object> {
            /* synthetic */ Object L$0;
            int label;
            final /* synthetic */ BridgeService this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(BridgeService bridgeService, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = bridgeService;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.this$0, continuation);
                anonymousClass1.L$0 = obj;
                return anonymousClass1;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(BridgeStatus bridgeStatus, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(bridgeStatus, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        BridgeStatus it = (BridgeStatus) this.L$0;
                        this.this$0.updateNotification(it);
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (FlowKt.collectLatest(BridgeService.this.engine.getStatus(), new AnonymousClass1(BridgeService.this, null), this) != coroutine_suspended) {
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

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;
        if (action != null) {
            switch (action.hashCode()) {
                case -1412188668:
                    if (action.equals(ACTION_START)) {
                        String host = intent.getStringExtra(EXTRA_REMOTE_HOST);
                        if (host == null) {
                            return 2;
                        }
                        int remotePortRaw = intent.getIntExtra(EXTRA_REMOTE_PORT, -1);
                        Integer remotePort = remotePortRaw != -1 ? Integer.valueOf(remotePortRaw) : null;
                        int localPort = intent.getIntExtra(EXTRA_LOCAL_PORT, -1);
                        int proxyPort = intent.getIntExtra(EXTRA_PROXY_PORT, -1);
                        int udpRelayPort = intent.getIntExtra(EXTRA_UDP_RELAY_PORT, -2);
                        startBridge(host, remotePort, localPort, proxyPort, udpRelayPort);
                        return 1;
                    }
                    return 1;
                case -1264906223:
                    if (!action.equals(ACTION_STOP_FROM_NOTIF)) {
                        return 1;
                    }
                    break;
                case -184101376:
                    if (!action.equals(ACTION_STOP)) {
                        return 1;
                    }
                    break;
                default:
                    return 1;
            }
            stopBridge();
            return 1;
        }
        return 1;
    }

    private final void startBridge(String host, Integer remotePort, int localPort, int proxyPort, int udpRelayPort) {
        startInForeground(this.engine.getStatus().getValue());
        BuildersKt__Builders_commonKt.launch$default(this.serviceScope, null, null, new C09691(host, remotePort, localPort, this, proxyPort, udpRelayPort, null), 3, null);
    }

    /* compiled from: BridgeService.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1", m162f = "BridgeService.kt", m163i = {1}, m164l = {99, 112, WKSRecord.Service.NETBIOS_SSN}, m165m = "invokeSuspend", m166n = {"lanIp"}, m167s = {"L$0"})
    /* renamed from: com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1 */
    static final class C09691 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $host;
        final /* synthetic */ int $localPort;
        final /* synthetic */ int $proxyPort;
        final /* synthetic */ Integer $remotePort;
        final /* synthetic */ int $udpRelayPort;
        Object L$0;
        int label;
        final /* synthetic */ BridgeService this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09691(String str, Integer num, int i, BridgeService bridgeService, int i2, int i3, Continuation<? super C09691> continuation) {
            super(2, continuation);
            this.$host = str;
            this.$remotePort = num;
            this.$localPort = i;
            this.this$0 = bridgeService;
            this.$proxyPort = i2;
            this.$udpRelayPort = i3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C09691(this.$host, this.$remotePort, this.$localPort, this.this$0, this.$proxyPort, this.$udpRelayPort, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09691) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 6, insn: 0x00f3: IGET (r9 I:com.ninjatech.minecraftlanbridge.service.BridgeService) = 
          (r6 I:com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1 A[D('this' com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1)])
         A[Catch: Exception -> 0x0041, RelayStartException -> 0x0044, TRY_ENTER] (LINE:127) com.ninjatech.minecraftlanbridge.service.BridgeService.startBridge.1.this$0 com.ninjatech.minecraftlanbridge.service.BridgeService, block:B:47:0x00f3 */
        /* JADX WARN: Not initialized variable reg: 6, insn: 0x0118: IGET (r9 I:com.ninjatech.minecraftlanbridge.service.BridgeService) = 
          (r6 I:com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1 A[D('this' com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1)])
         A[Catch: Exception -> 0x0041, RelayStartException -> 0x0044] (LINE:125) com.ninjatech.minecraftlanbridge.service.BridgeService.startBridge.1.this$0 com.ninjatech.minecraftlanbridge.service.BridgeService, block:B:48:0x0118 */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00a6 A[Catch: Exception -> 0x0033, RelayStartException -> 0x0036, TRY_ENTER, TryCatch #7 {RelayStartException -> 0x0036, Exception -> 0x0033, blocks: (B:14:0x002e, B:33:0x00a6, B:39:0x00bd, B:41:0x00d7, B:43:0x00db), top: B:77:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x0141 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Object] */
        /* JADX WARN: Type inference failed for: r1v10 */
        /* JADX WARN: Type inference failed for: r1v15 */
        /* JADX WARN: Type inference failed for: r1v20 */
        /* JADX WARN: Type inference failed for: r1v21 */
        /* JADX WARN: Type inference failed for: r1v5 */
        /* JADX WARN: Type inference failed for: r6v2, types: [com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1] */
        /* JADX WARN: Type inference failed for: r6v3, types: [com.ninjatech.minecraftlanbridge.service.BridgeService$startBridge$1] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ?? r6;
            ?? r62;
            C09691 c09691;
            String localHost;
            Object obj2;
            C09691 c096912;
            C09691 c096913;
            C09691 c096914;
            int i;
            ?? coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                try {
                    try {
                    } catch (RelayStartException e) {
                        e = e;
                        c09691 = coroutine_suspended;
                        c09691.this$0.engine.getLog().log("Startup failed: " + e.getMessage());
                        c09691.this$0.stopBridge();
                        return Unit.INSTANCE;
                    } catch (Exception e2) {
                        e = e2;
                        c09691 = coroutine_suspended;
                        c09691.this$0.engine.getLog().log("Startup error: " + e.getMessage());
                        c09691.this$0.stopBridge();
                        return Unit.INSTANCE;
                    }
                } catch (RelayStartException e3) {
                    r62.this$0.engine.getLog().log("Auth proxy failed to start: " + e3.getMessage());
                    c09691 = r62;
                } catch (Exception e4) {
                    r6.this$0.engine.getLog().log("Auth proxy error: " + e4.getMessage());
                    c09691 = r6;
                }
            } catch (RelayStartException e5) {
                e = e5;
                c09691.this$0.engine.getLog().log("Startup failed: " + e.getMessage());
                c09691.this$0.stopBridge();
                return Unit.INSTANCE;
            } catch (Exception e6) {
                e = e6;
                c09691.this$0.engine.getLog().log("Startup error: " + e.getMessage());
                c09691.this$0.stopBridge();
                return Unit.INSTANCE;
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    C09691 c096915 = this;
                    obj2 = obj;
                    TcpRelayEngine.RelayConfig relayConfig = new TcpRelayEngine.RelayConfig(c096915.$host, c096915.$remotePort, c096915.$localPort, null);
                    relayConfig.setAppContext(c096915.this$0.getApplicationContext());
                    c096915.label = 1;
                    Object objStart = c096915.this$0.engine.start(relayConfig, c096915);
                    c096914 = c096915;
                    if (objStart == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    new BridgeConfig(c096914.this$0).save(c096914.$host, c096914.$remotePort, c096914.$localPort, c096914.$proxyPort, c096914.$udpRelayPort);
                    localHost = c096914.this$0.engine.getStatus().getValue().getLocalHost();
                    i = c096914.$proxyPort;
                    c09691 = c096914;
                    if (i > 0) {
                        HttpProxyServer httpProxyServer = c096914.this$0.proxy;
                        String str = localHost;
                        if (str.length() == 0) {
                            str = null;
                        }
                        String str2 = str;
                        int i2 = c096914.$proxyPort;
                        String strLanHost = c096914.this$0.lanHost(c096914.this$0.engine);
                        int i3 = c096914.$proxyPort;
                        int i4 = c096914.$localPort;
                        int i5 = c096914.$udpRelayPort > 0 ? c096914.$udpRelayPort : 0;
                        c096914.L$0 = localHost;
                        c096914.label = 2;
                        Object objStart2 = httpProxyServer.start(new HttpProxyServer.ProxyConfig(str2, i2, new HttpProxyServer.DiscoveryInfo(strLanHost, i3, i4, i5)), c096914);
                        c09691 = c096914;
                        if (objStart2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    if (c09691.$udpRelayPort > 0) {
                        try {
                            UdpRelayEngine udpRelayEngine = c09691.this$0.udpRelay;
                            String str3 = localHost;
                            if ((str3.length() == 0 ? 1 : null) != null) {
                                str3 = null;
                            }
                            c09691.L$0 = null;
                            c09691.label = 3;
                            if (udpRelayEngine.start(new UdpRelayEngine.UdpRelayConfig((String) str3, c09691.$udpRelayPort), c09691) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            c096913 = c09691;
                            obj = obj2;
                        } catch (RelayStartException e7) {
                            e = e7;
                            c096912 = c09691;
                            obj = obj2;
                            c096912.this$0.engine.getLog().log("UDP relay failed to start: " + e.getMessage());
                            coroutine_suspended = c096912;
                            return Unit.INSTANCE;
                        } catch (Exception e8) {
                            e = e8;
                            c096912 = c09691;
                            obj = obj2;
                            c096912.this$0.engine.getLog().log("UDP relay error: " + e.getMessage());
                            coroutine_suspended = c096912;
                            return Unit.INSTANCE;
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    c096914 = this;
                    obj2 = obj;
                    ResultKt.throwOnFailure(obj2);
                    new BridgeConfig(c096914.this$0).save(c096914.$host, c096914.$remotePort, c096914.$localPort, c096914.$proxyPort, c096914.$udpRelayPort);
                    localHost = c096914.this$0.engine.getStatus().getValue().getLocalHost();
                    i = c096914.$proxyPort;
                    c09691 = c096914;
                    if (i > 0) {
                    }
                    if (c09691.$udpRelayPort > 0) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    C09691 c096916 = this;
                    obj2 = obj;
                    localHost = (String) c096916.L$0;
                    ResultKt.throwOnFailure(obj2);
                    c09691 = c096916;
                    if (c09691.$udpRelayPort > 0) {
                    }
                    return Unit.INSTANCE;
                case 3:
                    c096912 = this;
                    obj = obj;
                    try {
                        ResultKt.throwOnFailure(obj);
                        c096913 = c096912;
                    } catch (RelayStartException e9) {
                        e = e9;
                        c096912.this$0.engine.getLog().log("UDP relay failed to start: " + e.getMessage());
                        coroutine_suspended = c096912;
                        return Unit.INSTANCE;
                    } catch (Exception e10) {
                        e = e10;
                        c096912.this$0.engine.getLog().log("UDP relay error: " + e.getMessage());
                        coroutine_suspended = c096912;
                        return Unit.INSTANCE;
                    }
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String lanHost(TcpRelayEngine engine) {
        String localHost = engine.getStatus().getValue().getLocalHost();
        if (localHost.length() == 0) {
            String bestLanIpv4 = NetworkUtils.INSTANCE.getBestLanIpv4();
            if (bestLanIpv4 == null) {
                bestLanIpv4 = "";
            }
            localHost = bestLanIpv4;
        }
        return localHost;
    }

    /* compiled from: BridgeService.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.service.BridgeService$stopBridge$1", m162f = "BridgeService.kt", m163i = {}, m164l = {170, 172, 173}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.service.BridgeService$stopBridge$1 */
    static final class C09701 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09701(Continuation<? super C09701> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return BridgeService.this.new C09701(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09701) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0073 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00ab A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00ac  */
        /* JADX WARN: Type inference failed for: r1v0, types: [int] */
        /* JADX WARN: Type inference failed for: r1v10 */
        /* JADX WARN: Type inference failed for: r1v11 */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v13 */
        /* JADX WARN: Type inference failed for: r1v14 */
        /* JADX WARN: Type inference failed for: r1v5 */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            Object objStop;
            C09701 c09701;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            C09701 c097012 = this.label;
            try {
                try {
                } catch (Exception e) {
                    BridgeService.this.engine.getLog().log("Proxy stop error: " + e.getMessage());
                }
            } catch (Exception e2) {
                BridgeService.this.engine.getLog().log("UDP relay stop error: " + e2.getMessage());
            }
            switch (c097012) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    C09701 c097013 = this;
                    c097013.label = 1;
                    Object objStop2 = BridgeService.this.udpRelay.stop(c097013);
                    c097012 = c097013;
                    if (objStop2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c097012.label = 2;
                    objStop = BridgeService.this.proxy.stop(c097012);
                    c097012 = c097012;
                    if (objStop == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c097012.label = 3;
                    if (BridgeService.this.engine.stop(c097012) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c09701 = c097012;
                    BridgeService.this.stopForeground(1);
                    BridgeService.this.stopSelf();
                    return Unit.INSTANCE;
                case 1:
                    c097012 = this;
                    ResultKt.throwOnFailure(obj);
                    c097012.label = 2;
                    objStop = BridgeService.this.proxy.stop(c097012);
                    c097012 = c097012;
                    if (objStop == coroutine_suspended) {
                    }
                    c097012.label = 3;
                    if (BridgeService.this.engine.stop(c097012) == coroutine_suspended) {
                    }
                    break;
                case 2:
                    c097012 = this;
                    ResultKt.throwOnFailure(obj);
                    c097012.label = 3;
                    if (BridgeService.this.engine.stop(c097012) == coroutine_suspended) {
                    }
                    break;
                case 3:
                    c09701 = this;
                    ResultKt.throwOnFailure(obj);
                    BridgeService.this.stopForeground(1);
                    BridgeService.this.stopSelf();
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void stopBridge() {
        BuildersKt__Builders_commonKt.launch$default(this.serviceScope, null, null, new C09701(null), 3, null);
    }

    private final void startInForeground(BridgeStatus status) {
        Notification notification = buildNotification(status);
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(NOTIF_ID, notification, 1);
        } else {
            startForeground(NOTIF_ID, notification);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateNotification(BridgeStatus status) {
        if (status.getState() == BridgeState.STOPPED) {
            return;
        }
        Object systemService = getSystemService("notification");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.NotificationManager");
        NotificationManager nm = (NotificationManager) systemService;
        nm.notify(NOTIF_ID, buildNotification(status));
    }

    private final Notification buildNotification(BridgeStatus status) {
        String text;
        if ((status.getLocalHost().length() > 0) && status.getLocalPort() != 0) {
            text = getString(C0948R.string.notif_text, new Object[]{status.getLocalHost(), Integer.valueOf(status.getLocalPort())});
        } else {
            text = getString(C0948R.string.app_name);
        }
        Intrinsics.checkNotNull(text);
        String subText = getString(C0948R.string.notif_text_conns, new Object[]{Integer.valueOf(status.getActiveConnections())});
        Intrinsics.checkNotNullExpressionValue(subText, "getString(...)");
        Intent openIntent = new Intent(this, (Class<?>) MainActivity.class);
        openIntent.setFlags(603979776);
        PendingIntent openPi = PendingIntent.getActivity(this, 0, openIntent, 201326592);
        Intent stopIntent = new Intent(this, (Class<?>) BridgeService.class);
        stopIntent.setAction(ACTION_STOP_FROM_NOTIF);
        PendingIntent stopPi = PendingIntent.getService(this, 1, stopIntent, 201326592);
        Notification notificationBuild = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getString(C0948R.string.notif_title)).setContentText(text).setSubText(subText).setSmallIcon(C0948R.drawable.ic_bridge).setContentIntent(openPi).addAction(C0948R.drawable.ic_bridge, getString(C0948R.string.notif_stop), stopPi).setOngoing(true).setOnlyAlertOnce(true).setShowWhen(false).setCategory(NotificationCompat.CATEGORY_SERVICE).build();
        Intrinsics.checkNotNullExpressionValue(notificationBuild, "build(...)");
        return notificationBuild;
    }

    private final void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(C0948R.string.notif_channel_name), 2);
            channel.setDescription(getString(C0948R.string.notif_channel_desc));
            channel.setShowBadge(false);
            Object systemService = getSystemService("notification");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.NotificationManager");
            NotificationManager nm = (NotificationManager) systemService;
            nm.createNotificationChannel(channel);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        Job job = this.statusCollectorJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        BuildersKt__Builders_commonKt.launch$default(this.serviceScope, null, null, new C09681(null), 3, null);
        CoroutineScopeKt.cancel$default(this.serviceScope, null, 1, null);
    }

    /* compiled from: BridgeService.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.service.BridgeService$onDestroy$1", m162f = "BridgeService.kt", m163i = {}, m164l = {261, 262, 263}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.service.BridgeService$onDestroy$1 */
    static final class C09681 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09681(Continuation<? super C09681> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return BridgeService.this.new C09681(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09681) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:24:0x004e A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0062 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0063  */
        /* JADX WARN: Type inference failed for: r1v10 */
        /* JADX WARN: Type inference failed for: r1v11 */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v5 */
        /* JADX WARN: Type inference failed for: r1v8 */
        /* JADX WARN: Type inference failed for: r1v9 */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            Object objStop;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            C09681 c09681 = this.label;
            switch (c09681) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    C09681 c096812 = this;
                    c096812.label = 1;
                    Object objStop2 = BridgeService.this.udpRelay.stop(c096812);
                    c09681 = c096812;
                    if (objStop2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c09681.label = 2;
                    objStop = BridgeService.this.proxy.stop(c09681);
                    c09681 = c09681;
                    if (objStop == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c09681.label = 3;
                    return BridgeService.this.engine.stop(c09681) == coroutine_suspended ? coroutine_suspended : Unit.INSTANCE;
                case 1:
                    c09681 = this;
                    ResultKt.throwOnFailure(obj);
                    c09681.label = 2;
                    objStop = BridgeService.this.proxy.stop(c09681);
                    c09681 = c09681;
                    if (objStop == coroutine_suspended) {
                    }
                    c09681.label = 3;
                    if (BridgeService.this.engine.stop(c09681) == coroutine_suspended) {
                    }
                    break;
                case 2:
                    c09681 = this;
                    ResultKt.throwOnFailure(obj);
                    c09681.label = 3;
                    if (BridgeService.this.engine.stop(c09681) == coroutine_suspended) {
                    }
                    break;
                case 3:
                    ResultKt.throwOnFailure(obj);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: BridgeService.kt */
    @Metadata(m145d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J?\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00042\b\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u000e2\b\b\u0002\u0010\u0017\u001a\u00020\u000e¢\u0006\u0002\u0010\u0018J\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/service/BridgeService$Companion;", "", "()V", "ACTION_START", "", "ACTION_STOP", "ACTION_STOP_FROM_NOTIF", "CHANNEL_ID", "EXTRA_LOCAL_PORT", "EXTRA_PROXY_PORT", "EXTRA_REMOTE_HOST", "EXTRA_REMOTE_PORT", "EXTRA_UDP_RELAY_PORT", "NOTIF_ID", "", "start", "", "context", "Landroid/content/Context;", "host", "remotePort", "localPort", "proxyPort", "udpRelayPort", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/Integer;III)V", "stop", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ void start$default(Companion companion, Context context, String str, Integer num, int i, int i2, int i3, int i4, Object obj) {
            if ((i4 & 32) != 0) {
                i3 = BridgeConfig.DEFAULT_UDP_RELAY_PORT;
            }
            companion.start(context, str, num, i, i2, i3);
        }

        public final void start(Context context, String host, Integer remotePort, int localPort, int proxyPort, int udpRelayPort) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(host, "host");
            Intent intent = new Intent(context, (Class<?>) BridgeService.class);
            intent.setAction(BridgeService.ACTION_START);
            intent.putExtra(BridgeService.EXTRA_REMOTE_HOST, host);
            intent.putExtra(BridgeService.EXTRA_REMOTE_PORT, remotePort != null ? remotePort.intValue() : -1);
            intent.putExtra(BridgeService.EXTRA_LOCAL_PORT, localPort);
            intent.putExtra(BridgeService.EXTRA_PROXY_PORT, proxyPort);
            intent.putExtra(BridgeService.EXTRA_UDP_RELAY_PORT, udpRelayPort);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }

        public final void stop(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(context, (Class<?>) BridgeService.class);
            intent.setAction(BridgeService.ACTION_STOP);
            context.startService(intent);
        }
    }
}
