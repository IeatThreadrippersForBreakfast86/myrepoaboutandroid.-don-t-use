package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: BufferedChannel.kt */
@Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "kotlinx.coroutines.channels.BufferedChannel", m162f = "BufferedChannel.kt", m163i = {0, 0, 0, 0}, m164l = {3087}, m165m = "receiveCatchingOnNoWaiterSuspend-GKJJFZk", m166n = {"this", "segment", "index", "r"}, m167s = {"L$0", "L$1", "I$0", "J$0"})
/* loaded from: classes.dex */
final class BufferedChannel$receiveCatchingOnNoWaiterSuspend$1 extends ContinuationImpl {
    int I$0;
    long J$0;
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ BufferedChannel<E> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BufferedChannel$receiveCatchingOnNoWaiterSuspend$1(BufferedChannel<E> bufferedChannel, Continuation<? super BufferedChannel$receiveCatchingOnNoWaiterSuspend$1> continuation) {
        super(continuation);
        this.this$0 = bufferedChannel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) throws Throwable {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        Object objM1760receiveCatchingOnNoWaiterSuspendGKJJFZk = this.this$0.m1760receiveCatchingOnNoWaiterSuspendGKJJFZk(null, 0, 0L, this);
        return objM1760receiveCatchingOnNoWaiterSuspendGKJJFZk == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objM1760receiveCatchingOnNoWaiterSuspendGKJJFZk : ChannelResult.m1768boximpl(objM1760receiveCatchingOnNoWaiterSuspendGKJJFZk);
    }
}
