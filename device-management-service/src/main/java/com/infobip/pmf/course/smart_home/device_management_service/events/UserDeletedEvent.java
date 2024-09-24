package com.infobip.pmf.course.smart_home.device_management_service.events;

import java.io.Serializable;

public class UserDeletedEvent implements Serializable   // simple event class
{
    private static final long serialVersionUID = 2L;
    
    private Long userId;  // data relevant to the event

    public UserDeletedEvent() {}

    public UserDeletedEvent(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }
}
