package org.hack20.coreservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hack20.coreservice.gateway.model.policy.PlatformIdentifierType;
import org.hack20.coreservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class WeChatWorkMessageService implements ISmpMessageService<WeChatMessage> {

    private ConcurrentHashMap<String, SMPActivity> weChatMessageDataStore;
    private PolicyService policyService;

    @Autowired
    public WeChatWorkMessageService (final ConcurrentHashMap<String, SMPActivity> weChatWorkDataStore, final PolicyService policyService){
        this.weChatMessageDataStore = weChatWorkDataStore;
        this.policyService = policyService;
    }

    public ConcurrentHashMap<String, SMPActivity> getWeChatMessageContactMappings(){
        return weChatMessageDataStore;
    }

    @Override
    public void processMessage(final WeChatMessage weChatMessage) {
        final String msgType = StringUtils.trimToEmpty(weChatMessage.getMsgType()).toUpperCase();
        switch (msgType){
            case "EVENT":
                final String jpmUserSid = weChatMessage.getUserId();
                processEventMessageType(weChatMessage.getChangeType(), jpmUserSid, weChatMessage.getExternalUserId(), Long.valueOf(weChatMessage.getCreateTime()));
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

    private void processEventMessageType(final String changeType, final String jpmUserSid, final String externalUserId, final long createTimeInSeconds) {
        final List<String> eligibleContacts = policyService.getEligibleContacts(jpmUserSid, PlatformIdentifierType.WE_CHAT);
        final PersonActivityDetails externalContact = getExternalContactActivityDetails(externalUserId, createTimeInSeconds);
        log.info("Change Type received is = {}", changeType);
        final SMPActivity smpActivity = weChatMessageDataStore.computeIfAbsent(jpmUserSid, s -> weChatMessageDataStore.put(jpmUserSid, new SMPActivity()));
        final String externalContactSmpIdentifier = externalContact.getSmpIdentifier();

        switch (changeType){
            case "ADD_EXTERNAL_CONTACT":
                smpActivity.getTotalNumberOfContacts().add(externalContact);
                if (!eligibleContacts.contains(externalContactSmpIdentifier)) {
                    smpActivity.getTotalUnacceptableContacts().add(externalContact);
                    //We should try to send some event
                }
                return;
            case "DEL_EXTERNAL_CONTACT":
                smpActivity.getTotalNumberOfContacts().remove(externalContact);
                smpActivity.getTotalUnacceptableContacts().remove(externalContact);
                return;
            default:
                break;
        }
    }

    private void processTextMessageType(final WeChatMessage weChatMessage){
        final String text = weChatMessage.getContent();
        log.info("Processing text message = {}", text);
        final String fromUserName = weChatMessage.getFromUserName();
        final SMPActivity smpActivity = new SMPActivity();
        weChatMessageDataStore.putIfAbsent(fromUserName, smpActivity);
        final TextMessageDetails textMessageDetails = TextMessageDetails.build(fromUserName, weChatMessage.getToUserName(), text, Long.valueOf(weChatMessage.getCreateTime()));
        weChatMessageDataStore.get(fromUserName).getTextMessageDetails().add(textMessageDetails);
    }


}