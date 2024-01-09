package com.lyflovelyy.followhelper.fragment

import android.os.Bundle
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.StringUtils
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.FragmentHomeBinding
import com.lyflovelyy.followhelper.utils.isZh
import com.lyflovelyy.followhelper.viewmodel.HomeViewModel
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * @Author : liuyufei
 * @Date : on 2023-12-28 23:09.
 * @Description :
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_home).bindViewModel(BR._all)

    override fun init(savedInstanceState: Bundle?) {
        bind.homeModel = viewModel
        initData()
        initAppType()
        initClick()
        initObserve()
    }

    private fun initObserve() {
        viewModel.followAccount.observe(viewLifecycleOwner) {
            it?.run {
                CacheManager.instance.putDYAccount(account)
                setAccount()
            }
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            initData()
        }
    }

    private fun setAccount() {
        val account = CacheManager.instance.getDYAccount()
        val appName = getCurrFollowAppName()
        if (StringUtils.isEmpty(account)) {
            bind.tvBindAccount.text =
                String.format(Locale.ENGLISH, getString(R.string.follow_bind_text), appName)
            bind.btnBindAccount.text = getString(R.string.to_bind)
        } else {
            bind.tvBindAccount.text = String.format(
                Locale.ENGLISH,
                getString(R.string.follow_bind_text2),
                appName,
                account
            )
            bind.btnBindAccount.text = getString(R.string.to_change_account)
        }
    }

    private fun initClick() {
        bind.tvDouyin.setOnClickListener {
            changeTabData(0)
            initAppType()
        }
        bind.tvTiktop.setOnClickListener {
            changeTabData(1)
            initAppType()
        }
    }

    private fun changeTabData(position: Int) {
        MMKV.defaultMMKV().putInt(KV.APP_TYPE, position)
        // 相当于重新登录
        CacheManager.instance.putDYAccount("")
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).post("")
    }

    private fun initAppType() {
        var selectIndex = MMKV.defaultMMKV().getInt(KV.APP_TYPE, -1)
        if (selectIndex == -1) {
            selectIndex = if (isZh()) 0 else 1
        }
        bind.tvTiktop.isSelected = selectIndex != 0
        bind.tvDouyin.isSelected = selectIndex == 0
        setAccount()
    }

    private fun getCurrFollowAppName(): String {
        return if (bind.tvTiktop.isSelected) bind.tvTiktop.text.toString() else bind.tvDouyin.text.toString()
    }


    private fun initData() {
        viewModel.getTotalUserCount()
        viewModel.getTotalFollowCount()
        viewModel.getFollowAccount()
    }
}