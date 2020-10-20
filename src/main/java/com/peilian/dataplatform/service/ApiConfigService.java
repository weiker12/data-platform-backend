package com.peilian.dataplatform.service;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.dto.ApiInfoListDto;
import com.peilian.dataplatform.dto.ApiSourceDto;
import com.peilian.dataplatform.dto.DataSourceDto;
import com.peilian.dataplatform.dto.DataSourceListDto;
import com.peilian.dataplatform.entity.ApiSource;
import com.peilian.dataplatform.entity.DataSource;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 数据报表api接口信息配置接口
 *
 * @author zhengshangchao
 */
public interface ApiConfigService {

    /**
     * 根据查询条件apiCode和apiName返回接口信息
     * 其中dsCode是模糊查询
     *
     * @param dataSourceListDto
     * @return Page<DataSource>
     */
    Page<DataSource> getDataSourceList(DataSourceListDto dataSourceListDto);

    /**
     * 获取数据源配置列表
     *
     * @return
     */
    List<DataSource> getDataSourceList();

    /**
     * 根据查询条件apiCode和apiName返回接口信息
     * 其中apiCode是精确查询，apiName是模糊查询
     *
     * @param apiInfoListDto
     * @return Page<ApiSource>
     */
    Page<ApiSource> getApiInfoList(ApiInfoListDto apiInfoListDto);

    /**
     * 获取数据源详情
     *
     * @param id
     * @return DataSource
     */
    DataSource getDataSource(Long id);

    /**
     * 获取接口信息详情
     *
     * @param apiCode
     * @return ApiSourceDto
     * @throws Exception
     */
    ApiSourceDto getApiInfo(String apiCode) throws Exception;

    /**
     * 新增或者更新数据源配置信息
     *
     * @param dataSourceDto
     * @throws BizException
     */
    void saveDataSource(DataSourceDto dataSourceDto) throws BizException;

    /**
     * 新增或者更新接口配置信息
     *
     * @param apiSourceDto
     * @throws BizException
     */
    void saveApiInfo(ApiSourceDto apiSourceDto) throws BizException;

    /**
     * 根据id删除数据源配置信息
     *
     * @param id
     */
    void delDataSource(Long id);

    /**
     * 根据apiCode删除api配置信息
     *
     * @param apiCode
     */
    void delApiInfo(String apiCode);

}
