package com.peilian.dataplatform.task;

import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.controller.ApiController;
import com.peilian.dataplatform.dto.DataDto;
import com.peilian.dataplatform.entity.ApiSource;
import com.peilian.dataplatform.enums.ResultType;
import com.peilian.dataplatform.repository.ApiSourceRepository;
import com.peilian.dataplatform.util.MailRest;
import com.peilian.job.core.biz.model.ReturnT;
import com.peilian.job.core.handler.IJobHandler;
import com.peilian.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据报表平台的定时任务 xxl-job调度
 *
 * @author zhengshangchao
 */
@Component
@JobHandler("dataPlatformTask")
@Slf4j
public class DataPlatformTask extends IJobHandler {

    @Autowired
    private ApiController apiController;

    @Autowired
    private MailRest mailRest;

    @Autowired
    private ApiSourceRepository apiSourceRepository;

    /**
     * 短信缓存
     */
    private static Set<String> smsCache;

    /**
     * 数据统计报表调度
     *
     * @param paramJson
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String paramJson) throws Exception {
        log.info("调度任务入参paramJson={}", paramJson);
        long startTime = System.currentTimeMillis();
        JSONObject json = JSONObject.fromObject(paramJson);
        String apiCode = json.getString("apiCode");
        Assert.hasText(apiCode, "apiCode不能为空");
        json.remove("apiCode");
        // 开始调起任务
        DataDto dataDto = new DataDto();
        dataDto.setApiCode(apiCode);
        dataDto.setParamsJson(json.getJSONObject("paramsJson"));
        ResponseMessage responseMessage = apiController.getData(dataDto);
        long endTime = System.currentTimeMillis();
        log.info(String.format("调用成功，共耗时：%ds", (endTime - startTime) / 1000));
        ReturnT<String> returnT = new ReturnT<>();
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setMsg(responseMessage.getMessage());
        returnT.setContent(String.format("调用成功，共耗时：%ds", (endTime - startTime) / 1000));
        // 发送钉钉
        sendSmsCode(apiCode, responseMessage.getData().toString());
        return returnT;
    }

    /**
     * 发送短信验证码到钉钉群
     */
    private void sendSmsCode(String apiCode, String content) throws Exception {
        if(smsCache == null) {
            smsCache = new HashSet<>();
        }
        // 如果非短信验证码任务则忽略发钉钉消息
        if(!"smsCode".equals(apiCode)) {
            return;
        }
        ApiSource apiSource = apiSourceRepository.findByApiCode(apiCode);
        if(ResultType.OBJECT.getCode().equals(apiSource.getResultType())) {
            JSONObject jsonObject = JSONObject.fromObject(content);
            String sendMessage = String.format("小马AI陪练qa环境测试手机号:%s \n【小马AI陪练】验证码：%s, 1分钟内有效，请勿泄露。", jsonObject.get("mobile"), jsonObject.get("code"));
            mailRest.sendDingTalk(sendMessage);
        } else if (ResultType.ARRAY.getCode().equals(apiSource.getResultType())) {
            JSONArray jsonArray = JSONArray.fromObject(content);
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.getString("mobile") + jsonObject.getString("code");
                if(smsCache.contains(key)) {
                    continue;
                }
                smsCache.add(key);
                String sendMessage = String.format("小马AI陪练qa环境测试手机号:%s \n【小马AI陪练】验证码：%s, 1分钟内有效，请勿泄露。", jsonObject.get("mobile"), jsonObject.get("code"));
                mailRest.sendDingTalk(sendMessage);
            }
        } else {
            log.info("apiCode={}暂时无法处理", apiCode);
        }

    }

}
