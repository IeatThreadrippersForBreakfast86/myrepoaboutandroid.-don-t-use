package kotlinx.coroutines.flow;

import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.DelayKt;

/* compiled from: SharingStarted.kt */
@Metadata(m145d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u000bH\u0017J\b\u0010\u0011\u001a\u00020\u0012H\u0016R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, m146d2 = {"Lkotlinx/coroutines/flow/StartedWhileSubscribed;", "Lkotlinx/coroutines/flow/SharingStarted;", "stopTimeout", "", "replayExpiration", "(JJ)V", "command", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharingCommand;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "equals", "", "other", "", "hashCode", "toString", "", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
final class StartedWhileSubscribed implements SharingStarted {
    private final long replayExpiration;
    private final long stopTimeout;

    public StartedWhileSubscribed(long stopTimeout, long replayExpiration) {
        this.stopTimeout = stopTimeout;
        this.replayExpiration = replayExpiration;
        if (!(this.stopTimeout >= 0)) {
            throw new IllegalArgumentException(("stopTimeout(" + this.stopTimeout + " ms) cannot be negative").toString());
        }
        if (this.replayExpiration >= 0) {
        } else {
            throw new IllegalArgumentException(("replayExpiration(" + this.replayExpiration + " ms) cannot be negative").toString());
        }
    }

    /* compiled from: SharingStarted.kt */
    @Metadata(m145d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlinx/coroutines/flow/SharingCommand;", "count", ""}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.StartedWhileSubscribed$command$1", m162f = "SharingStarted.kt", m163i = {1, 2, 3}, m164l = {174, 176, 178, 179, 181}, m165m = "invokeSuspend", m166n = {"$this$transformLatest", "$this$transformLatest", "$this$transformLatest"}, m167s = {"L$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.flow.StartedWhileSubscribed$command$1 */
    static final class C12921 extends SuspendLambda implements Function3<FlowCollector<? super SharingCommand>, Integer, Continuation<? super Unit>, Object> {
        /* synthetic */ int I$0;
        private /* synthetic */ Object L$0;
        int label;

        C12921(Continuation<? super C12921> continuation) {
            super(3, continuation);
        }

        @Override // kotlin.jvm.functions.Function3
        public /* bridge */ /* synthetic */ Object invoke(FlowCollector<? super SharingCommand> flowCollector, Integer num, Continuation<? super Unit> continuation) {
            return invoke(flowCollector, num.intValue(), continuation);
        }

        public final Object invoke(FlowCollector<? super SharingCommand> flowCollector, int i, Continuation<? super Unit> continuation) {
            C12921 c12921 = StartedWhileSubscribed.this.new C12921(continuation);
            c12921.L$0 = flowCollector;
            c12921.I$0 = i;
            return c12921.invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x009c A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00af A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00b0  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C12921 c12921;
            FlowCollector $this$transformLatest;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c12921 = this;
                    $this$transformLatest = (FlowCollector) c12921.L$0;
                    int count = c12921.I$0;
                    if (count > 0) {
                        c12921.label = 1;
                        return $this$transformLatest.emit(SharingCommand.START, c12921) == coroutine_suspended ? coroutine_suspended : Unit.INSTANCE;
                    }
                    c12921.L$0 = $this$transformLatest;
                    c12921.label = 2;
                    if (DelayKt.delay(StartedWhileSubscribed.this.stopTimeout, c12921) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (StartedWhileSubscribed.this.replayExpiration > 0) {
                        c12921.L$0 = $this$transformLatest;
                        c12921.label = 3;
                        if ($this$transformLatest.emit(SharingCommand.STOP, c12921) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        c12921.L$0 = $this$transformLatest;
                        c12921.label = 4;
                        if (DelayKt.delay(StartedWhileSubscribed.this.replayExpiration, c12921) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    c12921.L$0 = null;
                    c12921.label = 5;
                    if ($this$transformLatest.emit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE, c12921) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                case 1:
                    ResultKt.throwOnFailure($result);
                case 2:
                    c12921 = this;
                    $this$transformLatest = (FlowCollector) c12921.L$0;
                    ResultKt.throwOnFailure($result);
                    if (StartedWhileSubscribed.this.replayExpiration > 0) {
                    }
                    c12921.L$0 = null;
                    c12921.label = 5;
                    if ($this$transformLatest.emit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE, c12921) != coroutine_suspended) {
                    }
                    break;
                case 3:
                    c12921 = this;
                    $this$transformLatest = (FlowCollector) c12921.L$0;
                    ResultKt.throwOnFailure($result);
                    c12921.L$0 = $this$transformLatest;
                    c12921.label = 4;
                    if (DelayKt.delay(StartedWhileSubscribed.this.replayExpiration, c12921) == coroutine_suspended) {
                    }
                    c12921.L$0 = null;
                    c12921.label = 5;
                    if ($this$transformLatest.emit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE, c12921) != coroutine_suspended) {
                    }
                    break;
                case 4:
                    c12921 = this;
                    $this$transformLatest = (FlowCollector) c12921.L$0;
                    ResultKt.throwOnFailure($result);
                    c12921.L$0 = null;
                    c12921.label = 5;
                    if ($this$transformLatest.emit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE, c12921) != coroutine_suspended) {
                    }
                    break;
                case 5:
                    ResultKt.throwOnFailure($result);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Override // kotlinx.coroutines.flow.SharingStarted
    public Flow<SharingCommand> command(StateFlow<Integer> subscriptionCount) {
        return FlowKt.distinctUntilChanged(FlowKt.dropWhile(FlowKt.transformLatest(subscriptionCount, new C12921(null)), new C12932(null)));
    }

    /* compiled from: SharingStarted.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "it", "Lkotlinx/coroutines/flow/SharingCommand;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.StartedWhileSubscribed$command$2", m162f = "SharingStarted.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.flow.StartedWhileSubscribed$command$2 */
    static final class C12932 extends SuspendLambda implements Function2<SharingCommand, Continuation<? super Boolean>, Object> {
        /* synthetic */ Object L$0;
        int label;

        C12932(Continuation<? super C12932> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C12932 c12932 = new C12932(continuation);
            c12932.L$0 = obj;
            return c12932;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SharingCommand sharingCommand, Continuation<? super Boolean> continuation) {
            return ((C12932) create(sharingCommand, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    SharingCommand it = (SharingCommand) this.L$0;
                    return Boxing.boxBoolean(it != SharingCommand.START);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public String toString() {
        List $this$toString_u24lambda_u242 = CollectionsKt.createListBuilder(2);
        if (this.stopTimeout > 0) {
            $this$toString_u24lambda_u242.add("stopTimeout=" + this.stopTimeout + "ms");
        }
        if (this.replayExpiration < Long.MAX_VALUE) {
            $this$toString_u24lambda_u242.add("replayExpiration=" + this.replayExpiration + "ms");
        }
        List params = CollectionsKt.build($this$toString_u24lambda_u242);
        return "SharingStarted.WhileSubscribed(" + CollectionsKt.joinToString$default(params, null, null, null, 0, null, null, 63, null) + ')';
    }

    public boolean equals(Object other) {
        return (other instanceof StartedWhileSubscribed) && this.stopTimeout == ((StartedWhileSubscribed) other).stopTimeout && this.replayExpiration == ((StartedWhileSubscribed) other).replayExpiration;
    }

    public int hashCode() {
        return (Long.hashCode(this.stopTimeout) * 31) + Long.hashCode(this.replayExpiration);
    }
}
