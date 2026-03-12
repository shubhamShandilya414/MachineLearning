import com.javatechie.aws.cicd.example.OrderService;
import com.javatechie.aws.cicd.example.OrderRepository;
import com.javatechie.aws.cicd.example.dto.OrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
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
        OrderDto orderDto = new OrderDto(1L, "Test Order", 10.99);
        when(orderRepository.findById(1L)).thenReturn(orderDto);
    }

    @Test
    public void testGetOrderById() {
        ResponseEntity<OrderDto> response = orderService.getOrderById(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(null, 0, 10);
        assertEquals(200, response.getStatusCodeValue());
    }
}