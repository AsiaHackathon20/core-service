package org.hack20.coreservice.service;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.exception.CoreServiceException;
import org.hack20.coreservice.gateway.WechatGateway;
import org.hack20.coreservice.gateway.model.wechat.Department;
import org.hack20.coreservice.gateway.model.wechat.GetAuthTokenResponse;
import org.hack20.coreservice.gateway.model.wechat.GetDepartmentsResponse;
import org.hack20.coreservice.gateway.model.wechat.GetUsersInDepartmentResponse;
import org.hack20.coreservice.gateway.model.wechat.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WechatService {

    @Autowired
    private WechatGateway wechatGateway;

    @Value("${credentials.stratos.corpID}")
    private String corpID;

    @Value("${credentials.stratos.corpSecret}")
    private String corpSecret;

    public String getToken() {
        GetAuthTokenResponse response = wechatGateway.getToken(corpID, corpSecret);
        log.info("Returned with errcode {} and errmsg {}", response.getErrcode(), response.getErrmsg());
        if (response.getErrcode() == 0) {
            return response.getAccess_token();
        } else {
            throw new CoreServiceException();
        }
    }

    public List<Department> getAllDepartments(String accessToken) {
        GetDepartmentsResponse response = wechatGateway.getAllDepartments(accessToken);
        log.info("Returned with errcode {} and errmsg {}", response.getErrcode(), response.getErrmsg());
        if (response.getErrcode() == 0) {
            return response.getDepartment();
        } else {
            throw new CoreServiceException();
        }
    }

    public List<User> getUsersInDepartment(String accessToken, Long departmentID) {
        GetUsersInDepartmentResponse response = wechatGateway.getUsersInDepartment(accessToken, departmentID);
        log.info("Returned with errcode {} and errmsg {}", response.getErrcode(), response.getErrmsg());
        if (response.getErrcode() == 0) {
            return response.getUserlist();
        } else {
            throw new CoreServiceException();
        }
    }
}
