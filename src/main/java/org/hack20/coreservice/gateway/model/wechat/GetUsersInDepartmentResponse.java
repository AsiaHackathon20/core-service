package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

import java.util.List;

@Data
public class GetUsersInDepartmentResponse extends Response {

    private List<User> userlist;
}
