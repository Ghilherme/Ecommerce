package com.teamviewer.ecommerce.unittests.services;

import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.domain.OrderItem;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.OrderEntity;
import com.teamviewer.ecommerce.entity.OrderItemEntity;
import com.teamviewer.ecommerce.entity.ProductEntity;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.OrderItemMapper;
import com.teamviewer.ecommerce.repositories.OrderItemRepository;
import com.teamviewer.ecommerce.services.OrderItemService;
import com.teamviewer.ecommerce.services.OrderService;
import com.teamviewer.ecommerce.services.ProductService;
import com.teamviewer.ecommerce.services.impl.OrderItemServiceDefault;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.teamviewer.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderItemServiceTest {
    @MockBean
    private OrderItemMapper orderItemMapper;

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @Autowired
    private OrderItemServiceDefault orderItemService;

    private OrderItem orderItemMock;
    private OrderItemEntity orderItemEntityMock;

    @BeforeEach
    void setUp() {
        orderItemMock = buildMockOrderItem();
        orderItemEntityMock = buildMockOrderItemEntity();
    }

    @Test
    void testFindAllOrderItems() {
        List<OrderItemEntity> entities = new ArrayList<>();
        entities.add(orderItemEntityMock);

        when(orderItemRepository.findAll()).thenReturn(entities);
        when(orderItemMapper.fromEntityToDomain(any(OrderItemEntity.class))).thenReturn(orderItemMock);

        List<OrderItem> orderItems = orderItemService.findAllOrderItems();

        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(orderItemRepository.findById(ORDER_ITEM_ID)).thenReturn(Optional.of(orderItemEntityMock));
        when(orderItemMapper.fromEntityToDomain(any(OrderItemEntity.class))).thenReturn(orderItemMock);

        OrderItem foundOrderItem = orderItemService.findById(ORDER_ITEM_ID);

        assertNotNull(foundOrderItem);
        assertEquals(ORDER_ITEM_ID, foundOrderItem.getId());
        verify(orderItemRepository, times(1)).findById(ORDER_ITEM_ID);
    }

    @Test
    void testFindById_NotFound() {
        when(orderItemRepository.findById(ORDER_ITEM_ID)).thenReturn(Optional.empty());

        OrderItem foundOrderItem = orderItemService.findById(ORDER_ITEM_ID);

        assertNull(foundOrderItem);
        verify(orderItemRepository, times(1)).findById(ORDER_ITEM_ID);
    }

    @Test
    void testCreateOrderItem() {
        when(orderItemMapper.fromDomainToEntity(any(OrderItem.class))).thenReturn(orderItemEntityMock);
        when(orderItemRepository.save(any(OrderItemEntity.class))).thenReturn(orderItemEntityMock);
        when(orderItemMapper.fromEntityToDomain(any(OrderItemEntity.class))).thenReturn(orderItemMock);
        when(productService.findById(anyString())).thenReturn(new Product(PRODUCT_ID, "", "",1.0));

        OrderItem createdOrderItem = orderItemService.createOrderItem(orderItemMock);

        assertNotNull(createdOrderItem);
        assertEquals(ORDER_ITEM_ID, createdOrderItem.getId());
        verify(orderItemRepository, times(1)).save(any(OrderItemEntity.class));
    }

    @Test
    void testUpdateOrderItem() {
        when(orderItemMapper.fromDomainToEntity(any(OrderItem.class))).thenReturn(orderItemEntityMock);
        when(orderItemRepository.save(any(OrderItemEntity.class))).thenReturn(orderItemEntityMock);
        when(orderItemMapper.fromEntityToDomain(any(OrderItemEntity.class))).thenReturn(orderItemMock);
        when(orderItemRepository.findById(anyString())).thenReturn(Optional.of(orderItemEntityMock));
        when(productService.findById(anyString())).thenReturn(new Product(PRODUCT_ID, "", "",1.0));

        OrderItem updatedOrderItem = orderItemService.updateOrderItem(orderItemMock);

        assertNotNull(updatedOrderItem);
        assertEquals(ORDER_ITEM_ID, updatedOrderItem.getId());
        verify(orderItemRepository, times(1)).save(any(OrderItemEntity.class));
    }

    @Test
    void testUpdateOrderItem_InvalidDataException() {
        OrderItem invalidOrderItem = buildMockOrderItem();
        invalidOrderItem.setOrder(new Order("different_order_id", "Order #2", 150.0, 3));

        when(orderItemRepository.findById(ORDER_ITEM_ID)).thenReturn(Optional.of(orderItemEntityMock));
        when(orderItemRepository.findById(anyString())).thenReturn(Optional.of(orderItemEntityMock));
        when(orderItemMapper.fromEntityToDomain(any(OrderItemEntity.class))).thenReturn(orderItemMock);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> orderItemService.updateOrderItem(invalidOrderItem));
        assertEquals("Order ID cannot be updated.", exception.getMessage());
    }

    @Test
    void testDeleteById() {
        when(orderItemRepository.existsById(ORDER_ITEM_ID)).thenReturn(true);

        boolean isDeleted = orderItemService.deleteById(ORDER_ITEM_ID);

        assertTrue(isDeleted);
        verify(orderItemRepository, times(1)).deleteById(ORDER_ITEM_ID);
    }

    @Test
    void testDeleteById_NotFound() {
        when(orderItemRepository.existsById(ORDER_ITEM_ID)).thenReturn(false);

        boolean isDeleted = orderItemService.deleteById(ORDER_ITEM_ID);

        assertFalse(isDeleted);
        verify(orderItemRepository, never()).deleteById(ORDER_ITEM_ID);
    }

    private static @NotNull OrderItemEntity buildMockOrderItemEntity() {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setId(ORDER_ITEM_ID);
        orderItemEntity.setOrder(new OrderEntity());
        orderItemEntity.setProduct(new ProductEntity());
        orderItemEntity.setQuantity(2);
        orderItemEntity.setFinalPrice(100.0);
        return orderItemEntity;
    }

    private static @NotNull OrderItem buildMockOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(ORDER_ITEM_ID);
        orderItem.setOrder(new Order(ORDER_ID, "Order #1", 100.0, 2));
        orderItem.setProduct(new Product(PRODUCT_ID, "Product 1", "Nice product", 100.0));
        orderItem.setQuantity(2);
        orderItem.setFinalPrice(100.0);
        return orderItem;
    }
}
