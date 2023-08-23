package com.stardust.auojs.inrt.ui.praise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import com.linsh.utilseverywhere.StringUtils
import com.stardust.auojs.inrt.data.BundleKeys
import com.stardust.auojs.inrt.ui.view.webview.X5WebView
import org.autojs.autoxjs.inrt.R

class WebViewActivity : ComponentActivity() {
    private var webView: X5WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        initView()
    }

    private val mCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            }
        }
    }

    private fun initView() {
        val tvTitle = findViewById<AppCompatTextView>(R.id.tv_title)
        tvTitle?.let {
            val title = intent.getStringExtra(BundleKeys.TITLE)
            it.text = title
        }
        webView = findViewById(R.id.webView)
        webView?.let {
            val url = intent.getStringExtra(BundleKeys.URL)
            if (StringUtils.isNotAllEmpty(url)) {
                it.loadUrl(url)
            }
        }
        onBackPressedDispatcher.addCallback(mCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.destroy()
        mCallback.remove()
    }
}