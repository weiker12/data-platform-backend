package com.peilian.dataplatform.controller;

import com.peilian.dataplatform.common.Auth;
import com.peilian.dataplatform.common.AuthEnum;
import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.config.Result;
import com.peilian.dataplatform.dto.LoginDto;
import com.peilian.dataplatform.dto.SmsCodeDto;
import com.peilian.dataplatform.service.LoginService;
import com.peilian.dataplatform.util.CommonValidator;
import com.peilian.dataplatform.util.SmsUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class LoginController {

    @Autowired
    CommonValidator commonValidator;

    @Autowired
    LoginService loginService;

    @Autowired
    SmsUtil smsUtil;

    /**
     * 登录接口
     *
     * @param loginDto
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    @Auth(value = AuthEnum.NO_LOGIN)
    public ResponseMessage login(@RequestBody LoginDto loginDto) throws BizException {
        log.info("入参loginDto={}", loginDto);
        commonValidator.checkDto(loginDto);
        String jwt = loginService.login(loginDto);
        return Result.success(jwt);
    }


    /**
     * 发送短信验证码接口
     *
     * @param smsCodeDto
     * @return
     */
    @ApiOperation(value = "发送短信验证码接口")
    @PostMapping("/sendSmsCode")
    @Auth(value = AuthEnum.NO_LOGIN)
    public ResponseMessage sendSmsCode(@RequestBody SmsCodeDto smsCodeDto) throws BizException {
        log.info("入参cellphone={}", smsCodeDto);
        smsUtil.sendSms(smsCodeDto.getCellphone());
        return Result.success();
    }

}
