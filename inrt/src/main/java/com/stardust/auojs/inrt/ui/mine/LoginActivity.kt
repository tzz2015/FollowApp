package com.stardust.auojs.inrt.ui.mine

import com.chad.library.BR
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_login).bindViewModel(BR.loginViewModel).bindTitle(R.string.login_title)


    override fun initialize() {

    }
}