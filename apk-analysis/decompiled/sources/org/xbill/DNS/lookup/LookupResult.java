package org.xbill.DNS.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

/* loaded from: classes8.dex */
public final class LookupResult {
    private final List<Name> aliases;
    private final boolean isAuthenticated;
    private final Map<Record, Message> queryResponsePairs;
    private final List<Record> records;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LookupResult)) {
            return false;
        }
        LookupResult other = (LookupResult) o;
        if (isAuthenticated() != other.isAuthenticated()) {
            return false;
        }
        Object this$records = getRecords();
        Object other$records = other.getRecords();
        if (this$records != null ? !this$records.equals(other$records) : other$records != null) {
            return false;
        }
        Object this$aliases = getAliases();
        Object other$aliases = other.getAliases();
        if (this$aliases != null ? !this$aliases.equals(other$aliases) : other$aliases != null) {
            return false;
        }
        Object this$queryResponsePairs = getQueryResponsePairs();
        Object other$queryResponsePairs = other.getQueryResponsePairs();
        return this$queryResponsePairs != null ? this$queryResponsePairs.equals(other$queryResponsePairs) : other$queryResponsePairs == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isAuthenticated() ? 79 : 97);
        Object $records = getRecords();
        int result2 = (result * 59) + ($records == null ? 43 : $records.hashCode());
        Object $aliases = getAliases();
        int result3 = (result2 * 59) + ($aliases == null ? 43 : $aliases.hashCode());
        Object $queryResponsePairs = getQueryResponsePairs();
        return (result3 * 59) + ($queryResponsePairs != null ? $queryResponsePairs.hashCode() : 43);
    }

    public String toString() {
        return "LookupResult(records=" + getRecords() + ", aliases=" + getAliases() + ", queryResponsePairs=" + getQueryResponsePairs() + ", isAuthenticated=" + isAuthenticated() + ")";
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public List<Name> getAliases() {
        return this.aliases;
    }

    Map<Record, Message> getQueryResponsePairs() {
        return this.queryResponsePairs;
    }

    boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Deprecated
    public LookupResult(List<Record> records, List<Name> aliases) {
        List<Name> listUnmodifiableList;
        this.records = Collections.unmodifiableList(new ArrayList(records));
        if (aliases == null) {
            listUnmodifiableList = Collections.emptyList();
        } else {
            listUnmodifiableList = Collections.unmodifiableList(new ArrayList(aliases));
        }
        this.aliases = listUnmodifiableList;
        this.queryResponsePairs = Collections.emptyMap();
        this.isAuthenticated = false;
    }

    LookupResult(boolean isAuthenticated) {
        this.queryResponsePairs = Collections.emptyMap();
        this.isAuthenticated = isAuthenticated;
        this.records = Collections.emptyList();
        this.aliases = Collections.emptyList();
    }

    LookupResult(Record query, boolean isAuthenticated, Record result) {
        this.queryResponsePairs = Collections.singletonMap(query, null);
        this.isAuthenticated = isAuthenticated;
        this.records = Collections.singletonList(result);
        this.aliases = Collections.emptyList();
    }

    LookupResult(LookupResult previous, Record query, Message answer, boolean isAuthenticated, List<Record> records, List<Name> aliases) {
        Map<Record, Message> map = new HashMap<>(previous.queryResponsePairs.size() + 1);
        map.putAll(previous.queryResponsePairs);
        map.put(query, answer);
        this.queryResponsePairs = Collections.unmodifiableMap(map);
        this.isAuthenticated = previous.isAuthenticated && isAuthenticated && this.queryResponsePairs.values().stream().filter(new Predicate() { // from class: org.xbill.DNS.lookup.LookupResult$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Message) obj);
            }
        }).allMatch(new Predicate() { // from class: org.xbill.DNS.lookup.LookupResult$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((Message) obj).getHeader().getFlag(10);
            }
        });
        this.records = Collections.unmodifiableList(new ArrayList(records));
        this.aliases = Collections.unmodifiableList(new ArrayList(aliases));
    }
}
