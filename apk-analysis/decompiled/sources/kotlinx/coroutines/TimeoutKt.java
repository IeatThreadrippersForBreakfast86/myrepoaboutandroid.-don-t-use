package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlinx.coroutines.intrinsics.UndispatchedKt;

/* compiled from: Timeout.kt */
@Metadata(m145d1 = {"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0000\u001a\\\u0010\b\u001a\u0004\u0018\u00010\t\"\u0004\b\u0000\u0010\n\"\b\b\u0001\u0010\u000b*\u0002H\n2\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\n\u0012\u0004\u0012\u0002H\u000b0\f2'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0002¢\u0006\u0002\u0010\u0012\u001aR\u0010\u0013\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0014\u001a\u00020\u00032'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0015\u001aW\u0010\u0013\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0016\u001a\u00020\u00172'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@ø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0004\b\u0018\u0010\u0015\u001aG\u0010\u0019\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0014\u001a\u00020\u00032'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@¢\u0006\u0002\u0010\u0015\u001aL\u0010\u0019\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\u0016\u001a\u00020\u00172'\u0010\r\u001a#\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e¢\u0006\u0002\b\u0011H\u0086@ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0015\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u001b"}, m146d2 = {"TimeoutCancellationException", "Lkotlinx/coroutines/TimeoutCancellationException;", "time", "", "delay", "Lkotlinx/coroutines/Delay;", "coroutine", "Lkotlinx/coroutines/Job;", "setupTimeout", "", "U", "T", "Lkotlinx/coroutines/TimeoutCoroutine;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/TimeoutCoroutine;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "withTimeout", "timeMillis", "(JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "timeout", "Lkotlin/time/Duration;", "withTimeout-KLykuaI", "withTimeoutOrNull", "withTimeoutOrNull-KLykuaI", "kotlinx-coroutines-core"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class TimeoutKt {

    /* compiled from: Timeout.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.TimeoutKt", m162f = "Timeout.kt", m163i = {0, 0, 0}, m164l = {101}, m165m = "withTimeoutOrNull", m166n = {"block", "coroutine", "timeMillis"}, m167s = {"L$0", "L$1", "J$0"})
    /* renamed from: kotlinx.coroutines.TimeoutKt$withTimeoutOrNull$1 */
    static final class C11191<T> extends ContinuationImpl {
        long J$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11191(Continuation<? super C11191> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TimeoutKt.withTimeoutOrNull(0L, null, this);
        }
    }

    public static final <T> Object withTimeout(long timeMillis, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        if (timeMillis <= 0) {
            throw new TimeoutCancellationException("Timed out immediately");
        }
        Object obj = setupTimeout(new TimeoutCoroutine(timeMillis, continuation), function2);
        if (obj == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return obj;
    }

    /* renamed from: withTimeout-KLykuaI, reason: not valid java name */
    public static final <T> Object m1754withTimeoutKLykuaI(long timeout, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        return withTimeout(DelayKt.m1746toDelayMillisLRDsOJo(timeout), function2, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r6v0, types: [T, kotlinx.coroutines.TimeoutCoroutine] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <T> Object withTimeoutOrNull(long j, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        C11191 c11191;
        Ref.ObjectRef objectRef;
        if (continuation instanceof C11191) {
            c11191 = (C11191) continuation;
            if ((c11191.label & Integer.MIN_VALUE) != 0) {
                c11191.label -= Integer.MIN_VALUE;
            } else {
                c11191 = new C11191(continuation);
            }
        }
        C11191 c111912 = c11191;
        Object obj = c111912.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c111912.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                if (j > 0) {
                    Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
                    try {
                        c111912.L$0 = function2;
                        c111912.L$1 = objectRef2;
                        c111912.J$0 = j;
                        c111912.label = 1;
                        ?? r6 = (T) new TimeoutCoroutine(j, c111912);
                        objectRef2.element = r6;
                        Object obj2 = setupTimeout(r6, function2);
                        if (obj2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            DebugProbesKt.probeCoroutineSuspended(c111912);
                        }
                        return obj2 == coroutine_suspended ? coroutine_suspended : obj2;
                    } catch (TimeoutCancellationException e) {
                        e = e;
                        objectRef = objectRef2;
                        break;
                    }
                } else {
                    return null;
                }
            case 1:
                long j2 = c111912.J$0;
                objectRef = (Ref.ObjectRef) c111912.L$1;
                try {
                    ResultKt.throwOnFailure(obj);
                    return obj;
                } catch (TimeoutCancellationException e2) {
                    e = e2;
                    break;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (e.coroutine == objectRef.element) {
            return null;
        }
        throw e;
    }

    /* renamed from: withTimeoutOrNull-KLykuaI, reason: not valid java name */
    public static final <T> Object m1755withTimeoutOrNullKLykuaI(long timeout, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        return withTimeoutOrNull(DelayKt.m1746toDelayMillisLRDsOJo(timeout), function2, continuation);
    }

    private static final <U, T extends U> Object setupTimeout(TimeoutCoroutine<U, ? super T> timeoutCoroutine, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        Continuation cont = timeoutCoroutine.uCont;
        CoroutineContext context = cont.get$context();
        JobKt.disposeOnCompletion(timeoutCoroutine, DelayKt.getDelay(context).invokeOnTimeout(timeoutCoroutine.time, timeoutCoroutine, timeoutCoroutine.get$context()));
        return UndispatchedKt.startUndispatchedOrReturnIgnoreTimeout(timeoutCoroutine, timeoutCoroutine, function2);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final TimeoutCancellationException TimeoutCancellationException(long time, Delay delay, Job coroutine) {
        String message;
        DelayWithTimeoutDiagnostics delayWithTimeoutDiagnostics = delay instanceof DelayWithTimeoutDiagnostics ? (DelayWithTimeoutDiagnostics) delay : null;
        if (delayWithTimeoutDiagnostics != null) {
            Duration.Companion companion = Duration.INSTANCE;
            message = delayWithTimeoutDiagnostics.m1747timeoutMessageLRDsOJo(DurationKt.toDuration(time, DurationUnit.MILLISECONDS));
            if (message == null) {
                message = "Timed out waiting for " + time + " ms";
            }
        }
        return new TimeoutCancellationException(message, coroutine);
    }
}
