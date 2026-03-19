package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = new RowMapper<Order>() {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setName(rs.getString("name"));
            order.setCategory(rs.getString("category"));
            order.setColor(rs.getString("color"));
            order.setPrice(rs.getDouble("price"));
            return order;
        }
    };

    public List<Order> getOrders() {
        return jdbcTemplate.query("SELECT * FROM ORDERS", ORDER_ROW_MAPPER);
    }

    public List<Order> getOrders(Double minPrice, Integer page, Integer size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ORDERS");
        
        if (minPrice != null) {
            sql.append(" WHERE price >= ?");
        }
        
        sql.append(" ORDER BY id");
        
        if (page != null && size != null) {
            sql.append(" LIMIT ? OFFSET ?");
        }
        
        if (minPrice != null && page != null && size != null) {
            return jdbcTemplate.query(sql.toString(), ORDER_ROW_MAPPER, minPrice, size, page * size);
        } else if (minPrice != null) {
            return jdbcTemplate.query(sql.toString(), ORDER_ROW_MAPPER, minPrice);
        } else if (page != null && size != null) {
            return jdbcTemplate.query(sql.toString(), ORDER_ROW_MAPPER, size, page * size);
        } else {
            return getOrders();
        }
    }

    public Optional<Order> findById(int id) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM ORDERS WHERE id = ?", ORDER_ROW_MAPPER, id);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    public Order addOrder(Order order) {
        jdbcTemplate.update("INSERT INTO ORDERS (name, category, color, price) VALUES (?, ?, ?, ?)",
                order.getName(), order.getCategory(), order.getColor(), order.getPrice());
        return order;
    }
}