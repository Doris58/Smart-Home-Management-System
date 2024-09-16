package com.infobip.pmf.course.smart_home.api_gateway.events;

public class ApiKeyValidationRequestEvent 
{
    private final String apiKey;

    public ApiKeyValidationRequestEvent(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getApiKey() 
    {
        return apiKey;
    }
}

