package com.peilian.dataplatform.task;

import com.peilian.dataplatform.config.ResponseMessage;
import com.peilian.dataplatform.controller.ApiController;
import com.peilian.job.core.biz.model.ReturnT;
import com.peilian.job.core.handler.IJobHandler;
import com.peilian.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        ResponseMessage responseMessage = apiController.getData(apiCode, json.toString());
        long endTime = System.currentTimeMillis();
        log.info(String.format("调用成功，共耗时：%ds", (endTime - startTime) / 1000));
        ReturnT<String> returnT = new ReturnT<>();
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setMsg(responseMessage.getMessage());
        returnT.setContent(String.format("调用成功，共耗时：%ds", (endTime - startTime) / 1000));
        return returnT;
    }

}
