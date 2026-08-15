package com.ninjatech.minecraftlanbridge.relay;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HttpProxyServer.kt */
@Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m146d2 = {"<anonymous>", "Ljava/net/Socket;", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "com.ninjatech.minecraftlanbridge.relay.HttpProxyServer$handleConnection$upstream$1", m162f = "HttpProxyServer.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
/* loaded from: classes3.dex */
final class HttpProxyServer$handleConnection$upstream$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Socket>, Object> {
    final /* synthetic */ int $defaultPort;
    final /* synthetic */ String $host;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    HttpProxyServer$handleConnection$upstream$1(String str, int i, Continuation<? super HttpProxyServer$handleConnection$upstream$1> continuation) {
        super(2, continuation);
        this.$host = str;
        this.$defaultPort = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HttpProxyServer$handleConnection$upstream$1(this.$host, this.$defaultPort, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Socket> continuation) {
        return ((HttpProxyServer$handleConnection$upstream$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) throws IOException {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                Socket $this$invokeSuspend_u24lambda_u240 = new Socket();
                String str = this.$host;
                int i = this.$defaultPort;
                $this$invokeSuspend_u24lambda_u240.setTcpNoDelay(true);
                $this$invokeSuspend_u24lambda_u240.connect(new InetSocketAddress(str, i), 8000);
                return $this$invokeSuspend_u24lambda_u240;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
