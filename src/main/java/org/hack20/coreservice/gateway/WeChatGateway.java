package org.hack20.coreservice.gateway;

import org.hack20.coreservice.gateway.model.wechat.GetAuthTokenResponse;
import org.hack20.coreservice.gateway.model.wechat.GetDepartmentsResponse;
import org.hack20.coreservice.gateway.model.wechat.GetExternalContactResponse;
import org.hack20.coreservice.gateway.model.wechat.GetUsersInDepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Please take note that for methods that require access token that needs to be sent as a parameter.
 * The access token parameter should be the last argument.
 * This is done so that the {@link org.hack20.coreservice.gateway.interceptor.wechat.ResponseInterceptor}
 * is able to inject the renewed token should the token expire.
 */

@FeignClient(value = "wechatGateway", url = "${gateways.wechatGateway.baseurl}")
public interface WeChatGateway {

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/gettoken")
    GetAuthTokenResponse getToken(@RequestParam("corpid") String corpid, @RequestParam("corpsecret") String corpsecret);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/department/list")
    GetDepartmentsResponse getAllDepartments(@RequestParam("access_token") String accessToken);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/user/list")
    GetUsersInDepartmentResponse getUsersInDepartment(@RequestParam("department_id") Long departmentID, @RequestParam("access_token") String accessToken);

    @RequestMapping(method = RequestMethod.GET, value = "/cgi-bin/externalcontact/list")
    GetExternalContactResponse getExternalContacts(@RequestParam("userid") String userID, @RequestParam("access_token") String accessToken);
}
