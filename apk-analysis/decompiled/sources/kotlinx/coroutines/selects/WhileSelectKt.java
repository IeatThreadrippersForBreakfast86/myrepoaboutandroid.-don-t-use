package kotlinx.coroutines.selects;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;

/* compiled from: WhileSelect.kt */
@Metadata(m145d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a/\u0010\u0000\u001a\u00020\u00012\u001f\b\u0004\u0010\u0002\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0004\u0012\u00020\u00010\u0003¢\u0006\u0002\b\u0006H\u0087H¢\u0006\u0002\u0010\u0007¨\u0006\b"}, m146d2 = {"whileSelect", "", "builder", "Lkotlin/Function1;", "Lkotlinx/coroutines/selects/SelectBuilder;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class WhileSelectKt {

    /* compiled from: WhileSelect.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 176)
    @DebugMetadata(m161c = "kotlinx.coroutines.selects.WhileSelectKt", m162f = "WhileSelect.kt", m163i = {0}, m164l = {37}, m165m = "whileSelect", m166n = {"builder"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.selects.WhileSelectKt$whileSelect$1 */
    static final class C13171 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C13171(Continuation<? super C13171> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return WhileSelectKt.whileSelect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x005c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x005d -> B:18:0x0062). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object whileSelect(Function1<? super SelectBuilder<? super Boolean>, Unit> function1, Continuation<? super Unit> continuation) {
        C13171 c13171;
        Function1 builder;
        Object $result;
        Object obj;
        if (continuation instanceof C13171) {
            c13171 = (C13171) continuation;
            if ((c13171.label & Integer.MIN_VALUE) != 0) {
                c13171.label -= Integer.MIN_VALUE;
            } else {
                c13171 = new C13171(continuation);
            }
        }
        C13171 c131712 = c13171;
        Object $result2 = c131712.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c131712.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                builder = function1;
                SelectImplementation $this$select_u24lambda_u241$iv = new SelectImplementation(c131712.getContext());
                builder.invoke($this$select_u24lambda_u241$iv);
                c131712.L$0 = builder;
                c131712.label = 1;
                Object objDoSelect = $this$select_u24lambda_u241$iv.doSelect(c131712);
                if (objDoSelect != $result3) {
                    return $result3;
                }
                Object obj2 = $result3;
                $result = $result2;
                $result2 = objDoSelect;
                obj = obj2;
                if (((Boolean) $result2).booleanValue()) {
                    return Unit.INSTANCE;
                }
                $result2 = $result;
                $result3 = obj;
                SelectImplementation $this$select_u24lambda_u241$iv2 = new SelectImplementation(c131712.getContext());
                builder.invoke($this$select_u24lambda_u241$iv2);
                c131712.L$0 = builder;
                c131712.label = 1;
                Object objDoSelect2 = $this$select_u24lambda_u241$iv2.doSelect(c131712);
                if (objDoSelect2 != $result3) {
                }
            case 1:
                builder = (Function1) c131712.L$0;
                ResultKt.throwOnFailure($result2);
                obj = $result3;
                $result = $result2;
                if (((Boolean) $result2).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    private static final Object whileSelect$$forInline(Function1<? super SelectBuilder<? super Boolean>, Unit> function1, Continuation<? super Unit> continuation) {
        InlineMarker.mark(3);
        Continuation continuation2 = null;
        continuation2.getContext();
        throw null;
    }
}
