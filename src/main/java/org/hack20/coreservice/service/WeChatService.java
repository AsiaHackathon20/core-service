package org.hack20.coreservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hack20.coreservice.gateway.WeChatGateway;
import org.hack20.coreservice.gateway.model.wechat.Department;
import org.hack20.coreservice.gateway.model.wechat.GetAuthTokenResponse;
import org.hack20.coreservice.gateway.model.wechat.GetDepartmentsResponse;
import org.hack20.coreservice.gateway.model.wechat.GetExternalContactResponse;
import org.hack20.coreservice.gateway.model.wechat.GetUsersInDepartmentResponse;
import org.hack20.coreservice.gateway.model.wechat.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WeChatService {

    @Autowired
    private WeChatGateway weChatGateway;

    @Value("${credentials.stratos.corpID}")
    private String corpID;

    @Value("${credentials.stratos.corpSecret}")
    private String corpSecret;

    private String inMemoryToken;

    public String getToken() {
        GetAuthTokenResponse response = weChatGateway.getToken(corpID, corpSecret);
        log.info("Get token returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        inMemoryToken = response.getAccessToken();
        return inMemoryToken;
    }

    public List<Department> getAllDepartments() {
        GetDepartmentsResponse response = weChatGateway.getAllDepartments(getInMemoryToken());
        log.info("Get all departments returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        return response.getDepartment();
    }

    public List<User> getUsersInDepartment(Long departmentID) {
        GetUsersInDepartmentResponse response = weChatGateway.getUsersInDepartment(getInMemoryToken(), departmentID);
        log.info("Get Users in department returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        return response.getUserList();
    }

    public List<String> getExternalContacts(String externalUserID) {
        GetExternalContactResponse response = weChatGateway.getExternalContacts(getInMemoryToken(), externalUserID);
        log.info("Get external contacts returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        return response.getExternalUserIds();
    }

    private String getInMemoryToken() {
        if (StringUtils.isNotBlank(inMemoryToken)) {
            return inMemoryToken;
        } else {
            return getToken();
        }
    }
}
