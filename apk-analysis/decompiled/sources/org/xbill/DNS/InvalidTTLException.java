package org.xbill.DNS;

/* loaded from: classes8.dex */
public class InvalidTTLException extends IllegalArgumentException {
    public InvalidTTLException(long ttl) {
        super("Invalid DNS TTL: " + ttl);
    }
}
