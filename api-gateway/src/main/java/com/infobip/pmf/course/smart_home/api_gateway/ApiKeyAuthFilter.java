package com.infobip.pmf.course.smart_home.api_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

//import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationEvent;
import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationRequestEvent;
import com.infobip.pmf.course.smart_home.api_gateway.events.ApiKeyValidationResponseEvent;
import com.infobip.pmf.course.smart_home.api_gateway.feignclient.UserClient;

import java.time.Duration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;

import reactor.core.publisher.Mono;

@Component
public class ApiKeyAuthFilter implements GatewayFilter 
{
    private boolean isApiKeyValid;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleApiKeyValidationResponse(ApiKeyValidationResponseEvent event) 
    {
        isApiKeyValid = event.isValid();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) 
    {
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");

        if(apiKey != null) 
        {
            eventPublisher.publishEvent(new ApiKeyValidationRequestEvent(apiKey));

            // Simulating a delay to wait for the response (could be a different approach in real apps)
            return Mono.defer(() -> 
            {
                if(isApiKeyValid) 
                {
                    return chain.filter(exchange);
                } 
                
                // If the API key is not valid, reject the request
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
                
            }).delaySubscription(Duration.ofMillis(200)); // Temporary delay to simulate async event processing
        }

        // If no API key is present, reject the request
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    /* 
    private boolean isValidApiKey(String apiKey)
    {
        // call user management service to validate API key
        return userClient.validateApiKey(apiKey);
        // validate API key against the database
        //return userRepository.findByApiKey(apiKey).isPresent();
    }
    */

}
