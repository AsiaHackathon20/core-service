package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

import java.util.List;

@Data
public class GetExternalContactResponse extends Response {

    private List<String> external_userid;
}
