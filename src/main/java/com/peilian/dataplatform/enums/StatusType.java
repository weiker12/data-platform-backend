package com.peilian.dataplatform.enums;

import com.peilian.dataplatform.dto.DictDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态类型的枚举定义
 *
 * @author zhengshangchao
 */
public enum StatusType {

    /**
     * 1-有效
     */
    YES(1, "有效"),

    /**
     * 0-无效
     */
    NO(0, "无效");

    private final Integer code;
    private final String name;

    StatusType(Integer code, String name) {
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
    public static StatusType getByCode(String code) {
        StatusType[] types = StatusType.values();
        for(StatusType type : types) {
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
    public static StatusType getByName(String code) {
        StatusType[] types = StatusType.values();
        for(StatusType type : types) {
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
        for(StatusType type : StatusType.values()) {
            DictDto dto = new DictDto();
            dto.setCode(type.getCode());
            dto.setName(type.getName());
            dicts.add(dto);
        }
        return dicts;
    }
}
