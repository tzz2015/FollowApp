package com.mind.data.data.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;

/**
 * @author LYF
 * @dec:
 * @date 2023/7/8
 **/
@Retention(SOURCE)
public @interface FunctionType {

    String CHANGE_PHONE = "修改手机号码";
    String CHANGE_EMAIL = "修改邮箱";
    String CHANGE_PSW = "修改密码";
    String FEEDBACK = "意见反馈";
    String LOGOUT = "注销登录";


}
