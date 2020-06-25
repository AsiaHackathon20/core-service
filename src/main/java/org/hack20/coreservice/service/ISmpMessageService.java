package org.hack20.coreservice.service;

import org.hack20.coreservice.model.ISMPMessage;

public interface ISmpMessageService<T extends ISMPMessage> {
    void processMessage(T ismpMessage);
}
