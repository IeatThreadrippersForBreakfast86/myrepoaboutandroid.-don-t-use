package org.xbill.DNS.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes8.dex */
public class AndroidResolverConfigProvider extends BaseResolverConfigProvider {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) AndroidResolverConfigProvider.class);
    private static Context context = null;

    public static void setContext(Context ctx) {
        context = ctx;
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public void initialize() throws InitializationException {
        LinkProperties lp;
        reset();
        if (context == null) {
            throw new InitializationException("Context must be initialized by calling setContext");
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        Network network = cm.getActiveNetwork();
        if (network == null || (lp = cm.getLinkProperties(network)) == null) {
            return;
        }
        for (InetAddress address : lp.getDnsServers()) {
            addNameserver(new InetSocketAddress(address, 53));
        }
        parseSearchPathList(lp.getDomains(), ",");
    }

    @Override // org.xbill.DNS.config.ResolverConfigProvider
    public boolean isEnabled() {
        return System.getProperty("java.vendor").contains("Android");
    }
}
