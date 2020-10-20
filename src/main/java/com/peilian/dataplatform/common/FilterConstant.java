package com.peilian.dataplatform.common;

import lombok.extern.log4j.Log4j2;

import static com.peilian.dataplatform.common.AppRequestConstants.HEADER_JWTTOKEN;
import static com.peilian.dataplatform.common.AppRequestConstants.LOGID;


/**
 * @author zhengshangchao
 */
@Log4j2
public class FilterConstant {

    /**
     * 检测是否为特定API接口
     *
     * @param url
     * @return
     */
    public static boolean isSpecialApi(String url) {
        String[] innerUrl = {"/druid", "/ops"};
        for (String s : innerUrl) {
            if (url.startsWith(GlobalValue.rootPath + s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是SWgger特定Url
     *
     * @param url
     * @return
     */
    public static boolean isSwaggerUrl(String url) {
        return url.contains("/swagger-resources") || url.contains("/webjars/")
                || url.startsWith(GlobalValue.rootPath + "/swagger-ui.html")
                || url.startsWith(GlobalValue.rootPath + "/v2/api-docs")
                || url.startsWith(GlobalValue.rootPath + "/csrf");
    }


    public static String headers() {
        return String.format("%s,%s", HEADER_JWTTOKEN, LOGID);
    }

}
