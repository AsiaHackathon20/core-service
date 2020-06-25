package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

@Data
public class Department {

    private Long id;
    private String name;
    private String name_en;
    private Long parentid;
    private Long order;
}
