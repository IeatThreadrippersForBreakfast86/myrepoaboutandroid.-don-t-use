package org.xbill.DNS;

import java.util.List;
import org.xbill.DNS.SVCBBase;

/* loaded from: classes8.dex */
public class SVCBRecord extends SVCBBase {
    SVCBRecord() {
    }

    public SVCBRecord(Name name, int dclass, long ttl, int priority, Name domain, List<SVCBBase.ParameterBase> params) {
        super(name, 64, dclass, ttl, priority, domain, params);
    }
}
