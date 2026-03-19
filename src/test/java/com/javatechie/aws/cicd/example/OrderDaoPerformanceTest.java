package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@SpringJUnitConfig
class OrderDaoPerformanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao();
        orderDao.jdbcTemplate = jdbcTemplate;
        
        // Create test table
        jdbcTemplate.execute("CREATE TABLE ORDERS (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "category VARCHAR(255), " +
                "color VARCHAR(255), " +
                "price DOUBLE" +
                ")");
        
        // Insert 10,000 test records for performance testing
        for (int i = 1; i <= 10000; i++) {
            jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                    "Product" + i, "Category" + (i % 10), "Color" + (i % 5), i * 1.5);
        }
    }

    @Test
    void testGetOrdersPerformanceWithoutFilters() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders();
        long endTime = System.currentTimeMillis();
        
        assertEquals(10000, orders.size());
        assertTrue((endTime - startTime) < 500, "Query should complete in under 500ms");
    }

    @Test
    void testGetOrdersPerformanceWithMinPriceFilter() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(5000.0, null, null);
        long endTime = System.currentTimeMillis();
        
        assertTrue(orders.size() > 0);
        assertTrue(orders.size() < 10000);
        assertTrue((endTime - startTime) < 500, "Filtered query should complete in under 500ms");
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 5000.0));
    }

    @Test
    void testGetOrdersPerformanceWithPagination() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(null, 100, 50);
        long endTime = System.currentTimeMillis();
        
        assertEquals(50, orders.size());
        assertTrue((endTime - startTime) < 500, "Paginated query should complete in under 500ms");
    }

    @Test
    void testGetOrdersPerformanceWithMinPriceAndPagination() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(1000.0, 10, 100);
        long endTime = System.currentTimeMillis();
        
        assertTrue(orders.size() <= 100);
        assertTrue((endTime - startTime) < 500, "Combined filter and pagination should complete in under 500ms");
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 1000.0));
    }

    @Test
    void testFindByIdPerformanceWithLargeDataset() {
        long startTime = System.currentTimeMillis();
        Optional<Order> order = orderDao.findById(5000);
        long endTime = System.currentTimeMillis();
        
        assertTrue(order.isPresent());
        assertEquals("Product5000", order.get().getName());
        assertTrue((endTime - startTime) < 100, "Find by ID should complete in under 100ms");
    }

    @Test
    void testGetOrdersWithHighSelectivityFilter() {
        // Test with a filter that returns very few results
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(14999.0, null, null);
        long endTime = System.currentTimeMillis();
        
        assertTrue(orders.size() < 10);
        assertTrue((endTime - startTime) < 500, "High selectivity filter should complete in under 500ms");
    }

    @Test
    void testGetOrdersWithLowSelectivityFilter() {
        // Test with a filter that returns most results
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(10.0, null, null);
        long endTime = System.currentTimeMillis();
        
        assertTrue(orders.size() > 9000);
        assertTrue((endTime - startTime) < 500, "Low selectivity filter should complete in under 500ms");
    }

    @Test
    void testGetOrdersWithLargePagination() {
        // Test pagination with large page numbers
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(null, 99, 100);
        long endTime = System.currentTimeMillis();
        
        assertEquals(1, orders.size()); // Only 1 record left on page 99 with size 100
        assertTrue((endTime - startTime) < 500, "Large page number should complete in under 500ms");
    }

    @Test
    void testGetOrdersFilteringBeforeSortingOptimization() {
        // Verify that filtering happens before sorting for performance
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderDao.getOrders(9000.0, 0, 10);
        long endTime = System.currentTimeMillis();
        
        assertEquals(10, orders.size());
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 9000.0));
        assertTrue((endTime - startTime) < 500, "Optimized query should complete in under 500ms");
        
        // Verify ordering is maintained
        for (int i = 1; i < orders.size(); i++) {
            assertTrue(orders.get(i).getId() > orders.get(i-1).getId());
        }
    }

    @Test
    void testMultipleConsecutiveQueries() {
        // Test that multiple queries don't degrade performance
        long totalTime = 0;
        
        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            List<Order> orders = orderDao.getOrders(i * 1000.0, i, 50);
            long endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
            
            assertNotNull(orders);
        }
        
        assertTrue(totalTime < 2000, "10 consecutive queries should complete in under 2 seconds total");
    }
}