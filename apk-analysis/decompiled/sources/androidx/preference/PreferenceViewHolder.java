package androidx.preference;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class PreferenceViewHolder extends RecyclerView.ViewHolder {
    private final Drawable mBackground;
    private final SparseArray<View> mCachedViews;
    private boolean mDividerAllowedAbove;
    private boolean mDividerAllowedBelow;
    private ColorStateList mTitleTextColors;

    PreferenceViewHolder(View itemView) {
        super(itemView);
        this.mCachedViews = new SparseArray<>(4);
        TextView titleView = (TextView) itemView.findViewById(android.R.id.title);
        this.mCachedViews.put(android.R.id.title, titleView);
        this.mCachedViews.put(android.R.id.summary, itemView.findViewById(android.R.id.summary));
        this.mCachedViews.put(android.R.id.icon, itemView.findViewById(android.R.id.icon));
        this.mCachedViews.put(C0483R.id.icon_frame, itemView.findViewById(C0483R.id.icon_frame));
        this.mCachedViews.put(16908350, itemView.findViewById(16908350));
        this.mBackground = itemView.getBackground();
        if (titleView != null) {
            this.mTitleTextColors = titleView.getTextColors();
        }
    }

    public static PreferenceViewHolder createInstanceForTests(View itemView) {
        return new PreferenceViewHolder(itemView);
    }

    public View findViewById(int id) {
        View cachedView = this.mCachedViews.get(id);
        if (cachedView != null) {
            return cachedView;
        }
        View v = this.itemView.findViewById(id);
        if (v != null) {
            this.mCachedViews.put(id, v);
        }
        return v;
    }

    public boolean isDividerAllowedAbove() {
        return this.mDividerAllowedAbove;
    }

    public void setDividerAllowedAbove(boolean allowed) {
        this.mDividerAllowedAbove = allowed;
    }

    public boolean isDividerAllowedBelow() {
        return this.mDividerAllowedBelow;
    }

    public void setDividerAllowedBelow(boolean allowed) {
        this.mDividerAllowedBelow = allowed;
    }

    void resetState() {
        if (this.itemView.getBackground() != this.mBackground) {
            ViewCompat.setBackground(this.itemView, this.mBackground);
        }
        TextView titleView = (TextView) findViewById(android.R.id.title);
        if (titleView != null && this.mTitleTextColors != null && !titleView.getTextColors().equals(this.mTitleTextColors)) {
            titleView.setTextColor(this.mTitleTextColors);
        }
    }
}
