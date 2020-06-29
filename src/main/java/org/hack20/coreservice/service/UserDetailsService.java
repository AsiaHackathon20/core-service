package org.hack20.coreservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hack20.coreservice.model.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailsService {

    @Value("classpath:userDetails.json")
    private Resource resource;

    private ObjectMapper mapper = new ObjectMapper();

    private Map<String, UserDetails> cache = new HashMap<>();

    @EventListener(classes = ApplicationReadyEvent.class)
    void init() throws IOException {
        log.info("Loading user details from file {}", resource.getURI());
        String file = IOUtils.toString(resource.getInputStream(), Charset.forName("UTF-8"));
        List<UserDetails> userDetails = mapper.readValue(file, new TypeReference<List<UserDetails>>() {
        });
        Map<String, UserDetails> map = userDetails.stream().collect(Collectors.toMap(UserDetails::getId, ud -> ud));
        cache.putAll(map);
        log.info("Loaded user details from file");
    }

    @Cacheable(value = "userCache")
    public UserDetails getUserDetails(String id) {
        log.info("Retrieving user details for {}", id);
        return cache.get(id);
    }
}
