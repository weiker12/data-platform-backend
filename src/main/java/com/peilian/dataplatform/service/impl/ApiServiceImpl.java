package com.peilian.dataplatform.service.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.peilian.dataplatform.config.*;
import com.peilian.dataplatform.entity.*;
import com.peilian.dataplatform.enums.CollectionType;
import com.peilian.dataplatform.enums.ResultType;
import com.peilian.dataplatform.enums.SendType;
import com.peilian.dataplatform.enums.StatusType;
import com.peilian.dataplatform.repository.ApiSourceRepository;
import com.peilian.dataplatform.repository.DataConvertRepository;
import com.peilian.dataplatform.repository.DataFlowRepository;
import com.peilian.dataplatform.repository.DataSourceRepository;
import com.peilian.dataplatform.service.ApiService;
import com.peilian.dataplatform.util.Convert;
import com.peilian.dataplatform.util.ConvertUtils;
import com.peilian.dataplatform.util.RestClient;
import com.peilian.dataplatform.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 接口实现类
 *
 * @author zhengshangchao
 * @since 2020-07-27 10:21:41
 */
@Slf4j
@Service("apiService")
public class ApiServiceImpl implements ApiService {

    @Value("${mail.style.address}")
    private String mailStyle;

    @Value("${mail.sendTo.address}")
    private String sendTo;

    @Autowired
    private MysqlFactory mysqlFactory;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private ApiSourceRepository apiSourceRepository;

    @Autowired
    private DataConvertRepository dataConvertRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    @Autowired
    private RestClient restClient;

    /**
     * 根据接口代码和传参进行信息查询
     * 1. 检查api_source表、data_source表和data_flow表的配置信息
     * 1. 根据apiCode读取接口信息和数据源配置
     * 2. 根据接口配置提取查询sql，根据数据源配置加载数据库查询器
     * 3. 根据api_source表的query_sql是否为空决定启用数据分片功能
     * 4. 将替换参数的sql发给查询器查询
     * 5. 获取查询结果并检查字段值是否需要做特殊处理
     * 6. 如果data_convert表对该apiCode有bsh脚本配置则加载bsh脚本对字段进行特殊处理
     * 7. 通过to_send判断是否发送通知
     * 8. 将最终结果返回给接口
     *
     * @param apiCode
     * @param paramsJson
     * @return
     * @throws Exception
     */
    @Override
    public List<JSONObject> queryList(String apiCode, String paramsJson) throws Exception {
        // 检查配置信息
        checkConfig(apiCode, paramsJson);
        // 通过api_code查询接口的数据源和表数据配置信息
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        // 通过ds_code查询数据库配置信息
        DataSource dataSource = dataSourceRepository.findByDsCode(apiSource.getDsCode());
        String querySql = apiSource.getQuerySql();
        // 获取分片信息
        List<DataFlow> dataFlowList = dataFlowRepository.findByApiCode(apiCode);
        // 获取参数map
        Map<String, Object> paramMap = (Map<String, Object>) JSONObject.toBean(JSONObject.fromObject(paramsJson), Map.class);
        // 获取数据库连接器
        MysqlConnector connector = mysqlFactory.build(dataSource);
        // 如果querySql配置为空则启用分片SQL执行功能
        List<JSONObject> jsonObjects;
        if (StringUtils.isEmpty(querySql)) {
            // 定义集合的去重容器
            Set<JSONObject> allSet = new HashSet<>();
            Set<JSONObject> subSet = new HashSet<>();
            for (DataFlow dataFlow : dataFlowList) {
                String sql = dataFlow.getQuerySql();
                String flowSql = SqlUtils.preSql(sql, paramMap);
                List<BeanProxy> list = connector.query(flowSql);
                jsonObjects = list.stream().filter(Objects::nonNull).map(beanProxy -> beanProxy.getData()).collect(Collectors.toList());
                String collectionType = dataFlow.getCollectionType();
                // 根据配置的sql返回集合类型分别处理
                if (CollectionType.ALL.getCode().equals(collectionType)) {
                    allSet.addAll(jsonObjects);
                } else if (CollectionType.SUB.getCode().equals(collectionType)) {
                    // sub集合进行去重union操作
                    subSet.addAll(jsonObjects);
                } else {
                    String errorMsg = String.format("data_flow表apiCode=%s的collection_type的类型配置%s有误", apiCode, collectionType);
                    log.error(errorMsg);
                    throw new BizException(errorMsg);
                }
            }
            // 进行集合运算
            allSet.removeAll(subSet);
            jsonObjects = new ArrayList<>(allSet);
        } else {
            // 如果querySql配置不为空则不启用SQL分片查询功能
            String formatSql = SqlUtils.preSql(querySql, paramMap);
            List<BeanProxy> list = connector.query(formatSql);
            jsonObjects = list.stream().filter(Objects::nonNull).map(beanProxy -> beanProxy.getData()).collect(Collectors.toList());
        }
        // 根据apiCode查找bsh脚本的配置信息对特殊字段进行逻辑处理
        dataConvert(apiCode, jsonObjects);
        log.info("apiCode={}处理完毕，产生{}条数据", apiCode, jsonObjects.size());
        // 校验to_send开关是否打开，如果打开则发送消息
        if (SendType.YES.getCode().equals(apiSource.getToSend())) {
            sendMail(jsonObjects, apiSource.getApiName());
            sendDingTalk(jsonObjects);
        }
        return jsonObjects;
    }


