package com.infobip.pmf.course.smart_home.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infobip.pmf.course.smart_home.notification_service.sender.email.InfobipEmailSender;
import com.infobip.pmf.course.smart_home.notification_service.sender.push.InfobipSender;

import java.util.List;

@Service
public class NotificationService 
{
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private InfobipSender infobipSender;

    @Autowired
    private InfobipEmailSender infobipEmailSender;

    public void sendPushNotificationToUsers(List<Long> userIds, String title, String message) 
    {
        for(Long userId : userIds) 
        {
            //infobipSender.send(userId, title, message);  // Send push notification to user
        }
    }

    public void sendEmailNotificationToUsers(List<String> userEmails, String subject, String message) 
    {
        for(String userEmail : userEmails) 
        {
            // Send email notification to a user
            infobipEmailSender.sendEmail(userEmail, subject, message);  
        }
    }
}
