package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Ref;
import org.xbill.DNS.WKSRecord;

/* JADX INFO: Add missing generic type declarations: [R] */
/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1<R> implements Flow<R> {
    final /* synthetic */ Object $initial$inlined;
    final /* synthetic */ Function3 $operation$inlined;
    final /* synthetic */ Flow $this_runningFold$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1", m162f = "Transform.kt", m163i = {0, 0, 0}, m164l = {WKSRecord.Service.AUTH, 114}, m165m = "collect", m166n = {"this", "$this$runningFold_u24lambda_u249", "accumulator"}, m167s = {"L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1$1 */
    public static final class C12741 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        public C12741(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.this.collect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0082 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r5v0, types: [T, java.lang.Object] */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super R> flowCollector, Continuation<? super Unit> continuation) {
        C12741 c12741;
        FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1 flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1;
        FlowCollector $this$runningFold_u24lambda_u249;
        Ref.ObjectRef accumulator;
        Flow flow;
        FlowKt__TransformKt$runningFold$1$1 flowKt__TransformKt$runningFold$1$1;
        if (continuation instanceof C12741) {
            c12741 = (C12741) continuation;
            if ((c12741.label & Integer.MIN_VALUE) != 0) {
                c12741.label -= Integer.MIN_VALUE;
            } else {
                c12741 = new C12741(continuation);
            }
        }
        C12741 c127412 = c12741;
        Object $result = c127412.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c127412.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1 = this;
                $this$runningFold_u24lambda_u249 = flowCollector;
                accumulator = new Ref.ObjectRef();
                accumulator.element = flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.$initial$inlined;
                T t = accumulator.element;
                c127412.L$0 = flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1;
                c127412.L$1 = $this$runningFold_u24lambda_u249;
                c127412.L$2 = accumulator;
                c127412.label = 1;
                if ($this$runningFold_u24lambda_u249.emit(t, c127412) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                flow = flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.$this_runningFold$inlined;
                flowKt__TransformKt$runningFold$1$1 = new FlowKt__TransformKt$runningFold$1$1(accumulator, flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.$operation$inlined, $this$runningFold_u24lambda_u249);
                c127412.L$0 = null;
                c127412.L$1 = null;
                c127412.L$2 = null;
                c127412.label = 2;
                if (flow.collect(flowKt__TransformKt$runningFold$1$1, c127412) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                accumulator = (Ref.ObjectRef) c127412.L$2;
                $this$runningFold_u24lambda_u249 = (FlowCollector) c127412.L$1;
                flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1 = (FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1) c127412.L$0;
                ResultKt.throwOnFailure($result);
                flow = flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.$this_runningFold$inlined;
                flowKt__TransformKt$runningFold$1$1 = new FlowKt__TransformKt$runningFold$1$1(accumulator, flowKt__TransformKt$runningFold$$inlined$unsafeFlow$1.$operation$inlined, $this$runningFold_u24lambda_u249);
                c127412.L$0 = null;
                c127412.L$1 = null;
                c127412.L$2 = null;
                c127412.label = 2;
                if (flow.collect(flowKt__TransformKt$runningFold$1$1, c127412) == coroutine_suspended) {
                }
                return Unit.INSTANCE;
            case 2:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public FlowKt__TransformKt$runningFold$$inlined$unsafeFlow$1(Object obj, Flow flow, Function3 function3) {
        this.$initial$inlined = obj;
        this.$this_runningFold$inlined = flow;
        this.$operation$inlined = function3;
    }
}
