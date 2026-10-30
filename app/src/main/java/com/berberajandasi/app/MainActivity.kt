
package com.berberajandasi.app

import android.app.Activity
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import androidx.webkit.WebViewAssetLoader

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private lateinit var errorView: TextView
    private var hasError = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = FrameLayout(this)

        webView = WebView(this)
        errorView = TextView(this).apply {
            textSize = 16f
            setPadding(32, 32, 32, 32)
            visibility = View.GONE
        }

        layout.addView(webView)
        layout.addView(errorView)
        setContentView(layout)

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(
                "/assets/",
                WebViewAssetLoader.AssetsPathHandler(this)
            )
            .build()

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = false
        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()

                if (url.startsWith(
                    "https://appassets.androidplatform.net/"
                )) {
                    return false
                }

                if (url.startsWith("https://wa.me/") ||
                    url.startsWith("whatsapp://")) {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    )
                }

                return true
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                if (request.isForMainFrame) {
                    hasError = true
                    errorView.visibility = View.VISIBLE
                    errorView.text =
                        "Sayfa yüklenemedi.\n\nHata: ${error.description}\nKod: ${error.errorCode}"
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!hasError) {
                    errorView.visibility = View.GONE
                }
            }
        }

        webView.loadUrl(
            "https://appassets.androidplatform.net/assets/index.html"
        )
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
