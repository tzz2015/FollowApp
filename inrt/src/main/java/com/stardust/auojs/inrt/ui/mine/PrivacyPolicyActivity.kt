package com.stardust.auojs.inrt.ui.mine

import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.util.isZh
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : BaseActivity<PrivacyPolicyViewModel, ActivityPrivacyPolicyBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_privacy_policy)
            .bindTitle(R.string.setting_privacypolicy_title)

    override fun initialize() {
        val url = if (isZh()) "file:////android_asset/privacy_cn.html"
        else "file:////android_asset/privacy.html"
        bind.webView.loadUrl(url)
    }

    override fun onDestroy() {
        _bind?.webView?.destroy()
        super.onDestroy()
    }


}