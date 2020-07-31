package com.peilian.dataplatform.config;

/**
 * @author zhengshangchao
 */
public class Result {

    private static final ResponseMessage RESPONSE_MESSAGE_SUCCESS = new ResponseMessage(ResponseMessageCodeEnum.OK.getCode(), "");

    public static ResponseMessage success() {
        return RESPONSE_MESSAGE_SUCCESS;
    }

    public static <T> ResponseMessage<T> success(T t) {
        return new ResponseMessage(ResponseMessageCodeEnum.OK.getCode(), "查询成功", t);
    }

    public static ResponseMessage error() {
        return error("");
    }

    public static ResponseMessage error(String message) {
        return error(ResponseMessageCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static ResponseMessage error(String code, String message) {
        return error(code, message, null);
    }

    public static <T> ResponseMessage<T> error(String code, String message, T t) {
        return new ResponseMessage(code, message, t);
    }
}
