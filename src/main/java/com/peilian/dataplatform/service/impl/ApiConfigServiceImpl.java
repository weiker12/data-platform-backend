package com.peilian.dataplatform.service.impl;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.dto.ApiInfoListDto;
import com.peilian.dataplatform.dto.ApiSourceDto;
import com.peilian.dataplatform.dto.DataSourceDto;
import com.peilian.dataplatform.dto.DataSourceListDto;
import com.peilian.dataplatform.entity.ApiSource;
import com.peilian.dataplatform.entity.DataConvert;
import com.peilian.dataplatform.entity.DataFlow;
import com.peilian.dataplatform.entity.DataSource;
import com.peilian.dataplatform.repository.ApiSourceRepository;
import com.peilian.dataplatform.repository.DataConvertRepository;
import com.peilian.dataplatform.repository.DataFlowRepository;
import com.peilian.dataplatform.repository.DataSourceRepository;
import com.peilian.dataplatform.service.ApiConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * 数据报表api接口信息配置接口实现类
 *
 * @author zhengshangchao
 */
@Slf4j
@Service("apiConfigService")
public class ApiConfigServiceImpl implements ApiConfigService {

    @Autowired
    DataSourceRepository dataSourceRepository;

    @Autowired
    ApiSourceRepository apiSourceRepository;

    @Autowired
    DataFlowRepository dataFlowRepository;

    @Autowired
    DataConvertRepository dataConvertRepository;

    private final static String ASTERISK = "*";

