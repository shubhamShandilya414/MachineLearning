// NEW
import com.javatechie.aws.cicd.example.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// NEW
@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderServiceApplication {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderDto>> fetchOrders() {
        return orderService.getOrders(null, 0, 10);
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}