package com.infobip.pmf.course.smart_home.notification_service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.infobip.pmf.course.smart_home.notification_service.events.DeviceStatusChangedEvent;
import com.infobip.pmf.course.smart_home.notification_service.events.UserDeletedEvent;
import com.infobip.pmf.course.smart_home.notification_service.feignclient.DeviceClient;
import com.infobip.pmf.course.smart_home.notification_service.service.NotificationService;

import jakarta.annotation.PostConstruct;

import java.util.List;

// listens to RabbitMQ messages when events occur
// consumes events and triggers notifications
@Service
public class NotificationEventListener 
{
    private static final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DeviceClient deviceClient;

    // listener for DeviceStatusChangedEvent that triggers the notification for each asssociated user
    @RabbitListener(queues = "device.status.changed.queue")
    public void handleDeviceStatusChanged(DeviceStatusChangedEvent event) 
    {
        logger.info("Received DeviceStatusChangedEvent for deviceId: {}", event.getDeviceId());
        
        // handle event - send notification to all associated users
        String message = "Device " + event.getDeviceName() + " status " + event.getOldStatus() + "changed to " + event.getNewStatus();
        String title = "Device Status Update"; // I CAN SEND ALSO THE PREVIOUS STATUS HERE!
        // title for push, for email for ex. subject

        // send push notification to all associated users
        //notificationService.sendPushNotificationToUsers(event.getUserIds(), title, message);

        try
        {
            // send email notification to all associated users
            notificationService.sendEmailNotificationToUsers(event.getUserEmails(), title, message);

            logger.info("Notifications sent successfully for DeviceStatusChangedEvent for deviceId: {}", event.getDeviceId());
        }
        catch(Exception e)
        {
            logger.error("Failed to send notifications for DeviceStatusChangedEvent for deviceId: {}", event.getDeviceId(), e);
        } 
    }

    // listens to the queue user.deleted.queue and processes the event when received
    @Retryable(
    value = { Exception.class }, 
    maxAttempts = 5, 
    backoff = @Backoff(delay = 2000, multiplier = 2))
    @RabbitListener(queues = "notification.user.deleted.queue")
    public void handleUserDeleted(UserDeletedEvent event) 
    {
        Long userId = event.getUserId();  // the only event data

        //System.out.println("Received UserDeletedEvent for userId: " + userId);
        logger.info("Received UserDeletedEvent for userId: {}", userId);

        // handle event - send email notification to all users associated with the devices controlled by the deleted user
        String message = "User with ID " + userId + " has been deleted.";
        String title = "User Deleted";

        try 
        {
            // fetch associated user emails
            List<String> associatedEmails = deviceClient.getAssociatedUserEmails(userId);

            // send email notification to all users associated with the devices controlled by the deleted user
            notificationService.sendEmailNotificationToUsers(associatedEmails, title, message);
            
            logger.info("Notifications sent successfully for UserDeletedEvent for userId: {}", userId);
        } 
        catch(Exception e) 
        {
            logger.error("Failed to send notifications for UserDeletedEvent for userId: {}", userId, e);
            throw e;  // Retry on failure
        }
    }
}

