package com.peilian.dataplatform.common;

import com.peilian.dataplatform.config.ResponseMessageCode;

import java.lang.annotation.*;

/**
 * 接口访问权限
 *
 * @author hui.wang
 * @date 2020/5/9
 * @since 1.0.0
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    AuthEnum value() default AuthEnum.APP_ALL;

    ResponseMessageCode errorCode() default ResponseMessageCode.UNAUTHORIZED;

}
