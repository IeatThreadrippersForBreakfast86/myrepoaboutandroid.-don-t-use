package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.flow.internal.SafeCollector;
import org.xbill.DNS.WKSRecord;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1<T> implements Flow<T> {
    final /* synthetic */ Function3 $action$inlined;
    final /* synthetic */ Flow $this_onCompletion$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1", m162f = "Emitters.kt", m163i = {0, 0, 1, 2}, m164l = {WKSRecord.Service.AUTH, 120, 127}, m165m = "collect", m166n = {"this", "$this$onCompletion_u24lambda_u242", "e", "sc"}, m167s = {"L$0", "L$1", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1$1 */
    public static final class C12161 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public C12161(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.this.collect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x008e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) throws Throwable {
        C12161 c12161;
        FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
        FlowCollector $this$onCompletion_u24lambda_u242;
        SafeCollector sc;
        Throwable th;
        SafeCollector sc2;
        Object objInvoke;
        if (continuation instanceof C12161) {
            c12161 = (C12161) continuation;
            if ((c12161.label & Integer.MIN_VALUE) != 0) {
                c12161.label -= Integer.MIN_VALUE;
            } else {
                c12161 = new C12161(continuation);
            }
        }
        C12161 c121612 = c12161;
        Object $result = c121612.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c121612.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 = this;
                    $this$onCompletion_u24lambda_u242 = flowCollector;
                    Flow flow = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$this_onCompletion$inlined;
                    c121612.L$0 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1;
                    c121612.L$1 = $this$onCompletion_u24lambda_u242;
                    c121612.label = 1;
                    if (flow.collect($this$onCompletion_u24lambda_u242, c121612) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    sc = new SafeCollector($this$onCompletion_u24lambda_u242, c121612.getContext());
                    try {
                        Function3 function3 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
                        c121612.L$0 = sc;
                        c121612.L$1 = null;
                        c121612.label = 3;
                        InlineMarker.mark(6);
                        objInvoke = function3.invoke(sc, null, c121612);
                        InlineMarker.mark(7);
                        if (objInvoke != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        sc2 = sc;
                        sc2.releaseIntercepted();
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        th = th2;
                        sc2 = sc;
                        sc2.releaseIntercepted();
                        throw th;
                    }
                case 1:
                    $this$onCompletion_u24lambda_u242 = (FlowCollector) c121612.L$1;
                    flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1 = (FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1) c121612.L$0;
                    ResultKt.throwOnFailure($result);
                    sc = new SafeCollector($this$onCompletion_u24lambda_u242, c121612.getContext());
                    Function3 function32 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
                    c121612.L$0 = sc;
                    c121612.L$1 = null;
                    c121612.label = 3;
                    InlineMarker.mark(6);
                    objInvoke = function32.invoke(sc, null, c121612);
                    InlineMarker.mark(7);
                    if (objInvoke != coroutine_suspended) {
                    }
                    break;
                case 2:
                    Throwable e = (Throwable) c121612.L$0;
                    ResultKt.throwOnFailure($result);
                    throw e;
                case 3:
                    sc2 = (SafeCollector) c121612.L$0;
                    try {
                        ResultKt.throwOnFailure($result);
                        sc2.releaseIntercepted();
                        return Unit.INSTANCE;
                    } catch (Throwable th3) {
                        th = th3;
                        sc2.releaseIntercepted();
                        throw th;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable e2) {
            ThrowingCollector throwingCollector = new ThrowingCollector(e2);
            Function3 function33 = flowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1.$action$inlined;
            c121612.L$0 = e2;
            c121612.L$1 = null;
            c121612.label = 2;
            if (FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(throwingCollector, function33, e2, c121612) == coroutine_suspended) {
                return coroutine_suspended;
            }
            throw e2;
        }
    }

    public FlowKt__EmittersKt$onCompletion$$inlined$unsafeFlow$1(Flow flow, Function3 function3) {
        this.$this_onCompletion$inlined = flow;
        this.$action$inlined = function3;
    }
}
