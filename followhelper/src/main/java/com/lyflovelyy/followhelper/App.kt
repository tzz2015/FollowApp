package com.lyflovelyy.followhelper


import android.app.Application
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.Utils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.UserModel
import com.mind.lib.base.BaseApp
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV


/**
 * Created by Stardust on 2017/7/1.
 */

class App : BaseApp() {
    val mApp by lazy { this }

    companion object {
        private lateinit var baseApp: BaseApp
        fun getContext(): Application {
            return baseApp
        }
    }

    var TAG = "inrt.application"
    override fun onCreate() {
        super.onCreate()
        baseApp = this
        Utils.init(this)
        initCache()
    }


    private fun initCache() {
        // mmkv 初始化
        MMKV.initialize(this)
        //liveBus 初始化
        LiveEventBus.config().lifecycleObserverAlwaysActive(true)
        val userInfo = MMKV.defaultMMKV().getString(KV.USER_INFO, "") ?: ""
        if (StringUtils.isNotAllEmpty(userInfo)) {
            val gson = Gson()
            val userModel = gson.fromJson(userInfo, UserModel::class.java)
            CacheManager.instance.putToken(userModel.token)
            CacheManager.instance.putPhone(userModel.phone)
            CacheManager.instance.putEmail(userModel.email)
            userModel.email?.let {
            }
        }

        CacheManager.instance.putVersion(BuildConfig.VERSION_NAME)
        CacheManager.instance.putLanguage(resources.configuration.locale.language)
    }


}
