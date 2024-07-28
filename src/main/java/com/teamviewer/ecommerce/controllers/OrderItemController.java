package com.teamviewer.ecommerce.controllers;

import com.ecommerce.model.OrderItemApi;
import com.ecommerce.model.OrderItemApiRequest;
import com.ecommerce.model.OrderItemApiResponse;

import com.teamviewer.ecommerce.domain.OrderItem;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.OrderItemMapper;
import com.teamviewer.ecommerce.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @GetMapping("/order-items")
    public ResponseEntity<OrderItemApiResponse> findAll() {
        List<OrderItemApi> orders = orderItemService.findAllOrderItems()
                .stream()
                .map(orderItemMapper::fromDomainToApi)
                .toList();
        OrderItemApiResponse response = new OrderItemApiResponse();
        response.setOrderItems(orders);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/order-items/{id}")
    public ResponseEntity<OrderItemApi> findById(@PathVariable String id) {
        OrderItem order = orderItemService.findById(id);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        OrderItemApi response = orderItemMapper.fromDomainToApi(order);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/order-items")
    public ResponseEntity<OrderItemApi> createOrderItem(@RequestBody OrderItemApiRequest orderItemApi) {
        if (orderItemApi.getQuantity() == null || orderItemApi.getProductId() == null) {
            throw new InvalidDataException("Order Item Id and Quantity cannot be null");
        }

        orderItemApi.setId(null);
        OrderItem created = orderItemService.createOrderItem(orderItemMapper.fromApiToDomain(orderItemApi));
        OrderItemApi response = orderItemMapper.fromDomainToApi(created);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/orders/{id}")
                        .buildAndExpand(response.getId())
                        .toUri())
                .body(response);
    }

    @PutMapping("/order-items/{id}")
    public ResponseEntity<OrderItemApi> updateOrderItem(@RequestBody OrderItemApiRequest orderItemApi, @PathVariable String id) {
        if (orderItemApi.getId() != null) {
            throw new InvalidDataException("Order Item Id cannot be updated.");
        }

        orderItemApi.setId(id);
        OrderItem created = orderItemService.updateOrderItem(orderItemMapper.fromApiToDomain(orderItemApi));

        OrderItemApi response = orderItemMapper.fromDomainToApi(created);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/order-items/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        boolean isDeleted = orderItemService.deleteById(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
