package com.stardust.auojs.inrt.ui.mine

import android.os.Bundle
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentNotificationsBinding

class MineFragment : BaseFragment<MineViewModel, FragmentNotificationsBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_notifications)


    override fun init(savedInstanceState: Bundle?) {
        viewModel.getAnnouncementList()
    }
}