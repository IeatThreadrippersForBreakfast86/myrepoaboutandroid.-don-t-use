package kotlinx.coroutines.flow.internal;

import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.YieldKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.channels.ChannelResult;
import kotlinx.coroutines.channels.SendChannel;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import org.xbill.DNS.WKSRecord;

/* compiled from: Combine.kt */
@Metadata(m145d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001ak\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0004\"\u0004\b\u0002\u0010\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00040\u00012(\u0010\u0007\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\bH\u0000¢\u0006\u0002\u0010\u000b\u001a\u008d\u0001\u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0014\u0010\u0010\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u00010\u00112\u0016\u0010\u0012\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u0001H\u000e\u0018\u00010\u00110\u001329\u0010\u0007\u001a5\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\b¢\u0006\u0002\b\u0014H\u0081@¢\u0006\u0002\u0010\u0015*\u001c\b\u0002\u0010\u0016\"\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u00172\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0017¨\u0006\u0018"}, m146d2 = {"zipImpl", "Lkotlinx/coroutines/flow/Flow;", "R", "T1", "T2", "flow", "flow2", "transform", "Lkotlin/Function3;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "combineInternal", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "flows", "", "arrayFactory", "Lkotlin/Function0;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/FlowCollector;[Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Update", "Lkotlin/collections/IndexedValue;", "kotlinx-coroutines-core"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class CombineKt {

    /* compiled from: Combine.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2", m162f = "Combine.kt", m163i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2}, m164l = {51, WKSRecord.Service.NETRJS_3, 76}, m165m = "invokeSuspend", m166n = {"latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch", "latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch", "latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues", "currentEpoch"}, m167s = {"L$0", "L$1", "L$2", "I$0", "I$1", "L$0", "L$1", "L$2", "I$0", "I$1", "L$0", "L$1", "L$2", "I$0", "I$1"})
    /* renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2 */
    static final class C13012 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function0<T[]> $arrayFactory;
        final /* synthetic */ Flow<T>[] $flows;
        final /* synthetic */ FlowCollector<R> $this_combineInternal;
        final /* synthetic */ Function3<FlowCollector<? super R>, T[], Continuation<? super Unit>, Object> $transform;
        int I$0;
        int I$1;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C13012(Flow<? extends T>[] flowArr, Function0<T[]> function0, Function3<? super FlowCollector<? super R>, ? super T[], ? super Continuation<? super Unit>, ? extends Object> function3, FlowCollector<? super R> flowCollector, Continuation<? super C13012> continuation) {
            super(2, continuation);
            this.$flows = flowArr;
            this.$arrayFactory = function0;
            this.$transform = function3;
            this.$this_combineInternal = flowCollector;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C13012 c13012 = new C13012(this.$flows, this.$arrayFactory, this.$transform, this.$this_combineInternal, continuation);
            c13012.L$0 = obj;
            return c13012;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C13012) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x00ea A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00eb  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00f8 A[LOOP:0: B:25:0x00f8->B:50:?, LOOP_START, PHI: r6 r9
          0x00f8: PHI (r6v11 'remainingAbsentValues' int) = (r6v10 'remainingAbsentValues' int), (r6v12 'remainingAbsentValues' int) binds: [B:22:0x00f2, B:50:?] A[DONT_GENERATE, DONT_INLINE]
          0x00f8: PHI (r9v8 'element' kotlin.collections.IndexedValue) = (r9v7 'element' kotlin.collections.IndexedValue), (r9v20 'element' kotlin.collections.IndexedValue) binds: [B:22:0x00f2, B:50:?] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x0122  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x016d  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0144 -> B:17:0x00d0). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x016a -> B:17:0x00d0). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x016d -> B:17:0x00d0). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C13012 c13012;
            Object[] latestValues;
            byte[] lastReceivedEpoch;
            int remainingAbsentValues;
            Channel resultChannel;
            int remainingAbsentValues2;
            Object[] latestValues2;
            Object holder;
            IndexedValue element;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c13012 = this;
                    CoroutineScope $this$flowScope = (CoroutineScope) c13012.L$0;
                    int size = c13012.$flows.length;
                    if (size == 0) {
                        return Unit.INSTANCE;
                    }
                    latestValues = new Object[size];
                    ArraysKt.fill$default(latestValues, NullSurrogateKt.UNINITIALIZED, 0, 0, 6, (Object) null);
                    Channel resultChannel2 = ChannelKt.Channel$default(size, null, null, 6, null);
                    AtomicInteger nonClosed = new AtomicInteger(size);
                    for (int i = 0; i < size; i++) {
                        BuildersKt__Builders_commonKt.launch$default($this$flowScope, null, null, new AnonymousClass1(c13012.$flows, i, nonClosed, resultChannel2, null), 3, null);
                    }
                    lastReceivedEpoch = new byte[size];
                    remainingAbsentValues = 0;
                    resultChannel = resultChannel2;
                    remainingAbsentValues2 = size;
                    remainingAbsentValues = (byte) (remainingAbsentValues + 1);
                    c13012.L$0 = latestValues;
                    c13012.L$1 = resultChannel;
                    c13012.L$2 = lastReceivedEpoch;
                    c13012.I$0 = remainingAbsentValues2;
                    c13012.I$1 = remainingAbsentValues;
                    c13012.label = 1;
                    holder = resultChannel.mo1761receiveCatchingJP2dKIU(c13012);
                    if (holder == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    latestValues2 = latestValues;
                    element = (IndexedValue) ChannelResult.m1773getOrNullimpl(holder);
                    if (element != null) {
                        return Unit.INSTANCE;
                    }
                    do {
                        int index = element.getIndex();
                        Object previous = latestValues2[index];
                        latestValues2[index] = element.getValue();
                        if (previous == NullSurrogateKt.UNINITIALIZED) {
                            remainingAbsentValues2--;
                        }
                        if (lastReceivedEpoch[index] != remainingAbsentValues) {
                            lastReceivedEpoch[index] = (byte) remainingAbsentValues;
                            element = (IndexedValue) ChannelResult.m1773getOrNullimpl(resultChannel.mo1762tryReceivePtdJZtk());
                        }
                        if (remainingAbsentValues2 != 0) {
                            latestValues = latestValues2;
                        } else {
                            Object[] results = (Object[]) c13012.$arrayFactory.invoke();
                            if (results == null) {
                                Function3<FlowCollector<? super R>, T[], Continuation<? super Unit>, Object> function3 = c13012.$transform;
                                Object obj = c13012.$this_combineInternal;
                                c13012.L$0 = latestValues2;
                                c13012.L$1 = resultChannel;
                                c13012.L$2 = lastReceivedEpoch;
                                c13012.I$0 = remainingAbsentValues2;
                                c13012.I$1 = remainingAbsentValues;
                                c13012.label = 2;
                                if (function3.invoke(obj, latestValues2, c13012) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                latestValues = latestValues2;
                            } else {
                                ArraysKt.copyInto$default(latestValues2, results, 0, 0, 0, 14, (Object) null);
                                Function3<FlowCollector<? super R>, T[], Continuation<? super Unit>, Object> function32 = c13012.$transform;
                                Object obj2 = c13012.$this_combineInternal;
                                c13012.L$0 = latestValues2;
                                c13012.L$1 = resultChannel;
                                c13012.L$2 = lastReceivedEpoch;
                                c13012.I$0 = remainingAbsentValues2;
                                c13012.I$1 = remainingAbsentValues;
                                c13012.label = 3;
                                if (function32.invoke(obj2, results, c13012) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                latestValues = latestValues2;
                            }
                        }
                        remainingAbsentValues = (byte) (remainingAbsentValues + 1);
                        c13012.L$0 = latestValues;
                        c13012.L$1 = resultChannel;
                        c13012.L$2 = lastReceivedEpoch;
                        c13012.I$0 = remainingAbsentValues2;
                        c13012.I$1 = remainingAbsentValues;
                        c13012.label = 1;
                        holder = resultChannel.mo1761receiveCatchingJP2dKIU(c13012);
                        if (holder == coroutine_suspended) {
                        }
                    } while (element != null);
                    if (remainingAbsentValues2 != 0) {
                    }
                    remainingAbsentValues = (byte) (remainingAbsentValues + 1);
                    c13012.L$0 = latestValues;
                    c13012.L$1 = resultChannel;
                    c13012.L$2 = lastReceivedEpoch;
                    c13012.I$0 = remainingAbsentValues2;
                    c13012.I$1 = remainingAbsentValues;
                    c13012.label = 1;
                    holder = resultChannel.mo1761receiveCatchingJP2dKIU(c13012);
                    if (holder == coroutine_suspended) {
                    }
                case 1:
                    c13012 = this;
                    int i2 = c13012.I$1;
                    int remainingAbsentValues3 = c13012.I$0;
                    byte[] lastReceivedEpoch2 = (byte[]) c13012.L$2;
                    resultChannel = (Channel) c13012.L$1;
                    latestValues2 = (Object[]) c13012.L$0;
                    ResultKt.throwOnFailure($result);
                    holder = ((ChannelResult) $result).getHolder();
                    remainingAbsentValues = i2;
                    lastReceivedEpoch = lastReceivedEpoch2;
                    remainingAbsentValues2 = remainingAbsentValues3;
                    element = (IndexedValue) ChannelResult.m1773getOrNullimpl(holder);
                    if (element != null) {
                    }
                    break;
                case 2:
                    c13012 = this;
                    int i3 = c13012.I$1;
                    int remainingAbsentValues4 = c13012.I$0;
                    byte[] lastReceivedEpoch3 = (byte[]) c13012.L$2;
                    resultChannel = (Channel) c13012.L$1;
                    Object[] latestValues3 = (Object[]) c13012.L$0;
                    ResultKt.throwOnFailure($result);
                    latestValues = latestValues3;
                    remainingAbsentValues = i3;
                    lastReceivedEpoch = lastReceivedEpoch3;
                    remainingAbsentValues2 = remainingAbsentValues4;
                    remainingAbsentValues = (byte) (remainingAbsentValues + 1);
                    c13012.L$0 = latestValues;
                    c13012.L$1 = resultChannel;
                    c13012.L$2 = lastReceivedEpoch;
                    c13012.I$0 = remainingAbsentValues2;
                    c13012.I$1 = remainingAbsentValues;
                    c13012.label = 1;
                    holder = resultChannel.mo1761receiveCatchingJP2dKIU(c13012);
                    if (holder == coroutine_suspended) {
                    }
                    break;
                case 3:
                    c13012 = this;
                    int i4 = c13012.I$1;
                    int remainingAbsentValues5 = c13012.I$0;
                    byte[] lastReceivedEpoch4 = (byte[]) c13012.L$2;
                    resultChannel = (Channel) c13012.L$1;
                    Object[] latestValues4 = (Object[]) c13012.L$0;
                    ResultKt.throwOnFailure($result);
                    latestValues = latestValues4;
                    remainingAbsentValues = i4;
                    lastReceivedEpoch = lastReceivedEpoch4;
                    remainingAbsentValues2 = remainingAbsentValues5;
                    remainingAbsentValues = (byte) (remainingAbsentValues + 1);
                    c13012.L$0 = latestValues;
                    c13012.L$1 = resultChannel;
                    c13012.L$2 = lastReceivedEpoch;
                    c13012.I$0 = remainingAbsentValues2;
                    c13012.I$1 = remainingAbsentValues;
                    c13012.label = 1;
                    holder = resultChannel.mo1761receiveCatchingJP2dKIU(c13012);
                    if (holder == coroutine_suspended) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* compiled from: Combine.kt */
        @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        @DebugMetadata(m161c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1", m162f = "Combine.kt", m163i = {}, m164l = {28}, m165m = "invokeSuspend", m166n = {}, m167s = {})
        /* renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1, reason: invalid class name */
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
            final /* synthetic */ Flow<T>[] $flows;

            /* renamed from: $i */
            final /* synthetic */ int f233$i;
            final /* synthetic */ AtomicInteger $nonClosed;
            final /* synthetic */ Channel<IndexedValue<Object>> $resultChannel;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            AnonymousClass1(Flow<? extends T>[] flowArr, int i, AtomicInteger atomicInteger, Channel<IndexedValue<Object>> channel, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.$flows = flowArr;
                this.f233$i = i;
                this.$nonClosed = atomicInteger;
                this.$resultChannel = channel;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                return new AnonymousClass1(this.$flows, this.f233$i, this.$nonClosed, this.$resultChannel, continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
                return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            /* JADX WARN: Removed duplicated region for block: B:18:0x0045  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x005c  */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public final Object invokeSuspend(Object $result) throws Throwable {
                Throwable th;
                AnonymousClass1 anonymousClass1;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        try {
                            this.label = 1;
                            if (this.$flows[this.f233$i].collect(new C13421(this.$resultChannel, this.f233$i), this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            anonymousClass1 = this;
                            if (anonymousClass1.$nonClosed.decrementAndGet() == 0) {
                                SendChannel.DefaultImpls.close$default(anonymousClass1.$resultChannel, null, 1, null);
                            }
                            return Unit.INSTANCE;
                        } catch (Throwable th2) {
                            th = th2;
                            anonymousClass1 = this;
                            if (anonymousClass1.$nonClosed.decrementAndGet() == 0) {
                                SendChannel.DefaultImpls.close$default(anonymousClass1.$resultChannel, null, 1, null);
                            }
                            throw th;
                        }
                    case 1:
                        anonymousClass1 = this;
                        try {
                            ResultKt.throwOnFailure($result);
                            if (anonymousClass1.$nonClosed.decrementAndGet() == 0) {
                            }
                            return Unit.INSTANCE;
                        } catch (Throwable th3) {
                            th = th3;
                            if (anonymousClass1.$nonClosed.decrementAndGet() == 0) {
                            }
                            throw th;
                        }
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }

            /* compiled from: Combine.kt */
            @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0003H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006"}, m146d2 = {"<anonymous>", "", "R", "T", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
            /* renamed from: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1$1, reason: invalid class name and collision with other inner class name */
            static final class C13421<T> implements FlowCollector {

                /* renamed from: $i */
                final /* synthetic */ int f234$i;
                final /* synthetic */ Channel<IndexedValue<Object>> $resultChannel;

                C13421(Channel<IndexedValue<Object>> channel, int i) {
                    this.$resultChannel = channel;
                    this.f234$i = i;
                }

                /* JADX WARN: Removed duplicated region for block: B:19:0x0055 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public final Object emit(T t, Continuation<? super Unit> continuation) {
                    CombineKt$combineInternal$2$1$1$emit$1 combineKt$combineInternal$2$1$1$emit$1;
                    if (continuation instanceof CombineKt$combineInternal$2$1$1$emit$1) {
                        combineKt$combineInternal$2$1$1$emit$1 = (CombineKt$combineInternal$2$1$1$emit$1) continuation;
                        if ((combineKt$combineInternal$2$1$1$emit$1.label & Integer.MIN_VALUE) != 0) {
                            combineKt$combineInternal$2$1$1$emit$1.label -= Integer.MIN_VALUE;
                        } else {
                            combineKt$combineInternal$2$1$1$emit$1 = new CombineKt$combineInternal$2$1$1$emit$1(this, continuation);
                        }
                    }
                    CombineKt$combineInternal$2$1$1$emit$1 combineKt$combineInternal$2$1$1$emit$12 = combineKt$combineInternal$2$1$1$emit$1;
                    Object $result = combineKt$combineInternal$2$1$1$emit$12.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    switch (combineKt$combineInternal$2$1$1$emit$12.label) {
                        case 0:
                            ResultKt.throwOnFailure($result);
                            Channel<IndexedValue<Object>> channel = this.$resultChannel;
                            IndexedValue<Object> indexedValue = new IndexedValue<>(this.f234$i, t);
                            combineKt$combineInternal$2$1$1$emit$12.label = 1;
                            Object value = channel.send(indexedValue, combineKt$combineInternal$2$1$1$emit$12);
                            if (value == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            combineKt$combineInternal$2$1$1$emit$12.label = 2;
                            if (YieldKt.yield(combineKt$combineInternal$2$1$1$emit$12) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            return Unit.INSTANCE;
                        case 1:
                            ResultKt.throwOnFailure($result);
                            combineKt$combineInternal$2$1$1$emit$12.label = 2;
                            if (YieldKt.yield(combineKt$combineInternal$2$1$1$emit$12) == coroutine_suspended) {
                            }
                            return Unit.INSTANCE;
                        case 2:
                            ResultKt.throwOnFailure($result);
                            return Unit.INSTANCE;
                        default:
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
            }
        }
    }

    public static final <R, T> Object combineInternal(FlowCollector<? super R> flowCollector, Flow<? extends T>[] flowArr, Function0<T[]> function0, Function3<? super FlowCollector<? super R>, ? super T[], ? super Continuation<? super Unit>, ? extends Object> function3, Continuation<? super Unit> continuation) {
        Object objFlowScope = FlowCoroutineKt.flowScope(new C13012(flowArr, function0, function3, flowCollector, null), continuation);
        return objFlowScope == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objFlowScope : Unit.INSTANCE;
    }

    public static final <T1, T2, R> Flow<R> zipImpl(final Flow<? extends T1> flow, final Flow<? extends T2> flow2, final Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> function3) {
        return new Flow<R>() { // from class: kotlinx.coroutines.flow.internal.CombineKt$zipImpl$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public Object collect(FlowCollector<? super R> flowCollector, Continuation<? super Unit> continuation) {
                Object objCoroutineScope = CoroutineScopeKt.coroutineScope(new CombineKt$zipImpl$1$1(flow2, flow, flowCollector, function3, null), continuation);
                return objCoroutineScope == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCoroutineScope : Unit.INSTANCE;
            }
        };
    }
}
