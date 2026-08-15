package androidx.fragment.app;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FragmentManager.kt */
@Metadata(m144bv = {1, 0, 3}, m145d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a3\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\bH\u0086\bø\u0001\u0000\u001a3\u0010\t\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\bH\u0086\bø\u0001\u0000\u001a=\u0010\n\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\bH\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\f"}, m146d2 = {"commit", "", "Landroidx/fragment/app/FragmentManager;", "allowStateLoss", "", "body", "Lkotlin/Function1;", "Landroidx/fragment/app/FragmentTransaction;", "Lkotlin/ExtensionFunctionType;", "commitNow", "transaction", "now", "fragment-ktx_release"}, m147k = 2, m148mv = {1, 4, 1})
/* loaded from: classes.dex */
public final class FragmentManagerKt {
    public static /* synthetic */ void commit$default(FragmentManager commit, boolean allowStateLoss, Function1 body, int i, Object obj) {
        if ((i & 1) != 0) {
            allowStateLoss = false;
        }
        Intrinsics.checkNotNullParameter(commit, "$this$commit");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction = commit.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction, "beginTransaction()");
        body.invoke(transaction);
        if (allowStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public static final void commit(FragmentManager commit, boolean allowStateLoss, Function1<? super FragmentTransaction, Unit> body) {
        Intrinsics.checkNotNullParameter(commit, "$this$commit");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction = commit.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction, "beginTransaction()");
        body.invoke(transaction);
        if (allowStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public static /* synthetic */ void commitNow$default(FragmentManager commitNow, boolean allowStateLoss, Function1 body, int i, Object obj) {
        if ((i & 1) != 0) {
            allowStateLoss = false;
        }
        Intrinsics.checkNotNullParameter(commitNow, "$this$commitNow");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction = commitNow.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction, "beginTransaction()");
        body.invoke(transaction);
        if (allowStateLoss) {
            transaction.commitNowAllowingStateLoss();
        } else {
            transaction.commitNow();
        }
    }

    public static final void commitNow(FragmentManager commitNow, boolean allowStateLoss, Function1<? super FragmentTransaction, Unit> body) {
        Intrinsics.checkNotNullParameter(commitNow, "$this$commitNow");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction = commitNow.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction, "beginTransaction()");
        body.invoke(transaction);
        if (allowStateLoss) {
            transaction.commitNowAllowingStateLoss();
        } else {
            transaction.commitNow();
        }
    }

    public static /* synthetic */ void transaction$default(FragmentManager transaction, boolean now, boolean allowStateLoss, Function1 body, int i, Object obj) {
        if ((i & 1) != 0) {
            now = false;
        }
        if ((i & 2) != 0) {
            allowStateLoss = false;
        }
        Intrinsics.checkNotNullParameter(transaction, "$this$transaction");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction2 = transaction.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction2, "beginTransaction()");
        body.invoke(transaction2);
        if (now) {
            if (allowStateLoss) {
                transaction2.commitNowAllowingStateLoss();
                return;
            } else {
                transaction2.commitNow();
                return;
            }
        }
        if (allowStateLoss) {
            transaction2.commitAllowingStateLoss();
        } else {
            transaction2.commit();
        }
    }

    @Deprecated(message = "Use commit { .. } or commitNow { .. } extensions")
    public static final void transaction(FragmentManager transaction, boolean now, boolean allowStateLoss, Function1<? super FragmentTransaction, Unit> body) {
        Intrinsics.checkNotNullParameter(transaction, "$this$transaction");
        Intrinsics.checkNotNullParameter(body, "body");
        FragmentTransaction transaction2 = transaction.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction2, "beginTransaction()");
        body.invoke(transaction2);
        if (now) {
            if (allowStateLoss) {
                transaction2.commitNowAllowingStateLoss();
                return;
            } else {
                transaction2.commitNow();
                return;
            }
        }
        if (allowStateLoss) {
            transaction2.commitAllowingStateLoss();
        } else {
            transaction2.commit();
        }
    }
}
