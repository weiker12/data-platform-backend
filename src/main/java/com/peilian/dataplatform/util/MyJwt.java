package com.peilian.dataplatform.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Fable on 16/5/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MyJwt implements Serializable {

    /**
     * jwt header
     */
    private String iss; //该JWT的签发者
    private String sub; //该JWT所面向的用户
    private String aud; //接收该JWT的一方：IP
    private String jti; //token
    private long exp; //什么时候过期，这里是一个Unix时间戳

    /**
     * jwt playload
     */
    private int ttls; //过期s数
    private String cellphone; // 用户手机号
    
    @JsonIgnore
    private Integer status; //0正常 -1非法 -2已过期 -3jwt密钥非法 -4未提供
    @JsonIgnore
    private String statusMsg;

}

