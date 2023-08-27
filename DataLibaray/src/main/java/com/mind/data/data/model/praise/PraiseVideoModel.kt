package com.mind.data.data.model.praise

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-24 06:32.
 * @Description :
 */
data class PraiseVideoModel(
    var id: Long = -1,
    var followType: Int = 1,
    var title: String = "",
    var url: String = "",
    var count: Int = 0
)