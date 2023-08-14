package com.stardust.autojs.project

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Modified by wilinz on 2022/5/23
 */
@Keep
data class SigningConfig (
    @SerializedName("alias")
    var alias: String? = null,
    @SerializedName("keystore")
    var keyStore: String? = null
)