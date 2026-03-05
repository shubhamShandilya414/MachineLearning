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
        List<Order> result = orderService.getOrders();

        // Assert
        assertEquals(4, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals("Mobile", result.get(0).getName());
        assertEquals(1, result.get(0).getQuantity());
        assertEquals(30000, result.get(0).getPrice());
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
        List<Order> result = orderService.getOrders(2000);

        // Assert
        assertEquals(3, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals("Mobile", result.get(0).getName());
        assertEquals(1, result.get(0).getQuantity());
        assertEquals(30000, result.get(0).getPrice());
    }

    @Test
    void should_returnOrderById_whenIdProvided() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrders()).thenReturn(Stream.of(order).collect(Collectors.toList()));

        // Act
        Order result = orderService.getOrderById(101);

        // Assert
        assertEquals(101, result.getId());
        assertEquals("Mobile", result.getName());
        assertEquals(1, result.getQuantity());
        assertEquals(30000, result.getPrice());
    }

    @Test
    void should_returnEmptyList_when_minPriceIsHigherThanAllOrders() {
        // Arrange
        List<Order> orders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrders(200000);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void should_returnNull_whenOrderIdDoesNotExist() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of().collect(Collectors.toList()));

        // Act
        Order result = orderService.getOrderById(101);

        // Assert
        assertEquals(null, result);
    }
}