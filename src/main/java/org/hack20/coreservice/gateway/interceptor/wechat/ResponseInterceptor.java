package org.hack20.coreservice.gateway.interceptor.wechat;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hack20.coreservice.gateway.model.wechat.Response;
import org.hack20.coreservice.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ResponseInterceptor {

    @Autowired
    private WeChatService weChatService;

    @Around("execution(* org.hack20.coreservice.gateway.WeChatGateway.*(..))")
    Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Invoking interceptor");
        Response response = (Response) joinPoint.proceed();
        if (Response.TOKEN_EXPIRED == response.getErrorCode()) {
            weChatService.getToken();
            return joinPoint.proceed();
        }
        return response;
    }
}
