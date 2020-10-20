package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * 数据源配置表(DataSource)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class DataDto implements Serializable {

    /**
     * 接口代码
     */
    @ApiModelProperty(value = "接口代码", example = "1")
    private String apiCode;

    /**
     * 参数json
     */
    @ApiModelProperty(value = "参数json", example = "1")
    private JSONObject paramsJson;

}