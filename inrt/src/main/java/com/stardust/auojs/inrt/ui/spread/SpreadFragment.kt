package com.stardust.auojs.inrt.ui.spread

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kc.openset.OSETVideoContent
import com.kc.openset.OSETVideoListener
import com.kc.openset.ad.OSETRewardVideoCache
import com.kc.openset.listener.OSETVideoContentFragmentListener
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
    private val looked: MutableList<Int> = ArrayList() //记录已经看过的视频下标

    private val downTime = 30000 //倒计时总时长

    private val timeSingle = 5000 //单次视频可倒计时时间（毫秒）

    private var timeTemp: Int = timeSingle //单次视频剩余倒计时时间

    private var time: Int = downTime //总的倒计时进度


    private var isDown = false
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
        bind.rlDown.setOnClickListener { showReward() }
    }

    private fun initObserver() {
        viewModel.spreadList.observe(viewLifecycleOwner) {
            val randomInt = Random.nextInt(0, it.size)
            val url = it[randomInt].url
            if (url == Constants.AD_VIDEO) {
                loadAdVideo()
                bind.rlDown.isVisible = true
                bind.fabRefresh.isVisible = false
            } else {
                bind.webView.loadUrl(url)
                bind.rlDown.isVisible = false
                bind.fabRefresh.isVisible = true
            }
        }
        bind.fabRefresh.setOnClickListener { bind.webView.reload() }
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)
    }

    override fun onPause() {
        super.onPause()
        isDown = false
    }

    private fun loadAdVideo() {
        OSETVideoContent.getInstance().showVideoContentForFragment(
            requireActivity(),
            AdUtils.POS_ID_VIDEOCONTENT,
            object : OSETVideoContentFragmentListener {
                override fun onError(s: String, s1: String) {}
                override fun loadSuccess(fragment: Fragment) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_video, fragment)
                        .commitAllowingStateLoss()
                    OSETVideoContent.getInstance().destroy()
                    handler.postDelayed(runnable, 50)
                }

                override fun startVideo(i: Int, isAd: Boolean, id: String) {
                    Log.e("videocontent", "startVideo--开始播放视频第" + i + "个")
                    isDown = true
                    var islooked = false
                    for (temp in looked) {
                        if (temp == i) {
                            islooked = true
                            break
                        }
                    }
                    if (!islooked) {
                        timeTemp = timeSingle
                        looked.add(i)
                    }
                }

                override fun pauseVideo(i: Int, isAd: Boolean, id: String) {
                    isDown = false
                    Log.e("videocontent", "pauseVideo--暂停播放视频第" + i + "个")
                }

                override fun resumeVideo(i: Int, isAd: Boolean, id: String) {
                    isDown = true
                    Log.e("videocontent", "resumeVideo--继续播放视频第" + i + "个")
                }

                override fun endVideo(i: Int, isAd: Boolean, id: String) {
                    isDown = false
                    Log.e("videocontent", "endVideo--完成播放视频第" + i + "个")
                }
            })

    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (!isDown) {
                handler.postDelayed(this, 50)
                return
            }
            timeTemp = timeTemp - 50
            if (timeTemp >= 0) { //单次视频剩余倒计时时间必须大于等于0才进行倒计时
                time = time - 50
                Log.e("time-----", time.toString() + "")
                if (time <= 0) {
                    bind.cpv.progress = 10000
                    bind.rlDown.isEnabled = true
                    bind.tvDesc.text = "领取"
                } else {
                    bind.cpv.progress = ((30000 - time) * 10000.0 / 30000).toInt()
                    bind.tvDesc.text = "${time / 1000}"
                    bind.rlDown.isEnabled = false
                }
            }
            handler.postDelayed(this, 50)
        }
    }

    private val handler = Handler(Looper.getMainLooper())


    @AfterPermissionGranted(110)
    fun checkStorageManagerPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            X5InitUtils.init()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                110,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    }

    private fun showReward() {
        OSETRewardVideoCache.getInstance().setContext(requireActivity())
            .setPosId(AdUtils.POS_ID_RewardVideo)
            .setOSETVideoListener(object : OSETVideoListener {
                override fun onShow(key: String) {}
                override fun onError(s: String, s1: String) {}
                override fun onClick() {}
                override fun onClose(s: String) {}
                override fun onVideoEnd(s: String) {}
                override fun onLoad() {}
                override fun onVideoStart() {}

                @SuppressLint("SetTextI18n")
                override fun onReward(s: String, arg: Int) {
                    time = downTime
                    bind.cpv.progress = 0
                    bind.tvDesc.text = "30"
                    bind.rlDown.isEnabled = false
                    Toast.makeText(requireContext(), "奖励红包达成，发放奖励~", Toast.LENGTH_SHORT).show()
                }
            }).showAd(requireActivity())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        OSETVideoContent.getInstance().destroy()
        OSETRewardVideoCache.getInstance().destroy()
        mCallback.remove()
        bind.webView.destroy()
        handler.removeCallbacks(runnable)
    }
}