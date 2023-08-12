package com.stardust.auojs.inrt.launch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.linsh.utilseverywhere.ContextUtils.getFilesDir
import com.linsh.utilseverywhere.StringUtils
import com.mind.data.data.mmkv.KV
import com.stardust.auojs.inrt.LogActivity
import com.stardust.auojs.inrt.Pref
import com.stardust.auojs.inrt.autojs.AutoJs
import com.stardust.auojs.inrt.util.EncryptionUtil
import com.stardust.autojs.engine.encryption.ScriptEncryption
import com.stardust.autojs.execution.ExecutionConfig
import com.stardust.autojs.execution.ScriptExecution
import com.stardust.autojs.project.ProjectConfig
import com.stardust.autojs.script.JavaScriptFileSource
import com.stardust.autojs.script.JavaScriptSource
import com.stardust.pio.PFiles
import com.stardust.pio.UncheckedIOException
import com.stardust.util.MD5
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


/**
 * Created by Stardust on 2018/1/24.
 */

open class AssetsProjectLauncher(
    private val assetsProjectDir: String,
    private val context: Context
) {
    private val mProjectDir: String = File(context.filesDir, "project/").path
    private val mProjectConfig =
        ProjectConfig.fromAssets(context, ProjectConfig.configFileOfDir(assetsProjectDir))!!
    private val mMainScriptFile: File = File(mProjectDir, mProjectConfig.mainScript!!)
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private var mScriptExecution: ScriptExecution? = null

    init {

        prepare()
    }

    fun launch(activity: Activity) {
        if (Pref.istHideLogs()) {
            Log.d(TAG, "launch: Launch Activity: Hide Logs")
            //隐藏日志---直接运行
            runScript(activity)
            return;
        }
        //不隐藏日志，
        Log.d(TAG, "launch: Launch Activity: Show Logs")
        if (activity !is LogActivity) {
            //且当前不是日志
            mHandler.post {
                activity.startActivity(
                    Intent(context, LogActivity::class.java)
                        .putExtra(LogActivity.EXTRA_LAUNCH_SCRIPT, true)
                )
                activity.finish()
            }
        } else {
            runScript(null)
        }
    }

    fun stop() {
        if (mScriptExecution?.engine?.isDestroyed != true) {
            mScriptExecution?.engine?.forceStop()
        }
    }

    private fun runScript(activity: Activity?) {
        if (mScriptExecution != null && mScriptExecution!!.engine != null &&
            !mScriptExecution!!.engine.isDestroyed
        ) {
            stop()
        }
        try {
            val source = JavaScriptFileSource("main", mMainScriptFile)
            val config = ExecutionConfig(workingDirectory = mProjectDir)
            if (source.executionMode and JavaScriptSource.EXECUTION_MODE_UI != 0) {
                config.intentFlags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            } else {
                activity?.finish()
            }
            mScriptExecution = AutoJs.instance.scriptEngineService.execute(source, config)
        } catch (e: Exception) {
            AutoJs.instance.globalConsole.error(e)
        }

    }

    /**
     * 运行脚本
     */
    fun runScript(name: String, followType: Int) {
        if (mScriptExecution != null && mScriptExecution!!.engine != null &&
            !mScriptExecution!!.engine.isDestroyed
        ) {
            stop()
        }
        try {
            var scripFile = File(mProjectDir, name)
            val newScriptString = getNewScriptString(followType)
            newScriptString?.let {
                val saveScriptFile = saveScriptFile(it)
                if (saveScriptFile.isNotEmpty()) {
                    scripFile = File(saveScriptFile)
                }
            }
            val source = JavaScriptFileSource(scripFile)
            val config = ExecutionConfig(workingDirectory = mProjectDir)
            if (source.executionMode and JavaScriptSource.EXECUTION_MODE_UI != 0) {
                config.intentFlags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            }
            mScriptExecution = AutoJs.instance.scriptEngineService.execute(source, config)
        } catch (e: Exception) {
            AutoJs.instance.globalConsole.error(e)
        }
    }

    private fun saveScriptFile(text: String): String {
        try {
            // 获取应用的私有文件目录
            val privateDir = getFilesDir()
            // 创建一个名为 "output.txt" 的文件
            val outputFile = File(privateDir, "output.txt")
            val path = outputFile.path
            // 创建一个输出流来写入数据到文件
            val outputStream: OutputStream = FileOutputStream(outputFile)
            // 将数据写入输出流
            outputStream.write(text.toByteArray())
            // 关闭输出流
            outputStream.close()
            return path
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getNewScriptString(followType: Int): String? {
        val key = MMKV.defaultMMKV().getString(KV.DECRYPT_KEY + followType, "")
        val script = MMKV.defaultMMKV().getString(KV.SCRIPT_TEXT + followType, "")
        if (StringUtils.isNotAllEmpty(key, script)) {
            return EncryptionUtil.decrypt(key, script)
        }
        return null
    }


    private fun prepare() {
        val projectConfigPath = PFiles.join(mProjectDir, ProjectConfig.CONFIG_FILE_NAME)
        val projectConfig = ProjectConfig.fromFile(projectConfigPath)
        if (!BuildConfig.DEBUG && projectConfig != null &&
            TextUtils.equals(projectConfig.buildInfo.buildId, mProjectConfig.buildInfo.buildId)
        ) {
            initKey(projectConfig)
            return
        }
        initKey(mProjectConfig)
        PFiles.deleteRecursively(File(mProjectDir))
        try {
            PFiles.copyAssetDir(context.assets, assetsProjectDir, mProjectDir, null)
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    private fun initKey(projectConfig: ProjectConfig) {
        val key =
            MD5.md5(projectConfig.packageName + projectConfig.versionName + projectConfig.mainScript)
        val vec = MD5.md5(projectConfig.buildInfo.buildId + projectConfig.name).substring(0, 16)
        ScriptEncryption.mKey = key
        ScriptEncryption.mInitVector = vec
    }

    companion object {
        val TAG = AssetsProjectLauncher::class.java.simpleName
    }
}
