package org.hack20.coreservice.service;

import lombok.extern.slf4j.Slf4j;
import org.hack20.coreservice.gateway.PolicyGateway;
import org.hack20.coreservice.gateway.model.policy.Contacts;
import org.hack20.coreservice.gateway.model.policy.Platform;
import org.hack20.coreservice.gateway.model.policy.PlatformIdentifierType;
import org.hack20.coreservice.gateway.model.policy.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PolicyService {

    @Autowired
    private PolicyGateway policyGateway;

    private Map<String, Policy> policyMap = new HashMap<>();

    @EventListener(classes = ApplicationReadyEvent.class)
    void init() {
        log.info("Loading cache from policy server");
        List<Policy> policies = policyGateway.getAllPolicies();
        if (Objects.nonNull(policies)) {
            policyMap = policies.stream().collect(Collectors.toMap(Policy::getSid, p -> p));
        }
        log.info("Loaded cache from policy server");
    }

    @Cacheable(value = "policyCache")
    public Policy getPolicy(String sid) {
        log.info("Retrieving policy for sid {}", sid);
        return policyMap.get(sid);
    }


    public List<String> getEligibleContacts(final String sid, final PlatformIdentifierType platformIdentifierType){
        final Policy policy = policyMap.getOrDefault(sid, new Policy());
        final List<String> eligibleContacts = new ArrayList<>();
        final List<Contacts> contacts = Optional.ofNullable(policy.getContacts()).orElse(Collections.EMPTY_LIST);
        contacts.forEach(contact -> {
            final Platform filteredPlatform = contact.getPlatforms().stream().filter(platform -> platformIdentifierType.equals(platform.getIdentifier())).findFirst().orElse(new Platform());
            eligibleContacts.addAll(Optional.ofNullable(filteredPlatform.getEnterpriseAccount()).orElse(Collections.EMPTY_LIST));
            eligibleContacts.addAll(Optional.ofNullable(filteredPlatform.getPersonalAccount()).orElse(Collections.EMPTY_LIST));
        });
        log.info("List of Eligible Contacts for sid = {}, platform = {} is {}", sid, platformIdentifierType, eligibleContacts);
        return eligibleContacts;
    }

    public void setPolicyMap(final List<Policy> policies) {
        if (!CollectionUtils.isEmpty(policies)) {
            policyMap.putAll(policies.stream().collect(Collectors.toMap(Policy::getSid, p -> p)));
        }
    }
}
