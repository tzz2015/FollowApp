package com.stardust.automator.simple_action

import androidx.annotation.Keep
import com.stardust.automator.UiObject

/**
 * Created by Stardust on 2017/1/27.
 */
@Keep
abstract class SimpleAction {

    @Volatile
    var isValid = true
    @Volatile
    var result = false

    abstract fun perform(root: UiObject): Boolean

}
