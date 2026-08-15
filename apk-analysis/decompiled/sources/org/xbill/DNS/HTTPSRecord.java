package org.xbill.DNS;

import java.util.List;
import org.xbill.DNS.SVCBBase;

/* loaded from: classes8.dex */
public class HTTPSRecord extends SVCBBase {
    HTTPSRecord() {
    }

    public HTTPSRecord(Name name, int dclass, long ttl, int priority, Name domain, List<SVCBBase.ParameterBase> params) {
        super(name, 65, dclass, ttl, priority, domain, params);
    }
}
