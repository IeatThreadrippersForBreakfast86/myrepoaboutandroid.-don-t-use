package org.xbill.DNS.lookup;

/* loaded from: classes8.dex */
public class RedirectLoopException extends RedirectOverflowException {
    public RedirectLoopException(int maxRedirects) {
        super("Detected a redirect loop", maxRedirects);
    }
}
