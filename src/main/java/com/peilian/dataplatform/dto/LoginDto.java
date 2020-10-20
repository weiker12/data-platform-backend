package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录dto
 */
@Data
public class LoginDto {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", example = "xxx")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3456789]\\d{9}", message = "请输入正确的手机号")
    private String cellphone;

    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码", example = "xxx")
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "\\d{6}", message = "请输入正确的验证码")
    private String smsCode;

}
