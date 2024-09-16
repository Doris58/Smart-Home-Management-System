
package com.infobip.pmf.course.smart_home.device_management_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.infobip.pmf.course.smart_home.device_management_service.model.UserDTO;

@FeignClient(name = "user-management-service")
public interface UserClient 
{
    @GetMapping("/users/{id}")                    
    UserDTO getUserById(@PathVariable Long id);
    // UM Service returns data in a format that aligns with the UserDTO
}
