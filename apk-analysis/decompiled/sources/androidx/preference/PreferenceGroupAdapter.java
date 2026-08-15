package androidx.preference;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PreferenceGroupAdapter extends RecyclerView.Adapter<PreferenceViewHolder> implements Preference.OnPreferenceChangeInternalListener, PreferenceGroup.PreferencePositionCallback {
    private final PreferenceGroup mPreferenceGroup;
    private final List<PreferenceResourceDescriptor> mPreferenceResourceDescriptors;
    private List<Preference> mPreferences;
    private List<Preference> mVisiblePreferences;
    private final Runnable mSyncRunnable = new Runnable() { // from class: androidx.preference.PreferenceGroupAdapter.1
        @Override // java.lang.Runnable
        public void run() {
            PreferenceGroupAdapter.this.updatePreferences();
        }
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        this.mPreferences = new ArrayList();
        this.mVisiblePreferences = new ArrayList();
        this.mPreferenceResourceDescriptors = new ArrayList();
        if (this.mPreferenceGroup instanceof PreferenceScreen) {
            setHasStableIds(((PreferenceScreen) this.mPreferenceGroup).shouldUseGeneratedIds());
        } else {
            setHasStableIds(true);
        }
        updatePreferences();
    }

    void updatePreferences() {
        for (Preference preference : this.mPreferences) {
            preference.setOnPreferenceChangeInternalListener(null);
        }
        int size = this.mPreferences.size();
        this.mPreferences = new ArrayList(size);
        flattenPreferenceGroup(this.mPreferences, this.mPreferenceGroup);
        final List<Preference> oldVisibleList = this.mVisiblePreferences;
        final List<Preference> visiblePreferenceList = createVisiblePreferencesList(this.mPreferenceGroup);
        this.mVisiblePreferences = visiblePreferenceList;
        PreferenceManager preferenceManager = this.mPreferenceGroup.getPreferenceManager();
        if (preferenceManager != null && preferenceManager.getPreferenceComparisonCallback() != null) {
            final PreferenceManager.PreferenceComparisonCallback comparisonCallback = preferenceManager.getPreferenceComparisonCallback();
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: androidx.preference.PreferenceGroupAdapter.2
                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getOldListSize() {
                    return oldVisibleList.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public int getNewListSize() {
                    return visiblePreferenceList.size();
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceItemsTheSame((Preference) oldVisibleList.get(oldItemPosition), (Preference) visiblePreferenceList.get(newItemPosition));
                }

                @Override // androidx.recyclerview.widget.DiffUtil.Callback
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceContentsTheSame((Preference) oldVisibleList.get(oldItemPosition), (Preference) visiblePreferenceList.get(newItemPosition));
                }
            });
            result.dispatchUpdatesTo(this);
        } else {
            notifyDataSetChanged();
        }
        for (Preference preference2 : this.mPreferences) {
            preference2.clearWasDetached();
        }
    }

    private void flattenPreferenceGroup(List<Preference> preferences, PreferenceGroup group) {
        group.sortPreferences();
        int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            Preference preference = group.getPreference(i);
            preferences.add(preference);
            PreferenceResourceDescriptor descriptor = new PreferenceResourceDescriptor(preference);
            if (!this.mPreferenceResourceDescriptors.contains(descriptor)) {
                this.mPreferenceResourceDescriptors.add(descriptor);
            }
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup nestedGroup = (PreferenceGroup) preference;
                if (nestedGroup.isOnSameScreenAsChildren()) {
                    flattenPreferenceGroup(preferences, nestedGroup);
                }
            }
            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    private List<Preference> createVisiblePreferencesList(PreferenceGroup group) {
        int visiblePreferenceCount = 0;
        List<Preference> visiblePreferences = new ArrayList<>();
        List<Preference> collapsedPreferences = new ArrayList<>();
        int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            Preference preference = group.getPreference(i);
            if (preference.isVisible()) {
                if (!isGroupExpandable(group) || visiblePreferenceCount < group.getInitialExpandedChildrenCount()) {
                    visiblePreferences.add(preference);
                } else {
                    collapsedPreferences.add(preference);
                }
                if (!(preference instanceof PreferenceGroup)) {
                    visiblePreferenceCount++;
                } else {
                    PreferenceGroup innerGroup = (PreferenceGroup) preference;
                    if (!innerGroup.isOnSameScreenAsChildren()) {
                        continue;
                    } else {
                        if (isGroupExpandable(group) && isGroupExpandable(innerGroup)) {
                            throw new IllegalStateException("Nesting an expandable group inside of another expandable group is not supported!");
                        }
                        List<Preference> innerList = createVisiblePreferencesList(innerGroup);
                        for (Preference inner : innerList) {
                            if (!isGroupExpandable(group) || visiblePreferenceCount < group.getInitialExpandedChildrenCount()) {
                                visiblePreferences.add(inner);
                            } else {
                                collapsedPreferences.add(inner);
                            }
                            visiblePreferenceCount++;
                        }
                    }
                }
            }
        }
        if (isGroupExpandable(group) && visiblePreferenceCount > group.getInitialExpandedChildrenCount()) {
            ExpandButton expandButton = createExpandButton(group, collapsedPreferences);
            visiblePreferences.add(expandButton);
        }
        return visiblePreferences;
    }

    private ExpandButton createExpandButton(final PreferenceGroup group, List<Preference> collapsedPreferences) {
        ExpandButton preference = new ExpandButton(group.getContext(), collapsedPreferences, group.getId());
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: androidx.preference.PreferenceGroupAdapter.3
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference2) {
                group.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
                PreferenceGroupAdapter.this.onPreferenceHierarchyChange(preference2);
                PreferenceGroup.OnExpandButtonClickListener listener = group.getOnExpandButtonClickListener();
                if (listener != null) {
                    listener.onExpandButtonClick();
                    return true;
                }
                return true;
            }
        });
        return preference;
    }

    private boolean isGroupExpandable(PreferenceGroup preferenceGroup) {
        return preferenceGroup.getInitialExpandedChildrenCount() != Integer.MAX_VALUE;
    }

    public Preference getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.mVisiblePreferences.get(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mVisiblePreferences.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        if (!hasStableIds()) {
            return -1L;
        }
        return getItem(position).getId();
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeInternalListener
    public void onPreferenceChange(Preference preference) {
        int index = this.mVisiblePreferences.indexOf(preference);
        if (index != -1) {
            notifyItemChanged(index, preference);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeInternalListener
    public void onPreferenceHierarchyChange(Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeInternalListener
    public void onPreferenceVisibilityChange(Preference preference) {
        onPreferenceHierarchyChange(preference);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        Preference preference = getItem(position);
        PreferenceResourceDescriptor descriptor = new PreferenceResourceDescriptor(preference);
        int viewType = this.mPreferenceResourceDescriptors.indexOf(descriptor);
        if (viewType != -1) {
            return viewType;
        }
        int viewType2 = this.mPreferenceResourceDescriptors.size();
        this.mPreferenceResourceDescriptors.add(descriptor);
        return viewType2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public PreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PreferenceResourceDescriptor descriptor = this.mPreferenceResourceDescriptors.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TypedArray a = parent.getContext().obtainStyledAttributes((AttributeSet) null, C0483R.styleable.BackgroundStyle);
        Drawable background = a.getDrawable(C0483R.styleable.BackgroundStyle_android_selectableItemBackground);
        if (background == null) {
            background = AppCompatResources.getDrawable(parent.getContext(), android.R.drawable.list_selector_background);
        }
        a.recycle();
        View view = inflater.inflate(descriptor.mLayoutResId, parent, false);
        if (view.getBackground() == null) {
            ViewCompat.setBackground(view, background);
        }
        ViewGroup widgetFrame = (ViewGroup) view.findViewById(android.R.id.widget_frame);
        if (widgetFrame != null) {
            if (descriptor.mWidgetLayoutResId != 0) {
                inflater.inflate(descriptor.mWidgetLayoutResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(8);
            }
        }
        return new PreferenceViewHolder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PreferenceViewHolder holder, int position) {
        Preference preference = getItem(position);
        holder.resetState();
        preference.onBindViewHolder(holder);
    }

    @Override // androidx.preference.PreferenceGroup.PreferencePositionCallback
    public int getPreferenceAdapterPosition(String key) {
        int size = this.mVisiblePreferences.size();
        for (int i = 0; i < size; i++) {
            Preference candidate = this.mVisiblePreferences.get(i);
            if (TextUtils.equals(key, candidate.getKey())) {
                return i;
            }
        }
        return -1;
    }

    @Override // androidx.preference.PreferenceGroup.PreferencePositionCallback
    public int getPreferenceAdapterPosition(Preference preference) {
        int size = this.mVisiblePreferences.size();
        for (int i = 0; i < size; i++) {
            Preference candidate = this.mVisiblePreferences.get(i);
            if (candidate != null && candidate.equals(preference)) {
                return i;
            }
        }
        return -1;
    }

    private static class PreferenceResourceDescriptor {
        String mClassName;
        int mLayoutResId;
        int mWidgetLayoutResId;

        PreferenceResourceDescriptor(Preference preference) {
            this.mClassName = preference.getClass().getName();
            this.mLayoutResId = preference.getLayoutResource();
            this.mWidgetLayoutResId = preference.getWidgetLayoutResource();
        }

        public boolean equals(Object o) {
            if (!(o instanceof PreferenceResourceDescriptor)) {
                return false;
            }
            PreferenceResourceDescriptor other = (PreferenceResourceDescriptor) o;
            return this.mLayoutResId == other.mLayoutResId && this.mWidgetLayoutResId == other.mWidgetLayoutResId && TextUtils.equals(this.mClassName, other.mClassName);
        }

        public int hashCode() {
            int result = (17 * 31) + this.mLayoutResId;
            return (((result * 31) + this.mWidgetLayoutResId) * 31) + this.mClassName.hashCode();
        }
    }
}
