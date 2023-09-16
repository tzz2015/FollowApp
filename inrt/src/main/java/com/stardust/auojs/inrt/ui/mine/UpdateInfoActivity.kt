package com.stardust.auojs.inrt.ui.mine

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseActivity
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.util.AdUtils
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ActivityUpdateInfoBinding

class UpdateInfoActivity : BaseActivity<UpdateInfoViewModel, ActivityUpdateInfoBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.activity_update_info).bindViewModel(BR.infoViewModel)
            .bindTitle(R.string.login_title)

    private val mTitle by lazy { intent.getStringExtra(Constants.UPDATE_FUNCTION) ?: "" }

    override fun initialize() {
        val titleView = bind.root.findViewById<AppCompatTextView>(com.mind.lib.R.id.tv_title)
        titleView.text = mTitle
        viewModel.changeText(mTitle)
        if (mTitle == getString(R.string.add_bind_account)) {
            bind.tvLogin.text = getString(R.string.add)
            bind.etInput.hint = getString(R.string.copy_account)
        }
        viewModel.isChangeSuccess.observe(this) {
            if (it == true) {
                LiveEventBus.get(MsgEvent.CHANGE_USER_INFO).post("")
                finish()
            }
        }
        AdUtils.showBannerAd(this, bind.fl)
    }



}