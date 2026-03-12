// NEW
import com.javatechie.aws/cicd/example.OrderService;
import com.javatechie.aws/cicd/example.dto.OrderDto;
import com.javatechie.aws/cicd/example.dto.OrderFilter;
import com.javatechie.aws/cicd/example.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Before
    public void setup() {
        // NEW
        orderRepository.save(new Order(1L, "Test Order", 10.99));
    }

    @Test
    public void testGetOrderById() {
        // NEW
        ResponseEntity<OrderDto> response = orderService.getOrderById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetOrders() {
        // NEW
        OrderFilter filter = new OrderFilter(5.0);
        Pageable pageable = PageRequest.of(0, 10);
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(5.0, 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}