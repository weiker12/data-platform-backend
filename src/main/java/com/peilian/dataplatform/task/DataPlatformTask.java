package com.peilian.dataplatform.task;

import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.controller.ApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 数据报表平台的定时任务
 *
 * @author zhengshangchao
 */
@Component
@Slf4j
public class DataPlatformTask {

    @Autowired
    private ApiController apiController;

    @Value("${data-platform.task.name}")
    private String taskName;

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduled() throws Exception {
        log.info("调度开始...");
        long startTime = System.currentTimeMillis();
        String json = "{\n" +
                "    \"prodCode\": \"BizExecutor\",\n" +
                "    \"bizType\": 1\n" +
                "}";
        apiController.getData(taskName, json);
        long endTime = System.currentTimeMillis();
        log.info(String.format("调度任务完成，共耗时：%ds", (endTime - startTime) / 1000));
    }
}
