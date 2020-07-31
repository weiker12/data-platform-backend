package com.peilian.dataplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 接口数据源表(ApiSource)实体类
 *
 * @author makejava
 * @since 2020-07-27 17:24:27
 */
@Data
@Table(name = "api_source")
@Entity
public class ApiSource implements Serializable {

    private static final long serialVersionUID = 641172262709160535L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 数据库代码
     */
    @Column(name = "ds_code")
    private String dsCode;
    /**
     * http接口代码
     */
    @Column(name = "api_code")
    private String apiCode;
    /**
     * 接口名称
     */
    @Column(name = "api_name")
    private String apiName;
    /**
     * 0-无效 1-有效 默认1
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 返回类型
     */
    @Column(name = "result_type")
    private String resultType;
    /**
     * 查询sql
     */
    @Column(name = "query_sql")
    private String querySql;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

}