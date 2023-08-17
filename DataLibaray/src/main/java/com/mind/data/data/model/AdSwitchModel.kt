package com.mind.data.data.model

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-10 08:41.
 * @Description : 开关
 */
data class AdSwitchModel(
    val version: String,
    val startSwitch: Boolean,
    val bannerSwitch: Boolean,
    val followSwitchNum: Int,
    val tableSwitch: Boolean,
    val reserveSwitch: Boolean

)
