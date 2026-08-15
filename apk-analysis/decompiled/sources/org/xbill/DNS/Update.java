package org.xbill.DNS;

import java.io.IOException;
import java.util.function.Consumer;

/* loaded from: classes8.dex */
public class Update extends Message {
    private final int dclass;
    private final Name origin;

    public Update(Name zone, int dclass) {
        if (!zone.isAbsolute()) {
            throw new RelativeNameException(zone);
        }
        DClass.check(dclass);
        getHeader().setOpcode(5);
        Record soa = Record.newRecord(zone, 6, 1);
        addRecord(soa, 0);
        this.origin = zone;
        this.dclass = dclass;
    }

    public Update(Name zone) {
        this(zone, 1);
    }

    private void newPrereq(Record rec) {
        addRecord(rec, 1);
    }

    private void newUpdate(Record rec) {
        addRecord(rec, 2);
    }

    public void present(Name name) {
        newPrereq(Record.newRecord(name, 255, 255, 0L));
    }

    public void present(Name name, int type) {
        newPrereq(Record.newRecord(name, type, 255, 0L));
    }

    public void present(Name name, int type, String recordToCheck) throws IOException {
        newPrereq(Record.fromString(name, type, this.dclass, 0L, recordToCheck, this.origin));
    }

    public void present(Name name, int type, Tokenizer tokenizer) throws IOException {
        newPrereq(Record.fromString(name, type, this.dclass, 0L, tokenizer, this.origin));
    }

    public void present(Record recordToCheck) {
        newPrereq(recordToCheck);
    }

    public void absent(Name name) {
        newPrereq(Record.newRecord(name, 255, 254, 0L));
    }

    public void absent(Name name, int type) {
        newPrereq(Record.newRecord(name, type, 254, 0L));
    }

    public void add(Name name, int type, long ttl, String recordToAdd) throws IOException {
        newUpdate(Record.fromString(name, type, this.dclass, ttl, recordToAdd, this.origin));
    }

    public void add(Name name, int type, long ttl, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, type, this.dclass, ttl, tokenizer, this.origin));
    }

    public void add(Record recordToAdd) {
        newUpdate(recordToAdd);
    }

    public void add(Record[] records) {
        for (Record r : records) {
            add(r);
        }
    }

    public <T extends Record> void add(RRset rrset) {
        rrset.rrs().forEach(new Update$$ExternalSyntheticLambda0(this));
    }

    public void delete(Name name) {
        newUpdate(Record.newRecord(name, 255, 255, 0L));
    }

    public void delete(Name name, int type) {
        newUpdate(Record.newRecord(name, type, 255, 0L));
    }

    public void delete(Name name, int type, String recordToDelete) throws IOException {
        newUpdate(Record.fromString(name, type, 254, 0L, recordToDelete, this.origin));
    }

    public void delete(Name name, int type, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, type, 254, 0L, tokenizer, this.origin));
    }

    public void delete(Record recordToDelete) {
        newUpdate(recordToDelete.withDClass(254, 0L));
    }

    public void delete(Record[] records) {
        for (Record r : records) {
            delete(r);
        }
    }

    public <T extends Record> void delete(RRset rrset) {
        rrset.rrs().forEach(new Consumer() { // from class: org.xbill.DNS.Update$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.delete((Record) obj);
            }
        });
    }

    public void replace(Name name, int type, long ttl, String recordToReplace) throws IOException {
        delete(name, type);
        add(name, type, ttl, recordToReplace);
    }

    public void replace(Name name, int type, long ttl, Tokenizer tokenizer) throws IOException {
        delete(name, type);
        add(name, type, ttl, tokenizer);
    }

    public void replace(Record recordToReplace) {
        delete(recordToReplace.getName(), recordToReplace.getType());
        add(recordToReplace);
    }

    public void replace(Record[] records) {
        for (Record r : records) {
            replace(r);
        }
    }

    public <T extends Record> void replace(RRset rrset) {
        delete(rrset.getName(), rrset.getType());
        rrset.rrs().forEach(new Update$$ExternalSyntheticLambda0(this));
    }
}
