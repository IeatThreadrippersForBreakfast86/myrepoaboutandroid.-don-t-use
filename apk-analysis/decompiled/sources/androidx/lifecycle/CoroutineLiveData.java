package androidx.lifecycle;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableJob;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;

/* compiled from: CoroutineLiveData.kt */
@Metadata(m145d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002BH\u0012\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012-\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u0013\u001a\u00020\u000bH\u0080@¢\u0006\u0004\b\u0014\u0010\u0015J\u001e\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00028\u00000\u0019H\u0080@¢\u0006\u0004\b\u001a\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u000bH\u0014J\b\u0010\u001d\u001a\u00020\u000bH\u0014R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, m146d2 = {"Landroidx/lifecycle/CoroutineLiveData;", "T", "Landroidx/lifecycle/MediatorLiveData;", "context", "Lkotlin/coroutines/CoroutineContext;", "timeoutInMs", "", "block", "Lkotlin/Function2;", "Landroidx/lifecycle/LiveDataScope;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/CoroutineContext;JLkotlin/jvm/functions/Function2;)V", "blockRunner", "Landroidx/lifecycle/BlockRunner;", "emittedSource", "Landroidx/lifecycle/EmittedSource;", "clearSource", "clearSource$lifecycle_livedata_release", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitSource", "Lkotlinx/coroutines/DisposableHandle;", "source", "Landroidx/lifecycle/LiveData;", "emitSource$lifecycle_livedata_release", "(Landroidx/lifecycle/LiveData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onActive", "onInactive", "lifecycle-livedata_release"}, m147k = 1, m148mv = {1, 8, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class CoroutineLiveData<T> extends MediatorLiveData<T> {
    private BlockRunner<T> blockRunner;
    private EmittedSource emittedSource;

    public /* synthetic */ CoroutineLiveData(EmptyCoroutineContext emptyCoroutineContext, long j, Function2 function2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? EmptyCoroutineContext.INSTANCE : emptyCoroutineContext, (i & 2) != 0 ? CoroutineLiveDataKt.DEFAULT_TIMEOUT : j, function2);
    }

    public CoroutineLiveData(CoroutineContext context, long timeoutInMs, Function2<? super LiveDataScope<T>, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(block, "block");
        CompletableJob supervisorJob = SupervisorKt.SupervisorJob((Job) context.get(Job.INSTANCE));
        CoroutineScope scope = CoroutineScopeKt.CoroutineScope(Dispatchers.getMain().getImmediate().plus(context).plus(supervisorJob));
        this.blockRunner = new BlockRunner<>(this, block, timeoutInMs, scope, new Function0<Unit>(this) { // from class: androidx.lifecycle.CoroutineLiveData.1
            final /* synthetic */ CoroutineLiveData<T> this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
                this.this$0 = this;
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                ((CoroutineLiveData) this.this$0).blockRunner = null;
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object emitSource$lifecycle_livedata_release(LiveData<T> liveData, Continuation<? super DisposableHandle> continuation) {
        CoroutineLiveData$emitSource$1 coroutineLiveData$emitSource$1;
        LiveData source;
        CoroutineLiveData<T> coroutineLiveData;
        Object objAddDisposableSource;
        if (continuation instanceof CoroutineLiveData$emitSource$1) {
            coroutineLiveData$emitSource$1 = (CoroutineLiveData$emitSource$1) continuation;
            if ((coroutineLiveData$emitSource$1.label & Integer.MIN_VALUE) != 0) {
                coroutineLiveData$emitSource$1.label -= Integer.MIN_VALUE;
            } else {
                coroutineLiveData$emitSource$1 = new CoroutineLiveData$emitSource$1(this, continuation);
            }
        }
        CoroutineLiveData$emitSource$1 coroutineLiveData$emitSource$12 = coroutineLiveData$emitSource$1;
        Object $result = coroutineLiveData$emitSource$12.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (coroutineLiveData$emitSource$12.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                coroutineLiveData$emitSource$12.L$0 = this;
                coroutineLiveData$emitSource$12.L$1 = liveData;
                coroutineLiveData$emitSource$12.label = 1;
                if (clearSource$lifecycle_livedata_release(coroutineLiveData$emitSource$12) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                source = liveData;
                coroutineLiveData = this;
                coroutineLiveData$emitSource$12.L$0 = coroutineLiveData;
                coroutineLiveData$emitSource$12.L$1 = null;
                coroutineLiveData$emitSource$12.label = 2;
                objAddDisposableSource = CoroutineLiveDataKt.addDisposableSource(coroutineLiveData, source, coroutineLiveData$emitSource$12);
                if (objAddDisposableSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                EmittedSource newSource = (EmittedSource) objAddDisposableSource;
                coroutineLiveData.emittedSource = newSource;
                return newSource;
            case 1:
                LiveData source2 = (LiveData) coroutineLiveData$emitSource$12.L$1;
                CoroutineLiveData<T> coroutineLiveData2 = (CoroutineLiveData) coroutineLiveData$emitSource$12.L$0;
                ResultKt.throwOnFailure($result);
                source = source2;
                coroutineLiveData = coroutineLiveData2;
                coroutineLiveData$emitSource$12.L$0 = coroutineLiveData;
                coroutineLiveData$emitSource$12.L$1 = null;
                coroutineLiveData$emitSource$12.label = 2;
                objAddDisposableSource = CoroutineLiveDataKt.addDisposableSource(coroutineLiveData, source, coroutineLiveData$emitSource$12);
                if (objAddDisposableSource == coroutine_suspended) {
                }
                EmittedSource newSource2 = (EmittedSource) objAddDisposableSource;
                coroutineLiveData.emittedSource = newSource2;
                return newSource2;
            case 2:
                coroutineLiveData = (CoroutineLiveData) coroutineLiveData$emitSource$12.L$0;
                ResultKt.throwOnFailure($result);
                objAddDisposableSource = $result;
                EmittedSource newSource22 = (EmittedSource) objAddDisposableSource;
                coroutineLiveData.emittedSource = newSource22;
                return newSource22;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object clearSource$lifecycle_livedata_release(Continuation<? super Unit> continuation) {
        CoroutineLiveData$clearSource$1 coroutineLiveData$clearSource$1;
        CoroutineLiveData coroutineLiveData;
        CoroutineLiveData coroutineLiveData2;
        if (continuation instanceof CoroutineLiveData$clearSource$1) {
            coroutineLiveData$clearSource$1 = (CoroutineLiveData$clearSource$1) continuation;
            if ((coroutineLiveData$clearSource$1.label & Integer.MIN_VALUE) != 0) {
                coroutineLiveData$clearSource$1.label -= Integer.MIN_VALUE;
            } else {
                coroutineLiveData$clearSource$1 = new CoroutineLiveData$clearSource$1(this, continuation);
            }
        }
        CoroutineLiveData$clearSource$1 coroutineLiveData$clearSource$12 = coroutineLiveData$clearSource$1;
        Object $result = coroutineLiveData$clearSource$12.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (coroutineLiveData$clearSource$12.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                coroutineLiveData = this;
                EmittedSource emittedSource = coroutineLiveData.emittedSource;
                if (emittedSource != null) {
                    coroutineLiveData$clearSource$12.L$0 = coroutineLiveData;
                    coroutineLiveData$clearSource$12.label = 1;
                    if (emittedSource.disposeNow(coroutineLiveData$clearSource$12) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    coroutineLiveData2 = coroutineLiveData;
                    coroutineLiveData = coroutineLiveData2;
                }
                coroutineLiveData.emittedSource = null;
                return Unit.INSTANCE;
            case 1:
                coroutineLiveData2 = (CoroutineLiveData) coroutineLiveData$clearSource$12.L$0;
                ResultKt.throwOnFailure($result);
                coroutineLiveData = coroutineLiveData2;
                coroutineLiveData.emittedSource = null;
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Override // androidx.lifecycle.MediatorLiveData, androidx.lifecycle.LiveData
    protected void onActive() {
        super.onActive();
        BlockRunner<T> blockRunner = this.blockRunner;
        if (blockRunner != null) {
            blockRunner.maybeRun();
        }
    }

    @Override // androidx.lifecycle.MediatorLiveData, androidx.lifecycle.LiveData
    protected void onInactive() {
        super.onInactive();
        BlockRunner<T> blockRunner = this.blockRunner;
        if (blockRunner != null) {
            blockRunner.cancel();
        }
    }
}
