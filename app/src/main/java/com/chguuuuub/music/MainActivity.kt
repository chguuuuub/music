package com.chguuuuub.music

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.util.Base64
import java.io.IOException

class MainActivity : Activity() {
    private lateinit var web: WebView
    private var pendingBytes: ByteArray? = null
    private var pendingMime: String = "application/octet-stream"

    inner class Bridge {
        @JavascriptInterface
        fun saveFile(base64: String, mime: String, fileName: String) {
            try {
                pendingBytes = Base64.decode(base64, Base64.DEFAULT)
                pendingMime = mime
                runOnUiThread {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = mime
                        putExtra(Intent.EXTRA_TITLE, fileName)
                    }
                    @Suppress("DEPRECATION")
                    startActivityForResult(intent, 4102)
                }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(this@MainActivity, "导出失败：${e.message}", Toast.LENGTH_LONG).show() }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = true
        web.settings.allowContentAccess = true
        web.settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        web.addJavascriptInterface(Bridge(), "AndroidBridge")
        web.webChromeClient = WebChromeClient()
        web.webViewClient = WebViewClient()
        setContentView(web)
        web.loadUrl("file:///android_asset/index.html")
    }

    @Deprecated("Deprecated in Android API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4102 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                try {
                    contentResolver.openOutputStream(uri)?.use { out -> out.write(pendingBytes ?: byteArrayOf()) }
                    Toast.makeText(this, "文件已保存", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(this, "保存失败：${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            pendingBytes = null
        }
    }

    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) web.goBack() else super.onBackPressed()
    }
}
