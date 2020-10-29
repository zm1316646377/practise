package com.example.practise.webview

import android.net.Uri
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import kotlinx.android.synthetic.main.activity_test_web_message.*

class TestWebMessageActivity : AppCompatActivity() {

    private lateinit var port: WebMessagePort

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_web_message)
    }

    override fun onResume() {
        super.onResume()
        webview.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(webview: WebView?, url: String?) {
                    initPort()
                }
            }
        }
        webview.loadUrl("file:///android_asset/test_web_message.html")
    }

    private fun initPort() {
        val channels = webview.createWebMessageChannel()
        port = channels[0]
        port.setWebMessageCallback(object : WebMessagePort.WebMessageCallback() {
            override fun onMessage(port: WebMessagePort?, message: WebMessage?) {
                super.onMessage(port, message)
                Toast.makeText(this@TestWebMessageActivity, message?.data, Toast.LENGTH_SHORT).show()
            }
        })
        webview.postWebMessage(
            WebMessage("", arrayOf(channels[1])),
            Uri.EMPTY
        )

        port.postMessage(
            WebMessage("Kathy")
        )
    }
}