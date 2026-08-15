package com.ninjatech.minecraftlanbridge.relay;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;

/* compiled from: TcpRelayEngine.kt */
@Metadata(m145d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u0002\u0010\u0000\u001a\u00020\u0001H\u008a@"}, m146d2 = {"<anonymous>", ""}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$4$1$1", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
/* loaded from: classes3.dex */
final class TcpRelayEngine$handleConnection$4$1$1 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    int label;

    TcpRelayEngine$handleConnection$4$1$1(Continuation<? super TcpRelayEngine$handleConnection$4$1$1> continuation) {
        super(1, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Continuation<?> continuation) {
        return new TcpRelayEngine$handleConnection$4$1$1(continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super Unit> continuation) {
        return ((TcpRelayEngine$handleConnection$4$1$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
