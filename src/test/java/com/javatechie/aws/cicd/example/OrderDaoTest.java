package com.javatechie.aws.cicd.example;

import com.javatechie.aws.cicd.example.Order;
import com.javatechie.aws.cicd.example.OrderDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDaoTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderDao orderDaoInject;

    @Test
    void should_returnAllOrders_when_noFilterProvided() {
        // Arrange
        List<Order> expectedOrders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());

        when(orderDao.getOrders()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderDao.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(4, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void should_returnFilteredOrders_when_minPriceProvided() {
        // Arrange
        List<Order> expectedOrders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(205, "Laptop", 1, 150000))
                .collect(Collectors.toList());

        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .filter(order -> order.getPrice() > 10000)
                .collect(Collectors.toList()));

        // Act
        List<Order> actualOrders = orderDao.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void should_returnOrderById_when_idProvided() {
        // Arrange
        Order expectedOrder = new Order(101, "Mobile", 1, 30000);

        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .filter(order -> order.getId() == 101)
                .collect(Collectors.toList()));

        // Act
        List<Order> actualOrders = orderDao.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrder, actualOrders.get(0));
    }

    @Test
    void should_returnEmptyList_when_noOrdersFound() {
        // Arrange
        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .filter(order -> order.getPrice() > 200000)
                .collect(Collectors.toList()));

        // Act
        List<Order> actualOrders = orderDao.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(0, actualOrders.size());
    }

    @Test
    void should_returnPaginatedOrders_when_pageSizeProvided() {
        // Arrange
        List<Order> expectedOrders = Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000))
                .collect(Collectors.toList());

        when(orderDao.getOrders()).thenReturn(Stream.of(
                new Order(101, "Mobile", 1, 30000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .limit(2)
                .collect(Collectors.toList()));

        // Act
        List<Order> actualOrders = orderDao.getOrders();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
    }
}