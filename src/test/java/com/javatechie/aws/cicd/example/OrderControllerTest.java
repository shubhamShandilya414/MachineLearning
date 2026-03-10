package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import com.javatechie.aws.cicd.example.OrderController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderController orderController;

    @Test
    void shouldReturnOrderById() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenOrderByIdIsNotPresent() {
        // Arrange
        when(orderDao.getOrderById(101)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnFilteredOrdersWhenMinPriceProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000)
        );
        when(orderDao.getOrdersByMinPrice(2000)).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrdersByMinPrice(2000);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
    }

    @Test
    void shouldReturnPaginatedOrders() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000)
        );
        when(orderDao.getOrders(anyInt(), anyInt())).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orders, response.getBody());
    }
}