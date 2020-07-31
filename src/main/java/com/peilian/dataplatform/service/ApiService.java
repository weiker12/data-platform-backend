package com.peilian.dataplatform.service;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.config.ResultType;
import net.sf.json.JSONObject;

import java.util.List;

public interface ApiService {

    /**
     * 通过apiCode查询接口数据
     * 返回类型array
     *
     * @param apiCode
     * @param paramsJson
     * @return
     */
    List<JSONObject> queryList(String apiCode, String paramsJson) throws Exception;

    /**
     * 通过apiCode查询接口数据
     * 返回类型object
     *
     * @param apiCode
     * @param paramsJson
     * @return
     */
    JSONObject query(String apiCode, String paramsJson) throws Exception;

    /**
     * 通过apiCode获取返回类型
     *
     * @param apiCode
     * @return
     */
    ResultType getResultType(String apiCode);

}
