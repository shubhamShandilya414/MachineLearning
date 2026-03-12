import com.javatechie.aws.cicd.example.OrderFilter;
import com.javatechie.aws.cicd.example.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> getOrders(Double minPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (minPrice != null) {
            return orderRepository.findByFilter(minPrice, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }
}