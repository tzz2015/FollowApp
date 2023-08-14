package com.stardust.autojs.runtime.exception;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/7/1.
 */
@Keep
public class ScriptEnvironmentException extends ScriptException {
    public ScriptEnvironmentException(String s) {
        super(s);
    }
}
