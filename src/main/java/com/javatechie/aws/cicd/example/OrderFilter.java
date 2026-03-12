// NEW
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order Filter class.
 * Represents a filter for orders.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilter {

    private Double minPrice;
}