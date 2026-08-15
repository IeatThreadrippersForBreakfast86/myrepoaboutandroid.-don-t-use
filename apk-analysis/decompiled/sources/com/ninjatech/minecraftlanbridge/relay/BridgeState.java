package com.ninjatech.minecraftlanbridge.relay;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt;

/* compiled from: BridgeState.kt */
@Metadata(m145d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/BridgeState;", "", "(Ljava/lang/String;I)V", "STOPPED", "STARTING", "LISTENING", DebugCoroutineInfoImplKt.RUNNING, "CONNECTING", "ERROR", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public enum BridgeState {
    STOPPED,
    STARTING,
    LISTENING,
    RUNNING,
    CONNECTING,
    ERROR;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<BridgeState> getEntries() {
        return $ENTRIES;
    }
}
