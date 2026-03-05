package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDaoTest {

    private static final Logger log = LoggerFactory.getLogger(OrderDaoTest.class);

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void should_returnAllOrders_whenNoFilterProvided() {
        // Arrange
        List<Order> expectedOrders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        );
        when(orderDao.getOrders()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.getOrders();

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        List<Order> expectedOrders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(205, "Laptop", 1, 150000)
        );
        when(orderDao.getOrders()).thenReturn(List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        ));

        // Act
        List<Order> actualOrders = orderService.getOrders(10000);

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void should_returnOrderById_whenIdProvided() {
        // Arrange
        Order expectedOrder = new Order(101, "Mobile", 1, 30000);
        when(orderDao.getOrderById(101)).thenReturn(Optional.of(expectedOrder));

        // Act
        Order actualOrder = orderService.getOrderById(101).orElseThrow();

        // Assert
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void should_throwException_whenOrderNotFoundById() {
        // Arrange
        when(orderDao.getOrderById(101)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> orderService.getOrderById(101).orElseThrow());
    }

    @Test
    void should_returnPaginatedOrders_whenPageableProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        List<Order> expectedOrders = List.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000)
        );
        when(orderDao.getOrders(pageable)).thenReturn(Page.of(expectedOrders));

        // Act
        Page<Order> actualOrders = orderService.getOrders(pageable);

        // Assert
        assertEquals(expectedOrders, actualOrders.getContent());
    }
}