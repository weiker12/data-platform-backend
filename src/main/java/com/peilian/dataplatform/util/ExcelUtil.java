package com.peilian.dataplatform.util;

import net.sf.json.JSONObject;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * excel处理工具
 *
 * @author zhengshangchao
 */
public class ExcelUtil {

    /**
     * 根据apiName拼接excel文件名
     *
     * @param apiName
     * @return
     */
    public static String getFileName(String apiName) {
        Assert.hasText(apiName, "apiName不能为空");
        return String.format("%s_%s", apiName, LocalDate.now(ZoneOffset.of("+8")));
    }

    /**
     * 获取excel表头的原理同获取sql字段的别名
     *
     * @param headLine
     * @return
     */
    public static List<List<String>> getHead(List<String> headLine) {
        List<List<String>> list = new ArrayList<>();
        for (String line : headLine) {
            List<String> head = new ArrayList<>();
            head.add(line);
            list.add(head);
        }
        return list;
    }

    /**
     * 根据表头顺序取出对应的内容对象
     *
     * @param contents
     * @param heads
     * @return
     */
    public static List<List<Object>> getBody(List<JSONObject> contents, List<String> heads) {
        Assert.notEmpty(heads, "heads不能为空！");
        List<List<Object>> result = new LinkedList<>();
        for (JSONObject jsonObject : contents) {
            List<Object> row = new LinkedList<>();
            for (String head : heads) {
                row.add(jsonObject.get(head));
            }
            result.add(row);
        }
        return result;
    }


    /**
     * 设置单元格内容的样式
     *
     * @return
     */
    public static CustomCellWriteHandler getCustomCellWriteHandler() {
        return new CustomCellWriteHandler();
    }

}
