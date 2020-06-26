package org.hack20.coreservice.gateway.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class User {

    @JsonProperty("userid")
    private String userID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("department")
    private List<Long> department;

    @JsonProperty("open_userid")
    private String openUserID;
}
