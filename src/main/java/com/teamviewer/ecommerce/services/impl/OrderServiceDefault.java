package com.teamviewer.ecommerce.services.impl;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.entity.OrderEntity;
import com.teamviewer.ecommerce.mappers.OrderMapper;
import com.teamviewer.ecommerce.repositories.OrderRepository;
import com.teamviewer.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceDefault implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<Order> findAllOrders() {
        List<Order> products = new ArrayList<>();

        orderRepository.findAll().forEach(product ->
                products.add(orderMapper.fromEntityToDomain(product))
        );

        return products;
    }

    @Override
    public Order findById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::fromEntityToDomain)
                .orElse(null);
    }

    @Override
    public Order createOrder(Order order) {
        return saveOrder(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return saveOrder(order);
    }

    @Override
    public boolean deleteById(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public String generateOrderName() {
        long orderCount = orderRepository.count();
        return "Order #" + (orderCount + 1);
    }

    private Order saveOrder(Order order) {
        OrderEntity entity = orderMapper.fromDomainToEntity(order);
        orderRepository.save(entity);

        return orderMapper.fromEntityToDomain(entity);
    }
}
