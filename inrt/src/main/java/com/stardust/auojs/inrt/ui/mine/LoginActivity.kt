package com.stardust.auojs.inrt.ui.mine

import com.chad.library.BR
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.mind.lib.util.CacheManager
import com.stardust.auojs.inrt.ui.home.UserViewModel
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<UserViewModel, ActivityLoginBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_login).bindViewModel(BR.loginViewModel)
            .bindTitle(R.string.login_title)


    override fun initialize() {
        viewModel.loginResult.observe(this) {
            val gson = Gson()
            MMKV.defaultMMKV().putString(KV.USER_INFO, gson.toJson(it))
            CacheManager.instance.putToken(it.token)
            CacheManager.instance.putPhone(it.phone)
            CacheManager.instance.putEmail(it.email)
            LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).post(it.token)
            finish()
        }
    }
}