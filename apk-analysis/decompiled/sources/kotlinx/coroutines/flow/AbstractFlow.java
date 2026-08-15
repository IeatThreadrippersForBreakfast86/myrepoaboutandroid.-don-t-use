package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.internal.SafeCollector;

/* compiled from: Flow.kt */
@Metadata(m145d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b'\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0086@¢\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH¦@¢\u0006\u0002\u0010\t¨\u0006\u000b"}, m146d2 = {"Lkotlinx/coroutines/flow/AbstractFlow;", "T", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/CancellableFlow;", "()V", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectSafely", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public abstract class AbstractFlow<T> implements Flow<T>, CancellableFlow<T> {

    /* compiled from: Flow.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.AbstractFlow", m162f = "Flow.kt", m163i = {0}, m164l = {226}, m165m = "collect", m166n = {"safeCollector"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.AbstractFlow$collect$1 */
    static final class C11871 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;
        final /* synthetic */ AbstractFlow<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C11871(AbstractFlow<T> abstractFlow, Continuation<? super C11871> continuation) {
            super(continuation);
            this.this$0 = abstractFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.collect(null, this);
        }
    }

    public abstract Object collectSafely(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation);

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r5v0, types: [kotlinx.coroutines.flow.AbstractFlow, kotlinx.coroutines.flow.AbstractFlow<T>] */
    /* JADX WARN: Type inference failed for: r6v0, types: [kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.flow.FlowCollector<? super T>] */
    /* JADX WARN: Type inference failed for: r6v1, types: [kotlinx.coroutines.flow.internal.SafeCollector] */
    /* JADX WARN: Type inference failed for: r6v10 */
    /* JADX WARN: Type inference failed for: r6v3, types: [kotlinx.coroutines.flow.internal.SafeCollector] */
    /* JADX WARN: Type inference failed for: r6v9 */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        C11871 c11871;
        if (continuation instanceof C11871) {
            c11871 = (C11871) continuation;
            if ((c11871.label & Integer.MIN_VALUE) != 0) {
                c11871.label -= Integer.MIN_VALUE;
            } else {
                c11871 = new C11871(this, continuation);
            }
        }
        C11871 c118712 = c11871;
        Object obj = c118712.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c118712.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    SafeCollector safeCollector = new SafeCollector(flowCollector, c118712.getContext());
                    c118712.L$0 = safeCollector;
                    c118712.label = 1;
                    Object objCollectSafely = collectSafely(safeCollector, c118712);
                    flowCollector = safeCollector;
                    if (objCollectSafely == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    SafeCollector safeCollector2 = (SafeCollector) c118712.L$0;
                    ResultKt.throwOnFailure(obj);
                    flowCollector = safeCollector2;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ((SafeCollector) flowCollector).releaseIntercepted();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            flowCollector.releaseIntercepted();
            throw th;
        }
    }
}
