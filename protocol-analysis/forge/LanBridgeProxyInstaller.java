package com.ninjatech.lanbridge.proxy;

import com.ninjatech.lanbridge.config.LanBridgeConfig;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Routes <strong>every</strong> network request the Minecraft client makes through
 * the Android host device's CONNECT-tunnel proxy over the existing LAN, without
 * giving the PC a new IP address.
 *
 * <h2>Root cause of &ldquo;authentication servers are currently
 * unreachable&rdquo;</h2>
 * <p>Authlib (4.x, used by Minecraft 1.20.1 / Forge) does <strong>not</strong> use
 * {@link java.net.ProxySelector} for auth traffic. Its
 * {@code HttpAuthenticationService} stores a single {@link Proxy} in a
 * {@code private final} field and calls {@code URL.openConnection(thatProxy)} for
 * every request. When you pass an explicit {@code Proxy} to
 * {@code URL.openConnection(Proxy)} the JVM skips {@code ProxySelector} entirely.
 * {@code Minecraft}&apos;s constructor (Forge: {@code net.minecraft.client.Minecraft})
 * does:</p>
 * <pre>
 *   this.proxy = gameConfig.user.proxy;          // Proxy.NO_PROXY by default
 *   this.authenticationService = new YggdrasilAuthenticationService(this.proxy);
 *   this.minecraftSessionService = this.authenticationService.createMinecraftSessionService();
 *   this.userApiService = this.createUserApiService(this.authenticationService, gameConfig);
 * </pre>
 * <p>so the {@code Proxy} is baked, {@code final}, into every session/profile
 * service. This is why a {@code ProxySelector}-only approach cannot redirect auth
 * traffic &mdash; the selector is simply never consulted for those calls.</p>
 *
 * <h2>How we actually fix it</h2>
 * <ol>
 *   <li><b>At-launch mixin.</b> {@code MinecraftMixin} uses {@code @ModifyArg} to
 *       rewrite the {@code Proxy} passed to {@code YggdrasilAuthenticationService}
 *       &apos;s constructor inside {@code Minecraft.&lt;init&gt;}. When a host is
 *       known at launch (from {@code config/lanbridge.json} or auto-discovery
 *       completed during early mod init), the auth service is constructed with our
 *       bridge proxy from the very start.</li>
 *   <li><b>Post-launch reflective patch.</b> {@link #patchAuthServices()}
 *       overwrites the {@code final proxy} field inside the already-built
 *       {@code YggdrasilAuthenticationService} / {@code MinecraftSessionService} /
 *       {@code UserApiService} (and {@code Minecraft.proxy}) when the user
 *       configures a host after launch. This makes a late config take effect
 *       without restarting the game.</li>
 *   <li><b>Selector + system properties (everything else).</b> The
 *       {@link BridgeProxySelector} and {@code https.proxyHost}/
 *       {@code https.proxyPort} system properties cover <em>every other</em>
 *       HTTPS call the client makes &mdash; skin/texture/cape downloads, telemetry,
 *       Realms, the marketplace, and third-party mod HTTPS traffic such as the
 *       Essential mod&rsquo;s API calls. With {@code authOnly=false} (the new
 *       default) <em>all</em> HTTPS traffic is routed through the bridge so that a
 *       PC with no direct internet can still reach every service the game and its
 *       mods talk to.</li>
 * </ol>
 *
 * <h2>The &ldquo;no new IP&rdquo; guarantee</h2>
 * <p>The proxy address is the host device's existing LAN IP (the same device that
 * forwards the game server). No tethering, hotspot or additional IP is created: the
 * PC opens a normal TCP socket to {@code <bridgeHostIp>:<proxyPort>} on the LAN,
 * sends an HTTP {@code CONNECT host:443} line, and the Android app tunnels the TLS
 * bytes to the real internet host. The PC never obtains a new IP address. The raw
 * TCP game connection to the relay port is a separate socket and is never proxied
 * (it is not HTTPS).</p>
 */
public final class LanBridgeProxyInstaller {

