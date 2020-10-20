package com.peilian.dataplatform.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户线程上下文
 * @author hui.wang
 * @date 2020/4/22
 * @since 1.0.0
 */
public class UserThreadContext {

    private static final ThreadLocal<Map<String,Object>> CTX_HOLDER = new ThreadLocal<>();

    public static final String REQUEST_HEADER_KEY = "requestHeader";
    public static final String REQUEST_WORK_REQUEST_KEY = "work_request";

    public static <T> T getAttribute(String attributeName){
        Map<String,Object> ctx = CTX_HOLDER.get();
        return ctx == null ? null : (T) ctx.get(attributeName);
    }
    public static void setAttribute(String attributeName,Object value){
        Map<String,Object> ctx = CTX_HOLDER.get();
        if(ctx == null){
            ctx = new HashMap<>();
            CTX_HOLDER.set(ctx);
        }
        ctx.put(attributeName,value);
    }
    public static void removeAll(){
        CTX_HOLDER.remove();
    }

    public static void requestStart(){
        removeAll();
    }

    public static void requestEnd(){
        removeAll();
    }

    public static UserVisitor getUserVisitor(){
        return getAttribute(REQUEST_HEADER_KEY);
    }

    public static void setUserVisitor(UserVisitor visitor){
        setAttribute(REQUEST_HEADER_KEY,visitor);
    }

    /**
     * 是否内网请求
     * @return true 内网访问 false 外网访问
     */
    public static boolean isWorkRequest(){
        Boolean workRequest =  getAttribute(REQUEST_WORK_REQUEST_KEY);
        return workRequest == null ? false : workRequest;
    }

    /**
     * 获取userId
     * @return
     */
    public static String getCellphone() {
        UserVisitor userVisitor = getAttribute(REQUEST_HEADER_KEY);
        return userVisitor == null? null: userVisitor.getCellphone();
    }

 }
