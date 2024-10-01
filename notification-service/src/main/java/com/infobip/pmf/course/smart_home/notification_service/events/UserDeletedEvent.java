package com.infobip.pmf.course.smart_home.notification_service.events;

import java.io.Serializable;
import java.util.List;

public class UserDeletedEvent  implements Serializable
{
    private static final long serialVersionUID = 2L;

    // data relevant to the event
    private Long userId; 
    private List<String> associatedEmails; 

    public UserDeletedEvent() {}

    public UserDeletedEvent(Long userId, List<String> associatedEmails) 
    {
        this.userId = userId;
        this.associatedEmails = associatedEmails;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public List<String> getAssociatedEmails() 
    {
        return associatedEmails;
    }
}
