package com.infobip.pmf.course.smart_home.device_management_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infobip.pmf.course.smart_home.device_management_service.feignclient.UserClient;
import com.infobip.pmf.course.smart_home.device_management_service.model.Device;
import com.infobip.pmf.course.smart_home.device_management_service.model.DeviceDTO;
import com.infobip.pmf.course.smart_home.device_management_service.model.UserDTO;
import com.infobip.pmf.course.smart_home.device_management_service.service.DeviceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/devices")
public class DeviceController 
{
    @Autowired
    private DeviceService deviceService;

    // Create a new device
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) 
    {
        return ResponseEntity.ok(deviceService.createDevice(device));
    }

    // Get all devices
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() 
    {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    // Get a device by ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) 
    {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update device details
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device deviceDetails) 
    {
        return deviceService.updateDevice(id, deviceDetails)
                .map(ResponseEntity::ok)  // dS.updateDevice needs to return Optinal<Device>, not Device
                .orElse(ResponseEntity.notFound().build());
    }

    // Associate a user to a device
    @PostMapping("/{deviceId}/users/{userId}/associate")
    public ResponseEntity<String> associateUserToDevice(@PathVariable Long deviceId, @PathVariable Long userId) 
    {
        deviceService.associateUserToDevice(deviceId, userId);
        return ResponseEntity.ok("User associated with device successfully.");
    }

    // Disassociate a user from a device
    @DeleteMapping("/{deviceId}/users/{userId}/disassociate")
    public ResponseEntity<String> disassociateUserFromDevice(@PathVariable Long deviceId, @PathVariable Long userId) 
    {
        deviceService.disassociateUserFromDevice(deviceId, userId);
        return ResponseEntity.ok("User disassociated from device successfully.");
    }

    // Delete a device
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) 
    {
        if(deviceService.getDeviceById(id).isPresent()) 
        {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok().build();
        } 
        else 
        {
            return ResponseEntity.notFound().build();
        }
    }

    // Get the emails of users associated with the devices controlled by a user, for ex. DeviceClient in notification service needs it
    @GetMapping("/users/{userId}/associated-emails")
    public List<String> getAssociatedUserEmails(@PathVariable Long userId) 
    {
        return deviceService.getEmailsByUserId(userId);
    }

    // Get the devices associated with a user, for ex. DeviceClient in the user management service needs it
    @GetMapping("/user/{userId}")
    public List<DeviceDTO> getDevicesByUserId(@PathVariable Long userId) 
    {
        List<Device> devices = deviceService.getDevicesByUserId(userId);
        return devices.stream()
                      .map(device -> new DeviceDTO(device.getId(), device.getName(), device.getStatus(), device.getLocation()))
                      .collect(Collectors.toList());
    }
}
