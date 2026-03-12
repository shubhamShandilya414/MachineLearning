// NEW
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // NEW
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Order findById(@Param("id") Long id);

    // NEW
    @Query("SELECT o FROM Order o WHERE o.price >= :minPrice")
    Page<Order> findByMinPrice(@Param("minPrice") Double minPrice, Pageable pageable);

    // NEW
    @Override
    Page<Order> findAll(Pageable pageable);
}