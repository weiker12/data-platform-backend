package com.peilian.dataplatform.dto;

import com.peilian.dataplatform.entity.DataConvert;
import com.peilian.dataplatform.entity.DataFlow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 接口数据源信息
 *
 * @author zhengshangchao
 * @since 2020-07-27 17:24:27
 */
@Data
public class ApiSourceDto implements Serializable {

    private static final long serialVersionUID = 641172262709160535L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;
    /**
     * 数据库代码
     */
    @ApiModelProperty(value = "数据库代码", example = "数据库代码")
    @Size(min = 0, max = 128, message = "dsCode长度应在128以内")
    private String dsCode;
    /**
     * http接口代码
     */
    @ApiModelProperty(value = "http接口代码", example = "接口代码")
    @Size(min = 0, max = 128, message = "apiCode长度应在128以内")
    private String apiCode;
    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称", example = "接口名称")
    @Size(min = 0, max = 128, message = "apiName长度应在128以内")
    private String apiName;
    /**
     * 0-无效 1-有效 默认1
     */
    @ApiModelProperty(value = "状态", example = "0-无效 1-有效 默认1")
    @Range(min = 0, max = 1, message = "status枚举类型的值必须为0或1")
    private Integer status;
    /**
     * 返回类型
     */
    @ApiModelProperty(value = "返回类型", example = "array/object")
    @Pattern(regexp = "array|object", message = "resultType枚举类型的值必须为array或object")
    private String resultType;
    /**
     * 查询sql
     */
    @ApiModelProperty(value = "查询sql", example = "select * from test")
    private String querySql;
    /**
     * 是否发送 1-发送 0-不发送
     */
    @ApiModelProperty(value = "是否发送 1-发送 0-不发送", example = "1")
    @Range(min = 0, max = 1, message = "toSend枚举类型的值必须为0或1")
    private Integer toSend;
    /**
     * 分片sql信息
     */
    @Valid
    private List<DataFlow> dataFlowList;
    /**
     * 特殊字段处理逻辑添加
     */
    @Valid
    private List<DataConvert> dataConvertList;

}