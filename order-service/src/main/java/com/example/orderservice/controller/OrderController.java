package com.example.orderservice.controller;

import com.example.basedomains.dto.Order;
import com.example.basedomains.dto.OrderEvent;
import com.example.orderservice.kafka.OrderProducer;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.requests.OffsetDeleteRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order)
    {
        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("Order Status is in Pending State");
        orderEvent.setOrder(order);

        orderProducer.sendMessage(orderEvent);
        return "Order Placed Successfully";
    }
}
