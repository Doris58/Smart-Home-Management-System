package com.infobip.pmf.course.smart_home.notification_service.sender.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfobipSenderConfiguration 
{
    // @Value annots inject properties directly from application.properties

    @Value("${infobip.api.base-url}")
    private String baseUrl;

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.api.from}")
    private String from;

    @Value("${infobip.api.to}")
    private String to;

    public String getBaseUrl() 
    {
        return baseUrl;
    }

    public String getApiKey() 
    {
        return apiKey;
    }

    public String getFrom() 
    {
        return from;
    }

    public String getTo() 
    {
        return to;
    }
}
