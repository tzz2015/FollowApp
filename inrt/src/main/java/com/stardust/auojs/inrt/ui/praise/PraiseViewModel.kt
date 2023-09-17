package com.stardust.auojs.inrt.ui.praise

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.linsh.utilseverywhere.FileUtils
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.praise.PraiseAccountModel
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher
import com.stardust.auojs.inrt.util.*
import com.stardust.autojs.BuildConfig
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R
import java.io.File
import java.io.IOException
import kotlin.random.Random

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-21 22:09.
 * @Description :
 */
class PraiseViewModel : BaseViewModel() {
    val praiseCount by lazy { ViewModelEvent<String>() }
    val praiseAccount by lazy { ViewModelEvent<PraiseAccountModel>() }
    val praiseVideoList by lazy { ViewModelEvent<MutableList<PraiseVideoModel>>() }
    val enablePraiseVideoList by lazy { ViewModelEvent<MutableList<PraiseVideoModel>>() }
    val praiseVideo by lazy { ViewModelEvent<PraiseVideoModel>() }
    private val mContext: Context by lazy { GlobalAppContext.get() }


    private var mPermiss: Pair<Boolean, Boolean>? = null


    /**
     * 获取点赞总数
     */
    fun getTotalPraiseCount() {
        loadHttp(request = { ApiClient.praiseApi.getTotalPraiseCount() }, resp = {
            it?.let {
                Log.e(javaClass.name, "getTotalPraiseCount:${it} ")
                praiseCount.postValue(formatLargeNumber(it))
            }

        }, err = {
            praiseCount.postValue(formatLargeNumber(8743609))
        }, isShowDialog = false
        )

    }

    /**
     * 获取个人绑定账户
     */
    fun getPraiseAccount() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getPraiseAccount(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getPraiseAccount:${it.toString()} ")
                    it?.let { praiseAccount.postValue(it) }
                },
                isShowDialog = false
            )
        }
    }

    /**
     * 自己的视频列表
     */
    fun getPraiseVideoList() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getPraiseVideoList(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getPraiseVideoList:${it.toString()} ")
                    it?.let { praiseVideoList.postValue(it) }
                },
                isShowDialog = false
            )
        }
    }


    fun checkNeedPermissions() {
        val accessibilityEnabled =
            AccessibilityServiceTool.isAccessibilityServiceEnabled(GlobalAppContext.get())
        val isCanDrawOverlays = DrawOverlaysPermission.isCanDrawOverlays(GlobalAppContext.get())
        mPermiss = Pair(accessibilityEnabled, isCanDrawOverlays)
    }

    /**
     * 允许点赞脚本
     * 广告判断
     */
    fun runPraiseScript(activity: FragmentActivity) {
        // 第一次点赞 不展示广告
        if (MMKV.defaultMMKV().getBoolean(KV.FIRST_FOLLOW, true)) {
            MMKV.defaultMMKV().putBoolean(KV.FIRST_FOLLOW, false)
            runPraiseScript()
            return
        }
        val num = MMKV.defaultMMKV().getInt(KV.FOLLOW_SWITCH_NUM, -1)
        if (num <= 0) {
            runPraiseScript()
            return
        }
        when (Random.nextInt(num + 1)) {
            0 -> {
                LogUtils.e("激励视频点赞")
                AdUtils.showRewardVideoAd(activity) {
                    LogUtils.e("关闭激励视频")
                    runPraiseScript()
                }
            }
            1 -> {
                LogUtils.e("全屏视频点赞")
                AdUtils.showFullVideoAd(activity) {
                    LogUtils.e("关闭全屏视频")
                    runPraiseScript()
                }
            }
            2 -> {
                LogUtils.e("插屏广告点赞")
                AdUtils.showInsertAd(activity) {
                    LogUtils.e("关闭插屏广告")
                    runPraiseScript()
                }
            }
            3 -> {
                LogUtils.e("插屏广告点赞2")
                AdUtils.showInsertHorizontalAd(activity) {
                    LogUtils.e("关闭插屏广告2")
                    runPraiseScript()
                }
            }
            else -> {
                LogUtils.e("免费点赞")
                runPraiseScript()
            }
        }

    }

    /**
     * 执行点赞
     */
    private fun runPraiseScript() {
        getEnablePraiseList { list ->
            if (list.isEmpty()) {
                ToastUtils.show(mContext.getString(R.string.not_any_praise))
            } else {
                val gson = Gson()
                performFileWrite(gson.toJson(list))
                stopRunScript()
                Thread {
                    GlobalProjectLauncher.runScript(Constants.DOUYIN_JS, getPraiseType())
                }.start()
            }
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
            val file = File(appSpecificDirectory, "praise.txt")
            FileUtils.deleteFile(file)
            FileUtils.writeString(file, toJson)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRunScript() {
        GlobalProjectLauncher.stop()
    }

    /**
     * 刷评论
     */
    fun runCommentScript() {
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.MAIN1_JS, getCommentType())
        }.start()
    }

    /**
     * 分享视频链接
     */
    fun runShareScript() {
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.SHARE_JS, -1)
        }.start()
    }

    /**
     * 获取脚本
     */
    fun getScript(type:Int) {
        if (!isLogined()) {
            return
        }
        val version = MMKV.defaultMMKV().getInt(KV.SCRIPT_VERSION + type, -1)
        loadHttp(request = {
            ApiClient.otherApi.findScript(
                version, type, BuildConfig.DEBUG
            )
        }, resp = {
            it?.let {
                LogUtils.e("getPraiseScript:$it")
                MMKV.defaultMMKV().putInt(KV.SCRIPT_VERSION + it.followType, it.followType)
                MMKV.defaultMMKV().putString(KV.DECRYPT_KEY + it.followType, it.decryptKey)
                MMKV.defaultMMKV().putString(KV.SCRIPT_TEXT + it.followType, it.scriptText)
            }
        }, isShowDialog = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopRunScript()
    }

    /**
     * 添加或者更新点赞视频
     */
    fun addPraiseVideo(praiseVideoModel: PraiseVideoModel?, title: String, findUrl: String) {
        val postPraiseVideoModel =
            PraiseVideoModel(title = title, followType = getPraiseType(), url = findUrl)
        praiseVideoModel?.let {
            postPraiseVideoModel.id = it.id
        }
        loadHttp(request = { ApiClient.praiseApi.addOrUpdatePraiseVideo(postPraiseVideoModel) },
            resp = {
                it?.let {
                    praiseVideo.postValue(it)
                }
            })

    }

    fun deletePraise(item: PraiseVideoModel, back: (Boolean) -> Unit = { }) {
        loadHttp(request = { ApiClient.praiseApi.deletePraiseVideo(item.id) }, resp = {
            it?.let {
                back(it)
            }
        })
    }

    /**
     * 获取可点赞列表
     */
    private fun getEnablePraiseList(back: (MutableList<PraiseVideoModel>) -> Unit) {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getEnablePraiseList(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getEnablePraiseList:${it.toString()} ")
                    it?.let {
                        back(it)
                    }
                },
                isShowDialog = true
            )
        }
    }

    fun checkRunScript() {
        val first = mPermiss?.first == true
        val second = mPermiss?.second == true
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
        afterLogin {
            getEnablePraiseList { list ->
                if (list.isEmpty()) {
                    ToastUtils.show(mContext.getString(R.string.not_any_praise))
                } else {
                    enablePraiseVideoList.postValue(list)
                }
            }
        }
    }

}