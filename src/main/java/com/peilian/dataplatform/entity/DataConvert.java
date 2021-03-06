package com.peilian.dataplatform.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 进行接口字段特殊处理的实体类
 *
 * @author zhengshangchao
 */
@Table(name = "data_convert")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
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
    @Size(min = 0, max = 128, message = "apiFieldName长度应在128以内")
    private String apiFieldName;

    /**
     * bsh转换脚本
     */
    @Column(name = "convert_script")
    @NotBlank(message = "convert_script配置不能为空")
    private String convertScript;

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
