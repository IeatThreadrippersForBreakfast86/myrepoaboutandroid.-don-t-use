package com.ninjatech.lanbridge.proxy;

import com.ninjatech.lanbridge.config.LanBridgeConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Auto-discovers a running Minecraft LAN Bridge host on the local network by
 * probing the {@code /__lanbridge_info} HTTP endpoint that the Android app's
 * {@code HttpProxyServer} exposes on its proxy port.
 *
 * <p>This is an optional convenience: if the user has already entered the host IP
 * in {@code config/lanbridge.json} (or via the ModMenu screen) discovery is
 * skipped. Discovery only ever probes IPs on the same LAN subnet, so it never
 * creates a new IP address or routes anything off the local network.</p>
 */
public final class LanBridgeDiscovery {

    private LanBridgeDiscovery() {}

    public static final class BridgeInfo {
        public final String lanHost;
        public final int proxyPort;
        public final int gameRelayPort;
        public final int udpRelayPort;
        public BridgeInfo(String lanHost, int proxyPort, int gameRelayPort, int udpRelayPort) {
            this.lanHost = lanHost;
            this.proxyPort = proxyPort;
            this.gameRelayPort = gameRelayPort;
            this.udpRelayPort = udpRelayPort;
        }
    }

    /** Synchronous discovery with a hard overall deadline. Returns null if nothing found. */
    public static BridgeInfo discover() {
        return discover(8, TimeUnit.SECONDS);
    }

    public static BridgeInfo discover(long timeout, TimeUnit unit) {
        LanBridgeConfig cfg = LanBridgeConfig.get();
        int port = cfg.getProxyPort();

        // 1. Try the configured host first (fast path).
        if (cfg.hasHost()) {
            BridgeInfo info = probe(cfg.getBridgeHostIp(), port);
            if (info != null) return info;
        }

        // 2. Otherwise scan local subnets /24 for any responsive bridge.
        List<String> candidates = collectCandidateIps();
        ExecutorService pool = Executors.newFixedThreadPool(Math.min(16, Math.max(2, candidates.size())));
        AtomicReference<BridgeInfo> winner = new AtomicReference<>();
        List<Future<?>> futures = new ArrayList<>();
        try {
            for (String ip : candidates) {
                futures.add(pool.submit(() -> {
                    if (winner.get() != null) return;
                    BridgeInfo info = probe(ip, port);
                    if (info != null) winner.compareAndSet(null, info);
                }));
            }
            pool.shutdown();
            try { pool.awaitTermination(timeout, unit); } catch (InterruptedException ignored) {}
        } finally {
            pool.shutdownNow();
        }
        return winner.get();
    }

    /** Probe a single IP:port for the discovery JSON. */
    private static BridgeInfo probe(String ip, int port) {
        // Quick TCP reachability check first.
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(ip, port), 600);
        } catch (Exception e) {
            return null;
        }
        // Fetch the discovery document.
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://" + ip + ":" + port + LanBridgeConfig.DISCOVERY_PATH);
            conn = (HttpURLConnection) url.openConnection(java.net.Proxy.NO_PROXY);
            conn.setConnectTimeout(800);
            conn.setReadTimeout(800);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code != 200) return null;
            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) sb.append(line).append('\n');
            }
            return parse(sb.toString(), ip, port);
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /** Parse the tiny discovery JSON without external libraries. */
    private static BridgeInfo parse(String json, String discoveredIp, int discoveredPort) {
        if (json == null || json.isEmpty()) return null;
        String lanHost = readString(json, "lanHost", discoveredIp);
        int proxyPort = readInt(json, "proxyPort", discoveredPort);
        int gameRelayPort = readInt(json, "gameRelayPort", LanBridgeConfig.DEFAULT_GAME_RELAY_PORT);
        int udpRelayPort = readInt(json, "udpRelayPort", LanBridgeConfig.DEFAULT_UDP_RELAY_PORT);
        if (proxyPort <= 0 || proxyPort > 65535) proxyPort = discoveredPort;
        if (udpRelayPort < 0 || udpRelayPort > 65535) udpRelayPort = 0; // 0 = disabled
        return new BridgeInfo(lanHost, proxyPort, gameRelayPort, udpRelayPort);
    }

    /** Collect candidate LAN IPs from the machine's own interfaces (same /24 subnets). */
    private static List<String> collectCandidateIps() {
        List<String> out = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface nif = ifaces.nextElement();
                if (!nif.isUp() || nif.isLoopback() || nif.isVirtual()) continue;
                Enumeration<InetAddress> addrs = nif.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress a = addrs.nextElement();
                    if (a.isLoopbackAddress()) continue;
                    byte[] bytes = a.getAddress();
                    if (bytes.length != 4) continue; // IPv4 only for /24 scan
                    // Enumerate .1 .. .254 on the same /24.
                    for (int i = 1; i <= 254; i++) {
                        byte[] candidate = new byte[4];
                        System.arraycopy(bytes, 0, candidate, 0, 3);
                        candidate[3] = (byte) i;
                        // Skip our own address.
                        if (candidate[0] == bytes[0] && candidate[1] == bytes[1]
                                && candidate[2] == bytes[2] && candidate[3] == bytes[3]) continue;
                        out.add(InetAddress.getByAddress(candidate).getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[LAN Bridge] Discovery enumeration failed: " + e.getMessage());
        }
        return out;
    }

    // ---- tiny JSON readers (same style as LanBridgeConfig) ----
    private static String readString(String json, String key, String def) {
        String raw = readRaw(json, key);
        if (raw == null) return def;
        raw = raw.trim();
        if (raw.length() >= 2 && raw.startsWith("\"") && raw.endsWith("\"")) {
            return raw.substring(1, raw.length() - 1);
        }
        return def;
    }
    private static int readInt(String json, String key, int def) {
        String raw = readRaw(json, key);
        if (raw == null) return def;
        try { return Integer.parseInt(raw.trim()); } catch (NumberFormatException e) { return def; }
    }
    private static String readRaw(String json, String key) {
        String needle = "\"" + key + "\"";
        int idx = json.indexOf(needle);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + needle.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start >= json.length()) return null;
        char first = json.charAt(start);
        if (first == '"') {
            int end = start + 1;
            while (end < json.length()) {
                char c = json.charAt(end);
                if (c == '\\') { end += 2; continue; }
                if (c == '"') { end++; break; }
                end++;
            }
            return json.substring(start, Math.min(end, json.length()));
        } else {
            int end = start;
            while (end < json.length()) {
                char c = json.charAt(end);
                if (c == ',' || c == '}' || c == '\n' || c == '\r') break;
                end++;
            }
            return json.substring(start, end).trim();
        }
    }
}
