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
class OrderDaoTest {

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
        
        // Insert test data
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                "Laptop", "Electronics", "Black", 999.99);
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                "Phone", "Electronics", "White", 599.99);
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                "Tablet", "Electronics", "Silver", 399.99);
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                "Book", "Education", "Blue", 29.99);
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                "Headphones", "Electronics", "Black", 199.99);
    }

    @Test
    void testGetOrders() {
        List<Order> orders = orderDao.getOrders();
        
        assertNotNull(orders);
        assertEquals(5, orders.size());
        assertEquals("Laptop", orders.get(0).getName());
        assertEquals(999.99, orders.get(0).getPrice());
    }

    @Test
    void testGetOrdersWithMinPrice() {
        List<Order> orders = orderDao.getOrders(500.0, null, null);
        
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 500.0));
    }

    @Test
    void testGetOrdersWithPagination() {
        List<Order> orders = orderDao.getOrders(null, 1, 2);
        
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("Tablet", orders.get(0).getName());
        assertEquals("Book", orders.get(1).getName());
    }

    @Test
    void testGetOrdersWithMinPriceAndPagination() {
        List<Order> orders = orderDao.getOrders(100.0, 0, 3);
        
        assertNotNull(orders);
        assertEquals(3, orders.size());
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 100.0));
    }

    @Test
    void testGetOrdersWithHighMinPrice() {
        List<Order> orders = orderDao.getOrders(2000.0, null, null);
        
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testGetOrdersWithLargePage() {
        List<Order> orders = orderDao.getOrders(null, 10, 10);
        
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testFindById() {
        Optional<Order> order = orderDao.findById(1);
        
        assertTrue(order.isPresent());
        assertEquals("Laptop", order.get().getName());
        assertEquals(999.99, order.get().getPrice());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Order> order = orderDao.findById(999);
        
        assertFalse(order.isPresent());
    }

    @Test
    void testAddOrder() {
        Order newOrder = new Order();
        newOrder.setName("Mouse");
        newOrder.setCategory("Electronics");
        newOrder.setColor("Red");
        newOrder.setPrice(49.99);
        
        Order addedOrder = orderDao.addOrder(newOrder);
        
        assertNotNull(addedOrder);
        assertEquals("Mouse", addedOrder.getName());
        
        List<Order> allOrders = orderDao.getOrders();
        assertEquals(6, allOrders.size());
    }

    @Test
    void testGetOrdersWithZeroMinPrice() {
        List<Order> orders = orderDao.getOrders(0.0, null, null);
        
        assertNotNull(orders);
        assertEquals(5, orders.size());
    }

    @Test
    void testGetOrdersWithPageZeroSizeOne() {
        List<Order> orders = orderDao.getOrders(null, 0, 1);
        
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals("Laptop", orders.get(0).getName());
    }

    @Test
    void testGetOrdersWithExactMinPrice() {
        List<Order> orders = orderDao.getOrders(399.99, null, null);
        
        assertNotNull(orders);
        assertEquals(3, orders.size());
        assertTrue(orders.stream().allMatch(order -> order.getPrice() >= 399.99));
    }
}