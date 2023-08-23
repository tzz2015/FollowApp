package com.mind.data.data.model.praise

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-24 06:32.
 * @Description :
 */
data class PraiseVideoModel(
    val id: Long,
    val followType: Int,
    val title: String,
    val url: String,
    val count: Int
)
