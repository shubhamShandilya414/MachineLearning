// NEW
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // NEW
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(OrderDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // NEW
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDto>> getOrders(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (minPrice != null) {
            return ResponseEntity.ok(orderRepository.findByMinPrice(minPrice, pageable).map(OrderDto::fromEntity));
        } else {
            return ResponseEntity.ok(orderRepository.findAll(pageable).map(OrderDto::fromEntity));
        }
    }
}