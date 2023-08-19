package com.stardust.auojs.inrt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.util.CacheManager
import com.stardust.auojs.inrt.util.AdUtils
import com.tencent.mmkv.MMKV
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        initBus()
        mainViewModel.getAdSwitch()
    }

    private fun initBus() {
        // 登录失败检测
        LiveEventBus.get(MsgEvent.TOKEN_OUT).observe(this) {
            MMKV.defaultMMKV().putString(KV.USER_INFO, "")
            CacheManager.instance.clearLogin()
            ToastUtils.show("登录失效，请重新登录")
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
        AdUtils.destroyAd()
    }
}