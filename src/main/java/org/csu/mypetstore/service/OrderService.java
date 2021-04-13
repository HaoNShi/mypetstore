package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Order;

import java.util.List;

public interface OrderService {

    void insertOrder(Order order);

    Order getOrder(int orderId);

    List<Order> getOrdersByUsername(String username);

    int getNextId(String name);
}
