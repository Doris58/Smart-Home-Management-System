package com.infobip.pmf.course.smart_home.user_management_service.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// this configuration will define the exchange and be responsible for publishing 
// all the user-related events
// defines the exchange userExchange where events are published
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
}
