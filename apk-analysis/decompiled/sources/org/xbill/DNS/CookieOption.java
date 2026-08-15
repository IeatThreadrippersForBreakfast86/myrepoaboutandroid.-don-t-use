package org.xbill.DNS;

import java.io.IOException;
import java.util.Optional;
import org.xbill.DNS.utils.base16;

/* loaded from: classes8.dex */
public class CookieOption extends EDNSOption {
    private byte[] clientCookie;
    private byte[] serverCookie;

    CookieOption() {
        super(10);
    }

    public CookieOption(byte[] clientCookie) {
        this(clientCookie, null);
    }

    public CookieOption(byte[] clientCookie, byte[] serverCookie) {
        int length;
        this();
        if (clientCookie == null) {
            throw new IllegalArgumentException("client cookie must not be null");
        }
        if (clientCookie.length != 8) {
            throw new IllegalArgumentException("client cookie must consist of eight bytes");
        }
        this.clientCookie = clientCookie;
        if (serverCookie != null && ((length = serverCookie.length) < 8 || length > 32)) {
            throw new IllegalArgumentException("server cookie must consist of 8 to 32 bytes");
        }
        this.serverCookie = serverCookie;
    }

    public byte[] getClientCookie() {
        return this.clientCookie;
    }

    public Optional<byte[]> getServerCookie() {
        return Optional.ofNullable(this.serverCookie);
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) throws IOException {
        int length = in.remaining();
        if (length < 8) {
            throw new WireParseException("invalid length of client cookie");
        }
        this.clientCookie = in.readByteArray(8);
        if (length > 8) {
            if (length < 16 || length > 40) {
                throw new WireParseException("invalid length of server cookie");
            }
            this.serverCookie = in.readByteArray();
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(DNSOutput out) {
        out.writeByteArray(this.clientCookie);
        if (this.serverCookie != null) {
            out.writeByteArray(this.serverCookie);
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        if (this.serverCookie != null) {
            return base16.toString(this.clientCookie) + " " + base16.toString(this.serverCookie);
        }
        return base16.toString(this.clientCookie);
    }
}
