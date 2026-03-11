// NEW
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    // existing code...

    public Order getOrderById(int id) {
        // NEW
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Page<Order> getOrders(int page, int size, Double minPrice) {
        // NEW
        List<Order> ordersList = orders.stream()
                .filter(order -> minPrice == null || order.getPrice() >= minPrice)
                .sorted(Comparator.comparingInt(Order::getPrice))
                .collect(Collectors.toList());

        int start = page * size;
        int end = start + size;

        return ordersList.subList(start, Math.min(end, ordersList.size())).stream()
                .collect(Collectors.toList());
    }
}