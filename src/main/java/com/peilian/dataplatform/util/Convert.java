package com.peilian.dataplatform.util;

/**
 * 动态代码逻辑处理接口
 *
 * @author zhengshangchao
 */
public interface Convert<T> {

    /**
     * str为要做特殊处理的字符串
     * Convert的实现类将会为str提供转换逻辑
     *
     * @param str
     * @return
     */
    T convert(T str);

}
