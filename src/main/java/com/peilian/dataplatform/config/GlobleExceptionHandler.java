package com.peilian.dataplatform.config;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常捕捉
 *
 * @author zhengshangchao
 */
@Component
@RestControllerAdvice
public class GlobleExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseMessage defaultExceptionHandler(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof BizException) {
            BizException bizException = (BizException) ex;
            String errorMsg = bizException.getMessage();
            return Result.error(ResponseMessageCode.INTERNAL_SERVER_ERROR.getCode(), errorMsg);
        } else {
            return Result.error(ResponseMessageCode.INTERNAL_SERVER_ERROR.getCode(), ex.toString());
        }
    }
}
