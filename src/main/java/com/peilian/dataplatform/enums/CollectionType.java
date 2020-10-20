package com.peilian.dataplatform.enums;

import com.peilian.dataplatform.dto.DictDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合类型的枚举定义
 *
 * @author zhengshangchao
 */
public enum CollectionType {

    /**
     * all-全集
     */
    ALL("all", "全集"),

    /**
     * sub-子集
     */
    SUB("sub", "子集");

    private final String code;
    private final String name;

    CollectionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举类型
     *
     * @param code
     * @return
     */
    public static CollectionType getByCode(String code) {
        CollectionType[] types = CollectionType.values();
        for (CollectionType type : types) {
            if (type.getCode().equals(code)) {
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
    public static CollectionType getByName(String code) {
        CollectionType[] types = CollectionType.values();
        for (CollectionType type : types) {
            if (type.getCode().equals(code)) {
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
        for (CollectionType type : CollectionType.values()) {
            DictDto dto = new DictDto();
            dto.setCode(type.getCode());
            dto.setName(type.getName());
            dicts.add(dto);
        }
        return dicts;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
