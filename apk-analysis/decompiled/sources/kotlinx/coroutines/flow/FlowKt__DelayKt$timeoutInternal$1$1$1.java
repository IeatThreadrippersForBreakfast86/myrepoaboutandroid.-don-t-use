package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.channels.ChannelResult;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Delay.kt */
@Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "value", "Lkotlinx/coroutines/channels/ChannelResult;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__DelayKt$timeoutInternal$1$1$1", m162f = "Delay.kt", m163i = {0}, m164l = {395}, m165m = "invokeSuspend", m166n = {"$this$onSuccess_u2dWpGqRn0$iv"}, m167s = {"L$0"})
/* loaded from: classes.dex */
final class FlowKt__DelayKt$timeoutInternal$1$1$1<T> extends SuspendLambda implements Function2<ChannelResult<? extends T>, Continuation<? super Boolean>, Object> {
    final /* synthetic */ FlowCollector<T> $downStream;
    /* synthetic */ Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__DelayKt$timeoutInternal$1$1$1(FlowCollector<? super T> flowCollector, Continuation<? super FlowKt__DelayKt$timeoutInternal$1$1$1> continuation) {
        super(2, continuation);
        this.$downStream = flowCollector;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        FlowKt__DelayKt$timeoutInternal$1$1$1 flowKt__DelayKt$timeoutInternal$1$1$1 = new FlowKt__DelayKt$timeoutInternal$1$1$1(this.$downStream, continuation);
        flowKt__DelayKt$timeoutInternal$1$1$1.L$0 = obj;
        return flowKt__DelayKt$timeoutInternal$1$1$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(Object obj, Continuation<? super Boolean> continuation) {
        return m1796invokeWpGqRn0(((ChannelResult) obj).getHolder(), continuation);
    }

    /* renamed from: invoke-WpGqRn0, reason: not valid java name */
    public final Object m1796invokeWpGqRn0(Object obj, Continuation<? super Boolean> continuation) {
        return ((FlowKt__DelayKt$timeoutInternal$1$1$1) create(ChannelResult.m1768boximpl(obj), continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005c  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) throws Throwable {
        Object holder;
        FlowKt__DelayKt$timeoutInternal$1$1$1<T> flowKt__DelayKt$timeoutInternal$1$1$1;
        Object obj2;
        FlowKt__DelayKt$timeoutInternal$1$1$1 flowKt__DelayKt$timeoutInternal$1$1$12;
        Object obj3;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                holder = ((ChannelResult) this.L$0).getHolder();
                FlowCollector<T> flowCollector = this.$downStream;
                if (!(holder instanceof ChannelResult.Failed)) {
                    this.L$0 = holder;
                    this.label = 1;
                    if (flowCollector.emit(holder, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowKt__DelayKt$timeoutInternal$1$1$1 = this;
                    obj2 = holder;
                    flowKt__DelayKt$timeoutInternal$1$1$12 = null;
                    holder = obj2;
                }
                obj3 = holder;
                if (!(obj3 instanceof ChannelResult.Closed)) {
                    Throwable thM1772exceptionOrNullimpl = ChannelResult.m1772exceptionOrNullimpl(obj3);
                    if (thM1772exceptionOrNullimpl != null) {
                        throw thM1772exceptionOrNullimpl;
                    }
                    return Boxing.boxBoolean(false);
                }
                return Boxing.boxBoolean(true);
            case 1:
                flowKt__DelayKt$timeoutInternal$1$1$1 = this;
                flowKt__DelayKt$timeoutInternal$1$1$12 = null;
                obj2 = flowKt__DelayKt$timeoutInternal$1$1$1.L$0;
                ResultKt.throwOnFailure(obj);
                holder = obj2;
                obj3 = holder;
                if (!(obj3 instanceof ChannelResult.Closed)) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
