package com.lyflovelyy.followhelper.activity

import com.chad.library.BR
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.ActivityRegisterBinding
import com.lyflovelyy.followhelper.viewmodel.UserViewModel
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig

class RegisterActivity : BaseActivity<UserViewModel, ActivityRegisterBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_register).bindViewModel(BR.loginViewModel)
            .bindTitle(R.string.login_register_title)

    override fun initialize() {
        viewModel.isRegisterSuccess.observe(this) {
            if (it == true) {
                finish()
            }
        }
    }


}