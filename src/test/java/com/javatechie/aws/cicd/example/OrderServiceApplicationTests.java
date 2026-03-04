package com/javatechie/aws/cicd/example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceApplicationTests {

    @Autowired
    private OrderServiceApplication orderServiceApplication;

    @Test
    void testFetchOrders() {
        List<Order> orders = orderServiceApplication.fetchOrders();
        assertTrue(orders.stream().allMatch(order -> order.getId() > 100)); // NEW
    }
}