package com.infobip.pmf.course.smart_home.api_gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.infobip.pmf.course.smart_home.api_gateway.config.FeignClientConfig;

import reactor.core.publisher.Mono;

// a Feign client to interact with the user management service
@FeignClient(name = "user-management-service", url = "http://user-management-service:8080", configuration = FeignClientConfig.class)
public interface UserClient 
{
    @GetMapping("/users/validate-api-key")
    ResponseEntity<Boolean> validateApiKey(@RequestParam("apiKey") String apiKey);
}