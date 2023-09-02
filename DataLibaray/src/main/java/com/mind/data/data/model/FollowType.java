package com.mind.data.data.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;

/**
 * @author LYF
 * @dec:
 * @date 2023/7/8
 **/
@Retention(SOURCE)
public @interface FollowType {
    /**
     * 抖音关注
     */
    int DOU_YIN = 0;
    /**
     * 抖音点赞
     */
    int DOU_YIN_PRAISE = 1;

    /**
     * 抖音刷屏
     */
    int DOUYIN_COMMENT = 4;

}
