package com.peilian.dataplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author zhengshangchao
 */
@EnableTransactionManagement
@EnableScheduling
@EnableJpaRepositories
@SpringBootApplication
@EnableJpaAuditing
public class DataPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPlatformApplication.class, args);
    }

}
