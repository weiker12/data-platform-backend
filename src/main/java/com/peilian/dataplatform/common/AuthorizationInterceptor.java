package com.peilian.dataplatform.common;

import com.peilian.dataplatform.config.GlobalVar;
import com.peilian.dataplatform.config.UnauthorizedException;
import com.peilian.dataplatform.util.JwtHelper;
import com.peilian.dataplatform.util.MyJwt;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * 接口鉴权拦截器
 * 兼容老逻辑，客户端不传uid(第三方回调接口), NO_LOGIN 注解需要配合执行
 * @author hui.wang
 * @date 2020/5/9
 * @since 1.0.0
 */
@Log4j2
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Value("${jwt.enable:true}")
    private boolean jwtEnable;

    @Value("${jwt.key}")
    private String jwtkey;

    @Autowired
    TokenHandler tokenHandler;


    /**
     * 5种权限
     * 1.NO_LOGIN：所有用户可访问（如：登陆、游客可见）
     * 2.APP_ALL：需要登陆后可访问，登陆后都可以访问（即uid>0）,默认权限
     * 3.REST_WORK：内部暴露的接口，需通过内网访问才予以放行（即RequestUrl使用内网IP/域名/k8s服务名）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException {
        if(!handler.getClass().isAssignableFrom(HandlerMethod.class)){
            return true;
        }
        String url = request.getRequestURI();
        String method = request.getMethod();
        // 预请求 埋点 第三方回调等相关的接口直接放行
        if (FilterConstant.isSpecialAPI(url) || "OPTIONS".equalsIgnoreCase(method) || FilterConstant.isSwaggerUrl(url)) {
            return true;
        }
        // 解析Auth注解
        UserVisitor visitor = UserThreadContext.getUserVisitor();
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method m = handlerMethod.getMethod();
        Auth authAnnotation = getMethodAuth(m);
        AuthEnum methodAuth = authAnnotation == null ? AuthEnum.APP_ALL : authAnnotation.value();

        //声明NO_LOGIN接口直接放行
        if(AuthEnum.NO_LOGIN.equals(methodAuth)){
            if(jwtEnable) {
                String jwt = visitor.getJwtToken();
                if(StringUtils.isNotBlank(jwt)) {
                    byte[] keyBytes = Base64.getDecoder().decode(jwtkey);
                    MyJwt myJwt = JwtHelper.parseMyJwt(jwt, keyBytes);
                    if (myJwt != null) {
                        visitor.setCellphone(myJwt.getCellphone());
                        request.setAttribute(GlobalVar.CELLPHONE, myJwt.getCellphone());
                    }
                }
            }
            return true;
        }

        //JWT 用户身份认证
        tokenHandler.authentication(request, response);
        return true;
    }

    /**
     * 访问API需要的权限，默认为需要登陆
     * @param m
     * @return
     */
    private Auth getMethodAuth(Method m){
        Auth authAnnotation = m.getAnnotation(Auth.class);
        if(authAnnotation == null){
            authAnnotation =  m.getDeclaringClass().getAnnotation(Auth.class);
        }
        return authAnnotation == null ? null : authAnnotation;
    }

}
