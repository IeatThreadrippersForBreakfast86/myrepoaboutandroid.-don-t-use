package kotlinx.coroutines.channels;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt__BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ChannelResult;
import org.xbill.DNS.Type;

/* compiled from: Channels.kt */
@Metadata(m145d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a%\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0005\u001a)\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002¢\u0006\u0002\u0010\b¨\u0006\t"}, m146d2 = {"sendBlocking", "", "E", "Lkotlinx/coroutines/channels/SendChannel;", "element", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)V", "trySendBlocking", "Lkotlinx/coroutines/channels/ChannelResult;", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 5, m148mv = {1, 9, 0}, m150xi = 48, m151xs = "kotlinx/coroutines/channels/ChannelsKt")
/* loaded from: classes.dex */
final /* synthetic */ class ChannelsKt__ChannelsKt {

    /* compiled from: Channels.kt */
    @Metadata(m145d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "Lkotlinx/coroutines/channels/ChannelResult;", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2", m162f = "Channels.kt", m163i = {}, m164l = {39}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2 */
    static final class C11312 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super ChannelResult<? extends Unit>>, Object> {
        final /* synthetic */ E $element;
        final /* synthetic */ SendChannel<E> $this_trySendBlocking;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11312(SendChannel<? super E> sendChannel, E e, Continuation<? super C11312> continuation) {
            super(2, continuation);
            this.$this_trySendBlocking = sendChannel;
            this.$element = e;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11312 c11312 = new C11312(this.$this_trySendBlocking, this.$element, continuation);
            c11312.L$0 = obj;
            return c11312;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super ChannelResult<? extends Unit>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super ChannelResult<Unit>>) continuation);
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super ChannelResult<Unit>> continuation) {
            return ((C11312) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x004f  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0058  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            Throwable th;
            Object r;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    SendChannel<E> sendChannel = this.$this_trySendBlocking;
                    E e = this.$element;
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        this.label = 1;
                    } catch (Throwable th2) {
                        th = th2;
                        Result.Companion companion2 = Result.INSTANCE;
                        r = Result.m255constructorimpl(ResultKt.createFailure(th));
                        return ChannelResult.m1768boximpl(!Result.m262isSuccessimpl(r) ? ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1781closedJP2dKIU(Result.m258exceptionOrNullimpl(r)));
                    }
                    if (sendChannel.send(e, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    r = Result.m255constructorimpl(Unit.INSTANCE);
                    return ChannelResult.m1768boximpl(!Result.m262isSuccessimpl(r) ? ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1781closedJP2dKIU(Result.m258exceptionOrNullimpl(r)));
                case 1:
                    try {
                        ResultKt.throwOnFailure($result);
                        r = Result.m255constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th3) {
                        th = th3;
                        Result.Companion companion22 = Result.INSTANCE;
                        r = Result.m255constructorimpl(ResultKt.createFailure(th));
                        return ChannelResult.m1768boximpl(!Result.m262isSuccessimpl(r) ? ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1781closedJP2dKIU(Result.m258exceptionOrNullimpl(r)));
                    }
                    return ChannelResult.m1768boximpl(!Result.m262isSuccessimpl(r) ? ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE) : ChannelResult.INSTANCE.m1781closedJP2dKIU(Result.m258exceptionOrNullimpl(r)));
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E> Object trySendBlocking(SendChannel<? super E> sendChannel, E e) {
        Object $this$onSuccess_u2dWpGqRn0$iv = sendChannel.mo1757trySendJP2dKIU(e);
        if ($this$onSuccess_u2dWpGqRn0$iv instanceof ChannelResult.Failed) {
            return ((ChannelResult) BuildersKt__BuildersKt.runBlocking$default(null, new C11312(sendChannel, e, null), 1, null)).getHolder();
        }
        return ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'trySendBlocking'. Consider handling the result of 'trySendBlocking' explicitly and rethrow exception if necessary", replaceWith = @ReplaceWith(expression = "trySendBlocking(element)", imports = {}))
    public static final /* synthetic */ void sendBlocking(SendChannel $this$sendBlocking, Object element) throws InterruptedException {
        if (!ChannelResult.m1778isSuccessimpl($this$sendBlocking.mo1757trySendJP2dKIU(element))) {
            BuildersKt__BuildersKt.runBlocking$default(null, new C11301($this$sendBlocking, element, null), 1, null);
        }
    }

    /* compiled from: Channels.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$sendBlocking$1", m162f = "Channels.kt", m163i = {}, m164l = {Type.TALINK}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$sendBlocking$1 */
    static final class C11301 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ E $element;
        final /* synthetic */ SendChannel<E> $this_sendBlocking;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11301(SendChannel<? super E> sendChannel, E e, Continuation<? super C11301> continuation) {
            super(2, continuation);
            this.$this_sendBlocking = sendChannel;
            this.$element = e;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C11301(this.$this_sendBlocking, this.$element, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C11301) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (this.$this_sendBlocking.send(this.$element, this) != coroutine_suspended) {
                        break;
                    } else {
                        return coroutine_suspended;
                    }
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }
}
