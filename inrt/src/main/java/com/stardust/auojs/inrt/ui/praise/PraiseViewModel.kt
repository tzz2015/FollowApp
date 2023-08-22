package com.stardust.auojs.inrt.ui.praise

import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.FollowType
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher
import com.stardust.auojs.inrt.util.isLogined
import com.stardust.autojs.BuildConfig
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-21 22:09.
 * @Description :
 */
class PraiseViewModel : BaseViewModel() {

    private lateinit var mPermiss: Pair<Boolean, Boolean>

    fun checkNeedPermissions() {
        val accessibilityEnabled =
            AccessibilityServiceTool.isAccessibilityServiceEnabled(GlobalAppContext.get())
        val isCanDrawOverlays = DrawOverlaysPermission.isCanDrawOverlays(GlobalAppContext.get())
        mPermiss = Pair(accessibilityEnabled, isCanDrawOverlays)
    }

    fun runPraiseScript() {
        val first = mPermiss.first
        val second = mPermiss.second
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
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.DOUYIN_JS, FollowType.DOU_YIN_PRAISE)
        }.start()
    }

    private fun stopRunScript() {
        GlobalProjectLauncher.stop()
    }

    fun runCommentScript() {
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.MAIN1_JS, -1)
        }.start()
    }

    /**
     * 获取脚本
     */
    fun getScript() {
        if (!isLogined()) {
            return
        }
        val version =
            MMKV.defaultMMKV().getInt(KV.SCRIPT_VERSION + FollowType.DOU_YIN_PRAISE, -1)
        loadHttp(
            request = {
                ApiClient.otherApi.findScript(
                    version, FollowType.DOU_YIN_PRAISE,
                    BuildConfig.DEBUG
                )
            },
            resp = {
                it?.let {
                    LogUtils.e("getPraiseScript:$it")
                    MMKV.defaultMMKV().putInt(KV.SCRIPT_VERSION + it.followType, it.followType)
                    MMKV.defaultMMKV().putString(KV.DECRYPT_KEY + it.followType, it.decryptKey)
                    MMKV.defaultMMKV().putString(KV.SCRIPT_TEXT + it.followType, it.scriptText)
                }
            },
            isShowDialog = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopRunScript()
    }

}