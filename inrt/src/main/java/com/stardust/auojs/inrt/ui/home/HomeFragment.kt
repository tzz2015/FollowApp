package com.stardust.auojs.inrt.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.BR
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.LogUtils
import com.mind.data.event.MsgEvent
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission.launchCanDrawOverlaysSettings
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool1
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


    override fun init(savedInstanceState: Bundle?) {
        bind.userModel = userViewModel
        bind.followModel = followViewModel
        setCheckedChangeListener()
        userViewModel.getFollowAccount()
        viewModel.getScript()
        initObserve()
    }

    private fun initObserve() {
        userViewModel.followAccount.observe(viewLifecycleOwner) {
            it?.run {
                CacheManager.instance.putDYAccount(account)
                bind.tvNoticeBind.text = String.format(
                    requireContext().getText(R.string.follow_bind_account).toString(), account
                )
                bind.btnBindAccount.text = requireContext().getText(R.string.to_change).toString()
            }
        }
        LiveEventBus.get(MsgEvent.LOGIN_TOKEN_EVENT).observe(viewLifecycleOwner) {
            userViewModel.getFollowAccount()
            viewModel.getScript()
        }
        LiveEventBus.get(MsgEvent.CHANGE_USER_INFO).observe(viewLifecycleOwner) {
            userViewModel.getFollowAccount()
        }
        viewModel.followList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showFollowDialog(it.size)
            }
        }
        bind.btnFollow.setOnClickListener { requestPermissions() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNeedPermissions()
        userViewModel.getTotalUserCount()
        followViewModel.getTotalFollowCount()
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
            message(text = getString(R.string.text_required_floating_window_permission))
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
                viewModel.runFollowScript()
            })
            negativeButton { dismiss() }
        }
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