package com.peilian.dataplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分步SQL任务表(DataFlow)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-29 19:30:21
 */
@Data
@Entity
@Table(name = "data_flow")
@EntityListeners(AuditingEntityListener.class)
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
    @NotBlank(message = "querySql不能为空")
    private String querySql;
    /**
     * sql执行结果集的集合类型
     */
    @Column(name = "collection_type")
    @NotBlank(message = "collectionType不能为空")
    @Pattern(regexp = "all|sub", message = "collectionType必须为all或者sub类型")
    private String collectionType;
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