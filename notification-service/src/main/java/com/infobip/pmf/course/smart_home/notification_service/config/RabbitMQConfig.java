package com.infobip.pmf.course.smart_home.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// define queues, exchanges, and bindings for events like **user deletion or device creation**

@Configuration
public class RabbitMQConfig 
{
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

    @Bean
    public TopicExchange userExchange() 
    {
        return new TopicExchange("userExchange");
    }

    // Define the queue for the user deletion event
    @Bean
    public Queue userDeletedQueue() 
    {
        return new Queue("notification.user.deleted.queue");
    }

    // Bind the queue to the exchange with a routing key
    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, TopicExchange userExchange) 
    {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with("notification.user.deleted");
    }

    // ˇˇ

    @Bean
    public TopicExchange deviceExchange() 
    {
        return new TopicExchange("deviceExchange");
    }

    // Define the queue for the device status changed event
    // This queue will hold the DeviceStatusChangedEvent messages
    @Bean
    public Queue deviceStatusChangedQueue() 
    {
        return new Queue("device.status.changed.queue");
    }

    // Bind the queue to the exchange with a routing key
    @Bean
    public Binding bindingDeviceStatusChanged(Queue deviceStatusChangedQueue, TopicExchange deviceExchange) 
    {
        return BindingBuilder.bind(deviceStatusChangedQueue).to(deviceExchange).with("device.status.changed");
    }

    // ^^ This configuration ensures that when a DeviceStatusChangedEvent is published, 
    // it will be sent to the device.status.changed.queue for consumption by the notification service

    /* 
    // when a new device is added to the system, send a notification about the successful creation of the device
    @Bean
    public Queue deviceCreatedQueue() {
    return new Queue("device.created.queue");
    }

    @Bean
    public Binding bindingDeviceCreated(Queue deviceCreatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(deviceCreatedQueue).to(userExchange).with("device.created");
    }

    @Bean
    public Queue securityAlertQueue() 
    {
        return new Queue("security.alert.queue");
    }

    @Bean
    public Binding bindingSecurityAlert(Queue securityAlertQueue, TopicExchange userExchange) 
    {
        return BindingBuilder.bind(securityAlertQueue).to(userExchange).with("security.alert");
    }
    */
}
