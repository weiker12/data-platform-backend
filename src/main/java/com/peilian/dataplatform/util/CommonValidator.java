package com.peilian.dataplatform.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @description: JSR303校验
 *
 * @author: zhengshangchao
 * @date: 2020-02-12 09:17
 **/
@Slf4j
@Component
public class CommonValidator<T> {

    public static final String VALIDATION_SUCCESS = "";
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    /**
     * 抛出校验异常
     *
     * @param vo
     */
    public void checkDto(T vo) {
        String validateResult = check(vo);
        if (!CommonValidator.VALIDATION_SUCCESS.equals(validateResult)) {
            log.error("校验失败：" + validateResult);
            throw new IllegalArgumentException("校验失败：" + validateResult);
        }
    }

    /**
     * 校验前端传来的数据对象
     *
     * @param vo
     * @return String
     * @author zhengshangchao
     * @date 2020年2月12日
     */
    private String check(T vo) {
        if (null == vo) {
            return "对象为空";
        }
        List<String> result = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(vo);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> va : violations) {
                result.add(va.getInvalidValue() + ":" + va.getMessage());
            }
            return result.toString();
        }
        return VALIDATION_SUCCESS;
    }

}
