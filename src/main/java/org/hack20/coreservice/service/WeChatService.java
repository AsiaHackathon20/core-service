package org.hack20.coreservice.service;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.exception.CoreServiceException;
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

    public String getToken() {
        GetAuthTokenResponse response = weChatGateway.getToken(corpID, corpSecret);
        log.info("Returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        if (response.getErrorCode() == 0) {
            return response.getAccessToken();
        } else {
            throw new CoreServiceException();
        }
    }

    public List<Department> getAllDepartments(String accessToken) {
        GetDepartmentsResponse response = weChatGateway.getAllDepartments(accessToken);
        log.info("Returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        if (response.getErrorCode() == 0) {
            return response.getDepartment();
        } else {
            throw new CoreServiceException();
        }
    }

    public List<User> getUsersInDepartment(String accessToken, Long departmentID) {
        GetUsersInDepartmentResponse response = weChatGateway.getUsersInDepartment(accessToken, departmentID);
        log.info("Returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        if (response.getErrorCode() == 0) {
            return response.getUserList();
        } else {
            throw new CoreServiceException();
        }
    }

    public List<String> getExternalContacts(String accessToken, String externalUserID) {
        GetExternalContactResponse response = weChatGateway.getExternalContacts(accessToken, externalUserID);
        log.info("Returned with errcode {} and errmsg {}", response.getErrorCode(), response.getErrorMessage());
        if (response.getErrorCode() == 0) {
            return response.getExternalUserIds();
        } else {
            throw new CoreServiceException();
        }
    }
}
