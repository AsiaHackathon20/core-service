package org.hack20.coreservice.controller;

import org.hack20.coreservice.gateway.model.policy.Policy;
import org.hack20.coreservice.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @GetMapping(value = "/policy-mapping/{sid}")
    public Policy getAllPolicies(@PathVariable("sid") String sid) {
        return policyService.getPolicy(sid);
    }
}
