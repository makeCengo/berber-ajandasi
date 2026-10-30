
package com.berberajandasi.app

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.widget.TextView
import android.view.ViewGroup
import android.graphics.Color
import android.text.TextUtils

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val webView = WebView(this)

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.allowFileAccess = true

            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()

            setContentView(webView)

            webView.loadUrl(
                "file:///android_asset/index.html"
            )

        } catch (e: Throwable) {
            val message = TextView(this)

            message.text = "UYGULAMA HATASI\n\n" +
                e.javaClass.name + "\n\n" +
                (e.message ?: "Ayrıntı bulunamadı")

            message.textSize = 16f
            message.setTextColor(Color.RED)
            message.setPadding(30, 40, 30, 40)
            message.setTextIsSelectable(true)

            setContentView(message)
        }
    }
}
