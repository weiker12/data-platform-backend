package com.peilian.dataplatform.controller;

import com.peilian.dataplatform.common.Auth;
import com.peilian.dataplatform.common.AuthEnum;
import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.config.Result;
import com.peilian.dataplatform.dto.*;
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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/v1")
public class ApiConfigController {

    @Autowired
    ApiConfigService apiConfigService;

    @Autowired
    CommonValidator commonValidator;

    /**
     * 获取数据源列表(支持分页查询)
     * 查询条件dsCode为模糊查询
     *
     * @param dataSourceListDto
     * @return
     */
    @ApiOperation(value = "获取数据源列表(支持分页查询)")
    @PostMapping("/getDataSourceList")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage getDataSourceList(@RequestBody DataSourceListDto dataSourceListDto) {
        log.info("入参dataSourceListDto={}", dataSourceListDto);
        Page<DataSource> apiSourcePage = apiConfigService.getDataSourceList(dataSourceListDto);
        return Result.success(apiSourcePage);
    }

    /**
     * 获取接口配置信息列表数据
     *
     * @param apiInfoListDto
     * @return=
     */
    @ApiOperation(value = "获取接口配置信息列表数据")
    @PostMapping("/getApiInfoList")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage getApiInfoList(@RequestBody ApiInfoListDto apiInfoListDto) {
        log.info("入参apiInfoListDto={}", apiInfoListDto);
        Page<ApiSource> apiSourcePage = apiConfigService.getApiInfoList(apiInfoListDto);
        return Result.success(apiSourcePage);
    }

    /**
     * 获取数据源详情信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取数据源详情信息")
    @GetMapping("/getDataSource")
    @Auth(value = AuthEnum.APP_ALL)
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
    @ApiOperation(value = "获取数据报表的详情信息")
    @GetMapping("/getApiInfo")
    @Auth(value = AuthEnum.APP_ALL)
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
    @ApiOperation(value = "新增或者更新数据源信息")
    @PostMapping("/saveDataSource")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage saveDataSource(@RequestBody DataSourceDto dataSourceDto) throws BizException {
        log.info("入参dataSourceDto={}", dataSourceDto);
        commonValidator.checkDto(dataSourceDto);
        apiConfigService.saveDataSource(dataSourceDto);
        return Result.success();
    }

    /**
     * 新增或者更新数据报表接口配置信息
     *
     * @param apiSourceDto
     * @return
     */
    @ApiOperation(value = "新增或者更新数据报表接口配置信息")
    @PostMapping("/saveApiInfo")
    @Auth(value = AuthEnum.APP_ALL)
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
     * @param sourceDto
     * @return
     */
    @ApiOperation(value = "删除数据源配置信息")
    @PostMapping("/delDataSource")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage delDataSource(@RequestBody SourceDto sourceDto) {
        log.info("入参sourceDto={}", sourceDto);
        apiConfigService.delDataSource(sourceDto.getId());
        return Result.success();
    }

    /**
     * 删除数据报表信息
     *
     * @param apiInfoDto
     * @return
     */
    @ApiOperation(value = "删除数据报表信息")
    @PostMapping("/delApiInfo")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage delApiInfo(@RequestBody ApiInfoDto apiInfoDto) {
        log.info("入参apiCode={}", apiInfoDto.getApiCode());
        apiConfigService.delApiInfo(apiInfoDto.getApiCode());
        return Result.success();
    }


    /**
     * 获取该接口配置清单的字典项
     *
     * @param apiCode
     * @return
     */
    @ApiOperation(value = "获取该接口配置清单的字典项")
    @GetMapping("/getDict")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage getDict(@RequestParam(value = "apiCode", required = false) String apiCode) throws Exception {
        log.info("入参sql={}", apiCode);
        Map<String, Object> dictMap = new HashMap<>(16);
        dictMap.put("status", StatusType.toDict());
        dictMap.put("send", SendType.toDict());
        dictMap.put("resultType", ResultType.toDict());
        dictMap.put("collectType", CollectionType.toDict());
        // 数据源代码字典项
        List<DataSource> dataSourceList = apiConfigService.getDataSourceList();
        if (!CollectionUtils.isEmpty(dataSourceList)) {
            List<String> dsCodeList = dataSourceList.stream().map(DataSource::getDsCode).collect(Collectors.toList());
            dictMap.put("dsCodes", dsCodeList);
        }
        if(StringUtils.isEmpty(apiCode)) {
            return Result.success(dictMap);
        }
        ApiSourceDto dto = apiConfigService.getApiInfo(apiCode);
        // api字段值字典项
        List<String> fields = new ArrayList<>();
        // api查询参数字典项
        List<String> params = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getQuerySql())) {
            // 从配置的sql中读取sql字段值
            fields.addAll(SqlUtils.getFields(dto.getQuerySql()));
            params.addAll(SqlUtils.getParamFields(dto.getQuerySql()));
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
                Set<String> list = SqlUtils.getParamFields(sql);
                return list;
            }).flatMap(Collection::stream).collect(Collectors.toList());
        }
        dictMap.put("apiFields", fields);
        dictMap.put("apiParams", params);
        return Result.success(dictMap);
    }

    /**
     * 获取该接口配置清单的字典项
     *
     * @param sqlInfoDto
     * @return
     */
    @ApiOperation(value = "获取该接口配置清单的字典项")
    @PostMapping("/getSqlInfo")
    @Auth(value = AuthEnum.APP_ALL)
    public ResponseMessage getSqlInfo(@RequestBody SqlInfoDto sqlInfoDto) throws Exception {
        log.info("入参sqlInfoDto={}", sqlInfoDto);
        String sql = sqlInfoDto.getSql();
        Map<String, Object> dictMap = new HashMap<>(16);
        // api字段值字典项
        List<String> fields = new ArrayList<>();
        // api查询参数字典项
        List<String> params = new ArrayList<>();
        if (StringUtils.isNotBlank(sql)) {
            // 从配置的sql中读取sql字段值
            fields.addAll(SqlUtils.getFields(sql));
            params.addAll(SqlUtils.getParamFields(sql));
        }
        dictMap.put("apiFields", fields);
        dictMap.put("apiParams", params);
        return Result.success(dictMap);
    }

}
