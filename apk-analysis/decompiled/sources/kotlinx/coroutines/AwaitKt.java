package kotlinx.coroutines;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import org.xbill.DNS.Type;

/* compiled from: Await.kt */
@Metadata(m145d1 = {"\u0000*\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a:\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001e\u0010\u0003\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004\"\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0086@¢\u0006\u0002\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\u0004\"\u00020\nH\u0086@¢\u0006\u0002\u0010\u000b\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\fH\u0086@¢\u0006\u0002\u0010\r\u001a\u0018\u0010\u0007\u001a\u00020\b*\b\u0012\u0004\u0012\u00020\n0\fH\u0086@¢\u0006\u0002\u0010\r¨\u0006\u000e"}, m146d2 = {"awaitAll", "", "T", "deferreds", "", "Lkotlinx/coroutines/Deferred;", "([Lkotlinx/coroutines/Deferred;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "joinAll", "", "jobs", "Lkotlinx/coroutines/Job;", "([Lkotlinx/coroutines/Job;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "", "(Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class AwaitKt {

    /* compiled from: Await.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.AwaitKt", m162f = "Await.kt", m163i = {0}, m164l = {47}, m165m = "joinAll", m166n = {"$this$forEach$iv"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.AwaitKt$joinAll$1 */
    static final class C11071 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11071(Continuation<? super C11071> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AwaitKt.joinAll((Job[]) null, this);
        }
    }

    /* compiled from: Await.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.AwaitKt", m162f = "Await.kt", m163i = {}, m164l = {Type.TALINK}, m165m = "joinAll", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.AwaitKt$joinAll$3 */
    static final class C11083 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11083(Continuation<? super C11083> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AwaitKt.joinAll((Collection<? extends Job>) null, this);
        }
    }

    public static final <T> Object awaitAll(Deferred<? extends T>[] deferredArr, Continuation<? super List<? extends T>> continuation) {
        return deferredArr.length == 0 ? CollectionsKt.emptyList() : new AwaitAll(deferredArr).await(continuation);
    }

    public static final <T> Object awaitAll(Collection<? extends Deferred<? extends T>> collection, Continuation<? super List<? extends T>> continuation) {
        return collection.isEmpty() ? CollectionsKt.emptyList() : new AwaitAll((Deferred[]) collection.toArray(new Deferred[0])).await(continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x0058 -> B:19:0x0059). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object joinAll(Job[] jobArr, Continuation<? super Unit> continuation) {
        C11071 c11071;
        int length;
        int i;
        Job[] jobArr2;
        if (continuation instanceof C11071) {
            c11071 = (C11071) continuation;
            if ((c11071.label & Integer.MIN_VALUE) != 0) {
                c11071.label -= Integer.MIN_VALUE;
            } else {
                c11071 = new C11071(continuation);
            }
        }
        C11071 c110712 = c11071;
        Object $result = c110712.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c110712.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                length = jobArr.length;
                i = 0;
                jobArr2 = jobArr;
                if (i < length) {
                    Job it = jobArr2[i];
                    c110712.L$0 = jobArr2;
                    c110712.I$0 = i;
                    c110712.I$1 = length;
                    c110712.label = 1;
                    if (it.join(c110712) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    i++;
                    if (i < length) {
                        return Unit.INSTANCE;
                    }
                }
            case 1:
                length = c110712.I$1;
                i = c110712.I$0;
                jobArr2 = (Job[]) c110712.L$0;
                ResultKt.throwOnFailure($result);
                i++;
                if (i < length) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object joinAll(Collection<? extends Job> collection, Continuation<? super Unit> continuation) {
        C11083 c11083;
        Iterator it;
        if (continuation instanceof C11083) {
            c11083 = (C11083) continuation;
            if ((c11083.label & Integer.MIN_VALUE) != 0) {
                c11083.label -= Integer.MIN_VALUE;
            } else {
                c11083 = new C11083(continuation);
            }
        }
        C11083 c110832 = c11083;
        Object $result = c110832.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c110832.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                Collection<? extends Job> $this$forEach$iv = collection;
                it = $this$forEach$iv.iterator();
                break;
            case 1:
                it = (Iterator) c110832.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            Object element$iv = it.next();
            Job it2 = (Job) element$iv;
            c110832.L$0 = it;
            c110832.label = 1;
            if (it2.join(c110832) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}
