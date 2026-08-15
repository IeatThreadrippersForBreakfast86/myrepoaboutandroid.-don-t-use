package kotlinx.coroutines.flow;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.coroutines.channels.ChannelsKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.ChannelFlowKt;

/* compiled from: Channels.kt */
@Metadata(m145d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a,\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0086@¢\u0006\u0002\u0010\n\u001a6\u0010\u000b\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\f\u001a\u00020\rH\u0082@¢\u0006\u0004\b\u000e\u0010\u000f\u001a$\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005¨\u0006\u0014"}, m146d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "consumeAsFlow", "Lkotlinx/coroutines/channels/ReceiveChannel;", "emitAll", "", "Lkotlinx/coroutines/flow/FlowCollector;", "channel", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitAllImpl", "consume", "", "emitAllImpl$FlowKt__ChannelsKt", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produceIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "receiveAsFlow", "kotlinx-coroutines-core"}, m147k = 5, m148mv = {1, 9, 0}, m150xi = 48, m151xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes.dex */
final /* synthetic */ class FlowKt__ChannelsKt {
    public static final <T> Object emitAll(FlowCollector<? super T> flowCollector, ReceiveChannel<? extends T> receiveChannel, Continuation<? super Unit> continuation) {
        Object objEmitAllImpl$FlowKt__ChannelsKt = emitAllImpl$FlowKt__ChannelsKt(flowCollector, receiveChannel, true, continuation);
        return objEmitAllImpl$FlowKt__ChannelsKt == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAllImpl$FlowKt__ChannelsKt : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0090 A[Catch: all -> 0x00b7, TRY_LEAVE, TryCatch #1 {all -> 0x00b7, blocks: (B:26:0x0088, B:28:0x0090), top: B:49:0x0088 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ae A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v2, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [java.lang.Object, kotlinx.coroutines.flow.FlowCollector] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r7v0, types: [kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.flow.FlowCollector<? super T>] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v24 */
    /* JADX WARN: Type inference failed for: r7v25 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5, types: [boolean] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x00a6 -> B:22:0x006d). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <T> Object emitAllImpl$FlowKt__ChannelsKt(FlowCollector<? super T> flowCollector, ReceiveChannel<? extends T> receiveChannel, boolean z, Continuation<? super Unit> continuation) {
        FlowKt__ChannelsKt$emitAllImpl$1 flowKt__ChannelsKt$emitAllImpl$1;
        Throwable th;
        ChannelIterator it;
        ChannelIterator channelIterator;
        Throwable th2;
        Object obj;
        ReceiveChannel receiveChannel2;
        Object obj2;
        Object obj3;
        Object obj4;
        Object objHasNext;
        if (continuation instanceof FlowKt__ChannelsKt$emitAllImpl$1) {
            flowKt__ChannelsKt$emitAllImpl$1 = (FlowKt__ChannelsKt$emitAllImpl$1) continuation;
            if ((flowKt__ChannelsKt$emitAllImpl$1.label & Integer.MIN_VALUE) != 0) {
                flowKt__ChannelsKt$emitAllImpl$1.label -= Integer.MIN_VALUE;
            } else {
                flowKt__ChannelsKt$emitAllImpl$1 = new FlowKt__ChannelsKt$emitAllImpl$1(continuation);
            }
        }
        FlowKt__ChannelsKt$emitAllImpl$1 flowKt__ChannelsKt$emitAllImpl$12 = flowKt__ChannelsKt$emitAllImpl$1;
        Object obj5 = flowKt__ChannelsKt$emitAllImpl$12.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ?? r2 = flowKt__ChannelsKt$emitAllImpl$12.label;
        try {
            switch (r2) {
                case 0:
                    ResultKt.throwOnFailure(obj5);
                    r2 = receiveChannel;
                    FlowKt.ensureActive(flowCollector);
                    th = null;
                    try {
                        it = r2.iterator();
                        r2 = r2;
                        obj4 = flowCollector;
                        flowKt__ChannelsKt$emitAllImpl$12.L$0 = obj4;
                        flowKt__ChannelsKt$emitAllImpl$12.L$1 = r2;
                        flowKt__ChannelsKt$emitAllImpl$12.L$2 = it;
                        flowKt__ChannelsKt$emitAllImpl$12.Z$0 = z;
                        flowKt__ChannelsKt$emitAllImpl$12.label = 1;
                        objHasNext = it.hasNext(flowKt__ChannelsKt$emitAllImpl$12);
                        if (objHasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        obj = obj4;
                        boolean z2 = (FlowCollector<? super T>) (z ? 1 : 0);
                        th2 = th;
                        channelIterator = it;
                        receiveChannel2 = r2;
                        obj2 = coroutine_suspended;
                        obj3 = obj5;
                        obj5 = objHasNext;
                        flowCollector = z2;
                        try {
                            if (((Boolean) obj5).booleanValue()) {
                                return Unit.INSTANCE;
                            }
                            Object next = channelIterator.next();
                            flowKt__ChannelsKt$emitAllImpl$12.L$0 = obj;
                            flowKt__ChannelsKt$emitAllImpl$12.L$1 = receiveChannel2;
                            flowKt__ChannelsKt$emitAllImpl$12.L$2 = channelIterator;
                            flowKt__ChannelsKt$emitAllImpl$12.Z$0 = (boolean) flowCollector;
                            flowKt__ChannelsKt$emitAllImpl$12.label = 2;
                            if (obj.emit(next, flowKt__ChannelsKt$emitAllImpl$12) == obj2) {
                                return obj2;
                            }
                            obj5 = obj3;
                            coroutine_suspended = obj2;
                            r2 = receiveChannel2;
                            it = channelIterator;
                            th = th2;
                            z = (??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) flowCollector;
                            obj4 = (FlowCollector<? super T>) obj;
                            flowKt__ChannelsKt$emitAllImpl$12.L$0 = obj4;
                            flowKt__ChannelsKt$emitAllImpl$12.L$1 = r2;
                            flowKt__ChannelsKt$emitAllImpl$12.L$2 = it;
                            flowKt__ChannelsKt$emitAllImpl$12.Z$0 = z;
                            flowKt__ChannelsKt$emitAllImpl$12.label = 1;
                            objHasNext = it.hasNext(flowKt__ChannelsKt$emitAllImpl$12);
                            if (objHasNext == coroutine_suspended) {
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            r2 = receiveChannel2;
                            Throwable th4 = th;
                            try {
                                throw th;
                            } finally {
                                if (flowCollector != 0) {
                                    ChannelsKt.cancelConsumed(r2, th4);
                                }
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        flowCollector = (FlowCollector<? super T>) (z ? 1 : 0);
                        Throwable th42 = th;
                        throw th;
                    }
                case 1:
                    boolean z3 = flowKt__ChannelsKt$emitAllImpl$12.Z$0;
                    channelIterator = (ChannelIterator) flowKt__ChannelsKt$emitAllImpl$12.L$2;
                    th2 = null;
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) flowKt__ChannelsKt$emitAllImpl$12.L$1;
                    FlowCollector flowCollector2 = (FlowCollector) flowKt__ChannelsKt$emitAllImpl$12.L$0;
                    ResultKt.throwOnFailure(obj5);
                    obj = flowCollector2;
                    receiveChannel2 = receiveChannel3;
                    obj2 = coroutine_suspended;
                    obj3 = obj5;
                    flowCollector = z3;
                    if (((Boolean) obj5).booleanValue()) {
                    }
                    break;
                case 2:
                    boolean z4 = flowKt__ChannelsKt$emitAllImpl$12.Z$0;
                    ChannelIterator channelIterator2 = (ChannelIterator) flowKt__ChannelsKt$emitAllImpl$12.L$2;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) flowKt__ChannelsKt$emitAllImpl$12.L$1;
                    FlowCollector flowCollector3 = (FlowCollector) flowKt__ChannelsKt$emitAllImpl$12.L$0;
                    ResultKt.throwOnFailure(obj5);
                    z = z4;
                    Object obj6 = (FlowCollector<? super T>) flowCollector3;
                    it = channelIterator2;
                    th = null;
                    r2 = receiveChannel4;
                    obj4 = obj6;
                    flowKt__ChannelsKt$emitAllImpl$12.L$0 = obj4;
                    flowKt__ChannelsKt$emitAllImpl$12.L$1 = r2;
                    flowKt__ChannelsKt$emitAllImpl$12.L$2 = it;
                    flowKt__ChannelsKt$emitAllImpl$12.Z$0 = z;
                    flowKt__ChannelsKt$emitAllImpl$12.label = 1;
                    objHasNext = it.hasNext(flowKt__ChannelsKt$emitAllImpl$12);
                    if (objHasNext == coroutine_suspended) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    public static final <T> Flow<T> receiveAsFlow(ReceiveChannel<? extends T> receiveChannel) {
        return new ChannelAsFlow(receiveChannel, false, null, 0, null, 28, null);
    }

    public static final <T> Flow<T> consumeAsFlow(ReceiveChannel<? extends T> receiveChannel) {
        return new ChannelAsFlow(receiveChannel, true, null, 0, null, 28, null);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "'BroadcastChannel' is obsolete and all corresponding operators are deprecated in the favour of StateFlow and SharedFlow")
    public static final <T> Flow<T> asFlow(final BroadcastChannel<T> broadcastChannel) {
        return new Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__ChannelsKt$asFlow$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
                Object objEmitAll = FlowKt.emitAll(flowCollector, broadcastChannel.openSubscription(), continuation);
                return objEmitAll == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEmitAll : Unit.INSTANCE;
            }
        };
    }

    public static final <T> ReceiveChannel<T> produceIn(Flow<? extends T> flow, CoroutineScope scope) {
        return ChannelFlowKt.asChannelFlow(flow).produceImpl(scope);
    }
}
