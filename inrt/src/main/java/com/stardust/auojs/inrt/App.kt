package com.stardust.auojs.inrt

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.fanjun.keeplive.KeepLive
import com.fanjun.keeplive.config.ForegroundNotification
import com.google.gson.Gson
import com.google.mlkit.common.MlKit
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.Utils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.UserModel
import com.mind.lib.base.BaseApp
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.autojs.AutoJs
import com.stardust.auojs.inrt.autojs.GlobalKeyObserver
import com.stardust.auojs.inrt.pluginclient.AutoXKeepLiveService
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.autojs.execution.ScriptExecuteActivity
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.BuildConfig
import org.autojs.autoxjs.inrt.R


/**
 * Created by Stardust on 2017/7/1.
 */

class App : BaseApp() {
    val mApp by lazy { this }

    var TAG = "inrt.application"
    override fun onCreate() {
        super.onCreate()
        GlobalAppContext.set(
            this, com.stardust.app.BuildConfig.generate(BuildConfig::class.java)
        )
        MlKit.initialize(this)
        Utils.init(this)
        AutoJs.initInstance(this)
        GlobalKeyObserver.init()
        initCache()

        //启动保活服务
        KeepLive.useSilenceMusice = false;
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val keepRunningWithForegroundService = sharedPreferences.getBoolean(
            getString(R.string.key_keep_running_with_foreground_service),
            false
        )
        if (keepRunningWithForegroundService) {
            val foregroundNotification = ForegroundNotification(
                GlobalAppContext.appName + "正在运行中",
                "点击打开【" + GlobalAppContext.appName + "】",
                R.drawable.icon_launcher
            )  //定义前台服务的通知点击事件
            { context, intent ->
                Log.d(TAG, "foregroundNotificationClick: ");
                val splashActivityintent = Intent(context, ScriptExecuteActivity::class.java)
                splashActivityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context!!.startActivity(splashActivityintent)
            }
            KeepLive.startWork(
                this,
                KeepLive.RunMode.ENERGY,
                foregroundNotification,
                AutoXKeepLiveService()
            );
        }

        if (BuildConfig.isMarket) {
            showNotification(this)
        }
        AdUtils.initAd()
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
                AdUtils.userId = userModel.email!!
              }
        }

        CacheManager.instance.putVersion(BuildConfig.VERSION_NAME)
    }

    private fun showNotification(context: Context) {
        val manager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder = Notification.Builder(context)
        builder.setWhen(System.currentTimeMillis())
            .setOngoing(true)
            .setSmallIcon(R.drawable.icon_launcher)
            .setContentTitle(GlobalAppContext.appName + "保持运行中")
            .setContentText("点击打开【" + GlobalAppContext.appName + "】")
            .setDefaults(NotificationCompat.FLAG_ONGOING_EVENT)
            .setPriority(Notification.PRIORITY_MAX)
        //SDK版本>=21才能设置悬挂式通知栏
        builder.setCategory(Notification.FLAG_ONGOING_EVENT.toString())
            .setVisibility(Notification.VISIBILITY_PUBLIC)
        val intent = Intent(context, SplashActivity::class.java)
        val pi =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pi)
        manager.notify(null, 0, builder.build())
    }


}
