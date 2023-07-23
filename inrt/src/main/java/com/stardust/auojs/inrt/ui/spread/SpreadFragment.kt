package com.stardust.auojs.inrt.ui.spread

import android.Manifest
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import com.stardust.auojs.inrt.util.X5InitUtils
import org.autojs.autoxjs.inrt.BR
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentDashboardBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import kotlin.random.Random

class SpreadFragment : BaseFragment<SpreadViewModel, FragmentDashboardBinding>() {

    private var isReLoad = false

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_dashboard)
            .bindViewModel(BR.spreadModel)
    private val mCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (bind.webView.canGoBack()) {
                bind.webView.goBack()
            }
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        viewModel.getSpreadList()
        initObserver()
        checkStorageManagerPermission()
    }

    private fun initObserver() {
        viewModel.spreadList.observe(viewLifecycleOwner) {
            val randomInt = Random.nextInt(0, it.size)
            bind.webView.loadUrl(it[randomInt].url)
        }
        bind.fabRefresh.setOnClickListener { bind.webView.reload() }
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)
    }


    @AfterPermissionGranted(110)
    fun checkStorageManagerPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            X5InitUtils.init()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                110,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        mCallback.remove()
        bind.webView.destroy()
    }
}