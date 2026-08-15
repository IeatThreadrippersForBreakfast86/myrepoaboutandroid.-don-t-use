package org.xbill.DNS.lookup;

/* loaded from: classes8.dex */
public class RedirectOverflowException extends LookupFailedException {
    private final int maxRedirects;

    public int getMaxRedirects() {
        return this.maxRedirects;
    }

    @Deprecated
    public RedirectOverflowException(String message) {
        super(message);
        this.maxRedirects = 0;
    }

    public RedirectOverflowException(int maxRedirects) {
        super("Refusing to follow more than " + maxRedirects + " redirects");
        this.maxRedirects = maxRedirects;
    }

    RedirectOverflowException(String message, int maxRedirects) {
        super(message);
        this.maxRedirects = maxRedirects;
    }
}
