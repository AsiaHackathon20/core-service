package org.hack20.coreservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.model.WeChatMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WeChatMessagesController {

    @PostMapping(value = "/message/")
    public void processWeChatMessage(@RequestBody final WeChatMessage weChatMessage) {
        log.info("******RECEIVED WECHAT MESSAGE****** = {}", weChatMessage);


    }

}
