package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.AbstractTimeSource;
import kotlinx.coroutines.AbstractTimeSourceKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoop_commonKt;
import kotlinx.coroutines.GlobalScope;
import org.xbill.DNS.WKSRecord;

/* compiled from: TickerChannels.kt */
@Metadata(m145d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a,\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@¢\u0006\u0002\u0010\u0007\u001a,\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007¨\u0006\u000f"}, m146d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class TickerChannelsKt {

    /* compiled from: TickerChannels.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.TickerChannelsKt", m162f = "TickerChannels.kt", m163i = {0, 0, 1, 1, 2, 2}, m164l = {102, 104, 105}, m165m = "fixedDelayTicker", m166n = {"channel", "delayMillis", "channel", "delayMillis", "channel", "delayMillis"}, m167s = {"L$0", "J$0", "L$0", "J$0", "L$0", "J$0"})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 */
    static final class C11771 extends ContinuationImpl {
        long J$0;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11771(Continuation<? super C11771> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TickerChannelsKt.fixedDelayTicker(0L, 0L, null, this);
        }
    }

    /* compiled from: TickerChannels.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.TickerChannelsKt", m162f = "TickerChannels.kt", m163i = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3}, m164l = {80, 84, 90, 92}, m165m = "fixedPeriodTicker", m166n = {"channel", "delayMillis", "deadline", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs"}, m167s = {"L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1"})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$fixedPeriodTicker$1 */
    static final class C11781 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11781(Continuation<? super C11781> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TickerChannelsKt.fixedPeriodTicker(0L, 0L, null, this);
        }
    }

    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long delayMillis, long initialDelayMillis, CoroutineContext context, TickerMode mode) {
        if (!(delayMillis >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative delay, but has " + delayMillis + " ms").toString());
        }
        if (!(initialDelayMillis >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative initial delay, but has " + initialDelayMillis + " ms").toString());
        }
        return ProduceKt.produce(GlobalScope.INSTANCE, Dispatchers.getUnconfined().plus(context), 0, new C11793(mode, delayMillis, initialDelayMillis, null));
    }

    /* compiled from: TickerChannels.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.TickerChannelsKt$ticker$3", m162f = "TickerChannels.kt", m163i = {}, m164l = {WKSRecord.Service.BOOTPC, 69}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$ticker$3 */
    static final class C11793 extends SuspendLambda implements Function2<ProducerScope<? super Unit>, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $delayMillis;
        final /* synthetic */ long $initialDelayMillis;
        final /* synthetic */ TickerMode $mode;
        private /* synthetic */ Object L$0;
        int label;

        /* compiled from: TickerChannels.kt */
        @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
        /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$ticker$3$WhenMappings */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[TickerMode.values().length];
                try {
                    iArr[TickerMode.FIXED_PERIOD.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[TickerMode.FIXED_DELAY.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C11793(TickerMode tickerMode, long j, long j2, Continuation<? super C11793> continuation) {
            super(2, continuation);
            this.$mode = tickerMode;
            this.$delayMillis = j;
            this.$initialDelayMillis = j2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11793 c11793 = new C11793(this.$mode, this.$delayMillis, this.$initialDelayMillis, continuation);
            c11793.L$0 = obj;
            return c11793;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super Unit> producerScope, Continuation<? super Unit> continuation) {
            return ((C11793) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            C11793 c11793;
            C11793 c117932;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    ProducerScope $this$produce = (ProducerScope) this.L$0;
                    switch (WhenMappings.$EnumSwitchMapping$0[this.$mode.ordinal()]) {
                        case 1:
                            this.label = 1;
                            if (TickerChannelsKt.fixedPeriodTicker(this.$delayMillis, this.$initialDelayMillis, $this$produce.getChannel(), this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            c11793 = this;
                            return Unit.INSTANCE;
                        case 2:
                            this.label = 2;
                            if (TickerChannelsKt.fixedDelayTicker(this.$delayMillis, this.$initialDelayMillis, $this$produce.getChannel(), this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            c117932 = this;
                            return Unit.INSTANCE;
                        default:
                            return Unit.INSTANCE;
                    }
                case 1:
                    c11793 = this;
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                case 2:
                    c117932 = this;
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Path cross not found for [B:35:0x00c1, B:42:0x00e3], limit reached: 45 */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00f6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x00e0 -> B:25:0x0090). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x00f7 -> B:25:0x0090). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object fixedPeriodTicker(long delayMillis, long initialDelayMillis, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        C11781 c11781;
        long delayMillis2;
        long delayNs;
        long delayNs2;
        long deadline;
        long deadline2;
        long nextDelay;
        long delayNs3;
        Unit unit;
        if (continuation instanceof C11781) {
            c11781 = (C11781) continuation;
            if ((c11781.label & Integer.MIN_VALUE) != 0) {
                c11781.label -= Integer.MIN_VALUE;
            } else {
                c11781 = new C11781(continuation);
            }
        }
        C11781 c117812 = c11781;
        Object $result = c117812.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c117812.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                AbstractTimeSource abstractTimeSource = AbstractTimeSourceKt.timeSource;
                long deadline3 = (abstractTimeSource != null ? abstractTimeSource.nanoTime() : System.nanoTime()) + EventLoop_commonKt.delayToNanos(initialDelayMillis);
                c117812.L$0 = sendChannel;
                c117812.J$0 = delayMillis;
                c117812.J$1 = deadline3;
                c117812.label = 1;
                if (DelayKt.delay(initialDelayMillis, c117812) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                delayMillis2 = delayMillis;
                delayNs = deadline3;
                delayNs2 = EventLoop_commonKt.delayToNanos(delayMillis2);
                long deadline4 = delayNs + delayNs2;
                unit = Unit.INSTANCE;
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline4;
                c117812.J$1 = delayNs2;
                c117812.label = 2;
                if (sendChannel.send(unit, c117812) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                deadline2 = deadline4;
                deadline = delayNs2;
                AbstractTimeSource abstractTimeSource2 = AbstractTimeSourceKt.timeSource;
                long now = abstractTimeSource2 == null ? abstractTimeSource2.nanoTime() : System.nanoTime();
                nextDelay = RangesKt.coerceAtLeast(deadline2 - now, 0L);
                if (nextDelay == 0 || deadline == 0) {
                    delayNs3 = EventLoop_commonKt.delayNanosToMillis(nextDelay);
                    c117812.L$0 = sendChannel;
                    c117812.J$0 = deadline2;
                    c117812.J$1 = deadline;
                    c117812.label = 4;
                    if (DelayKt.delay(delayNs3, c117812) != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    delayNs2 = deadline;
                    delayNs = deadline2;
                } else {
                    long adjustedDelay = deadline - ((now - deadline2) % deadline);
                    long deadline5 = now + adjustedDelay;
                    long now2 = EventLoop_commonKt.delayNanosToMillis(adjustedDelay);
                    c117812.L$0 = sendChannel;
                    c117812.J$0 = deadline5;
                    c117812.J$1 = deadline;
                    c117812.label = 3;
                    if (DelayKt.delay(now2, c117812) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    delayNs2 = deadline;
                    delayNs = deadline5;
                }
                long deadline42 = delayNs + delayNs2;
                unit = Unit.INSTANCE;
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline42;
                c117812.J$1 = delayNs2;
                c117812.label = 2;
                if (sendChannel.send(unit, c117812) == coroutine_suspended) {
                }
                break;
            case 1:
                delayNs = c117812.J$1;
                delayMillis2 = c117812.J$0;
                sendChannel = (SendChannel) c117812.L$0;
                ResultKt.throwOnFailure($result);
                delayNs2 = EventLoop_commonKt.delayToNanos(delayMillis2);
                long deadline422 = delayNs + delayNs2;
                unit = Unit.INSTANCE;
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline422;
                c117812.J$1 = delayNs2;
                c117812.label = 2;
                if (sendChannel.send(unit, c117812) == coroutine_suspended) {
                }
                break;
            case 2:
                deadline = c117812.J$1;
                deadline2 = c117812.J$0;
                sendChannel = (SendChannel) c117812.L$0;
                ResultKt.throwOnFailure($result);
                AbstractTimeSource abstractTimeSource22 = AbstractTimeSourceKt.timeSource;
                if (abstractTimeSource22 == null) {
                }
                nextDelay = RangesKt.coerceAtLeast(deadline2 - now, 0L);
                if (nextDelay == 0) {
                    break;
                }
                delayNs3 = EventLoop_commonKt.delayNanosToMillis(nextDelay);
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline2;
                c117812.J$1 = deadline;
                c117812.label = 4;
                if (DelayKt.delay(delayNs3, c117812) != coroutine_suspended) {
                }
                break;
            case 3:
                long delayNs4 = c117812.J$1;
                long deadline6 = c117812.J$0;
                sendChannel = (SendChannel) c117812.L$0;
                ResultKt.throwOnFailure($result);
                delayNs2 = delayNs4;
                delayNs = deadline6;
                long deadline4222 = delayNs + delayNs2;
                unit = Unit.INSTANCE;
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline4222;
                c117812.J$1 = delayNs2;
                c117812.label = 2;
                if (sendChannel.send(unit, c117812) == coroutine_suspended) {
                }
                break;
            case 4:
                long delayNs5 = c117812.J$1;
                long deadline7 = c117812.J$0;
                sendChannel = (SendChannel) c117812.L$0;
                ResultKt.throwOnFailure($result);
                delayNs2 = delayNs5;
                delayNs = deadline7;
                long deadline42222 = delayNs + delayNs2;
                unit = Unit.INSTANCE;
                c117812.L$0 = sendChannel;
                c117812.J$0 = deadline42222;
                c117812.J$1 = delayNs2;
                c117812.label = 2;
                if (sendChannel.send(unit, c117812) == coroutine_suspended) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0079 -> B:20:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final java.lang.Object fixedDelayTicker(long r3, long r5, kotlinx.coroutines.channels.SendChannel<? super kotlin.Unit> r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) {
        /*
            boolean r0 = r8 instanceof kotlinx.coroutines.channels.TickerChannelsKt.C11771
            if (r0 == 0) goto L14
            r0 = r8
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = (kotlinx.coroutines.channels.TickerChannelsKt.C11771) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L19
        L14:
            kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 r0 = new kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1
            r0.<init>(r8)
        L19:
            r8 = r0
            java.lang.Object r0 = r8.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r8.label
            switch(r2) {
                case 0: goto L4b;
                case 1: goto L41;
                case 2: goto L37;
                case 3: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.String r4 = "call to 'resume' before 'invoke' with coroutine"
            r3.<init>(r4)
            throw r3
        L2d:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L7c
        L37:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L6e
        L41:
            long r3 = r8.J$0
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.channels.SendChannel r5 = (kotlinx.coroutines.channels.SendChannel) r5
            kotlin.ResultKt.throwOnFailure(r0)
            goto L5d
        L4b:
            kotlin.ResultKt.throwOnFailure(r0)
            r8.L$0 = r7
            r8.J$0 = r3
            r2 = 1
            r8.label = r2
            java.lang.Object r5 = kotlinx.coroutines.DelayKt.delay(r5, r8)
            if (r5 != r1) goto L5c
            return r1
        L5c:
            r5 = r7
        L5d:
        L5e:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            r8.L$0 = r5
            r8.J$0 = r3
            r7 = 2
            r8.label = r7
            java.lang.Object r6 = r5.send(r6, r8)
            if (r6 != r1) goto L6e
            return r1
        L6e:
            r8.L$0 = r5
            r8.J$0 = r3
            r6 = 3
            r8.label = r6
            java.lang.Object r6 = kotlinx.coroutines.DelayKt.delay(r3, r8)
            if (r6 != r1) goto L7c
            return r1
        L7c:
            goto L5e
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.TickerChannelsKt.fixedDelayTicker(long, long, kotlinx.coroutines.channels.SendChannel, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
