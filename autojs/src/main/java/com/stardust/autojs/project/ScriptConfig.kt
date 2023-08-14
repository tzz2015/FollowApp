package com.stardust.autojs.project

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Modified by wilinz on 2022/5/23
 */
@Keep
data class ScriptConfig(
        @SerializedName("useFeatures") var features: List<String>,
        @SerializedName("uiMode") var uiMode: Boolean
) {
    constructor() : this(emptyList(), false)

    fun hasFeature(feature: String): Boolean {
        return features.contains(feature)
    }

    companion object {
        val FEATURE_CONTINUATION = "continuation"
    }
}