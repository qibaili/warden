package com.qibaili.warden.web.system.common.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


/**
 * @author qibaili
 * @date 2018/6/9
 */

@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 操作返回
     */
    OK(1, "操作成功"),
    LOGIN_OK(2, "登录成功"),
    LOGOUT_OK(3, "注销登录成功"),
    FAIL(-1, "操作失败"),
    LOGOUT_FAIL(-2, "注销登录失败"),
    USER_STATUS_ERROR(-3, "用户状态异常，请联系管理员"),
    LOGIN_INPUT_FAIL(-4, "账号或密码错误"),
    LOGIN_INPUT_EMPTY(-5, "账号或密码不能为空"),
    SESSION_TIME_OUT(-6, "登陆超时，请重新登录");

    public Integer code;

    public String message;
}
