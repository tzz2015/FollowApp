package com.lyflovelyy.followhelper.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.KeyboardUtils
import com.linsh.utilseverywhere.RegexUtils
import com.linsh.utilseverywhere.ToastUtils
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.adapter.PraiseVideoAdapter
import com.lyflovelyy.followhelper.databinding.FragmentPraiseBinding
import com.lyflovelyy.followhelper.utils.afterLogin
import com.lyflovelyy.followhelper.viewmodel.PraiseViewModel
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import java.util.*

/**
 * @Author : liuyufei
 * @Date : on 2023-12-28 23:09.
 * @Description :
 */
class PraiseFragment : BaseFragment<PraiseViewModel, FragmentPraiseBinding>() {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_praise).bindViewModel(BR._all)
    private val mAdapter: PraiseVideoAdapter by lazy { PraiseVideoAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        initObserve()
        initRecyclerView()
        initView()
        initData()
    }

    private fun initObserve() {
        viewModel.praiseVideoList.observe(viewLifecycleOwner) {
            mAdapter.setNewInstance(it)
            bind.tvEmpty.isVisible = it.isEmpty()
        }
        viewModel.praiseVideo.observe(viewLifecycleOwner) {
            updateAdapter(it)
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            initData()
        }
    }

    private fun updateAdapter(praiseVideoModel: PraiseVideoModel) {
        val data = mAdapter.data
        for ((index, praiseVideo) in data.withIndex()) {
            if (praiseVideo.id == praiseVideoModel.id) {
                praiseVideo.title = praiseVideoModel.title
                praiseVideo.url = praiseVideoModel.url
                mAdapter.notifyItemChanged(index)
                return
            }
        }
        mAdapter.addData(0, praiseVideoModel)
        bind.tvEmpty.isVisible = false
    }

    private fun initView() {
        bind.tvAddUrl.setOnClickListener { afterLogin { showEditDialog(null) } }
        bind.tvPraise.setOnClickListener { afterLogin { } }
        val emptyText = String.format(
            Locale.ENGLISH,
            requireContext().getText(R.string.click_add_url).toString(),
            requireContext().getText(R.string.add_video_url).toString()
        )
        bind.tvEmpty.text = emptyText
    }

    private fun initData() {
        viewModel.getPraiseVideoList()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            when (view.id) {
                R.id.tv_check -> toCheck(item)
                R.id.tv_delete -> showDeleteDialog(item)
                R.id.tv_edit -> showEditDialog(item)
            }
        }
    }

    /**
     * 校验
     */
    private fun toCheck(item: PraiseVideoModel) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(item.url)
        intent.data = contentUrl
        startActivity(intent)
    }

    private fun showEditDialog(praiseVideoModel: PraiseVideoModel?) {
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(R.layout.input_praise_video_layout)
        // 设置对话框标题
        customDialog.setTitle(getString(R.string.add_video_url))
        // 获取布局中的视图组件并设置操作
        val etTitle: AppCompatEditText = customDialog.findViewById(R.id.et_title)
        val etUrl: AppCompatEditText = customDialog.findViewById(R.id.et_url)
        val btnOk: AppCompatTextView = customDialog.findViewById(R.id.tv_ok)
        praiseVideoModel?.let {
            etTitle.setText(it.title)
            etUrl.setText(it.url)
        }
        etTitle.postDelayed({ KeyboardUtils.showKeyboard(etTitle) }, 100)
        btnOk.setOnClickListener {
            val title = etTitle.text.toString()
            if (TextUtils.isEmpty(title)) {
                ToastUtils.show(requireContext().getString(R.string.please_enter_title))
                return@setOnClickListener
            }
            val url = etUrl.text?.toString()
            val regex = "https://[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]+"
            val findUrl = RegexUtils.find(url, regex, 0)
            if (TextUtils.isEmpty(findUrl)) {
                ToastUtils.show(requireContext().getString(R.string.please_enter_url))
                return@setOnClickListener
            }
            customDialog.dismiss()
            viewModel.addPraiseVideo(praiseVideoModel, title, findUrl)
        }
        customDialog.show()


    }

    private fun showDeleteDialog(item: PraiseVideoModel) {
        MaterialDialog(requireActivity()).show {
            setTitle(R.string.delete)
            message(text = getString(R.string.delete_notice))
            positiveButton(res = R.string.delete, click = {
                dismiss()
                viewModel.deletePraise(item) { success ->
                    if (success) {
                        mAdapter.remove(item)
                    } else {
                        ToastUtils.show(requireContext().getString(R.string.delete_fail))
                    }
                }
            })
            negativeButton { dismiss() }
        }
    }


}
