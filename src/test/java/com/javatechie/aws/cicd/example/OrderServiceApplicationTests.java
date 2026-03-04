package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testFetchOrders() {
        ResponseEntity<List<Order>> response = testRestTemplate.getForEntity("/orders", List.class);
        assertNotNull(response.getBody());
        // NEW
        response.getBody().forEach(order -> {
            assert order.getId() > 100;
        });
    }
}