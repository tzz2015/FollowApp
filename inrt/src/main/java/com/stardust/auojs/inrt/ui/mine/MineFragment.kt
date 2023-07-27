package com.stardust.auojs.inrt.ui.mine

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mind.lib.base.BaseFragment
import com.mind.lib.base.ViewModelConfig
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.FragmentMineBinding

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override val viewModelConfig: ViewModelConfig
        get() = ViewModelConfig(R.layout.fragment_mine)


    override fun init(savedInstanceState: Bundle?) {
        viewModel.getAnnouncementList()
        setHeaderImage()
        setAnimator()
    }

    private fun setAnimator() {
        // 加载动画
        val animatorSet: AnimatorSet =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.anim_up_down) as AnimatorSet
        animatorSet.setTarget(bind.tvLogin)
        // 设置循环播放
        val objectAnimatorUp = animatorSet.childAnimations[0] as ObjectAnimator
        objectAnimatorUp.repeatMode = ObjectAnimator.REVERSE
        objectAnimatorUp.repeatCount = ObjectAnimator.INFINITE

        val objectAnimatorDown = animatorSet.childAnimations[1] as ObjectAnimator
        objectAnimatorDown.repeatMode = ObjectAnimator.REVERSE
        objectAnimatorDown.repeatCount = ObjectAnimator.INFINITE
        // 开始动画
        animatorSet.start()
    }

    private fun setHeaderImage() {
        Glide.with(requireContext()).load(viewModel.getHeadImage())
            .apply(RequestOptions.circleCropTransform()).into(bind.ivHeader)
    }
}