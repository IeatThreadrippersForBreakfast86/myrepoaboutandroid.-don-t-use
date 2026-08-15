package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.internal.SafeCollector;

/* compiled from: Share.kt */
@Metadata(m145d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002BB\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012-\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005¢\u0006\u0002\b\t¢\u0006\u0002\u0010\nJ\u0016\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00028\u0000H\u0096A¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u0007H\u0086@¢\u0006\u0002\u0010\u0010R7\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0005¢\u0006\u0002\b\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, m146d2 = {"Lkotlinx/coroutines/flow/SubscribedFlowCollector;", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "collector", "action", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "emit", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onSubscription", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class SubscribedFlowCollector<T> implements FlowCollector<T> {
    private final Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> action;
    private final FlowCollector<T> collector;

    /* compiled from: Share.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.SubscribedFlowCollector", m162f = "Share.kt", m163i = {0, 0}, m164l = {415, 419}, m165m = "onSubscription", m166n = {"this", "safeCollector"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 */
    static final class C12951 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;
        final /* synthetic */ SubscribedFlowCollector<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C12951(SubscribedFlowCollector<T> subscribedFlowCollector, Continuation<? super C12951> continuation) {
            super(continuation);
            this.this$0 = subscribedFlowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.onSubscription(this);
        }
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public Object emit(T t, Continuation<? super Unit> continuation) {
        return this.collector.emit(t, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SubscribedFlowCollector(FlowCollector<? super T> flowCollector, Function2<? super FlowCollector<? super T>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        this.collector = flowCollector;
        this.action = function2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlinx.coroutines.flow.internal.SafeCollector] */
    /* JADX WARN: Type inference failed for: r2v5, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object onSubscription(Continuation<? super Unit> continuation) {
        C12951 c12951;
        SubscribedFlowCollector subscribedFlowCollector;
        SafeCollector safeCollector;
        if (continuation instanceof C12951) {
            c12951 = (C12951) continuation;
            if ((c12951.label & Integer.MIN_VALUE) != 0) {
                c12951.label -= Integer.MIN_VALUE;
            } else {
                c12951 = new C12951(this, continuation);
            }
        }
        C12951 c129512 = c12951;
        Object $result = c129512.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ?? r2 = c129512.label;
        try {
            switch (r2) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    subscribedFlowCollector = this;
                    safeCollector = new SafeCollector(subscribedFlowCollector.collector, c129512.getContext());
                    Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> function2 = subscribedFlowCollector.action;
                    c129512.L$0 = subscribedFlowCollector;
                    c129512.L$1 = safeCollector;
                    c129512.label = 1;
                    if (function2.invoke(safeCollector, c129512) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    safeCollector.releaseIntercepted();
                    r2 = subscribedFlowCollector.collector instanceof SubscribedFlowCollector;
                    if (r2 != 0) {
                        SubscribedFlowCollector subscribedFlowCollector2 = (SubscribedFlowCollector) subscribedFlowCollector.collector;
                        c129512.L$0 = null;
                        c129512.L$1 = null;
                        c129512.label = 2;
                        if (subscribedFlowCollector2.onSubscription(c129512) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    safeCollector = (SafeCollector) c129512.L$1;
                    subscribedFlowCollector = (SubscribedFlowCollector) c129512.L$0;
                    ResultKt.throwOnFailure($result);
                    safeCollector.releaseIntercepted();
                    r2 = subscribedFlowCollector.collector instanceof SubscribedFlowCollector;
                    if (r2 != 0) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            r2.releaseIntercepted();
            throw th;
        }
    }
}
