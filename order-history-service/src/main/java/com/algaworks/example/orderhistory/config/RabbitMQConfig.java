package com.algaworks.example.orderhistory.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {


    public static final String ORDER_EXCHANGE_NAME = "order.v1.events";
    public static final String ORDER_HISTORY_QUEUE_NAME = "history.v1.order.events";

    public static final String ORDER_EVENTS_ROUTING_KEY = "order.#";


    @Bean
    public Queue queue()
    {
        return new Queue(ORDER_HISTORY_QUEUE_NAME);
    }

    @Bean
    public Binding binding()
    {
        TopicExchange topicExchange = new TopicExchange(ORDER_EXCHANGE_NAME);
        Queue queue = new Queue(ORDER_HISTORY_QUEUE_NAME);

        return BindingBuilder.bind(queue).to(topicExchange).with(ORDER_EVENTS_ROUTING_KEY);

    }



    //to make this application to create a new queue after the project initialize
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory)
    {
        return new RabbitAdmin(connectionFactory);
    }


    //last step to create the queue after the initialization process
    //that waits for spring after an initialization
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {

        return event ->  rabbitAdmin.initialize();
    }


    //complex objects need to be converted into JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter)
    {

        RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
