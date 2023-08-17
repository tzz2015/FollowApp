package com.stardust.auojs.inrt.util

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.apkfuns.logutils.LogUtils
import com.jiagu.sdk.OSETSDKProtected
import com.kc.openset.*
import com.kc.openset.ad.OSETInformationCache
import com.kc.openset.ad.OSETInsertCache
import com.kc.openset.ad.OSETInsertHorizontal
import com.kc.openset.ad.OSETRewardVideoCache
import com.kc.openset.listener.OSETInitListener
import com.mind.data.data.mmkv.KV
import com.stardust.app.GlobalAppContext.get
import com.tencent.mmkv.MMKV

object AdUtils {
    private var isInitAd = false
    private var isIniting = false

    const val AppKey = "16F1E572AB834A32"
    const val POS_ID_Splash = "33C59AA1DCBFC104DCA82B5D18E72C34" // 开屏
    const val POS_ID_Banner = "027E62B50F702E48318109A66C4D1F97" // banner
    const val POS_ID_Insert = "A2FDF5E57D1C3643778E8C7C092AA4DA" // 插屏
    const val POS_ID_Insert_Horizontal = "592AFB97E4FD2C63EC23AA781FF6E8B0"
    const val POS_ID_RewardVideo = "2785D56002C9780ACA08156CA6CA857F" // 激励视频
    const val POS_ID_FullVideo = "3E51EFB45956CE1074A0A2F1AFC5D88B" // 全屏视频
    const val POS_ID_VIDEOCONTENT = "C36F13809B2EF4706FB2844025248B95" //视频内容
    var userId = ""
    fun initAd() {
        if (isInitAd || isIniting) {
            return
        }
        isIniting = true
        OSETSDKProtected.install(get() as Application)
        OSETSDK.getInstance().setUserId(userId)
        OSETSDK.getInstance().init(get() as Application, AppKey, object : OSETInitListener {
            override fun onError(s: String) {
                isInitAd = false
                isIniting = false
                LogUtils.e("初始化广告失败")
            }

            override fun onSuccess() {
                isInitAd = true
                isIniting = false
                LogUtils.e("初始化广告成功")
            }
        })
    }

    fun initCache(activity: Activity?) {
        //在首页开启缓存（首页生命周期比较长）
        OSETRewardVideoCache.getInstance()
            .setContext(activity)
            .setUserId(userId)
            .setPosId(POS_ID_RewardVideo)
            .startLoad()

        /*  //同激励广告
        OSETInsertCache.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_Insert)
                .startLoad();

        //同激励广告
        OSETInsertHorizontal.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_Insert_Horizontal)
                .startLoad();*/

        /*OSETInformationCache.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_INFORMATION)
                .startLoad();*/OSETFullVideo.getInstance()
            .setContext(activity)
            .setPosId(POS_ID_FullVideo)
            .startLoad()
        OSETBanner.getInstance()
            .setContext(activity)
            .setUserId(userId)
            .setPosId(POS_ID_Banner)
            .startLoad()
    }

    fun showBannerAd(activity: Activity, fl: FrameLayout?) {
        initAd()
        val show = MMKV.defaultMMKV().getBoolean(KV.BANNER_SWITCH, false)
        if (!show) {
            return
        }
        val simpleName = activity.javaClass.simpleName
        OSETBanner.getInstance()
            .show(activity, POS_ID_Banner, fl, object : OSETListener {
                override fun onClick() {
                    LogUtils.e("onClick:$simpleName")
                }

                override fun onClose() {
                    LogUtils.e("onClose:$simpleName")
                }

                override fun onShow() {
                    LogUtils.e("onShow:$simpleName")
                }

                override fun onError(s: String, s1: String) {
                    LogUtils.e("加载banner失败:$simpleName")
                }
            })
    }


    /**
     * 激励视频
     */
    fun showRewardVideoAd(activity: Activity, back: () -> Unit) {
        initAd()
        OSETRewardVideoCache.getInstance().setOSETVideoListener(object : OSETVideoListener {
            override fun onLoad() {
                // 执行此方法后可调用调用OSETFullVideo.getInstance().showAd()进行展示全屏视频
//                Toast.makeText(activity, "加载成功，可以开始调用showAd方法显示广告", Toast.LENGTH_SHORT).show()
            }

            override fun onVideoStart() {
                Log.e("RewardVideo", "onVideoStart---")
            }

            override fun onReward(s: String, arg: Int) {
                Log.e("RewardVideo", "onReward---key:$s")
                OSETRewardVideoCache.getInstance().verify(
                    s
                ) { b: Boolean ->
                    Log.d(
                        "RewardVideo",
                        "onClose 服务器验证$b"
                    )
                }
            }

            override fun onShow(key: String) {
                Toast.makeText(activity, "onShow", Toast.LENGTH_SHORT).show()
            }

            override fun onError(s: String, s1: String) {
                Log.e("openseterror", "code:$s----message:$s1")
                back()
            }

            override fun onClick() {
                Log.e("RewardVideo", "onClick---")
            }

            override fun onClose(key: String) {
                Log.e("RewardVideo", "onClose---key:$key")
                back()
            }

            override fun onVideoEnd(key: String) {
                Log.e("RewardVideo", "onVideoEnd---key:$key")
            }
        }).showAd(activity)

    }

    /**
     * 全屏广告
     */
    fun showFullVideoAd(activity: Activity, back: () -> Unit) {
        initAd()
        OSETFullVideo.getInstance().setOSETVideoListener(object : OSETVideoListener {
            override fun onLoad() {
                // 执行此方法后可调用调用OSETFullVideo.getInstance().showAd()进行展示全屏视频
//                Toast.makeText(activity, "加载成功，可以开始调用showAd方法显示广告", Toast.LENGTH_SHORT).show()
            }

            override fun onVideoStart() {
                Log.e("FullVideo", "onVideoStart---")
            }

            override fun onReward(s: String, arg: Int) {
                Log.e("FullVideo", "onReward---key:$s")
            }

            override fun onShow(key: String) {
                Toast.makeText(activity, "onShow", Toast.LENGTH_SHORT).show()
            }

            override fun onError(s: String, s1: String) {
                Log.e("openseterror", "code:$s----message:$s1")
                back()
            }

            override fun onClick() {
                Log.e("FullVideo", "onClick---")
            }

            override fun onClose(key: String) {
                Log.e("FullVideo", "onClose---key:$key")
                back()
            }

            override fun onVideoEnd(key: String) {
                Log.e("FullVideo", "onVideoEnd---key:$key")
            }
        }).showAd(activity)

    }

    fun destroyAd() {
        OSETRewardVideoCache.getInstance().destroy()
        OSETInsertCache.getInstance().destroy()
        OSETInformationCache.getInstance().destroy()
        OSETInsertHorizontal.getInstance().destroy()
        OSETFullVideo.getInstance().destroy()
        OSETBanner.getInstance().destroy()
    }
}