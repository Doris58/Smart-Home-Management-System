package com.infobip.pmf.course.smart_home.device_management_service.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties.Logging;
import org.springframework.stereotype.Service;

import com.infobip.pmf.course.smart_home.device_management_service.events.UserDeletedEvent;
import com.infobip.pmf.course.smart_home.device_management_service.service.DeviceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// handles events related to devices

@Service
public class DeviceEventListener 
{
    // a logger that records when the event is received and when the corresponding action is performed
    private static final Logger logger = LoggerFactory.getLogger(DeviceEventListener.class);

    @Autowired
    private DeviceService deviceService;

    @RabbitListener(queues = "user.deleted.queue")
    public void handleUserDeleted(UserDeletedEvent event) 
    {
        Long userId = event.getUserId();

        // Logging the Event Reception
        // The {} in the logging strings are placeholders used by the SLF4J logging framework
        logger.info("Received UserDeletedEvent for userId: {}", userId);

        //deviceService.handleUserDeletion(event.getUserId());
        try 
        {
            // Handle device disassociation
            deviceService.handleUserDeletion(userId);
            // Logging After Handling
            logger.info("Successfully disassociated devices for userId: {}", userId);
        } 
        catch (Exception e) 
        {
            // Logging Errors: logs the error, including the stack trace
            logger.error("Error processing UserDeletedEvent (disasocciating devices) for userId: {}", userId, e);
        }
    }
}