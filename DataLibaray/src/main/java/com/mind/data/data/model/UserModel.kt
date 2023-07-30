package com.mind.data.data.model

/**
 * @Author : liuyufei
 * @Date : on 2023-07-30 17:16.
 * @Description :
 */
data class UserModel(
    val token: String,
    val phone: String,
    val password: String,
    val email: String,
    val userId: String
)