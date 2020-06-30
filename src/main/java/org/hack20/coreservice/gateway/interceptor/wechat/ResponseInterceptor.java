package org.hack20.coreservice.gateway.interceptor.wechat;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hack20.coreservice.exception.CoreServiceException;
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
        Response response = (Response) joinPoint.proceed();
        if (Response.TOKEN_EXPIRED == response.getErrorCode()
                || Response.INVALID_ACCESS_TOKEN == response.getErrorCode()) {
            log.info("Token issues observed for response {}", response);
            log.info("Attempting to re acquire token");
            weChatService.acquireToken();
            String token = weChatService.getCachedToken();
            Object[] enrichedArgs = joinPoint.getArgs();
            enrichedArgs[enrichedArgs.length - 1] = token;
            return joinPoint.proceed(enrichedArgs);
        } else if (Response.NO_ERROR_CODE == response.getErrorCode()) {
            return response;
        } else {
            log.error("Received error code {}, error message {}", response.getErrorCode(), response.getErrorCode());
            throw new CoreServiceException();
        }
    }
}
