
package com.javatechie/aws/cicd/example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testFetchOrders() {
        ResponseEntity<List<Order>> response = testRestTemplate.getForEntity("/orders", List.class);
        List<Order> orders = response.getBody();
        // NEW: verify that all orders have id > 100
        orders.forEach(order -> assertEquals(true, order.getId() > 100));
    }
}