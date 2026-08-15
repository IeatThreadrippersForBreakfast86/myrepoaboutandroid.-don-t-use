package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref;

/* compiled from: Count.kt */
@Metadata(m145d1 = {"\u0000$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086@¢\u0006\u0002\u0010\u0004\u001aB\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\"\u0010\u0005\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0006H\u0086@¢\u0006\u0002\u0010\n¨\u0006\u000b"}, m146d2 = {"count", "", "T", "Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "predicate", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 5, m148mv = {1, 9, 0}, m150xi = 48, m151xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes.dex */
final /* synthetic */ class FlowKt__CountKt {

    /* compiled from: Count.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__CountKt", m162f = "Count.kt", m163i = {0}, m164l = {13}, m165m = "count", m166n = {"i"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__CountKt$count$1 */
    static final class C12061<T> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C12061(Continuation<? super C12061> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt.count(null, this);
        }
    }

    /* compiled from: Count.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__CountKt", m162f = "Count.kt", m163i = {0}, m164l = {25}, m165m = "count", m166n = {"i"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__CountKt$count$3 */
    static final class C12083<T> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C12083(Continuation<? super C12083> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt.count(null, null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <T> Object count(Flow<? extends T> flow, Continuation<? super Integer> continuation) {
        C12061 c12061;
        Ref.IntRef i;
        if (continuation instanceof C12061) {
            c12061 = (C12061) continuation;
            if ((c12061.label & Integer.MIN_VALUE) != 0) {
                c12061.label -= Integer.MIN_VALUE;
            } else {
                c12061 = new C12061(continuation);
            }
        }
        C12061 c120612 = c12061;
        Object $result = c120612.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c120612.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                final Ref.IntRef i2 = new Ref.IntRef();
                FlowCollector<? super Object> flowCollector = new FlowCollector() { // from class: kotlinx.coroutines.flow.FlowKt__CountKt.count.2
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    public final Object emit(T t, Continuation<? super Unit> continuation2) {
                        i2.element++;
                        int i3 = i2.element;
                        return Unit.INSTANCE;
                    }
                };
                c120612.L$0 = i2;
                c120612.label = 1;
                if (flow.collect(flowCollector, c120612) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i = i2;
                break;
            case 1:
                i = (Ref.IntRef) c120612.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Boxing.boxInt(i.element);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <T> Object count(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super Integer> continuation) {
        C12083 c12083;
        Ref.IntRef i;
        if (continuation instanceof C12083) {
            c12083 = (C12083) continuation;
            if ((c12083.label & Integer.MIN_VALUE) != 0) {
                c12083.label -= Integer.MIN_VALUE;
            } else {
                c12083 = new C12083(continuation);
            }
        }
        C12083 c120832 = c12083;
        Object $result = c120832.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c120832.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                Ref.IntRef i2 = new Ref.IntRef();
                FlowCollector<? super Object> c12094 = new C12094<>(function2, i2);
                c120832.L$0 = i2;
                c120832.label = 1;
                if (flow.collect(c12094, c120832) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i = i2;
                break;
            case 1:
                i = (Ref.IntRef) c120832.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Boxing.boxInt(i.element);
    }

    /* compiled from: Count.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, m146d2 = {"<anonymous>", "", "T", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    /* renamed from: kotlinx.coroutines.flow.FlowKt__CountKt$count$4 */
    static final class C12094<T> implements FlowCollector {

        /* renamed from: $i */
        final /* synthetic */ Ref.IntRef f231$i;
        final /* synthetic */ Function2<T, Continuation<? super Boolean>, Object> $predicate;

        /* JADX WARN: Multi-variable type inference failed */
        C12094(Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> function2, Ref.IntRef intRef) {
            this.$predicate = function2;
            this.f231$i = intRef;
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
        @Override // kotlinx.coroutines.flow.FlowCollector
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object emit(T t, Continuation<? super Unit> continuation) {
            FlowKt__CountKt$count$4$emit$1 flowKt__CountKt$count$4$emit$1;
            C12094 c12094;
            Object value;
            if (continuation instanceof FlowKt__CountKt$count$4$emit$1) {
                flowKt__CountKt$count$4$emit$1 = (FlowKt__CountKt$count$4$emit$1) continuation;
                if ((flowKt__CountKt$count$4$emit$1.label & Integer.MIN_VALUE) != 0) {
                    flowKt__CountKt$count$4$emit$1.label -= Integer.MIN_VALUE;
                } else {
                    flowKt__CountKt$count$4$emit$1 = new FlowKt__CountKt$count$4$emit$1(this, continuation);
                }
            }
            FlowKt__CountKt$count$4$emit$1 flowKt__CountKt$count$4$emit$12 = flowKt__CountKt$count$4$emit$1;
            Object $result = flowKt__CountKt$count$4$emit$12.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (flowKt__CountKt$count$4$emit$12.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c12094 = this;
                    Function2<T, Continuation<? super Boolean>, Object> function2 = c12094.$predicate;
                    flowKt__CountKt$count$4$emit$12.L$0 = c12094;
                    flowKt__CountKt$count$4$emit$12.label = 1;
                    value = function2.invoke(t, flowKt__CountKt$count$4$emit$12);
                    if (value == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    C12094 c120942 = (C12094) flowKt__CountKt$count$4$emit$12.L$0;
                    ResultKt.throwOnFailure($result);
                    c12094 = c120942;
                    value = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (((Boolean) value).booleanValue()) {
                c12094.f231$i.element++;
                int i = c12094.f231$i.element;
            }
            return Unit.INSTANCE;
        }
    }
}
