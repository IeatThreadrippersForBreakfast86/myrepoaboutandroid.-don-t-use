package org.xbill.DNS.tools;

import java.util.Iterator;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.ZoneTransferIn;

/* loaded from: classes8.dex */
public class xfrin {
    private static void usage(String s) {
        System.out.println("Error: " + s);
        System.out.println("usage: xfrin [-i serial] [-k keyname/secret] [-s server] [-p port] [-f] zone");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        ZoneTransferIn xfrin;
        TSIG key = null;
        int ixfrSerial = -1;
        String server = null;
        int port = 53;
        boolean fallback = false;
        int arg = 0;
        while (arg < args.length) {
            if (args[arg].equals("-i")) {
                arg++;
                ixfrSerial = Integer.parseInt(args[arg]);
                if (ixfrSerial < 0) {
                    usage("invalid serial number");
                }
            } else if (args[arg].equals("-k")) {
                arg++;
                String s = args[arg];
                int index = s.indexOf(47);
                if (index < 0) {
                    usage("invalid key");
                }
                key = new TSIG(TSIG.HMAC_MD5, s.substring(0, index), s.substring(index + 1));
            } else if (args[arg].equals("-s")) {
                arg++;
                server = args[arg];
            } else if (args[arg].equals("-p")) {
                arg++;
                port = Integer.parseInt(args[arg]);
                if (port < 0 || port > 65535) {
                    usage("invalid port");
                }
            } else if (args[arg].equals("-f")) {
                fallback = true;
            } else if (!args[arg].startsWith("-")) {
                break;
            } else {
                usage("invalid option");
            }
            arg++;
        }
        if (arg >= args.length) {
            usage("no zone name specified");
        }
        Name zname = Name.fromString(args[arg]);
        if (server == null) {
            Lookup l = new Lookup(zname, 2);
            Record[] ns = l.run();
            if (ns == null) {
                System.out.println("failed to look up NS record: " + l.getErrorString());
                System.exit(1);
            }
            server = ns[0].rdataToString();
            System.out.println("sending to server '" + server + "'");
        }
        if (ixfrSerial >= 0) {
            xfrin = ZoneTransferIn.newIXFR(zname, ixfrSerial, fallback, server, port, key);
        } else {
            xfrin = ZoneTransferIn.newAXFR(zname, server, port, key);
        }
        xfrin.run();
        if (xfrin.isAXFR()) {
            if (ixfrSerial >= 0) {
                System.out.println("AXFR-like IXFR response");
            } else {
                System.out.println("AXFR response");
            }
            for (Record r : xfrin.getAXFR()) {
                System.out.println(r);
            }
            return;
        }
        if (xfrin.isIXFR()) {
            System.out.println("IXFR response");
            for (ZoneTransferIn.Delta delta : xfrin.getIXFR()) {
                System.out.println("delta from " + delta.start + " to " + delta.end);
                System.out.println("deletes");
                Iterator<Record> it2 = delta.deletes.iterator();
                while (it2.hasNext()) {
                    System.out.println(it2.next());
                }
                System.out.println("adds");
                Iterator<Record> it22 = delta.adds.iterator();
                while (it22.hasNext()) {
                    System.out.println(it22.next());
                }
            }
            return;
        }
        if (xfrin.isCurrent()) {
            System.out.println("up to date");
        }
    }
}
