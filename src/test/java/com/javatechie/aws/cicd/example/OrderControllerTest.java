package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderController;
import com.javatechie.aws.cicd.example.OrderDao;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderController orderController;

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        ));

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(2000);

        // Assert
        assertEquals(3, response.getBody().size());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        when(orderDao.getOrderById(101)).thenReturn(Optional.of(new Order(101, "Mobile", 1, 30000)));

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertEquals(101, response.getBody().getId());
    }

    @Test
    void should_return404_when_orderNotFoundById() {
        // Arrange
        when(orderDao.getOrderById(999)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> orderController.getOrderById(999));
    }

    @Test
    void should_returnPaginatedOrders_when_pageAndSizeProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        ));

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(0, 2);

        // Assert
        assertEquals(2, response.getBody().size());
    }

    @Test
    void should_returnBadRequest_when_invalidPaginationParameters() {
        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> orderController.getOrders(-1, 2));
    }
}