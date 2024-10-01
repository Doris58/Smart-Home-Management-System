package com.infobip.pmf.course.smart_home.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
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
        // Call the Feign client and get the ResponseEntity<Boolean>
        ResponseEntity<Boolean> response = userClient.validateApiKey(apiKey);
    
        // Check if the response status is 2xx (successful) and if the body is not null
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) 
        {
            // Return the boolean value from the response body
            return response.getBody();
        }
    
        // If something goes wrong (non-2xx status or null body), return false
        return false;
    }
}
