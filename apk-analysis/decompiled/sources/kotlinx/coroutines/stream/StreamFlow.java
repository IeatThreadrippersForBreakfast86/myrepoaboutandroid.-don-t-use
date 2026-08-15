package kotlinx.coroutines.stream;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.stream.Stream;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;

/* compiled from: Stream.kt */
@Metadata(m145d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u001c\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0096@¢\u0006\u0002\u0010\fR\t\u0010\u0006\u001a\u00020\u0007X\u0082\u0004R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m146d2 = {"Lkotlinx/coroutines/stream/StreamFlow;", "T", "Lkotlinx/coroutines/flow/Flow;", "stream", "Ljava/util/stream/Stream;", "(Ljava/util/stream/Stream;)V", "consumed", "Lkotlinx/atomicfu/AtomicBoolean;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
final class StreamFlow<T> implements Flow<T> {
    private static final /* synthetic */ AtomicIntegerFieldUpdater consumed$volatile$FU = AtomicIntegerFieldUpdater.newUpdater(StreamFlow.class, "consumed$volatile");
    private volatile /* synthetic */ int consumed$volatile = 0;
    private final Stream<T> stream;

    /* compiled from: Stream.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.stream.StreamFlow", m162f = "Stream.kt", m163i = {0, 0}, m164l = {22}, m165m = "collect", m166n = {"this", "collector"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.stream.StreamFlow$collect$1 */
    static final class C13181 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;
        final /* synthetic */ StreamFlow<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C13181(StreamFlow<T> streamFlow, Continuation<? super C13181> continuation) {
            super(continuation);
            this.this$0 = streamFlow;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.collect(null, this);
        }
    }

    private final /* synthetic */ int getConsumed$volatile() {
        return this.consumed$volatile;
    }

    private final /* synthetic */ void setConsumed$volatile(int i) {
        this.consumed$volatile = i;
    }

    public StreamFlow(Stream<T> stream) {
        this.stream = stream;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        C13181 c13181;
        StreamFlow streamFlow;
        FlowCollector collector;
        Iterator<T> it;
        if (continuation instanceof C13181) {
            c13181 = (C13181) continuation;
            if ((c13181.label & Integer.MIN_VALUE) != 0) {
                c13181.label -= Integer.MIN_VALUE;
            } else {
                c13181 = new C13181(this, continuation);
            }
        }
        C13181 c131812 = c13181;
        Object $result = c131812.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c131812.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    streamFlow = this;
                    if (!consumed$volatile$FU.compareAndSet(streamFlow, 0, 1)) {
                        throw new IllegalStateException("Stream.consumeAsFlow can be collected only once".toString());
                    }
                    collector = flowCollector;
                    it = streamFlow.stream.iterator();
                    break;
                case 1:
                    it = (Iterator) c131812.L$2;
                    collector = (FlowCollector) c131812.L$1;
                    streamFlow = (StreamFlow) c131812.L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            while (it.hasNext()) {
                T next = it.next();
                c131812.L$0 = streamFlow;
                c131812.L$1 = collector;
                c131812.L$2 = it;
                c131812.label = 1;
                if (collector.emit(next, c131812) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
            streamFlow.stream.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            streamFlow.stream.close();
            throw th;
        }
    }
}
