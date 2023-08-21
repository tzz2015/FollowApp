package com.stardust.auojs.inrt.ui.praise

import android.os.Bundle
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import org.autojs.autoxjs.inrt.BR
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentPraiseBinding

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-21 22:10.
 * @Description :
 */
class PraiseFragment : BaseFragment<PraiseViewModel, FragmentPraiseBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_praise).bindViewModel(BR.praiseViewModel)

    override fun init(savedInstanceState: Bundle?) {
        viewModel.checkNeedPermissions()
        viewModel.getScript()
    }
}