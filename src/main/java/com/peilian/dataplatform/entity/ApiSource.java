package com.peilian.dataplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口数据源表(ApiSource)实体类
 *
 * @author makejava
 * @since 2020-07-27 17:24:27
 */
@Data
@Table(name = "api_source")
@Entity
@EntityListeners(AuditingEntityListener.class)
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
     * 是否发送 1-发送 0-不发送
     */
    @Column(name = "to_send")
    private Integer toSend;
    /**
     * 最近创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最近更新时间
     */
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;

}