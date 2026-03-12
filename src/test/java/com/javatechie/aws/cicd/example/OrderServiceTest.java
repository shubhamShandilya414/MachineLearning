import com.javatechie.aws.cicd.example.OrderService;
import com.javatechie.aws.cicd.example.OrderRepository;
import com.javatechie.aws.cicd.example.OrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

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
        // Initialize mock data
    }

    @Test
    public void testGetOrderById() {
        // Test happy path
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(new OrderDto(1L, "Test Order", 10.99)));
        OrderDto order = orderService.getOrderById(1L).getBody();
        assertNotNull(order);
        assertEquals(1L, order.getId().longValue());
        assertEquals("Test Order", order.getName());
        assertEquals(10.99, order.getPrice(), 0.01);
    }

    @Test
    public void testGetOrders() {
        // Test happy path
        when(orderRepository.findByFilter(10.0, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(Arrays.asList(new OrderDto(1L, "Test Order 1", 10.99), new OrderDto(2L, "Test Order 2", 20.99))));
        Page<OrderDto> orders = orderService.getOrders(10.0, 0, 10).getBody();
        assertNotNull(orders);
        assertEquals(2, orders.getContent().size());
    }
}