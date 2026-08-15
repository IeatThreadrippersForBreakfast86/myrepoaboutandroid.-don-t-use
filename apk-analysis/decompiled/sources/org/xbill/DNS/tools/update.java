package org.xbill.DNS.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TTL;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;

/* loaded from: classes8.dex */
public class update {
    int defaultClass;
    long defaultTTL;
    PrintStream log = null;
    Message query;
    Resolver res;
    Message response;
    String server;
    Name zone;

    void print(Object o) {
        System.out.println(o);
        if (this.log != null) {
            this.log.println(o);
        }
    }

    public Message newMessage() {
        Message msg = new Message();
        msg.getHeader().setOpcode(5);
        return msg;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public update(InputStream in) throws IOException {
        String operation;
        String str = null;
        this.server = null;
        this.zone = Name.root;
        this.defaultClass = 1;
        List<BufferedReader> inputs = new LinkedList<>();
        List<InputStream> istreams = new LinkedList<>();
        this.query = newMessage();
        InputStreamReader isr = new InputStreamReader(in);
        inputs.add(new BufferedReader(isr));
        istreams.add(in);
        while (true) {
            try {
                InputStream is = istreams.get(0);
                BufferedReader br = inputs.get(0);
                if (is == System.in) {
                    System.out.print("> ");
                }
                String line = br.readLine();
                if (line == null) {
                    br.close();
                    inputs.remove(0);
                    istreams.remove(0);
                    if (inputs.isEmpty()) {
                        return;
                    }
                }
                if (line != null) {
                    if (this.log != null) {
                        this.log.println("> " + line);
                    }
                    if (!line.isEmpty() && line.charAt(0) != '#') {
                        line = line.charAt(0) == '>' ? line.substring(1) : line;
                        Tokenizer st = new Tokenizer(line);
                        try {
                            Tokenizer.Token token = st.get();
                            if (!token.isEOL()) {
                                operation = token.value();
                                switch (operation) {
                                    case "server":
                                        String keyname = st.getString();
                                        this.server = keyname;
                                        this.res = new SimpleResolver(this.server);
                                        Tokenizer.Token token2 = st.get();
                                        if (token2.isString()) {
                                            String portstr = token2.value();
                                            this.res.setPort(Short.parseShort(portstr));
                                        }
                                        st.close();
                                        break;
                                    case "key":
                                        String keyname2 = st.getString();
                                        String keydata = st.getString();
                                        if (this.res == null) {
                                            this.res = new SimpleResolver(this.server);
                                        }
                                        this.res.setTSIGKey(new TSIG(TSIG.HMAC_MD5, keyname2, keydata));
                                        st.close();
                                        break;
                                    case "edns":
                                        if (this.res == null) {
                                            this.res = new SimpleResolver(this.server);
                                        }
                                        this.res.setEDNS(st.getUInt16());
                                        st.close();
                                        break;
                                    case "port":
                                        if (this.res == null) {
                                            this.res = new SimpleResolver(this.server);
                                        }
                                        this.res.setPort(st.getUInt16());
                                        st.close();
                                        break;
                                    case "tcp":
                                        if (this.res == null) {
                                            this.res = new SimpleResolver(this.server);
                                        }
                                        this.res.setTCP(true);
                                        st.close();
                                        break;
                                    case "class":
                                        String classStr = st.getString();
                                        int newClass = DClass.value(classStr);
                                        if (newClass <= 0) {
                                            print("Invalid class " + classStr);
                                        } else {
                                            this.defaultClass = newClass;
                                        }
                                        st.close();
                                        break;
                                    case "ttl":
                                        this.defaultTTL = st.getTTL();
                                        st.close();
                                        break;
                                    case "origin":
                                    case "zone":
                                        this.zone = st.getName(Name.root);
                                        st.close();
                                        break;
                                    case "require":
                                        doRequire(st);
                                        st.close();
                                        break;
                                    case "prohibit":
                                        doProhibit(st);
                                        st.close();
                                        break;
                                    case "add":
                                        doAdd(st);
                                        st.close();
                                        break;
                                    case "delete":
                                        doDelete(st);
                                        st.close();
                                        break;
                                    case "glue":
                                        doGlue(st);
                                        st.close();
                                        break;
                                    case "help":
                                    case "?":
                                        Tokenizer.Token token3 = st.get();
                                        if (token3.isString()) {
                                            help(token3.value());
                                        } else {
                                            help(str);
                                        }
                                        st.close();
                                        break;
                                    case "echo":
                                        print(line.substring(4).trim());
                                        st.close();
                                        break;
                                    case "send":
                                        sendUpdate();
                                        this.query = newMessage();
                                        st.close();
                                        break;
                                    case "show":
                                        print(this.query);
                                        st.close();
                                        break;
                                    case "clear":
                                        this.query = newMessage();
                                        st.close();
                                        break;
                                    case "query":
                                        doQuery(st);
                                        st.close();
                                        break;
                                    case "quit":
                                    case "q":
                                        if (this.log != null) {
                                            this.log.close();
                                        }
                                        for (BufferedReader input : inputs) {
                                            input.close();
                                        }
                                        System.exit(0);
                                        st.close();
                                        break;
                                    case "file":
                                        doFile(st, inputs, istreams);
                                        st.close();
                                        break;
                                    case "log":
                                        doLog(st);
                                        st.close();
                                        break;
                                    case "assert":
                                        if (!doAssert(st)) {
                                            st.close();
                                            return;
                                        }
                                        st.close();
                                        break;
                                    case "sleep":
                                        long interval = st.getUInt32();
                                        try {
                                            Thread.sleep(interval);
                                            st.close();
                                            break;
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            throw new IOException(e);
                                        }
                                    case "date":
                                        Instant now = Instant.now();
                                        Tokenizer.Token token4 = st.get();
                                        if (token4.isString() && token4.value().equals("-ms")) {
                                            print(Long.toString(now.toEpochMilli()));
                                        } else {
                                            print(now);
                                        }
                                        st.close();
                                        break;
                                    default:
                                        print("invalid keyword: " + operation);
                                        st.close();
                                        break;
                                }
                            } else {
                                st.close();
                            }
                        } finally {
                        }
                    }
                }
                str = null;
            } catch (InterruptedIOException e2) {
                System.out.println("Operation timed out");
                str = null;
            } catch (SocketException e3) {
                System.out.println("Socket error");
                str = null;
            } catch (TextParseException tpe) {
                System.out.println(tpe.getMessage());
                str = null;
            } catch (IOException ioe) {
                System.out.println(ioe);
                str = null;
            }
        }
    }

    void sendUpdate() throws IOException {
        if (this.query.getHeader().getCount(2) == 0) {
            print("Empty update message.  Ignoring.");
            return;
        }
        if (this.query.getHeader().getCount(0) == 0) {
            Name updzone = this.zone;
            int dclass = this.defaultClass;
            if (updzone == null) {
                Iterator<Record> it = this.query.getSection(2).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Record rec = it.next();
                    if (updzone == null) {
                        updzone = new Name(rec.getName(), 1);
                    }
                    if (rec.getDClass() != 254 && rec.getDClass() != 255) {
                        dclass = rec.getDClass();
                        break;
                    }
                }
            }
            Record soa = Record.newRecord(updzone, 6, dclass);
            this.query.addRecord(soa, 0);
        }
        if (this.res == null) {
            this.res = new SimpleResolver(this.server);
        }
        this.response = this.res.send(this.query);
        print(this.response);
    }

