package com.stardust.auojs.inrt.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.BR
import com.linsh.utilseverywhere.LogUtils
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission.launchCanDrawOverlaysSettings
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {


    private val userViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private val followViewModel by lazy {
        ViewModelProvider(this)[FollowViewModel::class.java]
    }
    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_home).bindViewModel(BR.homeModel)


    override fun init(savedInstanceState: Bundle?) {
        bind.userModel = userViewModel
        bind.followModel = followViewModel
        setCheckedChangeListener()
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

    private val accessibilitySettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkNeedPermissions()
        }

    private val drawOverlaysSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkNeedPermissions()
        }


}