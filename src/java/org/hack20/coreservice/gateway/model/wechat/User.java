package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String userid;
    private String name;
    private List<Long> department;
    private String open_userid;
}
