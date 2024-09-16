package com.infobip.pmf.course.smart_home.user_management_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// this configuration will define the exchange and be responsible for publishing 
// all the user-related events
// defines the exchange userExchange where events are published
@Configuration
public class RabbitMQConfig 
{
    @Bean
    public TopicExchange userExchange() 
    {
        return new TopicExchange("userExchange");
    }

    // **** ˇˇˇ **** //

    // Define the queue for the user deletion event
    @Bean
    public Queue userDeletedQueue() 
    {
        return new Queue("user.deleted.queue");
    }

    // Bind the queue to the exchange with a routing key
    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, TopicExchange userExchange) 
    {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with("user.deleted");
    }
}
