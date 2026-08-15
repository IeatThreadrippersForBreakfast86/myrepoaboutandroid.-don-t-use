package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.NSEC3Record;

/* loaded from: classes8.dex */
public class DnssecAlgorithmOption extends EDNSOption {
    private final List<Integer> algCodes;

    private DnssecAlgorithmOption(int code) {
        super(code);
        switch (code) {
            case 5:
            case 6:
            case 7:
                this.algCodes = new ArrayList();
                return;
            default:
                throw new IllegalArgumentException("Invalid option code, must be one of DAU, DHU, N3U");
        }
    }

    public DnssecAlgorithmOption(int code, List<Integer> algCodes) {
        this(code);
        this.algCodes.addAll(algCodes);
    }

    public DnssecAlgorithmOption(int code, int... algCodes) {
        this(code);
        if (algCodes != null) {
            for (int algCode : algCodes) {
                this.algCodes.add(Integer.valueOf(algCode));
            }
        }
    }

    public List<Integer> getAlgorithms() {
        return Collections.unmodifiableList(this.algCodes);
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionFromWire(DNSInput in) throws IOException {
        this.algCodes.clear();
        while (in.remaining() > 0) {
            this.algCodes.add(Integer.valueOf(in.readU8()));
        }
    }

    @Override // org.xbill.DNS.EDNSOption
    void optionToWire(final DNSOutput out) {
        List<Integer> list = this.algCodes;
        Objects.requireNonNull(out);
        list.forEach(new Consumer() { // from class: org.xbill.DNS.DnssecAlgorithmOption$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                out.writeU8(((Integer) obj).intValue());
            }
        });
    }

    @Override // org.xbill.DNS.EDNSOption
    String optionToString() {
        Function<Integer, String> mapper;
        switch (getCode()) {
            case 5:
                mapper = new Function() { // from class: org.xbill.DNS.DnssecAlgorithmOption$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return DNSSEC.Algorithm.string(((Integer) obj).intValue());
                    }
                };
                break;
            case 6:
                mapper = new Function() { // from class: org.xbill.DNS.DnssecAlgorithmOption$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return DNSSEC.Digest.string(((Integer) obj).intValue());
                    }
                };
                break;
            case 7:
                mapper = new Function() { // from class: org.xbill.DNS.DnssecAlgorithmOption$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return NSEC3Record.Digest.string(((Integer) obj).intValue());
                    }
                };
                break;
            default:
                throw new IllegalStateException("Unknown option code");
        }
        return "[" + ((String) this.algCodes.stream().map(mapper).collect(Collectors.joining(", "))) + "]";
    }
}
