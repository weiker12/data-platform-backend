package com.peilian.dataplatform.entity;

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

    private String title;

    private String content;

    private Integer contentType;

    private String to;

    private String msName;

    private Integer type;

    private String keyword;

}
