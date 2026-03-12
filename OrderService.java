// NEW
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // NEW
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // NEW
    public Page<OrderDto> getOrders(Double minPrice, int page, int size) {
        OrderFilter orderFilter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByFilter(orderFilter, pageable);
    }
}