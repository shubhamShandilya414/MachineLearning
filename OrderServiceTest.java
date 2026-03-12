import com.javatechie.aws.cicd.example.OrderService;
import com.javatechie.aws.cicd.example.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setup() {
        // setup test data
    }

    @Test
    void testGetOrderById() {
        // test happy path
        ResponseEntity<OrderDto> response = orderService.getOrderById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetOrders() {
        // test happy path
        ResponseEntity<Page<OrderDto>> response = orderService.getOrders(null, 0, 10);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}