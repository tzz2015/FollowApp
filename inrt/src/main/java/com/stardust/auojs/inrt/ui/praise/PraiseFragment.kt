package com.stardust.auojs.inrt.ui.praise

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.ui.adapter.PraiseVideoAdapter
import com.stardust.auojs.inrt.util.AdUtils
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
    private val mAdapter: PraiseVideoAdapter by lazy { PraiseVideoAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        initObserve()
        bind.marqueeTextView.isSelected = true
        viewModel.checkNeedPermissions()
        viewModel.getScript()
        initRecyclerView()
        AdUtils.showBannerAd(requireActivity(), bind.fl)
        viewModel.getTotalPraiseCount()
        viewModel.getPraiseAccount()
        viewModel.getPraiseVideoList()
    }

    private fun initObserve() {
        viewModel.praiseAccount.observe(viewLifecycleOwner) {
            bind.tvOne.text = "${it.needPraiseCount}"
            bind.tvTwo.text = "${it.praiseCount}"
            bind.tvThree.text = "${it.praisedCount}"
        }
        viewModel.praiseVideoList.observe(viewLifecycleOwner) {
            mAdapter.setNewInstance(it)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            val item = mAdapter.getItem(position)
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(item.url)
            intent.data = contentUrl
            startActivity(intent)
        }
    }
}