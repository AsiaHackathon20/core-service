package org.hack20.coreservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hack20.coreservice.gateway.model.policy.PlatformIdentifierType;
import org.hack20.coreservice.model.PersonActivityDetails;
import org.hack20.coreservice.model.SMPActivity;
import org.hack20.coreservice.model.TextMessageDetails;
import org.hack20.coreservice.model.WeChatMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class WeChatWorkMessageService implements ISmpMessageService<WeChatMessage> {

    private ConcurrentHashMap<String,ConcurrentHashMap<String, SMPActivity>> weChatWorkDepartmentDataStore;
    private PolicyService policyService;
    private static final String ASIA_RATES_SALES_DEPARTMENT = "ASIA_RATES_SALES";

    @Autowired
    public WeChatWorkMessageService (final ConcurrentHashMap<String,ConcurrentHashMap<String, SMPActivity>> weChatWorkDepartmentDataStore, final PolicyService policyService){
        this.weChatWorkDepartmentDataStore = weChatWorkDepartmentDataStore;
        this.policyService = policyService;
    }

    public ConcurrentHashMap<String,ConcurrentHashMap<String, SMPActivity>> getWeChatMessageContactMappings(){
        //mockWeChatMessages();
        return weChatWorkDepartmentDataStore;
    }

    //TODO - In theory we dont want this but just added for sample data
    private void mockWeChatMessages()  {
        try {
            final String addExternalContactMessageForYichen = "<xml><ToUserName><![CDATA[ww4816b0ccb0d935f1]]></ToUserName><FromUserName><![CDATA[sys]]></FromUserName><CreateTime>1593528536</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[change_external_contact]]></Event><ChangeType><![CDATA[add_external_contact]]></ChangeType><UserID><![CDATA[N654179]]></UserID><ExternalUserID><![CDATA[wm5iBDBgAAGGB4tU88_fEDR6jCuIQEnQ]]></ExternalUserID><WelcomeCode><![CDATA[zK-YhB6_3D2K7vTyQvbZvL88eTb-KtAVcoNnpwlduQ8]]></WelcomeCode></xml>";
            final XmlMapper xmlMapper = new XmlMapper();
            final WeChatMessage weChatMessage = xmlMapper.readValue(addExternalContactMessageForYichen, WeChatMessage.class);
            weChatMessage.setDepartmentId(ASIA_RATES_SALES_DEPARTMENT);

            final String addExternalContactMessageForYichen1 = "<xml><ToUserName><![CDATA[ww4816b0ccb0d935f1]]></ToUserName><FromUserName><![CDATA[sys]]></FromUserName><CreateTime>1593527893</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[change_external_contact]]></Event><ChangeType><![CDATA[add_external_contact]]></ChangeType><UserID><![CDATA[N654179]]></UserID><ExternalUserID><![CDATA[wm5iBDBgAAPOb8JLN8qFbyS3IOE8zKuA]]></ExternalUserID><WelcomeCode><![CDATA[1INscmvu5vqxRpWcIKSIwEOtMSIRIlCpSnAaRqp1Osk]]></WelcomeCode></xml>";
            final WeChatMessage weChatMessage1 = xmlMapper.readValue(addExternalContactMessageForYichen1, WeChatMessage.class);
            weChatMessage1.setDepartmentId(ASIA_RATES_SALES_DEPARTMENT);

            processMessage(weChatMessage);
            processMessage(weChatMessage1);
        }catch (final Exception e){
            log.error("An exception occurred while mocking messages for wechat, e= {}", e);
        }

    }

    @Override
    public void processMessage(final WeChatMessage weChatMessage) {
        final String msgType = StringUtils.trimToEmpty(weChatMessage.getMsgType()).toUpperCase();
        switch (msgType){
            case "EVENT":
                processEventMessageType(weChatMessage);
                break;
            case "TEXT":
                processTextMessageType(weChatMessage);
                break;
            default:
                log.info("Unrecognized message type = {}", msgType);
                break;
        }
    }


    private PersonActivityDetails getExternalContactActivityDetails(final String externalUserId, final long createTimeStampInSeconds){
        //This external user Id is union id, we need to map this to wechat user id
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(createTimeStampInSeconds), ZoneOffset.UTC);
        return PersonActivityDetails.buildPerson(StringUtils.EMPTY, RandomStringUtils.randomAlphabetic(6), externalUserId, zonedDateTime);
    }

    private ConcurrentHashMap<String, SMPActivity> getWeChatMessageDataStore(final String departmentId){
        final String depId = StringUtils.trimToEmpty(departmentId).toUpperCase();
        weChatWorkDepartmentDataStore.putIfAbsent(depId, new ConcurrentHashMap<String, SMPActivity>());
        return weChatWorkDepartmentDataStore.get(depId);
    }

    private void processEventMessageType(final WeChatMessage weChatMessage) {
        final ConcurrentHashMap<String, SMPActivity> weChatMessageDataStore = getWeChatMessageDataStore(weChatMessage.getDepartmentId());
        final String changeType = StringUtils.trimToEmpty(weChatMessage.getChangeType()).toUpperCase();
        final String jpmUserSid = StringUtils.trimToEmpty(weChatMessage.getUserId()).toUpperCase();
        final String externalUserId = StringUtils.trimToEmpty(weChatMessage.getExternalUserId());
        final long createTimeInSeconds = Long.valueOf(weChatMessage.getCreateTime());

        final List<String> eligibleContacts = policyService.getEligibleContacts(jpmUserSid, PlatformIdentifierType.WE_CHAT);
        final PersonActivityDetails externalContact = getExternalContactActivityDetails(externalUserId, createTimeInSeconds);

        if(StringUtils.isNotBlank(changeType)){
            log.info("Change Type received is = {}", changeType);
            weChatMessageDataStore.putIfAbsent(jpmUserSid, new SMPActivity());
            final SMPActivity cachedSmpActivity = weChatMessageDataStore.get(jpmUserSid);
            final String externalContactSmpIdentifier = externalContact.getSmpIdentifier();

            final Set<PersonActivityDetails> totalNumberOfContacts = cachedSmpActivity.getTotalNumberOfContacts();
            final Set<PersonActivityDetails> totalUnacceptableContacts = cachedSmpActivity.getTotalUnacceptableContacts();
            switch (changeType){
                case "ADD_EXTERNAL_CONTACT":
                    if(!totalNumberOfContacts.contains(externalContact)){
                        totalNumberOfContacts.add(externalContact);
                    }
                    if (!eligibleContacts.contains(externalContactSmpIdentifier) && !totalUnacceptableContacts.contains(externalContact)) {
                        totalUnacceptableContacts.add(externalContact);
                        //We should try to send some event
                    }
                    return;
                case "DEL_EXTERNAL_CONTACT":
                    cachedSmpActivity.getTotalNumberOfContacts().remove(externalContact);
                    totalUnacceptableContacts.remove(externalContact);
                    return;
                default:
                    break;
            }
        }

    }

    private void processTextMessageType(final WeChatMessage weChatMessage){
        final ConcurrentHashMap<String, SMPActivity> weChatMessageDataStore = getWeChatMessageDataStore(weChatMessage.getDepartmentId());
        final String text = weChatMessage.getContent();
        log.info("Processing text message = {}", text);
        final String fromUserName = weChatMessage.getFromUserName();
        final SMPActivity smpActivity = new SMPActivity();
        weChatMessageDataStore.putIfAbsent(fromUserName, smpActivity);
        final TextMessageDetails textMessageDetails = TextMessageDetails.build(fromUserName, weChatMessage.getToUserName(), text, Long.valueOf(weChatMessage.getCreateTime()));
        weChatMessageDataStore.get(fromUserName).getTextMessageDetails().add(textMessageDetails);
    }


}
