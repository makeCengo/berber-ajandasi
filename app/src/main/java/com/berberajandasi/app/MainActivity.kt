
package com.berberajandasi.app

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.webkit.WebViewAssetLoader
import org.json.JSONObject

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private lateinit var assetLoader: WebViewAssetLoader

    private var pendingBackupJson: String? = null

    companion object {
        private const val CREATE_BACKUP = 1001
        private const val OPEN_BACKUP = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(
                "/assets/",
                WebViewAssetLoader.AssetsPathHandler(this)
            )
            .build()

        webView = WebView(this)

        webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = false
            allowContentAccess = false
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
        }

        webView.addJavascriptInterface(
            BackupInterface(),
            "Android"
        )

        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url ?: return null
                return assetLoader.shouldInterceptRequest(url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request?.url ?: return false
                val host = uri.host.orEmpty()

                if (host == "appassets.androidplatform.net") return false

                if (uri.scheme == "https" &&
                    (host == "wa.me" ||
                     host.endsWith(".whatsapp.com") ||
                     host == "api.whatsapp.com")) {
                    return openExternal(uri)
                }

                if (uri.scheme == "tel" || uri.scheme == "mailto") {
                    return openExternal(uri)
                }

                return openExternal(uri)
            }
        }

        setContentView(webView)

        if (savedInstanceState == null) {
            webView.loadUrl(
                "https://appassets.androidplatform.net/assets/index.html"
            )
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    inner class BackupInterface {

        @JavascriptInterface
        fun saveBackup(json: String) {
            runOnUiThread {
                pendingBackupJson = json

                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                    putExtra(
                        Intent.EXTRA_TITLE,
                        "berber-ajandasi-yedek.json"
                    )
                }

                startActivityForResult(intent, CREATE_BACKUP)
            }
        }

        @JavascriptInterface
        fun openBackup() {
            runOnUiThread {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                }

                startActivityForResult(intent, OPEN_BACKUP)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            pendingBackupJson = null
            return
        }

        val uri = data?.data ?: return

        when (requestCode) {

            CREATE_BACKUP -> {
                try {
                    val json = pendingBackupJson ?: return

                    contentResolver.openOutputStream(uri)?.use {
                        it.write(json.toByteArray(Charsets.UTF_8))
                    }

                    Toast.makeText(
                        this,
                        "Yedek başarıyla kaydedildi.",
                        Toast.LENGTH_LONG
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Yedek kaydedilemedi: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    pendingBackupJson = null
                }
            }

            OPEN_BACKUP -> {
                try {
                    val json = contentResolver.openInputStream(uri)
                        ?.bufferedReader(Charsets.UTF_8)
                        ?.use { it.readText() }
                        ?: throw Exception("Dosya okunamadı.")

                    val safeJson = JSONObject.quote(json)

                    webView.evaluateJavascript(
                        "window.onBackupLoaded($safeJson);",
                        null
                    )

                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Yedek açılamadı: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun openExternal(uri: Uri): Boolean {
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Bu bağlantıyı açacak uygulama bulunamadı.",
                Toast.LENGTH_LONG
            ).show()
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}
