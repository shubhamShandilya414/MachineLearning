package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceApplicationTest {

    @Autowired
    private OrderServiceApplication orderServiceApplication;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFetchOrders() {
        ResponseEntity<List<Order>> responseEntity = orderServiceApplication.fetchOrders();
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(4, responseEntity.getBody().size());
    }

    @Test
    public void testGetOrderById() {
        ResponseEntity<Order> responseEntity = orderServiceApplication.getOrderById(101);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(101, responseEntity.getBody().getId());
    }

    @Test
    public void testGetOrderByIdNotFound() {
        ResponseEntity<Order> responseEntity = orderServiceApplication.getOrderById(999);
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testFetchOrdersViaMockMvc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4));
    }

    @Test
    public void testGetOrderByIdViaMockMvc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/101"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(101));
    }

    @Test
    public void testGetOrderByIdNotFoundViaMockMvc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/999"))
                .andExpect(status().isNotFound());
    }
}