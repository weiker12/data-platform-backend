package com.peilian.dataplatform.common;

import com.peilian.dataplatform.config.GlobalVar;
import com.peilian.dataplatform.config.UnauthorizedException;
import com.peilian.dataplatform.enums.CodeMsg;
import com.peilian.dataplatform.util.MyJwt;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;


/**
 * AccessToken 校验
 *
 * @author zhengshangchao
 * @date 2020/9/1
 * @since 1.0.0
 */
@Log4j2
@Component
public class IneffectiveJsonWebTokenHandler implements TokenHandler {

    @Value("${jwt.enable:true}")
    private boolean jwtEnable;
    @Value("${jwt.key}")
    private String jwtkey;
    @Value("${jwt.timeout:90000}")
    private long timeout;
    @Value("${sms.white.list:17521671240}")
    private String whiteList;

    @Override
    public void authentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws UnauthorizedException {
        UserVisitor visitor = UserThreadContext.getUserVisitor();
        if (!jwtEnable) {
            return;
        }
        String urlAll = httpRequest.getRequestURL().toString();

        // 校验jwt
        if (jwtEnable) {
            String jwt = httpRequest.getHeader(AppRequestConstants.HEADER_JWTTOKEN);
            log.info("jwtEnable:{},JWT:{},url:{}", jwtEnable, jwt, urlAll);
            if (jwt == null || jwt.length() < 32) {
                log.warn("jwtEnable:{},JWT非法或者缺失:{},url:{},ip:{}", jwtEnable, jwt, urlAll);
                throw new UnauthorizedException(CodeMsg.CODE_401.getMsg());
            } else {
                byte[] keyBytes = Base64.getDecoder().decode(jwtkey);
                MyJwt myJwt = JwtHelper.parseMyJwt(jwt, keyBytes);
                String cellphone = myJwt.getCellphone();
                if(myJwt != null && myJwt.getStatus() == -2) {
                    log.error("服务内部错误,jwt解释错误");
                    throw new UnauthorizedException(CodeMsg.CODE_1004.getMsg());
                }
                if (myJwt == null || !whiteList.contains(cellphone)) {
                    log.error("服务内部错误,jwt解释错误");
                    throw new UnauthorizedException(CodeMsg.ROLE_ERROR.getMsg());
                }
                visitor.setCellphone(myJwt.getCellphone());
                httpRequest.setAttribute(GlobalVar.CELLPHONE, myJwt.getCellphone());
            }
        }
    }

}
