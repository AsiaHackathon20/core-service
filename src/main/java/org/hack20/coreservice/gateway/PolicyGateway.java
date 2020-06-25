package org.hack20.coreservice.gateway;

import org.hack20.coreservice.gateway.model.policy.Policy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "policyGateway", url = "${gateways.policyGateway.baseurl}")
public interface PolicyGateway {

    @RequestMapping(method = RequestMethod.GET, value = "/policy-mapping/all")
    List<Policy> getAllPolicies();
}
