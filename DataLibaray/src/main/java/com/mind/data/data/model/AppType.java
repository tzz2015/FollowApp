package com.mind.data.data.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;

/**
 * @author LYF
 * @dec:
 * @date 2023/7/8
 **/
@Retention(SOURCE)
public @interface AppType {
    /**
     * 抖音
     */
    int DOU_YIN = 0;
    /**
     * Tiktop
     */
    int TIKTOP = 1;


}
