package com.infobip.pmf.course.smart_home.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

//import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationEvent;
import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationRequestEvent;
import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationResponseEvent;
import com.infobip.pmf.course.smart_home.api_gateway.feignclient.UserClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ApiKeyValidationService 
{
    @Autowired
    private UserClient userClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleApiKeyValidationRequest(ApiKeyValidationRequestEvent event) 
    {
        String apiKey = event.getApiKey();

        // Validate the API key (call your validation logic or Feign client)
        boolean isValid = validateApiKey(apiKey);

        // Publish the result of the validation
        eventPublisher.publishEvent(new ApiKeyValidationResponseEvent(isValid));
    }
 
    public boolean validateApiKey(String apiKey) 
    {
        boolean isValid = userClient.validateApiKey(apiKey);
        return isValid;
    }
}
