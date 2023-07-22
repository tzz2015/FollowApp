package com.stardust.auojs.inrt.ui.home

import com.linsh.utilseverywhere.ToastUtils
import com.mind.lib.base.BaseViewModel
import com.stardust.app.GlobalAppContext
import com.stardust.app.permission.DrawOverlaysPermission
import com.stardust.auojs.inrt.autojs.AccessibilityServiceTool
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.launch.GlobalProjectLauncher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.autojs.autoxjs.inrt.R
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel() {

    private val _permiss = MutableStateFlow(Pair(false, false))
    val permiss = _permiss.asStateFlow()

    fun checkNeedPermissions() {
        val accessibilityEnabled =
            AccessibilityServiceTool.isAccessibilityServiceEnabled(GlobalAppContext.get())
        val isCanDrawOverlays = DrawOverlaysPermission.isCanDrawOverlays(GlobalAppContext.get())
        _permiss.update {
            it.copy(accessibilityEnabled, isCanDrawOverlays)
        }
    }

    fun toFollow() {
        val first = permiss.value.first
        val second = permiss.value.second
        if (!first) {
            ToastUtils.show(
                GlobalAppContext.get()
                    .getString(R.string.text_accessibility_service_is_not_turned_on)
            )
            return
        }
        if (!second) {
            ToastUtils.show(
                GlobalAppContext.get().getString(R.string.text_required_floating_window_permission)
            )
            return
        }
        stopRunScript()
        Thread {
            GlobalProjectLauncher.runScript(Constants.DOUYIN_JS)
        }.start()
    }

    fun stopRunScript() {
        GlobalProjectLauncher.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stopRunScript()
    }
}