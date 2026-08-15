package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import java.util.StringTokenizer;
import org.xbill.DNS.DNSSEC;

/* loaded from: classes8.dex */
public class KEYRecord extends KEYBase {
    public static final int FLAG_NOAUTH = 32768;
    public static final int FLAG_NOCONF = 16384;
    public static final int FLAG_NOKEY = 49152;
    public static final int OWNER_HOST = 512;
    public static final int OWNER_USER = 0;
    public static final int OWNER_ZONE = 256;
    public static final int PROTOCOL_ANY = 255;
    public static final int PROTOCOL_DNSSEC = 3;
    public static final int PROTOCOL_EMAIL = 2;
    public static final int PROTOCOL_IPSEC = 4;
    public static final int PROTOCOL_TLS = 1;

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ int getAlgorithm() {
        return super.getAlgorithm();
    }

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ int getFlags() {
        return super.getFlags();
    }

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ int getFootprint() {
        return super.getFootprint();
    }

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ byte[] getKey() {
        return super.getKey();
    }

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ int getProtocol() {
        return super.getProtocol();
    }

    @Override // org.xbill.DNS.KEYBase
    public /* bridge */ /* synthetic */ PublicKey getPublicKey() throws DNSSEC.DNSSECException {
        return super.getPublicKey();
    }

    public static class Protocol {
        public static final int ANY = 255;
        public static final int DNSSEC = 3;
        public static final int EMAIL = 2;
        public static final int IPSEC = 4;
        public static final int NONE = 0;
        public static final int TLS = 1;
        private static final Mnemonic protocols = new Mnemonic("KEY protocol", 2);

        private Protocol() {
        }

        static {
            protocols.setMaximum(255);
            protocols.setNumericAllowed(true);
            protocols.add(0, "NONE");
            protocols.add(1, "TLS");
            protocols.add(2, "EMAIL");
            protocols.add(3, "DNSSEC");
            protocols.add(4, "IPSEC");
            protocols.add(255, "ANY");
        }

        public static String string(int type) {
            return protocols.getText(type);
        }

        public static int value(String s) {
            return protocols.getValue(s);
        }
    }

    public static class Flags {
        public static final int EXTEND = 4096;
        public static final int FLAG10 = 32;
        public static final int FLAG11 = 16;
        public static final int FLAG2 = 8192;
        public static final int FLAG4 = 2048;
        public static final int FLAG5 = 1024;
        public static final int FLAG8 = 128;
        public static final int FLAG9 = 64;
        public static final int HOST = 512;
        private static final Mnemonic KEY_FLAGS = new Mnemonic("KEY flags", 2);
        public static final int NOAUTH = 32768;
        public static final int NOCONF = 16384;
        public static final int NOKEY = 49152;
        public static final int NTYP3 = 768;
        public static final int OWNER_MASK = 768;
        public static final int SIG0 = 0;
        public static final int SIG1 = 1;
        public static final int SIG10 = 10;
        public static final int SIG11 = 11;
        public static final int SIG12 = 12;
        public static final int SIG13 = 13;
        public static final int SIG14 = 14;
        public static final int SIG15 = 15;
        public static final int SIG2 = 2;
        public static final int SIG3 = 3;
        public static final int SIG4 = 4;
        public static final int SIG5 = 5;
        public static final int SIG6 = 6;
        public static final int SIG7 = 7;
        public static final int SIG8 = 8;
        public static final int SIG9 = 9;
        public static final int USER = 0;
        public static final int USE_MASK = 49152;
        public static final int ZONE = 256;

        private Flags() {
        }

        static {
            KEY_FLAGS.setMaximum(65535);
            KEY_FLAGS.setNumericAllowed(false);
            KEY_FLAGS.add(16384, "NOCONF");
            KEY_FLAGS.add(32768, "NOAUTH");
            KEY_FLAGS.add(49152, "NOKEY");
            KEY_FLAGS.add(8192, "FLAG2");
            KEY_FLAGS.add(4096, "EXTEND");
            KEY_FLAGS.add(2048, "FLAG4");
            KEY_FLAGS.add(1024, "FLAG5");
            KEY_FLAGS.add(0, "USER");
            KEY_FLAGS.add(256, "ZONE");
            KEY_FLAGS.add(512, "HOST");
            KEY_FLAGS.add(768, "NTYP3");
            KEY_FLAGS.add(128, "FLAG8");
            KEY_FLAGS.add(64, "FLAG9");
            KEY_FLAGS.add(32, "FLAG10");
            KEY_FLAGS.add(16, "FLAG11");
            KEY_FLAGS.add(0, "SIG0");
            KEY_FLAGS.add(1, "SIG1");
            KEY_FLAGS.add(2, "SIG2");
            KEY_FLAGS.add(3, "SIG3");
            KEY_FLAGS.add(4, "SIG4");
            KEY_FLAGS.add(5, "SIG5");
            KEY_FLAGS.add(6, "SIG6");
            KEY_FLAGS.add(7, "SIG7");
            KEY_FLAGS.add(8, "SIG8");
            KEY_FLAGS.add(9, "SIG9");
            KEY_FLAGS.add(10, "SIG10");
            KEY_FLAGS.add(11, "SIG11");
            KEY_FLAGS.add(12, "SIG12");
            KEY_FLAGS.add(13, "SIG13");
            KEY_FLAGS.add(14, "SIG14");
            KEY_FLAGS.add(15, "SIG15");
        }

        public static int value(String s) throws NumberFormatException {
            try {
                int value = Integer.parseInt(s);
                if (!Utils.isUInt16(value)) {
                    return -1;
                }
                return value;
            } catch (NumberFormatException e) {
                StringTokenizer st = new StringTokenizer(s, "|");
                int value2 = 0;
                while (st.hasMoreTokens()) {
                    int val = KEY_FLAGS.getValue(st.nextToken());
                    if (val < 0) {
                        return -1;
                    }
                    value2 |= val;
                }
                return value2;
            }
        }
    }

    KEYRecord() {
    }

    public KEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, 25, dclass, ttl, flags, proto, alg, key);
    }

    public KEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, PublicKey key) throws DNSSEC.DNSSECException {
        super(name, 25, dclass, ttl, flags, proto, alg, DNSSEC.fromPublicKey(key, alg));
        this.publicKey = key;
    }

    @Override // org.xbill.DNS.Record
    protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String flagString = st.getIdentifier();
        this.flags = Flags.value(flagString);
        if (this.flags < 0) {
            throw st.exception("Invalid flags: " + flagString);
        }
        String protoString = st.getIdentifier();
        this.proto = Protocol.value(protoString);
        if (this.proto < 0) {
            throw st.exception("Invalid protocol: " + protoString);
        }
        String algString = st.getIdentifier();
        this.alg = DNSSEC.Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception("Invalid algorithm: " + algString);
        }
        if ((this.flags & 49152) == 49152) {
            this.key = null;
        } else {
            this.key = st.getBase64();
        }
    }
}
