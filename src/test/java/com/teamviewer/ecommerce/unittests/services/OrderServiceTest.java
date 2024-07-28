package com.teamviewer.ecommerce.unittests.services;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.entity.OrderEntity;
import com.teamviewer.ecommerce.mappers.OrderMapper;
import com.teamviewer.ecommerce.repositories.OrderRepository;
import com.teamviewer.ecommerce.services.impl.OrderServiceDefault;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.teamviewer.ecommerce.TestUtils.ORDER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    @MockBean
    private OrderMapper OrderMapper;

    @MockBean
    private OrderRepository OrderRepository;

    @Autowired
    private OrderServiceDefault OrderService;

    private Order OrderMock;
    private OrderEntity OrderEntityMock;

    @BeforeEach
    void setUp() {
        OrderMock = buildMockOrder();
        OrderEntityMock = buildMockOrderEntity();
    }

    @Test
    void testFindAllOrders() {
        List<OrderEntity> entities = new ArrayList<>();
        entities.add(OrderEntityMock);

        when(OrderRepository.findAll()).thenReturn(entities);
        when(OrderMapper.fromEntityToDomain(any(OrderEntity.class))).thenReturn(OrderMock);

        List<Order> Orders = OrderService.findAllOrders();

        assertNotNull(Orders);
        assertEquals(1, Orders.size());
        verify(OrderRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(OrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(OrderEntityMock));
        when(OrderMapper.fromEntityToDomain(any(OrderEntity.class))).thenReturn(OrderMock);

        Order foundOrder = OrderService.findById(ORDER_ID);

        assertNotNull(foundOrder);
        assertEquals(ORDER_ID, foundOrder.getId());
        verify(OrderRepository, times(1)).findById(ORDER_ID);
    }

    @Test
    void testFindById_NotFound() {
        when(OrderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        Order foundOrder = OrderService.findById(ORDER_ID);

        assertNull(foundOrder);
        verify(OrderRepository, times(1)).findById(ORDER_ID);
    }

    @Test
    void testCreateOrder() {
        when(OrderMapper.fromDomainToEntity(any(Order.class))).thenReturn(OrderEntityMock);
        when(OrderRepository.save(any(OrderEntity.class))).thenReturn(OrderEntityMock);
        when(OrderMapper.fromEntityToDomain(any(OrderEntity.class))).thenReturn(OrderMock);

        Order createdOrder = OrderService.createOrder(OrderMock);

        assertNotNull(createdOrder);
        assertEquals(ORDER_ID, createdOrder.getId());
        verify(OrderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrder() {
        when(OrderMapper.fromDomainToEntity(any(Order.class))).thenReturn(OrderEntityMock);
        when(OrderRepository.save(any(OrderEntity.class))).thenReturn(OrderEntityMock);
        when(OrderMapper.fromEntityToDomain(any(OrderEntity.class))).thenReturn(OrderMock);

        Order updatedOrder = OrderService.updateOrder(OrderMock);

        assertNotNull(updatedOrder);
        assertEquals(ORDER_ID, updatedOrder.getId());
        verify(OrderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testDeleteById() {
        when(OrderRepository.existsById(ORDER_ID)).thenReturn(true);

        boolean isDeleted = OrderService.deleteById(ORDER_ID);

        assertTrue(isDeleted);
        verify(OrderRepository, times(1)).deleteById(ORDER_ID);
    }

    @Test
    void testDeleteById_NotFound() {
        when(OrderRepository.existsById(ORDER_ID)).thenReturn(false);

        boolean isDeleted = OrderService.deleteById(ORDER_ID);

        assertFalse(isDeleted);
        verify(OrderRepository, never()).deleteById(ORDER_ID);
    }

    private static @NotNull OrderEntity buildMockOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(ORDER_ID);
        orderEntity.setName("Test Order");
        orderEntity.setTotalPriceAmount(100.0);
        orderEntity.setTotalQuantityItems(2);
        return orderEntity;
    }

    private static @NotNull Order buildMockOrder() {
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setName("Test Order");
        order.setTotalPriceAmount(100.0);
        order.setTotalQuantityItems(2);
        return order;
    }
}
