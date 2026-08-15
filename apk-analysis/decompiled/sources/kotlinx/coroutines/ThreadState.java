package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: Interruptible.kt */
@Metadata(m145d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0012\u0010\u0012\u001a\u00020\r2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0006\u0010\u0015\u001a\u00020\rR\t\u0010\u0005\u001a\u00020\u0006X\u0082\u0004R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, m146d2 = {"Lkotlinx/coroutines/ThreadState;", "Lkotlinx/coroutines/InternalCompletionHandler;", "job", "Lkotlinx/coroutines/Job;", "(Lkotlinx/coroutines/Job;)V", "_state", "Lkotlinx/atomicfu/AtomicInt;", "cancelHandle", "Lkotlinx/coroutines/DisposableHandle;", "targetThread", "Ljava/lang/Thread;", "kotlin.jvm.PlatformType", "clearInterrupt", "", "invalidState", "", "state", "", "invoke", "cause", "", "setup", "kotlinx-coroutines-core"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
final class ThreadState implements InternalCompletionHandler {
    private static final /* synthetic */ AtomicIntegerFieldUpdater _state$volatile$FU = AtomicIntegerFieldUpdater.newUpdater(ThreadState.class, "_state$volatile");
    private volatile /* synthetic */ int _state$volatile;
    private DisposableHandle cancelHandle;
    private final Job job;
    private final Thread targetThread = Thread.currentThread();

    private final /* synthetic */ int get_state$volatile() {
        return this._state$volatile;
    }

    private final /* synthetic */ void loop$atomicfu(Object obj, AtomicIntegerFieldUpdater atomicIntegerFieldUpdater, Function1<? super Integer, Unit> function1) {
        while (true) {
            function1.invoke(Integer.valueOf(atomicIntegerFieldUpdater.get(obj)));
        }
    }

    private final /* synthetic */ void set_state$volatile(int i) {
        this._state$volatile = i;
    }

    public ThreadState(Job job) {
        this.job = job;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:103)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void setup() {
        /*
            r6 = this;
            kotlinx.coroutines.Job r0 = r6.job
            r1 = 1
            r2 = r6
            kotlinx.coroutines.InternalCompletionHandler r2 = (kotlinx.coroutines.InternalCompletionHandler) r2
            kotlinx.coroutines.DisposableHandle r0 = kotlinx.coroutines.JobKt.invokeOnCompletion(r0, r1, r1, r2)
            r6.cancelHandle = r0
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r0 = get_state$volatile$FU()
            r1 = r6
        L11:
            int r2 = r0.get(r6)
            r3 = 0
            switch(r2) {
                case 0: goto L23;
                case 1: goto L19;
                case 2: goto L22;
                case 3: goto L22;
                default: goto L19;
            }
        L19:
            r6.invalidState(r2)
            kotlin.KotlinNothingValueException r4 = new kotlin.KotlinNothingValueException
            r4.<init>()
            throw r4
        L22:
            return
        L23:
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r4 = get_state$volatile$FU()
            r5 = 0
            boolean r4 = r4.compareAndSet(r6, r2, r5)
            if (r4 == 0) goto L2f
            return
        L2f:
            goto L11
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.ThreadState.setup():void");
    }

    public final void clearInterrupt() {
        AtomicIntegerFieldUpdater handler$atomicfu$iv = _state$volatile$FU;
        while (true) {
            int state = handler$atomicfu$iv.get(this);
            switch (state) {
                case 0:
                    if (!_state$volatile$FU.compareAndSet(this, state, 1)) {
                        break;
                    } else {
                        DisposableHandle disposableHandle = this.cancelHandle;
                        if (disposableHandle != null) {
                            disposableHandle.dispose();
                            return;
                        }
                        return;
                    }
                case 1:
                default:
                    invalidState(state);
                    throw new KotlinNothingValueException();
                case 2:
                    break;
                case 3:
                    Thread.interrupted();
                    return;
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:103)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @Override // kotlinx.coroutines.InternalCompletionHandler
    public void invoke(java.lang.Throwable r7) {
        /*
            r6 = this;
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r0 = get_state$volatile$FU()
            r1 = r6
        L5:
            int r2 = r0.get(r6)
            r3 = 0
            switch(r2) {
                case 0: goto L17;
                case 1: goto L16;
                case 2: goto L16;
                case 3: goto L16;
                default: goto Ld;
            }
        Ld:
            r6.invalidState(r2)
            kotlin.KotlinNothingValueException r4 = new kotlin.KotlinNothingValueException
            r4.<init>()
            throw r4
        L16:
            return
        L17:
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r4 = get_state$volatile$FU()
            r5 = 2
            boolean r4 = r4.compareAndSet(r6, r2, r5)
            if (r4 == 0) goto L30
            java.lang.Thread r4 = r6.targetThread
            r4.interrupt()
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r4 = get_state$volatile$FU()
            r5 = 3
            r4.set(r6, r5)
            return
        L30:
            goto L5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.ThreadState.invoke(java.lang.Throwable):void");
    }

    private final Void invalidState(int state) {
        throw new IllegalStateException(("Illegal state " + state).toString());
    }
}
