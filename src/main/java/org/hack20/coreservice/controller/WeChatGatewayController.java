package org.hack20.coreservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.gateway.model.wechat.Department;
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
    public List<Department> getAllDepartments() {
        log.info("Retrieving all departments");
        return wechatService.getAllDepartments();
    }

    @PutMapping(value = "/department/users/all")
    public List<User> getUsersInDepartment(@RequestParam("deptID") Long departmentID) {
        log.info("Retrieving all users in department id {}", departmentID);
        return wechatService.getUsersInDepartment(departmentID);
    }

    @PutMapping(value = "/external-contact")
    public List<String> getExternalContacts(@RequestParam("userID") String userID) {
        log.info("Retrieving external contacts for user id {}", userID);
        return wechatService.getExternalContacts(userID);
    }
}
