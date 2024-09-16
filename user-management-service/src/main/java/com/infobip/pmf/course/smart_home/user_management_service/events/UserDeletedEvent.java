package com.infobip.pmf.course.smart_home.user_management_service.events;

public class UserDeletedEvent   // simple event class
{
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
