// NEW
import com.javatechie.aws.cicd.example.dto.OrderDto;
import com.javatechie.aws.cicd.example.dto.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // NEW
    Page<OrderDto> findByFilter(@Param("filter") OrderFilter filter, Pageable pageable);
}