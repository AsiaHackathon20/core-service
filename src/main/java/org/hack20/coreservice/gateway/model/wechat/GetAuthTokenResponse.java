package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

@Data
public class GetAuthTokenResponse extends Response {

    private String access_token;
    private long expires_in;
}
