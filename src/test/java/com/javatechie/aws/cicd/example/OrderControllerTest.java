package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderController orderController;

    @Test
    void should_returnAllOrders_when_noFilterProvided() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(null);

        // Assert
        assertEquals(4, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getId());
        assertEquals("Mobile", response.getBody().get(0).getName());
        assertEquals(1, response.getBody().get(0).getQuantity());
        assertEquals(30000, response.getBody().get(0).getPrice());
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(2000);

        // Assert
        assertEquals(3, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getId());
        assertEquals("Mobile", response.getBody().get(0).getName());
        assertEquals(1, response.getBody().get(0).getQuantity());
        assertEquals(30000, response.getBody().get(0).getPrice());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertEquals(101, response.getBody().getId());
        assertEquals("Mobile", response.getBody().getName());
        assertEquals(1, response.getBody().getQuantity());
        assertEquals(30000, response.getBody().getPrice());
    }

    @Test
    void should_returnEmptyList_when_minPriceHigherThanAllOrders() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(200000);

        // Assert
        assertEquals(0, response.getBody().size());
    }

    @Test
    void should_returnOrderById_when_idProvidedAndOrderExists() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertEquals(101, response.getBody().getId());
        assertEquals("Mobile", response.getBody().getName());
        assertEquals(1, response.getBody().getQuantity());
        assertEquals(30000, response.getBody().getPrice());
    }

    @Test
    void should_returnNull_when_idProvidedAndOrderDoesNotExist() {
        // Arrange
        when(orderDao.getOrderById(999)).thenReturn(null);

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(999);

        // Assert
        assertEquals(null, response.getBody());
    }
}