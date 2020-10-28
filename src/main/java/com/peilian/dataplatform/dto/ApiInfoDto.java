package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * 数据源配置表(ApiInfoDto)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class ApiInfoDto implements Serializable {

    /**
     * 接口代码
     */
    @ApiModelProperty(value = "接口代码", example = "1")
    private String apiCode;

}