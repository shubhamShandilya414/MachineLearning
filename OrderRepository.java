import com.javatechie.aws.cicd.example.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a page of orders, filtered by minimum price.
     *
     * @param minPrice the minimum price to filter by
     * @param pageable the pagination information
     * @return a page of orders, filtered by minimum price
     */
    @Query("SELECT o FROM Order o WHERE o.price >= :minPrice")
    Page<Order> findByFilter(@Param("minPrice") Double minPrice, Pageable pageable);

    Order findById(Long id);
}