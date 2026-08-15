package androidx.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.ViewTreeOnBackPressedDispatcherOwner;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.PreferenceFragmentCompat;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PreferenceHeaderFragmentCompat.kt */
@Metadata(m145d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u00012\u00020\u0002:\u0001&B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0017J\n\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u0016J\b\u0010\u0012\u001a\u00020\u0013H&J$\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0017J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u001eH\u0017J\u001a\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0017J\u0012\u0010!\u001a\u00020\u000e2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u0012\u0010\"\u001a\u00020\u000e2\b\u0010#\u001a\u0004\u0018\u00010$H\u0002J\u0010\u0010\"\u001a\u00020\u000e2\u0006\u0010%\u001a\u00020\u001eH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006'"}, m146d2 = {"Landroidx/preference/PreferenceHeaderFragmentCompat;", "Landroidx/fragment/app/Fragment;", "Landroidx/preference/PreferenceFragmentCompat$OnPreferenceStartFragmentCallback;", "()V", "onBackPressedCallback", "Landroidx/activity/OnBackPressedCallback;", "slidingPaneLayout", "Landroidx/slidingpanelayout/widget/SlidingPaneLayout;", "getSlidingPaneLayout", "()Landroidx/slidingpanelayout/widget/SlidingPaneLayout;", "buildContentView", "inflater", "Landroid/view/LayoutInflater;", "onAttach", "", "context", "Landroid/content/Context;", "onCreateInitialDetailFragment", "onCreatePreferenceHeader", "Landroidx/preference/PreferenceFragmentCompat;", "onCreateView", "Landroid/view/View;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onPreferenceStartFragment", "", "caller", "pref", "Landroidx/preference/Preference;", "onViewCreated", "view", "onViewStateRestored", "openPreferenceHeader", "intent", "Landroid/content/Intent;", "header", "InnerOnBackPressedCallback", "preference_release"}, m147k = 1, m148mv = {1, 6, 0}, m150xi = 48)
/* loaded from: classes.dex */
public abstract class PreferenceHeaderFragmentCompat extends Fragment implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private OnBackPressedCallback onBackPressedCallback;

    public abstract PreferenceFragmentCompat onCreatePreferenceHeader();

    public final SlidingPaneLayout getSlidingPaneLayout() {
        return (SlidingPaneLayout) requireView();
    }

    @Override // androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        Intrinsics.checkNotNullParameter(caller, "caller");
        Intrinsics.checkNotNullParameter(pref, "pref");
        if (caller.getId() == C0483R.id.preferences_header) {
            openPreferenceHeader(pref);
            return true;
        }
        if (caller.getId() == C0483R.id.preferences_detail) {
            FragmentFactory fragmentFactory = getChildFragmentManager().getFragmentFactory();
            ClassLoader classLoader = requireContext().getClassLoader();
            String fragment = pref.getFragment();
            Intrinsics.checkNotNull(fragment);
            Fragment frag = fragmentFactory.instantiate(classLoader, fragment);
            Intrinsics.checkNotNullExpressionValue(frag, "childFragmentManager.fra….fragment!!\n            )");
            frag.setArguments(pref.getExtras());
            FragmentManager childFragmentManager = getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
            FragmentTransaction transaction$iv = childFragmentManager.beginTransaction();
            Intrinsics.checkNotNullExpressionValue(transaction$iv, "beginTransaction()");
            transaction$iv.setReorderingAllowed(true);
            transaction$iv.replace(C0483R.id.preferences_detail, frag);
            transaction$iv.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction$iv.addToBackStack(null);
            transaction$iv.commit();
            return true;
        }
        return false;
    }

    /* compiled from: PreferenceHeaderFragmentCompat.kt */
    @Metadata(m145d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, m146d2 = {"Landroidx/preference/PreferenceHeaderFragmentCompat$InnerOnBackPressedCallback;", "Landroidx/activity/OnBackPressedCallback;", "Landroidx/slidingpanelayout/widget/SlidingPaneLayout$PanelSlideListener;", "caller", "Landroidx/preference/PreferenceHeaderFragmentCompat;", "(Landroidx/preference/PreferenceHeaderFragmentCompat;)V", "handleOnBackPressed", "", "onPanelClosed", "panel", "Landroid/view/View;", "onPanelOpened", "onPanelSlide", "slideOffset", "", "preference_release"}, m147k = 1, m148mv = {1, 6, 0}, m150xi = 48)
    private static final class InnerOnBackPressedCallback extends OnBackPressedCallback implements SlidingPaneLayout.PanelSlideListener {
        private final PreferenceHeaderFragmentCompat caller;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InnerOnBackPressedCallback(PreferenceHeaderFragmentCompat caller) {
            super(true);
            Intrinsics.checkNotNullParameter(caller, "caller");
            this.caller = caller;
            this.caller.getSlidingPaneLayout().addPanelSlideListener(this);
        }

        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            this.caller.getSlidingPaneLayout().closePane();
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelSlide(View panel, float slideOffset) {
            Intrinsics.checkNotNullParameter(panel, "panel");
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelOpened(View panel) {
            Intrinsics.checkNotNullParameter(panel, "panel");
            setEnabled(true);
        }

        @Override // androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelClosed(View panel) {
            Intrinsics.checkNotNullParameter(panel, "panel");
            setEnabled(false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super.onAttach(context);
        FragmentManager parentFragmentManager = getParentFragmentManager();
        Intrinsics.checkNotNullExpressionValue(parentFragmentManager, "parentFragmentManager");
        FragmentTransaction transaction$iv = parentFragmentManager.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction$iv, "beginTransaction()");
        transaction$iv.setPrimaryNavigationFragment(this);
        transaction$iv.commit();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        SlidingPaneLayout slidingPaneLayout = buildContentView(inflater);
        Fragment existingHeaderFragment = getChildFragmentManager().findFragmentById(C0483R.id.preferences_header);
        if (existingHeaderFragment == null) {
            PreferenceFragmentCompat newHeaderFragment = onCreatePreferenceHeader();
            FragmentManager childFragmentManager = getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
            FragmentTransaction transaction$iv = childFragmentManager.beginTransaction();
            Intrinsics.checkNotNullExpressionValue(transaction$iv, "beginTransaction()");
            transaction$iv.setReorderingAllowed(true);
            transaction$iv.add(C0483R.id.preferences_header, newHeaderFragment);
            transaction$iv.commit();
        }
        slidingPaneLayout.setLockMode(3);
        return slidingPaneLayout;
    }

    private final SlidingPaneLayout buildContentView(LayoutInflater inflater) {
        SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(inflater.getContext());
        slidingPaneLayout.setId(C0483R.id.preferences_sliding_pane_layout);
        FragmentContainerView $this$buildContentView_u24lambda_u2d5 = new FragmentContainerView(inflater.getContext());
        $this$buildContentView_u24lambda_u2d5.setId(C0483R.id.preferences_header);
        SlidingPaneLayout.LayoutParams $this$buildContentView_u24lambda_u2d6 = new SlidingPaneLayout.LayoutParams(getResources().getDimensionPixelSize(C0483R.dimen.preferences_header_width), -1);
        $this$buildContentView_u24lambda_u2d6.weight = getResources().getInteger(C0483R.integer.preferences_header_pane_weight);
        slidingPaneLayout.addView($this$buildContentView_u24lambda_u2d5, $this$buildContentView_u24lambda_u2d6);
        FragmentContainerView $this$buildContentView_u24lambda_u2d7 = new FragmentContainerView(inflater.getContext());
        $this$buildContentView_u24lambda_u2d7.setId(C0483R.id.preferences_detail);
        SlidingPaneLayout.LayoutParams $this$buildContentView_u24lambda_u2d8 = new SlidingPaneLayout.LayoutParams(getResources().getDimensionPixelSize(C0483R.dimen.preferences_detail_width), -1);
        $this$buildContentView_u24lambda_u2d8.weight = getResources().getInteger(C0483R.integer.preferences_detail_pane_weight);
        slidingPaneLayout.addView($this$buildContentView_u24lambda_u2d7, $this$buildContentView_u24lambda_u2d8);
        return slidingPaneLayout;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        OnBackPressedDispatcher onBackPressedDispatcher;
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.onBackPressedCallback = new InnerOnBackPressedCallback(this);
        View $this$doOnLayout$iv = getSlidingPaneLayout();
        if (ViewCompat.isLaidOut($this$doOnLayout$iv) && !$this$doOnLayout$iv.isLayoutRequested()) {
            OnBackPressedCallback onBackPressedCallback = this.onBackPressedCallback;
            Intrinsics.checkNotNull(onBackPressedCallback);
            onBackPressedCallback.setEnabled(getSlidingPaneLayout().isSlideable() && getSlidingPaneLayout().isOpen());
        } else {
            $this$doOnLayout$iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: androidx.preference.PreferenceHeaderFragmentCompat$onViewCreated$$inlined$doOnLayout$1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view2, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Intrinsics.checkParameterIsNotNull(view2, "view");
                    view2.removeOnLayoutChangeListener(this);
                    OnBackPressedCallback onBackPressedCallback2 = this.this$0.onBackPressedCallback;
                    Intrinsics.checkNotNull(onBackPressedCallback2);
                    onBackPressedCallback2.setEnabled(this.this$0.getSlidingPaneLayout().isSlideable() && this.this$0.getSlidingPaneLayout().isOpen());
                }
            });
        }
        getChildFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { // from class: androidx.preference.PreferenceHeaderFragmentCompat$$ExternalSyntheticLambda0
            @Override // androidx.fragment.app.FragmentManager.OnBackStackChangedListener
            public final void onBackStackChanged() {
                PreferenceHeaderFragmentCompat.m231onViewCreated$lambda10(this.f$0);
            }
        });
        OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = ViewTreeOnBackPressedDispatcherOwner.get(view);
        if (onBackPressedDispatcherOwner != null && (onBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher()) != null) {
            LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
            OnBackPressedCallback onBackPressedCallback2 = this.onBackPressedCallback;
            Intrinsics.checkNotNull(onBackPressedCallback2);
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onViewCreated$lambda-10, reason: not valid java name */
    public static final void m231onViewCreated$lambda10(PreferenceHeaderFragmentCompat this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        OnBackPressedCallback onBackPressedCallback = this$0.onBackPressedCallback;
        Intrinsics.checkNotNull(onBackPressedCallback);
        onBackPressedCallback.setEnabled(this$0.getChildFragmentManager().getBackStackEntryCount() == 0);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle savedInstanceState) {
        Fragment it;
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null && (it = onCreateInitialDetailFragment()) != null) {
            FragmentManager childFragmentManager = getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
            FragmentTransaction transaction$iv = childFragmentManager.beginTransaction();
            Intrinsics.checkNotNullExpressionValue(transaction$iv, "beginTransaction()");
            transaction$iv.setReorderingAllowed(true);
            transaction$iv.replace(C0483R.id.preferences_detail, it);
            transaction$iv.commit();
        }
    }

    public Fragment onCreateInitialDetailFragment() {
        Fragment fragmentFindFragmentById = getChildFragmentManager().findFragmentById(C0483R.id.preferences_header);
        if (fragmentFindFragmentById == null) {
            throw new NullPointerException("null cannot be cast to non-null type androidx.preference.PreferenceFragmentCompat");
        }
        PreferenceFragmentCompat headerFragment = (PreferenceFragmentCompat) fragmentFindFragmentById;
        if (headerFragment.getPreferenceScreen().getPreferenceCount() <= 0) {
            return null;
        }
        int preferenceCount = headerFragment.getPreferenceScreen().getPreferenceCount();
        int i = 0;
        while (i < preferenceCount) {
            int index = i;
            i++;
            Preference header = headerFragment.getPreferenceScreen().getPreference(index);
            Intrinsics.checkNotNullExpressionValue(header, "headerFragment.preferenc…reen.getPreference(index)");
            if (header.getFragment() != null) {
                String it = header.getFragment();
                Fragment fragment = it != null ? getChildFragmentManager().getFragmentFactory().instantiate(requireContext().getClassLoader(), it) : null;
                if (fragment != null) {
                    fragment.setArguments(header.getExtras());
                }
                return fragment;
            }
        }
        return null;
    }

    private final void openPreferenceHeader(Preference header) {
        if (header.getFragment() == null) {
            openPreferenceHeader(header.getIntent());
            return;
        }
        String it = header.getFragment();
        Fragment fragment = it == null ? null : getChildFragmentManager().getFragmentFactory().instantiate(requireContext().getClassLoader(), it);
        if (fragment != null) {
            Fragment $this$openPreferenceHeader_u24lambda_u2d16 = fragment;
            $this$openPreferenceHeader_u24lambda_u2d16.setArguments(header.getExtras());
        }
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = getChildFragmentManager().getBackStackEntryAt(0);
            Intrinsics.checkNotNullExpressionValue(entry, "childFragmentManager.getBackStackEntryAt(0)");
            getChildFragmentManager().popBackStack(entry.getId(), 1);
        }
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "childFragmentManager");
        FragmentTransaction transaction$iv = childFragmentManager.beginTransaction();
        Intrinsics.checkNotNullExpressionValue(transaction$iv, "beginTransaction()");
        transaction$iv.setReorderingAllowed(true);
        int i = C0483R.id.preferences_detail;
        Intrinsics.checkNotNull(fragment);
        transaction$iv.replace(i, fragment);
        if (getSlidingPaneLayout().isOpen()) {
            transaction$iv.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        getSlidingPaneLayout().openPane();
        transaction$iv.commit();
    }

    private final void openPreferenceHeader(Intent intent) {
        if (intent == null) {
            return;
        }
        startActivity(intent);
    }
}
