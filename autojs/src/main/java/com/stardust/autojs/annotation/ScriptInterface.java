package com.stardust.autojs.annotation;

import androidx.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Stardust on 2017/4/2.
 */
@Keep
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface ScriptInterface {
}
