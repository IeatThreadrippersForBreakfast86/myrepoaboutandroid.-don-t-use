package androidx.activity;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;

/* compiled from: FullyDrawnReporter.kt */
@Metadata(m145d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u001c\u0010\u0003\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004H\u0086H¢\u0006\u0002\u0010\u0007¨\u0006\b"}, m146d2 = {"reportWhenComplete", "", "Landroidx/activity/FullyDrawnReporter;", "reporter", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Landroidx/activity/FullyDrawnReporter;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "activity_release"}, m147k = 2, m148mv = {1, 8, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class FullyDrawnReporterKt {

    /* compiled from: FullyDrawnReporter.kt */
    @Metadata(m147k = 3, m148mv = {1, 8, 0}, m150xi = 176)
    @DebugMetadata(m161c = "androidx.activity.FullyDrawnReporterKt", m162f = "FullyDrawnReporter.kt", m163i = {0}, m164l = {185}, m165m = "reportWhenComplete", m166n = {"$this$reportWhenComplete"}, m167s = {"L$0"})
    /* renamed from: androidx.activity.FullyDrawnReporterKt$reportWhenComplete$1 */
    static final class C00121 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00121(Continuation<? super C00121> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FullyDrawnReporterKt.reportWhenComplete(null, null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object reportWhenComplete(FullyDrawnReporter $this$reportWhenComplete, Function1<? super Continuation<? super Unit>, ? extends Object> function1, Continuation<? super Unit> continuation) throws Throwable {
        C00121 c00121;
        FullyDrawnReporter $this$reportWhenComplete2;
        if (continuation instanceof C00121) {
            c00121 = (C00121) continuation;
            if ((c00121.label & Integer.MIN_VALUE) != 0) {
                c00121.label -= Integer.MIN_VALUE;
            } else {
                c00121 = new C00121(continuation);
            }
        }
        C00121 c001212 = c00121;
        Object $result = c001212.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c001212.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$reportWhenComplete.addReporter();
                if ($this$reportWhenComplete.isFullyDrawnReported()) {
                    return Unit.INSTANCE;
                }
                try {
                    c001212.L$0 = $this$reportWhenComplete;
                    c001212.label = 1;
                    if (function1.invoke(c001212) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    $this$reportWhenComplete2 = $this$reportWhenComplete;
                    InlineMarker.finallyStart(1);
                    $this$reportWhenComplete2.removeReporter();
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                } catch (Throwable th) {
                    th = th;
                    $this$reportWhenComplete2 = $this$reportWhenComplete;
                    InlineMarker.finallyStart(1);
                    $this$reportWhenComplete2.removeReporter();
                    InlineMarker.finallyEnd(1);
                    throw th;
                }
            case 1:
                $this$reportWhenComplete2 = (FullyDrawnReporter) c001212.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    InlineMarker.finallyStart(1);
                    $this$reportWhenComplete2.removeReporter();
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                } catch (Throwable th2) {
                    th = th2;
                    InlineMarker.finallyStart(1);
                    $this$reportWhenComplete2.removeReporter();
                    InlineMarker.finallyEnd(1);
                    throw th;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    private static final Object reportWhenComplete$$forInline(FullyDrawnReporter $this$reportWhenComplete, Function1<? super Continuation<? super Unit>, ? extends Object> function1, Continuation<? super Unit> continuation) {
        $this$reportWhenComplete.addReporter();
        if ($this$reportWhenComplete.isFullyDrawnReported()) {
            return Unit.INSTANCE;
        }
        try {
            function1.invoke(continuation);
            InlineMarker.finallyStart(1);
            $this$reportWhenComplete.removeReporter();
            InlineMarker.finallyEnd(1);
            return Unit.INSTANCE;
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            $this$reportWhenComplete.removeReporter();
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }
}
