package com.ninjatech.minecraftlanbridge.relay;

import androidx.core.app.FrameMetricsAggregator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BridgeState.kt */
@Metadata(m145d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b%\b\u0086\b\u0018\u00002\u00020\u0001Bc\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0007\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u000fJ\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0005HÆ\u0003J\t\u0010#\u001a\u00020\u0007HÆ\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010%\u001a\u00020\nHÆ\u0003J\t\u0010&\u001a\u00020\u0005HÆ\u0003J\t\u0010'\u001a\u00020\u0007HÆ\u0003J\t\u0010(\u001a\u00020\u0007HÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0005HÆ\u0003Jg\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00072\b\b\u0002\u0010\r\u001a\u00020\u00072\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010+\u001a\u00020\n2\b\u0010,\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010-\u001a\u00020\u0007HÖ\u0001J\t\u0010.\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\r\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\f\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\u0018\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0011R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 ¨\u0006/"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/BridgeStatus;", "", "state", "Lcom/ninjatech/minecraftlanbridge/relay/BridgeState;", "remoteHost", "", "remotePort", "", "resolvedRemoteHost", "viaSrv", "", "localHost", "localPort", "activeConnections", "lastError", "(Lcom/ninjatech/minecraftlanbridge/relay/BridgeState;Ljava/lang/String;ILjava/lang/String;ZLjava/lang/String;IILjava/lang/String;)V", "getActiveConnections", "()I", "getLastError", "()Ljava/lang/String;", "localDisplay", "getLocalDisplay", "getLocalHost", "getLocalPort", "remoteDisplay", "getRemoteDisplay", "getRemoteHost", "getRemotePort", "getResolvedRemoteHost", "getState", "()Lcom/ninjatech/minecraftlanbridge/relay/BridgeState;", "getViaSrv", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public final /* data */ class BridgeStatus {
    private final int activeConnections;
    private final String lastError;
    private final String localHost;
    private final int localPort;
    private final String remoteHost;
    private final int remotePort;
    private final String resolvedRemoteHost;
    private final BridgeState state;
    private final boolean viaSrv;

    public BridgeStatus() {
        this(null, null, 0, null, false, null, 0, 0, null, FrameMetricsAggregator.EVERY_DURATION, null);
    }

    /* renamed from: component1, reason: from getter */
    public final BridgeState getState() {
        return this.state;
    }

    /* renamed from: component2, reason: from getter */
    public final String getRemoteHost() {
        return this.remoteHost;
    }

    /* renamed from: component3, reason: from getter */
    public final int getRemotePort() {
        return this.remotePort;
    }

    /* renamed from: component4, reason: from getter */
    public final String getResolvedRemoteHost() {
        return this.resolvedRemoteHost;
    }

    /* renamed from: component5, reason: from getter */
    public final boolean getViaSrv() {
        return this.viaSrv;
    }

    /* renamed from: component6, reason: from getter */
    public final String getLocalHost() {
        return this.localHost;
    }

    /* renamed from: component7, reason: from getter */
    public final int getLocalPort() {
        return this.localPort;
    }

    /* renamed from: component8, reason: from getter */
    public final int getActiveConnections() {
        return this.activeConnections;
    }

    /* renamed from: component9, reason: from getter */
    public final String getLastError() {
        return this.lastError;
    }

    public final BridgeStatus copy(BridgeState state, String remoteHost, int remotePort, String resolvedRemoteHost, boolean viaSrv, String localHost, int localPort, int activeConnections, String lastError) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(remoteHost, "remoteHost");
        Intrinsics.checkNotNullParameter(localHost, "localHost");
        return new BridgeStatus(state, remoteHost, remotePort, resolvedRemoteHost, viaSrv, localHost, localPort, activeConnections, lastError);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BridgeStatus)) {
            return false;
        }
        BridgeStatus bridgeStatus = (BridgeStatus) other;
        return this.state == bridgeStatus.state && Intrinsics.areEqual(this.remoteHost, bridgeStatus.remoteHost) && this.remotePort == bridgeStatus.remotePort && Intrinsics.areEqual(this.resolvedRemoteHost, bridgeStatus.resolvedRemoteHost) && this.viaSrv == bridgeStatus.viaSrv && Intrinsics.areEqual(this.localHost, bridgeStatus.localHost) && this.localPort == bridgeStatus.localPort && this.activeConnections == bridgeStatus.activeConnections && Intrinsics.areEqual(this.lastError, bridgeStatus.lastError);
    }

    public int hashCode() {
        return (((((((((((((((this.state.hashCode() * 31) + this.remoteHost.hashCode()) * 31) + Integer.hashCode(this.remotePort)) * 31) + (this.resolvedRemoteHost == null ? 0 : this.resolvedRemoteHost.hashCode())) * 31) + Boolean.hashCode(this.viaSrv)) * 31) + this.localHost.hashCode()) * 31) + Integer.hashCode(this.localPort)) * 31) + Integer.hashCode(this.activeConnections)) * 31) + (this.lastError != null ? this.lastError.hashCode() : 0);
    }

    public String toString() {
        return "BridgeStatus(state=" + this.state + ", remoteHost=" + this.remoteHost + ", remotePort=" + this.remotePort + ", resolvedRemoteHost=" + this.resolvedRemoteHost + ", viaSrv=" + this.viaSrv + ", localHost=" + this.localHost + ", localPort=" + this.localPort + ", activeConnections=" + this.activeConnections + ", lastError=" + this.lastError + ")";
    }

    public BridgeStatus(BridgeState state, String remoteHost, int remotePort, String resolvedRemoteHost, boolean viaSrv, String localHost, int localPort, int activeConnections, String lastError) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(remoteHost, "remoteHost");
        Intrinsics.checkNotNullParameter(localHost, "localHost");
        this.state = state;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.resolvedRemoteHost = resolvedRemoteHost;
        this.viaSrv = viaSrv;
        this.localHost = localHost;
        this.localPort = localPort;
        this.activeConnections = activeConnections;
        this.lastError = lastError;
    }

    public /* synthetic */ BridgeStatus(BridgeState bridgeState, String str, int i, String str2, boolean z, String str3, int i2, int i3, String str4, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? BridgeState.STOPPED : bridgeState, (i4 & 2) != 0 ? "" : str, (i4 & 4) != 0 ? 0 : i, (i4 & 8) != 0 ? null : str2, (i4 & 16) != 0 ? false : z, (i4 & 32) == 0 ? str3 : "", (i4 & 64) != 0 ? 0 : i2, (i4 & 128) == 0 ? i3 : 0, (i4 & 256) == 0 ? str4 : null);
    }

    public final BridgeState getState() {
        return this.state;
    }

    public final String getRemoteHost() {
        return this.remoteHost;
    }

    public final int getRemotePort() {
        return this.remotePort;
    }

    public final String getResolvedRemoteHost() {
        return this.resolvedRemoteHost;
    }

    public final boolean getViaSrv() {
        return this.viaSrv;
    }

    public final String getLocalHost() {
        return this.localHost;
    }

    public final int getLocalPort() {
        return this.localPort;
    }

    public final int getActiveConnections() {
        return this.activeConnections;
    }

    public final String getLastError() {
        return this.lastError;
    }

    public final String getRemoteDisplay() {
        if (this.remoteHost.length() == 0) {
            return "—";
        }
        if (this.viaSrv && this.resolvedRemoteHost != null && !Intrinsics.areEqual(this.resolvedRemoteHost, this.remoteHost)) {
            return this.remoteHost + " -> " + this.resolvedRemoteHost + ":" + this.remotePort;
        }
        return this.remoteHost + ":" + this.remotePort;
    }

    public final String getLocalDisplay() {
        StringBuilder sbAppend;
        if (this.localHost.length() == 0) {
            sbAppend = new StringBuilder().append("0.0.0.0:").append(this.localPort);
        } else {
            sbAppend = new StringBuilder().append(this.localHost).append(":").append(this.localPort);
        }
        return sbAppend.toString();
    }
}
