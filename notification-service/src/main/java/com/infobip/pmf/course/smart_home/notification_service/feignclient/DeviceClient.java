package com.infobip.pmf.course.smart_home.notification_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.infobip.pmf.course.smart_home.notification_service.DeviceDTO;

import java.util.List;

// communicate with the device ms
@FeignClient(name = "device-management-service", url = "http://device-management-service:8080")
public interface DeviceClient 
{   
    /* Moved to the user management service! */
    // fetch the emails of users associated with devices controlled by the deleted user
    //@GetMapping("/devices/users/{userId}/associated-emails")
    //List<String> getAssociatedUserEmails(@PathVariable Long userId);

    @GetMapping("/devices/{deviceId}/associated-emails")
    ResponseEntity<List<String>> getAssociatedUserEmailsByDeviceId(@PathVariable("deviceId") Long deviceId);

    @GetMapping("/devices/{id}")
    ResponseEntity<DeviceDTO> getDeviceById(@PathVariable Long id);
}
