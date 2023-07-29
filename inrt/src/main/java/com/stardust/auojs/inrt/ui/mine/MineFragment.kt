package com.stardust.auojs.inrt.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.linsh.utilseverywhere.LogUtils
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.ui.adapter.AnnouncementAdapter
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentMineBinding

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_mine)
    private val mAdapter: AnnouncementAdapter by lazy { AnnouncementAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        viewModel.getAnnouncementList()
        setHeaderImage()
        initRecyclerView()
        initFunctionBtn()
        doAnimation()
    }

    private fun initFunctionBtn() {
        for (item in viewModel.functionArray) {
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

        }
    }


    private fun setHeaderImage() {
        Glide.with(requireContext()).load(viewModel.getHeadImage())
            .apply(RequestOptions.circleCropTransform()).into(bind.ivHeader)
    }
}