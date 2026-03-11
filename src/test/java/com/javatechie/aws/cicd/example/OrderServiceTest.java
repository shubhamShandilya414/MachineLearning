
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javatechie.aws.cicd.example.OrderController;
import com.javatechie.aws.cicd.example.OrderService;
import com.javatechie.aws.cicd.example.model.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testGetAllOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, 10.99));
        orders.add(new Order(2L, 9.99));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetOrderById() {
        // Arrange
        Order order = new Order(1L, 10.99);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(1L);

        // Assert
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetOrderByIdNotFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    public void testGetOrdersByMinPrice() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, 10.99));
        orders.add(new Order(2L, 9.99));
        when(orderRepository.findByMinPrice(any(Double.class), any(Pageable.class))).thenReturn(orders);

        // Act
        List<Order> result = orderService.getOrdersByMinPrice(9.99);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetOrdersByMinPriceNotFound() {
        // Arrange
        when(orderRepository.findByMinPrice(any(Double.class), any(Pageable.class))).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByMinPrice(9.99));
    }

    @Test
    public void testPaginateOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, 10.99));
        orders.add(new Order(2L, 9.99));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orders);

        // Act
        Page<Order> result = orderService.paginateOrders(0, 10);

        // Assert
        assertEquals(2, result.getTotalElements());
    }
}
