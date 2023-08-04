package com.stardust.auojs.inrt.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.LogUtils
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.ui.adapter.AnnouncementAdapter
import com.stardust.auojs.inrt.util.isLogined
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentMineBinding

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_mine)
    private val mAdapter: AnnouncementAdapter by lazy { AnnouncementAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        viewModel.getAnnouncementList()
        viewModel.getFollowAccount()
        setHeaderImage()
        initRecyclerView()
        initFunctionBtn()
        changeView(isLogined())
        doAnimation()
        initObserve()
    }

    private fun initObserve() {
        viewModel.followAccount.observe(viewLifecycleOwner) {
            bind.tvOne.text = "${it.needFollowedCount}"
            bind.tvTwo.text = "${it.followCount}"
            bind.tvThree.text = "${it.followedCount}"
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            changeView(isLogined())
            doAnimation()
            viewModel.getFollowAccount()
        }
        LiveEventBus.get(MsgEvent.TOKEN_OUT).observe(viewLifecycleOwner) {
            changeView(isLogined())
            doAnimation()
        }
    }


    private fun initFunctionBtn() {
        for (item in Constants.FUNCTION_ARRAY) {
            val parent = bind.flFunction.parent as ViewGroup
            val view =
                LayoutInflater.from(context).inflate(R.layout.item_function_tag, parent, false)
            val textView = view.findViewById<AppCompatTextView>(R.id.tv_tab_title)
            textView?.text = item
            bind.flFunction.addView(view)
            view.setOnClickListener { view ->
                LogUtils.e(item)
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = mAdapter
        viewModel.announcementList.observe(viewLifecycleOwner) {
            mAdapter.setNewInstance(it)
        }
    }

    private fun doAnimation() {
        if (!isLogined()) {
            bind.ivHeadBg.post {
                val width = bind.ivHeadBg.width
                val height = bind.ivHeadBg.height
                viewModel.doParabolaAnimation(
                    bind.tvLogin,
                    0,
                    height - bind.tvLogin.height,
                    width / 2,
                    -height / 2,
                    width - bind.tvLogin.width,
                    height - bind.tvLogin.height
                )
                viewModel.doParabolaAnimation(
                    bind.tvFindPsw,
                    width - bind.tvFindPsw.width,
                    height - bind.tvFindPsw.height,
                    width / 2,
                    -height + bind.tvFindPsw.height,
                    0,
                    height - bind.tvFindPsw.height
                )
                bind.tvLogin.setOnClickListener { viewModel.login() }
            }
        } else {
            viewModel.clearAnimation()
        }
    }

    private fun changeView(login: Boolean) {
        bind.tvLogin.isVisible = !login
        bind.tvFindPsw.isVisible = !login
        bind.flFunction.isVisible = login
        bind.cvFollow.isVisible = login
    }


    private fun setHeaderImage() {
        Glide.with(requireContext()).load(viewModel.getHeadImage())
            .apply(RequestOptions.circleCropTransform()).into(bind.ivHeader)
    }
}