package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable int id) {
        // NEW
        return orderDao.getOrderById(id);
    }

    @GetMapping("/orders")
    public List<Order> getOrders(@RequestParam(required = false) Double minPrice,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int size) {
        // NEW
        if (minPrice != null) {
            return orderDao.getOrdersByMinPrice(minPrice, page, size);
        } else {
            return orderDao.getOrders(page, size);
        }
    }
}