package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import kotlin.UByte;

/* loaded from: classes8.dex */
public class base64 {
    private static final String BASE_64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private static final String BASE_64_URL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    private base64() {
    }

    public static String toString(byte[] b) {
        return toString(b, false);
    }

    public static String toString(byte[] b, boolean useUrl) {
        String base = useUrl ? BASE_64_URL : BASE_64;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int i = 0; i < (b.length + 2) / 3; i++) {
            short[] s = new short[3];
            short[] t = new short[4];
            for (int j = 0; j < 3; j++) {
                if ((i * 3) + j < b.length) {
                    s[j] = (short) (b[(i * 3) + j] & UByte.MAX_VALUE);
                } else {
                    s[j] = -1;
                }
            }
            t[0] = (short) (s[0] >> 2);
            if (s[1] == -1) {
                t[1] = (short) ((s[0] & 3) << 4);
            } else {
                t[1] = (short) (((s[0] & 3) << 4) + (s[1] >> 4));
            }
            if (s[1] == -1) {
                t[3] = 64;
                t[2] = 64;
            } else if (s[2] == -1) {
                t[2] = (short) ((s[1] & 15) << 2);
                t[3] = 64;
            } else {
                t[2] = (short) (((s[1] & 15) << 2) + (s[2] >> 6));
                t[3] = (short) (s[2] & 63);
            }
            for (int j2 = 0; j2 < 4; j2++) {
                if (t[j2] != 64 || !useUrl) {
                    os.write(base.charAt(t[j2]));
                }
            }
        }
        return os.toString();
    }

    public static String formatString(byte[] b, int lineLength, String prefix, boolean addClose) {
        return BaseUtils.wrapLines(toString(b), lineLength, prefix, addClose);
    }

    public static byte[] fromString(String str) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte[] raw = str.getBytes();
        for (byte b : raw) {
            if (!Character.isWhitespace((char) b)) {
                bs.write(b);
            }
        }
        byte[] in = bs.toByteArray();
        if (in.length % 4 != 0) {
            return null;
        }
        bs.reset();
        DataOutputStream ds = new DataOutputStream(bs);
        for (int i = 0; i < (in.length + 3) / 4; i++) {
            short[] s = new short[4];
            short[] t = new short[3];
            for (int j = 0; j < 4; j++) {
                s[j] = (short) BASE_64.indexOf(in[(i * 4) + j]);
            }
            t[0] = (short) ((s[0] << 2) + (s[1] >> 4));
            if (s[2] == 64) {
                t[2] = -1;
                t[1] = -1;
                if ((s[1] & 15) != 0) {
                    return null;
                }
            } else if (s[3] == 64) {
                t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 255);
                t[2] = -1;
                if ((s[2] & 3) != 0) {
                    return null;
                }
            } else {
                t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 255);
                t[2] = (short) (((s[2] << 6) + s[3]) & 255);
            }
            for (int j2 = 0; j2 < 3; j2++) {
                try {
                    if (t[j2] >= 0) {
                        ds.writeByte(t[j2]);
                    }
                } catch (IOException e) {
                }
            }
        }
        return bs.toByteArray();
    }
}
