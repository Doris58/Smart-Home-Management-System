package com.infobip.pmf.course.smart_home.notification_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// call the user management service to check if a user exists
@FeignClient(name = "user-management-service", url = "http://user-management-service:8080")
public interface UserClient 
{
    @GetMapping("/users/check-existence")
    ResponseEntity<Boolean> checkUserExistsByEmail(@RequestParam("email") String email);
}
