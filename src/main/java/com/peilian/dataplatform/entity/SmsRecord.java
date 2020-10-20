package com.peilian.dataplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信发送记录表(SmsRecord)实体类
 *
 * @author makejava
 * @since 2020-08-07 16:16:51
 */
@Data
@Entity
@Table(name = "sms_record")
@EntityListeners(AuditingEntityListener.class)
public class SmsRecord implements Serializable {

    private static final long serialVersionUID = -62031098801320703L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 手机号
     */
    @Column(name = "cellphone")
    private String cellphone;

    /**
     * 是否使用：0-未使用 1-已使用
     */
    @Column(name = "use_status")
    private Integer useStatus;

    /**
     * 发送的验证码
     */
    @Column(name = "code")
    private String code;

    /**
     * 验证码类型：10-注册登录
     */
    @Column(name = "sms_type")
    private Integer smsType;

    /**
     * 发送状态：0-发送失败 1-发送成功
     */
    @Column(name = "send_status")
    private Integer sendStatus;

    /**
     * 发送耗时：单位毫秒
     */
    @Column(name = "spend_time")
    private Long spendTime;

}