package com.peilian.dataplatform.service;

import com.peilian.dataplatform.dto.DataDto;
import com.peilian.dataplatform.enums.ResultType;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * @author zhengshangchao
 */
public interface ApiService {

    /**
     * 通过apiCode查询接口数据
     * 返回类型array
     *
     * @param dataDto
     * @throws Exception
     * @return
     */
    List<JSONObject> queryList(DataDto dataDto) throws Exception;

    /**
     * 通过apiCode查询接口数据
     * 返回类型object
     *
     * @param dataDto
     * @throws Exception
     * @return
     */
    JSONObject query(DataDto dataDto) throws Exception;

    /**
     * 通过apiCode获取返回类型
     *
     * @param apiCode
     * @return
     */
    ResultType getResultType(String apiCode);

    /**
     * 通过apiCode获取接口层的表头
     *
     * @param apiCode
     * @return
     */
    List<String> getHead(String apiCode);

}
