package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7 implements Flow<Integer> {
    final /* synthetic */ int[] $this_asFlow$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7", m162f = "Builders.kt", m163i = {0, 0}, m164l = {114}, m165m = "collect", m166n = {"$this$asFlow_u24lambda_u2413", "$this$forEach$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7$1 */
    public static final class C11971 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public C11971(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7.this.collect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x006f -> B:19:0x0070). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super Integer> flowCollector, Continuation<? super Unit> continuation) {
        C11971 c11971;
        FlowCollector $this$asFlow_u24lambda_u2413;
        int[] $this$forEach$iv;
        int $i$f$forEach;
        int i;
        if (continuation instanceof C11971) {
            c11971 = (C11971) continuation;
            if ((c11971.label & Integer.MIN_VALUE) != 0) {
                c11971.label -= Integer.MIN_VALUE;
            } else {
                c11971 = new C11971(continuation);
            }
        }
        C11971 c119712 = c11971;
        Object $result = c119712.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c119712.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                int[] $this$forEach$iv2 = this.$this_asFlow$inlined;
                $this$asFlow_u24lambda_u2413 = flowCollector;
                $this$forEach$iv = $this$forEach$iv2;
                $i$f$forEach = $this$forEach$iv2.length;
                i = 0;
                if (i < $i$f$forEach) {
                    int value = $this$forEach$iv[i];
                    Integer numBoxInt = Boxing.boxInt(value);
                    c119712.L$0 = $this$asFlow_u24lambda_u2413;
                    c119712.L$1 = $this$forEach$iv;
                    c119712.I$0 = i;
                    c119712.I$1 = $i$f$forEach;
                    c119712.label = 1;
                    if ($this$asFlow_u24lambda_u2413.emit(numBoxInt, c119712) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    i++;
                    if (i < $i$f$forEach) {
                        return Unit.INSTANCE;
                    }
                }
            case 1:
                $i$f$forEach = c119712.I$1;
                i = c119712.I$0;
                $this$forEach$iv = (int[]) c119712.L$1;
                $this$asFlow_u24lambda_u2413 = (FlowCollector) c119712.L$0;
                ResultKt.throwOnFailure($result);
                i++;
                if (i < $i$f$forEach) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7(int[] iArr) {
        this.$this_asFlow$inlined = iArr;
    }
}
