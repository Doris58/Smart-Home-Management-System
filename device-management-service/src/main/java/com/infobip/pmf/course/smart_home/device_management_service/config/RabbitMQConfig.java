package com.infobip.pmf.course.smart_home.device_management_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// this configuration will define the queue and binding to listen to events
// listens for messages published to the userExchange using the user.deleted.queue queue
@Configuration
public class RabbitMQConfig 
{
    // define the exchange responsible for publishing 
    // all the device-related events
    // define the exchange deviceExchange where events are published
    @Bean
    public TopicExchange deviceExchange() 
    {
        return new TopicExchange("deviceExchange");
    }

    // define the queue and binding to listen to events
    // listens for messages published to the userExchange using the user.deleted.queue queue
    @Bean
    public Queue userDeletedQueue() 
    {
        return new Queue("user.deleted.queue");
    }

    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, TopicExchange userExchange) 
    {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with("user.deleted");
    }
    // By having these configurations, the User Management Service 
    // will publish UserDeletedEvent to userExchange, and the Device Management Service 
    // will consume those events from user.deleted.queue.

    // *** ˇˇˇ *** //

    @Bean
    public TopicExchange userExchange() 
    {
        return new TopicExchange("userExchange");
    }

    @Bean
    public Queue deviceStatusChangedQueue() 
    {
        return new Queue("device.status.changed.queue");
    }

    @Bean
    public Binding bindingDeviceStatusChanged(Queue deviceStatusChangedQueue, TopicExchange deviceExchange) 
    {
        return BindingBuilder.bind(deviceStatusChangedQueue).to(deviceExchange).with("device.status.changed");
    }
}

