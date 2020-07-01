package org.hack20.coreservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.IOUtils;
import org.hack20.coreservice.gateway.model.policy.Policy;
import org.hack20.coreservice.model.WeChatMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeChatWorkMessageServiceTest {


    private final PolicyService policyService = new PolicyService();

    private WeChatWorkMessageService weChatWorkMessageService = new WeChatWorkMessageService(new ConcurrentHashMap(), policyService);
    private static final String asiaRatesDepartment = "ASIA_RATES";
    private static final String asiaCreditDepartment = "ASIA_CREDIT";

    private final List<Policy> policies = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        populatePolicies();
        policyService.setPolicyMap(policies);
    }

    @Test
    void testProcessTextMessage() {
        final String ratesUserName = "TEST_RATES_USER";
        final String creditUserName = "TEST_CREDIT_USER";
        final WeChatMessage weChatMessage = new WeChatMessage();
        weChatMessage.setFromUserName(ratesUserName);
        weChatMessage.setContent("test1");
        weChatMessage.setMsgType("Text");
        weChatMessage.setDepartmentId(asiaRatesDepartment);
        weChatMessage.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage);

        final WeChatMessage weChatMessage1 = new WeChatMessage();
        weChatMessage1.setFromUserName(ratesUserName);
        weChatMessage1.setContent("test2");
        weChatMessage1.setMsgType("Text");
        weChatMessage1.setDepartmentId(asiaRatesDepartment);
        weChatMessage1.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage1);

        assertEquals(2, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaRatesDepartment).get(ratesUserName).getTextMessageDetails().size());

        final WeChatMessage weChatMessage2 = new WeChatMessage();
        weChatMessage2.setFromUserName(creditUserName);
        weChatMessage2.setContent("test2");
        weChatMessage2.setMsgType("Text");
        weChatMessage2.setDepartmentId(asiaCreditDepartment);
        weChatMessage2.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage2);

        assertEquals(1, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaCreditDepartment).get(creditUserName).getTextMessageDetails().size());
    }

    @Test
    void testProcessEventMessage(){
        final String asiaRatesDepartment = "ASIA_RATES";
        final String asiaCreditDepartment = "ASIA_CREDIT";
        final String ratesUserName = "TEST_RATES_USER";
        final String creditUserName = "TEST_CREDIT_USER";
        final String externalRatesUserName1 = "TEST_EXTERNAL_RATES_USER_CONTACT_1";
        final String externalRatesUserName2 = "TEST_EXTERNAL_RATES_USER_CONTACT_2";
        final String externalCreditUserName1 = "TEST_EXTERNAL_CREDIT_USER_CONTACT_1";


        final WeChatMessage weChatMessage = new WeChatMessage();
        weChatMessage.setUserId(ratesUserName);
        weChatMessage.setExternalUserId(externalRatesUserName1);
        weChatMessage.setMsgType("Event");
        weChatMessage.setChangeType("ADD_EXTERNAL_CONTACT");
        weChatMessage.setDepartmentId(asiaRatesDepartment);
        weChatMessage.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage);

        final WeChatMessage weChatMessage1 = new WeChatMessage();
        weChatMessage1.setUserId(ratesUserName);
        weChatMessage1.setExternalUserId(externalRatesUserName2);
        weChatMessage1.setMsgType("Event");
        weChatMessage1.setDepartmentId(asiaRatesDepartment);
        weChatMessage1.setChangeType("ADD_EXTERNAL_CONTACT");
        weChatMessage1.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage1);

        assertEquals(2, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaRatesDepartment).get(ratesUserName).getTotalNumberOfContacts().size());
        assertEquals(2, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaRatesDepartment).get(ratesUserName).getTotalUnacceptableContacts().size());

        final WeChatMessage weChatMessage2 = new WeChatMessage();
        weChatMessage2.setUserId(creditUserName);
        weChatMessage2.setExternalUserId(externalCreditUserName1);
        weChatMessage2.setMsgType("Event");
        weChatMessage2.setDepartmentId(asiaCreditDepartment);
        weChatMessage2.setChangeType("ADD_EXTERNAL_CONTACT");
        weChatMessage2.setCreateTime(String.valueOf(ZonedDateTime.now().toEpochSecond()));
        weChatWorkMessageService.processMessage(weChatMessage2);

        assertEquals(1, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaCreditDepartment).get(creditUserName).getTotalNumberOfContacts().size());
        assertEquals(1, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaCreditDepartment).get(creditUserName).getTotalUnacceptableContacts().size());
    }

    @Test
    void testPolicyValidations() throws JsonProcessingException {
        final String yichenUserId = "N654179";
        final String addExternalContactMessageForYichen = "<xml><ToUserName><![CDATA[ww4816b0ccb0d935f1]]></ToUserName><FromUserName><![CDATA[sys]]></FromUserName><CreateTime>1593528536</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[change_external_contact]]></Event><ChangeType><![CDATA[add_external_contact]]></ChangeType><UserID><![CDATA[N654179]]></UserID><ExternalUserID><![CDATA[wm5iBDBgAAGGB4tU88_fEDR6jCuIQEnQ]]></ExternalUserID><WelcomeCode><![CDATA[zK-YhB6_3D2K7vTyQvbZvL88eTb-KtAVcoNnpwlduQ8]]></WelcomeCode></xml>";
        final XmlMapper xmlMapper = new XmlMapper();
        final WeChatMessage weChatMessage = xmlMapper.readValue(addExternalContactMessageForYichen, WeChatMessage.class);
        weChatMessage.setDepartmentId(asiaRatesDepartment);
        weChatWorkMessageService.processMessage(weChatMessage);
        assertEquals(1, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaRatesDepartment).get(yichenUserId).getTotalNumberOfContacts().size());
        assertEquals(0, weChatWorkMessageService.getWeChatMessageContactMappings().get(asiaRatesDepartment).get(yichenUserId).getTotalUnacceptableContacts().size());
    }

    private void populatePolicies() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final ClassLoader classLoader = getClass().getClassLoader();
        final String policyJson = IOUtils.toString(classLoader.getResourceAsStream("defaultPolicy.json"), StandardCharsets.UTF_8);
        policies.addAll(objectMapper.readValue(policyJson, new TypeReference<List<Policy>>() {}));
    }

}