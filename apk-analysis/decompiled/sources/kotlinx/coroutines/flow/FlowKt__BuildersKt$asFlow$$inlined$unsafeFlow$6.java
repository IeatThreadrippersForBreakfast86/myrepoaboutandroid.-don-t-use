package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6<T> implements Flow<T> {
    final /* synthetic */ Object[] $this_asFlow$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6", m162f = "Builders.kt", m163i = {0, 0}, m164l = {114}, m165m = "collect", m166n = {"$this$asFlow_u24lambda_u2411", "$this$forEach$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6$1 */
    public static final class C11961 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public C11961(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6.this.collect(null, this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x006b -> B:19:0x006c). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        C11961 c11961;
        FlowCollector flowCollector2;
        Object[] objArr;
        int length;
        int i;
        FlowCollector flowCollector3;
        if (continuation instanceof C11961) {
            c11961 = (C11961) continuation;
            if ((c11961.label & Integer.MIN_VALUE) != 0) {
                c11961.label -= Integer.MIN_VALUE;
            } else {
                c11961 = new C11961(continuation);
            }
        }
        C11961 c119612 = c11961;
        Object obj = c119612.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c119612.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                Object[] objArr2 = this.$this_asFlow$inlined;
                flowCollector2 = flowCollector;
                objArr = objArr2;
                length = objArr2.length;
                i = 0;
                if (i < length) {
                    Object obj2 = objArr[i];
                    c119612.L$0 = flowCollector2;
                    c119612.L$1 = objArr;
                    c119612.I$0 = i;
                    c119612.I$1 = length;
                    c119612.label = 1;
                    if (flowCollector2.emit(obj2, c119612) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowCollector3 = flowCollector2;
                    i++;
                    flowCollector2 = flowCollector3;
                    if (i < length) {
                        return Unit.INSTANCE;
                    }
                }
            case 1:
                length = c119612.I$1;
                i = c119612.I$0;
                objArr = (Object[]) c119612.L$1;
                FlowCollector flowCollector4 = (FlowCollector) c119612.L$0;
                ResultKt.throwOnFailure(obj);
                flowCollector3 = flowCollector4;
                i++;
                flowCollector2 = flowCollector3;
                if (i < length) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$6(Object[] objArr) {
        this.$this_asFlow$inlined = objArr;
    }
}
