package com.algaworks.example.orderhistory.event;

import com.algaworks.example.order.model.OrderModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

public class OrderEvent {

    @JsonIgnore
    private OffsetDateTime date = OffsetDateTime.now();
    private OrderModel order;

    public OrderEvent() {
    }

    public OrderEvent(OrderModel order) {
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
