package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDaoTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void should_returnAllOrders_when_noFilterProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        List<Order> orders = orderService.getOrders(null, null, null);

        // Assert
        assertEquals(4, orders.size());
        assertEquals(101, orders.get(0).getId());
        assertEquals(58, orders.get(1).getId());
        assertEquals(205, orders.get(2).getId());
        assertEquals(809, orders.get(3).getId());
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        List<Order> orders = orderService.getOrders(10000, null, null);

        // Assert
        assertEquals(3, orders.size());
        assertEquals(101, orders.get(0).getId());
        assertEquals(205, orders.get(1).getId());
        assertEquals(809, orders.get(2).getId());
    }

    @Test
    void should_returnPaginatedOrders_when_pageSizeProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        List<Order> orders = orderService.getOrders(null, 2, null);

        // Assert
        assertEquals(2, orders.size());
        assertEquals(101, orders.get(0).getId());
        assertEquals(58, orders.get(1).getId());
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        Order order = orderService.getOrderById(101);

        // Assert
        assertEquals(101, order.getId());
        assertEquals("Mobile", order.getName());
        assertEquals(1, order.getQuantity());
        assertEquals(30000, order.getPrice());
    }

    @Test
    void should_returnEmptyList_when_noOrdersFound() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        List<Order> orders = orderService.getOrders(1000000, null, null);

        // Assert
        assertEquals(0, orders.size());
    }

    @Test
    void should_returnNull_when_orderIdNotFound() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList()));

        // Act
        Order order = orderService.getOrderById(1000);

        // Assert
        assertEquals(null, order);
    }
}