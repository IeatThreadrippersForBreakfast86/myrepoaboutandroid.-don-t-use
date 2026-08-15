package org.xbill.DNS;

import java.util.HashMap;
import java.util.function.Supplier;

/* loaded from: classes8.dex */
public final class Type {

    /* renamed from: A */
    public static final int f266A = 1;

    /* renamed from: A6 */
    public static final int f267A6 = 38;
    public static final int AAAA = 28;
    public static final int AFSDB = 18;
    public static final int AMTRELAY = 260;
    public static final int ANY = 255;
    public static final int APL = 42;
    public static final int ATMA = 34;
    public static final int AVC = 258;
    public static final int AXFR = 252;
    public static final int CAA = 257;
    public static final int CDNSKEY = 60;
    public static final int CDS = 59;
    public static final int CERT = 37;
    public static final int CNAME = 5;
    public static final int CSYNC = 62;
    public static final int DHCID = 49;
    public static final int DLV = 32769;
    public static final int DNAME = 39;
    public static final int DNSKEY = 48;
    public static final int DOA = 259;

    /* renamed from: DS */
    public static final int f268DS = 43;
    public static final int EID = 31;
    public static final int EUI48 = 108;
    public static final int EUI64 = 109;
    public static final int GID = 102;
    public static final int GPOS = 27;
    public static final int HINFO = 13;
    public static final int HIP = 55;
    public static final int HTTPS = 65;
    public static final int IPSECKEY = 45;
    public static final int ISDN = 20;
    public static final int IXFR = 251;
    public static final int KEY = 25;

    /* renamed from: KX */
    public static final int f269KX = 36;
    public static final int L32 = 105;
    public static final int L64 = 106;
    public static final int LOC = 29;

    /* renamed from: LP */
    public static final int f270LP = 107;
    public static final int MAILA = 254;
    public static final int MAILB = 253;

    /* renamed from: MB */
    public static final int f271MB = 7;

    /* renamed from: MD */
    public static final int f272MD = 3;

    /* renamed from: MF */
    public static final int f273MF = 4;

    /* renamed from: MG */
    public static final int f274MG = 8;
    public static final int MINFO = 14;

    /* renamed from: MR */
    public static final int f275MR = 9;

    /* renamed from: MX */
    public static final int f276MX = 15;
    public static final int NAPTR = 35;
    public static final int NID = 104;
    public static final int NIMLOC = 32;
    public static final int NINFO = 56;

    /* renamed from: NS */
    public static final int f277NS = 2;
    public static final int NSAP = 22;
    public static final int NSAP_PTR = 23;
    public static final int NSEC = 47;
    public static final int NSEC3 = 50;
    public static final int NSEC3PARAM = 51;
    public static final int NULL = 10;
    public static final int NXNAME = 128;
    public static final int NXT = 30;
    public static final int OPENPGPKEY = 61;
    public static final int OPT = 41;
    public static final int PTR = 12;

    /* renamed from: PX */
    public static final int f278PX = 26;
    public static final int RKEY = 57;

    /* renamed from: RP */
    public static final int f279RP = 17;
    public static final int RRSIG = 46;

    /* renamed from: RT */
    public static final int f280RT = 21;
    public static final int SIG = 24;
    public static final int SINK = 40;
    public static final int SMIMEA = 53;
    public static final int SOA = 6;
    public static final int SPF = 99;
    public static final int SRV = 33;
    public static final int SSHFP = 44;
    public static final int SVCB = 64;

    /* renamed from: TA */
    public static final int f281TA = 32768;
    public static final int TALINK = 58;
    public static final int TKEY = 249;
    public static final int TLSA = 52;
    public static final int TSIG = 250;
    public static final int TXT = 16;
    public static final int UID = 101;
    public static final int UINFO = 100;
    public static final int UNSPEC = 103;
    public static final int URI = 256;
    public static final int WKS = 11;
    public static final int X25 = 19;
    public static final int ZONEMD = 63;
    private static final TypeMnemonic types = new TypeMnemonic();

    private static class TypeMnemonic extends Mnemonic {
        private final HashMap<Integer, Supplier<Record>> factories;

        public TypeMnemonic() {
            super("Type", 2);
            setPrefix("TYPE");
            setMaximum(65535);
            this.factories = new HashMap<>();
        }

        public void add(int val, String str, Supplier<Record> factory) {
            super.add(val, str);
            this.factories.put(Integer.valueOf(val), factory);
        }

