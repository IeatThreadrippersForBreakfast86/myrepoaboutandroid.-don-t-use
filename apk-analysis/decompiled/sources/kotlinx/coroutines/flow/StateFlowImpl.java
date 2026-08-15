package kotlinx.coroutines.flow;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlow;
import kotlinx.coroutines.flow.internal.FusibleFlow;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.internal.Symbol;

/* compiled from: StateFlow.kt */
@Metadata(m145d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\b\u0012\u0004\u0012\u0002H\u00010\u0006B\r\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u001c\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u0019H\u0096@¢\u0006\u0002\u0010\u001aJ\u001d\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00028\u00002\u0006\u0010\u001e\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001fJ\b\u0010 \u001a\u00020\u0003H\u0014J\u001d\u0010!\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\"2\u0006\u0010#\u001a\u00020\u0011H\u0014¢\u0006\u0002\u0010$J\u0016\u0010%\u001a\u00020&2\u0006\u0010\u0012\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010'J&\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00112\u0006\u0010-\u001a\u00020.H\u0016J\b\u0010/\u001a\u00020&H\u0016J\u0015\u00100\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\u001a\u00102\u001a\u00020\u001c2\b\u00103\u001a\u0004\u0018\u00010\b2\u0006\u00104\u001a\u00020\bH\u0002R\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000bX\u0082\u0004R\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u0012\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00028\u00008V@VX\u0096\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\t¨\u00065"}, m146d2 = {"Lkotlinx/coroutines/flow/StateFlowImpl;", "T", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlow;", "Lkotlinx/coroutines/flow/StateFlowSlot;", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lkotlinx/coroutines/flow/CancellableFlow;", "Lkotlinx/coroutines/flow/internal/FusibleFlow;", "initialState", "", "(Ljava/lang/Object;)V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "replayCache", "", "getReplayCache", "()Ljava/util/List;", "sequence", "", "value", "getValue", "()Ljava/lang/Object;", "setValue", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "compareAndSet", "", "expect", "update", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "createSlot", "createSlotArray", "", "size", "(I)[Lkotlinx/coroutines/flow/StateFlowSlot;", "emit", "", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fuse", "Lkotlinx/coroutines/flow/Flow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "resetReplayCache", "tryEmit", "(Ljava/lang/Object;)Z", "updateState", "expectedState", "newState", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
final class StateFlowImpl<T> extends AbstractSharedFlow<StateFlowSlot> implements MutableStateFlow<T>, CancellableFlow<T>, FusibleFlow<T> {
    private static final /* synthetic */ AtomicReferenceFieldUpdater _state$volatile$FU = AtomicReferenceFieldUpdater.newUpdater(StateFlowImpl.class, Object.class, "_state$volatile");
    private volatile /* synthetic */ Object _state$volatile;
    private int sequence;

    /* compiled from: StateFlow.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.StateFlowImpl", m162f = "StateFlow.kt", m163i = {0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2}, m164l = {384, 396, TypedValues.CycleType.TYPE_CURVE_FIT}, m165m = "collect", m166n = {"this", "collector", "slot", "this", "collector", "slot", "collectorJob", "newState", "this", "collector", "slot", "collectorJob", "oldState"}, m167s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$0", "L$1", "L$2", "L$3", "L$4"})
    /* renamed from: kotlinx.coroutines.flow.StateFlowImpl$collect$1 */
    static final class C12941 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;
        final /* synthetic */ StateFlowImpl<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C12941(StateFlowImpl<T> stateFlowImpl, Continuation<? super C12941> continuation) {
            super(continuation);
            this.this$0 = stateFlowImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.collect(null, this);
        }
    }

    private final /* synthetic */ Object get_state$volatile() {
        return this._state$volatile;
    }

    private final /* synthetic */ void set_state$volatile(Object obj) {
        this._state$volatile = obj;
    }

    public StateFlowImpl(Object initialState) {
        this._state$volatile = initialState;
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow, kotlinx.coroutines.flow.StateFlow
    public T getValue() {
        Symbol symbol = NullSurrogateKt.NULL;
        T t = (T) _state$volatile$FU.get(this);
        if (t == symbol) {
            return null;
        }
        return t;
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow
    public void setValue(T t) {
        updateState(null, t == null ? NullSurrogateKt.NULL : t);
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow
    public boolean compareAndSet(T expect, T update) {
        return updateState(expect == null ? NullSurrogateKt.NULL : expect, update == null ? NullSurrogateKt.NULL : update);
    }

    private final boolean updateState(Object expectedState, Object newState) {
        synchronized (this) {
            Object oldState = _state$volatile$FU.get(this);
            if (expectedState != null && !Intrinsics.areEqual(oldState, expectedState)) {
                return false;
            }
            if (Intrinsics.areEqual(oldState, newState)) {
                return true;
            }
            _state$volatile$FU.set(this, newState);
            int curSequence = this.sequence;
            if ((curSequence & 1) == 0) {
                int curSequence2 = curSequence + 1;
                this.sequence = curSequence2;
                Object curSlots = getSlots();
                Unit unit = Unit.INSTANCE;
                while (true) {
                    StateFlowSlot[] stateFlowSlotArr = (StateFlowSlot[]) curSlots;
                    if (stateFlowSlotArr != null) {
                        for (StateFlowSlot stateFlowSlot : stateFlowSlotArr) {
                            if (stateFlowSlot != null) {
                                stateFlowSlot.makePending();
                            }
                        }
                    }
                    synchronized (this) {
                        if (this.sequence == curSequence2) {
                            this.sequence = curSequence2 + 1;
                            return true;
                        }
                        curSequence2 = this.sequence;
                        curSlots = getSlots();
                        Unit unit2 = Unit.INSTANCE;
                    }
                }
            } else {
                this.sequence = curSequence + 2;
                return true;
            }
        }
    }

    @Override // kotlinx.coroutines.flow.SharedFlow
    public List<T> getReplayCache() {
        return CollectionsKt.listOf(getValue());
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public boolean tryEmit(T value) {
        setValue(value);
        return true;
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow, kotlinx.coroutines.flow.FlowCollector
    public Object emit(T t, Continuation<? super Unit> continuation) {
        setValue(t);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public void resetReplayCache() {
        throw new UnsupportedOperationException("MutableStateFlow.resetReplayCache is not supported");
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00ae A[Catch: all -> 0x00f7, TryCatch #0 {all -> 0x00f7, blocks: (B:13:0x003f, B:29:0x00a4, B:31:0x00ae, B:33:0x00b3, B:44:0x00dc, B:46:0x00e2, B:35:0x00b9, B:39:0x00c1, B:16:0x0059, B:19:0x006c, B:28:0x0094, B:22:0x007c, B:24:0x0080), top: B:53:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b3 A[Catch: all -> 0x00f7, TryCatch #0 {all -> 0x00f7, blocks: (B:13:0x003f, B:29:0x00a4, B:31:0x00ae, B:33:0x00b3, B:44:0x00dc, B:46:0x00e2, B:35:0x00b9, B:39:0x00c1, B:16:0x0059, B:19:0x006c, B:28:0x0094, B:22:0x007c, B:24:0x0080), top: B:53:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e2 A[Catch: all -> 0x00f7, TRY_LEAVE, TryCatch #0 {all -> 0x00f7, blocks: (B:13:0x003f, B:29:0x00a4, B:31:0x00ae, B:33:0x00b3, B:44:0x00dc, B:46:0x00e2, B:35:0x00b9, B:39:0x00c1, B:16:0x0059, B:19:0x006c, B:28:0x0094, B:22:0x007c, B:24:0x0080), top: B:53:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x00e0 -> B:29:0x00a4). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x00f3 -> B:29:0x00a4). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.flow.SharedFlow, kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<?> continuation) {
        C12941 c12941;
        StateFlowImpl stateFlowImpl;
        StateFlowSlot slot;
        Job collectorJob;
        Object oldState;
        Object newState;
        FlowCollector<? super T> flowCollector2;
        Object newState2;
        Object obj;
        if (continuation instanceof C12941) {
            c12941 = (C12941) continuation;
            if ((c12941.label & Integer.MIN_VALUE) != 0) {
                c12941.label -= Integer.MIN_VALUE;
            } else {
                c12941 = new C12941(this, continuation);
            }
        }
        C12941 c129412 = c12941;
        Object $result = c129412.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c129412.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    stateFlowImpl = this;
                    slot = stateFlowImpl.allocateSlot();
                    if (flowCollector instanceof SubscribedFlowCollector) {
                        c129412.L$0 = stateFlowImpl;
                        c129412.L$1 = flowCollector;
                        c129412.L$2 = slot;
                        c129412.label = 1;
                        if (((SubscribedFlowCollector) flowCollector).onSubscription(c129412) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    collectorJob = (Job) c129412.get$context().get(Job.INSTANCE);
                    oldState = null;
                    newState2 = _state$volatile$FU.get(stateFlowImpl);
                    if (collectorJob != null) {
                        JobKt.ensureActive(collectorJob);
                    }
                    if (oldState != null || !Intrinsics.areEqual(oldState, newState2)) {
                        Object oldState2 = NullSurrogateKt.NULL;
                        obj = newState2 == oldState2 ? null : newState2;
                        c129412.L$0 = stateFlowImpl;
                        c129412.L$1 = flowCollector;
                        c129412.L$2 = slot;
                        c129412.L$3 = collectorJob;
                        c129412.L$4 = newState2;
                        c129412.label = 2;
                        if (flowCollector.emit(obj, c129412) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        flowCollector2 = flowCollector;
                        newState = newState2;
                        FlowCollector<? super T> flowCollector3 = flowCollector2;
                        oldState = newState;
                        flowCollector = flowCollector3;
                    }
                    if (!slot.takePending()) {
                        c129412.L$0 = stateFlowImpl;
                        c129412.L$1 = flowCollector;
                        c129412.L$2 = slot;
                        c129412.L$3 = collectorJob;
                        c129412.L$4 = oldState;
                        c129412.label = 3;
                        if (slot.awaitPending(c129412) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    newState2 = _state$volatile$FU.get(stateFlowImpl);
                    if (collectorJob != null) {
                    }
                    if (oldState != null) {
                    }
                    Object oldState22 = NullSurrogateKt.NULL;
                    if (newState2 == oldState22) {
                    }
                    c129412.L$0 = stateFlowImpl;
                    c129412.L$1 = flowCollector;
                    c129412.L$2 = slot;
                    c129412.L$3 = collectorJob;
                    c129412.L$4 = newState2;
                    c129412.label = 2;
                    if (flowCollector.emit(obj, c129412) == coroutine_suspended) {
                    }
                case 1:
                    Object newState3 = c129412.L$2;
                    slot = (StateFlowSlot) newState3;
                    flowCollector = (FlowCollector) c129412.L$1;
                    stateFlowImpl = (StateFlowImpl) c129412.L$0;
                    ResultKt.throwOnFailure($result);
                    collectorJob = (Job) c129412.get$context().get(Job.INSTANCE);
                    oldState = null;
                    newState2 = _state$volatile$FU.get(stateFlowImpl);
                    if (collectorJob != null) {
                    }
                    if (oldState != null) {
                    }
                    Object oldState222 = NullSurrogateKt.NULL;
                    if (newState2 == oldState222) {
                    }
                    c129412.L$0 = stateFlowImpl;
                    c129412.L$1 = flowCollector;
                    c129412.L$2 = slot;
                    c129412.L$3 = collectorJob;
                    c129412.L$4 = newState2;
                    c129412.label = 2;
                    if (flowCollector.emit(obj, c129412) == coroutine_suspended) {
                    }
                    break;
                case 2:
                    newState = c129412.L$4;
                    collectorJob = (Job) c129412.L$3;
                    slot = (StateFlowSlot) c129412.L$2;
                    flowCollector2 = (FlowCollector) c129412.L$1;
                    stateFlowImpl = (StateFlowImpl) c129412.L$0;
                    ResultKt.throwOnFailure($result);
                    FlowCollector<? super T> flowCollector32 = flowCollector2;
                    oldState = newState;
                    flowCollector = flowCollector32;
                    if (!slot.takePending()) {
                    }
                    newState2 = _state$volatile$FU.get(stateFlowImpl);
                    if (collectorJob != null) {
                    }
                    if (oldState != null) {
                    }
                    Object oldState2222 = NullSurrogateKt.NULL;
                    if (newState2 == oldState2222) {
                    }
                    c129412.L$0 = stateFlowImpl;
                    c129412.L$1 = flowCollector;
                    c129412.L$2 = slot;
                    c129412.L$3 = collectorJob;
                    c129412.L$4 = newState2;
                    c129412.label = 2;
                    if (flowCollector.emit(obj, c129412) == coroutine_suspended) {
                    }
                    break;
                case 3:
                    Object oldState3 = c129412.L$4;
                    collectorJob = (Job) c129412.L$3;
                    slot = (StateFlowSlot) c129412.L$2;
                    FlowCollector<? super T> flowCollector4 = (FlowCollector) c129412.L$1;
                    stateFlowImpl = (StateFlowImpl) c129412.L$0;
                    ResultKt.throwOnFailure($result);
                    oldState = oldState3;
                    flowCollector = flowCollector4;
                    newState2 = _state$volatile$FU.get(stateFlowImpl);
                    if (collectorJob != null) {
                    }
                    if (oldState != null) {
                    }
                    Object oldState22222 = NullSurrogateKt.NULL;
                    if (newState2 == oldState22222) {
                    }
                    c129412.L$0 = stateFlowImpl;
                    c129412.L$1 = flowCollector;
                    c129412.L$2 = slot;
                    c129412.L$3 = collectorJob;
                    c129412.L$4 = newState2;
                    c129412.label = 2;
                    if (flowCollector.emit(obj, c129412) == coroutine_suspended) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            stateFlowImpl.freeSlot(slot);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public StateFlowSlot createSlot() {
        return new StateFlowSlot();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public StateFlowSlot[] createSlotArray(int size) {
        return new StateFlowSlot[size];
    }

    @Override // kotlinx.coroutines.flow.internal.FusibleFlow
    public Flow<T> fuse(CoroutineContext context, int capacity, BufferOverflow onBufferOverflow) {
        return StateFlowKt.fuseStateFlow(this, context, capacity, onBufferOverflow);
    }
}
