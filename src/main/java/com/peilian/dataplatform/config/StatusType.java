package com.peilian.dataplatform.config;

/**
 * 状态类型的枚举定义
 */
public enum StatusType {

    /**
     * 1-有效
     * 0-无效
     */
    YES(1), NO(0);

    private Integer status;

    StatusType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
