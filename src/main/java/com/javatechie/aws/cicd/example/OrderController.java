// NEW
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderDao orderDao;

    @Autowired
    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable int id) {
        // NEW
        return orderDao.getOrderById(id);
    }

    @GetMapping("/orders")
    public Page<Order> getOrders(@RequestParam(defaultValue = "0") int page, 
                                  @RequestParam(defaultValue = "10") int size, 
                                  @RequestParam(required = false) Double minPrice) {
        // NEW
        return orderDao.getOrders(page, size, minPrice);
    }
}