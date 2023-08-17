package com.stardust.auojs.inrt.ui.mine

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kc.openset.OSETBanner
import com.mind.data.data.model.FunctionType
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
        if (mTitle == FunctionType.ADD_DOEYIN_ACCOUNT) {
            bind.tvLogin.text = "添加"
            bind.etInput.hint = "打开抖音->我的->点击抖音号复制"
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