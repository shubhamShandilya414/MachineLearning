package com.javatechie.aws.cicd.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> getOrders() {
        log.info("getOrders called");
        return orderDao.getOrders().stream()
                .sorted(Comparator.comparingLong(Order::getPrice))
                .collect(Collectors.toList());
    }

    // NEW: Added method to retrieve order by ID
    public ResponseEntity<Order> getOrderById(int id) {
        log.info("getOrderById called with id={}", id);
        try {
            Optional<Order> order = orderDao.getOrders().stream()
                    .filter(o -> o.getId() == id)
                    .findFirst();
            return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // NEW: Added method to filter orders by minimum price
    public List<Order> getOrdersByMinPrice(long minPrice) {
        log.info("getOrdersByMinPrice called with minPrice={}", minPrice);
        return orderDao.getOrders().stream()
                .filter(o -> o.getPrice() >= minPrice)
                .collect(Collectors.toList());
    }

    // NEW: Added method to paginate orders
    public List<Order> getOrdersPaginated(int pageNumber, int pageSize) {
        log.info("getOrdersPaginated called with pageNumber={} and pageSize={}", pageNumber, pageSize);
        if (pageNumber < 0 || pageSize < 1) {
            return List.of(); // or throw an exception
        }
        return orderDao.getOrders().stream()
                .sorted(Comparator.comparingLong(Order::getPrice))
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}