package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrderById() throws Exception {
        // Arrange
        int orderId = 101;

        // Act & Assert
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.name").value("Mobile"))
                .andExpect(jsonPath("$.price").value(300000));
    }

    @Test
    public void testGetOrdersByMinPrice() throws Exception {
        // Arrange
        double minPrice = 10000;

        // Act & Assert
        mockMvc.perform(get("/orders?minPrice={minPrice}", minPrice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].price", everyItem(greaterThanOrEqualTo((int) minPrice))));
    }

    @Test
    public void testGetOrdersPaginated() throws Exception {
        // Arrange
        int page = 0;
        int size = 2;

        // Act & Assert
        mockMvc.perform(get("/orders?page={page}&size={size}", page, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}