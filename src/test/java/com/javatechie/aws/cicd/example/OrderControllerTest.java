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
    public void testRetrieveOrderById() throws Exception {
        int orderId = 101;

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.name").value("Mobile"))
                .andExpect(jsonPath("$.price").value(300000));
    }

    @Test
    public void testRetrieveOrderByIdNotFound() throws Exception {
        int orderId = 999;

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterOrdersByMinPrice() throws Exception {
        double minPrice = 10000;

        mockMvc.perform(get("/orders?minPrice={minPrice}", minPrice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].price", everyItem(greaterThanOrEqualTo((int)minPrice))));
    }

    @Test
    public void testFilterOrdersByMinPriceHighValue() throws Exception {
        double minPrice = 200000;

        mockMvc.perform(get("/orders?minPrice={minPrice}", minPrice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mobile"));
    }

    @Test
    public void testPaginateOrders() throws Exception {
        int page = 0;
        int size = 2;

        mockMvc.perform(get("/orders?page={page}&size={size}", page, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPaginateOrdersSecondPage() throws Exception {
        int page = 1;
        int size = 2;

        mockMvc.perform(get("/orders?page={page}&size={size}", page, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPaginateOrdersLastPage() throws Exception {
        int page = 2;
        int size = 2;

        mockMvc.perform(get("/orders?page={page}&size={size}", page, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}