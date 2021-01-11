package com.peilian.dataplatform.service.impl;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.dto.LoginDto;
import com.peilian.dataplatform.service.LoginService;
import com.peilian.dataplatform.util.JwtHelper;
import com.peilian.dataplatform.util.MyJwt;
import com.peilian.dataplatform.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 接口实现类
 *
 * @author zhengshangchao
 * @since 2020-07-27 10:21:41
 */
@Slf4j
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${jwt.key}")
    private String base64Str;

    /**
     * 登录接口
     *
     * @param loginDto
     * @return
     * @throws Exception
     */
    @Override
    public String login(LoginDto loginDto) throws BizException {
        String smsCode = smsUtil.getSmsCacheCode(loginDto.getCellphone());
//        if (!loginDto.getSmsCode().equals(smsCode)) {
//            throw new BizException("请输入正确的验证码");
//        }
        MyJwt myJwt = new MyJwt();
        myJwt.setCellphone(loginDto.getCellphone());
        String token = JwtHelper.createMyJwt(myJwt, base64Str);
        return token;
    }


}