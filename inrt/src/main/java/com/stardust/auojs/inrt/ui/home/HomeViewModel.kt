package com.stardust.auojs.inrt.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.google.gson.Gson
import com.linsh.utilseverywhere.FileUtils
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.FollowAccount
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher
import com.stardust.auojs.inrt.ui.mine.LoginActivity
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.auojs.inrt.util.getFollowType
import com.stardust.auojs.inrt.util.isLogined
import com.stardust.autojs.BuildConfig
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.autojs.autoxjs.inrt.R
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _permiss = MutableStateFlow(Pair(false, false))
    val permiss = _permiss.asStateFlow()
    private val mContext: Context by lazy { GlobalAppContext.get() }
    val followList = ViewModelEvent<MutableList<FollowAccount>>()

    fun checkNeedPermissions() {
        val accessibilityEnabled =
            AccessibilityServiceTool.isAccessibilityServiceEnabled(GlobalAppContext.get())
        val isCanDrawOverlays = DrawOverlaysPermission.isCanDrawOverlays(GlobalAppContext.get())
        _permiss.update {
            it.copy(accessibilityEnabled, isCanDrawOverlays)
        }
    }

    fun toFollow() {
        if (!isLogined()) {
            toLogin()
            return
        }
        val first = permiss.value.first
        val second = permiss.value.second
        if (!first) {
            ToastUtils.show(
                GlobalAppContext.get()
                    .getString(R.string.text_accessibility_service_is_not_turned_on)
            )
            return
        }
        if (!second) {
            ToastUtils.show(
                GlobalAppContext.get().getString(R.string.text_required_floating_window_permission)
            )
            return
        }
        // 获取可关注的列表
        getEnableFollowList {
            /* val gson = Gson()
             performFileWrite(gson.toJson(it))*/
            LogUtils.e("getEnableFollowList:$it")
            if (it.isEmpty()) {
                ToastUtils.showLong(mContext.getString(R.string.not_any_follow))
            } else {
                followList.postValue(it)
            }
        }

    }

    private fun getEnableFollowList(back: (MutableList<FollowAccount>) -> Unit) {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.followAccountApi.getEnableFollowList(getFollowType()) },
                resp = {
                    it?.let {
                        back(it)
                    }
                },
                isShowDialog = true
            )
        }
    }

    /**
     * 写入文件到sd卡
     */
    private fun performFileWrite(toJson: String) {
        try {
            // 获取外部存储路径
            val appSpecificDirectory = File(Environment.getExternalStorageDirectory(), "follow")
            if (!appSpecificDirectory.exists()) {
                appSpecificDirectory.mkdirs()
            }
            val file = File(appSpecificDirectory, "account.txt")
            FileUtils.deleteFile(file)
            FileUtils.writeString(file, toJson)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * 运行关注脚本
     */
    fun runFollowScript(activity: Activity) {
        // 第一次关注 不展示广告
        if (MMKV.defaultMMKV().getBoolean(KV.FIRST_FOLLOW, true)) {
            MMKV.defaultMMKV().putBoolean(KV.FIRST_FOLLOW, false)
            runFollowScript()
            return
        }
        val num = MMKV.defaultMMKV().getInt(KV.FOLLOW_SWITCH_NUM, -1)
        if (num <= 0) {
            runFollowScript()
            return
        }
        when (Random.nextInt(num + 1)) {
            0 -> {
                LogUtils.e("激励视频关注")
                AdUtils.showRewardVideoAd(activity) {
                    LogUtils.e("关闭激励视频")
                    runFollowScript()
                }
            }
            1 -> {
                LogUtils.e("全屏视频关注")
                AdUtils.showFullVideoAd(activity) {
                    LogUtils.e("关闭全屏视频")
                    runFollowScript()
                }
            }
            2 -> {
                LogUtils.e("插屏广告关注")
                AdUtils.showInsertAd(activity) {
                    LogUtils.e("关闭插屏广告")
                    runFollowScript()
                }
            }
            3 -> {
                LogUtils.e("插屏广告关注2")
                AdUtils.showInsertHorizontalAd(activity) {
                    LogUtils.e("关闭插屏广告2")
                    runFollowScript()
                }
            }
            else -> {
                LogUtils.e("免费关注")
                runFollowScript()
            }
        }

    }

    private fun runFollowScript() {
        getEnableFollowList {
            if (it.isEmpty()) {
                ToastUtils.show(mContext.getString(R.string.not_any_follow))
            } else {
                val gson = Gson()
                performFileWrite(gson.toJson(it))
                stopRunScript()
                Thread {
                    GlobalProjectLauncher.runScript(Constants.DOUYIN_JS, getFollowType())
                }.start()
            }
        }

    }


    private fun toLogin() {
        val intent = Intent(mContext, LoginActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

    private fun stopRunScript() {
        GlobalProjectLauncher.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stopRunScript()
    }

    /**
     * 获取脚本
     */
    fun getScript(type: Int) {
        if (!isLogined()) {
            return
        }
        val version = MMKV.defaultMMKV().getInt(KV.SCRIPT_VERSION + type, -1)
        loadHttp(
            request = {
                ApiClient.otherApi.findScript(
                    version, type,
                    BuildConfig.DEBUG
                )
            },
            resp = {
                it?.let {
                    LogUtils.e("getScript:$it")
                    MMKV.defaultMMKV().putInt(KV.SCRIPT_VERSION + it.followType, it.followType)
                    MMKV.defaultMMKV().putString(KV.DECRYPT_KEY + it.followType, it.decryptKey)
                    MMKV.defaultMMKV().putString(KV.SCRIPT_TEXT + it.followType, it.scriptText)
                }
            },
            isShowDialog = false
        )
    }
}