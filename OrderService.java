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
 * Provides endpoints for retrieving orders.
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
     * @param id the ID of the order to retrieve
     * @return the order with the specified ID, or a 404 response if not found
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a page of orders, filtered by minimum price.
     *
     * @param minPrice the minimum price to filter by (optional)
     * @param page     the page number to retrieve (default: 0)
     * @param size     the page size to retrieve (default: 10)
     * @return a page of orders, filtered by minimum price
     */
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDto>> getOrders(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderFilter orderFilter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderRepository.findByFilter(minPrice, pageable));
    }
}