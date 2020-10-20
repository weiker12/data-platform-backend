package com.peilian.dataplatform.config;

/**
 * @author zhengshangchao
 */
public enum ResponseMessageCode {

    /** 200请求成功 */
    OK("200"),
    /** 207频繁操作 */
    MULTI_STATUS("207"),
    /** 303登录失败 */
    LOGIN_FAIL("303"),
    /** 400请求参数出错 */
    BAD_REQUEST("400"),
    /** 401没有登录 */
    UNAUTHORIZED("401"),
    /** 403没有权限 */
    FORBIDDEN("403"),
    /** 404找不到页面 */
    NOT_FOUND("404"),
    /** 408请求超时 */
    REQUEST_TIMEOUT("408"),
    /** 409发生冲突 */
    CONFLICT("409"),
    /** 410已被删除 */
    GONE("410"),
    /** 423已被锁定 */
    LOCKED("423"),
    /** 500服务器出错 */
    INTERNAL_SERVER_ERROR("500");

    private String code;

    ResponseMessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
