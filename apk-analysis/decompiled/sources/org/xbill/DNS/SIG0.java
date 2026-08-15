package org.xbill.DNS;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Iterator;
import org.xbill.DNS.DNSSEC;

/* loaded from: classes8.dex */
public class SIG0 {
    private static final Duration VALIDITY = Duration.ofSeconds(300);

    private SIG0() {
    }

    public static void signMessage(Message message, KEYRecord key, PrivateKey privkey, SIGRecord previous) throws NumberFormatException, DNSSEC.DNSSECException {
        signMessage(message, key, privkey, previous, Instant.now());
    }

    public static void signMessage(Message message, KEYRecord key, PrivateKey privkey, SIGRecord previous, Instant timeSigned) throws NumberFormatException, DNSSEC.DNSSECException {
        Duration validity;
        int validityOption = Options.intValue("sig0validity");
        if (validityOption < 0) {
            validity = VALIDITY;
        } else {
            validity = Duration.ofSeconds(validityOption);
        }
        Instant timeExpires = timeSigned.plus((TemporalAmount) validity);
        SIGRecord sig = DNSSEC.signMessage(message, previous, key, privkey, timeSigned, timeExpires);
        message.addRecord(sig, 3);
    }

    public static void verifyMessage(Message message, byte[] b, KEYRecord key, SIGRecord previous) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSEC.DNSSECException, CloneNotSupportedException {
        verifyMessage(message, b, key, previous, Instant.now());
    }

    public static void verifyMessage(Message message, byte[] b, KEYRecord key, SIGRecord previous, Instant now) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, DNSSEC.DNSSECException, CloneNotSupportedException {
        SIGRecord sig = null;
        Iterator<Record> it = message.getSection(3).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Record r = it.next();
            if (r.getType() == 24 && ((SIGRecord) r).getTypeCovered() == 0) {
                sig = (SIGRecord) r;
                break;
            }
        }
        DNSSEC.verifyMessage(message, b, sig, previous, key, now);
    }
}
