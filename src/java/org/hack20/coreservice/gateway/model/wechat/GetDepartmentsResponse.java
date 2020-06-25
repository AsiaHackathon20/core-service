package org.hack20.coreservice.gateway.model.wechat;

import lombok.Data;

import java.util.List;

@Data
public class GetDepartmentsResponse extends Response {

    private List<Department> department;
}
