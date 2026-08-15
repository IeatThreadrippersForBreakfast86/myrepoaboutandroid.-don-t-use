package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.InlineMarker;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1<T> implements Flow<T> {
    final /* synthetic */ Function4 $predicate$inlined;
    final /* synthetic */ Flow $this_retryWhen$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1", m162f = "Errors.kt", m163i = {0, 0, 0, 0, 1, 1, 1, 1}, m164l = {116, 118}, m165m = "collect", m166n = {"this", "$this$retryWhen_u24lambda_u242", "attempt", "shallRetry", "this", "$this$retryWhen_u24lambda_u242", "cause", "attempt"}, m167s = {"L$0", "L$1", "J$0", "I$0", "L$0", "L$1", "L$2", "J$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1$1 */
    public static final class C12221 extends ContinuationImpl {
        int I$0;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        public C12221(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1.this.collect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x007e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x00ac -> B:25:0x00b0). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:29:0x00c6 -> B:30:0x00cd). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) throws Throwable {
        C12221 c12221;
        int i;
        long attempt;
        FlowCollector<? super T> flowCollector2;
        FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
        int i2;
        Object obj;
        Object $result;
        Throwable cause;
        Throwable cause2;
        FlowCollector<? super T> flowCollector3;
        if (continuation instanceof C12221) {
            c12221 = (C12221) continuation;
            if ((c12221.label & Integer.MIN_VALUE) != 0) {
                c12221.label -= Integer.MIN_VALUE;
            } else {
                c12221 = new C12221(continuation);
            }
        }
        C12221 c122212 = c12221;
        Object $result2 = c122212.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c122212.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = this;
                int i3 = 0;
                long attempt2 = 0;
                i2 = 0;
                Flow flow = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$this_retryWhen$inlined;
                c122212.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                c122212.L$1 = flowCollector;
                c122212.L$2 = null;
                c122212.J$0 = attempt2;
                c122212.I$0 = 0;
                c122212.label = 1;
                Object objCatchImpl = FlowKt.catchImpl(flow, flowCollector, c122212);
                if (objCatchImpl == $result3) {
                    return $result3;
                }
                flowCollector2 = flowCollector;
                i = i3;
                attempt = attempt2;
                Object obj2 = $result3;
                $result = $result2;
                $result2 = objCatchImpl;
                flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                obj = obj2;
                cause = (Throwable) $result2;
                if (cause == null) {
                    Function4 function4 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1.$predicate$inlined;
                    Long lBoxLong = Boxing.boxLong(attempt);
                    c122212.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
                    c122212.L$1 = flowCollector2;
                    c122212.L$2 = cause;
                    c122212.J$0 = attempt;
                    c122212.label = 2;
                    InlineMarker.mark(6);
                    Object objInvoke = function4.invoke(flowCollector2, cause, lBoxLong, c122212);
                    InlineMarker.mark(7);
                    if (objInvoke == obj) {
                        return obj;
                    }
                    FlowCollector<? super T> flowCollector4 = flowCollector2;
                    cause2 = cause;
                    $result2 = objInvoke;
                    flowCollector3 = flowCollector4;
                    if (((Boolean) $result2).booleanValue()) {
                        throw cause2;
                    }
                    attempt2 = attempt + 1;
                    i3 = i;
                    flowCollector = flowCollector3;
                    i2 = 1;
                    $result2 = $result;
                    $result3 = obj;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
                    if (i2 == 0) {
                        return Unit.INSTANCE;
                    }
                    i2 = 0;
                    Flow flow2 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$this_retryWhen$inlined;
                    c122212.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                    c122212.L$1 = flowCollector;
                    c122212.L$2 = null;
                    c122212.J$0 = attempt2;
                    c122212.I$0 = 0;
                    c122212.label = 1;
                    Object objCatchImpl2 = FlowKt.catchImpl(flow2, flowCollector, c122212);
                    if (objCatchImpl2 == $result3) {
                    }
                } else {
                    $result2 = $result;
                    $result3 = obj;
                    flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1;
                    long j = attempt;
                    i3 = i;
                    flowCollector = flowCollector2;
                    attempt2 = j;
                    if (i2 == 0) {
                    }
                    i2 = 0;
                    Flow flow22 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12.$this_retryWhen$inlined;
                    c122212.L$0 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$12;
                    c122212.L$1 = flowCollector;
                    c122212.L$2 = null;
                    c122212.J$0 = attempt2;
                    c122212.I$0 = 0;
                    c122212.label = 1;
                    Object objCatchImpl22 = FlowKt.catchImpl(flow22, flowCollector, c122212);
                    if (objCatchImpl22 == $result3) {
                    }
                }
            case 1:
                i = 0;
                int i4 = c122212.I$0;
                attempt = c122212.J$0;
                flowCollector2 = (FlowCollector) c122212.L$1;
                FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$13 = (FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1) c122212.L$0;
                ResultKt.throwOnFailure($result2);
                flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$13;
                i2 = i4;
                obj = $result3;
                $result = $result2;
                cause = (Throwable) $result2;
                if (cause == null) {
                }
                break;
            case 2:
                i = 0;
                long attempt3 = c122212.J$0;
                Throwable cause3 = (Throwable) c122212.L$2;
                FlowCollector<? super T> flowCollector5 = (FlowCollector) c122212.L$1;
                FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$14 = (FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1) c122212.L$0;
                ResultKt.throwOnFailure($result2);
                flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1 = flowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$14;
                flowCollector3 = flowCollector5;
                cause2 = cause3;
                attempt = attempt3;
                obj = $result3;
                $result = $result2;
                if (((Boolean) $result2).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public FlowKt__ErrorsKt$retryWhen$$inlined$unsafeFlow$1(Flow flow, Function4 function4) {
        this.$this_retryWhen$inlined = flow;
        this.$predicate$inlined = function4;
    }
}
