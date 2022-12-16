package com.algaworks.example.order.event;

import com.algaworks.example.order.model.OrderModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

public class OrderCancelEvent {

    @JsonIgnore
    private OffsetDateTime date = OffsetDateTime.now();
    private OrderModel order;

    public OrderCancelEvent() {
    }

    public OrderCancelEvent(OrderModel order) {
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
