// NEW
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testGetOrderById() {
        // Arrange
        Order order = new Order(1L, "Test Order", 10.99);
        when(orderRepository.findById(1L)).thenReturn(order);

        // Act
        OrderDto result = orderService.getOrderById(1L).getBody();

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Test Order", result.getName());
        assertEquals(10.99, result.getPrice());
    }

    @Test
    public void testGetOrders() {
        // Arrange
        Page<Order> orders = Page.empty();
        when(orderRepository.findAll(Pageable.unpaged())).thenReturn(orders);

        // Act
        Page<OrderDto> result = orderService.getOrders(null, 0, 10).getBody();

        // Assert
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testGetOrdersByMinPrice() {
        // Arrange
        Page<Order> orders = Page.empty();
        when(orderRepository.findByMinPrice(10.0, Pageable.unpaged())).thenReturn(orders);

        // Act
        Page<OrderDto> result = orderService.getOrders(10.0, 0, 10).getBody();

        // Assert
        assertEquals(0, result.getTotalElements());
    }
}