package com.lyflovelyy.followhelper.activity

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.BR
import com.linsh.utilseverywhere.ToastUtils
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.adapter.GuideCopyAdapter
import com.lyflovelyy.followhelper.databinding.ActivityGuideBinding
import com.lyflovelyy.followhelper.entity.BundleKeys
import com.lyflovelyy.followhelper.entity.Constants
import com.lyflovelyy.followhelper.utils.copyToClipboard
import com.lyflovelyy.followhelper.utils.toOutWebView
import com.lyflovelyy.followhelper.viewmodel.GuideViewModel
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.tencent.mmkv.MMKV

class GuideActivity : BaseActivity<GuideViewModel, ActivityGuideBinding>() {
    private val mType by lazy { intent.getIntExtra(BundleKeys.GUIDE_TYPE, 0) }
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_guide).bindViewModel(BR.guideViewModel)
            .bindTitle(if (mType == 0) R.string.to_follow else R.string.to_praise)
    private val mAdapter: GuideCopyAdapter by lazy { GuideCopyAdapter() }

    override fun initialize() {
        initWebview()
        initRecycleView()
        initObserve()
    }

    private fun initObserve() {
        viewModel.getCopyList(mType)
        viewModel.enablePraiseVideoList.observe(this) {
            mAdapter.setNewInstance(it)
        }
    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            when (view.id) {
                R.id.tv_copy -> copyTitle(item)
            }
        }
    }

    private fun copyTitle(item: PraiseVideoModel) {
        copyToClipboard(item.title)
        ToastUtils.show(getString(R.string.copy_to_clipboard))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebview() {
        val webSettings: WebSettings = bind.webview.settings
        webSettings.javaScriptEnabled = true // 启用 JavaScript
        webSettings.builtInZoomControls = true // 启用缩放
        val selectIndex = MMKV.defaultMMKV().getInt(KV.APP_TYPE, -1)
        val loadUrl = if (selectIndex == 0) Constants.TUTORIAL_URL else Constants.HOST_URL
        bind.webview.loadUrl(loadUrl)
        bind.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (url != null) {
                    view.loadUrl(url)
                }
                return true
            }
        }
        bind.methodOne.setOnClickListener { toOutWebView(this, Constants.DOWN_APK_URL) }
        bind.methodTwo.setOnClickListener { toOutWebView(this, Constants.DOWN_APK_URL) }
        bind.tvTopTitle.setOnClickListener { toOutWebView(this, Constants.DOWN_APK_URL) }
    }

    override fun onResume() {
        super.onResume()
        bind.webview.onResume()
    }

    override fun onPause() {
        super.onPause()
        bind.webview.onPause()
    }

    override fun onDestroy() {
        bind.webview.destroy()
        super.onDestroy()
    }


}