package org.hack20.coreservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatMessage implements ISMPMessage{

    @JsonProperty("ToUserName")
    private String toUserName;
    @JsonProperty("FromUserName")
    private String fromUserName;
    @JsonProperty("CreateTime")
    private String createTime;
    @JsonProperty("MsgType")
    private String msgType;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("MsgId")
    private String messageId;
    @JsonProperty("AgentID")
    private String agentId;
    @JsonProperty("ChangeType")
    private String changeType;
    @JsonProperty("UserID")
    private String userId;
    @JsonProperty("WelcomeCode")
    private String welcomeCode;
    @JsonProperty("Event")
    private String event;
    @JsonProperty("ExternalUserID")
    private String externalUserId;

    @Override
    public String toString() {
        return "WeChatMessage{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", msgType='" + msgType + '\'' +
                ", content='" + content + '\'' +
                ", messageId='" + messageId + '\'' +
                ", agentId='" + agentId + '\'' +
                ", changeType='" + changeType + '\'' +
                ", userId='" + userId + '\'' +
                ", welcomeCode='" + welcomeCode + '\'' +
                ", event='" + event + '\'' +
                ", externalUserId='" + externalUserId + '\'' +
                '}';
    }
}
