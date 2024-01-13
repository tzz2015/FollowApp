package com.lyflovelyy.followhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.ToastUtils
import com.lyflovelyy.followhelper.adapter.MainPagerAdapter
import com.lyflovelyy.followhelper.databinding.ActivityMainBinding
import com.lyflovelyy.followhelper.viewmodel.MainViewModel
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val mAdapter by lazy { MainPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPage()
        initBus()
        mainViewModel.getAdSwitch()
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
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPage() {
        binding.viewPager.adapter = mAdapter
        binding.viewPager.offscreenPageLimit = 1
        binding.navView.setOnItemSelectedListener(mOnItemSelectedListener)
        binding.viewPager.registerOnPageChangeCallback(mPageChangeCallback)
        var lastY = 0f
        binding.viewPager.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val deltaY: Float = event.y - lastY
                if (deltaY > 0 || deltaY < 0) {
                    return@setOnTouchListener true
                }
            }
            lastY = event.y
            return@setOnTouchListener false
        }
    }

    private val mOnItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> binding.viewPager.currentItem = 0
                R.id.navigation_praise -> binding.viewPager.currentItem = 1
            }
            true
        }

    private val mPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when (position) {
                0 -> binding.navView.selectedItemId = R.id.navigation_home
                1 -> binding.navView.selectedItemId = R.id.navigation_praise
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(mPageChangeCallback)
        binding.navView.setOnItemSelectedListener(null)
    }

}