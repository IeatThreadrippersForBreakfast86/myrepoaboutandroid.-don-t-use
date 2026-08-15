package com.google.android.material.color;

import com.google.android.material.C0633R;

/* loaded from: classes.dex */
public final class HarmonizedColorAttributes {
    private static final int[] HARMONIZED_MATERIAL_ATTRIBUTES = {C0633R.attr.colorError, C0633R.attr.colorOnError, C0633R.attr.colorErrorContainer, C0633R.attr.colorOnErrorContainer};
    private final int[] attributes;
    private final int themeOverlay;

    public static HarmonizedColorAttributes create(int[] attributes) {
        return new HarmonizedColorAttributes(attributes, 0);
    }

    public static HarmonizedColorAttributes create(int[] attributes, int themeOverlay) {
        return new HarmonizedColorAttributes(attributes, themeOverlay);
    }

    public static HarmonizedColorAttributes createMaterialDefaults() {
        return create(HARMONIZED_MATERIAL_ATTRIBUTES, C0633R.style.ThemeOverlay_Material3_HarmonizedColors);
    }

    private HarmonizedColorAttributes(int[] attributes, int themeOverlay) {
        if (themeOverlay != 0 && attributes.length == 0) {
            throw new IllegalArgumentException("Theme overlay should be used with the accompanying int[] attributes.");
        }
        this.attributes = attributes;
        this.themeOverlay = themeOverlay;
    }

    public int[] getAttributes() {
        return this.attributes;
    }

    public int getThemeOverlay() {
        return this.themeOverlay;
    }
}
