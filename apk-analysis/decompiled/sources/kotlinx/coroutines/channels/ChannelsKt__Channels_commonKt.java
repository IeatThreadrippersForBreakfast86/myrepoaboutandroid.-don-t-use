package kotlinx.coroutines.channels;

import java.util.List;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExceptionsKt;
import kotlinx.coroutines.selects.SelectClause1;

/* compiled from: Channels.common.kt */
@Metadata(m145d1 = {"\u00008\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\u001a\u001a\u0010\u0002\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0001\u001aP\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\t0\u00042\u001d\u0010\n\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0004\u0012\u0004\u0012\u0002H\b0\u000b¢\u0006\u0002\b\fH\u0086\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\r\u001a2\u0010\u000e\u001a\u00020\u0003\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u00042\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\u00030\u000bH\u0086H¢\u0006\u0002\u0010\u0010\u001a$\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\u0012\"\b\b\u0000\u0010\t*\u00020\u0013*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0007\u001a$\u0010\u0014\u001a\u0004\u0018\u0001H\t\"\b\b\u0000\u0010\t*\u00020\u0013*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0087@¢\u0006\u0002\u0010\u0015\u001a$\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\t0\u0017\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0086@¢\u0006\u0002\u0010\u0015\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000¨\u0006\u0018"}, m146d2 = {"DEFAULT_CLOSE_MESSAGE", "", "cancelConsumed", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "cause", "", "consume", "R", "E", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "consumeEach", "action", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onReceiveOrNull", "Lkotlinx/coroutines/selects/SelectClause1;", "", "receiveOrNull", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toList", "", "kotlinx-coroutines-core"}, m147k = 5, m148mv = {1, 9, 0}, m150xi = 48, m151xs = "kotlinx/coroutines/channels/ChannelsKt")
/* loaded from: classes.dex */
final /* synthetic */ class ChannelsKt__Channels_commonKt {

    /* compiled from: Channels.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 176)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", m162f = "Channels.common.kt", m163i = {0, 0}, m164l = {82}, m165m = "consumeEach", m166n = {"action", "$this$consume$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1 */
    static final class C11321<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11321(Continuation<? super C11321> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__Channels_commonKt.consumeEach(null, null, this);
        }
    }

    /* compiled from: Channels.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", m162f = "Channels.common.kt", m163i = {0, 0}, m164l = {112}, m165m = "toList", m166n = {"$this$toList_u24lambda_u243", "$this$consume$iv$iv"}, m167s = {"L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$toList$1 */
    static final class C11331<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C11331(Continuation<? super C11331> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toList(null, this);
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'receiveCatching'", replaceWith = @ReplaceWith(expression = "receiveCatching().getOrNull()", imports = {}))
    public static final /* synthetic */ Object receiveOrNull(ReceiveChannel $this$receiveOrNull, Continuation $completion) {
        Intrinsics.checkNotNull($this$receiveOrNull, "null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E of kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.receiveOrNull?>");
        return $this$receiveOrNull.receiveOrNull($completion);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Deprecated in the favour of 'onReceiveCatching'")
    public static final /* synthetic */ SelectClause1 onReceiveOrNull(ReceiveChannel $this$onReceiveOrNull) {
        Intrinsics.checkNotNull($this$onReceiveOrNull, "null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E of kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt.onReceiveOrNull?>");
        return $this$onReceiveOrNull.getOnReceiveOrNull();
    }

    public static final <E, R> R consume(ReceiveChannel<? extends E> receiveChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        try {
            R rInvoke = function1.invoke(receiveChannel);
            InlineMarker.finallyStart(1);
            ChannelsKt.cancelConsumed(receiveChannel, null);
            InlineMarker.finallyEnd(1);
            return rInvoke;
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0081 A[Catch: all -> 0x00a0, TryCatch #0 {all -> 0x00a0, blocks: (B:24:0x0079, B:26:0x0081, B:27:0x0090), top: B:39:0x0079 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0090 A[Catch: all -> 0x00a0, TRY_LEAVE, TryCatch #0 {all -> 0x00a0, blocks: (B:24:0x0079, B:26:0x0081, B:27:0x0090), top: B:39:0x0079 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0070 -> B:39:0x0079). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object consumeEach(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        C11321 c11321;
        ReceiveChannel $this$consume$iv;
        Object $result;
        Function1 action;
        ReceiveChannel $this$consume$iv2;
        Throwable cause$iv;
        ChannelIterator channelIterator;
        int i;
        Object obj;
        if (continuation instanceof C11321) {
            c11321 = (C11321) continuation;
            if ((c11321.label & Integer.MIN_VALUE) != 0) {
                c11321.label -= Integer.MIN_VALUE;
            } else {
                c11321 = new C11321(continuation);
            }
        }
        C11321 c113212 = c11321;
        Object e = c113212.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c113212.label) {
            case 0:
                ResultKt.throwOnFailure(e);
                $this$consume$iv = receiveChannel;
                Throwable cause$iv2 = null;
                try {
                    ChannelIterator it = $this$consume$iv.iterator();
                    int $i$f$consumeEach = 0;
                    Function1 action2 = function1;
                    c113212.L$0 = action2;
                    c113212.L$1 = $this$consume$iv;
                    c113212.L$2 = it;
                    c113212.label = 1;
                    Object objHasNext = it.hasNext(c113212);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = e;
                    e = objHasNext;
                    action = action2;
                    $this$consume$iv2 = $this$consume$iv;
                    cause$iv = cause$iv2;
                    channelIterator = it;
                    i = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (((Boolean) e).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv);
                            InlineMarker.finallyEnd(1);
                            return Unit.INSTANCE;
                        }
                        action.invoke(channelIterator.next());
                        e = $result;
                        $result2 = obj;
                        $i$f$consumeEach = i;
                        it = channelIterator;
                        cause$iv2 = cause$iv;
                        $this$consume$iv = $this$consume$iv2;
                        action2 = action;
                        c113212.L$0 = action2;
                        c113212.L$1 = $this$consume$iv;
                        c113212.L$2 = it;
                        c113212.label = 1;
                        Object objHasNext2 = it.hasNext(c113212);
                        if (objHasNext2 != $result2) {
                        }
                    } catch (Throwable th) {
                        $this$consume$iv = $this$consume$iv2;
                        e$iv = th;
                        Throwable cause$iv3 = e$iv;
                        try {
                            throw e$iv;
                        } catch (Throwable e$iv) {
                            InlineMarker.finallyStart(1);
                            ChannelsKt.cancelConsumed($this$consume$iv, cause$iv3);
                            InlineMarker.finallyEnd(1);
                            throw e$iv;
                        }
                    }
                } catch (Throwable th2) {
                    e$iv = th2;
                    Throwable cause$iv32 = e$iv;
                    throw e$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c113212.L$2;
                $this$consume$iv = (ReceiveChannel) c113212.L$1;
                Function1 action3 = (Function1) c113212.L$0;
                try {
                    ResultKt.throwOnFailure(e);
                    action = action3;
                    $this$consume$iv2 = $this$consume$iv;
                    cause$iv = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    obj = $result2;
                    $result = e;
                    if (((Boolean) e).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv = th3;
                    Throwable cause$iv322 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    private static final <E> Object consumeEach$$forInline(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        try {
            ReceiveChannel<? extends E> $this$consumeEach_u24lambda_u241 = receiveChannel;
            ChannelIterator<? extends E> it = $this$consumeEach_u24lambda_u241.iterator();
            while (true) {
                InlineMarker.mark(3);
                InlineMarker.mark(0);
                Object objHasNext = it.hasNext(null);
                InlineMarker.mark(1);
                if (!((Boolean) objHasNext).booleanValue()) {
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel, null);
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                }
                Object e = it.next();
                function1.invoke(e);
            }
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0081 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0096 A[Catch: all -> 0x00ba, TryCatch #3 {all -> 0x00ba, blocks: (B:24:0x008e, B:26:0x0096, B:27:0x00ab), top: B:45:0x008e }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ab A[Catch: all -> 0x00ba, TRY_LEAVE, TryCatch #3 {all -> 0x00ba, blocks: (B:24:0x008e, B:26:0x0096, B:27:0x00ab), top: B:45:0x008e }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0082 -> B:45:0x008e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object toList(ReceiveChannel<? extends E> receiveChannel, Continuation<? super List<? extends E>> continuation) {
        C11331 c11331;
        ReceiveChannel $this$consume$iv$iv;
        Object $result;
        List list;
        List list2;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv;
        ChannelIterator channelIterator;
        int i;
        List list3;
        int $i$f$consume;
        Object obj;
        if (continuation instanceof C11331) {
            c11331 = (C11331) continuation;
            if ((c11331.label & Integer.MIN_VALUE) != 0) {
                c11331.label -= Integer.MIN_VALUE;
            } else {
                c11331 = new C11331(continuation);
            }
        }
        C11331 c113312 = c11331;
        Object e$iv = c113312.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c113312.label) {
            case 0:
                ResultKt.throwOnFailure(e$iv);
                List $this$toList_u24lambda_u243 = CollectionsKt.createListBuilder();
                $this$consume$iv$iv = receiveChannel;
                Throwable cause$iv$iv2 = null;
                try {
                    List $this$toList_u24lambda_u2432 = $this$toList_u24lambda_u243;
                    List $this$toList_u24lambda_u2433 = null;
                    int $i$f$consume2 = 0;
                    List $this$toList_u24lambda_u2434 = $this$toList_u24lambda_u243;
                    int $i$f$consumeEach = 0;
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    c113312.L$0 = $this$toList_u24lambda_u2434;
                    c113312.L$1 = $this$toList_u24lambda_u2432;
                    c113312.L$2 = $this$consume$iv$iv;
                    c113312.L$3 = it;
                    c113312.label = 1;
                    Object objHasNext = it.hasNext(c113312);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = e$iv;
                    e$iv = objHasNext;
                    list = $this$toList_u24lambda_u2434;
                    list2 = $this$toList_u24lambda_u2432;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = cause$iv$iv2;
                    channelIterator = it;
                    i = $i$f$consume2;
                    list3 = $this$toList_u24lambda_u2433;
                    $i$f$consume = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (((Boolean) e$iv).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv);
                            return CollectionsKt.build(list);
                        }
                        list2.add(channelIterator.next());
                        e$iv = $result;
                        $result2 = obj;
                        $i$f$consumeEach = $i$f$consume;
                        $this$toList_u24lambda_u2433 = list3;
                        $i$f$consume2 = i;
                        it = channelIterator;
                        cause$iv$iv2 = cause$iv$iv;
                        $this$consume$iv$iv = $this$consume$iv$iv2;
                        $this$toList_u24lambda_u2432 = list2;
                        $this$toList_u24lambda_u2434 = list;
                        c113312.L$0 = $this$toList_u24lambda_u2434;
                        c113312.L$1 = $this$toList_u24lambda_u2432;
                        c113312.L$2 = $this$consume$iv$iv;
                        c113312.L$3 = it;
                        c113312.label = 1;
                        Object objHasNext2 = it.hasNext(c113312);
                        if (objHasNext2 != $result2) {
                        }
                    } catch (Throwable th) {
                        $this$consume$iv$iv = $this$consume$iv$iv2;
                        e$iv$iv = th;
                        Throwable cause$iv$iv3 = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } catch (Throwable e$iv$iv) {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv3);
                            throw e$iv$iv;
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    Throwable cause$iv$iv32 = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c113312.L$3;
                $this$consume$iv$iv = (ReceiveChannel) c113312.L$2;
                List $this$toList_u24lambda_u2435 = (List) c113312.L$1;
                List list4 = (List) c113312.L$0;
                try {
                    ResultKt.throwOnFailure(e$iv);
                    list = list4;
                    list2 = $this$toList_u24lambda_u2435;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    list3 = null;
                    $i$f$consume = 0;
                    obj = $result2;
                    $result = e$iv;
                    if (((Boolean) e$iv).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    Throwable cause$iv$iv322 = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static final void cancelConsumed(ReceiveChannel<?> receiveChannel, Throwable cause) {
        if (cause != null) {
            CancellationException = cause instanceof CancellationException ? (CancellationException) cause : null;
            if (CancellationException == null) {
                CancellationException = ExceptionsKt.CancellationException("Channel was consumed, consumer had failed", cause);
            }
        }
        receiveChannel.cancel(CancellationException);
    }
}
