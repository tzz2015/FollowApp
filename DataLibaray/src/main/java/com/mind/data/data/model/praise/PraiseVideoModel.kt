package com.mind.data.data.model.praise

import androidx.annotation.Keep

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-24 06:32.
 * @Description :
 */
@Keep
data class PraiseVideoModel(
    var id: Long = -1,
    var userId: Long = -1,
    var followType: Int = 1,
    var title: String = "",
    var url: String = "",
    var count: Int = 0
)
