package org.hack20.coreservice.config;

import org.hack20.coreservice.model.SMPActivity;
import org.hack20.coreservice.service.PolicyService;
import org.hack20.coreservice.service.WeChatWorkMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableCaching
@EnableFeignClients(basePackages = "org.hack20.coreservice.gateway")
public class CoreServiceConfig {
    @Bean
    public PolicyService policyService(){
        return new PolicyService();
    }

}
