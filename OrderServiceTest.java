// NEW
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrderById() {
        // given
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when
        OrderDto orderDto = orderService.getOrderById(id);

        // then
        assertEquals(id, orderDto.getId());
    }

    @Test
    void testGetOrders() {
        // given
        Double minPrice = 10.0;
        int page = 0;
        int size = 10;
        OrderFilter orderFilter = new OrderFilter(minPrice);
        Pageable pageable = PageRequest.of(page, size);
        when(orderRepository.findByFilter(any(), any())).thenReturn(Page.empty());

        // when
        Page<OrderDto> orders = orderService.getOrders(minPrice, page, size);

        // then
        assertEquals(0, orders.getContent().size());
    }

    @Test
    void testGetOrderByIdNotFound() {
        // given
        Long id = 1L;
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        // when and then
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(id));
    }
}