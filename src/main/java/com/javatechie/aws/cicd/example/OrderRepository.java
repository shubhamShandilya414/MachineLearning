
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.javatechie.aws.cicd.example.model.Order;

/**
 * OrderRepository is responsible for encapsulating data access to the orders table.
 * It extends the JpaRepository interface to leverage Spring Data JPA's features.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the ID of the order to retrieve
     * @return the order with the specified ID, or null if no such order exists
     */
    Order findById(Long id);

    /**
     * Retrieves a page of orders filtered by minimum price.
     * 
     * @param minPrice the minimum price to filter by
     * @param pageable pagination information
     * @return a page of orders with prices greater than or equal to the specified minimum price
     */
    @Query("SELECT o FROM Order o WHERE o.price >= :minPrice")
    Page<Order> findByMinPrice(@Param("minPrice") Double minPrice, Pageable pageable);

    /**
     * Retrieves all orders with pagination.
     * 
     * @param pageable pagination information
     * @return a page of orders
     */
    @Override
    Page<Order> findAll(Pageable pageable);
}
