package org.xbill.DNS.dnssec;

import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.DSRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;

/* loaded from: classes8.dex */
class AlgorithmRequirements {
    private static final int MAX_ALGORITHMS = 255;
    private final short[] needs = new short[255];
    private int num;
    private final ValUtils valUtils;

    public AlgorithmRequirements(ValUtils valUtils) {
        this.valUtils = valUtils;
    }

    public int getNum() {
        return this.num;
    }

    void initList(List<Integer> sigalg) {
        this.num = 0;
        for (Integer algo : sigalg) {
            this.needs[algo.intValue()] = 1;
            this.num++;
        }
    }

    List<Integer> initDs(RRset dsRRset, int favoriteDsAlgorithm) {
        List<Integer> sigalg = new ArrayList<>();
        this.num = 0;
        for (Record r : dsRRset.rrs(false)) {
            DSRecord ds = (DSRecord) r;
            if (ds.getDigestID() == favoriteDsAlgorithm) {
                int algo = ds.getAlgorithm();
                if (this.valUtils.isAlgorithmSupported(algo) && this.needs[algo] == 0) {
                    this.needs[algo] = 1;
                    sigalg.add(Integer.valueOf(algo));
                    this.num++;
                }
            }
        }
        return sigalg;
    }

    boolean setSecure(int algo) {
        if (this.needs[algo] == 0) {
            return false;
        }
        this.needs[algo] = 0;
        this.num--;
        return this.num == 0;
    }

    void setBogus(int algo) {
        if (this.needs[algo] != 0) {
            this.needs[algo] = 2;
        }
    }

    int missing() {
        int miss = -1;
        for (int i = 0; i < this.needs.length; i++) {
            if (this.needs[i] == 2) {
                return 0;
            }
            if (this.needs[i] == 1 && miss == -1) {
                miss = i;
            }
        }
        if (miss != -1) {
            return miss;
        }
        return 0;
    }
}
