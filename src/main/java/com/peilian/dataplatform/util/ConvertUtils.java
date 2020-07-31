package com.peilian.dataplatform.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scripting.bsh.BshScriptUtils;
import org.springframework.util.StringUtils;

/**
 * spring bsh脚本式处理特殊字段逻辑
 *
 * @author zhengshangchao
 */
@Slf4j
public class ConvertUtils {

    /**
     * 根据特殊处理的convert脚本获取Convert处理器
     *
     * @param convertScript
     * @return
     */
    public static Convert convert(String convertScript) {
        log.info("入参convertScript={}", convertScript);
        if(StringUtils.isEmpty(convertScript)) {
            return null;
        }
        Convert convert = null;
        try {
            convert = (Convert) BshScriptUtils.createBshObject(convertScript, new Class[]{Convert.class});
        } catch (Exception e) {
            log.error("创建[{}]的转换器对象异常: {}", convertScript, e.getMessage());
        }
        return convert;
    }

}
