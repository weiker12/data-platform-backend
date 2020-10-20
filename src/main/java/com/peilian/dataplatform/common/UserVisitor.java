package com.peilian.dataplatform.common;

import lombok.Data;

/**
 * 请求头信息
 */
@Data
public class UserVisitor {

    /**
     * jwt token
     */
    private String jwtToken;

    /**
     * 登录人手机号
     */
    private String cellphone;

    /**
     * 日志ID
     */
    private String logId;

}
