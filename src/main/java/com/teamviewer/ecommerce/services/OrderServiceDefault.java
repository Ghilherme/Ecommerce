package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.entity.OrderEntity;
import com.teamviewer.ecommerce.mappers.OrderMapper;
import com.teamviewer.ecommerce.repositories.OrderRepository;
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
        return false;
    }

    private Order saveOrder(Order order) {
        OrderEntity entity = orderMapper.fromDomainToEntity(order);
        orderRepository.save(entity);

        return orderMapper.fromEntityToDomain(entity);
    }
}
