package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import com.javatechie.aws.cicd.example.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = orderService.getOrderById(101);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(order, response.getBody());
    }

    @Test
    void should_returnNotFound_when_idNotProvided() {
        // Arrange
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> orderService.getOrderById(101));
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderService.getOrders(2000);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void should_returnEmptyList_when_minPriceHigherThanAllOrders() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderService.getOrders(200000);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void should_returnPaginatedOrders_when_pageAndSizeProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderService.getOrders(0, 2);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void should_returnBadRequest_when_invalidPageOrSizeProvided() {
        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> orderService.getOrders(-1, 2));
        assertThrows(ResponseStatusException.class, () -> orderService.getOrders(0, -2));
    }
}