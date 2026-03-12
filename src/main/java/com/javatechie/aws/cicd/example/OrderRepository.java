// NEW
import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.dto.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Order Repository interface.
 * Defines data access methods for orders.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves an order by ID.
     *
     * @param id the order ID
     * @return the order details
     */
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findById(@Param("id") Long id);

    /**
     * Retrieves orders with filtering and pagination.
     *
     * @param filter the order filter
     * @param pageable the pagination parameters
     * @return the list of orders
     */
    @Query("SELECT o FROM Order o WHERE o.price >= :minPrice")
    Page<Order> findByFilter(@Param("minPrice") Double minPrice, Pageable pageable);
}