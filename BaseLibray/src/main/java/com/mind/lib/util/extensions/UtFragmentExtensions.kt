package com.mind.lib.util.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * 检查 [permissions] 的权限是否全部通过，是则返回 true。
 */
fun checkSelfPermissionsGranted(context: Context, permissions: Array<String>) : Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 * 检查 [permissions] 的权限是否有未通过的，是则返回 true。
 */
fun checkSelfPermissionsDenied(context: Context, permissions: Array<String>) : Boolean =
    !checkSelfPermissionsGranted(context, permissions)

fun Fragment.showDialog(dialog: DialogFragment) {
    if (!dialog.isAdded) {
        dialog.show(childFragmentManager, dialog.javaClass.name)
    }
}

fun Fragment.dismissDialog(dialog: DialogFragment) {
    if (dialog.isAdded) {
        dialog.dismiss()
    }
}


/**
 * 在 Fragment 中添加某个 View 的尺寸变化监听，并会在 Fragment 销毁时自动移除监听。
 */
fun Fragment.observeViewSize(view: View, onSizeChanged: (width: Int, height: Int) -> Unit) {
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var oldWidth: Int = view.width
        private var oldHeight: Int = view.height

        override fun onGlobalLayout() {
            val newWidth = view.width
            val newHeight = view.height

            if (oldWidth != newWidth || oldHeight != newHeight) {
                onSizeChanged(newWidth, newHeight)
                oldWidth = newWidth
                oldHeight = newHeight
            }
        }
    }

    view.viewTreeObserver.addOnGlobalLayoutListener(listener)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    })
}