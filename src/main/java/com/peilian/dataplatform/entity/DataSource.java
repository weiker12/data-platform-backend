package com.peilian.dataplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源配置表(DataSource)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
@Table(name = "data_source")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DataSource implements Serializable {

    private static final long serialVersionUID = -83465630700065383L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 数据源代码
     */
    @Column(name = "ds_code")
    private String dsCode;
    /**
     * 连接用户名
     */
    @Column(name = "username")
    private String username;
    /**
     * 连接密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 0-无效，1-有效 默认1
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 连接地址
     */
    @Column(name = "url")
    private String url;
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