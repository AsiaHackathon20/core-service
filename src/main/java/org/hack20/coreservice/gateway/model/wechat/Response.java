package org.hack20.coreservice.gateway.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Response {

    public static final Long NO_ERROR_CODE = 0l;
    public static final Long TOKEN_EXPIRED = 42001l;
    public static final Long INVALID_ACCESS_TOKEN = 40014l;

    @JsonProperty("errcode")
    private long errorCode;

    @JsonProperty("errmsg")
    private String errorMessage;
}
