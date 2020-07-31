package com.peilian.dataplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分步SQL任务表(DataFlow)实体类
 *
 * @author makejava
 * @since 2020-07-29 19:30:21
 */
@Data
@Entity
@Table(name = "data_flow")
public class DataFlow implements Serializable {

    private static final long serialVersionUID = 260503744323496426L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * apiCode接口代码
     */
    @Column(name = "api_code")
    private String apiCode;
    /**
     * 执行sql
     */
    @Column(name = "query_sql")
    private String querySql;
    /**
     * sql执行结果集的集合类型
     */
    @Column(name = "collection_type")
    private String collectionType;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

}