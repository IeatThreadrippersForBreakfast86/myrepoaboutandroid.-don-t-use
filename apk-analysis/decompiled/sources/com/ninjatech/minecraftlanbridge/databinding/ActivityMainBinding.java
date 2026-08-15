package com.ninjatech.minecraftlanbridge.databinding;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.ninjatech.minecraftlanbridge.C0948R;

/* loaded from: classes5.dex */
public final class ActivityMainBinding implements ViewBinding {
    public final MaterialButton btnStart;
    public final MaterialButton btnStop;
    public final MaterialButton btnTest;
    public final MaterialCardView cardAdvanced;
    public final MaterialCardView cardConfig;
    public final MaterialCardView cardLog;
    public final MaterialCardView cardStatus;
    public final TextInputEditText etLocalPort;
    public final TextInputEditText etProxyPort;
    public final TextInputEditText etRemoteHost;
    public final TextInputEditText etRemotePort;
    public final TextInputEditText etUdpRelayPort;
    public final ScrollView logScroll;
    private final NestedScrollView rootView;
    public final View statusDot;
    public final TextInputLayout tilLocalPort;
    public final TextInputLayout tilProxyPort;
    public final TextInputLayout tilRemoteHost;
    public final TextInputLayout tilRemotePort;
    public final TextInputLayout tilUdpRelayPort;
    public final MaterialTextView tvConnections;
    public final MaterialTextView tvLocal;
    public final MaterialTextView tvLog;
    public final MaterialTextView tvMappings;
    public final MaterialTextView tvProxy;
    public final MaterialTextView tvRemote;
    public final MaterialTextView tvStatus;
    public final MaterialTextView tvUdp;

    private ActivityMainBinding(NestedScrollView rootView, MaterialButton btnStart, MaterialButton btnStop, MaterialButton btnTest, MaterialCardView cardAdvanced, MaterialCardView cardConfig, MaterialCardView cardLog, MaterialCardView cardStatus, TextInputEditText etLocalPort, TextInputEditText etProxyPort, TextInputEditText etRemoteHost, TextInputEditText etRemotePort, TextInputEditText etUdpRelayPort, ScrollView logScroll, View statusDot, TextInputLayout tilLocalPort, TextInputLayout tilProxyPort, TextInputLayout tilRemoteHost, TextInputLayout tilRemotePort, TextInputLayout tilUdpRelayPort, MaterialTextView tvConnections, MaterialTextView tvLocal, MaterialTextView tvLog, MaterialTextView tvMappings, MaterialTextView tvProxy, MaterialTextView tvRemote, MaterialTextView tvStatus, MaterialTextView tvUdp) {
        this.rootView = rootView;
        this.btnStart = btnStart;
        this.btnStop = btnStop;
        this.btnTest = btnTest;
        this.cardAdvanced = cardAdvanced;
        this.cardConfig = cardConfig;
        this.cardLog = cardLog;
        this.cardStatus = cardStatus;
        this.etLocalPort = etLocalPort;
        this.etProxyPort = etProxyPort;
        this.etRemoteHost = etRemoteHost;
        this.etRemotePort = etRemotePort;
        this.etUdpRelayPort = etUdpRelayPort;
        this.logScroll = logScroll;
        this.statusDot = statusDot;
        this.tilLocalPort = tilLocalPort;
        this.tilProxyPort = tilProxyPort;
        this.tilRemoteHost = tilRemoteHost;
        this.tilRemotePort = tilRemotePort;
        this.tilUdpRelayPort = tilUdpRelayPort;
        this.tvConnections = tvConnections;
        this.tvLocal = tvLocal;
        this.tvLog = tvLog;
        this.tvMappings = tvMappings;
        this.tvProxy = tvProxy;
        this.tvRemote = tvRemote;
        this.tvStatus = tvStatus;
        this.tvUdp = tvUdp;
    }

