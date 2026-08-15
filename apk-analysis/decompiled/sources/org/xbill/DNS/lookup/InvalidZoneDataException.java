package org.xbill.DNS.lookup;

/* loaded from: classes8.dex */
public class InvalidZoneDataException extends LookupFailedException {
    InvalidZoneDataException(String message, Throwable inner) {
        super(message, inner);
    }

    public InvalidZoneDataException(String message) {
        super(message);
    }
}