    Record parseRR(Tokenizer st, int classValue, long TTLValue) throws IOException {
        long ttl;
        String s;
        Name name = st.getName(this.zone);
        String s2 = st.getString();
        try {
            long ttl2 = TTL.parseTTL(s2);
            s2 = st.getString();
            ttl = ttl2;
        } catch (NumberFormatException e) {
            ttl = TTLValue;
        }
        if (DClass.value(s2) < 0) {
            s = s2;
        } else {
            classValue = DClass.value(s2);
            s = st.getString();
        }
        int type = Type.value(s);
        if (type < 0) {
            throw new IOException("Invalid type: " + s);
        }
        return Record.fromString(name, type, classValue, ttl, st, this.zone);
    }

    void doRequire(Tokenizer st) throws IOException {
        Record r;
        Name name = st.getName(this.zone);
        Tokenizer.Token token = st.get();
        if (token.isString()) {
            int type = Type.value(token.value());
            if (type < 0) {
                throw new IOException("Invalid type: " + token.value());
            }
            boolean iseol = st.get().isEOL();
            st.unget();
            if (!iseol) {
                r = Record.fromString(name, type, this.defaultClass, 0L, st, this.zone);
            } else {
                r = Record.newRecord(name, type, 255, 0L);
            }
        } else {
            r = Record.newRecord(name, 255, 255, 0L);
        }
        this.query.addRecord(r, 1);
        print(r);
    }

