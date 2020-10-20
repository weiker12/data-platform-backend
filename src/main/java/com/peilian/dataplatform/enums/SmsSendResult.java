package com.peilian.dataplatform.enums;



/**
 * 短信发送结果
 * @author zhengshangchao
 */
public enum SmsSendResult {

    SUCCESS(200, "成功"),
    /**
     * 短信验证码发送次数达到上限
     */
    MAX_SEND_TIME_LIMIT(160001009, "验证码发送频次超过限制, 请稍后再试"),
    /**
     * 短信验证码发送失败
     */
    FAILED(160002003,"验证码发送失败，请重试或联系客服");

    private Integer code;
    private String msg;

    SmsSendResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public Integer getCode() {
        return code;
    }

}
