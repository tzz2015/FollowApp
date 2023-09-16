package com.stardust.auojs.inrt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.util.CacheManager
import com.stardust.auojs.inrt.ui.adapter.MainPagerAdapter
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.auojs.inrt.util.afterSafeOnClick
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val mAdapter by lazy { MainPagerAdapter(this) }

    private var mLastPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPage()
        initBus()
        mainViewModel.getAdSwitch()
    }

    private val mOnItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> binding.viewPager.currentItem = 0
                R.id.navigation_praise -> binding.viewPager.currentItem = 1
                R.id.navigation_dashboard -> binding.viewPager.currentItem = 2
                R.id.navigation_notifications -> binding.viewPager.currentItem = 3
            }
            updateDate()
            true
        }

    private fun updateDate() {
        afterSafeOnClick {
            val selectPosition = binding.viewPager.currentItem
            if (selectPosition == mLastPosition || kotlin.math.abs(selectPosition - mLastPosition) > 1) {
                val currFragment = mAdapter.getCurrFragment(selectPosition)
                if (currFragment is BaseFragment<*, *> && currFragment.isShow) {
                    currFragment.init(null)
                }
            }
            mLastPosition = selectPosition
        }
    }

    private val mPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when (position) {
                0 -> binding.navView.selectedItemId = R.id.navigation_home
                1 -> binding.navView.selectedItemId = R.id.navigation_praise
                2 -> binding.navView.selectedItemId = R.id.navigation_dashboard
                3 -> binding.navView.selectedItemId = R.id.navigation_notifications
            }
        }
    }

    private fun initViewPage() {
        binding.viewPager.adapter = mAdapter
        binding.viewPager.offscreenPageLimit = 1
        binding.navView.setOnItemSelectedListener(mOnItemSelectedListener)
        binding.viewPager.registerOnPageChangeCallback(mPageChangeCallback)
    }

    private fun initBus() {
        // 登录失败检测
        LiveEventBus.get(MsgEvent.TOKEN_OUT).observe(this) {
            MMKV.defaultMMKV().putString(KV.USER_INFO, "")
            CacheManager.instance.clearLogin()
            ToastUtils.show(getString(R.string.login_again))
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(this) {
            mainViewModel.getAdSwitch()
            AdUtils.initCache(this)
        }
    }

    override fun onResume() {
        super.onResume()
        AdUtils.initCache(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(mPageChangeCallback)
        binding.navView.setOnItemSelectedListener(null)
        AdUtils.destroyAd()
    }
}