package com.javatechie.aws.cicd.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    public void testGetOrders() {
        List<Order> orders = orderService.getOrders(0, 0, 10);
        assertEquals(4, orders.size());
    }

    @Test
    public void testGetOrderById() {
        Order order = orderService.getOrderById(101);
        assertEquals(101, order.getId());
    }
}