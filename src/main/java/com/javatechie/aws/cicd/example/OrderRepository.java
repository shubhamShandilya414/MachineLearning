// NEW
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // NEW
    @Query("SELECT o FROM Order o WHERE o.price >= :minPrice")
    Page<Order> findByFilter(@Param("minPrice") Double minPrice, Pageable pageable);

    // NEW
    Optional<Order> findById(Long id);
}