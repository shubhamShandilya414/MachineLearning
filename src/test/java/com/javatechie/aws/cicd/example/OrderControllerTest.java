package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.dto.Order;
import com.javatechie.aws.cicd.example.service.OrderService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerTest.class);

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        int minPrice = 2000;
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderService.getOrdersByMinPrice(minPrice)).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrdersByMinPrice(minPrice);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(4, response.getBody().size());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        int id = 101;
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderService.getOrderById(id)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void should_returnNotFound_when_idNotProvided() {
        // Arrange
        int id = 999;
        when(orderService.getOrderById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(id);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void should_returnPaginatedOrders_when_pageAndSizeProvided() {
        // Arrange
        int page = 0;
        int size = 2;
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000)
        );
        when(orderService.getOrders(page, size)).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(page, size);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void should_returnBadRequest_when_invalidPageOrSizeProvided() {
        // Arrange
        int page = -1;
        int size = 0;

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(page, size);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}