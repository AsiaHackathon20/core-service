package org.hack20.coreservice.gateway.model.policy;

import lombok.Data;

import java.util.List;

@Data
public class Contacts {
    private String name;
    private String clientIdentifier;
    private ClientIdentifierType clientIdentifierType;
    private List<Platform> platforms;
}
