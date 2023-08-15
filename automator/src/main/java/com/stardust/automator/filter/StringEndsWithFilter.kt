package com.stardust.automator.filter

import androidx.annotation.Keep
import com.stardust.automator.UiObject

/**
 * Created by Stardust on 2017/3/9.
 */
@Keep
class StringEndsWithFilter(private val mSuffix: String, private val mKeyGetter: KeyGetter) : Filter {

    override fun filter(node: UiObject): Boolean {
        val key = mKeyGetter.getKey(node)
        return key != null && key.endsWith(mSuffix)
    }

    override fun toString(): String {
        return mKeyGetter.toString() + "EndsWith(\"" + mSuffix + "\")"
    }
}
