package com.stardust.auojs.inrt.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.google.gson.Gson
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.FollowAccountType
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher
import com.stardust.auojs.inrt.ui.mine.LoginActivity
import com.stardust.auojs.inrt.util.isLogined
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.autojs.autoxjs.inrt.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

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
        /* if (!first) {
             ToastUtils.show(
                 GlobalAppContext.get()
                     .getString(R.string.text_accessibility_service_is_not_turned_on)
             )
             return
         }*/
        if (!second) {
            ToastUtils.show(
                GlobalAppContext.get().getString(R.string.text_required_floating_window_permission)
            )
            return
        }
        // 获取可关注的列表
        getEnableFollowList()

    }

    private fun getEnableFollowList() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.followAccountApi.getEnableFollowList(FollowAccountType.DOU_YIN) },
                resp = {
                    it?.let {
                        val gson = Gson()
                        performFileWrite(gson.toJson(it))
                        LogUtils.e("getEnableFollowList:$it")
                        if (it.isEmpty()) {
                            ToastUtils.showLong("当前无可关注，可以邀请更多朋友加入")
                        } else {
                            followList.postValue(it)
                        }
                    }

                }
            )
        }
    }

    /**
     * 写入文件到sd卡
     */
    private fun performFileWrite(toJson: String) {
        // 获取外部存储路径
        val appSpecificDirectory = File(Environment.getExternalStorageDirectory(), "follow")
        if (!appSpecificDirectory.exists()) {
            appSpecificDirectory.mkdirs()
        }
        val file = File(appSpecificDirectory, "account.txt")
        val tokenFile = File(appSpecificDirectory, "token.txt")

        try {
            val fos = FileOutputStream(file)
            fos.write(toJson.toByteArray())
            fos.close()
            val tokenFos = FileOutputStream(tokenFile)
            tokenFos.write(CacheManager.instance.getToken().toByteArray())
            tokenFos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 运行关注脚本
     */
    fun runFollowScript() {
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.DOUYIN_JS)
        }.start()
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
}