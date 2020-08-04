package com.peilian.dataplatform.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 数据源配置表(DataSource)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class DataSourceDto implements Serializable {

    private static final long serialVersionUID = -83465630700065383L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 数据源代码
     */
    @Size(min = 0, max = 128, message = "dsCode长度应在128以内")
    private String dsCode;
    /**
     * 连接用户名
     */
    @Size(min = 0, max = 64, message = "username长度应在64以内")
    private String username;
    /**
     * 连接密码
     */
    @Size(min = 0, max = 256, message = "password长度应在256以内")
    private String password;
    /**
     * 0-无效，1-有效 默认1
     */
    @Range(min = 0, max = 1, message = "status枚举类型的值必须为0或1")
    private Integer status;
    /**
     * 数据库连接地址
     */
    @Size(min = 0, max = 512, message = "url长度应在512以内")
    private String url;

}