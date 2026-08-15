package kotlin.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import org.xbill.DNS.Type;

/* compiled from: SlidingWindow.kt */
@Metadata(m145d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f"}, m146d2 = {"checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int size, int step) {
        if (!(size > 0 && step > 0)) {
            throw new IllegalArgumentException((size != step ? "Both size " + size + " and step " + step + " must be greater than zero." : "size " + size + " must be greater than zero.").toString());
        }
    }

    public static final <T> Sequence<List<T>> windowedSequence(final Sequence<? extends T> sequence, final int i, final int i2, final boolean z, final boolean z2) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        checkWindowSizeStep(i, i2);
        return new Sequence<List<? extends T>>() { // from class: kotlin.collections.SlidingWindowKt$windowedSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            public Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(sequence.iterator(), i, i2, z, z2);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: SlidingWindow.kt */
    @Metadata(m145d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", ""}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", m162f = "SlidingWindow.kt", m163i = {0, 0, 0, 2, 2, 3, 3}, m164l = {34, 40, 49, 55, Type.TALINK}, m165m = "invokeSuspend", m166n = {"$this$iterator", "buffer", "gap", "$this$iterator", "buffer", "$this$iterator", "buffer"}, m167s = {"L$0", "L$1", "I$0", "L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlin.collections.SlidingWindowKt$windowedIterator$1 */
    static final class C10081<T> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Iterator<T> $iterator;
        final /* synthetic */ boolean $partialWindows;
        final /* synthetic */ boolean $reuseBuffer;
        final /* synthetic */ int $size;
        final /* synthetic */ int $step;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C10081(int i, int i2, Iterator<? extends T> it, boolean z, boolean z2, Continuation<? super C10081> continuation) {
            super(2, continuation);
            this.$size = i;
            this.$step = i2;
            this.$iterator = it;
            this.$reuseBuffer = z;
            this.$partialWindows = z2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C10081 c10081 = new C10081(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
            c10081.L$0 = obj;
            return c10081;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SequenceScope<? super List<? extends T>> sequenceScope, Continuation<? super Unit> continuation) {
            return ((C10081) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0087  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00b3  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00e8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00fc  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0147  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x0154  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x017f  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x019d  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x00ac -> B:24:0x00af). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x013a -> B:54:0x013d). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x0176 -> B:67:0x0179). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C10081 c10081;
            RingBuffer buffer;
            SequenceScope sequenceScope;
            Iterator<T> it;
            int skip;
            Object obj;
            C10081 c100812;
            SequenceScope $this$iterator;
            int gap;
            ArrayList buffer2;
            Iterator<T> it2;
            RingBuffer buffer3;
            SequenceScope sequenceScope2;
            Object obj2;
            C10081 c100813;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c10081 = this;
                    SequenceScope $this$iterator2 = (SequenceScope) c10081.L$0;
                    int bufferInitialCapacity = RangesKt.coerceAtMost(c10081.$size, 1024);
                    int gap2 = c10081.$step - c10081.$size;
                    if (gap2 >= 0) {
                        ArrayList buffer4 = new ArrayList(bufferInitialCapacity);
                        skip = 0;
                        Iterator<T> it3 = c10081.$iterator;
                        obj = coroutine_suspended;
                        c100812 = c10081;
                        $this$iterator = $this$iterator2;
                        gap = gap2;
                        buffer2 = buffer4;
                        it2 = it3;
                        while (it2.hasNext()) {
                            T next = it2.next();
                            if (skip > 0) {
                                skip--;
                            } else {
                                buffer2.add(next);
                                if (buffer2.size() == c100812.$size) {
                                    c100812.L$0 = $this$iterator;
                                    c100812.L$1 = buffer2;
                                    c100812.L$2 = it2;
                                    c100812.I$0 = gap;
                                    c100812.label = 1;
                                    if ($this$iterator.yield(buffer2, c100812) == obj) {
                                        return obj;
                                    }
                                    if (c100812.$reuseBuffer) {
                                        buffer2 = new ArrayList(c100812.$size);
                                    } else {
                                        buffer2.clear();
                                    }
                                    skip = gap;
                                    while (it2.hasNext()) {
                                    }
                                }
                            }
                        }
                        if ((true ^ buffer2.isEmpty()) && (c100812.$partialWindows || buffer2.size() == c100812.$size)) {
                            c100812.L$0 = null;
                            c100812.L$1 = null;
                            c100812.L$2 = null;
                            c100812.label = 2;
                            if ($this$iterator.yield(buffer2, c100812) == obj) {
                                return obj;
                            }
                        }
                        return Unit.INSTANCE;
                    }
                    buffer = new RingBuffer(bufferInitialCapacity);
                    sequenceScope = $this$iterator2;
                    it = c10081.$iterator;
                    while (it.hasNext()) {
                        buffer.add((RingBuffer) it.next());
                        if (buffer.isFull()) {
                            if (buffer.size() >= c10081.$size) {
                                List arrayList = c10081.$reuseBuffer ? buffer : new ArrayList(buffer);
                                c10081.L$0 = sequenceScope;
                                c10081.L$1 = buffer;
                                c10081.L$2 = it;
                                c10081.label = 3;
                                if (sequenceScope.yield(arrayList, c10081) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                buffer.removeFirst(c10081.$step);
                                while (it.hasNext()) {
                                }
                            } else {
                                buffer = buffer.expanded(c10081.$size);
                            }
                        }
                    }
                    if (c10081.$partialWindows) {
                        return Unit.INSTANCE;
                    }
                    buffer3 = buffer;
                    sequenceScope2 = sequenceScope;
                    C10081 c100814 = c10081;
                    obj2 = coroutine_suspended;
                    c100813 = c100814;
                    if (buffer3.size() <= c100813.$step) {
                        List arrayList2 = c100813.$reuseBuffer ? buffer3 : new ArrayList(buffer3);
                        c100813.L$0 = sequenceScope2;
                        c100813.L$1 = buffer3;
                        c100813.L$2 = null;
                        c100813.label = 4;
                        if (sequenceScope2.yield(arrayList2, c100813) == obj2) {
                            return obj2;
                        }
                        buffer3.removeFirst(c100813.$step);
                        if (buffer3.size() <= c100813.$step) {
                            if (true ^ buffer3.isEmpty()) {
                                c100813.L$0 = null;
                                c100813.L$1 = null;
                                c100813.L$2 = null;
                                c100813.label = 5;
                                if (sequenceScope2.yield(buffer3, c100813) == obj2) {
                                    return obj2;
                                }
                            }
                            return Unit.INSTANCE;
                        }
                    }
                    break;
                case 1:
                    gap = this.I$0;
                    it2 = (Iterator) this.L$2;
                    buffer2 = (ArrayList) this.L$1;
                    SequenceScope $this$iterator3 = (SequenceScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$iterator = $this$iterator3;
                    obj = coroutine_suspended;
                    c100812 = this;
                    if (c100812.$reuseBuffer) {
                    }
                    skip = gap;
                    while (it2.hasNext()) {
                    }
                    if (true ^ buffer2.isEmpty()) {
                        c100812.L$0 = null;
                        c100812.L$1 = null;
                        c100812.L$2 = null;
                        c100812.label = 2;
                        if ($this$iterator.yield(buffer2, c100812) == obj) {
                        }
                        break;
                    }
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                case 3:
                    c10081 = this;
                    it = (Iterator) c10081.L$2;
                    buffer = (RingBuffer) c10081.L$1;
                    sequenceScope = (SequenceScope) c10081.L$0;
                    ResultKt.throwOnFailure($result);
                    buffer.removeFirst(c10081.$step);
                    while (it.hasNext()) {
                    }
                    if (c10081.$partialWindows) {
                    }
                    break;
                case 4:
                    buffer3 = (RingBuffer) this.L$1;
                    sequenceScope2 = (SequenceScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    obj2 = coroutine_suspended;
                    c100813 = this;
                    buffer3.removeFirst(c100813.$step);
                    if (buffer3.size() <= c100813.$step) {
                    }
                    break;
                case 5:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <T> Iterator<List<T>> windowedIterator(Iterator<? extends T> iterator, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        Intrinsics.checkNotNullParameter(iterator, "iterator");
        return !iterator.hasNext() ? EmptyIterator.INSTANCE : SequencesKt.iterator(new C10081(size, step, iterator, reuseBuffer, partialWindows, null));
    }
}
