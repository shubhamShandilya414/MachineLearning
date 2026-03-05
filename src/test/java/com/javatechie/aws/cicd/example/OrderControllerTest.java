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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerTest.class);

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderController orderController;

    @Test
    void should_returnAllOrders_when_noFilterProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(null, null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(4, response.getBody().size());
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        List<Order> orders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        ));

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(1000, null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        Order order = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(101, response.getBody().getId());
    }

    @Test
    void should_returnNotFound_when_idNotProvided() {
        // Arrange
        when(orderDao.getOrderById(anyInt())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(101);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
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
        ResponseEntity<List<Order>> response = orderController.getOrders(null, 0, 2);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void should_returnBadRequest_when_invalidPageOrSizeProvided() {
        // Arrange

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrders(null, -1, 2);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }
}