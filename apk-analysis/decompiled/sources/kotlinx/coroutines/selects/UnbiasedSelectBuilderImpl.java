package kotlinx.coroutines.selects;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;

/* compiled from: SelectOld.kt */
@Metadata(m145d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0001\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0001J\n\u0010\f\u001a\u0004\u0018\u00010\rH\u0001R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, m146d2 = {"Lkotlinx/coroutines/selects/UnbiasedSelectBuilderImpl;", "R", "Lkotlinx/coroutines/selects/UnbiasedSelectImplementation;", "uCont", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/Continuation;)V", "cont", "Lkotlinx/coroutines/CancellableContinuationImpl;", "handleBuilderException", "", "e", "", "initSelectResult", "", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class UnbiasedSelectBuilderImpl<R> extends UnbiasedSelectImplementation<R> {
    private final CancellableContinuationImpl<R> cont;

    public UnbiasedSelectBuilderImpl(Continuation<? super R> continuation) {
        super(continuation.get$context());
        this.cont = new CancellableContinuationImpl<>(IntrinsicsKt.intercepted(continuation), 1);
    }

    /* compiled from: SelectOld.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "R", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl$initSelectResult$1", m162f = "SelectOld.kt", m163i = {}, m164l = {67}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.selects.UnbiasedSelectBuilderImpl$initSelectResult$1 */
    static final class C13161 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        final /* synthetic */ UnbiasedSelectBuilderImpl<R> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C13161(UnbiasedSelectBuilderImpl<R> unbiasedSelectBuilderImpl, Continuation<? super C13161> continuation) {
            super(2, continuation);
            this.this$0 = unbiasedSelectBuilderImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C13161(this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C13161) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object result) {
            C13161 c13161;
            Throwable e;
            C13161 c131612;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(result);
                    c13161 = this;
                    try {
                        c13161.label = 1;
                        Object objDoSelect = c13161.this$0.doSelect(c13161);
                        if (objDoSelect != coroutine_suspended) {
                            result = objDoSelect;
                            SelectOldKt.resumeUndispatched(((UnbiasedSelectBuilderImpl) c13161.this$0).cont, result);
                            return Unit.INSTANCE;
                        }
                        return coroutine_suspended;
                    } catch (Throwable th) {
                        e = th;
                        c131612 = c13161;
                        SelectOldKt.resumeUndispatchedWithException(((UnbiasedSelectBuilderImpl) c131612.this$0).cont, e);
                        return Unit.INSTANCE;
                    }
                case 1:
                    c131612 = this;
                    try {
                        ResultKt.throwOnFailure(result);
                        c13161 = c131612;
                        SelectOldKt.resumeUndispatched(((UnbiasedSelectBuilderImpl) c13161.this$0).cont, result);
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        e = th2;
                        SelectOldKt.resumeUndispatchedWithException(((UnbiasedSelectBuilderImpl) c131612.this$0).cont, e);
                        return Unit.INSTANCE;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final Object initSelectResult() {
        if (this.cont.isCompleted()) {
            return this.cont.getResult();
        }
        BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(getContext()), null, CoroutineStart.UNDISPATCHED, new C13161(this, null), 1, null);
        return this.cont.getResult();
    }

    public final void handleBuilderException(Throwable e) {
        CancellableContinuationImpl<R> cancellableContinuationImpl = this.cont;
        Result.Companion companion = Result.INSTANCE;
        cancellableContinuationImpl.resumeWith(Result.m255constructorimpl(ResultKt.createFailure(e)));
    }
}
