package com.javatechie.aws.cicd.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderServiceApplication.class)
class OrderServiceApplicationEdgeCaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDao orderDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetOrderByIdWithZeroId() throws Exception {
        when(orderDao.findById(0)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/orders/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrderByIdWithMaxIntegerId() throws Exception {
        when(orderDao.findById(Integer.MAX_VALUE)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/orders/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrderByIdWithVeryLargeId() throws Exception {
        mockMvc.perform(get("/orders/999999999999999999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid number format"));
    }

    @Test
    void testGetOrderByIdWithSpecialCharacters() throws Exception {
        mockMvc.perform(get("/orders/@#$%"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid number format"));
    }

    @Test
    void testGetOrderByIdWithFloatingPointId() throws Exception {
        mockMvc.perform(get("/orders/1.5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid number format"));
    }

    @Test
    void testGetOrdersWithNegativeMinPrice() throws Exception {
        when(orderDao.getOrders(eq(-100.0), eq(0), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?minPrice=-100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithZeroMinPrice() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(1, "Product1", "Category1", "Color1", 0.0),
                new Order(2, "Product2", "Category2", "Color2", 10.0)
        );
        when(orderDao.getOrders(eq(0.0), eq(0), eq(10))).thenReturn(orders);
        
        mockMvc.perform(get("/orders?minPrice=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetOrdersWithVeryLargeMinPrice() throws Exception {
        when(orderDao.getOrders(eq(Double.MAX_VALUE), eq(0), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?minPrice=" + Double.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithInfinityMinPrice() throws Exception {
        mockMvc.perform(get("/orders?minPrice=Infinity"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithNaNMinPrice() throws Exception {
        mockMvc.perform(get("/orders?minPrice=NaN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithMaxIntegerPage() throws Exception {
        when(orderDao.getOrders(isNull(), eq(Integer.MAX_VALUE), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?page=" + Integer.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithMaxIntegerSize() throws Exception {
        List<Order> orders = Arrays.asList(new Order(1, "Product1", "Category1", "Color1", 10.0));
        when(orderDao.getOrders(isNull(), eq(0), eq(Integer.MAX_VALUE))).thenReturn(orders);
        
        mockMvc.perform(get("/orders?size=" + Integer.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetOrdersWithEmptyStringParameters() throws Exception {
        mockMvc.perform(get("/orders?minPrice=&page=&size="))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithWhitespaceParameters() throws Exception {
        mockMvc.perform(get("/orders?minPrice= &page= &size= "))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithMixedValidAndInvalidParameters() throws Exception {
        mockMvc.perform(get("/orders?minPrice=100&page=invalid&size=10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithDuplicateParameters() throws Exception {
        List<Order> orders = Arrays.asList(new Order(1, "Product1", "Category1", "Color1", 150.0));
        when(orderDao.getOrders(eq(200.0), eq(1), eq(5))).thenReturn(orders);
        
        // Spring takes the last value when parameters are duplicated
        mockMvc.perform(get("/orders?minPrice=100&minPrice=200&page=0&page=1&size=10&size=5"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrdersWithExtremelyLongParameterValues() throws Exception {
        String longValue = "1" + "0".repeat(1000);
        mockMvc.perform(get("/orders?minPrice=" + longValue))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithScientificNotationMinPrice() throws Exception {
        when(orderDao.getOrders(eq(1.5E3), eq(0), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?minPrice=1.5E3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithHexadecimalParameters() throws Exception {
        mockMvc.perform(get("/orders?page=0x10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersWithBooleanParameters() throws Exception {
        mockMvc.perform(get("/orders?page=true&size=false"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrdersBackwardCompatibilityWithExactDefaultValues() throws Exception {
        when(orderDao.getOrders()).thenReturn(Arrays.asList(
                new Order(1, "Product1", "Category1", "Color1", 10.0)
        ));
        
        // When page=0 and size=10 (defaults), should call the original method
        mockMvc.perform(get("/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetOrdersWithOnlyPageParameterUsesDefaultSize() throws Exception {
        when(orderDao.getOrders(isNull(), eq(1), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithOnlySizeParameterUsesDefaultPage() throws Exception {
        when(orderDao.getOrders(isNull(), eq(0), eq(5))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithMinPriceOnlyUsesDefaultPagination() throws Exception {
        when(orderDao.getOrders(eq(100.0), eq(0), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?minPrice=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrdersWithAllParametersAsZero() throws Exception {
        when(orderDao.getOrders(eq(0.0), eq(0), eq(10))).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/orders?minPrice=0&page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrdersWithDecimalPageAndSize() throws Exception {
        mockMvc.perform(get("/orders?page=1.5&size=2.7"))
                .andExpect(status().isBadRequest());
    }
}