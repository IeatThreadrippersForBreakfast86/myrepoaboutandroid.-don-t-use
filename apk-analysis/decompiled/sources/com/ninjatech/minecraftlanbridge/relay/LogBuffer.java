package com.ninjatech.minecraftlanbridge.relay;

import com.google.android.material.card.MaterialCardViewHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* compiled from: LogBuffer.kt */
@Metadata(m145d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\bJ\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0007R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/LogBuffer;", "", "capacity", "", "(I)V", "_lines", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "deque", "Ljava/util/concurrent/ConcurrentLinkedDeque;", "lines", "Lkotlinx/coroutines/flow/StateFlow;", "getLines", "()Lkotlinx/coroutines/flow/StateFlow;", "tsFormat", "Ljava/text/SimpleDateFormat;", "clear", "", "log", "message", "snapshot", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public final class LogBuffer {
    private final MutableStateFlow<List<String>> _lines;
    private final int capacity;
    private final ConcurrentLinkedDeque<String> deque;
    private final StateFlow<List<String>> lines;
    private final SimpleDateFormat tsFormat;

    public LogBuffer() {
        this(0, 1, null);
    }

    public LogBuffer(int capacity) {
        this.capacity = capacity;
        this.deque = new ConcurrentLinkedDeque<>();
        this._lines = StateFlowKt.MutableStateFlow(CollectionsKt.emptyList());
        this.lines = FlowKt.asStateFlow(this._lines);
        this.tsFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
    }

    public /* synthetic */ LogBuffer(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION : i);
    }

    public final StateFlow<List<String>> getLines() {
        return this.lines;
    }

    public final synchronized void log(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        String ts = this.tsFormat.format(new Date());
        String line = "[" + ts + "] " + message;
        this.deque.addLast(line);
        while (this.deque.size() > this.capacity) {
            this.deque.pollFirst();
        }
        this._lines.setValue(CollectionsKt.toList(this.deque));
    }

    public final synchronized void clear() {
        this.deque.clear();
        this._lines.setValue(CollectionsKt.emptyList());
    }

    public final List<String> snapshot() {
        return CollectionsKt.toList(this.deque);
    }
}
