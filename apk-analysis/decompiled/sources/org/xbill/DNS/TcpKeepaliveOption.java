package org.xbill.DNS;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.OptionalInt;

/* loaded from: classes8.dex */
public class TcpKeepaliveOption extends EDNSOption {
    private static final Duration UPPER_LIMIT = Duration.ofMillis(6553600);
    private Integer timeout;

    public TcpKeepaliveOption() {
        super(11);
        this.timeout = null;
    }

    public TcpKeepaliveOption(int t) {
        super(11);
        if (t < 0 || t > 65535) {
            throw new IllegalArgumentException("timeout must be betwee 0 and 65535");
        }
        this.timeout = Integer.valueOf(t);
    }

    public TcpKeepaliveOption(Duration t) {
        super(11);
        if (t.isNegative() || t.compareTo(UPPER_LIMIT) >= 0) {
            throw new IllegalArgumentException("timeout must be between 0 and 6553.6 seconds (exclusively)");
        }
        this.timeout = Integer.valueOf(((int) t.toMillis()) / 100);
    }

    public OptionalInt getTimeout() {
        return this.timeout == null ? OptionalInt.empty() : OptionalInt.of(this.timeout.intValue());
    }

    public Optional<Duration> getTimeoutDuration() {
        return this.timeout != null ? Optional.of(Duration.ofMillis(this.timeout.intValue() * 100)) : Optional.empty();
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) throws IOException {
        int length = in.remaining();
        switch (length) {
            case 0:
                this.timeout = null;
                return;
            case 1:
            default:
                throw new WireParseException("invalid length (" + length + ") of the data in the edns_tcp_keepalive option");
            case 2:
                this.timeout = Integer.valueOf(in.readU16());
                return;
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(DNSOutput out) {
        if (this.timeout != null) {
            out.writeU16(this.timeout.intValue());
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        return this.timeout != null ? String.valueOf(this.timeout) : "-";
    }
}
