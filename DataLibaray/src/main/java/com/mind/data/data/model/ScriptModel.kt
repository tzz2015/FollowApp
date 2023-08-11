package com.mind.data.data.model

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-10 08:41.
 * @Description : 脚本
 */
data class ScriptModel(
    val version: Int,
    val followType: Int,
    val decryptKey: String,
    val scriptText: String
)
