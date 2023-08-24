package com.stardust.auojs.inrt.ui.praise

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.util.Linkify
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.linsh.utilseverywhere.KeyboardUtils
import com.linsh.utilseverywhere.ToastUtils
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
        initView()
    }

    private fun initView() {
        bind.tvAdd.setOnClickListener { showEditDialog(null) }
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

    private fun showEditDialog(praiseViewModel: PraiseViewModel?) {
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.input_praise_video_layout)
        // 设置对话框标题
        customDialog.setTitle(getString(R.string.add_video_url))
        // 获取布局中的视图组件并设置操作
        val etTitle: AppCompatEditText = customDialog.findViewById(R.id.et_title)
        val etUrl: AppCompatEditText = customDialog.findViewById(R.id.et_url)
        val btnOk: AppCompatTextView = customDialog.findViewById(R.id.tv_ok)
        etTitle.postDelayed({ KeyboardUtils.showKeyboard(etTitle) }, 100)
        Linkify.addLinks(etUrl, Linkify.WEB_URLS);
        btnOk.setOnClickListener {
            val title = etTitle.text
            if (TextUtils.isEmpty(title)) {
                ToastUtils.show(requireContext().getString(R.string.please_enter_title))
                return@setOnClickListener
            }
            val url = etUrl.text.toString()
            if (TextUtils.isEmpty(url)) {
                ToastUtils.show(requireContext().getString(R.string.please_enter_url))
                return@setOnClickListener
            }
            customDialog.dismiss()
        }
        customDialog.show()


    }


}