    /**
     * 返回object类型
     *
     * @param apiCode
     * @param paramsJson
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject query(String apiCode, String paramsJson) throws Exception {
        List<JSONObject> resultList = queryList(apiCode, paramsJson);
        return CollectionUtils.isEmpty(resultList) ? new JSONObject() : resultList.get(0);
    }


    /**
     * 查询api处理的返回类型
     *
     * @param apiCode
     * @return
     */
    @Override
    public ResultType getResultType(String apiCode) {
        log.info("入参apiCode={}", apiCode);
        Assert.hasText(apiCode, "入参apiCode不能为空！");
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        Assert.notNull(apiSource, "apiCode的配置为空！请配置api_source表");
        return ResultType.getByCode(apiSource.getResultType());
    }

    /**
     * 获取接口字段的表头信息
     *
     * @param apiCode
     * @return
     */
    @Override
    public List<String> getHead(String apiCode) {
        // 通过api_code查询接口的数据源和表数据配置信息
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        Assert.notNull(apiSource, "接口配置信息apiSource配置不能为空！");
        String querySql = apiSource.getQuerySql();
        // 获取分片信息
        List<DataFlow> dataFlowList = dataFlowRepository.findByApiCode(apiCode);
        if(StringUtils.isEmpty(querySql)) {
            Assert.notEmpty(dataFlowList, "分片sql信息配置dataFlowList不能为空！");
        }
        // api字段值
        List<String> fields = new ArrayList<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(querySql)) {
            // 从配置的sql中读取sql字段值
            fields.addAll(SqlUtils.getFields(querySql));
        } else {
            // 从分片sql中读取字段值
            fields = dataFlowList.stream().map(flow -> {
                String sql = flow.getQuerySql();
                List<String> list = SqlUtils.getFields(sql);
                return list;
            }).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }
        return fields;
    }


    /**
     * 检查api_source表、data_source表和data_flow表的配置信息
     * data_convert表配置信息非必填故不校验
     *
     * @param apiCode
     * @param paramsJson
     */
    private void checkConfig(String apiCode, String paramsJson) {
        log.info("入参apiCode={}, params={}", apiCode, paramsJson);
        Assert.hasText(apiCode, "入参apiCode不能为空！");
        Assert.hasText(paramsJson, "入参paramsJson不能为空");

        // 校验api_source表配置信息
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        Assert.notNull(apiSource, "apiCode的配置为空！请配置api_source表");
        Assert.isTrue(StatusType.YES.getCode().equals(apiSource.getStatus()), "该接口配置已下架，请打开api_source表的配置status=1");
        // 校验data_source表配置信息
        String dsCode = apiSource.getDsCode();
        DataSource dataSource = dataSourceRepository.findByDsCode(dsCode);
        Assert.notNull(dataSource, "数据源配置不能为空！请配置data_source表");
        Assert.isTrue(StatusType.YES.getCode().equals(dataSource.getStatus()), "该接口配置已下架，请打开dataSource表的配置status=1");

        // 校验data_flow表配置信息
        List<DataFlow> dataFlowList = dataFlowRepository.findByApiCode(apiCode);
        String querySql = apiSource.getQuerySql();
        boolean querySqlConig = !StringUtils.isEmpty(querySql);
        boolean dataFlowConfig = !CollectionUtils.isEmpty(dataFlowList);
        Assert.isTrue((querySqlConig && !dataFlowConfig) || (!querySqlConig && dataFlowConfig), "配置querySql不能启用sql分片功能，没有配置query_sql功能必须启用sql分片功能");

        // 校验paramsJson对query_sql中的where参数配置
        JSONObject jsonObject = JSONObject.fromObject(paramsJson);
        Set<String> keys = jsonObject.keySet();
        Assert.notNull(jsonObject, "入参paramJson解析json失败！");
        if (!StringUtils.isEmpty(querySql)) {
            Set<String> paramNames = SqlUtils.getParamField(querySql);
            Assert.isTrue(CollectionUtils.isEqualCollection(paramNames, keys), "入参paramsJson的参数配置和api_source表query_sql的where查询参数不对应");
        } else {
            Set<String> queryParams = new HashSet<>();
            for (DataFlow dataFlow : dataFlowList) {
                String sql = dataFlow.getQuerySql();
                Assert.hasText(sql, "dataFlow的sql不能配置为空！");
                queryParams.addAll(SqlUtils.getParamField(sql));
            }
            Assert.isTrue(CollectionUtils.isEqualCollection(queryParams, keys), "入参paramsJson的参数配置和data_flow表query_sql的where查询参数不对应");
        }
    }


    /**
     * 数据特殊处理转换
     *
     * @param apiCode
     * @param jsonObjects
     */
    private void dataConvert(String apiCode, List<JSONObject> jsonObjects) {
        log.info("apiCode={}进行dataConvert转换处理", apiCode);
        if (CollectionUtils.isEmpty(jsonObjects)) {
            return;
        }
        // 根据apiCode查找bsh脚本的配置信息对特殊字段进行逻辑处理
        List<DataConvert> convertList = dataConvertRepository.findByApiCode(apiCode);
        if (!CollectionUtils.isEmpty(convertList)) {
            // 如果配置的特殊脚本不为空则做特殊处理
            Map<String, String> convertMap = convertList.stream().collect(Collectors.toMap(DataConvert::getApiFieldName, DataConvert::getConvertScript));
            Set<String> keys = convertMap.keySet();
            for (String key : keys) {
                String convertScript = convertMap.get(key);
                // 根据bsh脚本获取convert处理器
                Convert convert = ConvertUtils.convert(convertScript);
                for (JSONObject jsonObject : jsonObjects) {
                    // 完成bsh脚本的特殊转化
                    String convertResult = convert.convert(jsonObject.getString(key));
                    // 更新json中的特殊转化后的新值
                    jsonObject.put(key, convertResult);
                }
            }
        }
    }


    /**
     * 发送通知 邮件和钉钉
     *
     * @param jsonObjects
     */
    private void sendMail(List<JSONObject> jsonObjects, String apiName) throws Exception {
        File mailFile = ResourceUtils.getFile(mailStyle);
        String mailStyle = Files.readLines(mailFile, Charsets.UTF_8).stream().reduce((line1, line2) -> line1 + line2).get();
        String content = getMailHtmlContent(jsonObjects, apiName);
        content = mailStyle.replaceAll("#content", content);
        //  发送邮件
        restClient.sendMail(sendTo, content);
    }

    /**
     * 发送钉钉markdown文档
     *
     * @param jsonObjects
     * @throws Exception
     */
    private void sendDingTalk(List<JSONObject> jsonObjects) throws Exception {
        String content = String.format("data-platform数据统计报表表已发送至您的邮箱\n本次统计结果共有%d条\n详情请查看邮件~", jsonObjects.size());
        // 发送钉钉
        restClient.sendDingTalk(content);
    }


    /**
     * 根据json拼接html的表单
     *
     * @param jsonObjects
     * @param tableTitle
     * @return
     */
    private String getMailHtmlContent(List<JSONObject> jsonObjects, String tableTitle) {
        String tr = "";
        StringBuffer th = new StringBuffer();
        if (jsonObjects.isEmpty()) {
            return String.format("%s \n本次统计数据为空", tableTitle);
        }
        jsonObjects.get(0).keySet().stream().forEach(key -> {
            th.append(String.format("<th> %s </th> \n", key));
        });
        for (JSONObject jsonObject : jsonObjects) {
            Set<String> keys = jsonObject.keySet();
            String td = "";
            for (String key : keys) {
                td += String.format("<td>%s</td> \n", jsonObject.get(key));
            }
            tr += String.format("<tr> \n %s </tr>", td);
        }
        String caption = String.format("<caption> %s </caption>", tableTitle);
        String content = String.format("<table> \n %s \n<tr> \n %s \n</tr> \n %s \n </table>", caption, th, tr);
        return content;
    }

}