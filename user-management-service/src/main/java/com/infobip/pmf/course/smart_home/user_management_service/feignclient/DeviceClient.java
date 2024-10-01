package com.infobip.pmf.course.smart_home.user_management_service.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.infobip.pmf.course.smart_home.user_management_service.model.DeviceDTO;

// communicate with the dm service
@FeignClient(name = "device-management-service", url = "http://device-management-service:8080")   //http://localhost:8081
public interface DeviceClient 
{
    // fetch the devices (relevant data) associated with a user
    @GetMapping("/devices/user/{userId}")
    List<DeviceDTO> getDevicesByUserId(@PathVariable("userId") Long userId);

    // fetch the emails of users associated with devices controlled by the deleted user, before deletion!
    @GetMapping("/devices/users/{userId}/associated-emails")
    List<String> getAssociatedUserEmails(@PathVariable Long userId);
}
