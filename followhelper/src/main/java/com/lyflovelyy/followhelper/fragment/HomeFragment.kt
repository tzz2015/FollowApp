package com.lyflovelyy.followhelper.fragment

import android.os.Bundle
import com.chad.library.BR
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.FragmentHomeBinding
import com.lyflovelyy.followhelper.viewmodel.HomeViewModel
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig

/**
 * @Author : liuyufei
 * @Date : on 2023-12-28 23:09.
 * @Description :
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_home).bindViewModel(BR._all)

    override fun init(savedInstanceState: Bundle?) {
        bind.tvText.text = viewModel.getText()
    }
}
