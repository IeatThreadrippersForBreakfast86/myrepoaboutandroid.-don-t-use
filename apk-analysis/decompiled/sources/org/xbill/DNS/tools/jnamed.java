package org.xbill.DNS.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import org.xbill.DNS.Address;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DNAMERecord;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.SetResponse;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TSIGRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.Zone;
import org.xbill.DNS.ZoneTransferException;

/* loaded from: classes8.dex */
public class jnamed {
    static final int FLAG_DNSSECOK = 1;
    static final int FLAG_SIGONLY = 2;
    Map<Integer, Cache> caches;
    Map<Name, TSIG> tsigs;
    Map<Name, Zone> znames;

    private static String addrport(InetAddress addr, int port) {
        return addr.getHostAddress() + "#" + port;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:18:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public jnamed(String conffile) throws ZoneTransferException, IOException {
        List<Integer> ports = new ArrayList<>();
        List<InetAddress> addresses = new ArrayList<>();
        try {
            FileInputStream fs = new FileInputStream(conffile);
            InputStreamReader isr = new InputStreamReader(fs);
            BufferedReader br = new BufferedReader(isr);
            try {
                this.caches = new HashMap();
                this.znames = new HashMap();
                this.tsigs = new HashMap();
                while (true) {
                    String line = br.readLine();
                    if (line != null) {
                        StringTokenizer st = new StringTokenizer(line);
                        if (st.hasMoreTokens()) {
                            String keyword = st.nextToken();
                            if (!st.hasMoreTokens()) {
                                System.out.println("Invalid line: " + line);
                            } else {
                                char c = 0;
                                if (keyword.charAt(0) != '#') {
                                    switch (keyword.hashCode()) {
                                        case -1147692044:
                                            if (keyword.equals("address")) {
                                                c = 5;
                                                break;
                                            } else {
                                                c = 65535;
                                                break;
                                            }
                                        case -817598092:
                                            if (keyword.equals("secondary")) {
                                                c = 1;
                                                break;
                                            }
                                            break;
                                        case -314765822:
                                            if (keyword.equals("primary")) {
                                                break;
                                            }
                                            break;
                                        case 106079:
                                            if (keyword.equals("key")) {
                                                c = 3;
                                                break;
                                            }
                                            break;
                                        case 3446913:
                                            if (keyword.equals("port")) {
                                                c = 4;
                                                break;
                                            }
                                            break;
                                        case 94416770:
                                            if (keyword.equals("cache")) {
                                                c = 2;
                                                break;
                                            }
                                            break;
                                    }
                                    switch (c) {
                                        case 0:
                                            addPrimaryZone(st.nextToken(), st.nextToken());
                                            break;
                                        case 1:
                                            addSecondaryZone(st.nextToken(), st.nextToken());
                                            break;
                                        case 2:
                                            Cache cache = new Cache(st.nextToken());
                                            this.caches.put(1, cache);
                                            break;
                                        case 3:
                                            String s1 = st.nextToken();
                                            String s2 = st.nextToken();
                                            if (st.hasMoreTokens()) {
                                                addTSIG(s1, s2, st.nextToken());
                                                break;
                                            } else {
                                                addTSIG("hmac-md5", s1, s2);
                                                break;
                                            }
                                        case 4:
                                            String addr = st.nextToken();
                                            ports.add(Integer.valueOf(addr));
                                            break;
                                        case 5:
                                            String addr2 = st.nextToken();
                                            addresses.add(Address.getByAddress(addr2));
                                            break;
                                        default:
                                            System.out.println("unknown keyword: " + keyword);
                                            break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (ports.isEmpty()) {
                            ports.add(53);
                        }
                        if (addresses.isEmpty()) {
                            addresses.add(Address.getByAddress("0.0.0.0"));
                        }
                        for (InetAddress address : addresses) {
                            for (Integer o : ports) {
                                int port = o.intValue();
                                addUDP(address, port);
                                addTCP(address, port);
                                System.out.println("jnamed: listening on " + addrport(address, port));
                            }
                        }
                        System.out.println("jnamed: running");
                        return;
                    }
                }
            } finally {
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Cannot open " + conffile);
        }
    }

    public void addPrimaryZone(String zname, String zonefile) throws IOException {
        Name origin = null;
        if (zname != null) {
            origin = Name.fromString(zname, Name.root);
        }
        Zone newzone = new Zone(origin, zonefile);
        this.znames.put(newzone.getOrigin(), newzone);
    }

    public void addSecondaryZone(String zone, String remote) throws ZoneTransferException, IOException {
        Name zname = Name.fromString(zone, Name.root);
        Zone newzone = new Zone(zname, 1, remote);
        this.znames.put(zname, newzone);
    }

    public void addTSIG(String algstr, String namestr, String key) throws IOException {
        Name name = Name.fromString(namestr, Name.root);
        this.tsigs.put(name, new TSIG(algstr, namestr, key));
    }

    public Cache getCache(int dclass) {
        return this.caches.computeIfAbsent(Integer.valueOf(dclass), new Function() { // from class: org.xbill.DNS.tools.jnamed$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return new Cache(((Integer) obj).intValue());
            }
        });
    }

    public Zone findBestZone(Name name) {
        Zone foundzone = this.znames.get(name);
        if (foundzone != null) {
            return foundzone;
        }
        int labels = name.labels();
        for (int i = 1; i < labels; i++) {
            Name tname = new Name(name, i);
            Zone foundzone2 = this.znames.get(tname);
            if (foundzone2 != null) {
                return foundzone2;
            }
        }
        return null;
    }

    public RRset findExactMatch(Name name, int type, int dclass, boolean glue) {
        List<RRset> rrsets;
        Zone zone = findBestZone(name);
        if (zone != null) {
            return zone.findExactMatch(name, type);
        }
        Cache cache = getCache(dclass);
        if (glue) {
            rrsets = cache.findAnyRecords(name, type);
        } else {
            rrsets = cache.findRecords(name, type);
        }
        if (rrsets == null) {
            return null;
        }
        return rrsets.get(0);
    }

    void addRRset(Name name, Message response, RRset rrset, int section, int flags) {
        for (int s = 1; s <= section; s++) {
            if (response.findRRset(name, rrset.getType(), s)) {
                return;
            }
        }
        int s2 = flags & 2;
        if (s2 == 0) {
            for (Record r : rrset.rrs()) {
                if (r.getName().isWild() && !name.isWild()) {
                    r = r.withName(name);
                }
                response.addRecord(r, section);
            }
        }
        if ((flags & 3) != 0) {
            for (RRSIGRecord r2 : rrset.sigs()) {
                if (r2.getName().isWild() && !name.isWild()) {
                    r2 = r2.withName(name);
                }
                response.addRecord(r2, section);
            }
        }
    }

    private void addSOA(Message response, Zone zone) {
        response.addRecord(zone.getSOA(), 2);
    }

    private void addNS(Message response, Zone zone, int flags) {
        RRset nsRecords = zone.getNS();
        addRRset(nsRecords.getName(), response, nsRecords, 2, flags);
    }

    private void addCacheNS(Message response, Cache cache, Name name) {
        SetResponse sr = cache.lookupRecords(name, 2, 0);
        if (!sr.isDelegation()) {
            return;
        }
        RRset nsRecords = sr.getNS();
        for (Record r : nsRecords.rrs()) {
            response.addRecord(r, 2);
        }
    }

    private void addGlue(Message response, Name name, int flags) {
        RRset a = findExactMatch(name, 1, 1, true);
        if (a == null) {
            return;
        }
        addRRset(name, response, a, 3, flags);
    }

    private void addAdditional2(Message response, int section, int flags) {
        for (Record r : response.getSection(section)) {
            Name glueName = r.getAdditionalName();
            if (glueName != null) {
                addGlue(response, glueName, flags);
            }
        }
    }

    private void addAdditional(Message response, int flags) {
        addAdditional2(response, 1, flags);
        addAdditional2(response, 2, flags);
    }

    byte addAnswer(Message response, Name name, int type, int dclass, int iterations, int flags) {
        int type2;
        int flags2;
        SetResponse sr;
        if (iterations > 6) {
            return (byte) 0;
        }
        if (type == 24 || type == 46) {
            type2 = 255;
            flags2 = flags | 2;
        } else {
            flags2 = flags;
            type2 = type;
        }
        Zone zone = findBestZone(name);
        if (zone != null) {
            sr = zone.findRecords(name, type2);
        } else {
            Cache cache = getCache(dclass);
            sr = cache.lookupRecords(name, type2, 3);
        }
        if (sr.isUnknown()) {
            addCacheNS(response, getCache(dclass), name);
        }
        if (sr.isNXDOMAIN()) {
            response.getHeader().setRcode(3);
            if (zone != null) {
                addSOA(response, zone);
                if (iterations == 0) {
                    response.getHeader().setFlag(5);
                }
            }
            return (byte) 3;
        }
        if (sr.isNXRRSET()) {
            if (zone == null) {
                return (byte) 0;
            }
            addSOA(response, zone);
            if (iterations != 0) {
                return (byte) 0;
            }
            response.getHeader().setFlag(5);
            return (byte) 0;
        }
        if (sr.isDelegation()) {
            RRset nsRecords = sr.getNS();
            addRRset(nsRecords.getName(), response, nsRecords, 2, flags2);
            return (byte) 0;
        }
        if (sr.isCNAME()) {
            CNAMERecord cname = sr.getCNAME();
            RRset rrset = new RRset(cname);
            addRRset(name, response, rrset, 1, flags2);
            if (zone != null && iterations == 0) {
                response.getHeader().setFlag(5);
            }
            byte rcode = addAnswer(response, cname.getTarget(), type2, dclass, iterations + 1, flags2);
            return rcode;
        }
        Zone zone2 = zone;
        int flags3 = flags2;
        int type3 = type2;
        if (sr.isDNAME()) {
            DNAMERecord dname = sr.getDNAME();
            RRset rrset2 = new RRset(dname);
            addRRset(name, response, rrset2, 1, flags3);
            try {
                Name newname = name.fromDNAME(dname);
                RRset cnamerrset = new RRset(new CNAMERecord(name, dclass, 0L, newname));
                addRRset(name, response, cnamerrset, 1, flags3);
                if (zone2 != null && iterations == 0) {
                    response.getHeader().setFlag(5);
                }
                byte rcode2 = addAnswer(response, newname, type3, dclass, iterations + 1, flags3);
                return rcode2;
            } catch (NameTooLongException e) {
                return (byte) 6;
            }
        }
        int i = dclass;
        if (!sr.isSuccessful()) {
            return (byte) 0;
        }
        List<RRset> rrsets = sr.answers();
        for (RRset rrset3 : rrsets) {
            int flags4 = flags3;
            addRRset(name, response, rrset3, 1, flags4);
            flags3 = flags4;
            zone2 = zone2;
            i = i;
        }
        int flags5 = flags3;
        Zone zone3 = zone2;
        int i2 = i;
        if (zone3 != null) {
            addNS(response, zone3, flags5);
            if (iterations != 0) {
                return (byte) 0;
            }
            response.getHeader().setFlag(5);
            return (byte) 0;
        }
        addCacheNS(response, getCache(i2), name);
        return (byte) 0;
    }

    byte[] doAXFR(Name name, Message query, TSIG tsig, TSIGRecord qtsig, Socket s) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Zone zone = this.znames.get(name);
        boolean first = true;
        int i = 5;
        if (zone == null) {
            return errorMessage(query, 5);
        }
        try {
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
            int id = query.getHeader().getID();
            Iterator<RRset> it = zone.AXFR();
            TSIGRecord qtsig2 = qtsig;
            boolean first2 = true;
            while (true) {
                try {
                    boolean first3 = it.hasNext();
                    if (!first3) {
                        break;
                    }
                    RRset rrset = it.next();
                    Message response = new Message(id);
                    Header header = response.getHeader();
                    header.setFlag(0);
                    header.setFlag(i);
                    boolean first4 = first2;
                    try {
                        addRRset(rrset.getName(), response, rrset, 1, 1);
                        if (tsig != null) {
                            tsig.apply(response, qtsig2, first4);
                            qtsig2 = response.getTSIG();
                        }
                        first2 = false;
                    } catch (IOException e) {
                        first = first4;
                    }
                    try {
                        byte[] out = response.toWire();
                        dataOut.writeShort(out.length);
                        dataOut.write(out);
                        i = 5;
                    } catch (IOException e2) {
                        first = false;
                        System.out.println("AXFR failed");
                        s.close();
                        return null;
                    }
                } catch (IOException e3) {
                    first = first2;
                }
            }
        } catch (IOException e4) {
        }
        try {
            s.close();
            return null;
        } catch (IOException e5) {
            return null;
        }
    }

    byte[] generateReply(Message query, byte[] in, Socket s) {
        TSIG tsig;
        int maxLength;
        int flags;
        int i;
        Header header = query.getHeader();
        if (header.getFlag(0)) {
            return null;
        }
        if (header.getRcode() != 0) {
            return errorMessage(query, 1);
        }
        if (header.getOpcode() != 0) {
            return errorMessage(query, 4);
        }
        Record queryRecord = query.getQuestion();
        TSIGRecord queryTSIG = query.getTSIG();
        if (queryTSIG == null) {
            tsig = null;
        } else {
            TSIG tsig2 = this.tsigs.get(queryTSIG.getName());
            TSIG tsig3 = tsig2;
            if (tsig3 == null || tsig3.verify(query, in, null) != 0) {
                return formerrMessage(in);
            }
            tsig = tsig3;
        }
        OPTRecord queryOPT = query.getOPT();
        if (s != null) {
            maxLength = 65535;
        } else if (queryOPT != null) {
            maxLength = Math.max(queryOPT.getPayloadSize(), 512);
        } else {
            maxLength = 512;
        }
        if (queryOPT != null && (queryOPT.getFlags() & 32768) != 0) {
            flags = 1;
        } else {
            flags = 0;
        }
        Message response = new Message(query.getHeader().getID());
        response.getHeader().setFlag(0);
        if (query.getHeader().getFlag(7)) {
            response.getHeader().setFlag(7);
        }
        response.addRecord(queryRecord, 0);
        Name name = queryRecord.getName();
        int type = queryRecord.getType();
        int dclass = queryRecord.getDClass();
        if (type != 252 || s == null) {
            int flags2 = flags;
            if (!Type.isRR(type) && type != 255) {
                return errorMessage(query, 4);
            }
            int maxLength2 = maxLength;
            byte rcode = addAnswer(response, name, type, dclass, 0, flags2);
            if (rcode == 0 || rcode == 3) {
                addAdditional(response, flags2);
                if (queryOPT == null) {
                    i = 0;
                } else {
                    int optflags = flags2 != 1 ? 0 : 32768;
                    i = 0;
                    OPTRecord opt = new OPTRecord(4096, rcode, 0, optflags);
                    response.addRecord(opt, 3);
                }
                response.setTSIG(tsig, i, queryTSIG);
                return response.toWire(maxLength2);
            }
            return errorMessage(query, rcode);
        }
        return doAXFR(name, query, tsig, queryTSIG, s);
    }

    byte[] buildErrorMessage(Header header, int rcode, Record question) {
        Message response = new Message();
        response.setHeader(header);
        for (int i = 0; i < 4; i++) {
            response.removeAllRecords(i);
        }
        if (rcode == 2) {
            response.addRecord(question, 0);
        }
        header.setRcode(rcode);
        return response.toWire();
    }

    public byte[] formerrMessage(byte[] in) {
        try {
            Header header = new Header(in);
            return buildErrorMessage(header, 1, null);
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] errorMessage(Message query, int rcode) {
        return buildErrorMessage(query.getHeader(), rcode, query.getQuestion());
    }

    /* renamed from: TCPclient, reason: merged with bridge method [inline-methods] */
    public void m1871lambda$serveTCP$0$orgxbillDNStoolsjnamed(Socket s) throws IOException {
        byte[] response;
        try {
            InputStream is = s.getInputStream();
            try {
                DataInputStream dataIn = new DataInputStream(is);
                int inLength = dataIn.readUnsignedShort();
                byte[] in = new byte[inLength];
                dataIn.readFully(in);
                try {
                    Message query = new Message(in);
                    response = generateReply(query, in, s);
                    if (response == null) {
                        if (is != null) {
                            is.close();
                            return;
                        }
                        return;
                    }
                } catch (IOException e) {
                    response = formerrMessage(in);
                }
                DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
                dataOut.writeShort(response.length);
                dataOut.write(response);
                if (is != null) {
                    is.close();
                }
            } finally {
            }
        } catch (IOException e2) {
            System.out.println("TCPclient(" + addrport(s.getLocalAddress(), s.getLocalPort()) + "): " + e2);
        }
    }

    /* renamed from: serveTCP, reason: merged with bridge method [inline-methods] */
    public void m1869lambda$addTCP$0$orgxbillDNStoolsjnamed(InetAddress addr, int port) {
        try {
            ServerSocket sock = new ServerSocket(port, 128, addr);
            while (true) {
                try {
                    final Socket s = sock.accept();
                    Thread t = new Thread(new Runnable() { // from class: org.xbill.DNS.tools.jnamed$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() throws IOException {
                            this.f$0.m1871lambda$serveTCP$0$orgxbillDNStoolsjnamed(s);
                        }
                    });
                    t.start();
                } finally {
                }
            }
        } catch (IOException e) {
            System.out.println("serveTCP(" + addrport(addr, port) + "): " + e);
        }
    }

    /* renamed from: serveUDP, reason: merged with bridge method [inline-methods] */
    public void m1870lambda$addUDP$0$orgxbillDNStoolsjnamed(InetAddress addr, int port) {
        byte[] response;
        try {
            DatagramSocket sock = new DatagramSocket(port, addr);
            try {
                byte[] in = new byte[512];
                DatagramPacket datagramPacket = new DatagramPacket(in, in.length);
                DatagramPacket outdp = null;
                while (true) {
                    datagramPacket.setLength(in.length);
                    try {
                        sock.receive(datagramPacket);
                        try {
                            Message query = new Message(in);
                            response = generateReply(query, in, null);
                        } catch (IOException e) {
                            response = formerrMessage(in);
                        }
                    } catch (InterruptedIOException e2) {
                    }
                    if (response != null) {
                        if (outdp == null) {
                            outdp = new DatagramPacket(response, response.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        } else {
                            outdp.setData(response);
                            outdp.setLength(response.length);
                            outdp.setAddress(datagramPacket.getAddress());
                            outdp.setPort(datagramPacket.getPort());
                        }
                        sock.send(outdp);
                    }
                }
            } finally {
            }
        } catch (IOException e3) {
            System.out.println("serveUDP(" + addrport(addr, port) + "): " + e3);
        }
    }

    public void addTCP(final InetAddress addr, final int port) {
        Thread t = new Thread(new Runnable() { // from class: org.xbill.DNS.tools.jnamed$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1869lambda$addTCP$0$orgxbillDNStoolsjnamed(addr, port);
            }
        });
        t.start();
    }

    public void addUDP(final InetAddress addr, final int port) {
        Thread t = new Thread(new Runnable() { // from class: org.xbill.DNS.tools.jnamed$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m1870lambda$addUDP$0$orgxbillDNStoolsjnamed(addr, port);
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        String conf;
        if (args.length > 1) {
            System.out.println("usage: jnamed [conf]");
            System.exit(0);
        }
        try {
            if (args.length == 1) {
                conf = args[0];
            } else {
                conf = "jnamed.conf";
            }
            new jnamed(conf);
        } catch (IOException | ZoneTransferException e) {
            System.out.println(e);
        }
    }
}
