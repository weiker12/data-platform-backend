package com.peilian.dataplatform.enums;

/**
 * 业务错误码
 */
public enum CodeMsg {
    /**
     * 参数信息异常
     */
    CODE_1000(1000,"参数信息异常"),
    /**
     * 账号登录校验
     */
    LOGIN_ERROR(1001, "账号登录失败，请重新登录"),
    /**
     * 账号超时
     */
    LOGIN_TIMEOUT(1002, "账号超时，请重新登录"),
    /**
     * 无权限访问
     */
    CODE_401(401, "无访问权限");


    private Integer code;
    private String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
