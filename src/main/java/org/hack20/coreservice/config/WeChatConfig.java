package org.hack20.coreservice.config;

import org.hack20.coreservice.model.SMPActivity;
import org.hack20.coreservice.service.PolicyService;
import org.hack20.coreservice.service.WeChatWorkMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WeChatConfig {

    @Autowired
    private PolicyService policyService;


    @Bean
    public ConcurrentHashMap<String, SMPActivity> weChatWorkDataStore(){
        return new ConcurrentHashMap();
    }

    @Bean
    public WeChatWorkMessageService smpMessageService(){
        return new WeChatWorkMessageService(weChatWorkDataStore(), policyService);
    }

}
