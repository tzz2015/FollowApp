package com.stardust.auojs.inrt.ui.home

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission.launchCanDrawOverlaysSettings
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool1
import com.stardust.auojs.inrt.util.AdUtils
import com.stardust.auojs.inrt.util.getFollowType
import com.stardust.auojs.inrt.util.isZh
import com.stardust.util.ViewUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentHomeBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(),
    EasyPermissions.PermissionCallbacks {


    private val userViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private val followViewModel by lazy {
        ViewModelProvider(this)[FollowViewModel::class.java]
    }
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_home).bindViewModel(BR.homeModel)

    companion object {
        const val RC_STORAGE = 101
    }

    private var mTipAnimator: ObjectAnimator? = null

    override fun init(savedInstanceState: Bundle?) {
        bind.userModel = userViewModel
        bind.followModel = followViewModel
        setCheckedChangeListener()
        initData()
        initObserve()
        AdUtils.showBannerAd(requireActivity(), bind.fl)
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNeedPermissions()
    }

    private fun initView() {
        val num = MMKV.defaultMMKV().getInt(KV.FOLLOW_SWITCH_NUM, -1)
        bind.ivAd.isVisible = num > 0
        setAccount()
        initAppType()
        initTipShow()
    }

    private fun initTipShow() {
        bind.tipSwitch.hideText.setText(R.string.tip_1)
        bind.tipBind.hideText.setText(R.string.tip_2)
        bind.tipOpen.hideText.setText(R.string.tip_3)
        bind.tipFollow.hideText.setText(R.string.tip_4)
        bind.tipSwitch.tipRoot.setOnClickListener {
            bind.tipSwitch.tipRoot.isVisible = false
            bind.tipBind.tipRoot.isVisible = true
            MMKV.defaultMMKV().putInt(KV.TIP_MAIN_SHOW, 2)
            showAnimation(bind.tipBind.tipRoot)
        }
        bind.tipBind.tipRoot.setOnClickListener {
            bind.tipBind.tipRoot.isVisible = false
            bind.tipOpen.tipRoot.isVisible = true
            MMKV.defaultMMKV().putInt(KV.TIP_MAIN_SHOW, 3)
            showAnimation(bind.tipOpen.tipRoot)
        }
        bind.tipOpen.tipRoot.setOnClickListener {
            bind.tipOpen.tipRoot.isVisible = false
            bind.tipFollow.tipRoot.isVisible = true
            MMKV.defaultMMKV().putInt(KV.TIP_MAIN_SHOW, 4)
            showAnimation(bind.tipFollow.tipRoot)
        }
        bind.tipFollow.tipRoot.setOnClickListener {
            bind.tipFollow.tipRoot.isVisible = false
            MMKV.defaultMMKV().putInt(KV.TIP_MAIN_SHOW, 5)
            mTipAnimator?.cancel()
        }
        val showIndex = MMKV.defaultMMKV().getInt(KV.TIP_MAIN_SHOW, 1)
        bind.tipSwitch.tipRoot.isVisible = showIndex == 1
        bind.tipBind.tipRoot.isVisible = showIndex == 2
        bind.tipOpen.tipRoot.isVisible = showIndex == 3
        bind.tipFollow.tipRoot.isVisible = showIndex == 4
        if (showIndex == 1) {
            showAnimation(bind.tipSwitch.tipRoot)
        }
    }

    private fun showAnimation(view: View) {
        mTipAnimator?.cancel()
        mTipAnimator = ObjectAnimator.ofFloat(
            view,
            View.TRANSLATION_Y,
            0f,
            ViewUtils.dpToPx(context, 5).toFloat(),
            0f
        )
        mTipAnimator?.interpolator = AccelerateDecelerateInterpolator()
        mTipAnimator?.duration = 1000
        mTipAnimator?.repeatCount = -1
        mTipAnimator?.start()
    }

    private fun initAppType() {
        val options = arrayOf("抖音", "TikTok", "快手", "小红书", "YouTube")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
        bind.cbAppType.adapter = adapter
        bind.cbAppType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 1) {
                    ToastUtils.show(requireContext().getString(R.string.stay_tuned))
                } else {
                    MMKV.defaultMMKV().putInt(KV.APP_TYPE, position)
                    // 相当于重新登录
                    CacheManager.instance.putDYAccount("")
                    LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).post("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        var selectIndex = MMKV.defaultMMKV().getInt(KV.APP_TYPE, -1)
        if (selectIndex == -1) {
            selectIndex = if (isZh()) 0 else 1
        }
        bind.cbAppType.setSelection(selectIndex)
    }


    private fun initData() {
        userViewModel.getTotalUserCount()
        followViewModel.getTotalFollowCount()
        userViewModel.getFollowAccount()
        viewModel.getScript(getFollowType())
    }

    private fun initObserve() {
        userViewModel.followAccount.observe(viewLifecycleOwner) {
            it?.run {
                CacheManager.instance.putDYAccount(account)
                setAccount()
            }
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            userViewModel.getFollowAccount()
            viewModel.getScript(getFollowType())
        }
        LiveEventBus.get(MsgEvent.CHANGE_USER_INFO).observe(viewLifecycleOwner) {
            userViewModel.getFollowAccount(true)
        }
        viewModel.followList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showFollowDialog(it.size)
            }
        }
        bind.btnFollow.setOnClickListener { requestPermissions() }
    }

    private fun setAccount() {
        val dyAccount = CacheManager.instance.getDYAccount()
        if (!TextUtils.isEmpty(dyAccount)) {
            bind.tvNoticeBind.text = String.format(
                requireContext().getText(R.string.follow_bind_account).toString(), dyAccount
            )
            bind.btnBindAccount.text = requireContext().getText(R.string.to_change).toString()
        } else {
            bind.tvNoticeBind.text = requireContext().getText(R.string.follow_bind_text).toString()
            bind.btnBindAccount.text = requireContext().getText(R.string.to_bind).toString()
        }

    }


    private fun setCheckedChangeListener() {
        bind.swAccessibility.setOnCheckedChangeListener { buttonView, isChecked ->
            LogUtils.e("buttonView:${buttonView.id} isChecked:${isChecked}")
            val hasPermissions = viewModel.permiss.value.first
            if (!hasPermissions && isChecked) {
                showAccessibilityDialog()
            }
        }
        bind.swDrawOverlays.setOnCheckedChangeListener { buttonView, isChecked ->
            LogUtils.e("buttonView:${buttonView.id} isChecked:${isChecked}")
            val hasPermissions = viewModel.permiss.value.second
            if (!hasPermissions && isChecked) {
                showDrawOverlaysDialog()
            }
        }
    }

    private fun showDrawOverlaysDialog() {
        MaterialDialog(requireActivity()).show {
            setTitle(R.string.text_required_floating_window_permission)
            message(
                text = getString(R.string.text_required_floating_window_permission).format(
                    GlobalAppContext.appName
                )
            )
            positiveButton(res = R.string.text_to_open, click = {
                dismiss()
                drawOverlaysSettingsLauncher.launchCanDrawOverlaysSettings(requireActivity().packageName)
            })
            negativeButton { dismiss() }
        }
    }

    /**
     * 关注弹窗
     */
    private fun showFollowDialog(size: Int) {
        MaterialDialog(requireActivity()).show {
            setTitle(R.string.can_follow_title)
            message(text = String.format(getString(R.string.can_follow_text), size))
            positiveButton(res = R.string.to_follow, click = {
                dismiss()
                viewModel.runFollowScript(requireActivity())
            })
            negativeButton { dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mTipAnimator?.cancel()
    }

    private fun showAccessibilityDialog() {
        lifecycleScope.launch {
            val enabled = withContext(Dispatchers.IO) {
                AccessibilityServiceTool1.enableAccessibilityServiceByRootAndWaitFor(2000)
            }
            if (enabled) {
                Toast.makeText(
                    context,
                    getString(R.string.text_accessibility_service_turned_on),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.checkNeedPermissions()
                return@launch
            }
            MaterialDialog(requireActivity()).show {
                setTitle(R.string.text_need_to_enable_accessibility_service)
                message(
                    text = getString(R.string.explain_accessibility_permission).format(
                        GlobalAppContext.appName
                    )
                )
                positiveButton(res = R.string.text_to_open, click = {
                    dismiss()
                    accessibilitySettingsLauncher.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                })
                negativeButton { dismiss() }
            }
        }

    }

    @AfterPermissionGranted(RC_STORAGE)
    private fun requestPermissions() {
        // 请求权限
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireContext(), *permissions)) {
            viewModel.toFollow()
        } else {
            // 请求权限
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_STORAGE, *permissions)
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

    private val accessibilitySettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkNeedPermissions()
        }

    private val drawOverlaysSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkNeedPermissions()
        }


}