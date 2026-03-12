// NEW
import com.javatechie.aws.cicd.example.dto.OrderDto;
import com.javatechie.aws.cicd.example.dto.OrderFilter;
import com.javatechie.aws.cicd.example.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// NEW
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        // Initialize test data
    }

    @Test
    void testGetOrderById() {
        // Test getting an order by ID
        Long orderId = 1L;
        ResponseEntity<OrderDto> response = orderService.getOrderById(orderId);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetOrders() {
        // Test getting orders with filtering and pagination
        Double minPrice = 10.0;
        int page = 0;
        int size = 10;
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(minPrice, page, size);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}