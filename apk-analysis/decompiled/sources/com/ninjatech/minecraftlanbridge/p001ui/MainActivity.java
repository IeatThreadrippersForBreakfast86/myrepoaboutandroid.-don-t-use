package com.ninjatech.minecraftlanbridge.p001ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwnerKt;
import com.google.android.material.textfield.TextInputEditText;
import com.ninjatech.minecraftlanbridge.C0948R;
import com.ninjatech.minecraftlanbridge.config.BridgeConfig;
import com.ninjatech.minecraftlanbridge.databinding.ActivityMainBinding;
import com.ninjatech.minecraftlanbridge.relay.BridgeState;
import com.ninjatech.minecraftlanbridge.relay.BridgeStatus;
import com.ninjatech.minecraftlanbridge.relay.HttpProxyServer;
import com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine;
import com.ninjatech.minecraftlanbridge.relay.UdpRelayEngine;
import com.ninjatech.minecraftlanbridge.service.BridgeService;
import com.ninjatech.minecraftlanbridge.util.NetworkUtils;
import com.ninjatech.minecraftlanbridge.util.SrvResolver;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p002io.CloseableKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.FlowKt;
import org.xbill.DNS.WKSRecord;

/* compiled from: MainActivity.kt */
@Metadata(m145d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\u0012\u0010\u0011\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u000fH\u0014J\b\u0010\u0015\u001a\u00020\u000fH\u0002J\b\u0010\u0016\u001a\u00020\u000fH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0002J\u0016\u0010\u0018\u001a\u00020\u000f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\r0\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u001c\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020 0\u001f2\u0006\u0010!\u001a\u00020\"H\u0002J\u0010\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020 H\u0002J\u0018\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\r2\u0006\u0010(\u001a\u00020 H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/ninjatech/minecraftlanbridge/databinding/ActivityMainBinding;", "config", "Lcom/ninjatech/minecraftlanbridge/config/BridgeConfig;", "getConfig", "()Lcom/ninjatech/minecraftlanbridge/config/BridgeConfig;", "config$delegate", "Lkotlin/Lazy;", "notifPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "clearInputErrors", "", "loadConfigIntoUi", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onStartClicked", "onStopClicked", "onTestClicked", "renderLog", "lines", "", "renderStatus", NotificationCompat.CATEGORY_STATUS, "Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;", "stateVisual", "Lkotlin/Pair;", "", "state", "Lcom/ninjatech/minecraftlanbridge/relay/BridgeState;", "statusDotColor", "colorRes", "testRemoteConnectivity", "", "host", "port", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes6.dex */
public final class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    /* renamed from: config$delegate, reason: from kotlin metadata */
    private final Lazy config = LazyKt.lazy(new Function0<BridgeConfig>() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$config$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final BridgeConfig invoke() {
            return new BridgeConfig(this.this$0);
        }
    });
    private final ActivityResultLauncher<String> notifPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$$ExternalSyntheticLambda4
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.notifPermissionLauncher$lambda$0(((Boolean) obj).booleanValue());
        }
    });

    /* compiled from: MainActivity.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[BridgeState.values().length];
            try {
                iArr[BridgeState.STOPPED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[BridgeState.STARTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[BridgeState.LISTENING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[BridgeState.RUNNING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[BridgeState.CONNECTING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[BridgeState.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private final BridgeConfig getConfig() {
        return (BridgeConfig) this.config.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void notifPermissionLauncher$lambda$0(boolean granted) {
        if (!granted) {
            TcpRelayEngine.INSTANCE.getShared().getLog().log("Notification permission denied — service may be harder to manage");
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBindingInflate = ActivityMainBinding.inflate(getLayoutInflater());
        Intrinsics.checkNotNullExpressionValue(activityMainBindingInflate, "inflate(...)");
        this.binding = activityMainBindingInflate;
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        setContentView(activityMainBinding.getRoot());
        loadConfigIntoUi();
        ActivityMainBinding activityMainBinding2 = this.binding;
        if (activityMainBinding2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding2 = null;
        }
        activityMainBinding2.btnStart.setOnClickListener(new View.OnClickListener() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$1(this.f$0, view);
            }
        });
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        activityMainBinding3.btnStop.setOnClickListener(new View.OnClickListener() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$2(this.f$0, view);
            }
        });
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        activityMainBinding4.btnTest.setOnClickListener(new View.OnClickListener() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$lambda$3(this.f$0, view);
            }
        });
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, null, new C09714(null), 3, null);
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, null, new C09725(null), 3, null);
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, null, new C09736(null), 3, null);
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, null, new C09747(null), 3, null);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity.onCreate.8
            {
                super(true);
            }

            @Override // androidx.activity.OnBackPressedCallback
            public void handleOnBackPressed() {
                MainActivity.this.moveTaskToBack(true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$1(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onStartClicked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$2(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onStopClicked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$3(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onTestClicked();
    }

    /* compiled from: MainActivity.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$4", m162f = "MainActivity.kt", m163i = {}, m164l = {71}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$4 */
    static final class C09714 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09714(Continuation<? super C09714> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new C09714(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09714) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: MainActivity.kt */
        @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$4$1", m162f = "MainActivity.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$4$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<BridgeStatus, Continuation<? super Unit>, Object> {
            /* synthetic */ Object L$0;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
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
                        this.this$0.renderStatus(it);
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
                    if (FlowKt.collectLatest(TcpRelayEngine.INSTANCE.getShared().getStatus(), new AnonymousClass1(MainActivity.this, null), this) != coroutine_suspended) {
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

    /* compiled from: MainActivity.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$5", m162f = "MainActivity.kt", m163i = {}, m164l = {WKSRecord.Service.NETRJS_4}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$5 */
    static final class C09725 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09725(Continuation<? super C09725> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new C09725(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09725) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: MainActivity.kt */
        @Metadata(m145d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "", ""}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$5$1", m162f = "MainActivity.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$5$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<List<? extends String>, Continuation<? super Unit>, Object> {
            /* synthetic */ Object L$0;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.this$0, continuation);
                anonymousClass1.L$0 = obj;
                return anonymousClass1;
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Object invoke(List<? extends String> list, Continuation<? super Unit> continuation) {
                return invoke2((List<String>) list, continuation);
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Object invoke2(List<String> list, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(list, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        List it = (List) this.L$0;
                        this.this$0.renderLog(it);
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
                    if (FlowKt.collectLatest(TcpRelayEngine.INSTANCE.getShared().getLog().getLines(), new AnonymousClass1(MainActivity.this, null), this) != coroutine_suspended) {
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

    /* compiled from: MainActivity.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$6", m162f = "MainActivity.kt", m163i = {}, m164l = {WKSRecord.Protocol.WB_MON}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$6 */
    static final class C09736 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09736(Continuation<? super C09736> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new C09736(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09736) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: MainActivity.kt */
        @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "Lcom/ninjatech/minecraftlanbridge/relay/HttpProxyServer$ProxyStatus;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$6$1", m162f = "MainActivity.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$6$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<HttpProxyServer.ProxyStatus, Continuation<? super Unit>, Object> {
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(HttpProxyServer.ProxyStatus proxyStatus, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(proxyStatus, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        this.this$0.renderStatus(TcpRelayEngine.INSTANCE.getShared().getStatus().getValue());
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
                    if (FlowKt.collectLatest(HttpProxyServer.INSTANCE.getShared().getStatus(), new AnonymousClass1(MainActivity.this, null), this) != coroutine_suspended) {
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

    /* compiled from: MainActivity.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$7", m162f = "MainActivity.kt", m163i = {}, m164l = {82}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$7 */
    static final class C09747 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        C09747(Continuation<? super C09747> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new C09747(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09747) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: MainActivity.kt */
        @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "Lcom/ninjatech/minecraftlanbridge/relay/UdpRelayEngine$UdpRelayStatus;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$7$1", m162f = "MainActivity.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onCreate$7$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<UdpRelayEngine.UdpRelayStatus, Continuation<? super Unit>, Object> {
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(UdpRelayEngine.UdpRelayStatus udpRelayStatus, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(udpRelayStatus, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        this.this$0.renderStatus(TcpRelayEngine.INSTANCE.getShared().getStatus().getValue());
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
                    if (FlowKt.collectLatest(UdpRelayEngine.INSTANCE.getShared().getStatus(), new AnonymousClass1(MainActivity.this, null), this) != coroutine_suspended) {
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

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        renderStatus(TcpRelayEngine.INSTANCE.getShared().getStatus().getValue());
    }

    private final void loadConfigIntoUi() {
        String string;
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.etRemoteHost.setText(getConfig().getRemoteHost());
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        TextInputEditText textInputEditText = activityMainBinding3.etRemotePort;
        Integer remotePort = getConfig().getRemotePort();
        if (remotePort == null || (string = remotePort.toString()) == null) {
            string = "";
        }
        textInputEditText.setText(string);
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        activityMainBinding4.etLocalPort.setText(String.valueOf(getConfig().getLocalPort()));
        ActivityMainBinding activityMainBinding5 = this.binding;
        if (activityMainBinding5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding5 = null;
        }
        activityMainBinding5.etProxyPort.setText(String.valueOf(getConfig().getProxyPort()));
        ActivityMainBinding activityMainBinding6 = this.binding;
        if (activityMainBinding6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding6;
        }
        activityMainBinding2.etUdpRelayPort.setText(String.valueOf(getConfig().getUdpRelayPort()));
    }

    private final void onStartClicked() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        String host = StringsKt.trim((CharSequence) String.valueOf(activityMainBinding.etRemoteHost.getText())).toString();
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        String remotePortText = StringsKt.trim((CharSequence) String.valueOf(activityMainBinding3.etRemotePort.getText())).toString();
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        Integer intOrNull = StringsKt.toIntOrNull(StringsKt.trim((CharSequence) String.valueOf(activityMainBinding4.etLocalPort.getText())).toString());
        int localPort = intOrNull != null ? intOrNull.intValue() : -1;
        ActivityMainBinding activityMainBinding5 = this.binding;
        if (activityMainBinding5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding5 = null;
        }
        Integer intOrNull2 = StringsKt.toIntOrNull(StringsKt.trim((CharSequence) String.valueOf(activityMainBinding5.etProxyPort.getText())).toString());
        int proxyPort = intOrNull2 != null ? intOrNull2.intValue() : -1;
        ActivityMainBinding activityMainBinding6 = this.binding;
        if (activityMainBinding6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding6 = null;
        }
        Integer intOrNull3 = StringsKt.toIntOrNull(StringsKt.trim((CharSequence) String.valueOf(activityMainBinding6.etUdpRelayPort.getText())).toString());
        int udpRelayPort = intOrNull3 != null ? intOrNull3.intValue() : -1;
        Integer remotePort = remotePortText.length() == 0 ? null : StringsKt.toIntOrNull(remotePortText);
        clearInputErrors();
        if (!NetworkUtils.INSTANCE.isValidHostOrIp(host)) {
            ActivityMainBinding activityMainBinding7 = this.binding;
            if (activityMainBinding7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding7;
            }
            activityMainBinding2.tilRemoteHost.setError("Enter a valid hostname or IP");
            return;
        }
        if ((remotePortText.length() > 0) && remotePort == null) {
            ActivityMainBinding activityMainBinding8 = this.binding;
            if (activityMainBinding8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding8;
            }
            activityMainBinding2.tilRemotePort.setError("Port must be a number (or leave blank for default)");
            return;
        }
        if (remotePort != null && !NetworkUtils.INSTANCE.isValidPort(remotePort.intValue())) {
            ActivityMainBinding activityMainBinding9 = this.binding;
            if (activityMainBinding9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding9;
            }
            activityMainBinding2.tilRemotePort.setError("Port must be 1-65535");
            return;
        }
        if (!NetworkUtils.INSTANCE.isValidPort(localPort)) {
            ActivityMainBinding activityMainBinding10 = this.binding;
            if (activityMainBinding10 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding10;
            }
            activityMainBinding2.tilLocalPort.setError("Port must be 1-65535");
            return;
        }
        if (!NetworkUtils.INSTANCE.isValidPort(proxyPort)) {
            ActivityMainBinding activityMainBinding11 = this.binding;
            if (activityMainBinding11 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding11;
            }
            activityMainBinding2.tilProxyPort.setError("Port must be 1-65535");
            return;
        }
        if (udpRelayPort < 0) {
            ActivityMainBinding activityMainBinding12 = this.binding;
            if (activityMainBinding12 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding12;
            }
            activityMainBinding2.tilUdpRelayPort.setError("Enter a valid port (0 to disable)");
            return;
        }
        if (udpRelayPort > 0 && !NetworkUtils.INSTANCE.isValidPort(udpRelayPort)) {
            ActivityMainBinding activityMainBinding13 = this.binding;
            if (activityMainBinding13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding13;
            }
            activityMainBinding2.tilUdpRelayPort.setError("Port must be 1-65535");
            return;
        }
        if (proxyPort == localPort) {
            ActivityMainBinding activityMainBinding14 = this.binding;
            if (activityMainBinding14 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding14;
            }
            activityMainBinding2.tilProxyPort.setError("Auth proxy port must differ from the local game port");
            return;
        }
        if (udpRelayPort > 0 && udpRelayPort == localPort) {
            ActivityMainBinding activityMainBinding15 = this.binding;
            if (activityMainBinding15 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding15;
            }
            activityMainBinding2.tilUdpRelayPort.setError("UDP relay port must differ from the local game port");
            return;
        }
        if (udpRelayPort > 0 && udpRelayPort == proxyPort) {
            ActivityMainBinding activityMainBinding16 = this.binding;
            if (activityMainBinding16 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding16;
            }
            activityMainBinding2.tilUdpRelayPort.setError("UDP relay port must differ from the auth proxy port");
            return;
        }
        if (TcpRelayEngine.INSTANCE.getShared().getStatus().getValue().getState() == BridgeState.STOPPED && !NetworkUtils.INSTANCE.isLocalPortAvailable(localPort)) {
            ActivityMainBinding activityMainBinding17 = this.binding;
            if (activityMainBinding17 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding17;
            }
            activityMainBinding2.tilLocalPort.setError("Port " + localPort + " is already in use");
            TcpRelayEngine.INSTANCE.getShared().getLog().log("Port unavailable: " + localPort + " (in use)");
            return;
        }
        if (TcpRelayEngine.INSTANCE.getShared().getStatus().getValue().getState() == BridgeState.STOPPED && !NetworkUtils.INSTANCE.isLocalPortAvailable(proxyPort)) {
            ActivityMainBinding activityMainBinding18 = this.binding;
            if (activityMainBinding18 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding18;
            }
            activityMainBinding2.tilProxyPort.setError("Port " + proxyPort + " is already in use");
            TcpRelayEngine.INSTANCE.getShared().getLog().log("Auth proxy port unavailable: " + proxyPort + " (in use)");
            return;
        }
        if (udpRelayPort > 0 && TcpRelayEngine.INSTANCE.getShared().getStatus().getValue().getState() == BridgeState.STOPPED && !NetworkUtils.INSTANCE.isLocalPortAvailable(udpRelayPort)) {
            ActivityMainBinding activityMainBinding19 = this.binding;
            if (activityMainBinding19 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding19;
            }
            activityMainBinding2.tilUdpRelayPort.setError("Port " + udpRelayPort + " is already in use");
            TcpRelayEngine.INSTANCE.getShared().getLog().log("UDP relay port unavailable: " + udpRelayPort + " (in use)");
            return;
        }
        getConfig().save(host, remotePort, localPort, proxyPort, udpRelayPort);
        if (Build.VERSION.SDK_INT >= 33) {
            this.notifPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS");
        }
        BridgeService.INSTANCE.start(this, host, remotePort, localPort, proxyPort, udpRelayPort);
    }

    private final void onStopClicked() {
        BridgeService.INSTANCE.stop(this);
    }

    private final void onTestClicked() {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        String host = StringsKt.trim((CharSequence) String.valueOf(activityMainBinding.etRemoteHost.getText())).toString();
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        String remotePortText = StringsKt.trim((CharSequence) String.valueOf(activityMainBinding3.etRemotePort.getText())).toString();
        Integer explicitPort = remotePortText.length() == 0 ? null : StringsKt.toIntOrNull(remotePortText);
        clearInputErrors();
        if (!NetworkUtils.INSTANCE.isValidHostOrIp(host)) {
            ActivityMainBinding activityMainBinding4 = this.binding;
            if (activityMainBinding4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding4;
            }
            activityMainBinding2.tilRemoteHost.setError("Enter a valid hostname or IP");
            return;
        }
        if (explicitPort != null && !NetworkUtils.INSTANCE.isValidPort(explicitPort.intValue())) {
            ActivityMainBinding activityMainBinding5 = this.binding;
            if (activityMainBinding5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding5;
            }
            activityMainBinding2.tilRemotePort.setError("Port must be 1-65535");
            return;
        }
        if ((remotePortText.length() > 0) && explicitPort == null) {
            ActivityMainBinding activityMainBinding6 = this.binding;
            if (activityMainBinding6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding2 = activityMainBinding6;
            }
            activityMainBinding2.tilRemotePort.setError("Port must be a number (or leave blank for default)");
            return;
        }
        ActivityMainBinding activityMainBinding7 = this.binding;
        if (activityMainBinding7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding7 = null;
        }
        activityMainBinding7.btnTest.setEnabled(false);
        ActivityMainBinding activityMainBinding8 = this.binding;
        if (activityMainBinding8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding8 = null;
        }
        activityMainBinding8.btnTest.setText("Testing…");
        TcpRelayEngine.INSTANCE.getShared().getLog().log("Testing connection to " + host + "…");
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, null, new C09761(host, explicitPort, null), 3, null);
    }

    /* compiled from: MainActivity.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onTestClicked$1", m162f = "MainActivity.kt", m163i = {}, m164l = {WKSRecord.Service.LINK}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onTestClicked$1 */
    static final class C09761 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Integer $explicitPort;
        final /* synthetic */ String $host;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C09761(String str, Integer num, Continuation<? super C09761> continuation) {
            super(2, continuation);
            this.$host = str;
            this.$explicitPort = num;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return MainActivity.this.new C09761(this.$host, this.$explicitPort, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C09761) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* compiled from: MainActivity.kt */
        @Metadata(m145d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001*\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "Lkotlin/Pair;", "Lcom/ninjatech/minecraftlanbridge/util/SrvResolver$ResolvedTarget;", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.ui.MainActivity$onTestClicked$1$1", m162f = "MainActivity.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: com.ninjatech.minecraftlanbridge.ui.MainActivity$onTestClicked$1$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Pair<? extends SrvResolver.ResolvedTarget, ? extends Boolean>>, Object> {
            final /* synthetic */ Integer $explicitPort;
            final /* synthetic */ String $host;
            int label;
            final /* synthetic */ MainActivity this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(String str, Integer num, MainActivity mainActivity, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.$host = str;
                this.$explicitPort = num;
                this.this$0 = mainActivity;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.$host, this.$explicitPort, this.this$0, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Pair<? extends SrvResolver.ResolvedTarget, ? extends Boolean>> continuation) {
                return invoke2(coroutineScope, (Continuation<? super Pair<SrvResolver.ResolvedTarget, Boolean>>) continuation);
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Pair<SrvResolver.ResolvedTarget, Boolean>> continuation) {
                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj) {
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        SrvResolver.ResolvedTarget t = SrvResolver.INSTANCE.resolve(this.$host, this.$explicitPort, this.this$0.getApplicationContext());
                        if (t.getViaSrv()) {
                            TcpRelayEngine.INSTANCE.getShared().getLog().log("SRV: " + this.$host + " -> " + t.getHost() + ":" + t.getPort());
                        } else if (this.$explicitPort == null) {
                            TcpRelayEngine.INSTANCE.getShared().getLog().log("No SRV record for " + this.$host + "; using " + t.getHost() + ":" + t.getPort());
                        }
                        return TuplesKt.m153to(t, Boxing.boxBoolean(this.this$0.testRemoteConnectivity(t.getHost(), t.getPort())));
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            C09761 c09761;
            String display;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ActivityMainBinding activityMainBinding = null;
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c09761 = this;
                    c09761.label = 1;
                    Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass1(c09761.$host, c09761.$explicitPort, MainActivity.this, null), c09761);
                    if (objWithContext != coroutine_suspended) {
                        $result = objWithContext;
                        break;
                    } else {
                        return coroutine_suspended;
                    }
                case 1:
                    ResultKt.throwOnFailure($result);
                    c09761 = this;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Pair pair = (Pair) $result;
            SrvResolver.ResolvedTarget target = (SrvResolver.ResolvedTarget) pair.component1();
            boolean ok = ((Boolean) pair.component2()).booleanValue();
            ActivityMainBinding activityMainBinding2 = MainActivity.this.binding;
            if (activityMainBinding2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
                activityMainBinding2 = null;
            }
            activityMainBinding2.btnTest.setEnabled(true);
            ActivityMainBinding activityMainBinding3 = MainActivity.this.binding;
            if (activityMainBinding3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("binding");
            } else {
                activityMainBinding = activityMainBinding3;
            }
            activityMainBinding.btnTest.setText(MainActivity.this.getString(C0948R.string.btn_test));
            if (target.getViaSrv() && !Intrinsics.areEqual(target.getHost(), c09761.$host)) {
                display = c09761.$host + " -> " + target.getHost() + ":" + target.getPort();
            } else {
                display = target.getHost() + ":" + target.getPort();
            }
            if (ok) {
                TcpRelayEngine.INSTANCE.getShared().getLog().log("Test OK: reached " + display);
            } else {
                TcpRelayEngine.INSTANCE.getShared().getLog().log("Test FAILED: could not reach " + display);
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean testRemoteConnectivity(String host, int port) {
        try {
            Socket socket = new Socket();
            try {
                Socket s = socket;
                s.connect(new InetSocketAddress(host, port), 6000);
                boolean zIsConnected = s.isConnected();
                CloseableKt.closeFinally(socket, null);
                return zIsConnected;
            } finally {
            }
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void renderStatus(BridgeStatus status) {
        Pair<String, Integer> pairStateVisual = stateVisual(status.getState());
        String label = pairStateVisual.component1();
        int color = pairStateVisual.component2().intValue();
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.tvStatus.setText(label);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        activityMainBinding3.statusDot.setBackground(ContextCompat.getDrawable(this, statusDotColor(color)));
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        activityMainBinding4.tvRemote.setText(status.getState() == BridgeState.STOPPED ? "—" : status.getRemoteDisplay());
        ActivityMainBinding activityMainBinding5 = this.binding;
        if (activityMainBinding5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding5 = null;
        }
        activityMainBinding5.tvLocal.setText(status.getState() == BridgeState.STOPPED ? "—" : status.getLocalDisplay());
        HttpProxyServer.ProxyStatus proxyStatus = HttpProxyServer.INSTANCE.getShared().getStatus().getValue();
        ActivityMainBinding activityMainBinding6 = this.binding;
        if (activityMainBinding6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding6 = null;
        }
        activityMainBinding6.tvProxy.setText(!proxyStatus.getRunning() ? "—" : proxyStatus.getDisplay());
        ActivityMainBinding activityMainBinding7 = this.binding;
        if (activityMainBinding7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding7 = null;
        }
        activityMainBinding7.tvConnections.setText(String.valueOf(status.getActiveConnections()));
        UdpRelayEngine.UdpRelayStatus udpStatus = UdpRelayEngine.INSTANCE.getShared().getStatus().getValue();
        ActivityMainBinding activityMainBinding8 = this.binding;
        if (activityMainBinding8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding8 = null;
        }
        activityMainBinding8.tvUdp.setText(udpStatus.getRunning() ? udpStatus.getDisplay() : "—");
        ActivityMainBinding activityMainBinding9 = this.binding;
        if (activityMainBinding9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding9 = null;
        }
        activityMainBinding9.tvMappings.setText(String.valueOf(udpStatus.getActiveMappings()));
        boolean running = (status.getState() == BridgeState.STOPPED || status.getState() == BridgeState.ERROR) ? false : true;
        ActivityMainBinding activityMainBinding10 = this.binding;
        if (activityMainBinding10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding10 = null;
        }
        activityMainBinding10.btnStart.setVisibility(running ? 8 : 0);
        ActivityMainBinding activityMainBinding11 = this.binding;
        if (activityMainBinding11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding11 = null;
        }
        activityMainBinding11.btnStop.setVisibility(running ? 0 : 8);
        ActivityMainBinding activityMainBinding12 = this.binding;
        if (activityMainBinding12 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding12 = null;
        }
        activityMainBinding12.btnStart.setEnabled(status.getState() == BridgeState.STOPPED || status.getState() == BridgeState.ERROR);
        boolean editable = running ? false : true;
        ActivityMainBinding activityMainBinding13 = this.binding;
        if (activityMainBinding13 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding13 = null;
        }
        activityMainBinding13.etRemoteHost.setEnabled(editable);
        ActivityMainBinding activityMainBinding14 = this.binding;
        if (activityMainBinding14 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding14 = null;
        }
        activityMainBinding14.etRemotePort.setEnabled(editable);
        ActivityMainBinding activityMainBinding15 = this.binding;
        if (activityMainBinding15 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding15 = null;
        }
        activityMainBinding15.etLocalPort.setEnabled(editable);
        ActivityMainBinding activityMainBinding16 = this.binding;
        if (activityMainBinding16 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding16 = null;
        }
        activityMainBinding16.etProxyPort.setEnabled(editable);
        ActivityMainBinding activityMainBinding17 = this.binding;
        if (activityMainBinding17 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding17 = null;
        }
        activityMainBinding17.etUdpRelayPort.setEnabled(editable);
        ActivityMainBinding activityMainBinding18 = this.binding;
        if (activityMainBinding18 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding18;
        }
        activityMainBinding2.btnTest.setEnabled(editable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void renderLog(List<String> lines) {
        ActivityMainBinding activityMainBinding = this.binding;
        ActivityMainBinding activityMainBinding2 = null;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.tvLog.setText(CollectionsKt.joinToString$default(lines, "\n", null, null, 0, null, null, 62, null));
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
        } else {
            activityMainBinding2 = activityMainBinding3;
        }
        activityMainBinding2.logScroll.post(new Runnable() { // from class: com.ninjatech.minecraftlanbridge.ui.MainActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.renderLog$lambda$5(this.f$0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void renderLog$lambda$5(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        ActivityMainBinding activityMainBinding = this$0.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.logScroll.fullScroll(WKSRecord.Service.CISCO_FNA);
    }

    private final void clearInputErrors() {
        ActivityMainBinding activityMainBinding = this.binding;
        if (activityMainBinding == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding = null;
        }
        activityMainBinding.tilRemoteHost.setError(null);
        ActivityMainBinding activityMainBinding2 = this.binding;
        if (activityMainBinding2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding2 = null;
        }
        activityMainBinding2.tilRemotePort.setError(null);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding3 = null;
        }
        activityMainBinding3.tilLocalPort.setError(null);
        ActivityMainBinding activityMainBinding4 = this.binding;
        if (activityMainBinding4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding4 = null;
        }
        activityMainBinding4.tilProxyPort.setError(null);
        ActivityMainBinding activityMainBinding5 = this.binding;
        if (activityMainBinding5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("binding");
            activityMainBinding5 = null;
        }
        activityMainBinding5.tilUdpRelayPort.setError(null);
    }

    private final Pair<String, Integer> stateVisual(BridgeState state) {
        switch (WhenMappings.$EnumSwitchMapping$0[state.ordinal()]) {
            case 1:
                return TuplesKt.m153to(getString(C0948R.string.state_stopped), Integer.valueOf(C0948R.color.status_stopped));
            case 2:
                return TuplesKt.m153to(getString(C0948R.string.state_starting), Integer.valueOf(C0948R.color.status_connecting));
            case 3:
                return TuplesKt.m153to(getString(C0948R.string.state_listening), Integer.valueOf(C0948R.color.status_running));
            case 4:
                return TuplesKt.m153to(getString(C0948R.string.state_running), Integer.valueOf(C0948R.color.status_running));
            case 5:
                return TuplesKt.m153to(getString(C0948R.string.state_connecting), Integer.valueOf(C0948R.color.status_connecting));
            case 6:
                return TuplesKt.m153to(getString(C0948R.string.state_error), Integer.valueOf(C0948R.color.status_error));
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    private final int statusDotColor(int colorRes) {
        return colorRes == C0948R.color.status_running ? C0948R.drawable.status_dot_green : colorRes == C0948R.color.status_error ? C0948R.drawable.status_dot_red : colorRes == C0948R.color.status_connecting ? C0948R.drawable.status_dot_yellow : C0948R.drawable.status_dot;
    }
}
