package com.infobip.pmf.course.smart_home.device_management_service.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig 
{
    // allow Spring to serialize complex objects like DeviceStatusChangedEvent into JSON format
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() 
    {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) 
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    // define the exchange responsible for publishing 
    // all the device-related events
    // define the exchange deviceExchange where events are published
    @Bean
    public TopicExchange deviceExchange() 
    {
        return new TopicExchange("deviceExchange");
    }

    @Bean
    public TopicExchange userExchange() 
    {
        return new TopicExchange("userExchange");
    }

    // define the queue and binding to listen to events
    // listens for messages published to the userExchange using the user.deleted.queue queue
    @Bean
    public Queue userDeletedQueue() 
    {
        return new Queue("device.user.deleted.queue");
    }

    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, TopicExchange userExchange) 
    {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with("device.user.deleted");
    }
    // By having these configurations, the User Management Service 
    // will publish UserDeletedEvent to userExchange, and the Device Management Service 
    // will consume those events from user.deleted.queue

   /* 
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
    */
}

