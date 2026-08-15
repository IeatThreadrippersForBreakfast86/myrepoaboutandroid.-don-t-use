package androidx.preference;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import kotlin.sequences.Sequence;

/* compiled from: PreferenceGroup.kt */
@Metadata(m145d1 = {"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0003\u001a\u0015\u0010\n\u001a\u00020\u000b*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\u0002\u001a3\u0010\r\u001a\u00020\u000e*\u00020\u00032!\u0010\u000f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0010H\u0086\bø\u0001\u0000\u001aH\u0010\u0013\u001a\u00020\u000e*\u00020\u000326\u0010\u000f\u001a2\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u000e0\u0014H\u0086\bø\u0001\u0000\u001a&\u0010\u0016\u001a\u0004\u0018\u0001H\u0017\"\b\b\u0000\u0010\u0017*\u00020\u0002*\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0019H\u0086\n¢\u0006\u0002\u0010\u001a\u001a\u0015\u0010\u0016\u001a\u00020\u0002*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0007H\u0086\u0002\u001a\r\u0010\u001b\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\r\u0010\u001c\u001a\u00020\u000b*\u00020\u0003H\u0086\b\u001a\u0013\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00020\u001e*\u00020\u0003H\u0086\u0002\u001a\u0015\u0010\u001f\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\u001a\u0015\u0010 \u001a\u00020\u000e*\u00020\u00032\u0006\u0010\f\u001a\u00020\u0002H\u0086\n\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0016\u0010\u0006\u001a\u00020\u0007*\u00020\u00038Æ\u0002¢\u0006\u0006\u001a\u0004\b\b\u0010\t\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006!"}, m146d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroidx/preference/Preference;", "Landroidx/preference/PreferenceGroup;", "getChildren", "(Landroidx/preference/PreferenceGroup;)Lkotlin/sequences/Sequence;", "size", "", "getSize", "(Landroidx/preference/PreferenceGroup;)I", "contains", "", "preference", "forEach", "", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "forEachIndexed", "Lkotlin/Function2;", "index", "get", "T", "key", "", "(Landroidx/preference/PreferenceGroup;Ljava/lang/CharSequence;)Landroidx/preference/Preference;", "isEmpty", "isNotEmpty", "iterator", "", "minusAssign", "plusAssign", "preference-ktx_release"}, m147k = 2, m148mv = {1, 6, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class PreferenceGroupKt {
    public static final <T extends Preference> T get(PreferenceGroup preferenceGroup, CharSequence key) {
        Intrinsics.checkNotNullParameter(preferenceGroup, "<this>");
        Intrinsics.checkNotNullParameter(key, "key");
        return (T) preferenceGroup.findPreference(key);
    }

    public static final Preference get(PreferenceGroup $this$get, int index) {
        Intrinsics.checkNotNullParameter($this$get, "<this>");
        Preference preference = $this$get.getPreference(index);
        Intrinsics.checkNotNullExpressionValue(preference, "getPreference(index)");
        return preference;
    }

    public static final boolean contains(PreferenceGroup $this$contains, Preference preference) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        Intrinsics.checkNotNullParameter(preference, "preference");
        int preferenceCount = $this$contains.getPreferenceCount();
        int i = 0;
        while (i < preferenceCount) {
            int index = i;
            i++;
            if (Intrinsics.areEqual($this$contains.getPreference(index), preference)) {
                return true;
            }
        }
        return false;
    }

    public static final void plusAssign(PreferenceGroup $this$plusAssign, Preference preference) {
        Intrinsics.checkNotNullParameter($this$plusAssign, "<this>");
        Intrinsics.checkNotNullParameter(preference, "preference");
        $this$plusAssign.addPreference(preference);
    }

    public static final void minusAssign(PreferenceGroup $this$minusAssign, Preference preference) {
        Intrinsics.checkNotNullParameter($this$minusAssign, "<this>");
        Intrinsics.checkNotNullParameter(preference, "preference");
        $this$minusAssign.removePreference(preference);
    }

    public static final int getSize(PreferenceGroup $this$size) {
        Intrinsics.checkNotNullParameter($this$size, "<this>");
        return $this$size.getPreferenceCount();
    }

    public static final boolean isEmpty(PreferenceGroup $this$isEmpty) {
        Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.getPreferenceCount() == 0;
    }

    public static final boolean isNotEmpty(PreferenceGroup $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.getPreferenceCount() != 0;
    }

    public static final void forEach(PreferenceGroup $this$forEach, Function1<? super Preference, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        int preferenceCount = $this$forEach.getPreferenceCount();
        int i = 0;
        while (i < preferenceCount) {
            int index = i;
            i++;
            action.invoke(get($this$forEach, index));
        }
    }

    public static final void forEachIndexed(PreferenceGroup $this$forEachIndexed, Function2<? super Integer, ? super Preference, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEachIndexed, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        int preferenceCount = $this$forEachIndexed.getPreferenceCount();
        int i = 0;
        while (i < preferenceCount) {
            int index = i;
            i++;
            action.invoke(Integer.valueOf(index), get($this$forEachIndexed, index));
        }
    }

    /* compiled from: PreferenceGroup.kt */
    @Metadata(m145d1 = {"\u0000#\n\u0000\n\u0002\u0010)\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0005\u001a\u00020\u0006H\u0096\u0002J\t\u0010\u0007\u001a\u00020\u0002H\u0096\u0002J\b\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\n"}, m146d2 = {"androidx/preference/PreferenceGroupKt$iterator$1", "", "Landroidx/preference/Preference;", "index", "", "hasNext", "", "next", "remove", "", "preference-ktx_release"}, m147k = 1, m148mv = {1, 6, 0}, m150xi = 48)
    /* renamed from: androidx.preference.PreferenceGroupKt$iterator$1 */
    public static final class C04801 implements Iterator<Preference>, KMutableIterator {
        final /* synthetic */ PreferenceGroup $this_iterator;
        private int index;

        C04801(PreferenceGroup $receiver) {
            this.$this_iterator = $receiver;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            int i = this.index;
            PreferenceGroup $this$size$iv = this.$this_iterator;
            return i < $this$size$iv.getPreferenceCount();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Preference next() {
            PreferenceGroup preferenceGroup = this.$this_iterator;
            int i = this.index;
            this.index = i + 1;
            Preference preference = preferenceGroup.getPreference(i);
            Intrinsics.checkNotNullExpressionValue(preference, "getPreference(index++)");
            return preference;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.index--;
            this.$this_iterator.removePreference(this.$this_iterator.getPreference(this.index));
        }
    }

    public static final Iterator<Preference> iterator(PreferenceGroup $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new C04801($this$iterator);
    }

    public static final Sequence<Preference> getChildren(final PreferenceGroup $this$children) {
        Intrinsics.checkNotNullParameter($this$children, "<this>");
        return new Sequence<Preference>() { // from class: androidx.preference.PreferenceGroupKt$children$1
            @Override // kotlin.sequences.Sequence
            public Iterator<Preference> iterator() {
                return PreferenceGroupKt.iterator($this$children);
            }
        };
    }
}
