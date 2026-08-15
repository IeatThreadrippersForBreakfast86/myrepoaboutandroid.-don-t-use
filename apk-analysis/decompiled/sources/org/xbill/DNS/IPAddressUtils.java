package org.xbill.DNS;

/* loaded from: classes8.dex */
final class IPAddressUtils {
    static final int IPv4 = 1;
    static final int IPv6 = 2;

    private IPAddressUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static byte[] parseV4(String s) {
        byte[] values = new byte[4];
        int length = s.length();
        int currentOctet = 0;
        int currentValue = 0;
        int numDigits = 0;
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                if (numDigits == 3) {
                    return null;
                }
                if (numDigits > 0 && currentValue == 0) {
                    return null;
                }
                numDigits++;
                currentValue = (currentValue * 10) + (c - '0');
                if (currentValue > 255) {
                    return null;
                }
            } else {
                if (c != '.' || currentOctet == 3 || numDigits == 0) {
                    return null;
                }
                values[currentOctet] = (byte) currentValue;
                numDigits = 0;
                currentValue = 0;
                currentOctet++;
            }
        }
        if (currentOctet != 3 || numDigits == 0) {
            return null;
        }
        values[currentOctet] = (byte) currentValue;
        return values;
    }

    /* JADX WARN: Code restructure failed: missing block: B:62:0x00c3, code lost:
    
        if (r10 >= 16) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00c5, code lost:
    
        if (r0 >= 0) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00c7, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00c8, code lost:
    
        if (r0 < 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ca, code lost:
    
        r1 = 16 - r10;
        java.lang.System.arraycopy(r2, r0, r2, r0 + r1, r10 - r0);
        r8 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00d7, code lost:
    
        if (r8 >= (r0 + r1)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00d9, code lost:
    
        r2[r8] = 0;
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00de, code lost:
    
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static byte[] parseV6(String s) throws NumberFormatException {
        byte[] v4addr;
        int range = -1;
        byte[] data = new byte[16];
        String[] tokens = s.split(":", -1);
        int first = 0;
        int last = tokens.length - 1;
        if (tokens[0].isEmpty()) {
            if (last - 0 <= 0 || !tokens[1].isEmpty()) {
                return null;
            }
            first = 0 + 1;
        }
        if (tokens[last].isEmpty()) {
            if (last - first <= 0 || !tokens[last - 1].isEmpty()) {
                return null;
            }
            last--;
        }
        if ((last - first) + 1 > 8) {
            return null;
        }
        int i = first;
        int j = 0;
        while (true) {
            if (i > last) {
                break;
            }
            if (tokens[i].isEmpty()) {
                if (range >= 0) {
                    return null;
                }
                range = j;
            } else if (tokens[i].indexOf(46) >= 0) {
                if (i < last || i > 6 || (v4addr = Address.toByteArray(tokens[i], 1)) == null) {
                    return null;
                }
                int k = 0;
                while (k < 4) {
                    data[j] = v4addr[k];
                    k++;
                    j++;
                }
            } else {
                for (int k2 = 0; k2 < tokens[i].length(); k2++) {
                    try {
                        char c = tokens[i].charAt(k2);
                        if (Character.digit(c, 16) < 0) {
                            return null;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                int x = Integer.parseInt(tokens[i], 16);
                if (!Utils.isUInt16(x)) {
                    return null;
                }
                int j2 = j + 1;
                try {
                    data[j] = (byte) (x >>> 8);
                    j = j2 + 1;
                    data[j2] = (byte) (x & 255);
                } catch (NumberFormatException e2) {
                    return null;
                }
            }
            i++;
        }
    }
}
