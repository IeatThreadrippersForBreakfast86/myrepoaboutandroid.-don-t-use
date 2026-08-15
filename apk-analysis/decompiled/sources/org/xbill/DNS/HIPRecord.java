package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base64;

/* loaded from: classes8.dex */
public class HIPRecord extends Record {
    private byte[] hit;
    private int pkAlgorithm;
    private byte[] publicKey;
    private final List<Name> rvServers;

    HIPRecord() {
        this.rvServers = new ArrayList();
    }

    public HIPRecord(Name name, int dclass, long ttl, byte[] hit, int alg, byte[] key, List<Name> servers) {
        super(name, 55, dclass, ttl);
        this.rvServers = new ArrayList();
        this.hit = hit;
        this.pkAlgorithm = alg;
        this.publicKey = key;
        if (servers != null) {
            this.rvServers.addAll(servers);
        }
    }

    public HIPRecord(Name name, int dclass, long ttl, byte[] hit, int alg, byte[] key) {
        this(name, dclass, ttl, hit, alg, key, (List<Name>) null);
    }

    public HIPRecord(Name name, int dclass, long ttl, byte[] hit, int alg, PublicKey key, List<Name> servers) throws DNSSEC.DNSSECException {
        this(name, dclass, ttl, hit, alg, DNSSEC.fromPublicKey(key, mapAlgTypeToDnssec(alg)), servers);
    }

    public HIPRecord(Name name, int dclass, long ttl, byte[] hit, int alg, PublicKey key) throws DNSSEC.DNSSECException {
        this(name, dclass, ttl, hit, alg, key, (List<Name>) null);
    }

    public byte[] getHit() {
        return this.hit;
    }

    public int getAlgorithm() {
        return this.pkAlgorithm;
    }

    public byte[] getKey() {
        return this.publicKey;
    }

    public PublicKey getPublicKey() throws DNSSEC.DNSSECException {
        return DNSSEC.toPublicKey(mapAlgTypeToDnssec(this.pkAlgorithm), this.publicKey, this);
    }

    public List<Name> getRvServers() {
        return Collections.unmodifiableList(this.rvServers);
    }

    private static int mapAlgTypeToDnssec(int alg) throws DNSSEC.UnsupportedAlgorithmException {
        switch (alg) {
            case 1:
                return 3;
            case 2:
                return 5;
            default:
                throw new DNSSEC.UnsupportedAlgorithmException(alg);
        }
    }

    @Override // org.xbill.DNS.Record
    protected String rrToString() {
        StringBuilder sb = new StringBuilder();
        if (Options.multiline()) {
            sb.append("( ");
        }
        String separator = Options.multiline() ? "\n\t" : " ";
        sb.append(this.pkAlgorithm);
        sb.append(" ");
        sb.append(base16.toString(this.hit));
        sb.append(separator);
        sb.append(base64.toString(this.publicKey));
        if (!this.rvServers.isEmpty()) {
            sb.append(separator);
        }
        sb.append((String) this.rvServers.stream().map(new Function() { // from class: org.xbill.DNS.HIPRecord$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((Name) obj).toString();
            }
        }).collect(Collectors.joining(separator)));
        if (Options.multiline()) {
            sb.append(" )");
        }
        return sb.toString();
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.pkAlgorithm = st.getUInt8();
        this.hit = st.getHexString();
        this.publicKey = base64.fromString(st.getString());
        while (true) {
            Tokenizer.Token t = st.get();
            if (t.isString()) {
                this.rvServers.add(new Name(t.value()));
            } else {
                return;
            }
        }
    }

    @Override // org.xbill.DNS.Record
    protected void rrToWire(final DNSOutput out, Compression c, final boolean canonical) {
        out.writeU8(this.hit.length);
        out.writeU8(this.pkAlgorithm);
        out.writeU16(this.publicKey.length);
        out.writeByteArray(this.hit);
        out.writeByteArray(this.publicKey);
        this.rvServers.forEach(new Consumer() { // from class: org.xbill.DNS.HIPRecord$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Name) obj).toWire(out, null, canonical);
            }
        });
    }

    @Override // org.xbill.DNS.Record
    protected void rrFromWire(DNSInput in) throws IOException {
        int hitLength = in.readU8();
        this.pkAlgorithm = in.readU8();
        int pkLength = in.readU16();
        this.hit = in.readByteArray(hitLength);
        this.publicKey = in.readByteArray(pkLength);
        while (in.remaining() > 0) {
            this.rvServers.add(new Name(in));
        }
    }
}
