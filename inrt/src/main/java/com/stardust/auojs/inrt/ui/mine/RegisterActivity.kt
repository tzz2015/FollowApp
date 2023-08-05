package com.stardust.auojs.inrt.ui.mine

import com.chad.library.BR
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.ui.home.UserViewModel
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<UserViewModel, ActivityRegisterBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_register).bindViewModel(BR.userViewModel)
            .bindTitle(R.string.login_register)

    override fun initialize() {
        viewModel.isRegisterSuccess.observe(this) {
            if (it == true) {
                finish()
            }
        }
    }

}