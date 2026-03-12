// NEW
import com.javatechie.aws.cicd.example.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order DTO class.
 * Represents an order with its details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String name;
    private int quantity;
    private double price;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.name = order.getName();
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
    }
}