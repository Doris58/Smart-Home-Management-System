package com.infobip.pmf.course.smart_home.notification_service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.infobip.pmf.course.smart_home.notification_service.DeviceDTO;
import com.infobip.pmf.course.smart_home.notification_service.events.DeviceStatusChangedEvent;
import com.infobip.pmf.course.smart_home.notification_service.events.UserDeletedEvent;
import com.infobip.pmf.course.smart_home.notification_service.feignclient.DeviceClient;
import com.infobip.pmf.course.smart_home.notification_service.feignclient.UserClient;
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

    @Autowired
    private UserClient userClient;

    // listener for DeviceStatusChangedEvent that triggers the notification for each asssociated user
    @RabbitListener(queues = "device.status.changed.queue")
    public void handleDeviceStatusChanged(DeviceStatusChangedEvent event) 
    {
        Long deviceId = event.getDeviceId();
        logger.info("Received DeviceStatusChangedEvent for deviceId: {}", deviceId);

        // handle event - send notification to all associated users
        String message = "Device " + event.getDeviceName() + " status " + event.getOldStatus() + "changed to " + event.getNewStatus();
        String title = "Device Status Update"; // I CAN SEND ALSO THE PREVIOUS STATUS HERE!
        // title for push, for email for ex. subject

        // send push notification to all associated users
        //notificationService.sendPushNotificationToUsers(event.getUserIds(), title, message);

        try
        {
            // Check if the device still exists before proceeding
            ResponseEntity<DeviceDTO> deviceResponse = deviceClient.getDeviceById(deviceId);
    
            if(deviceResponse.getStatusCode().is2xxSuccessful() && deviceResponse.getBody() != null) 
            {
                logger.info("Device with deviceId: {} exists, proceeding with notification.", deviceId);

                // Fetch associated emails directly in the notification service by the deviceId !!
                ResponseEntity<List<String>> associatedEmailsResponse = deviceClient.getAssociatedUserEmailsByDeviceId(event.getDeviceId());

                if(associatedEmailsResponse.getBody() != null) 
                {
                    List<String> associatedEmails = associatedEmailsResponse.getBody();
            
                    // Iterate through each email and check if the user still exists before sending notification
                    for(String email : associatedEmails) 
                    {
                        // Perform a final check to ensure the user still exists 
                        ResponseEntity<Boolean> userExistsResponse = userClient.checkUserExistsByEmail(email);

                        if(userExistsResponse.getBody() != null && userExistsResponse.getBody()) 
                        {
                            // Send the notification if the user exists
                            notificationService.sendEmailNotificationToUser(email, title, message);
                            logger.info("Notification sent successfully to {}", email);
                        } 
                        else 
                        {
                            logger.warn("Skipping notification for {}, user no longer exists.", email);
                        }
                    }
                }
                logger.info("All notifications processed for DeviceStatusChangedEvent for deviceId: {}", deviceId);
            }
            else 
            {
                // Log if the device no longer exists
                logger.warn("Device with deviceId: {} no longer exists, skipping notification.", deviceId);
            }
        }    
        catch(Exception e) 
        {
            logger.error("Failed to send notifications for DeviceStatusChangedEvent for deviceId: {}", deviceId, e);
        }
    }

    // listens to the queue user.deleted.queue and processes the event when received
    /* @Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2)) */
    @RabbitListener(queues = "notification.user.deleted.queue") 
    public void handleUserDeleted(UserDeletedEvent event) 
    {
        Long userId = event.getUserId();  
        List<String> associatedEmails = event.getAssociatedEmails();  

        //System.out.println("Received UserDeletedEvent for userId: " + userId);
        logger.info("Received UserDeletedEvent for userId: {}", userId);

        // handle event - send email notification to all users associated with the devices controlled by the deleted user
        String message = "User with ID " + userId + " has been deleted.";
        String title = "User Deleted";

        try 
        {
            // Iterate through the associated emails
            for(String email : associatedEmails) 
            {
                // Perform a final check to ensure the user still exists 
                ResponseEntity<Boolean> response = userClient.checkUserExistsByEmail(email);

                if(response.getBody() != null && response.getBody()) 
                {
                    // Send notification if the user still exists
                    notificationService.sendEmailNotificationToUser(email, title, message);
                    logger.info("Notification sent successfully to {}", email);
                } 
                else 
                {
                    logger.warn("Skipping notification for {}, user no longer exists.", email);
                }
            }

            logger.info("All notifications processed for UserDeletedEvent for userId: {}", userId);
        } 
        catch(Exception e) 
        {
            logger.error("Failed to send notifications for UserDeletedEvent for userId: {}", userId, e);
        }
    }
}

