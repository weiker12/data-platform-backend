package com.peilian.dataplatform.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典项DTO
 *
 * @author zhengshangchao
 */
@Getter
@Setter
@ToString
public class DictDto {

    /**
     * 字典项代码
     */
    private Object code;

    /**
     * 字典项名称
     */
    private String name;

}
