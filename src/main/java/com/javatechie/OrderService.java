import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrders(Double minPrice, int page, int size) {
        if (minPrice != null) {
            return orderRepository.findByPriceGreaterThanEqual(minPrice, page, size);
        } else {
            return orderRepository.findAll(page, size);
        }
    }
}