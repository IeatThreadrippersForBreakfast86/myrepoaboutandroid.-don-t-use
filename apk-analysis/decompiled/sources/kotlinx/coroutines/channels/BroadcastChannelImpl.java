package kotlinx.coroutines.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.selects.SelectImplementation;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.TrySelectDetailedResult;

/* compiled from: BroadcastChannel.kt */
@Metadata(m145d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\n\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u000245B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0017\u0010\u001f\u001a\u00020\n2\b\u0010 \u001a\u0004\u0018\u00010!H\u0010¢\u0006\u0002\b\"J\u0012\u0010#\u001a\u00020\n2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u000e\u0010$\u001a\b\u0012\u0004\u0012\u00028\u00000%H\u0016J\u001e\u0010&\u001a\u00020'2\n\u0010(\u001a\u0006\u0012\u0002\b\u00030\u00132\b\u0010)\u001a\u0004\u0018\u00010\rH\u0014J\u0016\u0010*\u001a\u00020'2\f\u0010+\u001a\b\u0012\u0004\u0012\u00028\u00000%H\u0002J\u0016\u0010,\u001a\u00020'2\u0006\u0010)\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010-J\b\u0010.\u001a\u00020/H\u0016J#\u00100\u001a\b\u0012\u0004\u0012\u00020'012\u0006\u0010)\u001a\u00028\u0000H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b2\u00103R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00060\u000fj\u0002`\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R6\u0010\u0011\u001a*\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0013\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0012j\u0014\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0013\u0012\u0006\u0012\u0004\u0018\u00010\r`\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u001c\u001a\u0004\u0018\u00018\u00008F¢\u0006\f\u0012\u0004\b\u001d\u0010\u0019\u001a\u0004\b\u001e\u0010\u001b\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u00066"}, m146d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl;", "E", "Lkotlinx/coroutines/channels/BufferedChannel;", "Lkotlinx/coroutines/channels/BroadcastChannel;", "capacity", "", "(I)V", "getCapacity", "()I", "isClosedForSend", "", "()Z", "lastConflatedElement", "", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "onSendInternalResult", "Ljava/util/HashMap;", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/collections/HashMap;", "subscribers", "", "value", "getValue$annotations", "()V", "getValue", "()Ljava/lang/Object;", "valueOrNull", "getValueOrNull$annotations", "getValueOrNull", "cancelImpl", "cause", "", "cancelImpl$kotlinx_coroutines_core", "close", "openSubscription", "Lkotlinx/coroutines/channels/ReceiveChannel;", "registerSelectForSend", "", "select", "element", "removeSubscriber", "s", "send", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toString", "", "trySend", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "SubscriberBuffered", "SubscriberConflated", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class BroadcastChannelImpl<E> extends BufferedChannel<E> implements BroadcastChannel<E> {
    private final int capacity;
    private Object lastConflatedElement;
    private final ReentrantLock lock;
    private final HashMap<SelectInstance<?>, Object> onSendInternalResult;
    private List<? extends BufferedChannel<E>> subscribers;

    /* compiled from: BroadcastChannel.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.BroadcastChannelImpl", m162f = "BroadcastChannel.kt", m163i = {0, 0}, m164l = {227}, m165m = "send", m166n = {"this", "element"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.BroadcastChannelImpl$send$1 */
    static final class C11231 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;
        final /* synthetic */ BroadcastChannelImpl<E> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C11231(BroadcastChannelImpl<E> broadcastChannelImpl, Continuation<? super C11231> continuation) {
            super(continuation);
            this.this$0 = broadcastChannelImpl;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return this.this$0.send(null, this);
        }
    }

    public static /* synthetic */ void getValue$annotations() {
    }

    public static /* synthetic */ void getValueOrNull$annotations() {
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public BroadcastChannelImpl(int capacity) {
        super(0, null);
        this.capacity = capacity;
        if (!(this.capacity >= 1 || this.capacity == -1)) {
            throw new IllegalArgumentException(("BroadcastChannel capacity must be positive or Channel.CONFLATED, but " + this.capacity + " was specified").toString());
        }
        this.lock = new ReentrantLock();
        this.subscribers = CollectionsKt.emptyList();
        this.lastConflatedElement = BroadcastChannelKt.NO_ELEMENT;
        this.onSendInternalResult = new HashMap<>();
    }

    @Override // kotlinx.coroutines.channels.BroadcastChannel
    public ReceiveChannel<E> openSubscription() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            BufferedChannel s = this.capacity == -1 ? new SubscriberConflated() : new SubscriberBuffered();
            if (!isClosedForSend() || this.lastConflatedElement != BroadcastChannelKt.NO_ELEMENT) {
                if (this.lastConflatedElement != BroadcastChannelKt.NO_ELEMENT) {
                    s.mo1757trySendJP2dKIU(getValue());
                }
                this.subscribers = CollectionsKt.plus((Collection<? extends BufferedChannel>) this.subscribers, s);
                reentrantLock.unlock();
                return s;
            }
            s.close(getCloseCause());
            return s;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeSubscriber(ReceiveChannel<? extends E> s) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            Iterable $this$filter$iv = this.subscribers;
            ArrayList arrayList = new ArrayList();
            for (Object element$iv$iv : $this$filter$iv) {
                BufferedChannel it = (BufferedChannel) element$iv$iv;
                if (it != s) {
                    arrayList.add(element$iv$iv);
                }
            }
            this.subscribers = arrayList;
            Unit unit = Unit.INSTANCE;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x008a -> B:27:0x008e). Please report as a decompilation issue!!! */
    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object send(E e, Continuation<? super Unit> continuation) throws Throwable {
        C11231 c11231;
        Iterator it;
        BroadcastChannelImpl broadcastChannelImpl;
        Object element;
        Object $result;
        Object obj;
        boolean success;
        if (continuation instanceof C11231) {
            c11231 = (C11231) continuation;
            if ((c11231.label & Integer.MIN_VALUE) != 0) {
                c11231.label -= Integer.MIN_VALUE;
            } else {
                c11231 = new C11231(this, continuation);
            }
        }
        C11231 c112312 = c11231;
        Object $result2 = c112312.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c112312.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                ReentrantLock $this$withLock$iv = this.lock;
                ReentrantLock reentrantLock = $this$withLock$iv;
                reentrantLock.lock();
                try {
                    if (isClosedForSend()) {
                        throw getSendException();
                    }
                    if (this.capacity == -1) {
                        this.lastConflatedElement = e;
                    }
                    Iterable subs = this.subscribers;
                    reentrantLock.unlock();
                    Iterable $this$forEach$iv = subs;
                    it = $this$forEach$iv.iterator();
                    broadcastChannelImpl = this;
                    element = e;
                    if (it.hasNext()) {
                        Object element$iv = it.next();
                        c112312.L$0 = broadcastChannelImpl;
                        c112312.L$1 = element;
                        c112312.L$2 = it;
                        c112312.label = 1;
                        Object objSendBroadcast$kotlinx_coroutines_core = ((BufferedChannel) element$iv).sendBroadcast$kotlinx_coroutines_core(element, c112312);
                        if (objSendBroadcast$kotlinx_coroutines_core == $result3) {
                            return $result3;
                        }
                        Object obj2 = $result3;
                        $result = $result2;
                        $result2 = objSendBroadcast$kotlinx_coroutines_core;
                        obj = obj2;
                        success = ((Boolean) $result2).booleanValue();
                        if (success && broadcastChannelImpl.isClosedForSend()) {
                            throw broadcastChannelImpl.getSendException();
                        }
                        $result2 = $result;
                        $result3 = obj;
                        if (it.hasNext()) {
                            return Unit.INSTANCE;
                        }
                    }
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
                break;
            case 1:
                it = (Iterator) c112312.L$2;
                element = c112312.L$1;
                broadcastChannelImpl = (BroadcastChannelImpl) c112312.L$0;
                ResultKt.throwOnFailure($result2);
                obj = $result3;
                $result = $result2;
                success = ((Boolean) $result2).booleanValue();
                if (success) {
                    break;
                }
                $result2 = $result;
                $result3 = obj;
                if (it.hasNext()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    /* renamed from: trySend-JP2dKIU, reason: not valid java name */
    public Object mo1757trySendJP2dKIU(E element) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (isClosedForSend()) {
                return super.mo1757trySendJP2dKIU(element);
            }
            List<? extends BufferedChannel<E>> list = this.subscribers;
            char c = 0;
            c = 0;
            if (!(list instanceof Collection) || !list.isEmpty()) {
                Iterator<T> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (((BufferedChannel) it.next()).shouldSendSuspend$kotlinx_coroutines_core()) {
                        c = 1;
                        break;
                    }
                }
            }
            if (c != null) {
                return ChannelResult.INSTANCE.m1782failurePtdJZtk();
            }
            if (this.capacity == -1) {
                this.lastConflatedElement = element;
            }
            Iterator<T> it2 = this.subscribers.iterator();
            while (it2.hasNext()) {
                ((BufferedChannel) it2.next()).mo1757trySendJP2dKIU(element);
            }
            return ChannelResult.INSTANCE.m1783successJP2dKIU(Unit.INSTANCE);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    protected void registerSelectForSend(SelectInstance<?> select, Object element) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            Object result = this.onSendInternalResult.remove(select);
            if (result != null) {
                select.selectInRegistrationPhase(result);
                return;
            }
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(select.getContext()), null, CoroutineStart.UNDISPATCHED, new C11222(this, element, select, null), 1, null);
        } finally {
            reentrantLock.unlock();
        }
    }

    /* compiled from: BroadcastChannel.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.BroadcastChannelImpl$registerSelectForSend$2", m162f = "BroadcastChannel.kt", m163i = {}, m164l = {288}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.BroadcastChannelImpl$registerSelectForSend$2 */
    static final class C11222 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Object $element;
        final /* synthetic */ SelectInstance<?> $select;
        int label;
        final /* synthetic */ BroadcastChannelImpl<E> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C11222(BroadcastChannelImpl<E> broadcastChannelImpl, Object obj, SelectInstance<?> selectInstance, Continuation<? super C11222> continuation) {
            super(2, continuation);
            this.this$0 = broadcastChannelImpl;
            this.$element = obj;
            this.$select = selectInstance;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C11222(this.this$0, this.$element, this.$select, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C11222) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0064 A[Catch: all -> 0x00b0, TryCatch #0 {all -> 0x00b0, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a0, B:43:0x00a7, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0081 A[Catch: all -> 0x00b0, TryCatch #0 {all -> 0x00b0, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a0, B:43:0x00a7, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0084 A[Catch: all -> 0x00b0, TryCatch #0 {all -> 0x00b0, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a0, B:43:0x00a7, B:39:0x0084), top: B:50:0x005e }] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00a0 A[Catch: all -> 0x00b0, TryCatch #0 {all -> 0x00b0, blocks: (B:27:0x005e, B:29:0x0064, B:34:0x0073, B:35:0x0078, B:36:0x0079, B:38:0x0081, B:40:0x0088, B:42:0x00a0, B:43:0x00a7, B:39:0x0084), top: B:50:0x005e }] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            Throwable th;
            C11222 c11222;
            Object obj2;
            BroadcastChannelImpl<E> broadcastChannelImpl;
            SelectInstance<?> selectInstance;
            ReentrantLock reentrantLock;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    try {
                        this.label = 1;
                    } catch (Throwable th2) {
                        th = th2;
                        c11222 = this;
                        if (c11222.this$0.isClosedForSend() || (!(th instanceof ClosedSendChannelException) && c11222.this$0.getSendException() != th)) {
                            throw th;
                        }
                        obj2 = null;
                        ReentrantLock reentrantLock2 = ((BroadcastChannelImpl) c11222.this$0).lock;
                        broadcastChannelImpl = c11222.this$0;
                        selectInstance = c11222.$select;
                        reentrantLock = reentrantLock2;
                        reentrantLock.lock();
                        try {
                            if (DebugKt.getASSERTIONS_ENABLED()) {
                            }
                            ((BroadcastChannelImpl) broadcastChannelImpl).onSendInternalResult.put(selectInstance, obj2 != null ? Unit.INSTANCE : BufferedChannelKt.getCHANNEL_CLOSED());
                            Intrinsics.checkNotNull(selectInstance, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>");
                            if (((SelectImplementation) selectInstance).trySelectDetailed(broadcastChannelImpl, Unit.INSTANCE) != TrySelectDetailedResult.REREGISTER) {
                            }
                            Unit unit = Unit.INSTANCE;
                            reentrantLock.unlock();
                            return Unit.INSTANCE;
                        } catch (Throwable th3) {
                            reentrantLock.unlock();
                            throw th3;
                        }
                    }
                    if (this.this$0.send(this.$element, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c11222 = this;
                    obj2 = 1;
                    ReentrantLock reentrantLock22 = ((BroadcastChannelImpl) c11222.this$0).lock;
                    broadcastChannelImpl = c11222.this$0;
                    selectInstance = c11222.$select;
                    reentrantLock = reentrantLock22;
                    reentrantLock.lock();
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                        if (!(((BroadcastChannelImpl) broadcastChannelImpl).onSendInternalResult.get(selectInstance) == null)) {
                            throw new AssertionError();
                        }
                    }
                    ((BroadcastChannelImpl) broadcastChannelImpl).onSendInternalResult.put(selectInstance, obj2 != null ? Unit.INSTANCE : BufferedChannelKt.getCHANNEL_CLOSED());
                    Intrinsics.checkNotNull(selectInstance, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>");
                    if (((SelectImplementation) selectInstance).trySelectDetailed(broadcastChannelImpl, Unit.INSTANCE) != TrySelectDetailedResult.REREGISTER) {
                        ((BroadcastChannelImpl) broadcastChannelImpl).onSendInternalResult.remove(selectInstance);
                    }
                    Unit unit2 = Unit.INSTANCE;
                    reentrantLock.unlock();
                    return Unit.INSTANCE;
                case 1:
                    c11222 = this;
                    try {
                        ResultKt.throwOnFailure(obj);
                        obj2 = 1;
                    } catch (Throwable th4) {
                        th = th4;
                        if (c11222.this$0.isClosedForSend()) {
                            break;
                        }
                        throw th;
                    }
                    ReentrantLock reentrantLock222 = ((BroadcastChannelImpl) c11222.this$0).lock;
                    broadcastChannelImpl = c11222.this$0;
                    selectInstance = c11222.$select;
                    reentrantLock = reentrantLock222;
                    reentrantLock.lock();
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                    }
                    ((BroadcastChannelImpl) broadcastChannelImpl).onSendInternalResult.put(selectInstance, obj2 != null ? Unit.INSTANCE : BufferedChannelKt.getCHANNEL_CLOSED());
                    Intrinsics.checkNotNull(selectInstance, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>");
                    if (((SelectImplementation) selectInstance).trySelectDetailed(broadcastChannelImpl, Unit.INSTANCE) != TrySelectDetailedResult.REREGISTER) {
                    }
                    Unit unit22 = Unit.INSTANCE;
                    reentrantLock.unlock();
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    public boolean close(Throwable cause) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            Iterable $this$forEach$iv = this.subscribers;
            for (Object element$iv : $this$forEach$iv) {
                BufferedChannel it = (BufferedChannel) element$iv;
                it.close(cause);
            }
            Iterable $this$forEach$iv2 = this.subscribers;
            Iterable $this$filter$iv = $this$forEach$iv2;
            ArrayList arrayList = new ArrayList();
            for (Object element$iv$iv : $this$filter$iv) {
                BufferedChannel it2 = (BufferedChannel) element$iv$iv;
                if (it2.hasElements$kotlinx_coroutines_core()) {
                    arrayList.add(element$iv$iv);
                }
            }
            this.subscribers = arrayList;
            return super.close(cause);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public boolean cancelImpl$kotlinx_coroutines_core(Throwable cause) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            Iterable $this$forEach$iv = this.subscribers;
            for (Object element$iv : $this$forEach$iv) {
                BufferedChannel it = (BufferedChannel) element$iv;
                it.cancelImpl$kotlinx_coroutines_core(cause);
            }
            this.lastConflatedElement = BroadcastChannelKt.NO_ELEMENT;
            return super.cancelImpl$kotlinx_coroutines_core(cause);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel, kotlinx.coroutines.channels.SendChannel
    public boolean isClosedForSend() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return super.isClosedForSend();
        } finally {
            reentrantLock.unlock();
        }
    }

    /* compiled from: BroadcastChannel.kt */
    @Metadata(m145d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016¨\u0006\u0007"}, m146d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl$SubscriberBuffered;", "Lkotlinx/coroutines/channels/BufferedChannel;", "(Lkotlinx/coroutines/channels/BroadcastChannelImpl;)V", "cancelImpl", "", "cause", "", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    private final class SubscriberBuffered extends BufferedChannel<E> {
        /* JADX WARN: Multi-variable type inference failed */
        public SubscriberBuffered() {
            super(BroadcastChannelImpl.this.getCapacity(), null, 2, 0 == true ? 1 : 0);
        }

        @Override // kotlinx.coroutines.channels.BufferedChannel
        /* renamed from: cancelImpl, reason: merged with bridge method [inline-methods] */
        public boolean cancelImpl$kotlinx_coroutines_core(Throwable cause) {
            ReentrantLock $this$withLock$iv = ((BroadcastChannelImpl) BroadcastChannelImpl.this).lock;
            BroadcastChannelImpl<E> broadcastChannelImpl = BroadcastChannelImpl.this;
            ReentrantLock reentrantLock = $this$withLock$iv;
            reentrantLock.lock();
            try {
                broadcastChannelImpl.removeSubscriber(this);
                return super.cancelImpl$kotlinx_coroutines_core(cause);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /* compiled from: BroadcastChannel.kt */
    @Metadata(m145d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016¨\u0006\u0007"}, m146d2 = {"Lkotlinx/coroutines/channels/BroadcastChannelImpl$SubscriberConflated;", "Lkotlinx/coroutines/channels/ConflatedBufferedChannel;", "(Lkotlinx/coroutines/channels/BroadcastChannelImpl;)V", "cancelImpl", "", "cause", "", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    private final class SubscriberConflated extends ConflatedBufferedChannel<E> {
        public SubscriberConflated() {
            super(1, BufferOverflow.DROP_OLDEST, null, 4, null);
        }

        @Override // kotlinx.coroutines.channels.BufferedChannel
        /* renamed from: cancelImpl, reason: merged with bridge method [inline-methods] */
        public boolean cancelImpl$kotlinx_coroutines_core(Throwable cause) {
            BroadcastChannelImpl.this.removeSubscriber(this);
            return super.cancelImpl$kotlinx_coroutines_core(cause);
        }
    }

    public final E getValue() throws Throwable {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (!isClosedForSend()) {
                if (this.lastConflatedElement == BroadcastChannelKt.NO_ELEMENT) {
                    throw new IllegalStateException("No value".toString());
                }
                return (E) this.lastConflatedElement;
            }
            Throwable closeCause = getCloseCause();
            if (closeCause == null) {
                throw new IllegalStateException("This broadcast channel is closed");
            }
            throw closeCause;
        } finally {
            reentrantLock.unlock();
        }
    }

    public final E getValueOrNull() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            E e = null;
            if (!isClosedForReceive() && this.lastConflatedElement != BroadcastChannelKt.NO_ELEMENT) {
                e = (E) this.lastConflatedElement;
            }
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.BufferedChannel
    public String toString() {
        return (this.lastConflatedElement != BroadcastChannelKt.NO_ELEMENT ? "CONFLATED_ELEMENT=" + this.lastConflatedElement + "; " : "") + "BROADCAST=<" + super.toString() + ">; SUBSCRIBERS=" + CollectionsKt.joinToString$default(this.subscribers, ";", "<", ">", 0, null, null, 56, null);
    }
}
