package com.peilian.dataplatform.enums;

import com.peilian.dataplatform.dto.DictDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 是否发送通知的枚举定义
 *
 * @author zhengshangchao
 */
public enum SendType {


    /**
     * 1-发送
     */
    YES(1, "发送"),

    /**
     * 0-不发送
     */
    NO(0, "不发送");

    private final Integer code;
    private final String name;

    SendType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据code获取枚举类型
     *
     * @param code
     * @return
     */
    public static SendType getByCode(String code) {
        SendType[] types = SendType.values();
        for(SendType type : types) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据name获取枚举类型
     *
     * @param code
     * @return
     */
    public static SendType getByName(String code) {
        SendType[] types = SendType.values();
        for(SendType type : types) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 返回枚举的字典项值
     *
     * @return List<DictDto>
     */
    public static List<DictDto> toDict() {
        List<DictDto> dicts = new ArrayList<>();
        for(SendType type : SendType.values()) {
            DictDto dto = new DictDto();
            dto.setCode(type.getCode());
            dto.setName(type.getName());
            dicts.add(dto);
        }
        return dicts;
    }

}
