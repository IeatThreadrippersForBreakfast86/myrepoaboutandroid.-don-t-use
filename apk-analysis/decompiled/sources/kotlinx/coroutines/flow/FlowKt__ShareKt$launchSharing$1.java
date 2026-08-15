package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: Share.kt */
@Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
@DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1", m162f = "Share.kt", m163i = {}, m164l = {210, 214, 215, 221}, m165m = "invokeSuspend", m166n = {}, m167s = {})
/* loaded from: classes.dex */
final class FlowKt__ShareKt$launchSharing$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ T $initialValue;
    final /* synthetic */ MutableSharedFlow<T> $shared;
    final /* synthetic */ SharingStarted $started;
    final /* synthetic */ Flow<T> $upstream;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__ShareKt$launchSharing$1(SharingStarted sharingStarted, Flow<? extends T> flow, MutableSharedFlow<T> mutableSharedFlow, T t, Continuation<? super FlowKt__ShareKt$launchSharing$1> continuation) {
        super(2, continuation);
        this.$started = sharingStarted;
        this.$upstream = flow;
        this.$shared = mutableSharedFlow;
        this.$initialValue = t;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new FlowKt__ShareKt$launchSharing$1(this.$started, this.$upstream, this.$shared, this.$initialValue, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((FlowKt__ShareKt$launchSharing$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0084 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0085  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object $result) {
        FlowKt__ShareKt$launchSharing$1 flowKt__ShareKt$launchSharing$1;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                flowKt__ShareKt$launchSharing$1 = this;
                if (flowKt__ShareKt$launchSharing$1.$started == SharingStarted.INSTANCE.getEagerly()) {
                    flowKt__ShareKt$launchSharing$1.label = 1;
                    if (flowKt__ShareKt$launchSharing$1.$upstream.collect(flowKt__ShareKt$launchSharing$1.$shared, flowKt__ShareKt$launchSharing$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else if (flowKt__ShareKt$launchSharing$1.$started == SharingStarted.INSTANCE.getLazily()) {
                    flowKt__ShareKt$launchSharing$1.label = 2;
                    if (FlowKt.first(flowKt__ShareKt$launchSharing$1.$shared.getSubscriptionCount(), new C12581(null), flowKt__ShareKt$launchSharing$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    flowKt__ShareKt$launchSharing$1.label = 3;
                    if (flowKt__ShareKt$launchSharing$1.$upstream.collect(flowKt__ShareKt$launchSharing$1.$shared, flowKt__ShareKt$launchSharing$1) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    flowKt__ShareKt$launchSharing$1.label = 4;
                    if (FlowKt.collectLatest(FlowKt.distinctUntilChanged(flowKt__ShareKt$launchSharing$1.$started.command(flowKt__ShareKt$launchSharing$1.$shared.getSubscriptionCount())), new C12592(flowKt__ShareKt$launchSharing$1.$upstream, flowKt__ShareKt$launchSharing$1.$shared, flowKt__ShareKt$launchSharing$1.$initialValue, null), flowKt__ShareKt$launchSharing$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            case 1:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            case 2:
                flowKt__ShareKt$launchSharing$1 = this;
                ResultKt.throwOnFailure($result);
                flowKt__ShareKt$launchSharing$1.label = 3;
                if (flowKt__ShareKt$launchSharing$1.$upstream.collect(flowKt__ShareKt$launchSharing$1.$shared, flowKt__ShareKt$launchSharing$1) != coroutine_suspended) {
                }
                break;
            case 3:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            case 4:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* compiled from: Share.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "it", ""}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1", m162f = "Share.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$1 */
    static final class C12581 extends SuspendLambda implements Function2<Integer, Continuation<? super Boolean>, Object> {
        /* synthetic */ int I$0;
        int label;

        C12581(Continuation<? super C12581> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C12581 c12581 = new C12581(continuation);
            c12581.I$0 = ((Number) obj).intValue();
            return c12581;
        }

        public final Object invoke(int i, Continuation<? super Boolean> continuation) {
            return ((C12581) create(Integer.valueOf(i), continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Integer num, Continuation<? super Boolean> continuation) {
            return invoke(num.intValue(), continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    int it = this.I$0;
                    return Boxing.boxBoolean(it > 0);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* compiled from: Share.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "it", "Lkotlinx/coroutines/flow/SharingCommand;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2", m162f = "Share.kt", m163i = {}, m164l = {223}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2 */
    static final class C12592 extends SuspendLambda implements Function2<SharingCommand, Continuation<? super Unit>, Object> {
        final /* synthetic */ T $initialValue;
        final /* synthetic */ MutableSharedFlow<T> $shared;
        final /* synthetic */ Flow<T> $upstream;
        /* synthetic */ Object L$0;
        int label;

        /* compiled from: Share.kt */
        @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        /* renamed from: kotlinx.coroutines.flow.FlowKt__ShareKt$launchSharing$1$2$WhenMappings */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[SharingCommand.values().length];
                try {
                    iArr[SharingCommand.START.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[SharingCommand.STOP.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[SharingCommand.STOP_AND_RESET_REPLAY_CACHE.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C12592(Flow<? extends T> flow, MutableSharedFlow<T> mutableSharedFlow, T t, Continuation<? super C12592> continuation) {
            super(2, continuation);
            this.$upstream = flow;
            this.$shared = mutableSharedFlow;
            this.$initialValue = t;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C12592 c12592 = new C12592(this.$upstream, this.$shared, this.$initialValue, continuation);
            c12592.L$0 = obj;
            return c12592;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SharingCommand sharingCommand, Continuation<? super Unit> continuation) {
            return ((C12592) create(sharingCommand, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            C12592 c12592;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    switch (WhenMappings.$EnumSwitchMapping$0[((SharingCommand) this.L$0).ordinal()]) {
                        case 1:
                            this.label = 1;
                            if (this.$upstream.collect(this.$shared, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            c12592 = this;
                            return Unit.INSTANCE;
                        case 3:
                            if (this.$initialValue == SharedFlowKt.NO_VALUE) {
                                this.$shared.resetReplayCache();
                            } else {
                                this.$shared.tryEmit(this.$initialValue);
                            }
                        case 2:
                        default:
                            return Unit.INSTANCE;
                    }
                case 1:
                    c12592 = this;
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }
}
