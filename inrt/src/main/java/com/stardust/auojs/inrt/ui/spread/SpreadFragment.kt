package com.stardust.auojs.inrt.ui.spread

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import org.autojs.autoxjs.inrt.BR
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentDashboardBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import kotlin.random.Random

class SpreadFragment : BaseFragment<SpreadViewModel, FragmentDashboardBinding>() {

    private var isReLoad = false

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_dashboard)
            .bindViewModel(BR.spreadModel)
    private val mCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (bind.webView.canGoBack()) {
                bind.webView.goBack()
            }
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        viewModel.getSpreadList()
        initObserver()
        checkStorageManagerPermission()
    }

    private fun initObserver() {
        viewModel.spreadList.observe(viewLifecycleOwner) {
            val randomInt = Random.nextInt(0, it.size)
            bind.webView.loadUrl(it[randomInt].url)
        }
        bind.fabRefresh.setOnClickListener { bind.webView.reload() }
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)
    }


    @AfterPermissionGranted(110)
    fun checkStorageManagerPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            initTbsListener()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                110,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    }

    private fun initTbsListener() {
        initTbsSdk()
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                Log.e("app", "onDownloadFinish -->下载X5内核完成：$i")
            }

            override fun onInstallFinish(i: Int) {
                Log.e("app", "onInstallFinish -->安装X5内核进度：$i")
            }

            override fun onDownloadProgress(i: Int) {
                Log.e("app", "onDownloadProgress -->下载X5内核进度：$i")
            }
        })
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(success: Boolean) {
                //x5內核初始化完成的回调，true表x5内核加载成功，否则表加载失败，会自动切换到系统内核。
                Log.e("app", " 内核加载 $success")
                if (!success && !isReLoad) {
                    isReLoad = true
                    QbSdk.reset(requireContext())
                    initTbsSdk()
                    TbsDownloader.startDownload(requireContext())
                }
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(requireActivity(), cb)
    }

    private fun initTbsSdk() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map: HashMap<String, Any> = HashMap()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        QbSdk.setDownloadWithoutWifi(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mCallback.remove()
        bind.webView.destroy()
    }
}