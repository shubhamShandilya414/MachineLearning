package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDaoTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnOrderById_whenIdProvided() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(101).orElseThrow();

        // Assert
        assertEquals(101, result.getId());
        assertEquals("Mobile", result.getName());
        assertEquals(1, result.getQuantity());
        assertEquals(30000, result.getPrice());
    }

    @Test
    void shouldReturnNotFound_whenIdNotProvided() {
        // Arrange
        when(orderDao.getOrderById(101)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(101).orElseThrow());
    }

    @Test
    void shouldReturnFilteredOrders_whenMinPriceProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrdersByMinPrice(10000);

        // Assert
        assertEquals(3, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals(205, result.get(1).getId());
        assertEquals(809, result.get(2).getId());
    }

    @Test
    void shouldReturnEmptyList_whenMinPriceHigherThanAllOrders() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrdersByMinPrice(200000);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnPaginatedOrders() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrdersPaginated(0, 2);

        // Assert
        assertEquals(2, result.size());
        assertEquals(101, result.get(0).getId());
        assertEquals(58, result.get(1).getId());
    }

    @Test
    void shouldReturnEmptyList_whenPaginationParametersInvalid() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrdersPaginated(-1, 2));
        assertThrows(RuntimeException.class, () -> orderService.getOrdersPaginated(0, -2));
    }
}