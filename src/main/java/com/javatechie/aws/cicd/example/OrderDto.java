// NEW
import com.javatechie.aws/cicd/example.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String name;
    private Long price;

    public static OrderDto fromEntity(Order order) {
        return new OrderDto(order.getId(), order.getName(), order.getPrice());
    }
}