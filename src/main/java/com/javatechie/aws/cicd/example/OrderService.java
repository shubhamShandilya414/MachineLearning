// NEW
import com.javatechie.aws.cicd.example.dto.OrderDto;
import com.javatechie.aws.cicd.example.dto.OrderFilter;
import com.javatechie.aws.cicd.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Order Service API implementation.
 * Provides endpoints for retrieving orders with filtering, pagination, and order lookup by ID.
 */
@RestController
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves an order by ID.
     *
     * @param id the order ID
     * @return the order details
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(OrderDto::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves orders with filtering and pagination.
     *
     * @param minPrice the minimum price filter
     * @param page     the page number
     * @param size     the page size
     * @return the list of orders
     */
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDto>> getOrders(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderFilter orderFilter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderRepository.findByFilter(orderFilter, pageable).map(OrderDto::new));
    }
}