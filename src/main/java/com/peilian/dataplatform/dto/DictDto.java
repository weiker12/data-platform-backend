package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典项DTO
 *
 * @author zhengshangchao
 */
@Getter
@Setter
@ToString
public class DictDto {

    /**
     * 字典项代码
     */
    @ApiModelProperty(value = "字典项代码", example = "xxx")
    private Object code;

    /**
     * 字典项名称
     */
    @ApiModelProperty(value = "字典项名称", example = "xxx")
    private String name;

}
