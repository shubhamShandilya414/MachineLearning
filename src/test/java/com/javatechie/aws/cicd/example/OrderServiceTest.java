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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

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
        List<Order> result = orderService.getOrdersByMinPrice(2000);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.of(new Order(101, "Mobile", 1, 30000)));

        // Act
        Order result = orderService.getOrderById(101).orElseThrow();

        // Assert
        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    void should_returnNotFound_when_orderIdNotFound() {
        // Arrange
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Order> result = orderService.getOrderByIdResponseEntity(101);

        // Assert
        assertEquals(404, result.getStatusCodeValue());
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
        List<Order> result = orderService.getOrdersPaginated(0, 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}