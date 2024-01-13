package com.lyflovelyy.followhelper.activity

import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.ActivityPrivacyPolicyBinding
import com.lyflovelyy.followhelper.utils.isZh
import com.lyflovelyy.followhelper.viewmodel.PrivacyPolicyViewModel
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig

class PrivacyPolicyActivity : BaseActivity<PrivacyPolicyViewModel, ActivityPrivacyPolicyBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_privacy_policy)
            .bindTitle(R.string.setting_privacypolicy_title)

    override fun initialize() {
        val url = if (isZh()) "file:////android_asset/privacy_cn.html"
        else "file:////android_asset/privacy.html"
        bind.webView.loadUrl(url)
    }

    override fun onPause() {
        super.onPause()
        _bind?.webView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        _bind?.webView?.onResume()
    }

    override fun onDestroy() {
        _bind?.webView?.destroy()
        super.onDestroy()
    }


}