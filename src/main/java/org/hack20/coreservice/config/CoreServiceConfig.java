package org.hack20.coreservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableFeignClients(basePackages = "org.hack20.coreservice.gateway")
public class CoreServiceConfig {
}
