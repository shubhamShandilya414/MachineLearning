package com.javatechie.aws.cicd.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDao orderDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testAddOrder() throws Exception {
        Order order = new Order(1, "Laptop", "Electronics", "Black", 999.99);
        when(orderDao.addOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.name").value("Laptop"))
                .andExpected(jsonPath("$.category").value("Electronics"))
                .andExpected(jsonPath("$.color").value("Black"))
                .andExpected(jsonPath("$.price").value(999.99));
    }

    @Test
    void testGetOrders() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(1, "Laptop", "Electronics", "Black", 999.99),
                new Order(2, "Phone", "Electronics", "White", 599.99)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(2))
                .andExpected(jsonPath("$[0].name").value("Laptop"))
                .andExpected(jsonPath("$[1].name").value("Phone"));
    }

    @Test
    void testGetOrdersWithMinPrice() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(1, "Laptop", "Electronics", "Black", 999.99)
        );
        when(orderDao.getOrders(eq(800.0), eq(0), eq(10))).thenReturn(orders);

        mockMvc.perform(get("/orders?minPrice=800.0"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(1))
                .andExpected(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void testGetOrdersWithPagination() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(3, "Tablet", "Electronics", "Silver", 399.99)
        );
        when(orderDao.getOrders(isNull(), eq(1), eq(5))).thenReturn(orders);

        mockMvc.perform(get("/orders?page=1&size=5"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(1))
                .andExpected(jsonPath("$[0].name").value("Tablet"));
    }

    @Test
    void testGetOrdersWithAllParameters() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(1, "Laptop", "Electronics", "Black", 999.99)
        );
        when(orderDao.getOrders(eq(500.0), eq(0), eq(20))).thenReturn(orders);

        mockMvc.perform(get("/orders?minPrice=500.0&page=0&size=20"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(1))
                .andExpected(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void testGetOrdersWithoutParameters() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order(1, "Laptop", "Electronics", "Black", 999.99),
                new Order(2, "Phone", "Electronics", "White", 599.99)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(2))
                .andExpected(jsonPath("$[0].name").value("Laptop"))
                .andExpected(jsonPath("$[1].name").value("Phone"));
    }

    @Test
    void testGetOrdersWithNegativePage() throws Exception {
        mockMvc.perform(get("/orders?page=-1"))
                .andExpected(status().isBadRequest())
                .andExpected(content().string("Page number must be non-negative"));
    }

    @Test
    void testGetOrdersWithZeroSize() throws Exception {
        mockMvc.perform(get("/orders?size=0"))
                .andExpected(status().isBadRequest())
                .andExpected(content().string("Page size must be positive"));
    }

    @Test
    void testGetOrdersWithEmptyResult() throws Exception {
        when(orderDao.getOrders(eq(10000.0), eq(0), eq(10))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/orders?minPrice=10000.0"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order(1, "Laptop", "Electronics", "Black", 999.99);
        when(orderDao.findById(1)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/1"))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.id").value(1))
                .andExpected(jsonPath("$.name").value("Laptop"))
                .andExpected(jsonPath("$.category").value("Electronics"))
                .andExpected(jsonPath("$.color").value("Black"))
                .andExpected(jsonPath("$.price").value(999.99));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderDao.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/999"))
                .andExpected(status().isNotFound());
    }

    @Test
    void testGetOrderByIdInvalidId() throws Exception {
        mockMvc.perform(get("/orders/invalid"))
                .andExpected(status().isBadRequest());
    }
}