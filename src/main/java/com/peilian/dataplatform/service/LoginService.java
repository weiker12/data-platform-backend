package com.peilian.dataplatform.service;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.dto.LoginDto;

/**
 * @author zhengshangchao
 */
public interface LoginService {

    /**
     * 登录接口
     * 收到短信和验证码校验合法后
     * 返回jwt
     * @param loginDto
     * @return
     * @throws BizException
     */
    String login(LoginDto loginDto) throws BizException;

}
