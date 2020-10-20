package com.peilian.dataplatform.config;

/**
 * @param <T>
 * @author zhengshangchao
 */
public class ResponseMessage<T> {

    private String code;
    private String message;
    private T data;

    public ResponseMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseMessage(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isOk() {
        return this.code.equals(ResponseMessageCode.OK.getCode());
    }
}
