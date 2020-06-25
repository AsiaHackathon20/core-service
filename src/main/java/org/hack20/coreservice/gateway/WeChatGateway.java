package org.hack20.coreservice.gateway;

import org.hack20.coreservice.gateway.model.wechat.GetAuthTokenResponse;
import org.hack20.coreservice.gateway.model.wechat.GetDepartmentsResponse;
import org.hack20.coreservice.gateway.model.wechat.GetExternalContactResponse;
import org.hack20.coreservice.gateway.model.wechat.GetUsersInDepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "wechatGateway", url = "${gateways.wechatGateway.baseurl}")
public interface WeChatGateway {

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/gettoken")
    GetAuthTokenResponse getToken(@RequestParam("corpid") String corpid, @RequestParam("corpsecret") String corpsecret);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/department/list")
    GetDepartmentsResponse getAllDepartments(@RequestParam("access_token") String accessToken);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/user/list")
    GetUsersInDepartmentResponse getUsersInDepartment(@RequestParam("access_token") String accessToken, @RequestParam("department_id") Long departmentID);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/externalcontact/get")
    GetExternalContactResponse getExternalContacts(@RequestParam("access_token") String accessToken, @RequestParam("external_userid") String externalUserID);
}
