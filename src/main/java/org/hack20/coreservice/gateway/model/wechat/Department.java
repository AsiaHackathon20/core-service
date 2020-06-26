package org.hack20.coreservice.gateway.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Department {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("name_en")
    private String nameInEnglish;

    @JsonProperty("parentid")
    private Long parentID;

    @JsonProperty("order")
    private Long order;
}