    void doProhibit(Tokenizer st) throws IOException {
        int type;
        Name name = st.getName(this.zone);
        Tokenizer.Token token = st.get();
        if (token.isString()) {
            int iValue = Type.value(token.value());
            type = iValue;
            if (iValue < 0) {
                throw new IOException("Invalid type: " + token.value());
            }
        } else {
            type = 255;
        }
        Record r = Record.newRecord(name, type, 254, 0L);
        this.query.addRecord(r, 1);
        print(r);
    }

    void doAdd(Tokenizer st) throws IOException {
        Record r = parseRR(st, this.defaultClass, this.defaultTTL);
        this.query.addRecord(r, 2);
        print(r);
    }

    void doDelete(Tokenizer st) throws IOException {
        Record r;
        String s;
        Name name = st.getName(this.zone);
        Tokenizer.Token token = st.get();
        if (token.isString()) {
            String s2 = token.value();
            if (DClass.value(s2) < 0) {
                s = s2;
            } else {
                s = st.getString();
            }
            int type = Type.value(s);
            if (type < 0) {
                throw new IOException("Invalid type: " + s);
            }
            boolean iseol = st.get().isEOL();
            st.unget();
            if (!iseol) {
                r = Record.fromString(name, type, 254, 0L, st, this.zone);
            } else {
                r = Record.newRecord(name, type, 255, 0L);
            }
        } else {
            r = Record.newRecord(name, 255, 255, 0L);
        }
        this.query.addRecord(r, 2);
        print(r);
    }

    void doGlue(Tokenizer st) throws IOException {
        Record r = parseRR(st, this.defaultClass, this.defaultTTL);
        this.query.addRecord(r, 3);
        print(r);
    }

    void doQuery(Tokenizer st) throws IOException {
        int type = 1;
        int dclass = this.defaultClass;
        Name name = st.getName(this.zone);
        Tokenizer.Token token = st.get();
        if (token.isString()) {
            type = Type.value(token.value());
            if (type < 0) {
                throw new IOException("Invalid type");
            }
            Tokenizer.Token token2 = st.get();
            if (token2.isString() && (dclass = DClass.value(token2.value())) < 0) {
                throw new IOException("Invalid class");
            }
        }
        Record rec = Record.newRecord(name, type, dclass);
        Message newQuery = Message.newQuery(rec);
        if (this.res == null) {
            this.res = new SimpleResolver(this.server);
        }
        this.response = this.res.send(newQuery);
        print(this.response);
    }

    void doFile(Tokenizer st, List<BufferedReader> inputs, List<InputStream> istreams) throws IOException {
        InputStream is;
        String s = st.getString();
        try {
            if (s.equals("-")) {
                is = System.in;
            } else {
                is = new FileInputStream(s);
            }
            istreams.add(0, is);
            inputs.add(0, new BufferedReader(new InputStreamReader(is)));
        } catch (FileNotFoundException e) {
            print(s + " not found");
        }
    }

    void doLog(Tokenizer st) throws IOException {
        String s = st.getString();
        try {
            FileOutputStream fos = new FileOutputStream(s);
            try {
                this.log = new PrintStream(fos);
                fos.close();
            } finally {
            }
        } catch (Exception e) {
            print("Error opening " + s);
        }
    }