    private static final AtomicReference<Proxy> ACTIVE_PROXY = new AtomicReference<>();
    private static volatile boolean installed = false;
    private static volatile boolean earlyInstalled = false;
    private static ProxySelector previousSelector;
    private static BridgeProxySelector liveSelector;

    /**
     * The address of the Android host's UDP relay ({@code bridgeHost:udpRelayPort}).
     * Set by {@link #install(String)} and read by the {@code DatagramSocketMixin}
     * on every {@code send()} / {@code receive()}. When {@code null} the mixin
     * becomes a no-op (no relay configured).
     */
    private static final AtomicReference<InetSocketAddress> UDP_RELAY_ADDR = new AtomicReference<>();

    private LanBridgeProxyInstaller() {}

    /**
     * Hosts whose HTTPS traffic is routed through the bridge when
     * {@code authOnly} is {@code true}. These cover the full Minecraft Java Edition
     * authentication and session surface area. When {@code authOnly} is
     * {@code false} (the default) this set is irrelevant &mdash; everything is
     * routed.
     */
    private static final Set<String> AUTH_HOSTS = new HashSet<>();
    static {
        // Microsoft / Xbox Live OAuth
        AUTH_HOSTS.add("login.live.com");
        AUTH_HOSTS.add("login.microsoftonline.com");
        AUTH_HOSTS.add("user.auth.xboxlive.com");
        AUTH_HOSTS.add("xsts.auth.xboxlive.com");
        AUTH_HOSTS.add("title.mgt.xboxlive.com");
        AUTH_HOSTS.add("device.auth.xboxlive.com");
        AUTH_HOSTS.add("sis.xboxlive.com");
        // Mojang / Minecraft services
        AUTH_HOSTS.add("api.minecraftservices.com");
        AUTH_HOSTS.add("sessionserver.mojang.com");
        AUTH_HOSTS.add("minecraft.net");
        AUTH_HOSTS.add("www.minecraft.net");
        // Skin / texture / asset hosts used during session/profile lookups
        AUTH_HOSTS.add("textures.minecraft.net");
        AUTH_HOSTS.add("assets.minecraft.net");
        AUTH_HOSTS.add("mcassets.cloud");
        // Session/launchermeta endpoints
        AUTH_HOSTS.add("launchermeta.mojang.com");
        AUTH_HOSTS.add("launcher.mojang.com");
        AUTH_HOSTS.add("piston-meta.mojang.com");
        // Additional endpoints seen in modern Minecraft auth flows
        AUTH_HOSTS.add("api.mojang.com");
        AUTH_HOSTS.add("mojang.com");
        AUTH_HOSTS.add("account.mojang.com");
        AUTH_HOSTS.add("premium.minecraft.net");
    }

    /**
     * Hosts / host-suffixes whose <strong>UDP</strong> traffic should be routed
     * through the LAN Bridge UDP relay when {@code routeEssential} is enabled.
     * These cover the Essential mod&rsquo;s custom ICE/STUN/TURN/QUIC stack, which
     * uses raw {@link java.net.DatagramSocket} for all of its P2P networking.
     *
     * <p>The Essential mod&rsquo;s known infrastructure domains are:
     * <ul>
     *   <li>{@code *.stun.essential.gg} &mdash; STUN servers (UDP 3478)</li>
     *   <li>{@code *.turn.essential.gg} &mdash; TURN relay servers (UDP 3478)</li>
     *   <li>{@code essential.gg} and subdomains &mdash; general infrastructure</li>
     * </ul>
     * In addition, we route <strong>all</strong> non-local UDP traffic when
     * {@code routeEssential} is on, because the Essential mod also performs
     * direct peer-to-peer QUIC connections to other players&rsquo; public IPs
     * (which are not on any known domain). The domain list is a fast-path
     * optimisation; the real decision is made in {@link #shouldRouteUdp}.</p>
     */
    private static final Set<String> ESSENTIAL_UDP_SUFFIXES = new HashSet<>();
    static {
        ESSENTIAL_UDP_SUFFIXES.add("essential.gg");
        ESSENTIAL_UDP_SUFFIXES.add("stun.essential.gg");
        ESSENTIAL_UDP_SUFFIXES.add("turn.essential.gg");
    }

