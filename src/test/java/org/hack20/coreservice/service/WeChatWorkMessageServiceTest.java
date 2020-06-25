package org.hack20.coreservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hack20.coreservice.model.SMPActivity;
import org.hack20.coreservice.model.WeChatMessage;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class WeChatWorkMessageServiceTest {

    public ConcurrentHashMap<String, SMPActivity> weChatWorkDataStore(){
        return new ConcurrentHashMap();
    }

    private WeChatWorkMessageService weChatWorkMessageService = new WeChatWorkMessageService(weChatWorkDataStore(), null);

    @Test
    void testProcessMessage(){
        WeChatMessage weChatMessage = new WeChatMessage();
        weChatMessage.setFromUserName("F241770");
        weChatMessage.setContent("test1");
        weChatMessage.setMsgType("Text");
        weChatMessage.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage);

        WeChatMessage weChatMessage1 = new WeChatMessage();
        weChatMessage1.setFromUserName("F241770");
        weChatMessage1.setContent("test2");
        weChatMessage1.setMsgType("Text");
        weChatMessage1.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage1);
        assertEquals(2, weChatWorkMessageService.getWeChatMessageContactMappings().get("F241770").getTextMessageDetails().size());
    }

}