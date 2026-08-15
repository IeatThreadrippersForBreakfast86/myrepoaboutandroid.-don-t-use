package com.ninjatech.minecraftlanbridge.config;

import android.content.Context;
import android.content.SharedPreferences;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: BridgeConfig.kt */
@Metadata(m145d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 #2\u00020\u0001:\u0001#B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J9\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00112\b\u0010\u0017\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u001c\u001a\u00020\u0006¢\u0006\u0002\u0010\"R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R$\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u000f\u0010\t\"\u0004\b\u0010\u0010\u000bR$\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00118F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R(\u0010\u0017\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR$\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u001d\u0010\t\"\u0004\b\u001e\u0010\u000b¨\u0006$"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/config/BridgeConfig;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "localPort", "getLocalPort", "()I", "setLocalPort", "(I)V", "prefs", "Landroid/content/SharedPreferences;", "proxyPort", "getProxyPort", "setProxyPort", "", "remoteHost", "getRemoteHost", "()Ljava/lang/String;", "setRemoteHost", "(Ljava/lang/String;)V", "remotePort", "getRemotePort", "()Ljava/lang/Integer;", "setRemotePort", "(Ljava/lang/Integer;)V", "udpRelayPort", "getUdpRelayPort", "setUdpRelayPort", "save", "", "host", "(Ljava/lang/String;Ljava/lang/Integer;III)V", "Companion", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes4.dex */
public final class BridgeConfig {
    public static final int DEFAULT_LOCAL_PORT = 25565;
    public static final int DEFAULT_PROXY_PORT = 25580;
    public static final String DEFAULT_REMOTE_HOST = "play.example.com";
    public static final int DEFAULT_UDP_RELAY_PORT = 25581;
    private static final String KEY_LOCAL_PORT = "local_port";
    private static final String KEY_MIGRATED_V2 = "migrated_v2_optional_port";
    private static final String KEY_PROXY_PORT = "proxy_port";
    private static final String KEY_REMOTE_HOST = "remote_host";
    private static final String KEY_REMOTE_PORT = "remote_port";
    private static final String KEY_UDP_RELAY_PORT = "udp_relay_port";
    private static final int PORT_UNSET = -1;
    private static final String PREFS_NAME = "minecraft_lan_bridge_config";
    private final SharedPreferences prefs;

    public BridgeConfig(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        this.prefs = sharedPreferences;
        if (this.prefs.getBoolean(KEY_MIGRATED_V2, false)) {
            return;
        }
        int oldPort = this.prefs.getInt("remote_port", -1);
        if (oldPort == 25565) {
            this.prefs.edit().putInt("remote_port", -1).apply();
        }
        this.prefs.edit().putBoolean(KEY_MIGRATED_V2, true).apply();
    }

    public final String getRemoteHost() {
        String string = this.prefs.getString("remote_host", DEFAULT_REMOTE_HOST);
        return string == null ? DEFAULT_REMOTE_HOST : string;
    }

    public final void setRemoteHost(String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.prefs.edit().putString("remote_host", StringsKt.trim((CharSequence) value).toString()).apply();
    }

    public final Integer getRemotePort() {
        int v = this.prefs.getInt("remote_port", -1);
        if (v == -1) {
            return null;
        }
        return Integer.valueOf(v);
    }

    public final void setRemotePort(Integer value) {
        this.prefs.edit().putInt("remote_port", value != null ? value.intValue() : -1).apply();
    }

    public final int getLocalPort() {
        return this.prefs.getInt("local_port", 25565);
    }

    public final void setLocalPort(int value) {
        this.prefs.edit().putInt("local_port", value).apply();
    }

    public final int getProxyPort() {
        return this.prefs.getInt("proxy_port", DEFAULT_PROXY_PORT);
    }

    public final void setProxyPort(int value) {
        this.prefs.edit().putInt("proxy_port", value).apply();
    }

    public final int getUdpRelayPort() {
        return this.prefs.getInt("udp_relay_port", DEFAULT_UDP_RELAY_PORT);
    }

    public final void setUdpRelayPort(int value) {
        this.prefs.edit().putInt("udp_relay_port", value).apply();
    }

    public static /* synthetic */ void save$default(BridgeConfig bridgeConfig, String str, Integer num, int i, int i2, int i3, int i4, Object obj) {
        int proxyPort;
        int udpRelayPort;
        if ((i4 & 8) == 0) {
            proxyPort = i2;
        } else {
            proxyPort = bridgeConfig.getProxyPort();
        }
        if ((i4 & 16) == 0) {
            udpRelayPort = i3;
        } else {
            udpRelayPort = bridgeConfig.getUdpRelayPort();
        }
        bridgeConfig.save(str, num, i, proxyPort, udpRelayPort);
    }

    public final void save(String host, Integer remotePort, int localPort, int proxyPort, int udpRelayPort) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.prefs.edit().putString("remote_host", StringsKt.trim((CharSequence) host).toString()).putInt("remote_port", remotePort != null ? remotePort.intValue() : -1).putInt("local_port", localPort).putInt("proxy_port", proxyPort).putInt("udp_relay_port", udpRelayPort).apply();
    }
}
