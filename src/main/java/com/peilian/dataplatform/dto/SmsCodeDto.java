package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 获取验证码dto
 */
@Data
public class SmsCodeDto {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", example = "xxx")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3456789]\\d{9}", message = "请输入正确的手机号")
    private String cellphone;

}
