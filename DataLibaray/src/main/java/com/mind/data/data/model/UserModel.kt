package com.mind.data.data.model

/**
 * @Author : liuyufei
 * @Date : on 2023-07-30 17:16.
 * @Description :
 */
data class UserModel(
    var token: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var email: String? = null,
    var userId: String? = null
)