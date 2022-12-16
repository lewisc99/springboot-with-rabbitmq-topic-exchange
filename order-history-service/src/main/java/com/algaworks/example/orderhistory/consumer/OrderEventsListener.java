package com.algaworks.example.orderhistory.consumer;
import com.algaworks.example.orderhistory.config.RabbitMQConfig;
import com.algaworks.example.orderhistory.event.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsListener {

    @RabbitListener(queues = RabbitMQConfig.ORDER_HISTORY_QUEUE_NAME)
    public void onOrderEvent(OrderEvent message)
    {
        System.out.println("venda recebida de Id: " + message.getOrder().getId());
    }
}
