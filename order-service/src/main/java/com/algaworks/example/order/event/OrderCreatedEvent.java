package com.algaworks.example.order.event;

import com.algaworks.example.order.model.OrderModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;


public class OrderCreatedEvent {

    @JsonIgnore
    private OffsetDateTime date = OffsetDateTime.now();
    private OrderModel order;

    public OrderCreatedEvent() {

    }

    public OrderCreatedEvent(OrderModel order) {
        this.order = order;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}
