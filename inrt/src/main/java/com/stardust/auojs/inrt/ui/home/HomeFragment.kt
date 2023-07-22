package com.stardust.auojs.inrt.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.linsh.utilseverywhere.LogUtils
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission.launchCanDrawOverlaysSettings
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewModel = homeViewModel
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCheckedChangeListener()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.checkNeedPermissions()
    }

    private fun setCheckedChangeListener() {
        binding.swAccessibility.setOnCheckedChangeListener { buttonView, isChecked ->
            LogUtils.e("buttonView:${buttonView.id} isChecked:${isChecked}")
            val hasPermissions = homeViewModel.permiss.value.first
            if (!hasPermissions && isChecked) {
                showAccessibilityDialog()
            }
        }
        binding.swDrawOverlays.setOnCheckedChangeListener { buttonView, isChecked ->
            LogUtils.e("buttonView:${buttonView.id} isChecked:${isChecked}")
            val hasPermissions = homeViewModel.permiss.value.second
            if (!hasPermissions && isChecked) {
                showDrawOverlaysDialog()
            }
        }
    }

    private fun showDrawOverlaysDialog() {
        val dialog = MaterialDialog.Builder(requireActivity())
            .title(getString(R.string.text_required_floating_window_permission))
            .content(getString(R.string.text_required_floating_window_permission))//内容
            .positiveText(getString(R.string.text_to_open)) //肯定按键
            .negativeText(getString(R.string.text_cancel)).onPositive { dialog, _ ->
                dialog.dismiss()
                drawOverlaysSettingsLauncher.launchCanDrawOverlaysSettings(requireActivity().packageName)
            }.onNegative { dialog, _ ->
                dialog.dismiss()
            }.canceledOnTouchOutside(false).build()
        dialog.show()
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
                homeViewModel.checkNeedPermissions()
                return@launch
            }
            val dialog = MaterialDialog.Builder(requireActivity())
                .title(R.string.text_need_to_enable_accessibility_service)
                .content(R.string.explain_accessibility_permission, GlobalAppContext.appName)
                .positiveText(getString(R.string.text_to_open)) //肯定按键
                .negativeText(getString(R.string.text_cancel)).onPositive { dialog, _ ->
                    dialog.dismiss()
                    accessibilitySettingsLauncher.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }.onNegative { dialog, _ ->
                    dialog.dismiss()
                }.canceledOnTouchOutside(false).build()
            dialog.show()
        }

    }

    private val accessibilitySettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            homeViewModel.checkNeedPermissions()
        }

    private val drawOverlaysSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            homeViewModel.checkNeedPermissions()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}