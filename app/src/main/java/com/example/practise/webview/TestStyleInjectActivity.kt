package com.example.practise.webview

import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import kotlinx.android.synthetic.main.activity_test_style_inject.*

// js结尾的";"不能丢
private const val JS = "var style = document.createElement('style');" +
                       "style.type = \"text/css\";" +
                       "style.appendChild(document.createTextNode(\"body{color: red;}\"));" +
                       "document.body.appendChild(style);"

class TestStyleInjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_style_inject)
    }

    override fun onResume() {
        super.onResume()
        webview.apply {
            settings.javaScriptEnabled = true // 注入js必须设置为true
            webChromeClient = WebChromeClient() // 设置可以显示 JS alert
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(webview: WebView?, url: String?) {
                    webview?.loadUrl("javascript:${JS}")
                }
            }
        }
        webview.loadUrl("file:///android_asset/test_injection.html")
    }

}