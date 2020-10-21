package com.peilian.dataplatform.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql工具
 *
 * @author zhengshangchao
 */
@Slf4j
public class SqlUtils {

    /**
     * sql参数 #{param} 的正则表达式
     */
    private static final String SQL_PARAM_REGEX = "#\\{\\w*\\}";

    /**
     * 校验是否为select语句
     *
     * @param sql
     */
    public static void checkSql(String sql) {
        log.info("入参sql={}", sql);
        Assert.hasText(sql, "sql入参不能为空！");
        String formatSql = sql.trim().toLowerCase();
        Assert.isTrue((formatSql.indexOf("select") == 0) || formatSql.contentEquals("from"), "非select查询语句不合法");
        String paramStr = formatSql.substring("select".length(), formatSql.indexOf("from")).trim();
        Assert.isTrue(!paramStr.contains("*"), "sql不能用select * 必须写出具体的字段名");
    }

    /**
     * 获取数据库中配置的query_sql的#{param}的配置的变量名
     *
     * @param sql
     * @return
     */
    public static Set<String> getParamFields(String sql) {
        checkSql(sql);
        Pattern pattern = Pattern.compile(SQL_PARAM_REGEX);
        Matcher matcher = pattern.matcher(sql);
        Set<String> paramList = new HashSet<>();
        while (matcher.find()) {
            String matchStr = matcher.group();
            String param = matchStr.replace("#", "").replace("{", "").replace("}", "");
            paramList.add(param);
        }
        return paramList;
    }

    /**
     * 获取sql的字段列表
     *
     * @param sql
     * @return
     */
    public static List<String> getFields(String sql) {
        checkSql(sql);
        String formatSql = sql.trim().toLowerCase();
        String paramStr = formatSql.substring("select".length(), formatSql.indexOf("from")).trim();
        String[] array = paramStr.split(",");
        List<String> paramList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            String param = array[i].trim();
            // 提取sql结果集时按逗号来提取，但要过滤掉mysql函数产生的逗号 如select sum(x,y) z from test;最终提取的结果是z而非sum(x和z
            if(StringUtils.countMatches(param, "(") > StringUtils.countMatches(param,")")) {
                continue;
            }
            if (!param.contains(" ")) {
                paramList.add(param);
            } else {
                paramList.add(param.substring(param.lastIndexOf(" ")).trim());
            }
        }
        Assert.notEmpty(paramList, "select 字段名不能为空");
        return paramList;
    }

    /**
     * 查询sql前对sql参数进行预处理
     *
     * @param sql
     * @param params
     * @return sql
     */
    public static String preSql(String sql, Map<String, Object> params) {
        log.info("sql入参为{}, params={}", sql, params);
        Assert.hasText(sql, "入参sql不能为空！");
        checkSql(sql);
        Pattern pattern = Pattern.compile(SQL_PARAM_REGEX);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String matchStr = matcher.group();
            String param = matchStr.replace("#", "").replace("{", "").replace("}", "");
            Object value = params.get(param);
            sql = sql.replace(matchStr, "'" + value + "'");
        }
        log.info("替换参数后的sql={}", sql);
        return sql;
    }

}
