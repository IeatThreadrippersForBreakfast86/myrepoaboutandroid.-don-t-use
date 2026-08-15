package org.xbill.DNS;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes8.dex */
public class Cache {
    private static final int DEFAULT_MAX_ENTRIES = 50000;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Cache.class);
    private final CacheMap data;
    private final int dclass;
    private int maxcache;
    private int maxncache;

    private interface Element {
        int compareCredibility(int i);

        boolean expired();

        int getType();

        boolean isAuthenticated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int limitExpire(long ttl, long maxttl) {
        if (maxttl >= 0 && maxttl < ttl) {
            ttl = maxttl;
        }
        long expire = (System.currentTimeMillis() / 1000) + ttl;
        if (expire < 0 || expire > TTL.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) expire;
    }

    static class CacheRRset extends RRset implements Element {
        int credibility;
        int expire;
        boolean isAuthenticated;

        public CacheRRset(Record rec, int cred, long maxttl, boolean isAuthenticated) {
            this.credibility = cred;
            this.expire = Cache.limitExpire(rec.getTTL(), maxttl);
            this.isAuthenticated = isAuthenticated;
            addRR(rec);
        }

        public CacheRRset(RRset rrset, int cred, long maxttl, boolean isAuthenticated) {
            super(rrset);
            this.credibility = cred;
            this.expire = Cache.limitExpire(rrset.getTTL(), maxttl);
            this.isAuthenticated = isAuthenticated;
        }

        @Override // org.xbill.DNS.Cache.Element
        public final boolean expired() {
            int now = (int) (System.currentTimeMillis() / 1000);
            return now >= this.expire;
        }

        @Override // org.xbill.DNS.Cache.Element
        public final int compareCredibility(int cred) {
            return this.credibility - cred;
        }

        @Override // org.xbill.DNS.RRset
        public String toString() {
            return super.toString() + " cl = " + this.credibility;
        }

        @Override // org.xbill.DNS.Cache.Element
        public boolean isAuthenticated() {
            return this.isAuthenticated;
        }
    }

    private static class NegativeElement implements Element {
        int credibility;
        int expire;
        boolean isAuthenticated;
        Name name;
        int type;

        public NegativeElement(Name name, int type, SOARecord soa, int cred, long maxttl, boolean isAuthenticated) {
            this.name = name;
            this.type = type;
            long cttl = soa != null ? Math.min(soa.getMinimum(), soa.getTTL()) : 0L;
            this.credibility = cred;
            this.expire = Cache.limitExpire(cttl, maxttl);
            this.isAuthenticated = isAuthenticated;
        }

        @Override // org.xbill.DNS.Cache.Element
        public int getType() {
            return this.type;
        }

        @Override // org.xbill.DNS.Cache.Element
        public final boolean expired() {
            int now = (int) (System.currentTimeMillis() / 1000);
            return now >= this.expire;
        }

        @Override // org.xbill.DNS.Cache.Element
        public final int compareCredibility(int cred) {
            return this.credibility - cred;
        }

        @Override // org.xbill.DNS.Cache.Element
        public boolean isAuthenticated() {
            return this.isAuthenticated;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.type == 0) {
                sb.append("NXDOMAIN ").append(this.name);
            } else {
                sb.append("NXRRSET ").append(this.name).append(" ").append(Type.string(this.type));
            }
            sb.append(" cl = ");
            sb.append(this.credibility);
            return sb.toString();
        }
    }

    private static class CacheMap extends LinkedHashMap<Name, Object> {
        private int maxsize;

        CacheMap(int maxsize) {
            super(16, 0.75f, true);
            this.maxsize = maxsize;
        }

        int getMaxSize() {
            return this.maxsize;
        }

        void setMaxSize(int maxsize) {
            this.maxsize = maxsize;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Name, Object> eldest) {
            return this.maxsize >= 0 && size() > this.maxsize;
        }
    }

    public Cache(int dclass) {
        this.maxncache = -1;
        this.maxcache = -1;
        this.dclass = dclass;
        this.data = new CacheMap(DEFAULT_MAX_ENTRIES);
    }

    public Cache() {
        this(1);
    }

    public Cache(String file) throws IOException {
        this(new Master(file));
    }

    public Cache(InputStream input) throws IOException {
        this(new Master(input));
    }

    private <T> Cache(Master m) throws IOException {
        this();
        while (true) {
            try {
                Record r = m.nextRecord();
                if (r != null) {
                    addRecord(r, 0);
                } else {
                    return;
                }
            } finally {
                m.close();
            }
        }
    }

    private synchronized Object exactName(Name name) {
        return this.data.get(name);
    }

    private synchronized void removeName(Name name) {
        this.data.remove(name);
    }

    private synchronized Element[] allElements(Object types) {
        if (types instanceof List) {
            List<Element> typelist = (List) types;
            int size = typelist.size();
            return (Element[]) typelist.toArray(new Element[size]);
        }
        Element set = (Element) types;
        return new Element[]{set};
    }

    private synchronized Element oneElement(Name name, Object types, int type, int minCred) {
        Element found = null;
        if (type == 255) {
            throw new IllegalArgumentException("oneElement(ANY)");
        }
        if (types instanceof List) {
            List<Element> list = (List) types;
            Iterator<Element> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Element set = it.next();
                if (set.getType() == type) {
                    found = set;
                    break;
                }
            }
        } else {
            Element set2 = (Element) types;
            if (set2.getType() == type) {
                found = set2;
            }
        }
        if (found == null) {
            return null;
        }
        if (found.expired()) {
            removeElement(name, type);
            return null;
        }
        if (found.compareCredibility(minCred) < 0) {
            return null;
        }
        return found;
    }

    private synchronized Element findElement(Name name, int type, int minCred) {
        Object types = exactName(name);
        if (types == null) {
            return null;
        }
        return oneElement(name, types, type, minCred);
    }

    private synchronized void addElement(Name name, Element element) {
        Object types = this.data.get(name);
        if (types == null) {
            this.data.put(name, element);
            return;
        }
        int type = element.getType();
        if (types instanceof List) {
            List<Element> list = (List) types;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == type) {
                    list.set(i, element);
                    return;
                }
            }
            list.add(element);
        } else {
            Element elt = (Element) types;
            if (elt.getType() == type) {
                this.data.put(name, element);
            } else {
                LinkedList<Element> list2 = new LinkedList<>();
                list2.add(elt);
                list2.add(element);
                this.data.put(name, list2);
            }
        }
    }

    private synchronized void removeElement(Name name, int type) {
        Object types = this.data.get(name);
        if (types == null) {
            return;
        }
        if (types instanceof List) {
            List<Element> list = (List) types;
            for (int i = 0; i < list.size(); i++) {
                Element elt = list.get(i);
                if (elt.getType() == type) {
                    list.remove(i);
                    if (list.isEmpty()) {
                        this.data.remove(name);
                    }
                    return;
                }
            }
        } else {
            Element elt2 = (Element) types;
            if (elt2.getType() == type) {
                this.data.remove(name);
            }
        }
    }

    public synchronized void clearCache() {
        this.data.clear();
    }

    @Deprecated
    public synchronized void addRecord(Record r, int cred, Object o) {
        addRecord(r, cred, false);
    }

    public synchronized void addRecord(Record r, int cred) {
        addRecord(r, cred, false);
    }

    private synchronized void addRecord(Record r, int cred, boolean isAuthenticated) {
        Name name = r.getName();
        int type = r.getRRsetType();
        if (Type.isRR(type)) {
            Element element = findElement(name, type, cred);
            if (element == null) {
                CacheRRset crrset = new CacheRRset(r, cred, this.maxcache, isAuthenticated);
                addRRset(crrset, cred, isAuthenticated);
            } else if (element.compareCredibility(cred) == 0 && (element instanceof CacheRRset)) {
                CacheRRset crrset2 = (CacheRRset) element;
                crrset2.addRR(r);
            }
        }
    }

    public synchronized <T extends Record> void addRRset(RRset rrset, int cred) {
        addRRset(rrset, cred, false);
    }

    private synchronized <T extends Record> void addRRset(RRset rrset, int cred, boolean isAuthenticated) {
        CacheRRset crrset;
        long ttl = rrset.getTTL();
        Name name = rrset.getName();
        int type = rrset.getType();
        Element element = findElement(name, type, 0);
        if (ttl == 0) {
            if (element != null && element.compareCredibility(cred) <= 0) {
                removeElement(name, type);
            }
        } else {
            if (element != null && element.compareCredibility(cred) <= 0) {
                element = null;
            }
            if (element == null) {
                if (rrset instanceof CacheRRset) {
                    crrset = (CacheRRset) rrset;
                } else {
                    crrset = new CacheRRset(rrset, cred, this.maxcache, isAuthenticated);
                }
                addElement(name, crrset);
            }
        }
    }

    public synchronized void addNegative(Name name, int type, SOARecord soa, int cred) {
        addNegative(name, type, soa, cred, false);
    }

    private synchronized void addNegative(Name name, int type, SOARecord soa, int cred, boolean isAuthenticated) {
        long ttl;
        Element element;
        if (soa == null) {
            ttl = 0;
        } else {
            long ttl2 = Math.min(soa.getMinimum(), soa.getTTL());
            ttl = ttl2;
        }
        Element element2 = findElement(name, type, 0);
        if (ttl == 0) {
            if (element2 != null && element2.compareCredibility(cred) <= 0) {
                removeElement(name, type);
            }
        } else {
            if (element2 != null && element2.compareCredibility(cred) <= 0) {
                element = null;
            } else {
                element = element2;
            }
            if (element == null) {
                addElement(name, new NegativeElement(name, type, soa, cred, this.maxncache, isAuthenticated));
            }
        }
    }

    protected synchronized SetResponse lookup(Name name, int type, int minCred) {
        Name tname;
        int labels;
        int labels2;
        int labels3 = name.labels();
        int tlabels = labels3;
        while (true) {
            boolean isExact = true;
            if (tlabels < 1) {
                return SetResponse.ofType(SetResponseType.UNKNOWN);
            }
            boolean isRoot = tlabels == 1;
            if (tlabels != labels3) {
                isExact = false;
            }
            if (isRoot) {
                tname = Name.root;
            } else if (isExact) {
                tname = name;
            } else {
                tname = new Name(name, labels3 - tlabels);
            }
            Object types = this.data.get(tname);
            if (types == null) {
                labels = labels3;
            } else {
                if (!isExact || type != 255) {
                    labels = labels3;
                    if (isExact) {
                        Element element = oneElement(tname, types, type, minCred);
                        if (element instanceof CacheRRset) {
                            return SetResponse.ofType(SetResponseType.SUCCESSFUL, (CacheRRset) element);
                        }
                        if (element != null) {
                            return SetResponse.ofType(SetResponseType.NXRRSET);
                        }
                        Element element2 = oneElement(tname, types, 5, minCred);
                        if (element2 instanceof CacheRRset) {
                            return SetResponse.ofType(SetResponseType.CNAME, (CacheRRset) element2);
                        }
                    } else {
                        Element element3 = oneElement(tname, types, 39, minCred);
                        if (element3 instanceof CacheRRset) {
                            return SetResponse.ofType(SetResponseType.DNAME, (CacheRRset) element3);
                        }
                    }
                } else {
                    Element[] elements = allElements(types);
                    SetResponse sr = SetResponse.ofType(SetResponseType.SUCCESSFUL);
                    int added = 0;
                    int length = elements.length;
                    int i = 0;
                    while (i < length) {
                        Element value = elements[i];
                        if (value.expired()) {
                            removeElement(tname, value.getType());
                            labels2 = labels3;
                        } else {
                            labels2 = labels3;
                            if ((value instanceof CacheRRset) && value.compareCredibility(minCred) >= 0) {
                                sr.addRRset((CacheRRset) value);
                                added++;
                            }
                        }
                        i++;
                        labels3 = labels2;
                    }
                    labels = labels3;
                    if (added > 0) {
                        return sr;
                    }
                }
                Element element4 = oneElement(tname, types, 2, minCred);
                if (element4 instanceof CacheRRset) {
                    return SetResponse.ofType(SetResponseType.DELEGATION, (CacheRRset) element4);
                }
                if (isExact && oneElement(tname, types, 0, minCred) != null) {
                    return SetResponse.ofType(SetResponseType.NXDOMAIN);
                }
            }
            tlabels--;
            labels3 = labels;
        }
    }

    public SetResponse lookupRecords(Name name, int type, int minCred) {
        return lookup(name, type, minCred);
    }

    private List<RRset> findRecords(Name name, int type, int minCred) {
        SetResponse cr = lookupRecords(name, type, minCred);
        if (cr.isSuccessful()) {
            return cr.answers();
        }
        return null;
    }

    public List<RRset> findRecords(Name name, int type) {
        return findRecords(name, type, 3);
    }

    public List<RRset> findAnyRecords(Name name, int type) {
        return findRecords(name, type, 2);
    }

    private int getCred(int section, boolean isAuth) {
        if (section == 1) {
            return isAuth ? 4 : 3;
        }
        if (section == 2) {
            return isAuth ? 4 : 3;
        }
        if (section == 3) {
            return 1;
        }
        throw new IllegalArgumentException("getCred: invalid section");
    }

    private static void markAdditional(RRset rrset, Set<Name> names) {
        Record first = rrset.first();
        if (first.getAdditionalName() == null) {
            return;
        }
        for (Record r : rrset.rrs()) {
            Name name = r.getAdditionalName();
            if (name != null) {
                names.add(name);
            }
        }
    }

    public SetResponse addMessage(Message in) {
        int rcode;
        SetResponse response;
        RRset ns;
        HashSet<Name> additionalNames;
        SetResponse response2;
        SOARecord soarec;
        SetResponseType responseType;
        int qclass;
        Record question;
        boolean isAuthoritative = in.getHeader().getFlag(5);
        boolean isAuthenticated = in.getHeader().getFlag(10);
        Record question2 = in.getQuestion();
        int rcode2 = in.getHeader().getRcode();
        SetResponse response3 = null;
        if ((rcode2 != 0 && rcode2 != 3) || question2 == null) {
            return null;
        }
        Name qname = question2.getName();
        int qtype = question2.getType();
        int qclass2 = question2.getDClass();
        Name curname = qname;
        HashSet<Name> additionalNames2 = new HashSet<>();
        List<RRset> answers = in.getSectionRRsets(1);
        int i = 0;
        boolean completed = false;
        while (i < answers.size()) {
            RRset answer = answers.get(i);
            if (answer.getDClass() != qclass2) {
                qclass = qclass2;
                question = question2;
                rcode = rcode2;
            } else {
                qclass = qclass2;
                int type = answer.getType();
                question = question2;
                Name name = answer.getName();
                rcode = rcode2;
                int cred = getCred(1, isAuthoritative);
                if ((type == qtype || qtype == 255) && name.equals(curname)) {
                    addRRset(answer, cred, isAuthenticated);
                    if (curname == qname) {
                        if (response3 == null) {
                            response3 = SetResponse.ofType(SetResponseType.SUCCESSFUL);
                        }
                        response3.addRRset(answer);
                    }
                    markAdditional(answer, additionalNames2);
                    completed = true;
                } else if (type == 39 && curname.subdomain(name)) {
                    addRRset(answer, cred, isAuthenticated);
                    if (curname == qname) {
                        response3 = SetResponse.ofType(SetResponseType.DNAME, answer, isAuthenticated);
                    }
                    if (i + 1 >= answers.size()) {
                        response = response3;
                    } else {
                        RRset next = answers.get(i + 1);
                        response = response3;
                        if (next.getType() == 5 && next.getName().equals(curname)) {
                            response3 = response;
                        }
                    }
                    DNAMERecord dname = (DNAMERecord) answer.first();
                    try {
                        curname = curname.fromDNAME(dname);
                        response3 = response;
                    } catch (NameTooLongException e) {
                    }
                } else if (type == 5 && name.equals(curname)) {
                    addRRset(answer, cred, isAuthenticated);
                    if (curname == qname) {
                        response3 = SetResponse.ofType(SetResponseType.CNAME, answer, isAuthenticated);
                    }
                    CNAMERecord cname = (CNAMERecord) answer.first();
                    curname = cname.getTarget();
                }
            }
            i++;
            qclass2 = qclass;
            question2 = question;
            rcode2 = rcode;
        }
        rcode = rcode2;
        response = response3;
        List<RRset> auth = in.getSectionRRsets(2);
        RRset soa = null;
        RRset ns2 = null;
        for (RRset rset : auth) {
            if (rset.getType() == 6 && curname.subdomain(rset.getName())) {
                soa = rset;
            } else if (rset.getType() == 2 && curname.subdomain(rset.getName())) {
                ns2 = rset;
            }
        }
        if (!completed) {
            int rcode3 = rcode;
            Name curname2 = curname;
            int cachetype = rcode3 == 3 ? 0 : qtype;
            if (rcode3 == 3 || soa != null || ns2 == null) {
                int cred2 = getCred(2, isAuthoritative);
                if (soa == null) {
                    soarec = null;
                } else {
                    SOARecord soarec2 = (SOARecord) soa.first();
                    soarec = soarec2;
                }
                additionalNames = additionalNames2;
                ns = ns2;
                addNegative(curname2, cachetype, soarec, cred2, isAuthenticated);
                if (response == null) {
                    if (rcode3 == 3) {
                        responseType = SetResponseType.NXDOMAIN;
                    } else {
                        responseType = SetResponseType.NXRRSET;
                    }
                    response = SetResponse.ofType(responseType);
                }
            } else {
                int cred3 = getCred(2, isAuthoritative);
                addRRset(ns2, cred3, isAuthenticated);
                markAdditional(ns2, additionalNames2);
                if (response == null) {
                    response = SetResponse.ofType(SetResponseType.DELEGATION, ns2, isAuthenticated);
                    ns = ns2;
                    additionalNames = additionalNames2;
                } else {
                    ns = ns2;
                    additionalNames = additionalNames2;
                }
            }
            response2 = response;
        } else {
            ns = ns2;
            additionalNames = additionalNames2;
            if (rcode == 0 && ns != null) {
                int cred4 = getCred(2, isAuthoritative);
                addRRset(ns, cred4, isAuthenticated);
                markAdditional(ns, additionalNames);
            }
            response2 = response;
        }
        List<RRset> additional = in.getSectionRRsets(3);
        Iterator<RRset> it = additional.iterator();
        while (it.hasNext()) {
            RRset rRset = it.next();
            int type2 = rRset.getType();
            if (type2 == 1 || type2 == 28 || type2 == 38) {
                if (additionalNames.contains(rRset.getName())) {
                    RRset ns3 = ns;
                    int cred5 = getCred(3, isAuthoritative);
                    addRRset(rRset, cred5, isAuthenticated);
                    ns = ns3;
                    it = it;
                }
            }
        }
        log.debug("Caching {} for {}/{}", response2, in.getQuestion().getName(), Type.string(in.getQuestion().getType()));
        return response2;
    }

    public void flushSet(Name name, int type) {
        removeElement(name, type);
    }

    public void flushName(Name name) {
        removeName(name);
    }

    public void setMaxNCache(int seconds) {
        this.maxncache = seconds;
    }

    public int getMaxNCache() {
        return this.maxncache;
    }

    public void setMaxCache(int seconds) {
        this.maxcache = seconds;
    }

    public int getMaxCache() {
        return this.maxcache;
    }

    public int getSize() {
        return this.data.size();
    }

    public int getMaxEntries() {
        return this.data.getMaxSize();
    }

    public void setMaxEntries(int entries) {
        this.data.setMaxSize(entries);
    }

    public int getDClass() {
        return this.dclass;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        synchronized (this) {
            for (Object o : this.data.values()) {
                Element[] elements = allElements(o);
                for (Element element : elements) {
                    sb.append(element);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
