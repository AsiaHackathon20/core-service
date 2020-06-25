package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

@Data
public class GetExternalContactResponse extends Response {

    private ExternalContact external_contact;
}
