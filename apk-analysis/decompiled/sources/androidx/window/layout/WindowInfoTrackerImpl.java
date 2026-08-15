package androidx.window.layout;

import android.app.Activity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import androidx.window.layout.WindowInfoTrackerImpl;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;

/* compiled from: WindowInfoTrackerImpl.kt */
@Metadata(m145d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m146d2 = {"Landroidx/window/layout/WindowInfoTrackerImpl;", "Landroidx/window/layout/WindowInfoTracker;", "windowMetricsCalculator", "Landroidx/window/layout/WindowMetricsCalculator;", "windowBackend", "Landroidx/window/layout/WindowBackend;", "(Landroidx/window/layout/WindowMetricsCalculator;Landroidx/window/layout/WindowBackend;)V", "windowLayoutInfo", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/window/layout/WindowLayoutInfo;", "activity", "Landroid/app/Activity;", "Companion", "window_release"}, m147k = 1, m148mv = {1, 6, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class WindowInfoTrackerImpl implements WindowInfoTracker {
    private static final int BUFFER_CAPACITY = 10;
    private final WindowBackend windowBackend;
    private final WindowMetricsCalculator windowMetricsCalculator;

    public WindowInfoTrackerImpl(WindowMetricsCalculator windowMetricsCalculator, WindowBackend windowBackend) {
        Intrinsics.checkNotNullParameter(windowMetricsCalculator, "windowMetricsCalculator");
        Intrinsics.checkNotNullParameter(windowBackend, "windowBackend");
        this.windowMetricsCalculator = windowMetricsCalculator;
        this.windowBackend = windowBackend;
    }

    /* compiled from: WindowInfoTrackerImpl.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "Landroidx/window/layout/WindowLayoutInfo;"}, m147k = 3, m148mv = {1, 6, 0}, m150xi = 48)
    @DebugMetadata(m161c = "androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1", m162f = "WindowInfoTrackerImpl.kt", m163i = {0, 0, 1, 1}, m164l = {ConstraintLayout.LayoutParams.Table.LAYOUT_MARGIN_BASELINE, 55}, m165m = "invokeSuspend", m166n = {"$this$flow", "listener", "$this$flow", "listener"}, m167s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1 */
    static final class C06321 extends SuspendLambda implements Function2<FlowCollector<? super WindowLayoutInfo>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Activity $activity;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06321(Activity activity, Continuation<? super C06321> continuation) {
            super(2, continuation);
            this.$activity = activity;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C06321 c06321 = WindowInfoTrackerImpl.this.new C06321(this.$activity, continuation);
            c06321.L$0 = obj;
            return c06321;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(FlowCollector<? super WindowLayoutInfo> flowCollector, Continuation<? super Unit> continuation) {
            return ((C06321) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0079 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:17:0x007a  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0087 A[Catch: all -> 0x00b1, TRY_LEAVE, TryCatch #0 {all -> 0x00b1, blocks: (B:18:0x007f, B:20:0x0087), top: B:32:0x007f }] */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00a4  */
        /* JADX WARN: Type inference failed for: r1v0, types: [int] */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v13 */
        /* JADX WARN: Type inference failed for: r1v9, types: [androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00a0 -> B:14:0x0067). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            Consumer<WindowLayoutInfo> consumer;
            FlowCollector flowCollector;
            ChannelIterator it;
            C06321 c06321;
            Object obj2;
            Object obj3;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            Continuation<? super Boolean> continuation = this.label;
            try {
                switch (continuation) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        C06321 c063212 = this;
                        flowCollector = (FlowCollector) c063212.L$0;
                        final Channel channelChannel$default = ChannelKt.Channel$default(10, BufferOverflow.DROP_OLDEST, null, 4, null);
                        consumer = new Consumer() { // from class: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1$$ExternalSyntheticLambda0
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj4) {
                                WindowInfoTrackerImpl.C06321.m244invokeSuspend$lambda0(channelChannel$default, (WindowLayoutInfo) obj4);
                            }
                        };
                        WindowInfoTrackerImpl.this.windowBackend.registerLayoutChangeCallback(c063212.$activity, new ProfileInstallReceiver$$ExternalSyntheticLambda0(), consumer);
                        it = channelChannel$default.iterator();
                        continuation = c063212;
                        continuation.L$0 = flowCollector;
                        continuation.L$1 = consumer;
                        continuation.L$2 = it;
                        continuation.label = 1;
                        objHasNext = it.hasNext(continuation);
                        if (objHasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object obj4 = coroutine_suspended;
                        obj3 = obj;
                        obj = objHasNext;
                        c06321 = continuation;
                        obj2 = obj4;
                        try {
                            if (((Boolean) obj).booleanValue()) {
                                WindowInfoTrackerImpl.this.windowBackend.unregisterLayoutChangeCallback(consumer);
                                return Unit.INSTANCE;
                            }
                            c06321.L$0 = flowCollector;
                            c06321.L$1 = consumer;
                            c06321.L$2 = it;
                            c06321.label = 2;
                            if (flowCollector.emit((WindowLayoutInfo) it.next(), c06321) == obj2) {
                                return obj2;
                            }
                            obj = obj3;
                            coroutine_suspended = obj2;
                            continuation = c06321;
                            continuation.L$0 = flowCollector;
                            continuation.L$1 = consumer;
                            continuation.L$2 = it;
                            continuation.label = 1;
                            objHasNext = it.hasNext(continuation);
                            if (objHasNext == coroutine_suspended) {
                            }
                        } catch (Throwable th) {
                            continuation = c06321;
                            th = th;
                            WindowInfoTrackerImpl.this.windowBackend.unregisterLayoutChangeCallback(consumer);
                            throw th;
                        }
                    case 1:
                        ChannelIterator channelIterator = (ChannelIterator) this.L$2;
                        consumer = (Consumer) this.L$1;
                        flowCollector = (FlowCollector) this.L$0;
                        ResultKt.throwOnFailure(obj);
                        it = channelIterator;
                        c06321 = this;
                        obj2 = coroutine_suspended;
                        obj3 = obj;
                        if (((Boolean) obj).booleanValue()) {
                        }
                        break;
                    case 2:
                        C06321 c063213 = this;
                        ChannelIterator channelIterator2 = (ChannelIterator) c063213.L$2;
                        consumer = (Consumer) c063213.L$1;
                        flowCollector = (FlowCollector) c063213.L$0;
                        ResultKt.throwOnFailure(obj);
                        it = channelIterator2;
                        continuation = c063213;
                        continuation.L$0 = flowCollector;
                        continuation.L$1 = consumer;
                        continuation.L$2 = it;
                        continuation.label = 1;
                        objHasNext = it.hasNext(continuation);
                        if (objHasNext == coroutine_suspended) {
                        }
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: invokeSuspend$lambda-0, reason: not valid java name */
        public static final void m244invokeSuspend$lambda0(Channel $channel, WindowLayoutInfo info) {
            Intrinsics.checkNotNullExpressionValue(info, "info");
            $channel.mo1757trySendJP2dKIU(info);
        }
    }

    @Override // androidx.window.layout.WindowInfoTracker
    public Flow<WindowLayoutInfo> windowLayoutInfo(Activity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        return FlowKt.flow(new C06321(activity, null));
    }
}
