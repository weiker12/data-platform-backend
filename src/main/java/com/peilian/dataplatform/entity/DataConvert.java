package com.peilian.dataplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "data_convert")
@Entity
@Data
public class DataConvert {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * api_code关联api_source
     */
    @Column(name = "api_code")
    private String apiCode;

    /**
     * 字段名称
     */
    @Column(name = "api_field_name")
    private String apiFieldName;

    /**
     * bsh转换脚本
     */
    @Column(name = "convert_script")
    private String convertScript;

    /**
     * 状态 0-无效 1-有效 默认1
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 最近创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 最近更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}
