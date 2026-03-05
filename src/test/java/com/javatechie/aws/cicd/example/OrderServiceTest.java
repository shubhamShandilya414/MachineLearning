package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import com.javatechie.aws.cicd.example.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void should_returnAllOrders_whenNoFilterProvided() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrders(null, null, null);

        // Assert
        assertEquals(orders, result);
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
        List<Order> result = orderService.getOrders(1000, null, null);

        // Assert
        List<Order> expected = orders.stream()
                .filter(order -> order.getPrice() >= 1000)
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @Test
    void should_returnPaginatedOrders_whenPageAndSizeProvided() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrders(null, 0, 2);

        // Assert
        List<Order> expected = orders.stream()
                .limit(2)
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @Test
    void should_returnOrderById() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(order);

        // Act
        Order result = orderService.getOrderById(101);

        // Assert
        assertEquals(order, result);
    }

    @Test
    void should_returnEmptyList_whenNoOrdersFound() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(List.of());

        // Act
        List<Order> result = orderService.getOrders(null, null, null);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test
    void should_returnNull_whenOrderIdNotFound() {
        // Arrange
        when(orderDao.getOrderById(101)).thenReturn(null);

        // Act
        Order result = orderService.getOrderById(101);

        // Assert
        assertEquals(null, result);
    }
}