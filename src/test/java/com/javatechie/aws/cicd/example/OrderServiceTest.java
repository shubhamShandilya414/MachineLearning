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
        Order order = new Order(1L, "Laptop", 75000.0);
        when(orderRepository.findById(1L)).thenReturn(order);

        // Act
        ResponseEntity<OrderDto> result = orderService.getOrderById(1L);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1L, result.getBody().getId());
    }

    @Test
    public void testGetOrders() {
        // Arrange
        Page<Order> orders = Page.empty();
        when(orderRepository.findAll(Pageable.unpaged())).thenReturn(orders);

        // Act
        ResponseEntity<Page<OrderDto>> result = orderService.getOrders(null, 0, 10);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(0, result.getBody().getTotalElements());
    }
}