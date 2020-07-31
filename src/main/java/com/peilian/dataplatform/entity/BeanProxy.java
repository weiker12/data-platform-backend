package com.peilian.dataplatform.entity;

import net.sf.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;


public class BeanProxy {

    /**
     * 存储数据表元信息的容器
     */
    LinkedHashMap<String, Object> container = new LinkedHashMap<>();

    public void setValue(String key, Object value) {
        container.put(key, value);
    }


    /**
     * 获取BeanProxy的值
     * 适合select * from test形式的sql
     *
     * @return
     */
    public JSONObject getData() {
        JSONObject jsonObject = new JSONObject();
        Set<String> fields = container.keySet();
        for(String field : fields) {
            Object value = container.get(field);
            jsonObject.put(field, value);
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeanProxy beanProxy = (BeanProxy) o;
        return Objects.equals(container, beanProxy.container);
    }

    @Override
    public int hashCode() {
        return Objects.hash(container);
    }

}
