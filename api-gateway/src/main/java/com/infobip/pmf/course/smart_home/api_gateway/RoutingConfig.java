package com.infobip.pmf.course.smart_home.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import com.infobip.pmf.course.smart_home.api_gateway.feignclient.UserClient;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class RoutingConfig 
{
    //@Autowired
    //@Lazy // delay initialization to break the cycle of dependencies
   
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    @Autowired
    public RoutingConfig(ApiKeyAuthFilter apiKeyAuthFilter)
    {
        this.apiKeyAuthFilter = apiKeyAuthFilter;
    }

    // each service runs internally on container port 8080 
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) 
    {
        return builder.routes()
                .route("user-registration", r -> r.path("/users/register")
                        .uri("http://user-management-service:8080")) // No filter for registration
                .route("user-management-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(apiKeyAuthFilter))
                        .uri("http://user-management-service:8080"))
                .route("device-management-service", r -> r.path("/devices/**")
                        .filters(f -> f.filter(apiKeyAuthFilter))
                        .uri("http://device-management-service:8080"))
                .build();
    }
}

