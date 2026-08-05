package com.esf06.visitas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
        createWebView();
    }

    private void showLoading() {
        TextView loading = new TextView(this);
        loading.setText("Carregando agenda ESF06…");
        loading.setTextSize(18f);
        loading.setTextColor(Color.rgb(23, 51, 46));
        loading.setGravity(android.view.Gravity.CENTER);
        loading.setBackgroundColor(Color.rgb(244, 247, 246));
        setContentView(loading, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void createWebView() {
        try {
            webView = new WebView(this);
            webView.setBackgroundColor(Color.rgb(244, 247, 246));
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(false);
            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);
            settings.setMediaPlaybackRequiresUserGesture(true);

            FrameLayout root = new FrameLayout(this);
            root.addView(webView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
            setContentView(root);

            webView.loadUrl("file:///android_asset/index.html");
        } catch (Throwable error) {
            TextView failure = new TextView(this);
            failure.setText("Não foi possível abrir a agenda.\n\n" + error.getClass().getSimpleName());
            failure.setTextSize(17f);
            failure.setTextColor(Color.rgb(180, 35, 24));
            failure.setGravity(android.view.Gravity.CENTER);
            failure.setPadding(32, 32, 32, 32);
            setContentView(failure);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
