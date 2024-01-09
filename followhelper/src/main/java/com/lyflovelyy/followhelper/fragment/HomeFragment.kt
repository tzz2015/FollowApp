package com.lyflovelyy.followhelper.fragment

import android.os.Bundle
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
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
        setAccount()
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
        val dyAccount = CacheManager.instance.getDYAccount()
       /* if (!TextUtils.isEmpty(dyAccount)) {
            bind.tvNoticeBind.text = String.format(
                requireContext().getText(R.string.follow_bind_account).toString(), dyAccount
            )
            bind.btnBindAccount.text = requireContext().getText(R.string.to_change).toString()
        } else {
            bind.tvNoticeBind.text = requireContext().getText(R.string.follow_bind_text).toString()
            bind.btnBindAccount.text = requireContext().getText(R.string.to_bind).toString()
        }*/

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
    }

    private fun initData() {
        viewModel.getTotalUserCount()
        viewModel.getTotalFollowCount()
        viewModel.getFollowAccount()
    }
}
