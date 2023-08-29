package com.mind.data.data.model.praise

import androidx.annotation.Keep

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-23 20:32.
 * @Description :
 */
@Keep
data class PraiseAccountModel(
    val needPraiseCount: Int,
    val praiseCount: Int,
    val praisedCount: Int
)
