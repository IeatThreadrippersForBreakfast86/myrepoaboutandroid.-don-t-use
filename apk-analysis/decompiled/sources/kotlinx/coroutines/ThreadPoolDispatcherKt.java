package kotlinx.coroutines;

import kotlin.Metadata;

@Metadata(m145d1 = {"kotlinx/coroutines/ThreadPoolDispatcherKt__MultithreadedDispatchers_commonKt", "kotlinx/coroutines/ThreadPoolDispatcherKt__ThreadPoolDispatcherKt"}, m147k = 4, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class ThreadPoolDispatcherKt {
    public static final ExecutorCoroutineDispatcher newFixedThreadPoolContext(int nThreads, String name) {
        return ThreadPoolDispatcherKt__ThreadPoolDispatcherKt.newFixedThreadPoolContext(nThreads, name);
    }

    public static final ExecutorCoroutineDispatcher newSingleThreadContext(String name) {
        return ThreadPoolDispatcherKt__MultithreadedDispatchers_commonKt.newSingleThreadContext(name);
    }
}
