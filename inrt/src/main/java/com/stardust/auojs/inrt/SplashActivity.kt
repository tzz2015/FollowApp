package com.stardust.auojs.inrt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.Nullable
import com.kc.openset.OSETListener
import com.kc.openset.OSETSplash
import com.mind.data.data.mmkv.KV
import com.stardust.auojs.inrt.util.AdUtils
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R

/**
 * Created by Stardust on 2018/2/2.
 * Modified by wilinz on 2022/5/23
 */

class SplashActivity : ComponentActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    private lateinit var mActivity: SplashActivity
    private var isOnPause = false //判断是否跳转了广告落地页

    private var isClick = false //是否进行了点击

    private var isClose = false //是否回调了Close


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {
        val show = MMKV.defaultMMKV().getBoolean(KV.SPLASH_AD_SWITCH, true)
        if (!show) {
            toMainActivity()
        } else {
            initAdView()
        }

    }

    private fun initAdView() {
        val fl = findViewById<FrameLayout>(R.id.fl_ad)
        OSETSplash.getInstance().show(this, fl, AdUtils.POS_ID_Splash, object : OSETListener {
            override fun onShow() {
                Log.e(TAG, "onShow")
            }

            override fun onError(s: String, s1: String) {
                Log.e(TAG, "onError——————code:$s----message:$s1")
                toMainActivity()
            }

            override fun onClick() {
                isClick = true
                Log.e(TAG, "onClick")
            }

            override fun onClose() {
                Log.e(TAG, "onclose +isOnPause=" + isOnPause + "isClick=" + isClick)
                isClose = true
                if (!isOnPause && !isClick) { //如果已经调用了onPause说明已经跳转了广告落地页
                    toMainActivity()
                }
            }
        })

    }

    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        if (isOnPause && isClose) { //判断是否点击，并且跳转了落地页，如果是，就相当于走了onclose
            toMainActivity()
        } else {
            isClick = false
            isOnPause = false
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
        if (isClick) {
            isOnPause = true
        }
    }

    override fun onDestroy() {
        OSETSplash.getInstance().destroy()
        super.onDestroy()
    }


}

