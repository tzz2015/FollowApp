package com.stardust.automator.filter

import androidx.annotation.Keep
import com.stardust.automator.UiObject

/**
 * Created by Stardust on 2017/3/9.
 */
@Keep
interface Filter {

    fun filter(node: UiObject): Boolean

}
