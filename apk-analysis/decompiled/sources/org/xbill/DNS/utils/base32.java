package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import kotlin.UByte;

/* loaded from: classes8.dex */
public class base32 {
    private final String alphabet;
    private final boolean lowercase;
    private final boolean padding;

    public static class Alphabet {
        public static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";
        public static final String BASE32HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV=";

        private Alphabet() {
        }
    }

    public base32(String alphabet, boolean padding, boolean lowercase) {
        this.alphabet = alphabet;
        this.padding = padding;
        this.lowercase = lowercase;
    }

    private static int blockLenToPadding(int blocklen) {
        switch (blocklen) {
            case 1:
                return 6;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 1;
            case 5:
                return 0;
            default:
                return -1;
        }
    }

    private static int paddingToBlockLen(int padlen) {
        switch (padlen) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
            case 5:
            default:
                return -1;
            case 3:
                return 3;
            case 4:
                return 2;
            case 6:
                return 1;
        }
    }

    public String toString(byte[] b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int i = 0; i < (b.length + 4) / 5; i++) {
            short[] s = new short[5];
            int[] t = new int[8];
            int blocklen = 5;
            for (int j = 0; j < 5; j++) {
                if ((i * 5) + j < b.length) {
                    s[j] = (short) (b[(i * 5) + j] & UByte.MAX_VALUE);
                } else {
                    s[j] = 0;
                    blocklen--;
                }
            }
            int padlen = blockLenToPadding(blocklen);
            t[0] = (byte) ((s[0] >> 3) & 31);
            t[1] = (byte) (((s[0] & 7) << 2) | ((s[1] >> 6) & 3));
            t[2] = (byte) ((s[1] >> 1) & 31);
            t[3] = (byte) (((s[1] & 1) << 4) | ((s[2] >> 4) & 15));
            t[4] = (byte) (((s[2] & 15) << 1) | (1 & (s[3] >> 7)));
            t[5] = (byte) ((s[3] >> 2) & 31);
            t[6] = (byte) (((s[4] >> 5) & 7) | ((s[3] & 3) << 3));
            t[7] = (byte) (s[4] & 31);
            for (int j2 = 0; j2 < t.length - padlen; j2++) {
                char c = this.alphabet.charAt(t[j2]);
                if (this.lowercase) {
                    c = Character.toLowerCase(c);
                }
                os.write(c);
            }
            if (this.padding) {
                for (int j3 = t.length - padlen; j3 < t.length; j3++) {
                    os.write(61);
                }
            }
        }
        return os.toString();
    }

    public byte[] fromString(String str) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte[] raw = str.getBytes();
        for (byte b : raw) {
            char c = (char) b;
            if (!Character.isWhitespace(c)) {
                bs.write((byte) Character.toUpperCase(c));
            }
        }
        char c2 = '=';
        int i = 8;
        if (this.padding) {
            if (bs.size() % 8 != 0) {
                return null;
            }
        } else {
            while (bs.size() % 8 != 0) {
                bs.write(61);
            }
        }
        byte[] in = bs.toByteArray();
        bs.reset();
        DataOutputStream ds = new DataOutputStream(bs);
        int i2 = 0;
        while (i2 < in.length / i) {
            short[] s = new short[i];
            int[] t = new int[5];
            int padlen = 8;
            int j = 0;
            while (j < i && ((char) in[(i2 * 8) + j]) != c2) {
                s[j] = (short) this.alphabet.indexOf(in[(i2 * 8) + j]);
                if (s[j] < 0) {
                    return null;
                }
                padlen--;
                j++;
                c2 = '=';
                i = 8;
            }
            int blocklen = paddingToBlockLen(padlen);
            if (blocklen < 0) {
                return null;
            }
            t[0] = (s[0] << 3) | (s[1] >> 2);
            t[1] = ((s[1] & 3) << 6) | (s[2] << 1) | (s[3] >> 4);
            t[2] = ((s[3] & 15) << 4) | ((s[4] >> 1) & 15);
            t[3] = (s[4] << 7) | (s[5] << 2) | (s[6] >> 3);
            t[4] = ((s[6] & 7) << 5) | s[7];
            for (int j2 = 0; j2 < blocklen; j2++) {
                try {
                    ds.writeByte((byte) (t[j2] & 255));
                } catch (IOException e) {
                }
            }
            i2++;
            c2 = '=';
            i = 8;
        }
        return bs.toByteArray();
    }
}
