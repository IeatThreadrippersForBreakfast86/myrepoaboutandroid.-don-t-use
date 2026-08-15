package org.xbill.DNS.tools;

import java.util.Iterator;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Zone;

/* loaded from: classes8.dex */
public class primary {
    private static void usage() {
        System.out.println("usage: primary [-t] [-a | -i] origin file");
        System.exit(1);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void main(String[] args) throws Exception {
        boolean time = false;
        boolean axfr = false;
        boolean iterator = false;
        int arg = 0;
        if (args.length < 2) {
            usage();
        }
        while (args.length - arg > 2) {
            char c = 0;
            String str = args[0];
            switch (str.hashCode()) {
                case 1492:
                    if (!str.equals("-a")) {
                        c = 65535;
                        break;
                    } else {
                        c = 1;
                        break;
                    }
                case 1500:
                    if (str.equals("-i")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1511:
                    if (!str.equals("-t")) {
                    }
                    break;
            }
            switch (c) {
                case 0:
                    time = true;
                    break;
                case 1:
                    axfr = true;
                    break;
                case 2:
                    iterator = true;
                    break;
            }
            arg++;
        }
        int arg2 = arg + 1;
        Name origin = Name.fromString(args[arg], Name.root);
        int i = arg2 + 1;
        String file = args[arg2];
        long start = System.currentTimeMillis();
        Zone zone = new Zone(origin, file);
        long end = System.currentTimeMillis();
        if (axfr) {
            Iterator<RRset> it = zone.AXFR();
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        } else if (iterator) {
            Iterator<RRset> it2 = zone.iterator();
            while (it2.hasNext()) {
                System.out.println(it2.next());
            }
        } else {
            System.out.println(zone);
        }
        if (time) {
            System.out.println("; Load time: " + (end - start) + " ms");
        }
    }
}