    /**
     * 根据查询条件apiCode和apiName返回接口信息
     * 其中dsCode是模糊查询
     *
     * @param dataSourceListDto
     * @return
     */
    @Override
    public Page<DataSource> getDataSourceList(DataSourceListDto dataSourceListDto) {
        log.info("dataSourceListDto={}", dataSourceListDto);
        Specification<DataSource> specification = (Specification<DataSource>) (root, query, cb) -> {
            // 添加查询条件，apiCode为精确查询apiName为模糊查询
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(dataSourceListDto.getDsCode())) {
                predicates.add(cb.like(root.get("dsCode").as(String.class), "%" + dataSourceListDto.getDsCode() + "%"));
            }
            // 创建一个查询条件的集合，长度为满足上述两个条件的个数
            Predicate[] pre = new Predicate[predicates.size()];
            // 查询结果排序g规则设置为根据id倒序排列
            query.orderBy(cb.desc(root.get("id")));
            // 将上面拼接好的条件返回
            return query.where(predicates.toArray(pre)).getRestriction();
        };
        Page<DataSource> page = dataSourceRepository.findAll(specification, dataSourceListDto);
        List<DataSource> dataSources = page.getContent();
        // 数据库密码脱敏处理
        dataSources.stream().forEach(dataSource -> {
            dataSource.setPassword(dataSource.getPassword().replaceAll("\\w", ASTERISK));
        });
        log.info("查询结果返回dataSourceList={}", page);
        return page;
    }

    /**
     * 获取数据源列表
     *
     * @return
     */
    @Override
    public List<DataSource> getDataSourceList() {
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        return dataSourceList;
    }

    /**
     * 根据查询条件apiCode和apiName返回接口信息
     * 其中apiCode是精确查询，apiName是模糊查询
     *
     * @param apiInfoListDto
     * @return
     */
    @Override
    public Page<ApiSource> getApiInfoList(ApiInfoListDto apiInfoListDto) {
        log.info("入参apiInfoListDto={}", apiInfoListDto);
        Specification<ApiSource> specification = (Specification<ApiSource>) (root, query, cb) -> {
            // 添加查询条件，apiCode为精确查询apiName为模糊查询
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(apiInfoListDto.getApiCode())) {
                predicates.add(cb.equal(root.get("apiCode").as(String.class), apiInfoListDto.getApiCode()));
            }
            if (StringUtils.isNotBlank(apiInfoListDto.getApiName())) {
                predicates.add(cb.like(root.get("apiName").as(String.class), "%" + apiInfoListDto.getApiName() + "%"));
            }
            // 创建一个查询条件的集合，长度为满足上述两个条件的个数
            Predicate[] pre = new Predicate[predicates.size()];
            // 查询结果排序g规则设置为根据id倒序排列
            query.orderBy(cb.desc(root.get("id")));
            // 将上面拼接好的条件返回
            return query.where(predicates.toArray(pre)).getRestriction();
        };
        Page<ApiSource> page = apiSourceRepository.findAll(specification, apiInfoListDto);
        log.info("查询结果返回apiSourceList={}", page);
        return page;
    }

    /**
     * 获取数据源详情
     *
     * @param id
     * @return
     */
    @Override
    public DataSource getDataSource(Long id) {
        log.info("id={}", id);
        Optional<DataSource> dataSource = dataSourceRepository.findById(id);
        DataSource dataSource1 = dataSource.get();
        // 数据库密码脱敏处理
        dataSource1.setPassword(dataSource1.getPassword().replaceAll("\\w", ASTERISK));
        return dataSource1;
    }

    /**
     * 获取接口信息详情
     *
     * @param apiCode
     * @return
     */
    @Override
    public ApiSourceDto getApiInfo(String apiCode) {
        log.info("入参apiCode={}", apiCode);
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        Assert.notNull(apiCode, String.format("%s不存在api_source配置信息", apiCode));
        List<DataFlow> dataFlowList = dataFlowRepository.findByApiCode(apiCode);
        List<DataConvert> dataConvertList = dataConvertRepository.findByApiCode(apiCode);
        dataFlowList = CollectionUtils.isEmpty(dataFlowList) ? new ArrayList<>() : dataFlowList;
        dataConvertList = CollectionUtils.isEmpty(dataConvertList) ? new ArrayList<>() : dataConvertList;
        ApiSourceDto dto = new ApiSourceDto();
        BeanUtils.copyProperties(apiSource, dto);
        dto.setDataFlowList(dataFlowList);
        dto.setDataConvertList(dataConvertList);
        return dto;
    }

    /**
     * 新增或者更新数据源配置信息
     *
     * @param dataSourceDto
     * @return
     * @throws BizException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDataSource(DataSourceDto dataSourceDto) throws BizException {
        log.info("入参dataSourceDto={}", dataSourceDto);
        Long id = dataSourceDto.getId();
        String dsCode = dataSourceDto.getDsCode();
        DataSource dataSource = dataSourceRepository.findByDsCode(dsCode);
        // 校验dsCode命名是否有冲突
        if (dataSource != null && !dataSource.getId().equals(id)) {
            throw new BizException("dsCode在数据源配置中已存在！");
        }
        if (null == id) {
            // 新增数据源信息
            DataSource ds = new DataSource();
            BeanUtils.copyProperties(dataSourceDto, ds);
            dataSourceRepository.save(ds);
        } else {
            String oldPassword = dataSource.getPassword();
            String password = dataSourceDto.getPassword();
            if (StringUtils.isNotBlank(password) && password.contains(ASTERISK)) {
                password = oldPassword;
            }
            DataSource dataSource1 = dataSourceRepository.findById(id).get();
            BeanUtils.copyProperties(dataSourceDto, dataSource1);
            dataSource1.setPassword(password);
            dataSourceRepository.save(dataSource1);
        }
    }

    /**
     * 新增或者更新接口配置信息
     *
     * @param apiSourceDto
     * @return
     * @throws BizException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveApiInfo(ApiSourceDto apiSourceDto) throws BizException {
        String apiCode = apiSourceDto.getApiCode();
        Long id = apiSourceDto.getId();
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        // 校验apiCode是否重复
        if (apiSource != null && !apiSource.getId().equals(id)) {
            throw new BizException("apiCode在数据源配置中已存在");
        }
        if (null == id) {
            // 新增接口配置信息
            apiSource = new ApiSource();
            BeanUtils.copyProperties(apiSourceDto, apiSource);
            apiSourceRepository.save(apiSource);
        } else {
            ApiSource apiSource1 = apiSourceRepository.findById(id).get();
            BeanUtils.copyProperties(apiSourceDto, apiSource1);
            apiSourceRepository.save(apiSource1);
        }
        // 保存dataFlow信息
        List<DataFlow> dataFlows = apiSourceDto.getDataFlowList();
        dataFlowRepository.deleteByApiCode(apiCode);
        if (!CollectionUtils.isEmpty(dataFlows)) {
            dataFlows.stream().forEach(dataFlow -> dataFlow.setApiCode(apiCode));
            dataFlowRepository.saveAll(dataFlows);
        }
        // 保存dataConvert信息
        List<DataConvert> dataConverts = apiSourceDto.getDataConvertList();
        dataConvertRepository.deleteByApiCode(apiCode);
        if (!CollectionUtils.isEmpty(dataConverts)) {
            dataConverts.stream().forEach(dataConvert -> dataConvert.setApiCode(apiCode));
            dataConvertRepository.saveAll(dataConverts);
        }
    }

    /**
     * 根据id删除数据源配置信息
     *
     * @param id
     */
    @Override
    public void delDataSource(Long id) {
        log.info("入参id={}", id);
        Optional<DataSource> dataSourceOptional = dataSourceRepository.findById(id);
        if (dataSourceOptional.isPresent()) {
            dataSourceRepository.deleteById(id);
        }
    }

    /**
     * 根据apiCode删除api配置信息
     *
     * @param apiCode
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delApiInfo(String apiCode) {
        log.info("入参apiCode={}", apiCode);
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        if (!Objects.isNull(apiSource)) {
            apiSourceRepository.deleteByApiCode(apiCode);
        }
        List<DataFlow> dataFlows = dataFlowRepository.findByApiCode(apiCode);
        if (!CollectionUtils.isEmpty(dataFlows)) {
            dataFlowRepository.deleteByApiCode(apiCode);
        }
        List<DataConvert> dataConverts = dataConvertRepository.findByApiCode(apiCode);
        if (!CollectionUtils.isEmpty(dataConverts)) {
            dataConvertRepository.deleteByApiCode(apiCode);
        }
    }

}
