package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.domain.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> findAllOrderItems();

    OrderItem findById(String id);

    OrderItem createOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(OrderItem orderItem);

    boolean deleteById(String id);
}
