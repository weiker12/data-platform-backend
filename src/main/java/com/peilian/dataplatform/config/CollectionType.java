package com.peilian.dataplatform.config;

/**
 * 集合类型的枚举定义
 */
public enum CollectionType {

    /**
     * 1-有效
     * 0-无效
     */
    ALL("all"), SUB("sub");

    private String type;

    CollectionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
