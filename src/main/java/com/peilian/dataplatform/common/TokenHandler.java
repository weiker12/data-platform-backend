package com.peilian.dataplatform.common;

import com.peilian.dataplatform.config.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 校验
 *
 * @author zhengshangchao
 * @date 2020/6/1
 * @since 1.0.0
 */
public interface TokenHandler {

    /**
     * jwt校验
     *
     * @param httpRequest
     * @param httpResponse
     * @throws UnauthorizedException
     */
    void authentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws UnauthorizedException;
}
