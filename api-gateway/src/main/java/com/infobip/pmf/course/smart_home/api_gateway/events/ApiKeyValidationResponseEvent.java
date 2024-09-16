package com.infobip.pmf.course.smart_home.api_gateway.events;

public class ApiKeyValidationResponseEvent 
{
    private final boolean isValid;

    public ApiKeyValidationResponseEvent(boolean isValid) 
    {
        this.isValid = isValid;
    }

    public boolean isValid() 
    {
        return isValid;
    }
}