package androidx.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.TextView;
import androidx.preference.DialogPreference;

@Deprecated
/* loaded from: classes.dex */
public abstract class PreferenceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    @Deprecated
    protected static final String ARG_KEY = "key";
    private static final String SAVE_STATE_ICON = "PreferenceDialogFragment.icon";
    private static final String SAVE_STATE_LAYOUT = "PreferenceDialogFragment.layout";
    private static final String SAVE_STATE_MESSAGE = "PreferenceDialogFragment.message";
    private static final String SAVE_STATE_NEGATIVE_TEXT = "PreferenceDialogFragment.negativeText";
    private static final String SAVE_STATE_POSITIVE_TEXT = "PreferenceDialogFragment.positiveText";
    private static final String SAVE_STATE_TITLE = "PreferenceDialogFragment.title";
    private BitmapDrawable mDialogIcon;
    private int mDialogLayoutRes;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private DialogPreference mPreference;
    private int mWhichButtonClicked;

    @Deprecated
    public abstract void onDialogClosed(boolean z);

    @Deprecated
    public PreferenceDialogFragment() {
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComponentCallbacks2 targetFragment = getTargetFragment();
        if (!(targetFragment instanceof DialogPreference.TargetFragment)) {
            throw new IllegalStateException("Target fragment must implement TargetFragment interface");
        }
        DialogPreference.TargetFragment fragment = (DialogPreference.TargetFragment) targetFragment;
        String key = getArguments().getString(ARG_KEY);
        if (savedInstanceState == null) {
            this.mPreference = (DialogPreference) fragment.findPreference(key);
            this.mDialogTitle = this.mPreference.getDialogTitle();
            this.mPositiveButtonText = this.mPreference.getPositiveButtonText();
            this.mNegativeButtonText = this.mPreference.getNegativeButtonText();
            this.mDialogMessage = this.mPreference.getDialogMessage();
            this.mDialogLayoutRes = this.mPreference.getDialogLayoutResource();
            Drawable icon = this.mPreference.getDialogIcon();
            if (icon == null || (icon instanceof BitmapDrawable)) {
                this.mDialogIcon = (BitmapDrawable) icon;
                return;
            }
            Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            icon.draw(canvas);
            this.mDialogIcon = new BitmapDrawable(getResources(), bitmap);
            return;
        }
        this.mDialogTitle = savedInstanceState.getCharSequence(SAVE_STATE_TITLE);
        this.mPositiveButtonText = savedInstanceState.getCharSequence(SAVE_STATE_POSITIVE_TEXT);
        this.mNegativeButtonText = savedInstanceState.getCharSequence(SAVE_STATE_NEGATIVE_TEXT);
        this.mDialogMessage = savedInstanceState.getCharSequence(SAVE_STATE_MESSAGE);
        this.mDialogLayoutRes = savedInstanceState.getInt(SAVE_STATE_LAYOUT, 0);
        Bitmap bitmap2 = (Bitmap) savedInstanceState.getParcelable(SAVE_STATE_ICON);
        if (bitmap2 != null) {
            this.mDialogIcon = new BitmapDrawable(getResources(), bitmap2);
        }
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(SAVE_STATE_TITLE, this.mDialogTitle);
        outState.putCharSequence(SAVE_STATE_POSITIVE_TEXT, this.mPositiveButtonText);
        outState.putCharSequence(SAVE_STATE_NEGATIVE_TEXT, this.mNegativeButtonText);
        outState.putCharSequence(SAVE_STATE_MESSAGE, this.mDialogMessage);
        outState.putInt(SAVE_STATE_LAYOUT, this.mDialogLayoutRes);
        if (this.mDialogIcon != null) {
            outState.putParcelable(SAVE_STATE_ICON, this.mDialogIcon.getBitmap());
        }
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        this.mWhichButtonClicked = -2;
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(this.mDialogTitle).setIcon(this.mDialogIcon).setPositiveButton(this.mPositiveButtonText, this).setNegativeButton(this.mNegativeButtonText, this);
        View contentView = onCreateDialogView(context);
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.setView(contentView);
        } else {
            builder.setMessage(this.mDialogMessage);
        }
        onPrepareDialogBuilder(builder);
        Dialog dialog = builder.create();
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        return dialog;
    }

    @Deprecated
    public DialogPreference getPreference() {
        if (this.mPreference == null) {
            String key = getArguments().getString(ARG_KEY);
            DialogPreference.TargetFragment fragment = (DialogPreference.TargetFragment) getTargetFragment();
            this.mPreference = (DialogPreference) fragment.findPreference(key);
        }
        return this.mPreference;
    }

    @Deprecated
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    }

    protected boolean needInputMethod() {
        return false;
    }

    private void requestInputMethod(Dialog dialog) {
        Window window = dialog.getWindow();
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.showIme(window);
        } else {
            window.setSoftInputMode(5);
        }
    }

    @Deprecated
    protected View onCreateDialogView(Context context) {
        int resId = this.mDialogLayoutRes;
        if (resId == 0) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(resId, (ViewGroup) null);
    }

    @Deprecated
    protected void onBindDialogView(View view) {
        View dialogMessageView = view.findViewById(android.R.id.message);
        if (dialogMessageView != null) {
            CharSequence message = this.mDialogMessage;
            int newVisibility = 8;
            if (!TextUtils.isEmpty(message)) {
                if (dialogMessageView instanceof TextView) {
                    ((TextView) dialogMessageView).setText(message);
                }
                newVisibility = 0;
            }
            if (dialogMessageView.getVisibility() != newVisibility) {
                dialogMessageView.setVisibility(newVisibility);
            }
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    @Deprecated
    public void onClick(DialogInterface dialog, int which) {
        this.mWhichButtonClicked = which;
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDialogClosed(this.mWhichButtonClicked == -1);
    }

    private static class Api30Impl {
        private Api30Impl() {
        }

        static void showIme(Window dialogWindow) {
            dialogWindow.getDecorView().getWindowInsetsController().show(WindowInsets.Type.ime());
        }
    }
}
