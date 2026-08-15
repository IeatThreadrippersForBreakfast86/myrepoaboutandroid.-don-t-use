package org.xbill.DNS.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/* loaded from: classes8.dex */
public class ResolvConfResolverConfigProvider extends BaseResolverConfigProvider {
    private int ndots = 1;

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() throws IOException {
        reset();
        if (!tryParseResolveConf("/etc/resolv.conf")) {
            tryParseResolveConf("sys:/etc/resolv.cfg");
        }
    }

    private boolean tryParseResolveConf(String path) throws IOException {
        Path p = Paths.get(path, new String[0]);
        if (Files.exists(p, new LinkOption[0])) {
            try {
                InputStream in = Files.newInputStream(p, new OpenOption[0]);
                try {
                    parseResolvConf(in);
                    if (in != null) {
                        in.close();
                        return true;
                    }
                    return true;
                } finally {
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void parseResolvConf(InputStream in) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        try {
            BufferedReader br = new BufferedReader(isr);
            while (true) {
                try {
                    String line = br.readLine();
                    if (line != null) {
                        StringTokenizer st = new StringTokenizer(line);
                        if (st.hasMoreTokens()) {
                            switch (st.nextToken()) {
                                case "nameserver":
                                    addNameserver(new InetSocketAddress(st.nextToken(), 53));
                                    break;
                                case "domain":
                                    this.searchlist.clear();
                                    if (!st.hasMoreTokens()) {
                                        break;
                                    } else {
                                        addSearchPath(st.nextToken());
                                        break;
                                    }
                                case "search":
                                    this.searchlist.clear();
                                    while (st.hasMoreTokens()) {
                                        addSearchPath(st.nextToken());
                                    }
                                    break;
                                case "options":
                                    while (st.hasMoreTokens()) {
                                        String token = st.nextToken();
                                        if (token.startsWith("ndots:")) {
                                            this.ndots = parseNdots(token.substring(6));
                                        }
                                    }
                                    break;
                            }
                        }
                    } else {
                        br.close();
                        isr.close();
                        String localdomain = System.getenv("LOCALDOMAIN");
                        if (localdomain != null && !localdomain.isEmpty()) {
                            this.searchlist.clear();
                            parseSearchPathList(localdomain, " ");
                        }
                        String resOptions = System.getenv("RES_OPTIONS");
                        if (resOptions != null && !resOptions.isEmpty()) {
                            StringTokenizer st2 = new StringTokenizer(resOptions, " ");
                            while (st2.hasMoreTokens()) {
                                String token2 = st2.nextToken();
                                if (token2.startsWith("ndots:")) {
                                    this.ndots = parseNdots(token2.substring(6));
                                }
                            }
                            return;
                        }
                        return;
                    }
                } finally {
                }
            }
        } catch (Throwable th) {
            try {
                isr.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public int ndots() {
        return this.ndots;
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public boolean isEnabled() {
        return (System.getProperty("os.name").contains("Windows") || System.getProperty("java.specification.vendor").toLowerCase().contains("android")) ? false : true;
    }
}
