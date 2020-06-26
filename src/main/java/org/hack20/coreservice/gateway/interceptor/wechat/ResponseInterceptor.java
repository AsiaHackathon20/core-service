package org.hack20.coreservice.gateway.interceptor.wechat;

import org.hack20.coreservice.exception.CoreServiceException;
import org.hack20.coreservice.gateway.model.wechat.Response;
import org.hack20.coreservice.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseInterceptor {

    @Autowired
    private WeChatService weChatService;

    void intercept() {
        long errorCode = 1l;
        if (Response.TOKEN_EXPIRED == errorCode) {

        } else if (Response.NO_ERROR_CODE == errorCode) {

        } else {
            throw new CoreServiceException();
        }
    }
}