    @Override // androidx.viewbinding.ViewBinding
    public NestedScrollView getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(C0948R.layout.activity_main, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ActivityMainBinding bind(View rootView) throws Resources.NotFoundException {
        View statusDot;
        int id = C0948R.id.btnStart;
        MaterialButton btnStart = (MaterialButton) ViewBindings.findChildViewById(rootView, id);
        if (btnStart != null) {
            id = C0948R.id.btnStop;
            MaterialButton btnStop = (MaterialButton) ViewBindings.findChildViewById(rootView, id);
            if (btnStop != null) {
                id = C0948R.id.btnTest;
                MaterialButton btnTest = (MaterialButton) ViewBindings.findChildViewById(rootView, id);
                if (btnTest != null) {
                    id = C0948R.id.cardAdvanced;
                    MaterialCardView cardAdvanced = (MaterialCardView) ViewBindings.findChildViewById(rootView, id);
                    if (cardAdvanced != null) {
                        id = C0948R.id.cardConfig;
                        MaterialCardView cardConfig = (MaterialCardView) ViewBindings.findChildViewById(rootView, id);
                        if (cardConfig != null) {
                            id = C0948R.id.cardLog;
                            MaterialCardView cardLog = (MaterialCardView) ViewBindings.findChildViewById(rootView, id);
                            if (cardLog != null) {
                                id = C0948R.id.cardStatus;
                                MaterialCardView cardStatus = (MaterialCardView) ViewBindings.findChildViewById(rootView, id);
                                if (cardStatus != null) {
                                    id = C0948R.id.etLocalPort;
                                    TextInputEditText etLocalPort = (TextInputEditText) ViewBindings.findChildViewById(rootView, id);
                                    if (etLocalPort != null) {
                                        id = C0948R.id.etProxyPort;
                                        TextInputEditText etProxyPort = (TextInputEditText) ViewBindings.findChildViewById(rootView, id);
                                        if (etProxyPort != null) {
                                            id = C0948R.id.etRemoteHost;
                                            TextInputEditText etRemoteHost = (TextInputEditText) ViewBindings.findChildViewById(rootView, id);
                                            if (etRemoteHost != null) {
                                                id = C0948R.id.etRemotePort;
                                                TextInputEditText etRemotePort = (TextInputEditText) ViewBindings.findChildViewById(rootView, id);
                                                if (etRemotePort != null) {
                                                    id = C0948R.id.etUdpRelayPort;
                                                    TextInputEditText etUdpRelayPort = (TextInputEditText) ViewBindings.findChildViewById(rootView, id);
                                                    if (etUdpRelayPort != null) {
                                                        id = C0948R.id.logScroll;
                                                        ScrollView logScroll = (ScrollView) ViewBindings.findChildViewById(rootView, id);
                                                        if (logScroll != null && (statusDot = ViewBindings.findChildViewById(rootView, (id = C0948R.id.statusDot))) != null) {
                                                            id = C0948R.id.tilLocalPort;
                                                            TextInputLayout tilLocalPort = (TextInputLayout) ViewBindings.findChildViewById(rootView, id);
                                                            if (tilLocalPort != null) {
                                                                id = C0948R.id.tilProxyPort;
                                                                TextInputLayout tilProxyPort = (TextInputLayout) ViewBindings.findChildViewById(rootView, id);
                                                                if (tilProxyPort != null) {
                                                                    id = C0948R.id.tilRemoteHost;
                                                                    TextInputLayout tilRemoteHost = (TextInputLayout) ViewBindings.findChildViewById(rootView, id);
                                                                    if (tilRemoteHost != null) {
                                                                        id = C0948R.id.tilRemotePort;
                                                                        TextInputLayout tilRemotePort = (TextInputLayout) ViewBindings.findChildViewById(rootView, id);
                                                                        if (tilRemotePort != null) {
                                                                            id = C0948R.id.tilUdpRelayPort;
                                                                            TextInputLayout tilUdpRelayPort = (TextInputLayout) ViewBindings.findChildViewById(rootView, id);
                                                                            if (tilUdpRelayPort != null) {
                                                                                id = C0948R.id.tvConnections;
                                                                                MaterialTextView tvConnections = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                if (tvConnections != null) {
                                                                                    id = C0948R.id.tvLocal;
                                                                                    MaterialTextView tvLocal = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                    if (tvLocal != null) {
                                                                                        id = C0948R.id.tvLog;
                                                                                        MaterialTextView tvLog = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                        if (tvLog != null) {
                                                                                            id = C0948R.id.tvMappings;
                                                                                            MaterialTextView tvMappings = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                            if (tvMappings != null) {
                                                                                                id = C0948R.id.tvProxy;
                                                                                                MaterialTextView tvProxy = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                if (tvProxy != null) {
                                                                                                    id = C0948R.id.tvRemote;
                                                                                                    MaterialTextView tvRemote = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                    if (tvRemote != null) {
                                                                                                        id = C0948R.id.tvStatus;
                                                                                                        MaterialTextView tvStatus = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                        if (tvStatus != null) {
                                                                                                            id = C0948R.id.tvUdp;
                                                                                                            MaterialTextView tvUdp = (MaterialTextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                            if (tvUdp != null) {
                                                                                                                return new ActivityMainBinding((NestedScrollView) rootView, btnStart, btnStop, btnTest, cardAdvanced, cardConfig, cardLog, cardStatus, etLocalPort, etProxyPort, etRemoteHost, etRemotePort, etUdpRelayPort, logScroll, statusDot, tilLocalPort, tilProxyPort, tilRemoteHost, tilRemotePort, tilUdpRelayPort, tvConnections, tvLocal, tvLog, tvMappings, tvProxy, tvRemote, tvStatus, tvUdp);
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
