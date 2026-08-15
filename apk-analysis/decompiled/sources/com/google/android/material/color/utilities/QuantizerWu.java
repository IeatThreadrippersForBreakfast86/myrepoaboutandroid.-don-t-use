package com.google.android.material.color.utilities;

import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class QuantizerWu implements Quantizer {
    private static final int INDEX_BITS = 5;
    private static final int INDEX_COUNT = 33;
    private static final int TOTAL_SIZE = 35937;
    Box[] cubes;
    double[] moments;
    int[] momentsB;
    int[] momentsG;
    int[] momentsR;
    int[] weights;

    private enum Direction {
        RED,
        GREEN,
        BLUE
    }

    @Override // com.google.android.material.color.utilities.Quantizer
    public QuantizerResult quantize(int[] pixels, int colorCount) {
        QuantizerResult mapResult = new QuantizerMap().quantize(pixels, colorCount);
        constructHistogram(mapResult.colorToCount);
        createMoments();
        CreateBoxesResult createBoxesResult = createBoxes(colorCount);
        List<Integer> colors = createResult(createBoxesResult.resultCount);
        Map<Integer, Integer> resultMap = new LinkedHashMap<>();
        Iterator<Integer> it = colors.iterator();
        while (it.hasNext()) {
            int color = it.next().intValue();
            resultMap.put(Integer.valueOf(color), 0);
        }
        return new QuantizerResult(resultMap);
    }

    static int getIndex(int r, int g, int b) {
        return (r << 10) + (r << 6) + r + (g << 5) + g + b;
    }

    void constructHistogram(Map<Integer, Integer> pixels) {
        QuantizerWu quantizerWu = this;
        quantizerWu.weights = new int[TOTAL_SIZE];
        quantizerWu.momentsR = new int[TOTAL_SIZE];
        quantizerWu.momentsG = new int[TOTAL_SIZE];
        quantizerWu.momentsB = new int[TOTAL_SIZE];
        quantizerWu.moments = new double[TOTAL_SIZE];
        for (Iterator<Map.Entry<Integer, Integer>> it = pixels.entrySet().iterator(); it.hasNext(); it = it) {
            Map.Entry<Integer, Integer> pair = it.next();
            int pixel = pair.getKey().intValue();
            int count = pair.getValue().intValue();
            int red = ColorUtils.redFromArgb(pixel);
            int green = ColorUtils.greenFromArgb(pixel);
            int blue = ColorUtils.blueFromArgb(pixel);
            int iR = (red >> 3) + 1;
            int iG = (green >> 3) + 1;
            int iB = (blue >> 3) + 1;
            int index = getIndex(iR, iG, iB);
            int[] iArr = quantizerWu.weights;
            iArr[index] = iArr[index] + count;
            int[] iArr2 = quantizerWu.momentsR;
            iArr2[index] = iArr2[index] + (red * count);
            int[] iArr3 = quantizerWu.momentsG;
            iArr3[index] = iArr3[index] + (green * count);
            int[] iArr4 = quantizerWu.momentsB;
            iArr4[index] = iArr4[index] + (blue * count);
            double[] dArr = quantizerWu.moments;
            dArr[index] = dArr[index] + (count * ((red * red) + (green * green) + (blue * blue)));
            quantizerWu = this;
        }
    }

    void createMoments() {
        int r = 1;
        while (true) {
            int i = 33;
            if (r < 33) {
                int[] area = new int[33];
                int[] areaR = new int[33];
                int[] areaG = new int[33];
                int[] areaB = new int[33];
                double[] area2 = new double[33];
                int g = 1;
                while (g < i) {
                    int line = 0;
                    int lineR = 0;
                    int lineG = 0;
                    int lineB = 0;
                    double line2 = 0.0d;
                    int b = 1;
                    while (b < i) {
                        int index = getIndex(r, g, b);
                        int line3 = line + this.weights[index];
                        int lineR2 = lineR + this.momentsR[index];
                        lineG += this.momentsG[index];
                        lineB += this.momentsB[index];
                        line2 += this.moments[index];
                        area[b] = area[b] + line3;
                        areaR[b] = areaR[b] + lineR2;
                        areaG[b] = areaG[b] + lineG;
                        areaB[b] = areaB[b] + lineB;
                        area2[b] = area2[b] + line2;
                        int previousIndex = getIndex(r - 1, g, b);
                        this.weights[index] = this.weights[previousIndex] + area[b];
                        this.momentsR[index] = this.momentsR[previousIndex] + areaR[b];
                        this.momentsG[index] = this.momentsG[previousIndex] + areaG[b];
                        this.momentsB[index] = this.momentsB[previousIndex] + areaB[b];
                        this.moments[index] = this.moments[previousIndex] + area2[b];
                        b++;
                        line = line3;
                        lineR = lineR2;
                        i = 33;
                    }
                    g++;
                    i = 33;
                }
                r++;
            } else {
                return;
            }
        }
    }

    CreateBoxesResult createBoxes(int maxColorCount) {
        this.cubes = new Box[maxColorCount];
        for (int i = 0; i < maxColorCount; i++) {
            this.cubes[i] = new Box();
        }
        double[] volumeVariance = new double[maxColorCount];
        Box firstBox = this.cubes[0];
        firstBox.f155r1 = 32;
        firstBox.f153g1 = 32;
        firstBox.f151b1 = 32;
        int generatedColorCount = maxColorCount;
        int next = 0;
        int i2 = 1;
        while (true) {
            if (i2 >= maxColorCount) {
                break;
            }
            if (cut(this.cubes[next], this.cubes[i2]).booleanValue()) {
                volumeVariance[next] = this.cubes[next].vol > 1 ? variance(this.cubes[next]) : 0.0d;
                volumeVariance[i2] = this.cubes[i2].vol > 1 ? variance(this.cubes[i2]) : 0.0d;
            } else {
                volumeVariance[next] = 0.0d;
                i2--;
            }
            next = 0;
            double temp = volumeVariance[0];
            for (int j = 1; j <= i2; j++) {
                if (volumeVariance[j] > temp) {
                    temp = volumeVariance[j];
                    next = j;
                }
            }
            if (temp > 0.0d) {
                i2++;
            } else {
                generatedColorCount = i2 + 1;
                break;
            }
        }
        return new CreateBoxesResult(maxColorCount, generatedColorCount);
    }

    List<Integer> createResult(int colorCount) {
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < colorCount; i++) {
            Box cube = this.cubes[i];
            int weight = volume(cube, this.weights);
            if (weight > 0) {
                int r = volume(cube, this.momentsR) / weight;
                int g = volume(cube, this.momentsG) / weight;
                int b = volume(cube, this.momentsB) / weight;
                int color = ((r & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((g & 255) << 8) | (b & 255);
                colors.add(Integer.valueOf(color));
            }
        }
        return colors;
    }

    double variance(Box cube) {
        int dr = volume(cube, this.momentsR);
        int dg = volume(cube, this.momentsG);
        int db = volume(cube, this.momentsB);
        double xx = ((((((this.moments[getIndex(cube.f155r1, cube.f153g1, cube.f151b1)] - this.moments[getIndex(cube.f155r1, cube.f153g1, cube.f150b0)]) - this.moments[getIndex(cube.f155r1, cube.f152g0, cube.f151b1)]) + this.moments[getIndex(cube.f155r1, cube.f152g0, cube.f150b0)]) - this.moments[getIndex(cube.f154r0, cube.f153g1, cube.f151b1)]) + this.moments[getIndex(cube.f154r0, cube.f153g1, cube.f150b0)]) + this.moments[getIndex(cube.f154r0, cube.f152g0, cube.f151b1)]) - this.moments[getIndex(cube.f154r0, cube.f152g0, cube.f150b0)];
        int hypotenuse = (dr * dr) + (dg * dg) + (db * db);
        int volume = volume(cube, this.weights);
        return xx - (hypotenuse / volume);
    }

    Boolean cut(Box one, Box two) {
        Direction cutDirection;
        int wholeR = volume(one, this.momentsR);
        int wholeG = volume(one, this.momentsG);
        int wholeB = volume(one, this.momentsB);
        int wholeW = volume(one, this.weights);
        MaximizeResult maxRResult = maximize(one, Direction.RED, one.f154r0 + 1, one.f155r1, wholeR, wholeG, wholeB, wholeW);
        MaximizeResult maxGResult = maximize(one, Direction.GREEN, one.f152g0 + 1, one.f153g1, wholeR, wholeG, wholeB, wholeW);
        MaximizeResult maxBResult = maximize(one, Direction.BLUE, one.f150b0 + 1, one.f151b1, wholeR, wholeG, wholeB, wholeW);
        double maxR = maxRResult.maximum;
        double maxG = maxGResult.maximum;
        double maxB = maxBResult.maximum;
        if (maxR < maxG || maxR < maxB) {
            if (maxG >= maxR && maxG >= maxB) {
                cutDirection = Direction.GREEN;
            } else {
                cutDirection = Direction.BLUE;
            }
        } else {
            if (maxRResult.cutLocation < 0) {
                return false;
            }
            cutDirection = Direction.RED;
        }
        two.f155r1 = one.f155r1;
        two.f153g1 = one.f153g1;
        two.f151b1 = one.f151b1;
        switch (cutDirection) {
            case RED:
                one.f155r1 = maxRResult.cutLocation;
                two.f154r0 = one.f155r1;
                two.f152g0 = one.f152g0;
                two.f150b0 = one.f150b0;
                break;
            case GREEN:
                one.f153g1 = maxGResult.cutLocation;
                two.f154r0 = one.f154r0;
                two.f152g0 = one.f153g1;
                two.f150b0 = one.f150b0;
                break;
            case BLUE:
                one.f151b1 = maxBResult.cutLocation;
                two.f154r0 = one.f154r0;
                two.f152g0 = one.f152g0;
                two.f150b0 = one.f151b1;
                break;
        }
        one.vol = (one.f155r1 - one.f154r0) * (one.f153g1 - one.f152g0) * (one.f151b1 - one.f150b0);
        two.vol = (two.f155r1 - two.f154r0) * (two.f153g1 - two.f152g0) * (two.f151b1 - two.f150b0);
        return true;
    }

    MaximizeResult maximize(Box cube, Direction direction, int first, int last, int wholeR, int wholeG, int wholeB, int wholeW) {
        int bottomG;
        int bottomB;
        QuantizerWu quantizerWu = this;
        Box box = cube;
        int bottomR = bottom(box, direction, quantizerWu.momentsR);
        int bottomG2 = bottom(box, direction, quantizerWu.momentsG);
        int bottomB2 = bottom(box, direction, quantizerWu.momentsB);
        int bottomW = bottom(box, direction, quantizerWu.weights);
        double max = 0.0d;
        int cut = -1;
        int halfR = 0;
        int i = first;
        while (i < last) {
            halfR = top(box, direction, i, quantizerWu.momentsR) + bottomR;
            int bottomR2 = bottomR;
            int halfG = top(box, direction, i, quantizerWu.momentsG) + bottomG2;
            int halfB = top(box, direction, i, quantizerWu.momentsB) + bottomB2;
            int halfW = top(box, direction, i, quantizerWu.weights) + bottomW;
            if (halfW == 0) {
                bottomG = bottomG2;
                bottomB = bottomB2;
            } else {
                double tempNumerator = (halfR * halfR) + (halfG * halfG) + (halfB * halfB);
                bottomG = bottomG2;
                bottomB = bottomB2;
                double tempDenominator = halfW;
                double temp = tempNumerator / tempDenominator;
                halfR = wholeR - halfR;
                halfG = wholeG - halfG;
                halfB = wholeB - halfB;
                halfW = wholeW - halfW;
                if (halfW != 0) {
                    double tempNumerator2 = (halfR * halfR) + (halfG * halfG) + (halfB * halfB);
                    double tempDenominator2 = halfW;
                    double temp2 = temp + (tempNumerator2 / tempDenominator2);
                    if (temp2 > max) {
                        max = temp2;
                        cut = i;
                    }
                }
            }
            i++;
            quantizerWu = this;
            box = cube;
            bottomR = bottomR2;
            bottomG2 = bottomG;
            bottomB2 = bottomB;
        }
        return new MaximizeResult(cut, max);
    }

    static int volume(Box cube, int[] moment) {
        return ((((((moment[getIndex(cube.f155r1, cube.f153g1, cube.f151b1)] - moment[getIndex(cube.f155r1, cube.f153g1, cube.f150b0)]) - moment[getIndex(cube.f155r1, cube.f152g0, cube.f151b1)]) + moment[getIndex(cube.f155r1, cube.f152g0, cube.f150b0)]) - moment[getIndex(cube.f154r0, cube.f153g1, cube.f151b1)]) + moment[getIndex(cube.f154r0, cube.f153g1, cube.f150b0)]) + moment[getIndex(cube.f154r0, cube.f152g0, cube.f151b1)]) - moment[getIndex(cube.f154r0, cube.f152g0, cube.f150b0)];
    }

    static int bottom(Box cube, Direction direction, int[] moment) {
        switch (direction) {
            case RED:
                return (((-moment[getIndex(cube.f154r0, cube.f153g1, cube.f151b1)]) + moment[getIndex(cube.f154r0, cube.f153g1, cube.f150b0)]) + moment[getIndex(cube.f154r0, cube.f152g0, cube.f151b1)]) - moment[getIndex(cube.f154r0, cube.f152g0, cube.f150b0)];
            case GREEN:
                return (((-moment[getIndex(cube.f155r1, cube.f152g0, cube.f151b1)]) + moment[getIndex(cube.f155r1, cube.f152g0, cube.f150b0)]) + moment[getIndex(cube.f154r0, cube.f152g0, cube.f151b1)]) - moment[getIndex(cube.f154r0, cube.f152g0, cube.f150b0)];
            case BLUE:
                return (((-moment[getIndex(cube.f155r1, cube.f153g1, cube.f150b0)]) + moment[getIndex(cube.f155r1, cube.f152g0, cube.f150b0)]) + moment[getIndex(cube.f154r0, cube.f153g1, cube.f150b0)]) - moment[getIndex(cube.f154r0, cube.f152g0, cube.f150b0)];
            default:
                throw new IllegalArgumentException("unexpected direction " + direction);
        }
    }

    static int top(Box cube, Direction direction, int position, int[] moment) {
        switch (direction) {
            case RED:
                return ((moment[getIndex(position, cube.f153g1, cube.f151b1)] - moment[getIndex(position, cube.f153g1, cube.f150b0)]) - moment[getIndex(position, cube.f152g0, cube.f151b1)]) + moment[getIndex(position, cube.f152g0, cube.f150b0)];
            case GREEN:
                return ((moment[getIndex(cube.f155r1, position, cube.f151b1)] - moment[getIndex(cube.f155r1, position, cube.f150b0)]) - moment[getIndex(cube.f154r0, position, cube.f151b1)]) + moment[getIndex(cube.f154r0, position, cube.f150b0)];
            case BLUE:
                return ((moment[getIndex(cube.f155r1, cube.f153g1, position)] - moment[getIndex(cube.f155r1, cube.f152g0, position)]) - moment[getIndex(cube.f154r0, cube.f153g1, position)]) + moment[getIndex(cube.f154r0, cube.f152g0, position)];
            default:
                throw new IllegalArgumentException("unexpected direction " + direction);
        }
    }

    private static final class MaximizeResult {
        int cutLocation;
        double maximum;

        MaximizeResult(int cut, double max) {
            this.cutLocation = cut;
            this.maximum = max;
        }
    }

    private static final class CreateBoxesResult {
        int resultCount;

        CreateBoxesResult(int requestedCount, int resultCount) {
            this.resultCount = resultCount;
        }
    }

    private static final class Box {

        /* renamed from: b0 */
        int f150b0;

        /* renamed from: b1 */
        int f151b1;

        /* renamed from: g0 */
        int f152g0;

        /* renamed from: g1 */
        int f153g1;

        /* renamed from: r0 */
        int f154r0;

        /* renamed from: r1 */
        int f155r1;
        int vol;

        private Box() {
            this.f154r0 = 0;
            this.f155r1 = 0;
            this.f152g0 = 0;
            this.f153g1 = 0;
            this.f150b0 = 0;
            this.f151b1 = 0;
            this.vol = 0;
        }
    }
}
