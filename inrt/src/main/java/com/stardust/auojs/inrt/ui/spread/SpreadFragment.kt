package com.stardust.auojs.inrt.ui.spread

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kc.openset.OSETVideoContent
import com.kc.openset.ad.OSETRewardVideoCache
import com.kc.openset.listener.OSETVideoContentFragmentListener
import com.linsh.utilseverywhere.LogUtils
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.auojs.inrt.util.X5InitUtils
import org.autojs.autoxjs.inrt.BR
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentDashboardBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import kotlin.random.Random

class SpreadFragment : BaseFragment<SpreadViewModel, FragmentDashboardBinding>() {


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
            val url = it[randomInt].url
            if (url == Constants.AD_VIDEO) {
                loadAdVideo()
                bind.fabRefresh.isVisible = false
            } else {
                bind.webView.loadUrl(url)
                bind.fabRefresh.isVisible = true
            }
        }
        bind.fabRefresh.setOnClickListener { bind.webView.reload() }
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)
    }


    private fun loadAdVideo() {
        AdUtils.initAd()
        OSETVideoContent.getInstance().showVideoContentForFragment(
            requireActivity(),
            AdUtils.POS_ID_VIDEOCONTENT,
            object : OSETVideoContentFragmentListener {
                override fun onError(s: String, s1: String) {
                    LogUtils.e("加载短视频失败")
                }

                override fun loadSuccess(fragment: Fragment) {
                    LogUtils.e("加载短视频成功")
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_video, fragment)
                        .commitAllowingStateLoss()
                    OSETVideoContent.getInstance().destroy()
                }

                override fun startVideo(i: Int, isAd: Boolean, id: String) {
                    Log.e("videocontent", "startVideo--开始播放视频第" + i + "个")
                }

                override fun pauseVideo(i: Int, isAd: Boolean, id: String) {
                    Log.e("videocontent", "pauseVideo--暂停播放视频第" + i + "个")
                }

                override fun resumeVideo(i: Int, isAd: Boolean, id: String) {
                    Log.e("videocontent", "resumeVideo--继续播放视频第" + i + "个")
                }

                override fun endVideo(i: Int, isAd: Boolean, id: String) {
                    Log.e("videocontent", "endVideo--完成播放视频第" + i + "个")
                }
            })

    }


    @AfterPermissionGranted(110)
    fun checkStorageManagerPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(
                requireContext(),
                *permissions
            )
        ) {
            X5InitUtils.init()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                110,
                *permissions
            )
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        OSETRewardVideoCache.getInstance().destroy()
        mCallback.remove()
        bind.webView.destroy()
    }
}