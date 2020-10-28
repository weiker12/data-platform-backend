package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据源配置表(ApiInfoDto)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class SourceDto implements Serializable {

    /**
     * 数据源id
     */
    @ApiModelProperty(value = "数据源id", example = "1")
    private Long id;

}