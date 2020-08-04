package com.peilian.dataplatform.controller;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.config.Result;
import com.peilian.dataplatform.dto.ApiSourceDto;
import com.peilian.dataplatform.dto.DataSourceDto;
import com.peilian.dataplatform.entity.ApiSource;
import com.peilian.dataplatform.entity.DataFlow;
import com.peilian.dataplatform.entity.DataSource;
import com.peilian.dataplatform.enums.CollectionType;
import com.peilian.dataplatform.enums.ResultType;
import com.peilian.dataplatform.enums.SendType;
import com.peilian.dataplatform.enums.StatusType;
import com.peilian.dataplatform.service.ApiConfigService;
import com.peilian.dataplatform.util.CommonValidator;
import com.peilian.dataplatform.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据报表平台接口
 *
 * @author zhengshangchao
 */
@Slf4j
@RestController
@RequestMapping("/v1/data-paltform")
public class ApiConfigController {

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    CommonValidator commonValidator;

    /**
     * 获取数据源列表(支持分页查询)
     * 查询条件dsCode为模糊查询
     *
     * @param dsCode
     * @return
     */
    @GetMapping("/getDataSourceList")
    public ResponseMessage getDataSourceList(@RequestParam(value = "dsCode", required = false) String dsCode,
                                             @PageableDefault Pageable pageable) {
        log.info("入参dsCode={}, pageable={}", dsCode, pageable);
        Page<DataSource> apiSourcePage = apiConfigService.getDataSourceList(dsCode, pageable);
        return Result.success(apiSourcePage);
    }

    /**
     * 获取数据报表接口列表数据
     *
     * @param apiCode
     * @return=
     */
    @GetMapping("/getApiInfoList")
    public ResponseMessage getApiInfoList(@RequestParam(value = "apiCode", required = false) String apiCode,
                                          @RequestParam(value = "apiName", required = false) String apiName,
                                          @PageableDefault Pageable pageable) {
        log.info("入参apiCode={}, apiName={}, pageable={}", apiCode, apiName, pageable);
        Page<ApiSource> apiSourcePage = apiConfigService.getApiInfoList(apiCode, apiName, pageable);
        return Result.success(apiSourcePage);
    }

    /**
     * 获取数据源详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getDataSource")
    public ResponseMessage getDataSource(@RequestParam("id") Long id) {
        log.info("入参id={}", id);
        DataSource dataSource = apiConfigService.getDataSource(id);
        return Result.success(dataSource);
    }

    /**
     * 获取数据报表的详情信息
     *
     * @param apiCode
     * @return
     */
    @GetMapping("/getApiInfo")
    public ResponseMessage getApiInfo(@RequestParam("apiCode") String apiCode) throws Exception {
        log.info("入参apiCode={}", apiCode);
        ApiSourceDto apiSourceDto = apiConfigService.getApiInfo(apiCode);
        return Result.success(apiSourceDto);
    }

    /**
     * 新增或者更新数据源信息
     *
     * @param dataSourceDto
     * @return
     */
    @PostMapping("/saveDataSource")
    public ResponseMessage saveDataSource(@RequestBody DataSourceDto dataSourceDto) throws BizException {
        log.info("入参dataSourceDto={}", dataSourceDto);
        commonValidator.checkDto(dataSourceDto);
        apiConfigService.saveDataSource(dataSourceDto);
        return Result.success();
    }

    /**
     * 新增或者更新数据报表接口信息
     *
     * @param apiSourceDto
     * @return
     */
    @PostMapping("/saveApiInfo")
    public ResponseMessage saveApiInfo(@RequestBody ApiSourceDto apiSourceDto) throws BizException {
        log.info("入参apiCode={}, paramsJson={}", apiSourceDto);
        commonValidator.checkDto(apiSourceDto);
        if (StringUtils.isNotBlank(apiSourceDto.getQuerySql())) {
            // 校验query_sql
            SqlUtils.checkSql(apiSourceDto.getQuerySql());
        } else {
            // 校验分片sql
            List<DataFlow> dataFlows = apiSourceDto.getDataFlowList();
            Assert.notEmpty(dataFlows, "分片sql未配置！");
            dataFlows.stream().forEach(dataFlow -> SqlUtils.checkSql(dataFlow.getQuerySql()));
        }
        apiConfigService.saveApiInfo(apiSourceDto);
        return Result.success();
    }

    /**
     * 删除数据源配置信息
     *
     * @param id
     * @return
     */
    @PostMapping("/delDataSource")
    public ResponseMessage delDataSource(@RequestParam("id") Long id) {
        log.info("入参id={}", id);
        apiConfigService.delDataSource(id);
        return Result.success();
    }

    /**
     * 删除数据报表信息
     *
     * @param apiCode
     * @return
     */
    @PostMapping("/delApiInfo")
    public ResponseMessage delApiInfo(@RequestParam("apiCode") String apiCode) {
        log.info("入参apiCode={}, paramsJson={}", apiCode);
        apiConfigService.delApiInfo(apiCode);
        return Result.success();
    }

    /**
     * 获取该接口配置清单的字典项
     *
     * @param apiCode
     * @return
     */
    @GetMapping("/getDict")
    public ResponseMessage getDict(@RequestParam(value = "apiCode") String apiCode) throws Exception {
        log.info("入参dsCode={}", apiCode);
        Map<String, Object> dictMap = new HashMap<>(16);
        dictMap.put("status", StatusType.toDict());
        dictMap.put("send", SendType.toDict());
        dictMap.put("result_type", ResultType.toDict());
        dictMap.put("collect_type", CollectionType.toDict());
        ApiSourceDto dto = apiConfigService.getApiInfo(apiCode);
        // api字段值字典项
        List<String> fields = new ArrayList<>();
        // api查询参数字典项
        List<String> params = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getQuerySql())) {
            // 从配置的sql中读取sql字段值
            fields.addAll(SqlUtils.getFields(dto.getQuerySql()));
            params.addAll(SqlUtils.getParamField(dto.getQuerySql()));
        } else {
            List<DataFlow> dataFlows = dto.getDataFlowList();
            // 从分片sql中读取字段值
            fields = dataFlows.stream().map(flow -> {
                String sql = flow.getQuerySql();
                List<String> list = SqlUtils.getFields(sql);
                return list;
            }).flatMap(Collection::stream).distinct().collect(Collectors.toList());
            params = dataFlows.stream().map(flow -> {
                String sql = flow.getQuerySql();
                Set<String> list = SqlUtils.getParamField(sql);
                return list;
            }).flatMap(Collection::stream).collect(Collectors.toList());
        }
        dictMap.put("api_fields", fields);
        dictMap.put("api_params", params);
        // 数据源代码字典项
        List<DataSource> dataSourceList = apiConfigService.getDataSourceList();
        if(!CollectionUtils.isEmpty(dataSourceList)) {
            List<String> dsCodeList = dataSourceList.stream().map(DataSource::getDsCode).collect(Collectors.toList());
            dictMap.put("dsCodes", dsCodeList);
        }
        return Result.success(dictMap);
    }

}
