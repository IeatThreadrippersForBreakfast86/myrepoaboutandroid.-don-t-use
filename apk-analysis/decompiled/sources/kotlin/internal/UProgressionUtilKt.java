package kotlin.internal;

import kotlin.Metadata;
import kotlin.UByte$$ExternalSyntheticBackport1;
import kotlin.UByte$$ExternalSyntheticBackport2;
import kotlin.UInt;
import kotlin.ULong;

/* compiled from: UProgressionUtil.kt */
@Metadata(m145d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a'\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002¢\u0006\u0004\b\u0005\u0010\u0006\u001a'\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\b\u0010\t\u001a'\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001¢\u0006\u0004\b\u000f\u0010\u0006\u001a'\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001¢\u0006\u0004\b\u0011\u0010\t¨\u0006\u0012"}, m146d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, m147k = 2, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA, reason: not valid java name */
    private static final int m1461differenceModuloWZ9TVnA(int a, int b, int c) {
        int ac = UByte$$ExternalSyntheticBackport1.m155m(a, c);
        int bc = UByte$$ExternalSyntheticBackport1.m155m(b, c);
        return UInt.m350constructorimpl(Integer.compare(ac ^ Integer.MIN_VALUE, bc ^ Integer.MIN_VALUE) >= 0 ? ac - bc : UInt.m350constructorimpl(ac - bc) + c);
    }

    /* renamed from: differenceModulo-sambcqE, reason: not valid java name */
    private static final long m1462differenceModulosambcqE(long a, long b, long c) {
        long ac = UByte$$ExternalSyntheticBackport2.m156m(a, c);
        long bc = UByte$$ExternalSyntheticBackport2.m156m(b, c);
        return ULong.m429constructorimpl(Long.compare(ac ^ Long.MIN_VALUE, bc ^ Long.MIN_VALUE) >= 0 ? ac - bc : ULong.m429constructorimpl(ac - bc) + c);
    }

    /* renamed from: getProgressionLastElement-Nkh28Cs, reason: not valid java name */
    public static final int m1464getProgressionLastElementNkh28Cs(int start, int end, int step) {
        if (step > 0) {
            if (Integer.compare(start ^ Integer.MIN_VALUE, end ^ Integer.MIN_VALUE) < 0) {
                return UInt.m350constructorimpl(end - m1461differenceModuloWZ9TVnA(end, start, UInt.m350constructorimpl(step)));
            }
        } else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (Integer.compare(start ^ Integer.MIN_VALUE, end ^ Integer.MIN_VALUE) > 0) {
                return UInt.m350constructorimpl(m1461differenceModuloWZ9TVnA(start, end, UInt.m350constructorimpl(-step)) + end);
            }
        }
        return end;
    }

    /* renamed from: getProgressionLastElement-7ftBX0g, reason: not valid java name */
    public static final long m1463getProgressionLastElement7ftBX0g(long start, long end, long step) {
        if (step > 0) {
            if (Long.compare(start ^ Long.MIN_VALUE, end ^ Long.MIN_VALUE) < 0) {
                return ULong.m429constructorimpl(end - m1462differenceModulosambcqE(end, start, ULong.m429constructorimpl(step)));
            }
        } else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (Long.compare(start ^ Long.MIN_VALUE, end ^ Long.MIN_VALUE) > 0) {
                return ULong.m429constructorimpl(m1462differenceModulosambcqE(start, end, ULong.m429constructorimpl(-step)) + end);
            }
        }
        return end;
    }
}
