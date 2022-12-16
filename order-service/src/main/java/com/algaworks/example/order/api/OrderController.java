package com.algaworks.example.order.api;

import com.algaworks.example.order.config.RabbitMQConfig;
import com.algaworks.example.order.api.domain.OrderRepository;
import com.algaworks.example.order.api.domain.Order;
import com.algaworks.example.order.event.OrderCancelEvent;
import com.algaworks.example.order.event.OrderCreatedEvent;
import com.algaworks.example.order.event.OrderPaidEvent;
import com.algaworks.example.order.model.OrderInputModel;
import com.algaworks.example.order.model.OrderModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

	@Autowired
	private OrderRepository orders;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static final String ORDER_PAID_BASIC_ROUTING_KEY = "order.basic.paid";
	private static final String ORDER_PAID_VIP_ROUTING_KEY = "order.vip.paid";

	public static final String ORDER_CANCEL_VIP_ROUTING_KEY = "order.vip.cancel";
	public static final String ORDER_CANCEL_BASIC_ROUTING_KEY = "order.basic.cancel";

	private static final String ORDER_CREATED = "order.created";


	@PostMapping
	public OrderModel create(@RequestBody OrderInputModel order) {
		OrderModel orderModel =  OrderModel.of(orders.save(order.toOrder()));

		rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, ORDER_CREATED, new OrderCreatedEvent(orderModel));

		return orderModel;
	}

	@GetMapping
	public List<OrderModel> list() {
		return orders.findAll().stream().map(OrderModel::of).toList();
	}

	@GetMapping("{id}")
	public OrderModel findById(@PathVariable Long id) {
		return OrderModel.of(orders.findById(id).orElseThrow());
	}

	@PostMapping("{id}/pay")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pay(@PathVariable Long id) {
		Order order = orders.findById(id).orElseThrow();
		order.markAsPaid();


		String routingKey;
		 if (order.getValue().compareTo(BigDecimal.valueOf(100)) >= 0)
		 {
			routingKey = ORDER_PAID_VIP_ROUTING_KEY;
		 }
		 else
		 {
			routingKey = ORDER_PAID_BASIC_ROUTING_KEY;
		 }

		rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME,routingKey,new OrderPaidEvent(OrderModel.of(order)));
		orders.save(order);
	}

	@PostMapping("{id}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancel(@PathVariable Long id) {
		Order order = orders.findById(id).orElseThrow();
		order.cancel();

		String routingKey;
		if (order.getValue().compareTo(BigDecimal.valueOf(100)) >= 0)
		{
			routingKey = ORDER_CANCEL_VIP_ROUTING_KEY;
		}
		else
		{
			routingKey = ORDER_CANCEL_BASIC_ROUTING_KEY;
		}

		rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME,routingKey,new OrderCancelEvent(OrderModel.of(order)));
		orders.save(order);

		orders.save(order);
	}
	
}
