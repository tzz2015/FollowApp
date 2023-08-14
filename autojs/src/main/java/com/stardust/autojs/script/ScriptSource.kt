package com.stardust.autojs.script

import androidx.annotation.Keep
import java.io.Serializable

/**
 * Created by Stardust on 2017/4/2.
 */
@Keep
abstract class ScriptSource(val name: String) : Serializable {
    abstract val engineName: String
}