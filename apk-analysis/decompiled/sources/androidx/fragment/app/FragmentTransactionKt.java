package androidx.fragment.app;

import android.os.Bundle;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FragmentTransaction.kt */
@Metadata(m144bv = {1, 0, 3}, m145d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a;\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00012\b\b\u0001\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tH\u0086\b\u001a-\u0010\u0000\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tH\u0086\b\u001a;\u0010\n\u001a\u00020\u0001\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\u00020\u00012\b\b\u0001\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tH\u0086\b¨\u0006\u000b"}, m146d2 = {"add", "Landroidx/fragment/app/FragmentTransaction;", "F", "Landroidx/fragment/app/Fragment;", "containerViewId", "", "tag", "", "args", "Landroid/os/Bundle;", "replace", "fragment-ktx_release"}, m147k = 2, m148mv = {1, 4, 1})
/* loaded from: classes.dex */
public final class FragmentTransactionKt {
    public static /* synthetic */ FragmentTransaction add$default(FragmentTransaction add, int containerViewId, String tag, Bundle args, int i, Object obj) {
        if ((i & 2) != 0) {
            tag = (String) null;
        }
        if ((i & 4) != 0) {
            args = (Bundle) null;
        }
        Intrinsics.checkNotNullParameter(add, "$this$add");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionAdd = add.add(containerViewId, Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionAdd, "add(containerViewId, F::class.java, args, tag)");
        return fragmentTransactionAdd;
    }

    public static final /* synthetic */ <F extends Fragment> FragmentTransaction add(FragmentTransaction add, int containerViewId, String tag, Bundle args) {
        Intrinsics.checkNotNullParameter(add, "$this$add");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionAdd = add.add(containerViewId, Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionAdd, "add(containerViewId, F::class.java, args, tag)");
        return fragmentTransactionAdd;
    }

    public static /* synthetic */ FragmentTransaction add$default(FragmentTransaction add, String tag, Bundle args, int i, Object obj) {
        if ((i & 2) != 0) {
            args = (Bundle) null;
        }
        Intrinsics.checkNotNullParameter(add, "$this$add");
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionAdd = add.add(Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionAdd, "add(F::class.java, args, tag)");
        return fragmentTransactionAdd;
    }

    public static final /* synthetic */ <F extends Fragment> FragmentTransaction add(FragmentTransaction add, String tag, Bundle args) {
        Intrinsics.checkNotNullParameter(add, "$this$add");
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionAdd = add.add(Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionAdd, "add(F::class.java, args, tag)");
        return fragmentTransactionAdd;
    }

    public static /* synthetic */ FragmentTransaction replace$default(FragmentTransaction replace, int containerViewId, String tag, Bundle args, int i, Object obj) {
        if ((i & 2) != 0) {
            tag = (String) null;
        }
        if ((i & 4) != 0) {
            args = (Bundle) null;
        }
        Intrinsics.checkNotNullParameter(replace, "$this$replace");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionReplace = replace.replace(containerViewId, Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionReplace, "replace(containerViewId, F::class.java, args, tag)");
        return fragmentTransactionReplace;
    }

    public static final /* synthetic */ <F extends Fragment> FragmentTransaction replace(FragmentTransaction replace, int containerViewId, String tag, Bundle args) {
        Intrinsics.checkNotNullParameter(replace, "$this$replace");
        Intrinsics.reifiedOperationMarker(4, "F");
        FragmentTransaction fragmentTransactionReplace = replace.replace(containerViewId, Fragment.class, args, tag);
        Intrinsics.checkNotNullExpressionValue(fragmentTransactionReplace, "replace(containerViewId, F::class.java, args, tag)");
        return fragmentTransactionReplace;
    }
}
