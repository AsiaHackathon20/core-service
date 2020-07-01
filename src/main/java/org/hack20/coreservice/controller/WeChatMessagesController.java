package org.hack20.coreservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.model.SMPActivity;
import org.hack20.coreservice.model.WeChatMessage;
import org.hack20.coreservice.service.WeChatWorkMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class WeChatMessagesController {

    private WeChatWorkMessageService weChatWorkMessageService;

    @Autowired
    public WeChatMessagesController (final WeChatWorkMessageService weChatWorkMessageService){
        this.weChatWorkMessageService = weChatWorkMessageService;
    }

    @PostMapping(value = "/message/")
    public void processWeChatMessage(@RequestBody final WeChatMessage weChatMessage) {
        log.info("******RECEIVED WECHAT MESSAGE****** = {}", weChatMessage);
        weChatWorkMessageService.processMessage(weChatMessage);
    }

    @GetMapping(value = "/getWeChatActivitySnapshot/")
    public ConcurrentHashMap<String, ConcurrentHashMap<String, SMPActivity>> getWeChatActivitySnapshot() {
        final ConcurrentHashMap<String, ConcurrentHashMap<String, SMPActivity>> weChatMessageContactMappings = weChatWorkMessageService.getWeChatMessageContactMappings();
        log.info("****** Sending WeChatMessage Activity Snapshot, size = {} ******", weChatMessageContactMappings.size());
        return weChatMessageContactMappings;
    }

}
