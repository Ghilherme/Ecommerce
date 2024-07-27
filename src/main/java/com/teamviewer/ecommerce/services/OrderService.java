package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();

    Order findById(String id);

    Order createOrder(Order product);

    Order updateOrder(Order product);

    boolean deleteById(String id);
}
