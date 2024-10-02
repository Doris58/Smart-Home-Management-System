package com.infobip.pmf.course.smart_home.device_management_service.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infobip.pmf.course.smart_home.device_management_service.feignclient.UserClient;
import com.infobip.pmf.course.smart_home.device_management_service.model.Device;
import com.infobip.pmf.course.smart_home.device_management_service.model.UserDTO;
import com.infobip.pmf.course.smart_home.device_management_service.model.UserDeviceAssociation;
import com.infobip.pmf.course.smart_home.device_management_service.repository.DeviceRepository;
import com.infobip.pmf.course.smart_home.device_management_service.repository.UserDeviceAssociationRepository;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

import com.infobip.pmf.course.smart_home.device_management_service.events.DeviceStatusChangedEvent;
import com.infobip.pmf.course.smart_home.device_management_service.exception.ResourceNotFoundException;

import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class DeviceService 
{
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserDeviceAssociationRepository userDeviceAssociationRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void configureRabbitTemplate(RabbitTemplate rabbitTemplate, Jackson2JsonMessageConverter converter) 
    {
        rabbitTemplate.setMessageConverter(converter);
    }

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    private final MeterRegistry meterRegistry;

    @Autowired
    public DeviceService(MeterRegistry meterRegistry) 
    {
        this.meterRegistry = meterRegistry;
        
        // Register the gauge metric
        Gauge.builder("active_devices", this, DeviceService::getActiveDevices)
             .description("Number of active devices")
             .register(meterRegistry);
    }

    private int activeDevices = 0;

    public int getActiveDevices() 
    {
        return activeDevices;
    }

    @PostConstruct // Spring will run this after the service is instantiated and dependencies are injected
    public void initializeActiveDevices() 
    {
        // Query the repository to count active devices at the start
        activeDevices = (int)deviceRepository.countByStatus("active");
    }

    // Create a new device
    public Device createDevice(Device device) 
    {
        Device newDevice = deviceRepository.save(device);

        if ("active".equals(newDevice.getStatus())) 
        {
            activeDevices++;
        }

        return newDevice;
    }

    // Get all devices
    public List<Device> getAllDevices() 
    {
        return deviceRepository.findAll();
    }

    // Get a device by ID
    public Optional<Device> getDeviceById(Long id) 
    {
        return deviceRepository.findById(id);
    }

    // Update device details
    public Optional<Device> updateDevice(Long id, Device deviceDetails) 
    {
        //Device device = deviceRepository.findById(id)
        //.orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + id));

        // this returns Optional<Device>:
        return deviceRepository.findById(id)
        .map(device -> 
        {
            String previousStatus = device.getStatus();

            // Perform the update
            device.setName(deviceDetails.getName());
            device.setStatus(deviceDetails.getStatus());
            device.setLocation(deviceDetails.getLocation());

            Device updatedDevice = deviceRepository.save(device);

            String newStatus = updatedDevice.getStatus();

            if(!previousStatus.equals(newStatus)) 
            {
                // Publish the DeviceStatusChangedEvent event to RabbitMQ
                // publishDeviceStatusChangedEvent(updatedDevice); // without notifying about old status
                publishDeviceStatusChangedEvent(updatedDevice, previousStatus);

                if(previousStatus.equals("inactive") && newStatus.equals("active"))
                    activeDevices++;
                if(previousStatus.equals("active") && newStatus.equals("inactive"))
                    activeDevices--;
            }

            return updatedDevice;
        });
    }

    // Publish the DeviceStatusChangeEvent event to RabbitMQ
    private void publishDeviceStatusChangedEvent(Device device, String oldStatus) 
    {
        // event setters - ok, + default constructor; or use paramterized constructor
        DeviceStatusChangedEvent event = new DeviceStatusChangedEvent();
        event.setDeviceId(device.getId());
        event.setDeviceName(device.getName());
        event.setNewStatus(device.getStatus());
        event.setOldStatus(oldStatus);

        rabbitTemplate.convertAndSend("deviceExchange", "device.status.changed", event);

        // track when the event is published
        logger.info("Published DeviceStatusChangedEvent for deviceId: {}", device.getId());
    }

    // Associate a user to a device
    public void associateUserToDevice(Long deviceId, Long userId) 
    {
        // Check if the device exists
        Device device = deviceRepository.findById(deviceId)
        .orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));

        /*
        // Check if the user exists by calling the UM service and fetch the user by userId
        UserDTO user = userClient.getUserById(userId);  // userClient is a Feign client
        if (user == null) 
        {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        */

        // Check if the user exists by calling the UM service and fetch the user by userId
        ResponseEntity<UserDTO> response = userClient.getUserById(userId);
        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
        {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    
        UserDTO user = response.getBody();  // Not needed
        
        // Create the user-device association
        UserDeviceAssociation association = new UserDeviceAssociation(userId, deviceId);

        // Save the association in the device entity
        device.getUserAssociations().add(association);  // getters usage - ok; adds the association to the list
        deviceRepository.save(device);  // persists both device and association
    }

    // Disassociate a user from a device - brise bas tu konkretnu vezu iz baze
    public void disassociateUserFromDevice(Long deviceId, Long userId) 
    {
        UserDeviceAssociation association = userDeviceAssociationRepository.findByUserIdAndDeviceId(userId, deviceId);
        if (association != null) 
        {
            userDeviceAssociationRepository.delete(association);

            Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));

            // The @OneToMany relationship is not bidirectional by default -->
            // Manually remove the association from the device entity's list
            device.getUserAssociations().remove(association);
            deviceRepository.save(device);  // persists the change
        }
    }

    // Delete a device 
    public void deleteDevice(Long id) 
    {
        Optional<Device> device = deviceRepository.findById(id);
        if(device.isPresent() && "active".equals(device.get().getStatus())) 
        {
            activeDevices--;
        }
   
        deviceRepository.deleteById(id);
    }

    // Disassociate devices when a user is deleted
    @Transactional // wrapping the method in a transactional context; db op exec as a single transaction
    public void handleUserDeletion(Long userId) // OR "disassociateDevices"
    {
        List<UserDeviceAssociation> associations = userDeviceAssociationRepository.findByUserId(userId);
        for(UserDeviceAssociation association : associations) 
        {
            // Delete the association - disassociate device from user 
            userDeviceAssociationRepository.delete(association); 
        }
    }

    // Moved from the publishDeviceStatusChangedEvent method
    public List<String> getAssociatedUserEmailsByDeviceId(Long deviceId) 
    {
        // Fetch the user-device associations for the given device ID
        List<UserDeviceAssociation> associations = userDeviceAssociationRepository.findByDeviceId(deviceId);

        // Extract the user emails from the associations
        return associations.stream()
                .map(association -> 
                {
                    ResponseEntity<UserDTO> response = userClient.getUserById(association.getUserId());
                    UserDTO userDTO = response.getBody();
                    return userDTO != null ? userDTO.getEmail() : null;
                })
                .filter(Objects::nonNull) // Filter out null emails
                .collect(Collectors.toList());
    }
 
    // Get the emails of users associated with the devices controlled by a user
    public List<String> getEmailsByUserId(Long userId) 
    {
        List<Long> associatedUserIds = userDeviceAssociationRepository.findAssociatedUserIdsByUserId(userId);
        List<String> emails = new ArrayList<>();

        for (Long associatedUserId : associatedUserIds) 
        {
            logger.info("Checking user existence for userId: {}", associatedUserId);

            // Fetch the user details from User Management Service
            ResponseEntity<UserDTO> response = userClient.getUserById(associatedUserId);
            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
            {
                emails.add(response.getBody().getEmail());
            }
            else
            {
                logger.warn("User with userId {} not found in user-management-service.", associatedUserId);
            }
        }

        return emails;
    }

    /* 
    public List<String> getEmailsByUserId(Long userId) 
    {
        List<Long> associatedUserIds = userDeviceAssociationRepository.findAssociatedUserIdsByUserId(userId);
        List<String> emails = new ArrayList<>();

        for(Long associatedUserId : associatedUserIds) 
        {
            // Call to User Management Service to fetch user details
            UserDTO user = userClient.getUserById(associatedUserId);  
            emails.add(user.getEmail());
        }

        return emails;
    }
    */

    // Get the devices associated with a user
    public List<Device> getDevicesByUserId(Long userId) 
    {
        List<UserDeviceAssociation> associations = userDeviceAssociationRepository.findByUserId(userId);
        List<Long> deviceIds = associations.stream()
                                           .map(UserDeviceAssociation::getDeviceId)
                                           .collect(Collectors.toList());
        return deviceRepository.findAllById(deviceIds);
    }
}