    /** @return the currently active bridge {@link Proxy}, or {@code null} if disabled. */
    public static Proxy getActiveProxy() {
        return ACTIVE_PROXY.get();
    }

    /** @return true when the installer has applied a bridge proxy. */
    public static boolean isInstalled() { return installed; }

    // ===================================================================
    //  UDP relay infrastructure (used by DatagramSocketMixin)
    // ===================================================================

    /**
     * @return the {@link InetSocketAddress} of the Android host&rsquo;s UDP relay
     *         ({@code bridgeHost:udpRelayPort}), or {@code null} if the UDP relay
     *         is not configured / disabled. The {@code DatagramSocketMixin} calls
     *         this on every {@code send()} and {@code receive()}; when it returns
     *         {@code null} the mixin is a complete no-op.
     */
    public static InetSocketAddress getUdpRelayAddress() {
        return UDP_RELAY_ADDR.get();
    }

    /**
     * Check whether a given destination address <em>is</em> the UDP relay itself.
     * Used by the mixin to avoid an infinite send-loop (we must not re-frame a
     * packet that is already addressed to the relay).
     *
     * @param addr the destination {@link InetAddress} of the datagram
     * @param port the destination port
     * @return {@code true} if this is the relay address (so the mixin should let
     *         the original {@code send()} proceed unmodified)
     */
    public static boolean isUdpRelayAddress(InetAddress addr, int port) {
        InetSocketAddress relay = UDP_RELAY_ADDR.get();
        if (relay == null || addr == null) return false;
        if (relay.getPort() != port) return false;
        InetAddress relayAddr = relay.getAddress();
        if (relayAddr == null) return false;
        return relayAddr.equals(addr);
    }

    /**
     * Check whether an address is a local / loopback address. The mixin uses this
     * to avoid routing localhost UDP traffic (e.g. DNS to a local resolver, or
     * internal IPC) through the relay.
     *
     * @param addr the address to test
     * @return {@code true} if the address is loopback, link-local, or any of the
     *         machine&rsquo;s own interface addresses
     */
    public static boolean isLocalAddress(InetAddress addr) {
        if (addr == null) return false;
        try {
            if (addr.isLoopbackAddress()) return true;
            if (addr.isLinkLocalAddress()) return true;
            if (addr.isAnyLocalAddress()) return true;
        } catch (Throwable ignored) {}
        // Also treat the relay host's own LAN IP as "local" so we never try to
        // relay traffic that is already going to the bridge host.
        InetSocketAddress relay = UDP_RELAY_ADDR.get();
        if (relay != null && relay.getAddress() != null && relay.getAddress().equals(addr)) {
            return true;
        }
        return false;
    }

    /**
     * Decide whether a given UDP destination should be routed through the relay.
     *
     * <p>When {@code routeEssential} is enabled (the default) we route
     * <strong>all</strong> non-local, non-relay UDP traffic through the relay.
     * This is the safe and complete choice: the Essential mod&rsquo;s P2P stack
     * connects to STUN servers, TURN relays, and &mdash; critically &mdash;
     * directly to other players&rsquo; public IP addresses (which are not on any
     * known domain). The only way to catch all of that is to route everything
     * that is not obviously local.</p>
     *
     * <p>When {@code routeEssential} is disabled, we return {@code false} and the
     * UDP relay is effectively turned off (the mixin lets all sends proceed
     * directly). This gives users a way to disable the UDP tunnel independently
     * of the HTTPS/TCP proxy.</p>
     *
     * @param dest the real destination of the datagram (before any rewriting)
     * @return {@code true} if the datagram should be framed and sent to the relay
     */
    public static boolean shouldRouteUdp(InetSocketAddress dest) {
        if (dest == null) return false;
        // Must have a relay configured.
        if (UDP_RELAY_ADDR.get() == null) return false;
        // Must have the feature enabled.
        if (!LanBridgeConfig.get().isRouteEssential()) return false;
        InetAddress addr = dest.getAddress();
        if (addr == null) return false;
        // Never route local/loopback traffic.
        if (isLocalAddress(addr)) return false;
        // Never route traffic already going to the relay.
        if (isUdpRelayAddress(addr, dest.getPort())) return false;
        // Route everything else. The Essential mod's P2P connections go to
        // arbitrary public IPs (other players) and to *.essential.gg STUN/TURN
        // servers; there is no finite host list we can match against.
        return true;
    }

