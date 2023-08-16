package com.stardust.auojs.inrt.ui.mine

import com.chad.library.BR
import com.kc.openset.OSETBanner
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.util.AdUtils
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivitySuggestionBinding

class SuggestionActivity : BaseActivity<UpdateInfoViewModel,ActivitySuggestionBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_suggestion).bindViewModel(BR.infoViewModel)
            .bindTitle(R.string.suggestion_title)

    override fun initialize() {
        viewModel.isChangeSuccess.observe(this) {
            if (it == true) {
                finish()
            }
        }
        AdUtils.showBannerAd(this, bind.fl)
    }
    override fun onDestroy() {
        super.onDestroy()
        OSETBanner.getInstance().destroy()
    }

}