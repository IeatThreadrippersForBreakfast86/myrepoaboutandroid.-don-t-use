package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.flow.internal.SafeCollector;
import org.xbill.DNS.WKSRecord;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: SafeCollector.common.kt */
@Metadata(m145d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@¢\u0006\u0002\u0010\u0006¨\u0006\u0007¸\u0006\u0000"}, m146d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1<T> implements Flow<T> {
    final /* synthetic */ Function2 $action$inlined;
    final /* synthetic */ Flow $this_onStart$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1", m162f = "Emitters.kt", m163i = {0, 0, 0}, m164l = {WKSRecord.Service.SFTP, WKSRecord.Service.NNTP}, m165m = "collect", m166n = {"this", "$this$onStart_u24lambda_u241", "safeCollector"}, m167s = {"L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1$1 */
    public static final class C12181 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        public C12181(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1.this.collect(null, this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0087 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlinx.coroutines.flow.internal.SafeCollector] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.lang.Object] */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        C12181 c12181;
        FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1 flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1;
        FlowCollector $this$onStart_u24lambda_u241;
        SafeCollector safeCollector;
        if (continuation instanceof C12181) {
            c12181 = (C12181) continuation;
            if ((c12181.label & Integer.MIN_VALUE) != 0) {
                c12181.label -= Integer.MIN_VALUE;
            } else {
                c12181 = new C12181(continuation);
            }
        }
        C12181 c121812 = c12181;
        Object $result = c121812.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ?? Collect = c121812.label;
        try {
            switch (Collect) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1 = this;
                    $this$onStart_u24lambda_u241 = flowCollector;
                    safeCollector = new SafeCollector($this$onStart_u24lambda_u241, c121812.getContext());
                    Function2 function2 = flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1.$action$inlined;
                    c121812.L$0 = flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1;
                    c121812.L$1 = $this$onStart_u24lambda_u241;
                    c121812.L$2 = safeCollector;
                    c121812.label = 1;
                    InlineMarker.mark(6);
                    Object objInvoke = function2.invoke(safeCollector, c121812);
                    InlineMarker.mark(7);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    safeCollector.releaseIntercepted();
                    Flow flow = flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1.$this_onStart$inlined;
                    c121812.L$0 = null;
                    c121812.L$1 = null;
                    c121812.L$2 = null;
                    c121812.label = 2;
                    Collect = flow.collect($this$onStart_u24lambda_u241, c121812);
                    if (Collect == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 1:
                    safeCollector = (SafeCollector) c121812.L$2;
                    $this$onStart_u24lambda_u241 = (FlowCollector) c121812.L$1;
                    flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1 = (FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1) c121812.L$0;
                    ResultKt.throwOnFailure($result);
                    safeCollector.releaseIntercepted();
                    Flow flow2 = flowKt__EmittersKt$onStart$$inlined$unsafeFlow$1.$this_onStart$inlined;
                    c121812.L$0 = null;
                    c121812.L$1 = null;
                    c121812.L$2 = null;
                    c121812.label = 2;
                    Collect = flow2.collect($this$onStart_u24lambda_u241, c121812);
                    if (Collect == coroutine_suspended) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            Collect.releaseIntercepted();
            throw th;
        }
    }

    public FlowKt__EmittersKt$onStart$$inlined$unsafeFlow$1(Function2 function2, Flow flow) {
        this.$action$inlined = function2;
        this.$this_onStart$inlined = flow;
    }
}
