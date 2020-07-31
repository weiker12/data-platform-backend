package com.peilian.dataplatform.controller;

import com.peilian.dataplatform.config.*;
import com.peilian.dataplatform.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据报表平台接口
 *
 * @author zhengshangchao
 */
@Slf4j
@RestController
@RequestMapping("/v1/data-paltform")
public class ApiController {

    @Autowired
    ApiService apiService;

    @PostMapping("/getData")
    public ResponseMessage getData(@RequestParam("apiCode") String apiCode, @RequestBody String paramsJson) throws Exception {
        log.info("入参apiCode={}, paramsJson={}", apiCode, paramsJson);
        ResultType resultType = apiService.getResultType(apiCode);
        if(ResultType.array.equals(resultType)) {
            return Result.success(apiService.queryList(apiCode, paramsJson));
        } else if (ResultType.object.equals(resultType)) {
            return Result.success(apiService.query(apiCode, paramsJson));
        } else {
            return Result.error(ResponseMessageCodeEnum.INTERNAL_SERVER_ERROR.getCode(), "api_source表的返回类型配置不正确，应为object/array");
        }
    }

}
