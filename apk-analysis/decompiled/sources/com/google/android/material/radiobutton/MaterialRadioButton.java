package com.google.android.material.radiobutton;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.C0633R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

/* loaded from: classes.dex */
public class MaterialRadioButton extends AppCompatRadioButton {
    private static final int DEF_STYLE_RES = C0633R.style.Widget_MaterialComponents_CompoundButton_RadioButton;
    private static final int[][] ENABLED_CHECKED_STATES = {new int[]{R.attr.state_enabled, R.attr.state_checked}, new int[]{R.attr.state_enabled, -16842912}, new int[]{-16842910, R.attr.state_checked}, new int[]{-16842910, -16842912}};
    private ColorStateList materialThemeColorsTintList;
    private boolean useMaterialThemeColors;

    public MaterialRadioButton(Context context) {
        this(context, null);
    }

    public MaterialRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, C0633R.attr.radioButtonStyle);
    }

    public MaterialRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        Context context2 = getContext();
        TypedArray attributes = ThemeEnforcement.obtainStyledAttributes(context2, attrs, C0633R.styleable.MaterialRadioButton, defStyleAttr, DEF_STYLE_RES, new int[0]);
        if (attributes.hasValue(C0633R.styleable.MaterialRadioButton_buttonTint)) {
            CompoundButtonCompat.setButtonTintList(this, MaterialResources.getColorStateList(context2, attributes, C0633R.styleable.MaterialRadioButton_buttonTint));
        }
        this.useMaterialThemeColors = attributes.getBoolean(C0633R.styleable.MaterialRadioButton_useMaterialThemeColors, false);
        attributes.recycle();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.useMaterialThemeColors && CompoundButtonCompat.getButtonTintList(this) == null) {
            setUseMaterialThemeColors(true);
        }
    }

    public void setUseMaterialThemeColors(boolean useMaterialThemeColors) {
        this.useMaterialThemeColors = useMaterialThemeColors;
        if (useMaterialThemeColors) {
            CompoundButtonCompat.setButtonTintList(this, getMaterialThemeColorsTintList());
        } else {
            CompoundButtonCompat.setButtonTintList(this, null);
        }
    }

    public boolean isUseMaterialThemeColors() {
        return this.useMaterialThemeColors;
    }

    private ColorStateList getMaterialThemeColorsTintList() {
        if (this.materialThemeColorsTintList == null) {
            int colorControlActivated = MaterialColors.getColor(this, C0633R.attr.colorControlActivated);
            int colorOnSurface = MaterialColors.getColor(this, C0633R.attr.colorOnSurface);
            int colorSurface = MaterialColors.getColor(this, C0633R.attr.colorSurface);
            int[] radioButtonColorList = new int[ENABLED_CHECKED_STATES.length];
            radioButtonColorList[0] = MaterialColors.layer(colorSurface, colorControlActivated, 1.0f);
            radioButtonColorList[1] = MaterialColors.layer(colorSurface, colorOnSurface, 0.54f);
            radioButtonColorList[2] = MaterialColors.layer(colorSurface, colorOnSurface, 0.38f);
            radioButtonColorList[3] = MaterialColors.layer(colorSurface, colorOnSurface, 0.38f);
            this.materialThemeColorsTintList = new ColorStateList(ENABLED_CHECKED_STATES, radioButtonColorList);
        }
        return this.materialThemeColorsTintList;
    }
}
