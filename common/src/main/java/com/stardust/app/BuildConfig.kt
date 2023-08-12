package com.stardust.app

import androidx.annotation.Keep
import kotlin.reflect.full.primaryConstructor

@Keep
data class BuildConfig(
    @JvmField
    val DEBUG: Boolean = false,
    @JvmField
    val APPLICATION_ID: String = "",
    @JvmField
    val BUILD_TYPE: String = "",
    @JvmField
    val VERSION_CODE: Long = 0,
    @JvmField
    val VERSION_NAME: String = ""
) {
    companion object {
        fun generate(rawBuildConfigClass: Class<*>): BuildConfig {
            if (rawBuildConfigClass.simpleName != "BuildConfig") {
                throw Exception("please pass in build config and ignore code obfuscation!")
            }
            val constructor = BuildConfig::class.primaryConstructor!!
            val paramList = mutableListOf<Any>()
            for (field in constructor.parameters) {
                field.name?.let {
                    try {
                        val param = rawBuildConfigClass.getField(it).get(null)
                        param?.let { paramList.add(param) }
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    }
                }
            }
            return constructor.call(*paramList.toTypedArray())
        }
    }
}