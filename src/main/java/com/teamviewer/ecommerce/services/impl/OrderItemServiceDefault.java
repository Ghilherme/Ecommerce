package com.teamviewer.ecommerce.services.impl;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.domain.OrderItem;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.OrderItemEntity;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.exceptions.NoExistenceException;
import com.teamviewer.ecommerce.mappers.OrderItemMapper;
import com.teamviewer.ecommerce.repositories.OrderItemRepository;
import com.teamviewer.ecommerce.services.OrderItemService;
import com.teamviewer.ecommerce.services.OrderService;
import com.teamviewer.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceDefault implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderService orderService;
    private final ProductService productService;

    @Override
    public List<OrderItem> findAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();

        orderItemRepository.findAll().forEach(orderItem ->
                orderItems.add(orderItemMapper.fromEntityToDomain(orderItem))
        );
        return orderItems;
    }

    @Override
    public OrderItem findById(String id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::fromEntityToDomain)
                .orElse(null);
    }

    @Override
    @Transactional
    public OrderItem createOrderItem(OrderItem orderItem) {
        return saveOrderItem(orderItem);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItem(OrderItem orderItem) {
        validateOrderItemIsUpdatable(orderItem);

        return saveOrderItem(orderItem);
    }

    @Override
    public boolean deleteById(String id) {
        if (orderItemRepository.existsById(id)) {
            orderItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Verify rules for updating an order item
     * @param orderItem {@link OrderItem} validated.
     */
    private void validateOrderItemIsUpdatable(OrderItem orderItem) {
        OrderItem order = findById(orderItem.getId());
        if (order == null) {
            throw new NoExistenceException("Order item " + orderItem.getId());
        }

        if (!Objects.equals(order.getOrder().getId(), orderItem.getOrder().getId())) {
            throw new InvalidDataException("Order ID cannot be updated.");
        }

        if (!Objects.equals(order.getProduct().getId(), orderItem.getProduct().getId())) {
            throw new InvalidDataException("Product ID cannot be updated.");
        }
    }

    /**
     * Save {@link OrderItem} on database.
     * @param orderItem {@link OrderItem} to persist.
     * @return {@link OrderItem} saved.
     */
    private OrderItem saveOrderItem(OrderItem orderItem) {
        validateOrderAndProductExistence(orderItem);

        applyFinalPriceForOrderItem(orderItem);
        processOrder(orderItem);

        OrderItemEntity entity = orderItemMapper.fromDomainToEntity(orderItem);
        orderItemRepository.save(entity);

        return orderItemMapper.fromEntityToDomain(entity);
    }

    /**
     * Formula for applying final price for an orderItem
     * @param orderItem {@link OrderItem} with the final price updated.
     */
    private static void applyFinalPriceForOrderItem(OrderItem orderItem) {
        orderItem.setFinalPrice((orderItem.getProduct().getPrice() * orderItem.getQuantity()) - orderItem.getDiscount());
    }

    /**
     * Persists Order price and quantity based on {@link OrderItem} provided.
     * @param orderItem {@link OrderItem} to update order from.
     */
    private void processOrder(OrderItem orderItem) {
        Order order = orderItem.getOrder();

        if (order.getId() == null) {
            order.setName(orderService.generateOrderName());
            order.setTotalQuantityItems(orderItem.getQuantity());
            order.setTotalPriceAmount(orderItem.getFinalPrice());
        }
        else {
            order.setTotalQuantityItems(order.getTotalQuantityItems() + orderItem.getQuantity());
            order.setTotalPriceAmount(order.getTotalPriceAmount() + orderItem.getFinalPrice());
        }

        orderItem.setOrder(orderService.createOrder(order));
    }

    /**
     * Validates if order and product exists. Product must exist, if not will throw an exception.
     * If Order doesn't exist, it will create a new one.
     * @param orderItem orderItem {@link OrderItem} to validate
     */
    private void validateOrderAndProductExistence(OrderItem orderItem) {
        Order order = validateAndGetOrder(orderItem.getOrder());
        Product product = validateAndGetProduct(orderItem.getProduct());

        orderItem.setOrder(order);
        orderItem.setProduct(product);
    }

    /**
     * Validate Order and if it doesn't exist, it will create a new one.
     * @param order {@link Order} to validate
     */
    private Order validateAndGetOrder(Order order) {
        return Optional.ofNullable(order)
                .map(Order::getId)
                .map(orderService::findById)
                .orElseGet(Order::new);
    }

    /**
     * Validate Product and if it doesn't exist, it will throw an error.
     * @param product {@link Product} to validate
     */
    private Product validateAndGetProduct(Product product) {
        return Optional.ofNullable(productService.findById(product.getId()))
                .orElseThrow(() -> new NoExistenceException("Product ID " + product.getId()));
    }

}
