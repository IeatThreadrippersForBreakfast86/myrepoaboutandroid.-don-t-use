package org.xbill.DNS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.TSIG;

/* loaded from: classes8.dex */
public class ZoneTransferIn {
    private static final int AXFR = 6;
    private static final int END = 7;
    private static final int FIRSTDATA = 1;
    private static final int INITIALSOA = 0;
    private static final int IXFR_ADD = 5;
    private static final int IXFR_ADDSOA = 4;
    private static final int IXFR_DEL = 3;
    private static final int IXFR_DELSOA = 2;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ZoneTransferIn.class);
    private final SocketAddress address;
    private TCPClient client;
    private long currentSerial;
    private int dclass;
    private long endSerial;
    private ZoneTransferHandler handler;
    private Record initialSoaRecord;
    private final long ixfrSerial;
    private SocketAddress localAddress;
    private int qtype;
    private int rtype;
    private int state;
    private Duration timeout = Duration.ofMinutes(15);
    private final TSIG tsig;
    private TSIG.StreamVerifier verifier;
    private final boolean wantFallback;
    private final Name zname;

    public interface ZoneTransferHandler {
        void handleRecord(Record record) throws ZoneTransferException;

        void startAXFR() throws ZoneTransferException;

        void startIXFR() throws ZoneTransferException;

        void startIXFRAdds(Record record) throws ZoneTransferException;

        void startIXFRDeletes(Record record) throws ZoneTransferException;
    }

    public static class Delta {
        public List<Record> adds;
        public List<Record> deletes;
        public long end;
        public long start;

        private Delta() {
            this.adds = new ArrayList();
            this.deletes = new ArrayList();
        }
    }

    private static class BasicHandler implements ZoneTransferHandler {
        private List<Record> axfr;
        private List<Delta> ixfr;

        private BasicHandler() {
        }

        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
        public void startAXFR() {
            this.axfr = new ArrayList();
        }

        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
        public void startIXFR() {
            this.ixfr = new ArrayList();
        }

        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
        public void startIXFRDeletes(Record soa) {
            Delta delta = new Delta();
            delta.deletes.add(soa);
            delta.start = ZoneTransferIn.getSOASerial(soa);
            this.ixfr.add(delta);
        }

        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
        public void startIXFRAdds(Record soa) {
            Delta delta = this.ixfr.get(this.ixfr.size() - 1);
            delta.adds.add(soa);
            delta.end = ZoneTransferIn.getSOASerial(soa);
        }

        @Override // org.xbill.DNS.ZoneTransferIn.ZoneTransferHandler
        public void handleRecord(Record r) {
            if (this.ixfr != null) {
                Delta delta = this.ixfr.get(this.ixfr.size() - 1);
                if (!delta.adds.isEmpty()) {
                    delta.adds.add(r);
                    return;
                } else {
                    delta.deletes.add(r);
                    return;
                }
            }
            this.axfr.add(r);
        }
    }

    ZoneTransferIn(Name zone, int xfrtype, long serial, boolean fallback, SocketAddress address, TSIG key) {
        this.address = address;
        this.tsig = key;
        if (zone.isAbsolute()) {
            this.zname = zone;
        } else {
            try {
                this.zname = Name.concatenate(zone, Name.root);
            } catch (NameTooLongException e) {
                throw new IllegalArgumentException("ZoneTransferIn: name too long");
            }
        }
        this.qtype = xfrtype;
        this.dclass = 1;
        this.ixfrSerial = serial;
        this.wantFallback = fallback;
        this.state = 0;
    }

    public static ZoneTransferIn newAXFR(Name zone, SocketAddress address, TSIG key) {
        return new ZoneTransferIn(zone, 252, 0L, false, address, key);
    }

    public static ZoneTransferIn newAXFR(Name zone, String host, int port, TSIG key) {
        if (port == 0) {
            port = 53;
        }
        return newAXFR(zone, new InetSocketAddress(host, port), key);
    }

    public static ZoneTransferIn newAXFR(Name zone, String host, TSIG key) {
        return newAXFR(zone, host, 0, key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, SocketAddress address, TSIG key) {
        return new ZoneTransferIn(zone, Type.IXFR, serial, fallback, address, key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, String host, int port, TSIG key) {
        if (port == 0) {
            port = 53;
        }
        return newIXFR(zone, serial, fallback, new InetSocketAddress(host, port), key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, String host, TSIG key) {
        return newIXFR(zone, serial, fallback, host, 0, key);
    }

    public Name getName() {
        return this.zname;
    }

    public int getType() {
        return this.qtype;
    }

    @Deprecated
    public void setTimeout(int secs) {
        if (secs < 0) {
            throw new IllegalArgumentException("timeout cannot be negative");
        }
        this.timeout = Duration.ofSeconds(secs);
    }

    public void setTimeout(Duration t) {
        this.timeout = t;
    }

    public void setDClass(int dclass) {
        DClass.check(dclass);
        this.dclass = dclass;
    }

    public void setLocalAddress(SocketAddress addr) {
        this.localAddress = addr;
    }

    private void openConnection() throws IOException {
        this.client = createTcpClient(this.timeout);
        if (this.localAddress != null) {
            this.client.bind(this.localAddress);
        }
        this.client.connect(this.address);
    }

    TCPClient createTcpClient(Duration timeout) throws IOException {
        return new TCPClient(timeout);
    }

    private void sendQuery() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Record question = Record.newRecord(this.zname, this.qtype, this.dclass);
        Message query = new Message();
        query.getHeader().setOpcode(0);
        query.addRecord(question, 0);
        if (this.qtype == 251) {
            Record soa = new SOARecord(this.zname, this.dclass, 0L, Name.root, Name.root, this.ixfrSerial, 0L, 0L, 0L, 0L);
            query.addRecord(soa, 2);
        }
        if (this.tsig != null) {
            this.tsig.apply(query, null);
            this.verifier = new TSIG.StreamVerifier(this.tsig, query.getTSIG());
        }
        byte[] out = query.toWire(65535);
        this.client.send(out);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getSOASerial(Record rec) {
        SOARecord soa = (SOARecord) rec;
        return soa.getSerial();
    }

    private void logxfr(String s) {
        log.debug("{}: {}", this.zname, s);
    }

    private void fail(String s) throws ZoneTransferException {
        throw new ZoneTransferException(s);
    }

    private void fallback() throws ZoneTransferException {
        if (!this.wantFallback) {
            fail("server doesn't support IXFR");
        }
        logxfr("falling back to AXFR");
        this.qtype = 252;
        this.state = 0;
    }

    private void parseRR(Record rec) throws ZoneTransferException {
        int type = rec.getType();
        switch (this.state) {
            case 0:
                if (type != 6) {
                    fail("missing initial SOA");
                }
                this.initialSoaRecord = rec;
                this.endSerial = getSOASerial(rec);
                if (this.qtype == 251 && Serial.compare(this.endSerial, this.ixfrSerial) <= 0) {
                    logxfr("up to date");
                    this.state = 7;
                    break;
                } else {
                    this.state = 1;
                    break;
                }
                break;
            case 1:
                if (this.qtype == 251 && type == 6 && getSOASerial(rec) == this.ixfrSerial) {
                    this.rtype = Type.IXFR;
                    this.handler.startIXFR();
                    logxfr("got incremental response");
                    this.state = 2;
                } else {
                    this.rtype = 252;
                    this.handler.startAXFR();
                    this.handler.handleRecord(this.initialSoaRecord);
                    logxfr("got nonincremental response");
                    this.state = 6;
                }
                parseRR(rec);
                break;
            case 2:
                this.handler.startIXFRDeletes(rec);
                this.state = 3;
                break;
            case 3:
                if (type == 6) {
                    this.currentSerial = getSOASerial(rec);
                    this.state = 4;
                    parseRR(rec);
                    break;
                } else {
                    this.handler.handleRecord(rec);
                    break;
                }
            case 4:
                this.handler.startIXFRAdds(rec);
                this.state = 5;
                break;
            case 5:
                if (type == 6) {
                    long soa_serial = getSOASerial(rec);
                    if (soa_serial != this.endSerial) {
                        if (soa_serial != this.currentSerial) {
                            fail("IXFR out of sync: expected serial " + this.currentSerial + " , got " + soa_serial);
                        } else {
                            this.state = 2;
                            parseRR(rec);
                            break;
                        }
                    } else {
                        this.state = 7;
                        break;
                    }
                }
                this.handler.handleRecord(rec);
                break;
            case 6:
                if (type != 1 || rec.getDClass() == this.dclass) {
                    this.handler.handleRecord(rec);
                    if (type == 6) {
                        this.state = 7;
                        break;
                    }
                }
                break;
            case 7:
                fail("extra data");
                break;
            default:
                fail("invalid state");
                break;
        }
    }

    private void closeConnection() {
        try {
            if (this.client != null) {
                this.client.close();
            }
        } catch (IOException e) {
        }
    }

    private Message parseMessage(byte[] b) throws WireParseException {
        try {
            return new Message(b);
        } catch (IOException e) {
            if (e instanceof WireParseException) {
                throw ((WireParseException) e);
            }
            throw new WireParseException("Error parsing message", e);
        }
    }

    private void doxfr() throws ZoneTransferException, IllegalStateException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        sendQuery();
        while (this.state != 7) {
            byte[] in = this.client.recv();
            Message response = parseMessage(in);
            List<Record> answers = response.getSection(1);
            if (response.getHeader().getRcode() == 0 && this.verifier != null) {
                int error = this.verifier.verify(response, in, answers.get(answers.size() - 1).getType() == 6);
                if (error != 0) {
                    if (this.verifier.getErrorMessage() != null) {
                        fail("TSIG failure: " + Rcode.TSIGstring(error) + " (" + this.verifier.getErrorMessage() + ")");
                    } else {
                        fail("TSIG failure: " + Rcode.TSIGstring(error));
                    }
                }
            }
            if (this.state == 0) {
                int rcode = response.getRcode();
                if (rcode != 0) {
                    if (this.qtype == 251 && rcode == 4) {
                        fallback();
                        doxfr();
                        return;
                    }
                    fail(Rcode.string(rcode));
                }
                Record question = response.getQuestion();
                if (question != null && question.getType() != this.qtype) {
                    fail("invalid question section");
                }
                if (answers.isEmpty() && this.qtype == 251) {
                    fallback();
                    doxfr();
                    return;
                }
            }
            for (Record answer : answers) {
                parseRR(answer);
            }
        }
    }

    public void run(ZoneTransferHandler handler) throws ZoneTransferException, IOException {
        this.handler = handler;
        try {
            openConnection();
            doxfr();
        } finally {
            closeConnection();
        }
    }

    public void run() throws ZoneTransferException, IOException {
        BasicHandler basicHandler = new BasicHandler();
        run(basicHandler);
    }

    private BasicHandler getBasicHandler() throws IllegalArgumentException {
        if (this.handler instanceof BasicHandler) {
            return (BasicHandler) this.handler;
        }
        throw new IllegalArgumentException("ZoneTransferIn used callback interface");
    }

    public boolean isAXFR() {
        return this.rtype == 252;
    }

    public List<Record> getAXFR() throws IllegalArgumentException {
        BasicHandler basicHandler = getBasicHandler();
        return basicHandler.axfr;
    }

    public boolean isIXFR() {
        return this.rtype == 251;
    }

    public List<Delta> getIXFR() throws IllegalArgumentException {
        BasicHandler basicHandler = getBasicHandler();
        return basicHandler.ixfr;
    }

    public boolean isCurrent() throws IllegalArgumentException {
        BasicHandler basicHandler = getBasicHandler();
        return basicHandler.axfr == null && basicHandler.ixfr == null;
    }
}
