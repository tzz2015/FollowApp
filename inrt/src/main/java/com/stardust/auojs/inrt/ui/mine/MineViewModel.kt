package com.stardust.auojs.inrt.ui.mine

import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.jeremyliao.liveeventbus.LiveEventBus
import com.linsh.utilseverywhere.LogUtils
import com.mind.data.data.model.AnnouncementModel
import com.mind.data.data.model.FunctionType
import com.mind.data.event.MsgEvent
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.data.Constants.IMAGE_ARRAY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MineViewModel @Inject constructor() : BaseViewModel() {
    private val mValueAnimatorList: MutableList<ValueAnimator> = ArrayList()
    val announcementList by lazy { ViewModelEvent<MutableList<AnnouncementModel>>() }
    private val mContext: Context by lazy { GlobalAppContext.get() }


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
        val randomInt = Random.nextInt(0, IMAGE_ARRAY.size)
        return IMAGE_ARRAY[randomInt]
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
        valueAnimator.duration = 4000
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

    fun login() {
        LogUtils.e("点击登录")
        val intent = Intent(mContext, LoginActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

    fun clearAnimation() {
        for (animator in mValueAnimatorList) {
            animator.removeAllUpdateListeners()
            animator.removeAllListeners()
        }
        mValueAnimatorList.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearAnimation()
    }

    /**
     * 点击功能
     */
    fun clickFunction(item: String) {
        when (item) {
            FunctionType.LOGOUT -> logout()
        }
    }

    private fun logout() {
        loadHttp(
            request = { ApiClient.userApi.logout() },
            resp = {
                LiveEventBus.get(MsgEvent.TOKEN_OUT).post("")
            },
        )
    }
}