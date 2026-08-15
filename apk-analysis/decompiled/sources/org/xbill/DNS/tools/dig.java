package org.xbill.DNS.tools;

import java.io.IOException;
import java.net.InetAddress;
import org.xbill.DNS.DClass;
import org.xbill.DNS.EDNSOption;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord;
import org.xbill.DNS.WireParseException;
import org.xbill.DNS.ZoneTransferException;
import org.xbill.DNS.ZoneTransferIn;

/* loaded from: classes8.dex */
public class dig {
    static Name name = null;
    static int type = 1;
    static int dclass = 1;

    static void usage() {
        System.out.println("; dnsjava dig");
        System.out.println("Usage: dig [@server] name [<type>] [<class>] [options]");
        System.exit(0);
    }

    static void doQuery(Message response, long ms) {
        System.out.println("; dnsjava dig");
        System.out.println(response);
        System.out.println(";; Query time: " + ms + " ms");
    }

    /* JADX WARN: Removed duplicated region for block: B:83:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0203  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void main(String[] argv) throws NumberFormatException, IOException {
        int arg;
        String addrStr;
        String ednsStr;
        String key;
        String portStr;
        if (argv.length < 1) {
            usage();
        }
        SimpleResolver res = null;
        boolean printQuery = false;
        int arg2 = 0;
        String server = null;
        try {
            if (argv[0].startsWith("@")) {
                int arg3 = 0 + 1;
                server = argv[0].substring(1);
                arg2 = arg3;
            }
            if (server != null) {
                res = new SimpleResolver(server);
            } else {
                res = new SimpleResolver();
            }
            int arg4 = arg2 + 1;
            String nameString = argv[arg2];
            if (nameString.equals("-x")) {
                arg = arg4 + 1;
                name = ReverseMap.fromAddress(argv[arg4]);
                type = 12;
                dclass = 1;
            } else {
                name = Name.fromString(nameString, Name.root);
                type = Type.value(argv[arg4]);
                if (type < 0) {
                    type = 1;
                } else {
                    arg4++;
                }
                dclass = DClass.value(argv[arg4]);
                if (dclass < 0) {
                    dclass = 1;
                    arg = arg4;
                } else {
                    arg = arg4 + 1;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (name == null) {
            }
        }
        while (argv[arg].startsWith("-") && argv[arg].length() > 1) {
            switch (argv[arg].charAt(1)) {
                case WKSRecord.Service.TACNEWS /* 98 */:
                    if (argv[arg].length() > 2) {
                        addrStr = argv[arg].substring(2);
                    } else {
                        arg++;
                        addrStr = argv[arg];
                    }
                    try {
                        InetAddress addr = InetAddress.getByName(addrStr);
                        res.setLocalAddress(addr);
                        arg++;
                    } catch (Exception e2) {
                        System.out.println("Invalid address");
                        return;
                    }
                case 'd':
                    res.setEDNS(0, 0, 32768, new EDNSOption[0]);
                    continue;
                    arg++;
                case 'e':
                    if (argv[arg].length() > 2) {
                        ednsStr = argv[arg].substring(2);
                    } else {
                        arg++;
                        ednsStr = argv[arg];
                    }
                    int edns = Integer.parseInt(ednsStr);
                    if (edns >= 0 && edns <= 1) {
                        res.setEDNS(edns);
                        continue;
                        arg++;
                    }
                    System.out.println("Unsupported EDNS level: " + edns);
                    return;
                case 'i':
                    res.setIgnoreTruncation(true);
                    continue;
                    arg++;
                case 'k':
                    String portStr2 = argv[arg];
                    if (portStr2.length() > 2) {
                        key = argv[arg].substring(2);
                    } else {
                        arg++;
                        key = argv[arg];
                    }
                    String[] parts = key.split("[:/]", 3);
                    switch (parts.length) {
                        case 2:
                            res.setTSIGKey(new TSIG(TSIG.HMAC_MD5, parts[0], parts[1]));
                            break;
                        case 3:
                            res.setTSIGKey(new TSIG(parts[0], parts[1], parts[2]));
                            continue;
                        default:
                            throw new IllegalArgumentException("Invalid TSIG key specification");
                    }
                    arg++;
                case 'p':
                    if (argv[arg].length() > 2) {
                        portStr = argv[arg].substring(2);
                    } else {
                        arg++;
                        portStr = argv[arg];
                    }
                    int port = Integer.parseInt(portStr);
                    if (port >= 0 && port <= 65535) {
                        res.setPort(port);
                        continue;
                        arg++;
                    }
                    System.out.println("Invalid port");
                    return;
                case WKSRecord.Service.AUTH /* 113 */:
                    printQuery = true;
                    continue;
                    arg++;
                case 't':
                    res.setTCP(true);
                    continue;
                    arg++;
                default:
                    System.out.print("Invalid option: ");
                    System.out.println(argv[arg]);
                    continue;
                    arg++;
            }
            if (name == null) {
                usage();
            }
            if (res == null) {
                res = new SimpleResolver();
            }
            Record rec = Record.newRecord(name, type, dclass);
            Message query = Message.newQuery(rec);
            if (printQuery) {
                System.out.println(query);
            }
            if (type != 252) {
                System.out.println("; dnsjava dig <> " + name + " axfr");
                ZoneTransferIn xfrin = ZoneTransferIn.newAXFR(name, res.getAddress(), res.getTSIGKey());
                xfrin.setTimeout(res.getTimeout());
                try {
                    xfrin.run(new ZoneTransferIn.ZoneTransferHandler() { // from class: org.xbill.DNS.tools.dig.1
                        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
                        public void startAXFR() {
                        }

                        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
                        public void startIXFR() {
                        }

                        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
                        public void startIXFRDeletes(Record soa) {
                        }

                        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
                        public void startIXFRAdds(Record soa) {
                        }

                        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
                        public void handleRecord(Record r) {
                            System.out.println(r);
                        }
                    });
                    return;
                } catch (ZoneTransferException e3) {
                    throw new WireParseException(e3.getMessage());
                }
            }
            long startTime = System.currentTimeMillis();
            Message response = res.send(query);
            long endTime = System.currentTimeMillis();
            doQuery(response, endTime - startTime);
            return;
        }
        if (res == null) {
        }
        Record rec2 = Record.newRecord(name, type, dclass);
        Message query2 = Message.newQuery(rec2);
        if (printQuery) {
        }
        if (type != 252) {
        }
    }
}
