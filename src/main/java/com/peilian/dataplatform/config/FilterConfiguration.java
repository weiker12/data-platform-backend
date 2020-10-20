package com.peilian.dataplatform.config;

import com.peilian.dataplatform.common.ContextFilter;
import com.peilian.dataplatform.common.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean registrationJwt(CorsFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }


    @Bean
    public FilterRegistrationBean registrationContext(ContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

}
