package com.mind.data.data.model

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-03 08:18.
 * @Description :
 */
data class FollowAccount(
    val account: String,
    val followType: Int,
    val needFollowedCount: Int,
    val followCount:Int,
    val followedCount: Int,
    val payFollowCount: Int
)
