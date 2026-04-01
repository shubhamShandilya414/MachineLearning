package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import com.javatechie.aws.cicd.example.OrderServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Order Service Application.
 */
@SpringBootTest
class OrderServiceApplicationTests {

    /**
     * The base URL for the Order Service API.
     */
    private static final String BASE_URL = "http://localhost:8080/orders";

    /**
     * The REST template for making HTTP requests.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Tests that the context loads successfully.
     */
    @Test
    void contextLoads() {
        // No-op test to verify that the application context loads correctly
    }

    /**
     * Tests that the fetchOrders endpoint returns a list of orders.
     */
    @Test
    void testFetchOrders() {
        // Make a GET request to the fetchOrders endpoint
        ResponseEntity<List<Order>> response = restTemplate.getForEntity(BASE_URL, List.class);

        // Verify that the response status code is 200 (OK)
        assertEquals(200, response.getStatusCodeValue());

        // Verify that the response body is not empty
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    /**
     * Tests that the fetchOrderById endpoint returns an order by its ID.
     */
    @Test
    void testFetchOrderById() {
        // Make a GET request to the fetchOrderById endpoint with a valid order ID
        int orderId = 101;
        ResponseEntity<Order> response = restTemplate.getForEntity(BASE_URL + "/" + orderId, Order.class);

        // Verify that the response status code is 200 (OK)
        assertEquals(200, response.getStatusCodeValue());

        // Verify that the response body is not null
        assertNotNull(response.getBody());

        // Verify that the response body has the correct order ID
        assertEquals(orderId, response.getBody().getId());
    }

    /**
     * Tests that the fetchOrderById endpoint returns a 404 status code for an invalid order ID.
     */
    @Test
    void testFetchOrderByIdInvalidId() {
        // Make a GET request to the fetchOrderById endpoint with an invalid order ID
        int orderId = 999;
        ResponseEntity<Order> response = restTemplate.getForEntity(BASE_URL + "/" + orderId, Order.class);

        // Verify that the response status code is 404 (Not Found)
        assertEquals(404, response.getStatusCodeValue());
    }
}