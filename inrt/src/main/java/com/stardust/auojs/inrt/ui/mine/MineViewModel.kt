package com.stardust.auojs.inrt.ui.mine

import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.mind.data.data.model.AnnouncementModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.autojs.autoxjs.inrt.R
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MineViewModel @Inject constructor() : BaseViewModel() {
    private val mValueAnimatorList: MutableList<ValueAnimator> = ArrayList()

    val announcementList by lazy { ViewModelEvent<MutableList<AnnouncementModel>>() }
    private val imageArray = arrayOf(
        R.mipmap.icon_header_1_1,
        R.mipmap.icon_header_1_2,
        R.mipmap.icon_header_1_3,
        R.mipmap.icon_header_1_4,
        R.mipmap.icon_header_1_5,
        R.mipmap.icon_header_1_6,
        R.mipmap.icon_header_1_7,
        R.mipmap.icon_header_1_8,
        R.mipmap.icon_header_1_9,
        R.mipmap.icon_header_1_10,
        R.mipmap.icon_header_1_11,
        R.mipmap.icon_header_1_12,
        R.mipmap.icon_header_1_13,
        R.mipmap.icon_header_1_14,
        R.mipmap.icon_header_1_15,
        R.mipmap.icon_header_1_16,
        R.mipmap.icon_header_1_17,
    )

    val functionArray = arrayOf("修改手机号码", "修改邮箱", "修改密码", "意见反馈", "注销登录")

    fun getAnnouncementList() {
        loadHttp(
            request = { ApiClient.otherApi.getAnnouncementList() },
            resp = {
                Log.e(javaClass.name, "getAnnouncementList:${it.toString()} ")
                it?.let {
                    announcementList.postValue(it)
                }
            },
            isShowDialog = false
        )
    }

    fun getHeadImage(): Int {
        val randomInt = Random.nextInt(0, imageArray.size)
        return imageArray[randomInt]
    }

    fun doParabolaAnimation(
        view: View,
        startX: Int,
        startY: Int,
        controlX: Int,
        controlY: Int,
        endX: Int,
        endY: Int
    ) {
        // 创建 ValueAnimator 实例
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        // 设置插值器为线性插值器，使得动画匀速进行
        valueAnimator.interpolator = LinearInterpolator()
        // 设置动画时长，单位为毫秒
        valueAnimator.duration = 3000
        valueAnimator.repeatMode = REVERSE
        valueAnimator.repeatCount = -1
        // 添加动画更新监听器
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedValue as Float
            // 根据贝塞尔曲线公式计算 View 的当前位置
            val currentX =
                (1 - fraction) * (1 - fraction) * startX + 2 * fraction * (1 - fraction) * controlX + fraction * fraction * endX
            val currentY =
                (1 - fraction) * (1 - fraction) * startY + 2 * fraction * (1 - fraction) * controlY + fraction * fraction * endY
            // 将 View 移动到计算得到的位置
            view.x = currentX
            view.y = currentY
        }
        // 开始动画
        valueAnimator.start()
        mValueAnimatorList.add(valueAnimator)
    }

    override fun onCleared() {
        super.onCleared()
        for (animator in mValueAnimatorList) {
            animator.removeAllUpdateListeners()
            animator.removeAllListeners()
        }
        mValueAnimatorList.clear()
    }
}