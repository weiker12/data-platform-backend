package com.peilian.dataplatform.enums;

import com.peilian.dataplatform.dto.DictDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口返回类型的枚举定义
 *
 * @author zhengshangchao
 */
public enum ResultType {

    /**
     * object-对象类型
     */
    OBJECT("object", "返回对象"),

    /**
     * array-数组类型
     */
    ARRAY("array", "返回数组");

    private final String code;
    private final String name;

    ResultType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举类型
     *
     * @param code
     * @return
     */
    public static ResultType getByCode(String code) {
        ResultType[] types = ResultType.values();
        for (ResultType type : types) {
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
    public static ResultType getByName(String code) {
        ResultType[] types = ResultType.values();
        for (ResultType type : types) {
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
        for (ResultType type : ResultType.values()) {
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
