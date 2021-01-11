package com.peilian.dataplatform.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送邮件实体类
 *
 * @author zhengshangchao
 */
@Data
public class MailSend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件头
     */
    private String title;

    /**
     * 发送邮件别名
     */
    private String fromNick;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件类型 txt或者html
     */
    private Integer contentType;

    /**
     * 发送地址
     */
    private String to;

    /**
     * 发送方的微服务名称
     */
    private String msName;

    /**
     * 邮件类型
     */
    private Integer type;

    /**
     * 邮件关键字
     */
    private String keyword;

}