        public void replace(int val, String str, Supplier<Record> factory) {
            int oldVal = getValue(str);
            if (oldVal != -1) {
                if (oldVal != val) {
                    throw new IllegalArgumentException("mnemnonic \"" + str + "\" already used by type " + oldVal);
                }
                remove(val);
                this.factories.remove(Integer.valueOf(val));
            }
            add(val, str, factory);
        }

        @Override // org.xbill.DNS.Mnemonic
        public void check(int val) {
            Type.check(val);
        }

        public Supplier<Record> getFactory(int val) {
            check(val);
            return this.factories.get(Integer.valueOf(val));
        }
    }

    static {
        types.add(1, "A", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ARecord();
            }
        });
        types.add(2, "NS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSRecord();
            }
        });
        types.add(3, "MD", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda14
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MDRecord();
            }
        });
        types.add(4, "MF", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda26
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MFRecord();
            }
        });
        types.add(5, "CNAME", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda38
            @Override // java.util.function.Supplier
            public final Object get() {
                return new CNAMERecord();
            }
        });
        types.add(6, "SOA", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda50
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SOARecord();
            }
        });
        types.add(7, "MB", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda53
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MBRecord();
            }
        });
        types.add(8, "MG", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda54
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MGRecord();
            }
        });
        types.add(9, "MR", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda56
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MRRecord();
            }
        });
        types.add(10, "NULL", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda57
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NULLRecord();
            }
        });
        types.add(11, "WKS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda11
            @Override // java.util.function.Supplier
            public final Object get() {
                return new WKSRecord();
            }
        });
        types.add(12, "PTR", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda22
            @Override // java.util.function.Supplier
            public final Object get() {
                return new PTRRecord();
            }
        });
        types.add(13, "HINFO", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda33
            @Override // java.util.function.Supplier
            public final Object get() {
                return new HINFORecord();
            }
        });
        types.add(14, "MINFO", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda44
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MINFORecord();
            }
        });
        types.add(15, "MX", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda55
            @Override // java.util.function.Supplier
            public final Object get() {
                return new MXRecord();
            }
        });
        types.add(16, "TXT", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda58
            @Override // java.util.function.Supplier
            public final Object get() {
                return new TXTRecord();
            }
        });
        types.add(17, "RP", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda59
            @Override // java.util.function.Supplier
            public final Object get() {
                return new RPRecord();
            }
        });
        types.add(18, "AFSDB", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda60
            @Override // java.util.function.Supplier
            public final Object get() {
                return new AFSDBRecord();
            }
        });
        types.add(19, "X25", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda61
            @Override // java.util.function.Supplier
            public final Object get() {
                return new X25Record();
            }
        });
        types.add(20, "ISDN", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ISDNRecord();
            }
        });
        types.add(21, "RT", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return new RTRecord();
            }
        });
        types.add(22, "NSAP", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSAPRecord();
            }
        });
        types.add(23, "NSAP-PTR", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSAP_PTRRecord();
            }
        });
        types.add(24, "SIG", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SIGRecord();
            }
        });
        types.add(25, "KEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final Object get() {
                return new KEYRecord();
            }
        });
        types.add(26, "PX", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final Object get() {
                return new PXRecord();
            }
        });
        types.add(27, "GPOS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final Object get() {
                return new GPOSRecord();
            }
        });
        types.add(28, "AAAA", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final Object get() {
                return new AAAARecord();
            }
        });
        types.add(29, "LOC", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final Object get() {
                return new LOCRecord();
            }
        });
        types.add(30, "NXT", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda13
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NXTRecord();
            }
        });
        types.add(31, "EID");
        types.add(32, "NIMLOC");
        types.add(33, "SRV", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda15
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SRVRecord();
            }
        });
        types.add(34, "ATMA");
        types.add(35, "NAPTR", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda16
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NAPTRRecord();
            }
        });
        types.add(36, "KX", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda17
            @Override // java.util.function.Supplier
            public final Object get() {
                return new KXRecord();
            }
        });
        types.add(37, "CERT", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda18
            @Override // java.util.function.Supplier
            public final Object get() {
                return new CERTRecord();
            }
        });
        types.add(38, "A6", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda19
            @Override // java.util.function.Supplier
            public final Object get() {
                return new A6Record();
            }
        });
        types.add(39, "DNAME", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda20
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DNAMERecord();
            }
        });
        types.add(40, "SINK");
        types.add(41, "OPT", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda21
            @Override // java.util.function.Supplier
            public final Object get() {
                return new OPTRecord();
            }
        });
        types.add(42, "APL", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda23
            @Override // java.util.function.Supplier
            public final Object get() {
                return new APLRecord();
            }
        });
        types.add(43, "DS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda24
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DSRecord();
            }
        });
        types.add(44, "SSHFP", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda25
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SSHFPRecord();
            }
        });
        types.add(45, "IPSECKEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda27
            @Override // java.util.function.Supplier
            public final Object get() {
                return new IPSECKEYRecord();
            }
        });
        types.add(46, "RRSIG", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda28
            @Override // java.util.function.Supplier
            public final Object get() {
                return new RRSIGRecord();
            }
        });
        types.add(47, "NSEC", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda29
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSECRecord();
            }
        });
        types.add(48, "DNSKEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda30
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DNSKEYRecord();
            }
        });
        types.add(49, "DHCID", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda31
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DHCIDRecord();
            }
        });
        types.add(50, "NSEC3", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda32
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSEC3Record();
            }
        });
        types.add(51, "NSEC3PARAM", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda34
            @Override // java.util.function.Supplier
            public final Object get() {
                return new NSEC3PARAMRecord();
            }
        });
        types.add(52, "TLSA", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda35
            @Override // java.util.function.Supplier
            public final Object get() {
                return new TLSARecord();
            }
        });
        types.add(53, "SMIMEA", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda36
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SMIMEARecord();
            }
        });
        types.add(55, "HIP", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda37
            @Override // java.util.function.Supplier
            public final Object get() {
                return new HIPRecord();
            }
        });
        types.add(56, "NINFO");
        types.add(57, "RKEY");
        types.add(58, "TALINK");
        types.add(59, "CDS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda39
            @Override // java.util.function.Supplier
            public final Object get() {
                return new CDSRecord();
            }
        });
        types.add(60, "CDNSKEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda40
            @Override // java.util.function.Supplier
            public final Object get() {
                return new CDNSKEYRecord();
            }
        });
        types.add(61, "OPENPGPKEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda41
            @Override // java.util.function.Supplier
            public final Object get() {
                return new OPENPGPKEYRecord();
            }
        });
        types.add(62, "CSYNC");
        types.add(63, "ZONEMD", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda42
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ZoneMDRecord();
            }
        });
        types.add(64, "SVCB", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda43
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SVCBRecord();
            }
        });
        types.add(65, "HTTPS", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda45
            @Override // java.util.function.Supplier
            public final Object get() {
                return new HTTPSRecord();
            }
        });
        types.add(99, "SPF", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda46
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SPFRecord();
            }
        });
        types.add(100, "UINFO");
        types.add(101, "UID");
        types.add(102, "GID");
        types.add(103, "UNSPEC");
        types.add(104, "NID");
        types.add(105, "L32");
        types.add(L64, "L64");
        types.add(107, "LP");
        types.add(108, "EUI48");
        types.add(109, "EUI64");
        types.add(TKEY, "TKEY", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda47
            @Override // java.util.function.Supplier
            public final Object get() {
                return new TKEYRecord();
            }
        });
        types.add(250, "TSIG", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda48
            @Override // java.util.function.Supplier
            public final Object get() {
                return new TSIGRecord();
            }
        });
        types.add(IXFR, "IXFR");
        types.add(252, "AXFR");
        types.add(253, "MAILB");
        types.add(254, "MAILA");
        types.add(255, "ANY");
        types.add(256, "URI", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda49
            @Override // java.util.function.Supplier
            public final Object get() {
                return new URIRecord();
            }
        });
        types.add(257, "CAA", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda51
            @Override // java.util.function.Supplier
            public final Object get() {
                return new CAARecord();
            }
        });
        types.add(AVC, "AVC");
        types.add(DOA, "DOA");
        types.add(AMTRELAY, "AMTRELAY");
        types.add(32768, "TA");
        types.add(32769, "DLV", new Supplier() { // from class: org.xbill.DNS.Type$$ExternalSyntheticLambda52
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DLVRecord();
            }
        });
    }

    private Type() {
    }

    public static void check(int val) {
        if (!Utils.isUInt16(val)) {
            throw new InvalidTypeException(val);
        }
    }

    public static void register(int val, String str, Supplier<Record> factory) {
        types.replace(val, str, factory);
    }

    public static String string(int val) {
        return types.getText(val);
    }

    public static int value(String s, boolean numberok) {
        int val = types.getValue(s);
        if (val == -1 && numberok) {
            return types.getValue("TYPE" + s);
        }
        return val;
    }

    public static int value(String s) {
        return value(s, false);
    }

    static Supplier<Record> getFactory(int val) {
        return types.getFactory(val);
    }

    public static boolean isRR(int type) {
        switch (type) {
            case 41:
            case TKEY /* 249 */:
            case 250:
            case IXFR /* 251 */:
            case 252:
            case 253:
            case 254:
            case 255:
                return false;
            default:
                return true;
        }
    }
}
