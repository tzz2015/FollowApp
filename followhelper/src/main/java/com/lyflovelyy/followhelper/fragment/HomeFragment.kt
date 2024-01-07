package com.lyflovelyy.followhelper.fragment

import android.os.Bundle
import com.chad.library.BR
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.FragmentHomeBinding
import com.lyflovelyy.followhelper.utils.isZh
import com.lyflovelyy.followhelper.viewmodel.HomeViewModel
import com.mind.data.data.mmkv.KV
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
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
    }

    private fun initClick() {
        bind.tvTiktop.setOnClickListener {
            MMKV.defaultMMKV().putInt(KV.APP_TYPE, 0)
            initAppType()
        }
        bind.tvDouyin.setOnClickListener {
            MMKV.defaultMMKV().putInt(KV.APP_TYPE, 1)
            initAppType()
        }
    }

    private fun initAppType() {
        var selectIndex = MMKV.defaultMMKV().getInt(KV.APP_TYPE, -1)
        if (selectIndex == -1) {
            selectIndex = if (isZh()) 0 else 1
        }
        bind.tvTiktop.isSelected = selectIndex == 0
        bind.tvDouyin.isSelected = selectIndex != 0
        viewModel.getFollowAccount()
    }

    private fun initData() {
        viewModel.getTotalUserCount()
        viewModel.getTotalFollowCount()

    }
}
