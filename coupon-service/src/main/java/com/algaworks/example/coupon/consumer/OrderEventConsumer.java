package com.algaworks.example.coupon.consumer;


import com.algaworks.example.coupon.config.RabbitMQConfig;
import com.algaworks.example.coupon.event.OrderPaidEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {


    @RabbitListener(queues = RabbitMQConfig.COUPON_GENERATE_BASIC_QUEUE_NAME)
    public void onOrderPaidGenerateBasicCoupon(OrderPaidEvent event)
    {
        System.out.println("Venda recebida de um cliente basic  da venda: "+ event.getOrder().getId());
    }

    @RabbitListener(queues = RabbitMQConfig.COUPON_GENERATE_VIP_QUEUE_NAME)
    public void onOrderPaidGenerateVipCoupon(OrderPaidEvent event)
    {
        System.out.println("Venda recebida de um cliente vip  da venda: "+ event.getOrder().getId());
    }


    @RabbitListener(queues = RabbitMQConfig.COUPON_CANCEL_QUEUE_NAME)
    public void onOrderCancel(OrderPaidEvent event)
    {
        System.out.println("Venda cancelada recebida de um cliente  id: "+ event.getOrder().getId());
    }
}
