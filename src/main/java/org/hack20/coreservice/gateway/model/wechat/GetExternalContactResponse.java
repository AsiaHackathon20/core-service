package org.hack20.coreservice.gateway.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetExternalContactResponse extends Response {

    @JsonProperty("external_userid")
    private List<String> externalUserIds;
}
