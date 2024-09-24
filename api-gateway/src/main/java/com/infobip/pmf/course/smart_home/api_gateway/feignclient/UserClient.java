package com.infobip.pmf.course.smart_home.api_gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;

// a Feign client to interact with the user management service
@FeignClient(name = "user-management-service", url = "http://user-management-service:8080")
public interface UserClient 
{
    @GetMapping("/validate-api-key")
    boolean validateApiKey(@RequestParam("apiKey") String apiKey);
}