package org.xbill.DNS;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

/* loaded from: classes8.dex */
final class FormattedTime {
    private static final DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

    private FormattedTime() {
    }

    public static String format(Instant date) {
        return DEFAULT_FORMAT.format(date);
    }

    public static Instant parse(String s) throws DateTimeParseException {
        if (s.length() == 14) {
            return (Instant) DEFAULT_FORMAT.parse(s, new TemporalQuery() { // from class: org.xbill.DNS.FormattedTime$$ExternalSyntheticLambda0
                @Override // java.time.temporal.TemporalQuery
                public final Object queryFrom(TemporalAccessor temporalAccessor) {
                    return Instant.from(temporalAccessor);
                }
            });
        }
        if (s.length() <= 10) {
            return Instant.ofEpochSecond(Long.parseLong(s));
        }
        throw new DateTimeParseException("Invalid time encoding: ", s, 0);
    }
}
