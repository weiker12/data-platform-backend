package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * sql信息实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class SqlInfoDto implements Serializable {

    /**
     * 接口代码
     */
    @ApiModelProperty(value = "sql语句", example = "select 1 from xxx;")
    private String sql;

}