    /**
     * Patch the proxy inside Minecraft's already-built auth services so that a host
     * configured <em>after</em> launch (e.g. via the in-game config screen) still
     * takes effect without restarting the game.
     *
     * <p>Forge 1.20.1 field names (official mappings, runtime-stable): the client
     * class is {@code net.minecraft.client.Minecraft}; it has
     * {@code private final java.net.Proxy proxy},
     * {@code private final YggdrasilAuthenticationService authenticationService},
     * {@code private final MinecraftSessionService minecraftSessionService} and
     * {@code private final UserApiService userApiService}. Authlib&rsquo;s
     * {@code HttpAuthenticationService} (the superclass of the Yggdrasil services)
     * has {@code private final java.net.Proxy proxy}. We locate the
     * {@code Proxy} field by <em>type</em> so this is robust against any naming
     * differences.</p>
     *
     * <p>This is best-effort: every step is wrapped so a failure never propagates.
     * Returns {@code true} if at least one field was rewritten.</p>
     */
    public static boolean patchAuthServices() {
        Proxy bridge = ACTIVE_PROXY.get();
        if (bridge == null) {
            System.out.println("[LAN Bridge] patchAuthServices: no bridge proxy active, nothing to patch.");
            return false;
        }
        boolean any = false;
        try {
            Class<?> mcClass = tryClass("net.minecraft.client.Minecraft");
            if (mcClass == null) {
                System.err.println("[LAN Bridge] patchAuthServices: Minecraft class not found.");
                return false;
            }
            Object client = null;
            try {
                java.lang.reflect.Method getInstance = mcClass.getMethod("getInstance");
                client = getInstance.invoke(null);
            } catch (NoSuchMethodException ignored) {}
            if (client == null) {
                System.err.println("[LAN Bridge] patchAuthServices: Minecraft.getInstance() returned null (not yet constructed?).");
                return false;
            }

            // Patch Minecraft.proxy (consulted by some non-auth code paths too).
            any |= setProxyFieldByType(mcClass, client, bridge, "proxy");

            // authenticationService -> its proxy field (HttpAuthenticationService.proxy).
            Object authService = getFieldValue(client, "authenticationService");
            if (authService != null) {
                for (Class<?> c = authService.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    any |= setProxyFieldByType(c, authService, bridge, "proxy");
                }
            }

            // minecraftSessionService (Forge name) — also try sessionService for safety.
            Object sessionService = getFieldValue(client, "minecraftSessionService");
            if (sessionService == null) sessionService = getFieldValue(client, "sessionService");
            if (sessionService != null) {
                for (Class<?> c = sessionService.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    any |= setProxyFieldByType(c, sessionService, bridge, "proxy");
                }
            }

            // userApiService.
            Object userApiService = getFieldValue(client, "userApiService");
            if (userApiService != null) {
                for (Class<?> c = userApiService.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    any |= setProxyFieldByType(c, userApiService, bridge, "proxy");
                }
            }
        } catch (Throwable t) {
            System.err.println("[LAN Bridge] patchAuthServices failed: " + t);
        }
        if (any) {
            System.out.println("[LAN Bridge] patchAuthServices: successfully rewrote auth proxy -> " + bridge);
        } else {
            System.err.println("[LAN Bridge] patchAuthServices: no proxy fields were rewritten.");
        }
        return any;
    }

