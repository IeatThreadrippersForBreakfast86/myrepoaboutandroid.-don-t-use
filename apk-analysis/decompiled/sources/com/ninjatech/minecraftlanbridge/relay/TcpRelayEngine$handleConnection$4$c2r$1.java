package com.ninjatech.minecraftlanbridge.relay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: TcpRelayEngine.kt */
@Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.TcpRelayEngine$handleConnection$4$c2r$1", m162f = "TcpRelayEngine.kt", m163i = {}, m164l = {351}, m165m = "invokeSuspend", m166n = {}, m167s = {})
/* loaded from: classes3.dex */
final class TcpRelayEngine$handleConnection$4$c2r$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Socket $client;
    final /* synthetic */ Ref.ObjectRef<Socket> $remote;
    int label;
    final /* synthetic */ TcpRelayEngine this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    TcpRelayEngine$handleConnection$4$c2r$1(TcpRelayEngine tcpRelayEngine, Socket socket, Ref.ObjectRef<Socket> objectRef, Continuation<? super TcpRelayEngine$handleConnection$4$c2r$1> continuation) {
        super(2, continuation);
        this.this$0 = tcpRelayEngine;
        this.$client = socket;
        this.$remote = objectRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new TcpRelayEngine$handleConnection$4$c2r$1(this.this$0, this.$client, this.$remote, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((TcpRelayEngine$handleConnection$4$c2r$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws IOException {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                TcpRelayEngine tcpRelayEngine = this.this$0;
                InputStream inputStream = this.$client.getInputStream();
                Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                OutputStream outputStream = this.$remote.element.getOutputStream();
                Intrinsics.checkNotNullExpressionValue(outputStream, "getOutputStream(...)");
                this.label = 1;
                if (tcpRelayEngine.copy(inputStream, outputStream, this) != coroutine_suspended) {
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
