package com.peilian.dataplatform.enums;

/**
 * 业务错误码
 */
public enum CodeMsg {

    SYSTEM_ERROR(500,"抱歉 系统开小差了！"),
	ROLE_ERROR(1002,"账号登录失败，请重新登录"),
	CODE_1003(1003,"获取身份验证信息异常！"),
    CODE_1004(1004,"账号超时，请重新登录"),
    CODE_1009(1009,"服务器未响应，请重新尝试"),
    CODE_1000(1000,"参数信息异常"),

    /**
     * 当前手机号获取验证码次数达到上限, 请联系客服进行处理
     */
    MOBILE_MORE_THAN_GET_CODE(160001009,"当前手机号获取验证码次数达到上限, 请联系客服进行处理"),
    SMS_SERVICE_SEND_LIMIT(160001010,"验证码发送频次超过限制, 请稍后再试"),
    CODE_160002003(160002003,"验证码发送失败，请重试或联系客服"),
    CODE_160002004(160002003, "手机号不正确，无法获取验证码"),

    CODE_200(200, "成功"),
    CODE_401(401,"无访问权限");


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
