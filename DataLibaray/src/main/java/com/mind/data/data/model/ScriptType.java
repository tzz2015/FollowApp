package com.mind.data.data.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;

/**
 * @author LYF
 * @dec:
 * @date 2023/7/8
 **/
@Retention(SOURCE)
public @interface ScriptType {
    /**
     * 抖音关注
     */
    int DOU_YIN = 0;
    /**
     * 抖音点赞
     */
    int DOU_YIN_PRAISE = 1;

    /**
     * Tik top关注
     */
    int TIK_TOP = 2;
    /**
     * Tik top点赞
     */
    int TIK_TOP_PRAISE = 3;

    /**
     * 抖音刷屏
     */
    int DOUYIN_COMMENT = 4;

    /**
     * tik top刷屏
     */
    int TIK_TOP_COMMENT = 5;


}
