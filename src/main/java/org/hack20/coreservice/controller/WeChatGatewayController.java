package org.hack20.coreservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.gateway.model.wechat.Department;
import org.hack20.coreservice.gateway.model.wechat.ExternalContact;
import org.hack20.coreservice.gateway.model.wechat.User;
import org.hack20.coreservice.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class WeChatGatewayController {

    @Autowired
    private WeChatService wechatService;

    @GetMapping(value = "/token")
    public String accessToken() {
        log.info("Retrieving access token");
        return wechatService.getToken();
    }

    @PutMapping(value = "/department/all")
    public List<Department> getAllDepartments(@RequestParam("accessToken") String accessToken) {
        log.info("Retrieving all departments");
        return wechatService.getAllDepartments(accessToken);
    }

    @PutMapping(value = "/department/users/all")
    public List<User> getUsersInDepartment(@RequestParam("accessToken") String accessToken, @RequestParam("deptID") Long departmentID) {
        log.info("Retrieving all users in department id {}", departmentID);
        return wechatService.getUsersInDepartment(accessToken, departmentID);
    }

    @PutMapping(value = "/external-contact")
    public ExternalContact getExternalContacts(@RequestParam("accessToken") String accessToken, @RequestParam("userID") String externalUserID) {
        log.info("Retrieving user information for external user {}", externalUserID);
        return wechatService.getExternalContacts(accessToken, externalUserID);
    }
}
