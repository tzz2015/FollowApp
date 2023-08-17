package com.stardust.auojs.inrt.ui.mine

import com.chad.library.BR
import com.kc.openset.OSETBanner
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.ui.home.UserViewModel
import com.stardust.auojs.inrt.util.AdUtils
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityChangePswBinding

class ChangePswActivity : BaseActivity<UserViewModel, ActivityChangePswBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_change_psw).bindViewModel(BR.userViewModel)
            .bindTitle(R.string.change_psw_title)


    override fun initialize() {
        viewModel.init()
        viewModel.isChangePswSuccess.observe(this) {
            if (it == true) {
                viewModel.toLogin()
                finish()
            }
        }
        AdUtils.showBannerAd(this, bind.fl)
    }


}