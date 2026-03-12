// NEW
import com.javatechie.aws.cicd.example.dto.OrderDto;
import com.javatechie.aws.cicd.example.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

// NEW
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Before
    public void setup() {
        // setup test data
    }

    @Test
    public void testGetOrderById() {
        // test getOrderById method
        Long id = 1L;
        OrderDto orderDto = new OrderDto();
        when(orderRepository.findById(id)).thenReturn(orderDto);
        ResponseEntity<OrderDto> response = orderService.getOrderById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrders() {
        // test getOrders method
        Double minPrice = 10.0;
        int page = 0;
        int size = 10;
        OrderFilter orderFilter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = orderRepository.findByFilter(orderFilter, pageable);
        when(orderRepository.findByFilter(orderFilter, pageable)).thenReturn(orders);
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(minPrice, page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}