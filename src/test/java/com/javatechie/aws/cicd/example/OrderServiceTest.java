// NEW
import com.javatechie.aws/cicd/example.OrderService;
import com.javatechie.aws/cicd/example.dto.OrderDto;
import com.javatechie.aws/cicd/example.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Before
    public void setup() {
        // Initialize test data
    }

    @Test
    public void testGetOrderById() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<OrderDto> response = orderService.getOrderById(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderId, response.getBody().getId());
    }

    @Test
    public void testGetOrders() {
        // Arrange
        Double minPrice = 10.0;
        int page = 0;
        int size = 10;
        OrderFilter filter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders = List.of(new Order());
        when(orderRepository.findByFilter(filter, pageable)).thenReturn(orders);

        // Act
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(minPrice, page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orders.size(), response.getBody().getContent().size());
    }
}