    private static Class<?> tryClass(String... names) {
        for (String n : names) {
            try { return Class.forName(n); } catch (ClassNotFoundException ignored) {}
        }
        return null;
    }

    private static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null) return null;
        for (Class<?> c = obj.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f.get(obj);
            } catch (NoSuchFieldException ignored) {
            } catch (Throwable t) {
                System.err.println("[LAN Bridge] getFieldValue(" + fieldName + ") on " + c.getName() + ": " + t);
                return null;
            }
        }
        return null;
    }

    private static boolean setProxyFieldByType(Class<?> clazz, Object obj, Proxy newProxy, String hintName) {
        if (clazz == null || obj == null) return false;
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(hintName);
                if (f.getType() == Proxy.class) {
                    f.setAccessible(true);
                    f.set(obj, newProxy);
                    return true;
                }
            } catch (NoSuchFieldException ignored) {
            } catch (Throwable t) {
                System.err.println("[LAN Bridge] setProxyField hint " + c.getName() + "." + hintName + ": " + t);
            }
        }
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getType() == Proxy.class) {
                    try {
                        f.setAccessible(true);
                        f.set(obj, newProxy);
                        return true;
                    } catch (Throwable t) {
                        System.err.println("[LAN Bridge] setProxyField type fallback " + c.getName() + "." + f.getName() + ": " + t);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Install the mutable {@link BridgeProxySelector} as the JVM default
     * <em>immediately</em>, even if no host is configured yet. Called as early as
     * possible (from the mod constructor) so Minecraft's cached HTTP clients pick
     * up our selector from the start. The selector routes direct until a host is
     * set; when {@link #install(String)} is called later the same selector object
     * is updated in-place.
     */
    public static synchronized void installEarly() {
        if (earlyInstalled) {
            return;
        }
        previousSelector = ProxySelector.getDefault();
        liveSelector = new BridgeProxySelector(previousSelector);
        ProxySelector.setDefault(liveSelector);
        earlyInstalled = true;
        installed = true;
        System.out.println("[LAN Bridge] Early proxy selector installed (no host yet; direct until configured).");
    }

    /**
     * Install or refresh the bridge proxy based on the current config. If no host
     * is configured and no {@code discoveredHost} is supplied, the installer
     * becomes a no-op (selector stays installed but routes direct).
     */
    public static synchronized void install(String discoveredHost) {
        if (!earlyInstalled) {
            installEarly();
        }

        LanBridgeConfig cfg = LanBridgeConfig.get();
        String host = cfg.hasHost() ? cfg.getBridgeHostIp() : (discoveredHost == null ? "" : discoveredHost);

        if (host == null || host.isEmpty()) {
            uninstall();
            return;
        }

        InetSocketAddress addr = new InetSocketAddress(host, cfg.getProxyPort());
        if (addr.isUnresolved()) {
            System.err.println("[LAN Bridge] Could not resolve bridge host '" + host + "'; proxy not installed.");
            return;
        }

        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        ACTIVE_PROXY.set(proxy);

        // Configure the UDP relay address for the DatagramSocketMixin.
        // When udpRelayPort > 0 the mixin will frame and redirect UDP traffic
        // (Essential's STUN/TURN/P2P) to this address; when 0 the UDP tunnel
        // is disabled and the mixin is a no-op.
        int udpPort = cfg.getUdpRelayPort();
        if (udpPort > 0) {
            UDP_RELAY_ADDR.set(new InetSocketAddress(host, udpPort));
            System.out.println("[LAN Bridge] UDP relay address -> " + host + ":" + udpPort
                    + " (routeEssential=" + cfg.isRouteEssential() + ")");
        } else {
            UDP_RELAY_ADDR.set(null);
            System.out.println("[LAN Bridge] UDP relay disabled (udpRelayPort=0).");
        }

        // System properties cover HttpURLConnection + java.net.http.HttpClient +
        // OkHttp's default proxy resolution (Essential mod, etc.).
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", Integer.toString(cfg.getProxyPort()));
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", Integer.toString(cfg.getProxyPort()));
        // Never proxy localhost / the LAN bridge host itself (avoids loops).
        System.setProperty("https.nonProxyHosts", "localhost|127.*|[::1]");
        System.setProperty("http.nonProxyHosts", "localhost|127.*|[::1]");

        // Update the live selector IN PLACE so any client that already holds a
        // reference to it sees the change.
        if (liveSelector != null) {
            liveSelector.update(proxy, cfg.isAuthOnly());
        }

        installed = true;

        // Patch the already-built auth services (authlib's explicit-Proxy path).
        patchAuthServices();

        System.out.println("[LAN Bridge] Proxy installed -> " + host + ":" + cfg.getProxyPort()
                + " (authOnly=" + cfg.isAuthOnly() + ")");
    }

    /** Remove the bridge proxy and restore direct routing. */
    public static synchronized void uninstall() {
        ACTIVE_PROXY.set(null);
        UDP_RELAY_ADDR.set(null);
        if (liveSelector != null) {
            liveSelector.update(null, true);
        }
        installed = earlyInstalled;
        System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("https.nonProxyHosts");
        System.clearProperty("http.nonProxyHosts");
        System.out.println("[LAN Bridge] Proxy + UDP relay removed (selector now direct).");
    }

    /**
     * {@link ProxySelector} that returns the bridge proxy for the configured host
     * set (or for everything when {@code authOnly} is false) and delegates to the
     * original selector otherwise. The proxy and authOnly settings are
     * {@code volatile} and updated in-place via {@link #update(Proxy, boolean)}.
     */
    private static final class BridgeProxySelector extends ProxySelector {

        private final ProxySelector delegate;
        private volatile Proxy bridge;       // null = route direct
        private volatile boolean authOnly;

        BridgeProxySelector(ProxySelector delegate) {
            this.delegate = delegate;
            this.bridge = null;
            this.authOnly = false; // default: route everything (overridden by update())
        }

        void update(Proxy bridge, boolean authOnly) {
            this.bridge = bridge;
            this.authOnly = authOnly;
        }

        @Override
        public List<Proxy> select(URI uri) {
            Proxy b = bridge;  // snapshot
            if (b == null) {
                return delegate == null ? Collections.singletonList(Proxy.NO_PROXY) : delegate.select(uri);
            }
            if (uri == null) {
                return Collections.singletonList(b);
            }
            String scheme = uri.getScheme();
            if (scheme == null) scheme = "";
            String host = uri.getHost();
            if (!scheme.equalsIgnoreCase("https") && !scheme.equalsIgnoreCase("http")) {
                return delegate == null ? Collections.singletonList(Proxy.NO_PROXY) : delegate.select(uri);
            }
            if (host != null && isLocalhost(host)) {
                return Collections.singletonList(Proxy.NO_PROXY);
            }
            boolean route;
            if (authOnly) {
                route = host != null && shouldRoute(host);
            } else {
                route = true; // route ALL HTTPS through the bridge
            }
            System.out.println("[LAN Bridge] ProxySelector.select(" + uri + ") -> " + (route ? b : "DIRECT"));
            if (route) {
                return Collections.singletonList(b);
            }
            return delegate == null ? Collections.singletonList(Proxy.NO_PROXY) : delegate.select(uri);
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            if (delegate != null) {
                try { delegate.connectFailed(uri, sa, ioe); } catch (Exception ignored) {}
            }
        }

        private static boolean shouldRoute(String host) {
            if (host == null) return false;
            String h = host.toLowerCase();
            if (AUTH_HOSTS.contains(h)) return true;
            for (String suffix : AUTH_HOSTS) {
                if (h.endsWith("." + suffix)) return true;
            }
            return false;
        }

        private static boolean isLocalhost(String host) {
            if (host == null) return false;
            String h = host.toLowerCase();
            return h.equals("localhost") || h.equals("127.0.0.1") || h.equals("::1") || h.startsWith("127.");
        }
    }
}
