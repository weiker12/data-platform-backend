package com.peilian.dataplatform.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.config.ResponseMessageCodeEnum;
import com.peilian.dataplatform.config.Result;
import com.peilian.dataplatform.dto.ApiSourceDto;
import com.peilian.dataplatform.enums.ResultType;
import com.peilian.dataplatform.service.ApiConfigService;
import com.peilian.dataplatform.service.ApiService;
import com.peilian.dataplatform.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

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

    @Autowired
    ApiConfigService apiConfigService;

    /**
     * 根据apiCode获取报表数据的接口
     *
     * @param apiCode
     * @param paramsJson
     * @return
     * @throws Exception
     */
    @PostMapping("/getData")
    public ResponseMessage getData(@RequestParam("apiCode") String apiCode, @RequestBody String paramsJson) throws Exception {
        log.info("入参apiCode={}, paramsJson={}", apiCode, paramsJson);
        ResultType resultType = apiService.getResultType(apiCode);
        if (ResultType.ARRAY.equals(resultType)) {
            return Result.success(apiService.queryList(apiCode, paramsJson));
        } else if (ResultType.OBJECT.equals(resultType)) {
            return Result.success(apiService.query(apiCode, paramsJson));
        } else {
            return Result.error(ResponseMessageCodeEnum.INTERNAL_SERVER_ERROR.getCode(), "api_source表的返回类型配置不正确，应为object/array");
        }
    }

    /**
     * 根据apiCode获取报表数据的excel
     *
     * @param apiCode
     * @param paramsJson
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/getExcel")
    public void getExcel(@RequestParam("apiCode") String apiCode, @RequestBody String paramsJson, HttpServletResponse response) throws Exception {
        log.info("入参apiCode={}, paramsJson={}", apiCode, paramsJson);
        ApiSourceDto sourceDto =  apiConfigService.getApiInfo(apiCode);
        String fileName = ExcelUtil.getFileName(sourceDto.getApiName());
        List<String> headLine = apiService.getHead(apiCode);
        List<JSONObject> jsonDataList = apiService.queryList(apiCode, paramsJson);
        List<List<Object>> contents = ExcelUtil.getBody(jsonDataList, headLine);
        List<List<String>> heads = ExcelUtil.getHead(headLine);
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        EasyExcel.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX).autoCloseStream(false).sheet(sourceDto.getApiName())
                .autoTrim(true).registerWriteHandler(ExcelUtil.getCustomCellWriteHandler()).head(heads).doWrite(contents);
    }

}