    boolean doAssert(Tokenizer st) throws IOException {
        String field = st.getString();
        String expected = st.getString();
        String value = null;
        boolean flag = true;
        if (this.response == null) {
            print("No response has been received");
            return true;
        }
        if (field.equalsIgnoreCase("rcode")) {
            int rcode = this.response.getHeader().getRcode();
            if (rcode != Rcode.value(expected)) {
                value = Rcode.string(rcode);
                flag = false;
            }
        } else if (field.equalsIgnoreCase("serial")) {
            List<Record> answers = this.response.getSection(1);
            if (answers.isEmpty() || !(answers.get(0) instanceof SOARecord)) {
                print("Invalid response (no SOA)");
            } else {
                SOARecord soa = (SOARecord) answers.get(0);
                long serial = soa.getSerial();
                if (serial != Long.parseLong(expected)) {
                    value = Long.toString(serial);
                    flag = false;
                }
            }
        } else if (field.equalsIgnoreCase("tsig")) {
            if (this.response.isSigned()) {
                if (this.response.isVerified()) {
                    value = "ok";
                } else {
                    value = "failed";
                }
            } else {
                value = "unsigned";
            }
            if (!value.equalsIgnoreCase(expected)) {
                flag = false;
            }
        } else {
            int section = Section.value(field);
            if (section >= 0) {
                int count = this.response.getHeader().getCount(section);
                if (count != Integer.parseInt(expected)) {
                    value = Integer.toString(count);
                    flag = false;
                }
            } else {
                print("Invalid assertion keyword: " + field);
            }
        }
        if (!flag) {
            print("Expected " + field + " " + expected + ", received " + value);
            while (true) {
                Tokenizer.Token token = st.get();
                if (!token.isString()) {
                    break;
                }
                print(token.value());
            }
            st.unget();
        }
        return flag;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:93:0x015a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void help(String topic) {
        String topic2;
        System.out.println();
        if (topic == null) {
            System.out.println("The following are supported commands:\nadd      assert   class    clear    date     delete\necho     edns     file     glue     help     key\nlog      port     prohibit query    quit     require\nsend     server   show     sleep    tcp      ttl\nzone     #\n");
        }
        topic2 = topic.toLowerCase();
        switch (topic2) {
            case "add":
                System.out.println("add <name> [ttl] [class] <type> <data>\n\nspecify a record to be added\n");
                break;
            case "assert":
                System.out.println("assert <field> <value> [msg]\n\nasserts that the value of the field in the last\nresponse matches the value specified.  If not,\nthe message is printed (if present) and the\nprogram exits.  The field may be any of <rcode>,\n<serial>, <tsig>, <qu>, <an>, <au>, or <ad>.\n");
                break;
            case "class":
                System.out.println("class <class>\n\nclass of the zone to be updated (default: IN)\n");
                break;
            case "clear":
                System.out.println("clear\n\nclears the current update packet\n");
                break;
            case "date":
                System.out.println("date [-ms]\n\nprints the current date and time in human readable\nformat or as the number of milliseconds since the\nepoch");
                break;
            case "delete":
                System.out.println("delete <name> [ttl] [class] <type> <data> \ndelete <name> <type> \ndelete <name>\n\nspecify a record or set to be deleted, or that\nall records at a name should be deleted\n");
                break;
            case "echo":
                System.out.println("echo <text>\n\nprints the text\n");
                break;
            case "edns":
                System.out.println("edns <level>\n\nEDNS level specified when sending messages\n");
                break;
            case "file":
                System.out.println("file <file>\n\nopens the specified file as the new input source\n(- represents stdin)\n");
                break;
            case "glue":
                System.out.println("glue <name> [ttl] [class] <type> <data>\n\nspecify an additional record\n");
                break;
            case "help":
                System.out.println("help\nhelp [topic]\n\nprints a list of commands or help about a specific\ncommand\n");
                break;
            case "key":
                System.out.println("key <name> <data>\n\nTSIG key used to sign messages\n");
                break;
            case "log":
                System.out.println("log <file>\n\nopens the specified file and uses it to log output\n");
                break;
            case "port":
                System.out.println("port <port>\n\nUDP/TCP port messages are sent to (default: 53)\n");
                break;
            case "prohibit":
                System.out.println("prohibit <name> <type> \nprohibit <name>\n\nrequire that a set or name is not present\n");
                break;
            case "query":
                System.out.println("query <name> [type [class]] \n\nissues a query\n");
                break;
            case "q":
            case "quit":
                System.out.println("quit\n\nquits the program\n");
                break;
            case "require":
                System.out.println("require <name> [ttl] [class] <type> <data> \nrequire <name> <type> \nrequire <name>\n\nrequire that a record, set, or name is present\n");
                break;
            case "send":
                System.out.println("send\n\nsends and resets the current update packet\n");
                break;
            case "server":
                System.out.println("server <name> [port]\n\nserver that receives send updates/queries\n");
                break;
            case "show":
                System.out.println("show\n\nshows the current update packet\n");
                break;
            case "sleep":
                System.out.println("sleep <milliseconds>\n\npause for interval before next command\n");
                break;
            case "tcp":
                System.out.println("tcp\n\nTCP should be used to send all messages\n");
                break;
            case "ttl":
                System.out.println("ttl <ttl>\n\ndefault ttl of added records (default: 0)\n");
                break;
            case "zone":
            case "origin":
                System.out.println("zone <zone>\n\nzone to update (default: .\n");
                break;
            case "#":
                System.out.println("# <text>\n\na comment\n");
                break;
            default:
                System.out.println("Topic '" + topic2 + "' unrecognized\n");
                break;
        }
    }

    public static void main(String[] args) {
        InputStream in = null;
        if (args.length >= 1) {
            try {
                in = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                System.out.println(args[0] + " not found.");
                System.exit(1);
            }
        } else {
            in = System.in;
        }
        new update(in);
    }
}
