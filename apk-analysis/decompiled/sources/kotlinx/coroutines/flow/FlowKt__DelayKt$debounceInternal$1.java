package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.selects.OnTimeoutKt;
import kotlinx.coroutines.selects.SelectImplementation;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Delay.kt */
@Metadata(m145d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1", m162f = "Delay.kt", m163i = {0, 0, 0, 0, 1, 1, 1}, m164l = {215, 418}, m165m = "invokeSuspend", m166n = {"downstream", "values", "lastValue", "timeoutMillis", "downstream", "values", "lastValue"}, m167s = {"L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2"})
/* loaded from: classes.dex */
final class FlowKt__DelayKt$debounceInternal$1<T> extends SuspendLambda implements Function3<CoroutineScope, FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<T> $this_debounceInternal;
    final /* synthetic */ Function1<T, Long> $timeoutMillisSelector;
    private /* synthetic */ Object L$0;
    /* synthetic */ Object L$1;
    Object L$2;
    Object L$3;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__DelayKt$debounceInternal$1(Function1<? super T, Long> function1, Flow<? extends T> flow, Continuation<? super FlowKt__DelayKt$debounceInternal$1> continuation) {
        super(3, continuation);
        this.$timeoutMillisSelector = function1;
        this.$this_debounceInternal = flow;
    }

    @Override // kotlin.jvm.functions.Function3
    public final Object invoke(CoroutineScope coroutineScope, FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        FlowKt__DelayKt$debounceInternal$1 flowKt__DelayKt$debounceInternal$1 = new FlowKt__DelayKt$debounceInternal$1(this.$timeoutMillisSelector, this.$this_debounceInternal, continuation);
        flowKt__DelayKt$debounceInternal$1.L$0 = coroutineScope;
        flowKt__DelayKt$debounceInternal$1.L$1 = flowCollector;
        return flowKt__DelayKt$debounceInternal$1.invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x013f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x014b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x0140 -> B:53:0x0142). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object $result) {
        FlowKt__DelayKt$debounceInternal$1 flowKt__DelayKt$debounceInternal$1;
        Ref.ObjectRef lastValue;
        ReceiveChannel values;
        FlowCollector downstream;
        Ref.LongRef timeoutMillis;
        FlowCollector downstream2;
        ReceiveChannel values2;
        Ref.ObjectRef lastValue2;
        SelectImplementation $this$select_u24lambda_u241$iv;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        long j = 0;
        int i = 1;
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                flowKt__DelayKt$debounceInternal$1 = this;
                CoroutineScope $this$scopedFlow = (CoroutineScope) flowKt__DelayKt$debounceInternal$1.L$0;
                FlowCollector downstream3 = (FlowCollector) flowKt__DelayKt$debounceInternal$1.L$1;
                ReceiveChannel values3 = ProduceKt.produce$default($this$scopedFlow, null, 0, new FlowKt__DelayKt$debounceInternal$1$values$1(flowKt__DelayKt$debounceInternal$1.$this_debounceInternal, null), 3, null);
                lastValue = new Ref.ObjectRef();
                values = values3;
                downstream = downstream3;
                if (lastValue.element != NullSurrogateKt.DONE) {
                    timeoutMillis = new Ref.LongRef();
                    if (lastValue.element != null) {
                        Function1<T, Long> function1 = flowKt__DelayKt$debounceInternal$1.$timeoutMillisSelector;
                        Symbol this_$iv = NullSurrogateKt.NULL;
                        T t = lastValue.element;
                        if (t == this_$iv) {
                            t = null;
                        }
                        timeoutMillis.element = function1.invoke(t).longValue();
                        if ((timeoutMillis.element >= j ? i : 0) == 0) {
                            throw new IllegalArgumentException("Debounce timeout should not be negative".toString());
                        }
                        if (timeoutMillis.element == j) {
                            Symbol this_$iv2 = NullSurrogateKt.NULL;
                            T t2 = lastValue.element;
                            if (t2 == this_$iv2) {
                                t2 = null;
                            }
                            flowKt__DelayKt$debounceInternal$1.L$0 = downstream;
                            flowKt__DelayKt$debounceInternal$1.L$1 = values;
                            flowKt__DelayKt$debounceInternal$1.L$2 = lastValue;
                            flowKt__DelayKt$debounceInternal$1.L$3 = timeoutMillis;
                            flowKt__DelayKt$debounceInternal$1.label = i;
                            if (downstream.emit(t2, flowKt__DelayKt$debounceInternal$1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            lastValue.element = null;
                            downstream2 = downstream;
                            values2 = values;
                            lastValue2 = lastValue;
                            if (DebugKt.getASSERTIONS_ENABLED()) {
                                if (((lastValue2.element == null || timeoutMillis.element > j) ? i : 0) == 0) {
                                    throw new AssertionError();
                                }
                            }
                            $this$select_u24lambda_u241$iv = new SelectImplementation(flowKt__DelayKt$debounceInternal$1.get$context());
                            SelectImplementation $this$invokeSuspend_u24lambda_u242 = $this$select_u24lambda_u241$iv;
                            if (lastValue2.element != null) {
                                OnTimeoutKt.onTimeout($this$invokeSuspend_u24lambda_u242, timeoutMillis.element, new FlowKt__DelayKt$debounceInternal$1$3$1(downstream2, lastValue2, null));
                            }
                            $this$invokeSuspend_u24lambda_u242.invoke(values2.getOnReceiveCatching(), new FlowKt__DelayKt$debounceInternal$1$3$2(lastValue2, downstream2, null));
                            flowKt__DelayKt$debounceInternal$1.L$0 = downstream2;
                            flowKt__DelayKt$debounceInternal$1.L$1 = values2;
                            flowKt__DelayKt$debounceInternal$1.L$2 = lastValue2;
                            flowKt__DelayKt$debounceInternal$1.L$3 = null;
                            flowKt__DelayKt$debounceInternal$1.label = 2;
                            if ($this$select_u24lambda_u241$iv.doSelect(flowKt__DelayKt$debounceInternal$1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            lastValue = lastValue2;
                            values = values2;
                            downstream = downstream2;
                            j = 0;
                            i = 1;
                            if (lastValue.element != NullSurrogateKt.DONE) {
                                return Unit.INSTANCE;
                            }
                        }
                    }
                    downstream2 = downstream;
                    values2 = values;
                    lastValue2 = lastValue;
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                    }
                    $this$select_u24lambda_u241$iv = new SelectImplementation(flowKt__DelayKt$debounceInternal$1.get$context());
                    SelectImplementation $this$invokeSuspend_u24lambda_u2422 = $this$select_u24lambda_u241$iv;
                    if (lastValue2.element != null) {
                    }
                    $this$invokeSuspend_u24lambda_u2422.invoke(values2.getOnReceiveCatching(), new FlowKt__DelayKt$debounceInternal$1$3$2(lastValue2, downstream2, null));
                    flowKt__DelayKt$debounceInternal$1.L$0 = downstream2;
                    flowKt__DelayKt$debounceInternal$1.L$1 = values2;
                    flowKt__DelayKt$debounceInternal$1.L$2 = lastValue2;
                    flowKt__DelayKt$debounceInternal$1.L$3 = null;
                    flowKt__DelayKt$debounceInternal$1.label = 2;
                    if ($this$select_u24lambda_u241$iv.doSelect(flowKt__DelayKt$debounceInternal$1) == coroutine_suspended) {
                    }
                }
                break;
            case 1:
                flowKt__DelayKt$debounceInternal$1 = this;
                timeoutMillis = (Ref.LongRef) flowKt__DelayKt$debounceInternal$1.L$3;
                lastValue = (Ref.ObjectRef) flowKt__DelayKt$debounceInternal$1.L$2;
                values = (ReceiveChannel) flowKt__DelayKt$debounceInternal$1.L$1;
                downstream = (FlowCollector) flowKt__DelayKt$debounceInternal$1.L$0;
                ResultKt.throwOnFailure($result);
                lastValue.element = null;
                downstream2 = downstream;
                values2 = values;
                lastValue2 = lastValue;
                if (DebugKt.getASSERTIONS_ENABLED()) {
                }
                $this$select_u24lambda_u241$iv = new SelectImplementation(flowKt__DelayKt$debounceInternal$1.get$context());
                SelectImplementation $this$invokeSuspend_u24lambda_u24222 = $this$select_u24lambda_u241$iv;
                if (lastValue2.element != null) {
                }
                $this$invokeSuspend_u24lambda_u24222.invoke(values2.getOnReceiveCatching(), new FlowKt__DelayKt$debounceInternal$1$3$2(lastValue2, downstream2, null));
                flowKt__DelayKt$debounceInternal$1.L$0 = downstream2;
                flowKt__DelayKt$debounceInternal$1.L$1 = values2;
                flowKt__DelayKt$debounceInternal$1.L$2 = lastValue2;
                flowKt__DelayKt$debounceInternal$1.L$3 = null;
                flowKt__DelayKt$debounceInternal$1.label = 2;
                if ($this$select_u24lambda_u241$iv.doSelect(flowKt__DelayKt$debounceInternal$1) == coroutine_suspended) {
                }
                break;
            case 2:
                flowKt__DelayKt$debounceInternal$1 = this;
                lastValue2 = (Ref.ObjectRef) flowKt__DelayKt$debounceInternal$1.L$2;
                values2 = (ReceiveChannel) flowKt__DelayKt$debounceInternal$1.L$1;
                downstream2 = (FlowCollector) flowKt__DelayKt$debounceInternal$1.L$0;
                ResultKt.throwOnFailure($result);
                lastValue = lastValue2;
                values = values2;
                downstream = downstream2;
                j = 0;
                i = 1;
                if (lastValue.element != NullSurrogateKt.DONE) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
