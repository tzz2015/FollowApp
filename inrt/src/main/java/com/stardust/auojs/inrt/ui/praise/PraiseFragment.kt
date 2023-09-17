package com.stardust.auojs.inrt.ui.praise

import android.Manifest
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
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.KeyboardUtils
import com.linsh.utilseverywhere.RegexUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.ui.adapter.PraiseVideoAdapter
import com.stardust.auojs.inrt.ui.home.HomeFragment
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.auojs.inrt.util.formatLargeNumber
import com.stardust.auojs.inrt.util.getCommentType
import com.stardust.auojs.inrt.util.getPraiseType
import org.autojs.autoxjs.inrt.BR
import org.autojs.autoxjs.inrt.BuildConfig
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentPraiseBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.*

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-21 22:10.
 * @Description :
 */
class PraiseFragment : BaseFragment<PraiseViewModel, FragmentPraiseBinding>(),
    EasyPermissions.PermissionCallbacks {
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_praise).bindViewModel(BR.praiseViewModel)
    private val mAdapter: PraiseVideoAdapter by lazy { PraiseVideoAdapter() }

    override fun init(savedInstanceState: Bundle?) {
        initObserve()
        bind.marqueeTextView.isSelected = true
        initRecyclerView()
        AdUtils.showBannerAd(requireActivity(), bind.fl)
        initView()
        initData()
    }

    private fun initData() {
        viewModel.getScript(getPraiseType())
        viewModel.getScript(getCommentType())
        viewModel.getTotalPraiseCount()
        viewModel.getPraiseAccount()
        viewModel.getPraiseVideoList()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNeedPermissions()
    }

    private fun initView() {
        bind.tvAdd.setOnClickListener { showEditDialog(null) }
        bind.tvPraise.setOnClickListener { requestPermissions() }
        if (!BuildConfig.DEBUG) {
            bind.tvShare.isVisible = false
            bind.tvComment.isVisible = false
        }
        val emptyText = String.format(
            Locale.ENGLISH,
            requireContext().getText(R.string.click_add_url).toString(),
            requireContext().getText(R.string.add_video_url).toString()
        )
        bind.tvEmpty.text = emptyText
    }

    private fun initObserve() {
        viewModel.praiseAccount.observe(viewLifecycleOwner) {
            bind.tvOne.text = formatLargeNumber(it.needPraiseCount.toLong())
            bind.tvTwo.text = formatLargeNumber(it.praiseCount.toLong())
            bind.tvThree.text = formatLargeNumber(it.praisedCount.toLong())
        }
        viewModel.praiseVideoList.observe(viewLifecycleOwner) {
            mAdapter.setNewInstance(it)
            bind.tvEmpty.isVisible = it.isEmpty()
        }
        viewModel.praiseVideo.observe(viewLifecycleOwner) {
            updateAdapter(it)
        }
        viewModel.enablePraiseVideoList.observe(viewLifecycleOwner) {
            showPraiseDialog(it.size)
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            initData()
        }
    }

    private fun showPraiseDialog(size: Int) {
        MaterialDialog(requireActivity()).show {
            setTitle(R.string.can_praise_title)
            message(text = String.format(getString(R.string.can_praise_text), size))
            positiveButton(res = R.string.to_praise, click = {
                dismiss()
                viewModel.runPraiseScript(requireActivity())
            })
            negativeButton { dismiss() }
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
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = mAdapter
        /* mAdapter.setOnItemClickListener { _, _, position ->
             val item = mAdapter.getItem(position)
             val intent = Intent()
             intent.action = "android.intent.action.VIEW"
             val contentUrl = Uri.parse(item.url)
             intent.data = contentUrl
             startActivity(intent)
         }*/
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.getItem(position)
            when (view.id) {
                R.id.tv_check -> toCheck(item)
                R.id.tv_delete -> showDeleteDialog(item)
                R.id.tv_edit -> showEditDialog(item)
            }
        }
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

    @AfterPermissionGranted(HomeFragment.RC_STORAGE)
    private fun requestPermissions() {
        // 请求权限
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireContext(), *permissions)) {
            viewModel.checkRunScript()
        } else {
            // 请求权限
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, HomeFragment.RC_STORAGE, *permissions)
                    .setRationale("需要读写存储权限以进行操作。")
                    .setPositiveButtonText("授予")
                    .setNegativeButtonText("取消")
                    .build()
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }


}