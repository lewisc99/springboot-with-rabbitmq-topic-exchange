package com.algaworks.example.coupon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static final String ORDER_CANCEL_ROUTING_KEY = "order.*.cancel";

    private static final String ORDER_PAID_BASIC_ROUTING_KEY = "order.basic.paid";
    private static final String ORDER_PAID_VIP_ROUTING_KEY = "order.vip.paid";
    public static final String ORDER_EXCHANGE_NAME = "order.v1.events";


    public static final String COUPON_GENERATE_VIP_QUEUE_NAME = "coupon.v1.on-order-paid-vip.generate-coupon";
    public static final String COUPON_GENERATE_BASIC_QUEUE_NAME = "coupon.v1.on-order-basic-vip.generate-coupon";
    public static final String COUPON_CANCEL_QUEUE_NAME = "coupon.v1.on-order-cancel.cancel-coupon";



    @Bean
    public Queue queueCouponGenerateBasic()
    {
        return new Queue(COUPON_GENERATE_BASIC_QUEUE_NAME);
    }


    @Bean
    public Queue queueCouponGenerateVip()
    {
        return new Queue(COUPON_GENERATE_VIP_QUEUE_NAME);
    }

    @Bean
    public Queue queueCouponCancel()
    {
        return new Queue(COUPON_CANCEL_QUEUE_NAME);
    }

    @Bean
    public Binding bindingGenerateCouponBasic()
    {
        Queue queue = new Queue(COUPON_GENERATE_BASIC_QUEUE_NAME);
        TopicExchange exchange = new TopicExchange(ORDER_EXCHANGE_NAME);

        return BindingBuilder.bind(queue).to(exchange).with(ORDER_PAID_BASIC_ROUTING_KEY);
    }

    @Bean
    public Binding bindingGenerateCouponVip()
    {
        Queue queue = new Queue(COUPON_GENERATE_VIP_QUEUE_NAME);
        TopicExchange exchange = new TopicExchange(ORDER_EXCHANGE_NAME);

        return BindingBuilder.bind(queue).to(exchange).with(ORDER_PAID_VIP_ROUTING_KEY);
    }

    @Bean
    public Binding BindingGenerateCouponCancel()
    {
        Queue queue = new Queue(COUPON_CANCEL_QUEUE_NAME);
        TopicExchange exchange = new TopicExchange(ORDER_EXCHANGE_NAME);

        return BindingBuilder.bind(queue).to(exchange).with(ORDER_CANCEL_ROUTING_KEY);
    }



    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
