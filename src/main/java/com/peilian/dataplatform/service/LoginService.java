package com.peilian.dataplatform.service;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.dto.LoginDto;
import com.peilian.dataplatform.enums.ResultType;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * @author zhengshangchao
 */
public interface LoginService {

    /**
     * 登录接口
     * 收到短信和验证码校验合法后
     * 返回jwt
     *
     * @param loginDto
     * @return jwt
     */
    String login(LoginDto loginDto) throws BizException;

}
