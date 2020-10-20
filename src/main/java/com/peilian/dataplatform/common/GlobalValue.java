package com.peilian.dataplatform.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author liuxiaogang
 * @date 2018年9月26日
 */
@Log4j2
@Component
public class GlobalValue {

    public static String jwtkey;
    public static String rootPath;
    public static boolean jwtEnable;
    // msName
    public static String msName;
    // 环境配置
    public static String profile;
    // jwtTimeOut
    public static String jwtTimeOut;

    @Value("${jwt.key}")
    public void setJwtkey(String jwtkey) {
        GlobalValue.jwtkey = jwtkey;
    }

    @Value("${server.servlet.context-path}")
    public void setRootPath(String rootPath) {
        GlobalValue.rootPath = rootPath;
    }

    @Value("${jwt.enable:true}")
    public void setJwtEnable(boolean jwtEnable) {
        GlobalValue.jwtEnable = jwtEnable;
    }

    @Value("${info.name}")
    public void setMsName(String msName) {
        GlobalValue.msName = msName;
    }

    @Value("${info.profile:test}")
    public void setProfile(String profile) {
        GlobalValue.profile = profile;
    }

    @Value("${jwt.timeout}")
    public void setJwtTimeOut(String jwtTimeOut) {
        GlobalValue.jwtTimeOut = jwtTimeOut;
    }

    @PostConstruct
    public void init() {
        log.info("========默认参数初始化=========");
    }
}
