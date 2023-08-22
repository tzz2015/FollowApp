package com.mind.data.data.model

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-03 08:18.
 * @Description :
 */
data class FollowAccount(
    var account: String? = null,
    var userId: Long = 0L,
    var followType: Int = FollowType.DOU_YIN,
    var needFollowedCount: Int = 0,
    var followCount: Int = 0,
    var followedCount: Int = 0,
    var payFollowCount: Int = 0
)
